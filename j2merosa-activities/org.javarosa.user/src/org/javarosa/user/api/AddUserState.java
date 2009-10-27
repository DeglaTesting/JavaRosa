/**
 * 
 */
package org.javarosa.user.api;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;

import org.javarosa.core.api.State;
import org.javarosa.core.services.locale.Localization;
import org.javarosa.core.services.storage.IStorageUtility;
import org.javarosa.core.services.storage.StorageFullException;
import org.javarosa.core.services.storage.StorageManager;
import org.javarosa.j2me.view.J2MEDisplay;
import org.javarosa.user.api.transitions.AddUserStateTransitions;
import org.javarosa.user.model.User;
import org.javarosa.user.utility.IUserDecorator;
import org.javarosa.user.utility.UserValidator;
import org.javarosa.user.view.UserForm;

/**
 * @author ctsims
 *
 */
public class AddUserState implements State<AddUserStateTransitions>, CommandListener {

	private AddUserStateTransitions transitions;
	private UserForm view;
	
	public final static String PASSWORD_FORMAT_ALPHA_NUMERIC = "a";
	public final static String PASSWORD_FORMAT_NUMERIC = "n";
	
	public final static Command CMD_SAVE = new Command(Localization.get("menu.Save"),
			Command.OK, 2);
	public final static Command CMD_CANCEL = new Command(Localization.get("menu.Exit"),
			Command.EXIT, 2);

	
	public AddUserState(String passwordFormat) {
		this(passwordFormat, null);
	}
	public AddUserState(String passwordFormat, IUserDecorator decorator) {
		this.view = new UserForm(Localization.get("activity.adduser.adduser"), decorator);
		this.view.setPasswordMode(passwordFormat);
		this.view.addCommand(CMD_SAVE);
		this.view.addCommand(CMD_CANCEL);
		this.view.setCommandListener(this);

	}
	
	public void enter(AddUserStateTransitions transitions) {
		 this.transitions = transitions;
	}

	public void start() {
		J2MEDisplay.getDisplay().setCurrent(view);
	}
	
	public void commandAction(Command c, Displayable arg1) {
		if(c.equals(CMD_SAVE)) {
			UserValidator validator = new UserValidator(view);
			int status = validator.validateNewUser();
			if(status == UserValidator.OK) {
				User u = validator.constructUser();
				//Save to RMS
				IStorageUtility users = StorageManager.getStorage(User.STORAGE_KEY);
				try {
					users.write(u);
				} catch (StorageFullException e) {
					throw new RuntimeException("uh-oh, storage full [users]"); //TODO: handle this
				}

				transitions.userAdded(u);
			} else {
				validator.handleInvalidUser(status, this);
			}
		} else if(c.equals(CMD_CANCEL)) {
			transitions.cancel();
		}
	}
}
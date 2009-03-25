package org.javarosa.formmanager.view.clforms.acquire;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;

import org.javarosa.core.model.data.IAnswerData;
import org.javarosa.formmanager.view.FormElementBinding;
import org.javarosa.formmanager.view.clforms.SingleQuestionScreen;

/**
 * @author mel An extended SingleQuestionScreen that acquires the answer data
 *         for the question by capturing (and possibly processing) some data
 * 
 */
public abstract class AcquiringQuestionScreen extends SingleQuestionScreen {
	protected IAnswerData acquiredData;
	public Command acquireCommand;
	AcquireScreen acquireScreen;

	public AcquiringQuestionScreen(FormElementBinding prompt) {

		super(prompt);
		addAcquireCommand();

	}

	public AcquiringQuestionScreen(FormElementBinding prompt, int temp) {

		super(prompt, temp);
		addAcquireCommand();

	}

	public AcquiringQuestionScreen(FormElementBinding prompt, String str) {

		super(prompt, str);
		addAcquireCommand();
	}

	public AcquiringQuestionScreen(FormElementBinding prompt, char str) {
		super(prompt, str);
		addAcquireCommand();

	}

	/**
	 * @return the command that is used to stop capturing and attempt to
	 *         transform the captured data into answer data
	 */
	public abstract Command getAcquireCommand();

	public void addAcquireCommand() {
		this.acquireCommand = getAcquireCommand();
		this.addCommand(acquireCommand);
	}

	protected void setAcquiredData(IAnswerData acquiredData) {
		this.acquiredData = acquiredData;
		updateDisplay();
	}

	/**
	 * Update the question screen with a representation of the acquired data
	 */
	protected abstract void updateDisplay();

	/**
	 * @param callingListener
	 *            the listener to which control will be returned
	 * @return the screen that will do the acquiring
	 */
	public abstract AcquireScreen getAcquireScreen(
			CommandListener callingListener);

}

package org.javarosa.formmanager.view;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Spacer;
import javax.microedition.lcdui.StringItem;

import org.javarosa.core.services.transport.TransportMessage;
import java.util.Enumeration;
import org.javarosa.core.JavaRosaServiceProvider;

public class SubmitStatusScreen extends Form  {
    private StringItem msg;
    private Command okCommand;
    int counter = 0;

    public static final int REFRESH_INTERVAL = 1000;
    public static final int TIMEOUT = 60000;

	public SubmitStatusScreen () {
    	//#style submitPopup
		super("Send Status");

		okCommand = new Command("OK", Command.OK, 1);
		msg = new StringItem(null, "Sending...");

		addCommand(okCommand);
		append(new Spacer(80, 0));
		append(msg);
	}

	public String getServerResponse()  {
		Enumeration messages = JavaRosaServiceProvider.instance().getTransportManager().getMessages();
 		        String receipt ="";
 		    //while (messages.hasMoreElements())
 		    //{
 		    TransportMessage response = (TransportMessage) messages.nextElement();
 		     receipt = new String(response.getReplyloadData());
 		    //}
 		    return receipt;
 		    }

	public void updateStatus (int status) {

		String message;
		String s = getServerResponse();

		switch (status) {
		case TransportMessage.STATUS_NEW:       message = (counter < TIMEOUT ? "Sending..."
				: "Sending is taking a long time; you may check on the status and/or resend later in 'View Saved'"); break;
		case TransportMessage.STATUS_DELIVERED: message = ("Form has been submitted successfully! Your reference is:"+s); break;
		case TransportMessage.STATUS_FAILED:    message = "Submission failed! Please try to submit the form again later in 'View Saved'."; break;
		default:                                message = "Unknown sending error; form not sent!"; break;
		}

		msg.setText(message);
	}
}

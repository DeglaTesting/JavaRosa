package org.javarosa.clforms.midlet;

import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import org.javarosa.clforms.shell.JavaRosaCLFormsShell;
import org.javarosa.core.JavaRosaServiceProvider;
import org.javarosa.core.api.IShell;
import org.javarosa.j2me.view.J2MEDisplay;

/**
 * This is the starting point for the JavarosaDemo application
 * @author Brian DeRenzi
 *
 */
public class JavaRosaCLFormsMidlet extends MIDlet {
	IShell shell = null;


	protected void destroyApp(boolean arg0) throws MIDletStateChangeException {
		// TODO Auto-generated method stub

	}

	protected void pauseApp() {
		// TODO Auto-generated method stub

	}

	protected void startApp() throws MIDletStateChangeException {
		/*
		 * Duplicate this class and change the following line to
		 * create a custom midlet to launch from
		 */
		shell = new JavaRosaCLFormsShell();

		// Do NOT edit below
		JavaRosaServiceProvider.instance().initialize();
		JavaRosaServiceProvider.instance().setDisplay(new J2MEDisplay(Display.getDisplay(this)));
		((JavaRosaCLFormsShell)shell).setMIDlet(this);
		shell.run();
	}

}
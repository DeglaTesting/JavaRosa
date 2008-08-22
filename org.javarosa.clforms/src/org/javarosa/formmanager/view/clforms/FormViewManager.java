package org.javarosa.formmanager.view.clforms;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Gauge;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemCommandListener;
import javax.microedition.lcdui.StringItem;

import org.javarosa.core.model.Constants;
import org.javarosa.core.model.QuestionDef;
import org.javarosa.core.model.data.IAnswerData;
import org.javarosa.formmanager.activity.FormEntryContext;
import org.javarosa.formmanager.controller.FormEntryController;
import org.javarosa.formmanager.model.FormEntryModel;
import org.javarosa.formmanager.utility.FormEntryModelListener;
import org.javarosa.formmanager.view.IFormEntryView;
import org.javarosa.formmanager.view.clforms.widgets.DateQuestionWidget;
import org.javarosa.formmanager.view.clforms.widgets.NumericQuestionWidget;
import org.javarosa.formmanager.view.clforms.widgets.Select1QuestionWidget;
import org.javarosa.formmanager.view.clforms.widgets.SelectQuestionWidget;
import org.javarosa.formmanager.view.clforms.widgets.TextQuestionWidget;
import org.javarosa.formmanager.view.clforms.widgets.TimeQuestionWidget;

public class FormViewManager extends Form implements IFormEntryView, FormEntryModelListener, CommandListener, ItemCommandListener
{
	private FormEntryController controller;
	private FormEntryModel model;
	private FormViewScreen parent;
	
	private boolean multiLingual;
	private int index;
	private QuestionDef prompt;
	private IAnswerData answer;
	private SingleQuestionScreen widget;
	// GUI elements

	private static Command previousCommand;
	private static Command nextCommand;
	private static Command goToListCommand;

	//widget commands
	private Command backItemCommand = new Command("back Item Command", Command.ITEM, 1);
	private Command nextItemCommand = new Command("next", Command.ITEM, 1);

/*	public FormViewManager()
	{
		//constructor
	}*/
	
	public FormViewManager(String formTitle, FormEntryModel model, FormEntryController controller, int questionIndex, FormViewScreen node)
	{
		super(formTitle);

    	this.parent = node;
		this.model = model;
    	this.controller = controller;
		//setup commands only once
		setUpCommands();
		//immediately setup question, need to decide if this is the best place to do it
    	this.getView(questionIndex);
    	//controller.setView(this);

    	multiLingual = (model.getForm().getLocalizer() != null);
    	model.registerObservable(this);
	}
	public int getIndex()
	{
		index = model.getQuestionIndex();//return index of active question
		return index;
	}
	public void getView(int qIndex)
	{	
		
		prompt = model.getQuestion(qIndex);
		//checks question type
		int qType = prompt.getDataType();
	
		//obtains correct view
		switch (qType)
		{
		case Constants.DATATYPE_DATE:
			//go to DateQuestion Widget
			widget = new DateQuestionWidget(prompt.getName());
			this.append(widget.initWidget(prompt));
			break;
		case Constants.DATATYPE_LIST_MULTIPLE:
			//go to SelectQuestion Widget
			widget = new SelectQuestionWidget(prompt.getName());
			this.append(widget.initWidget(prompt));
			break;
		case Constants.DATATYPE_LIST_EXCLUSIVE:
			//go to Select1Question Widget
			widget = new Select1QuestionWidget(prompt.getName());
			this.append(widget.initWidget(prompt));
			break;
		case Constants.DATATYPE_TEXT:
			//go to TextQuestion Widget
			widget = new TextQuestionWidget(prompt.getName());
			this.append(widget.initWidget(prompt));
			break;
		case Constants.DATATYPE_TIME:
			//go to TimeQuestion Widget
			widget = new TimeQuestionWidget(prompt.getName());
			this.append(widget.initWidget(prompt));
			break;
		case Constants.DATATYPE_INTEGER:
			widget = new NumericQuestionWidget(prompt.getName());
			this.append(widget.initWidget(prompt));
			break;
		default:
			System.out.println("Unsupported type!");
			break;
		}
		//add widget item commands...
		addNavigationButtons();
	}
	

	public void destroy() {
		model.unregisterObservable(this);
		
	}


	public void setContext(FormEntryContext context) {
		// TODO Auto-generated method stub
		
	}


	public void show() {
		// TODO Auto-generated method stub
		controller.setDisplay(this);
	}

	public void refreshView()
	{
		this.deleteAll();
		int nextQIndex = getIndex();
		getView(nextQIndex);//refresh view
	}

	public void formComplete() {
		try {
			Thread.sleep(1000); 
		} catch (InterruptedException ie) { }

		controller.save();//always save form
		controller.exit();
		
	}


	public void questionIndexChanged(int questionIndex) {
		// TODO Auto-generated method stub
		
	}


	public void saveStateChanged(int instanceID, boolean dirty) {
		// TODO Auto-generated method stub
		
	}

	private void setUpCommands() 
	{	System.out.println("setting up comands");
		previousCommand = new Command("back", Command.SCREEN, 2); 
		nextCommand = new Command("next", Command.SCREEN, 1); 
		goToListCommand = new Command("View Answers", Command.SCREEN, 1);
	
		this.addCommand(previousCommand);
		//this.addCommand(nextCommand);//disable command, handled by item command
		this.addCommand(goToListCommand);
	
		this.setCommandListener(this);
		System.out.println("command listener set");

	}

	public void commandAction(Command command, Displayable arg1) 
	{
		if (command == nextCommand) {
				answer=widget.getWidgetValue();
				//System.out.println("you answered "+ answer.getDisplayText()+" for "+prompt.getLongText()+" moving on");
				controller.questionAnswered(this.prompt, answer);//store answers
				refreshView();
				}
			
		else if (command == previousCommand) {
			controller.stepQuestion(false);
			refreshView();
			//parent.show();
		}
		else if (command == goToListCommand){
			controller.save();//always save
			//controller.exit();
			 parent.show();
		}
	}
	
    public void commandAction(Command c, Item item)
    {
    	if(c == nextItemCommand)
      {
			answer=widget.getWidgetValue();
			//System.out.println("you answered "+ answer.getDisplayText()+" for "+prompt.getLongText()+" moving on");
			controller.questionAnswered(this.prompt, answer);//store answers
			refreshView();

      }else if (c == backItemCommand) {
			controller.stepQuestion(false);
		}


    }

	public void addNavigationButtons() 
	{
		StringItem backItem = new StringItem(null,"BACK",Item.BUTTON);
		StringItem nextItem = new StringItem(null,"NEXT",Item.BUTTON);

		this.append(nextItem);
	    //this.append(backItem);//disable, handled by previousCommand

	    backItem.setDefaultCommand(backItemCommand);     // add Command to Item.
	    backItem.setItemCommandListener(this);       // set item command listener
	    nextItem.setDefaultCommand(nextItemCommand);     // add Command to Item.
	    nextItem.setItemCommandListener(this);       // set item command listener

	}
}
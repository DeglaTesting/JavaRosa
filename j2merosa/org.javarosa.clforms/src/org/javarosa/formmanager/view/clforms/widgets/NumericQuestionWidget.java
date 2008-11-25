package org.javarosa.formmanager.view.clforms.widgets;

import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.TextField;

import org.javarosa.core.model.QuestionDef;
import org.javarosa.core.model.data.IAnswerData;
import org.javarosa.core.model.data.IntegerData;
import org.javarosa.formmanager.view.clforms.SingleQuestionScreen;

public class NumericQuestionWidget extends SingleQuestionScreen
{
	protected TextField tf;

	public NumericQuestionWidget(QuestionDef question){
		super(question);

	}
	
	public NumericQuestionWidget(QuestionDef prompt, int num) {
		super (prompt,num);
	}
	public NumericQuestionWidget(QuestionDef prompt, String str) {
		super (prompt,str);
	}
	public NumericQuestionWidget(QuestionDef prompt, char c) {
		super (prompt,c);
	}
	

	public void creatView() {
		//#style textBox
		 tf = new TextField("", "", 200, TextField.NUMERIC);	
		 if(qDef.isRequired())
				tf.setLabel("*"+qDef.getLongText()); //visual symbol for required
				else
					tf.setLabel(qDef.getLongText());
		this.append(tf);
		this.addNavigationButtons();
		if (qDef.getHelpText()!=null){
			setHint(qDef.getHelpText());
		}
	}

	public IAnswerData getWidgetValue () {
		String s = tf.getString();
		//if empty
		if (s == null || s.equals(""))
			return null;

		//check answer integrity
		int i = -99999;
		try {
			i = Integer.parseInt(s);
		} catch (NumberFormatException nfe) {
			System.err.println("Non-numeric data in numeric entry field!");
		}
		return new IntegerData(i);
	}

}
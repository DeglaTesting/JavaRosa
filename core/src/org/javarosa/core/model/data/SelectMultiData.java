/*
 * Copyright (C) 2009 JavaRosa
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.javarosa.core.model.data;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.ArrayList;
import java.util.List;

import org.javarosa.core.model.data.helper.Selection;
import org.javarosa.core.model.utils.DateUtils;
import org.javarosa.core.util.externalizable.DeserializationException;
import org.javarosa.core.util.externalizable.ExtUtil;
import org.javarosa.core.util.externalizable.ExtWrapList;
import org.javarosa.core.util.externalizable.PrototypeFactory;

/**
 * A response to a question requesting a selection of
 * any number of items from a list.
 *
 * @author Drew Roos
 *
 */
public class SelectMultiData implements IAnswerData {
   List<Selection> vs; //vector of Selection

	/**
	 * Empty Constructor, necessary for dynamic construction during deserialization.
	 * Shouldn't be used otherwise.
	 */
	public SelectMultiData() {

	}

	public SelectMultiData (List<Selection> vs) {
		setValue(vs);
	}

    @Override
	public IAnswerData clone () {
       List<Selection> v = new ArrayList<Selection>(vs.size());
		for (int i = 0; i < vs.size(); i++) {
			v.add(vs.get(i).clone());
		}
		return new SelectMultiData(v);
	}

	/*
	 * (non-Javadoc)
	 * @see org.javarosa.core.model.data.IAnswerData#setValue(java.lang.Object)
	 */
    @Override
	public void setValue (Object o) {
		if(o == null) {
			throw new NullPointerException("Attempt to set an IAnswerData class to null.");
		}

		vs = vectorCopy((List<Selection>)o);
	}

	/*
	 * (non-Javadoc)
	 * @see org.javarosa.core.model.data.IAnswerData#getValue()
	 */
    @Override
	public Object getValue () {
		return vectorCopy(vs);
	}

	/**
	 * @return A type checked vector containing all of the elements
	 * contained in the vector input
	 * TODO: move to utility class
	 */
	private List<Selection> vectorCopy(List<Selection> input) {
      List<Selection> output = new ArrayList<Selection>(input.size());
		//validate type
		for (int i = 0; i < input.size(); i++) {
			Selection s = input.get(i);
			output.add(s);
		}
		return output;
	}
	/**
	 * @return THE XMLVALUE!!
	 */
	/*
	 * (non-Javadoc)
	 * @see org.javarosa.core.model.data.IAnswerData#getDisplayText()
	 */
    @Override
	public String getDisplayText () {
		StringBuilder b = new StringBuilder();

		for (int i = 0; i < vs.size(); i++) {
			Selection s = (Selection)vs.get(i);
			b.append(s.getValue());
			if (i < vs.size() - 1)
				b.append(", ");
		}

		return b.toString();
	}
	/* (non-Javadoc)
	 * @see org.javarosa.core.services.storage.utilities.Externalizable#readExternal(java.io.DataInputStream)
	 */
	@SuppressWarnings("unchecked")
    @Override
	public void readExternal(DataInputStream in, PrototypeFactory pf) throws IOException, DeserializationException {
		vs = (List<Selection>)ExtUtil.read(in, new ExtWrapList(Selection.class), pf);
	}

	/* (non-Javadoc)
	 * @see org.javarosa.core.services.storage.utilities.Externalizable#writeExternal(java.io.DataOutputStream)
	 */
    @Override
	public void writeExternal(DataOutputStream out) throws IOException {
		ExtUtil.write(out, new ExtWrapList(vs));
	}

    @Override
	public UncastData uncast() {
		StringBuilder selectString = new StringBuilder();

       for (Selection selection : vs) {
          if (selectString.length() > 0)
             selectString.append(" ");
          selectString.append(selection.getValue());
       }
		//As Crazy, and stupid, as it sounds, this is the XForms specification
		//for storing multiple selections.
		return new UncastData(selectString.toString());
	}

    @Override
	public SelectMultiData cast(UncastData data) throws IllegalArgumentException {

       List<String> choices = DateUtils.split(data.value, " ", true);
       List<Selection> v = new ArrayList<Selection>(choices.size());

		for(String s : choices) {
			v.add(new Selection(s));
		}
		return new SelectMultiData(v);
	}
}

/*
 * Copyright (C) 2014 JavaRosa
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
import java.util.ArrayList;

import org.javarosa.core.util.externalizable.DeserializationException;
import org.javarosa.core.util.externalizable.ExtUtil;
import org.javarosa.core.util.externalizable.PrototypeFactory;


/**
 * A response to a question requesting an GeoShape Value.
 * Consisting of a comma-separated ordered list of GeoPoint values.
 *
 * @author mitchellsundt@gmail.com
 *
 */
public class GeoShapeData implements IAnswerData {

	/**
	 * The data value contained in a GeoShapeData object is a GeoShape
	 *
	 * @author mitchellsundt@gmail.com
	 *
	 */
	public static class GeoShape {
		public ArrayList<double[]> points;

		public GeoShape() {
			points = new ArrayList<double[]>();
		}

		public GeoShape(ArrayList<double[]> points) {
			this.points = points;
		}
	};

	public final ArrayList<GeoPointData> points = new ArrayList<GeoPointData>();


    /**
     * Empty Constructor, necessary for dynamic construction during
     * deserialization. Shouldn't be used otherwise.
     */
    public GeoShapeData() {
    }

    /**
     * Copy constructor (deep)
     *
     * @param data
     */
    public GeoShapeData(GeoShapeData data) {
    	for ( GeoPointData p : data.points ) {
    		points.add(new GeoPointData(p));
    	}
    }

    public GeoShapeData(GeoShape ashape) {
    	for ( double[] da : ashape.points ) {
    		points.add(new GeoPointData(da));
    	}
    }

    public IAnswerData clone() {
        return new GeoShapeData(this);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.javarosa.core.model.data.IAnswerData#getDisplayText()
     */
    public String getDisplayText() {
    	StringBuilder b = new StringBuilder();
    	boolean first = true;
    	for ( GeoPointData p : points ) {
    		if ( !first ) {
    			b.append(", ");
    		}
    		first = false;
    		b.append(p.getDisplayText());
    	}
    	return b.toString();
    }


    /*
     * (non-Javadoc)
     *
     * @see org.javarosa.core.model.data.IAnswerData#getValue()
     */
    public Object getValue() {
    	ArrayList<double[]> pts = new ArrayList<double[]>();
    	for ( GeoPointData p : points ) {
    		pts.add((double[])p.getValue());
    	}
    	GeoShape gs = new GeoShape(pts);
        return gs;
    }

    public void setValue(Object o) {
        if (o == null) {
            throw new NullPointerException("Attempt to set an IAnswerData class to null.");
        }
        GeoShape gs = (GeoShape) o;
        ArrayList<GeoPointData> temp = new ArrayList<GeoPointData>();
        for ( double[] da : gs.points ) {
        	temp.add(new GeoPointData(da));
        }
        points.clear();
        points.addAll(temp);
    }


    public void readExternal(DataInputStream in, PrototypeFactory pf) throws IOException,
            DeserializationException {
    	points.clear();
        int len = (int) ExtUtil.readNumeric(in);
        for ( int i = 0 ; i < len ; ++i ) {
        	GeoPointData t = new GeoPointData();
        	t.readExternal(in, pf);
        	points.add(t);
        }
    }


    public void writeExternal(DataOutputStream out) throws IOException {
        ExtUtil.writeNumeric(out, points.size());
        for ( int i = 0 ; i < points.size() ; ++i ) {
        	GeoPointData t = points.get(i);
        	t.writeExternal(out);
        }
    }


	public UncastData uncast() {
		return new UncastData(getDisplayText());
	}

	public GeoShapeData cast(UncastData data) throws IllegalArgumentException {
		String[] parts = data.value.split(",");

		// silly...
		GeoPointData t = new GeoPointData();

		GeoShapeData d = new GeoShapeData();
		for ( String part : parts ) {
			// allow for arbitrary surrounding whitespace
			d.points.add(t.cast(new UncastData(part.trim())));
		}
		return d;
	}
}

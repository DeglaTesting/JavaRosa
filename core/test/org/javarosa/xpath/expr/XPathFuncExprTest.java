/*
/*
 * Copyright (C) 2009 JavaRosa
 *
 * Originally developed by Dobility, Inc. (as part of SurveyCTO)
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

package org.javarosa.xpath.expr;

import junit.framework.TestCase;

public class XPathFuncExprTest extends TestCase {

    public void testDeDuplicate() throws Exception {
        String functionName = "de-duplicate";

        assertEquals("value1,value2", XPathFuncExpr.deDuplicate(functionName, ",", "value1,value2,value1"));
        assertEquals("value1,value2", XPathFuncExpr.deDuplicate(functionName, ",", "value1,value2,value1,,,"));
        assertEquals("value1,value2", XPathFuncExpr.deDuplicate(functionName, ",", "value1,value2,,,value1,"));
        assertEquals("value1,value2", XPathFuncExpr.deDuplicate(functionName, ",", ",,,value1,value2,value1,"));

        assertEquals("value1 value2", XPathFuncExpr.deDuplicate(functionName, " ", " value1  value2 value1  "));
        assertEquals("value1  value2", XPathFuncExpr.deDuplicate(functionName, "  ", "value1  value2  value1  "));

        assertEquals("", XPathFuncExpr.deDuplicate(functionName, ",", ""));
        assertEquals(" ", XPathFuncExpr.deDuplicate(functionName, ",", " "));
        assertEquals("value1", XPathFuncExpr.deDuplicate(functionName, ",", "value1"));
        assertEquals(null, XPathFuncExpr.deDuplicate(functionName, ",", null));
    }

    public void testRankIndex() throws Exception {
        // test the normal case
        assertTrue(XPathFuncExpr.rankIndex(1, new Object[]{1.5, 5.2, 7.8, 2.2}) == 4);
        assertTrue(XPathFuncExpr.rankIndex(2, new Object[]{1.5, 5.2, 7.8, 2.2}) == 2);
        assertTrue(XPathFuncExpr.rankIndex(3, new Object[]{1.5, 5.2, 7.8, 2.2}) == 1);
        assertTrue(XPathFuncExpr.rankIndex(4, new Object[]{1.5, 5.2, 7.8, 2.2}) == 3);

        // test the index parameter
        assertTrue(XPathFuncExpr.rankIndex(1, new Object[]{"1", "5", "3", "2"}) == 4);
        assertTrue(XPathFuncExpr.rankIndex(1.0, new Object[]{"1", "5", "3", "2"}) == 4);
        assertTrue(XPathFuncExpr.rankIndex(1.9, new Object[]{"1", "5", "3", "2"}) == 4);        // the decimal part is ignored
        assertTrue(XPathFuncExpr.rankIndex("1.0", new Object[]{"1", "5", "3", "2"}) == 4);
        assertTrue(XPathFuncExpr.rankIndex("1.9", new Object[]{"1", "5", "3", "2"}) == 4);      // the decimal part is ignored
        assertTrue(XPathFuncExpr.rankIndex("1", new Object[]{"1", "5", "3", "2"}) == 4);
        assertTrue(XPathFuncExpr.rankIndex(null, new Object[]{"1", "5", "3", "2"}) == XPathFuncExpr.INVALID_RANK_RESULT);
        assertTrue(XPathFuncExpr.rankIndex("index", new Object[]{"1", "5", "3", "2"}) == XPathFuncExpr.INVALID_RANK_RESULT);

        // test out-of-bounds indexes
        assertTrue(XPathFuncExpr.rankIndex(0, new Object[]{"1", "5", "3", "2"}) == XPathFuncExpr.INVALID_RANK_RESULT);
        assertTrue(XPathFuncExpr.rankIndex(5, new Object[]{"1", "5", "3", "2"}) == XPathFuncExpr.INVALID_RANK_RESULT);
        assertTrue(XPathFuncExpr.rankIndex(-1, new Object[]{"1", "5", "3", "2"}) == XPathFuncExpr.INVALID_RANK_RESULT);
        assertTrue(XPathFuncExpr.rankIndex(null, new Object[]{"1", "5", "3", "2"}) == XPathFuncExpr.INVALID_RANK_RESULT);

        // test with no data
        assertTrue(XPathFuncExpr.rankIndex(1, null) == XPathFuncExpr.INVALID_RANK_RESULT);
        assertTrue(XPathFuncExpr.rankIndex(1, new Object[0]) == XPathFuncExpr.INVALID_RANK_RESULT);
        assertTrue(XPathFuncExpr.rankIndex(1, new Object[]{null}) == XPathFuncExpr.INVALID_RANK_RESULT);

        // test blank values with string data
        // blank values are treated as negative infinity
        assertTrue(XPathFuncExpr.rankIndex(1, new Object[]{"1", "5", null, "2"}) == 3);
        assertTrue(XPathFuncExpr.rankIndex(2, new Object[]{"1", "5", null, "2"}) == 1);
        assertTrue(XPathFuncExpr.rankIndex(3, new Object[]{"1", "5", null, "2"}) == XPathFuncExpr.INVALID_RANK_RESULT);
        assertTrue(XPathFuncExpr.rankIndex(4, new Object[]{"1", "5", null, "2"}) == 2);

        // test blank values with integer data
        // again, blank values are treated as negative infinity
        assertTrue(XPathFuncExpr.rankIndex(1, new Object[]{1, 5, null, 2}) == 3);
        assertTrue(XPathFuncExpr.rankIndex(2, new Object[]{1, 5, null, 2}) == 1);
        assertTrue(XPathFuncExpr.rankIndex(3, new Object[]{1, 5, null, 2}) == XPathFuncExpr.INVALID_RANK_RESULT);
        assertTrue(XPathFuncExpr.rankIndex(4, new Object[]{1, 5, null, 2}) == 2);

        // test with double data
        assertTrue(XPathFuncExpr.rankIndex(1, new Object[]{1d, 5d, null, 2d}) == 3);
        assertTrue(XPathFuncExpr.rankIndex(2, new Object[]{1d, 5d, null, 2d}) == 1);
        assertTrue(XPathFuncExpr.rankIndex(3, new Object[]{1d, 5d, null, 2d}) == XPathFuncExpr.INVALID_RANK_RESULT);
        assertTrue(XPathFuncExpr.rankIndex(4, new Object[]{1d, 5d, null, 2d}) == 2);

        // test with mixed data
        assertTrue(XPathFuncExpr.rankIndex(1, new Object[]{1d, "5", null, 2}) == 3);
        assertTrue(XPathFuncExpr.rankIndex(2, new Object[]{"1", 5, null, 2d}) == 1);
        assertTrue(XPathFuncExpr.rankIndex(3, new Object[]{1, 5d, null, "2"}) == XPathFuncExpr.INVALID_RANK_RESULT);
        assertTrue(XPathFuncExpr.rankIndex(4, new Object[]{1, 5d, null, "2"}) == 2);
    }

    public void testRankIndex_ties() throws Exception {
        // test ties
        assertEquals(new Integer(4), XPathFuncExpr.rankIndex(1, new Object[]{1.3, 4.5, 6.7, 4.5}));
        assertEquals(new Integer(3), XPathFuncExpr.rankIndex(2, new Object[]{1.3, 4.5, 6.7, 4.5}));
        assertEquals(new Integer(1), XPathFuncExpr.rankIndex(3, new Object[]{1.3, 4.5, 6.7, 4.5}));
        assertEquals(new Integer(2), XPathFuncExpr.rankIndex(4, new Object[]{1.3, 4.5, 6.7, 4.5}));

        assertEquals(new Integer(3), XPathFuncExpr.rankIndex(1, new Object[]{4.5, 4.5, 1.3, 6.7}));
        assertEquals(new Integer(2), XPathFuncExpr.rankIndex(2, new Object[]{4.5, 4.5, 1.3, 6.7}));
        assertEquals(new Integer(4), XPathFuncExpr.rankIndex(3, new Object[]{4.5, 4.5, 1.3, 6.7}));
        assertEquals(new Integer(1), XPathFuncExpr.rankIndex(4, new Object[]{4.5, 4.5, 1.3, 6.7}));

        assertEquals(new Integer(3), XPathFuncExpr.rankIndex(1, new Object[]{4, 4, 5}));
    }

    public void testRankValue() throws Exception {
        assertEquals(new Integer(1), XPathFuncExpr.rankValue(5, "4 4 5"));
        assertEquals(new Integer(2), XPathFuncExpr.rankValue(4, "5 4 4"));

        assertEquals(new Integer(3), XPathFuncExpr.rankValue(3, "3 4 5"));
        assertEquals(new Integer(2), XPathFuncExpr.rankValue(4, "4 4 5"));
        assertEquals(new Integer(1), XPathFuncExpr.rankValue(5, "5 4 5"));
        
        assertEquals(new Integer(XPathFuncExpr.INVALID_RANK_RESULT), XPathFuncExpr.rankValue(null, "5 4 5"));
        assertEquals(new Integer(XPathFuncExpr.INVALID_RANK_RESULT), XPathFuncExpr.rankValue(2, "5 4 5"));
        assertEquals(new Integer(XPathFuncExpr.INVALID_RANK_RESULT), XPathFuncExpr.rankValue(2, "3 f 5"));
        assertEquals(new Integer(2), XPathFuncExpr.rankValue(3, "3 f 5"));
        assertEquals(new Integer(1), XPathFuncExpr.rankValue(5, "3 f 5"));
        assertEquals(new Integer(XPathFuncExpr.INVALID_RANK_RESULT), XPathFuncExpr.rankValue(5, ""));
        assertEquals(new Integer(XPathFuncExpr.INVALID_RANK_RESULT), XPathFuncExpr.rankValue(5, null));
        assertEquals(new Integer(XPathFuncExpr.INVALID_RANK_RESULT), XPathFuncExpr.rankValue(5, "4"));
        assertEquals(new Integer(1), XPathFuncExpr.rankValue(5, "5"));
    }
}
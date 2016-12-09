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

package org.javarosa.core.util;

import java.text.NumberFormat;

/**
 * Author: Meletis Margaritis
 * Date: 6/27/14
 * Time: 1:42 PM
 */
public class FormatUtils {

  private static NumberFormat NUMBER_FORMAT_INSTANCE;

  /**
   * Formats an number using the default locale's format
   * to include the thousands separator for example.
   *
   * @param value the value as a String
   * @return a string containing the formatted value.
   */
  public static String formatNumber(String value) {
    if (value == null) {
      return null;
    }

    try {
      Double doubleValue = Double.valueOf(value);
      return getDefaultNumberInstance().format(doubleValue);
    } catch (Exception e) {
      return value;
    }
  }

  public static NumberFormat getDefaultNumberInstance() {
    if (NUMBER_FORMAT_INSTANCE == null) {
      NUMBER_FORMAT_INSTANCE = getNumberInstance(0, 15);
    }
    return NUMBER_FORMAT_INSTANCE;
  }

  /**
   * Returns the current device format settings for numbers.
   *
   * @param minimumFractionDigits minimumFractionDigits
   * @param maximumFractionDigits maximumFractionDigits
   * @return a {@link NumberFormat} instance
   */
  public static NumberFormat getNumberInstance(int minimumFractionDigits, int maximumFractionDigits) {
    NumberFormat numberFormat = NumberFormat.getInstance();
    numberFormat.setMinimumFractionDigits(minimumFractionDigits);
    numberFormat.setMaximumFractionDigits(maximumFractionDigits);
    return numberFormat;
  }
}

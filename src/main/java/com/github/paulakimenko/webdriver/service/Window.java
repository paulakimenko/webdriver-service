package com.github.paulakimenko.webdriver.service;

import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * Class which describes browser window properties.
 */
public final class Window {
    public static final String DEFAULT      = "default";
    public static final String MAXIMIZE     = "maximize";

    /**
     * Transform given dimension in string to int array.
     * Example : "1400x900" -> "[1400, 900]".
     * @param dimensionInString window dimension [width]x[height]
     * @return int array with two elements
     */
    public static int[] dimension(String dimensionInString) {
        int[] dimension = new int[2];

        if (isNullOrEmpty(dimensionInString))
            throw new RuntimeException("Arg string is null, or empty.");

        String[] strArray = dimensionInString.split("x");

        if (strArray.length != 2)
            throw new RuntimeException("Can't parse this string : " + dimensionInString);

        for (int i = 0; i < dimension.length; i++)
            dimension[i] = Integer.parseInt(strArray[i].trim());

        return dimension;
    }
}

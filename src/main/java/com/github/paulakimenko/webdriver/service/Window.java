package com.github.paulakimenko.webdriver.service;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;

import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * Class which describes browser window properties.
 */
public final class Window {
    private final Size size;
    private final Dimension customSize;
    private final Point position;

    /**
     * Build Browser window model
     * @param size window size. See com.github.paulakimenko.webdriver.service.Size.
     * @param customSize custom window size.
     * @param position window position.
     */
    public Window(Size size, Dimension customSize, Point position) {
        this.size = size;
        this.customSize = customSize;
        this.position = position;
    }

    /**
     * Get given window size mode.
     * @return window size mode
     */
    Size getSize() {
        return size;
    }

    /**
     * Get given custom window size.
     * @return custom window size
     */
    Dimension getCustomSize() {
        return customSize;
    }

    /**
     * Get given window position.
     * @return window position
     */
    Point getPosition() {
        return position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Window window = (Window) o;

        if (customSize != null ? !customSize.equals(window.customSize) : window.customSize != null) return false;
        if (!position.equals(window.position)) return false;
        if (size != window.size) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = size.hashCode();
        result = 31 * result + (customSize != null ? customSize.hashCode() : 0);
        result = 31 * result + position.hashCode();
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        if (size.equals(Size.CUSTOM)) {
            sb.append(customSize.getWidth());
            sb.append("x");
            sb.append(customSize.getHeight());
        } else {
            sb.append(size.toString().toLowerCase());
        }

        sb.append(" on ");
        sb.append(position.getX());
        sb.append("x");
        sb.append(position.getY());

        return sb.toString();
    }

    /**
     * Build Window instance from String.
     * <p>
     * example : 1400x900 on 10x45
     * <p>
     * example : maximize on 45x100
     * <p>
     * example : default
     * @param string string with syntax [height]x[width] on [xPos]x[yPos]
     * @return Window instance
     */
    public static Window valueOf(String string) {
        Size size;
        Dimension customSize;
        Point position;

        if (isNullOrEmpty(string))
            return new Window(Size.DEFAULT, null, new Point(0, 0));

        String[] strings = string.split("on");

        String sizeInString = strings[0].trim().toUpperCase();

        try {
            size = Size.valueOf(sizeInString);
            customSize = null;
        } catch (IllegalArgumentException e) {
            size = Size.CUSTOM;
            int[] dimensionArray = separateDimension(sizeInString.toLowerCase());
            customSize = new Dimension(dimensionArray[0], dimensionArray[1]);
        }

        if (strings.length == 1) {
            position = new Point(0, 0);
        } else if (strings.length == 2) {
            int[] pointArray = separateDimension(strings[1].trim().toLowerCase());
            position = new Point(pointArray[0], pointArray[1]);
        } else {
            throw new RuntimeException("Can't parse this string : " + string);
        }

        return new Window(size, customSize, position);
    }

    private static int[] separateDimension(String dimensionInString) {
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

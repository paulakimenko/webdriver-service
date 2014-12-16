package com.github.paulakimenko.webdriver.service;

/**
 * Properties key names for initialization from Map or Properties
 */
public final class PropertyKey {
    public static final String WEB_DRIVER_FACTORY = "webdriver.service.";
    public static final String DRIVER = WEB_DRIVER_FACTORY + "driver";
    public static final String REMOTE_ADDRESS = WEB_DRIVER_FACTORY + "remoteAddress";
    public static final String REMOTE_BROWSER = WEB_DRIVER_FACTORY + "remoteBrowser";
    public static final String TIME_UNIT = WEB_DRIVER_FACTORY + "timeUnit";
    public static final String IMPLICITLY_WAIT = WEB_DRIVER_FACTORY + "implicitlyWait";
    public static final String PAGE_LOAD_TIMEOUT = WEB_DRIVER_FACTORY + "pageLoadTimeout";
    public static final String SCRIPT_TIMEOUT = WEB_DRIVER_FACTORY + "scriptTimeout";
    public static final String FLUENT_WAIT_TIMEOUT = WEB_DRIVER_FACTORY + "fluentWaitTimeout";
    public static final String WINDOW_SIZE = WEB_DRIVER_FACTORY + "windowSize";
}

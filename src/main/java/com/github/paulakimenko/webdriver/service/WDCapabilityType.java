package com.github.paulakimenko.webdriver.service;

/**
 * WDCapabilities key names
 */
public final class WDCapabilityType {
    public static final String WEB_DRIVER_FACTORY = "webdriver.service.";
    public static final String REMOTE = "remote";
    public static final String HUB_URL = WEB_DRIVER_FACTORY + "hubUrl";
    public static final String TIME_UNIT = WEB_DRIVER_FACTORY + "timeUnit";
    public static final String IMPLICITLY_WAIT = WEB_DRIVER_FACTORY + "implicitlyWait";
    public static final String PAGE_LOAD_TIMEOUT = WEB_DRIVER_FACTORY + "pageLoadTimeout";
    public static final String SCRIPT_TIMEOUT = WEB_DRIVER_FACTORY + "scriptTimeout";
    public static final String FLUENT_WAIT_TIMEOUT = WEB_DRIVER_FACTORY + "fluentWaitTimeout";
    public static final String WINDOW = WEB_DRIVER_FACTORY + "window";
}

package com.github.paulakimenko.webdriver.service;

import org.openqa.selenium.Capabilities;

import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * Extension for Selenium Capabilities.
 */
public interface WDCapabilities extends Capabilities {

    /**
     * Is Remote WebDriver used.
     * @return true, if used Remote WebDriver
     */
    boolean isRemote();

    /**
     * Get remote URL (hub of grid).
     * @return remote URL
     */
    URL getHubUrl();

    /**
     * Get TimeUnit for WebDriver waits.
     * @return TimeUnit for WebDriver waits
     */
    TimeUnit getTimeUnit();

    /**
     * Get implicitly wait timeout.
     * @return implicitly wait timeout
     */
    long getImplicitlyWait();

    /**
     * Get page load timeout.
     * @return page load timeout
     */
    long getPageLoadTimeout();

    /**
     * Get script timeout.
     * @return script timeout
     */
    long getScriptTimeout();

    /**
     * Get default WebDriver fluent wait timeout.
     * <p>
     * Used in com.github.paulakimenko.webdriver.service.getDefWebDriverWait() method.
     * @return default WebDriver fluent wait timeout
     */
    long getFluentWaitTimeout();

    /**
     * Get default WebDriver browser window properties.
     * @return default WebDriver browser window properties
     */
    Window getWindow();
}

package com.github.paulakimenko.webdriver.service;

import com.google.common.base.Function;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.internal.WrapsDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public interface WDService {
    /**
     * Initiate WebDriver with current properties. Timeouts will be enabled.
     * <p>
     * Throws RuntimeException with "WebDriver has been already initialized. Terminate it first.".
     * <p>
     * Throws IllegalArgumentException with "Given driver type has been not implemented yet.".
     */
    void init();

    /**
     * Initiate WebDriver with current properties and capabilities.
     * <p>
     * Throws RuntimeException with "WebDriver has been already initialized. Terminate it first.".
     * <p>
     * Throws IllegalArgumentException with "Given driver type has been not implemented yet.".
     * @param capabilities driver capabilities
     */
    void init(Capabilities capabilities);

    /**
     * Invoke close() and quit() methods and assign to null driver variable.
     * Throws NullPointerException with "WebDriver has been not initialized.".
     */
    void terminate();

    /**
     * Enable timeouts for WebDriver with parameters from Properties.
     */
    void enableTimeouts();

    /**
     * Disable timeouts for WebDriver (assign it to 0).
     */
    void disableTimeouts();

    /**
     * Set custom WebDriverProperties
     * @param properties custom WebDriverProperties
     */
    void setProperties(WDProperties properties);

    /**
     * Wraps current WebDriver with wrapper(must implement WebDriver, WrapsDriver).
     * <p>
     * Throws IllegalArgumentException with "transformFunction doesn't produce WebDriver instance".
     * @param transformFunction function to wrap current driver with wrapped
     */
    void wrapWith(Function<WebDriver, WrapsDriver> transformFunction);

    /**
     * Wraps current WebDriver with wrapper(must implements WebDriver, WrapsDriver).
     * <p>
     * Throws IllegalArgumentException with "Wrapper class is not instance of WebDriver.".
     * <p>
     * Throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException.
     * @param driverWrapperClass WebDriver wrapped class with (WebDriver instance) constructor
     * @param <T> must implement WebDriver, WrapsDriver
     */
    <T extends WrapsDriver> void wrapWith(Class<T> driverWrapperClass);

    /**
     * Set custom instance of WebDriver to provide it.
     * @param driver WebDriver instance
     */
    void setCustomDriver(WebDriver driver);

    /**
     * Get current WebDriver instance.
     * <p>
     * @return current WebDriver instance, or null if WebDriver instance hasn't initialized
     */
    WebDriver getDriver();

    /**
     * Get JavascriptExecutor instance for current WebDriver instance.
     * @return JavascriptExecutor instance, or null if WebDriver instance hasn't initialized
     */
    JavascriptExecutor getJsExecutor();

    /**
     * Get TakesScreenshot instance for current WebDriver instance.
     * @return TakesScreenshot instance, or null if WebDriver instance hasn't initialized
     */
    TakesScreenshot getScreenshotMaker();

    /**
     * Get WebDriverWait instance for current WebDriver instance.
     * @return WebDriverWait instance, or null if WebDriver instance hasn't initialized
     */
    WebDriverWait getDefWebDriverWait();
}

package com.github.paulakimenko.webdriver.service;

import com.google.common.base.Function;
import com.opera.core.systems.OperaDriver;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.internal.WrapsDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ThreadGuard;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.TimeUnit;

/**
 * Implementation of WDService.
 * <p>
 * Uses java.lang.ThreadLocal for generating of thread local instances.
 */
public class WDServiceProvider implements WDService {
    private static final ThreadLocal<WDService> THREAD_LOCAL = new ThreadLocal<WDService>() {
            @Override
            public WDService initialValue() {
                return new WDServiceProvider();
            }
    };

    private WebDriver driver;
    private WDProperties properties;

    private WDServiceProvider() {
        driver = null;
        properties = null;
    }

    /**
     * Get single instance on WDService for current thread.
     * @return single instance on WDService for current thread
     */
    public static WDService getInstance() {
        return THREAD_LOCAL.get();
    }

    @Override
    public void init() {
        init(null);
    }

    @Override
    public void init(Capabilities capabilities) {
        if (driver != null)
            throw new RuntimeException("WebDriver has been already initialized. Terminate it first.");

        if (properties == null)
            properties = WDProperties.buildFromSystemProperties();

        switch (properties.getDriver()) {
            case REMOTE:
                if (capabilities == null)
                    capabilities = getCapabilitiesByName(properties.getRemoteBrowser());

                RemoteWebDriver remoteWebDriver = new RemoteWebDriver(properties.getRemoteAddress(), capabilities);
                remoteWebDriver.setFileDetector(new LocalFileDetector());
                driver = ThreadGuard.protect(new Augmenter().augment(remoteWebDriver));
                break;
            case FIREFOX:
                driver = new FirefoxDriver(capabilities);
                break;
            case CHROME:
                driver = new ChromeDriver(capabilities);
                break;
            case SAFARI:
                driver = capabilities == null
                        ? new SafariDriver(DesiredCapabilities.safari())
                        : new SafariDriver(capabilities);
                break;
            case IEXPLORE:
                driver = new InternetExplorerDriver(capabilities);
                break;
            case OPERA:
                driver = capabilities == null
                        ? new OperaDriver()
                        : new OperaDriver(capabilities);
            case HTMLUNIT:
                driver = new HtmlUnitDriver();
                break;
            case PHANTOMJS:
                driver = capabilities == null
                        ? new PhantomJSDriver(DesiredCapabilities.phantomjs())
                        : new PhantomJSDriver(capabilities);
                break;
            default:
                throw new IllegalArgumentException("Given driver type has been not implemented yet.");
        }
        enableTimeouts();
    }

    @Override
    public void terminate() {
        if (driver != null) {
            driver.quit();
            driver = null;
        } else {
            throw new NullPointerException("WebDriver has been not initialized.");
        }
    }

    @Override
    public void enableTimeouts() {
        setTimeouts(
                properties.getImplicitlyWait(),
                properties.getPageLoadTimeout(),
                properties.getScriptTimeout(),
                properties.getTimeUnit()
        );
    }

    @Override
    public void disableTimeouts() {
        setTimeouts(0, 0, 0, properties.getTimeUnit());
    }

    @Override
    public void setProperties(WDProperties properties) {
        this.properties = properties;
    }

    @Override
    public void wrapWith(Function<WebDriver, WrapsDriver> transformFunction) {
        WrapsDriver wrapsDriver = transformFunction.apply(driver);
        if (wrapsDriver instanceof WebDriver) {
            driver = (WebDriver) wrapsDriver;
        } else {
            throw new IllegalArgumentException("transformFunction doesn't produce WebDriver instance");
        }
    }

    @Override
    public <T extends WrapsDriver> void wrapWith(Class<T> driverWrapperClass) {
        WrapsDriver wrapsDriver = null;

        try {
            wrapsDriver = driverWrapperClass.getDeclaredConstructor(WebDriver.class).newInstance(driver);
        } catch (InstantiationException
                | IllegalAccessException
                | InvocationTargetException
                | NoSuchMethodException e) {
            e.printStackTrace();
        }

        if (wrapsDriver instanceof WebDriver) {
            driver = (WebDriver) wrapsDriver;
        } else {
            throw new IllegalArgumentException("Wrapper class is not instance of WebDriver.");
        }
    }

    @Override
    public void setCustomDriver(WebDriver driver) {
        this.driver = driver;
    }

    @Override
    public WebDriver getDriver() {
        if (driver != null)
            return driver;

        throw new NullPointerException("WebDriver has been not initialized. Try to call init() first.");
    }

    @Override
    public JavascriptExecutor getJsExecutor() {
        return (JavascriptExecutor) driver;
    }

    @Override
    public TakesScreenshot getScreenshotMaker() {
        return (TakesScreenshot) driver;
    }

    @Override
    public WebDriverWait getDefWebDriverWait() {
        return new WebDriverWait(driver, properties.getFluentWaitTimeout());
    }

    private void setTimeouts(long implicitlyWait, long pageLoadTimeout, long scriptTimeout, TimeUnit timeUnit) {
        WebDriver.Timeouts timeouts = driver.manage().timeouts();
        timeouts.implicitlyWait(implicitlyWait, timeUnit);
        timeouts.pageLoadTimeout(pageLoadTimeout, timeUnit);
        timeouts.setScriptTimeout(scriptTimeout, timeUnit);
    }

    private static Capabilities getCapabilitiesByName(String capabilityName) {
        try {
            return (Capabilities) DesiredCapabilities.class
                    .getMethod(capabilityName, null).invoke(null, null);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Incorrect browser name for capabilities. " +
                    "Set it from org.openqa.selenium.remote.DesiredCapabilities static methods names " +
                    "(ex. firefox).", e);
        }

        return null;
    }
}

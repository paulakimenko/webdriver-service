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
import org.openqa.selenium.remote.BrowserType;
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
    private static ThreadLocal<WDService> threadLocal = new ThreadLocal<WDService>() {
            @Override
            public WDService initialValue() {
                return new WDServiceProvider();
            }
    };

    private WebDriver driver;
    private WDCapabilities wdCapabilities;

    private WDServiceProvider() {
        driver = null;
        wdCapabilities = WDDesiredCapabilities.getDefault();
    }

    /**
     * Get single instance (or create new) of WDService for current thread.
     * @return single instance of WDService for current thread
     */
    public static WDService getInstance() {
        return threadLocal.get();
    }

    /**
     * Remove current thread instance.
     */
    public static void removeInstance() {
        threadLocal.remove();
    }

    @Override
    public void init() {
        if (driver != null)
            throw new RuntimeException("WebDriver has been already initialized. Terminate it first.");

        if (wdCapabilities.isRemote()) {
            RemoteWebDriver remoteWebDriver = new RemoteWebDriver(wdCapabilities.getHubUrl(), wdCapabilities);
            remoteWebDriver.setFileDetector(new LocalFileDetector());
            driver = ThreadGuard.protect(new Augmenter().augment(remoteWebDriver));
        } else {
            switch (wdCapabilities.getBrowserName()) {
                case BrowserType.FIREFOX:
                    driver = new FirefoxDriver(wdCapabilities);
                    break;
                case BrowserType.CHROME:
                    driver = new ChromeDriver(wdCapabilities);
                    break;
                case BrowserType.SAFARI:
                    driver = new SafariDriver(wdCapabilities);
                    break;
                case BrowserType.IEXPLORE:
                case BrowserType.IE:
                    driver = new InternetExplorerDriver(wdCapabilities);
                    break;
                case BrowserType.OPERA:
                    driver = new OperaDriver(wdCapabilities);
                    break;
                case BrowserType.HTMLUNIT:
                    driver = new HtmlUnitDriver(wdCapabilities);
                    break;
                case BrowserType.PHANTOMJS:
                    driver = new PhantomJSDriver(wdCapabilities);
                    break;
                default:
                    throw new IllegalArgumentException("Given driver type has been not implemented yet.");
            }
        }
        changeWindowSize();
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
                wdCapabilities.getImplicitlyWait(),
                wdCapabilities.getPageLoadTimeout(),
                wdCapabilities.getScriptTimeout(),
                wdCapabilities.getTimeUnit()
        );
    }

    @Override
    public void disableTimeouts() {
        setTimeouts(0, 0, 0, wdCapabilities.getTimeUnit());
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
    public void setCapabilities(Capabilities capabilities) {
        if (!(capabilities instanceof WDCapabilities)) {
            wdCapabilities = WDDesiredCapabilities.getDefault().merge(capabilities);
        } else {
            this.wdCapabilities = (WDDesiredCapabilities) capabilities;
        }
    }

    @Override
    public void setCustomDriver(WebDriver driver) {
        this.driver = driver;
    }

    @Override
    public WebDriver getDriver() {
        return driver;
    }

    @Override
    public JavascriptExecutor getJsExecutor() {
        return (JavascriptExecutor) getDriver();
    }

    @Override
    public TakesScreenshot getScreenshotMaker() {
        return (TakesScreenshot) getDriver();
    }

    @Override
    public WebDriverWait getDefWebDriverWait() {
        if (getDriver() == null || wdCapabilities == null)
            return null;
        return new WebDriverWait(getDriver(), wdCapabilities.getFluentWaitTimeout());
    }

    private void changeWindowSize() {
        Window window = wdCapabilities.getWindow();
        if (Size.MAXIMIZE.equals(window.getSize())) {
            driver.manage().window().maximize();
        } else if (Size.CUSTOM.equals(window.getSize())) {
            driver.manage().window().setSize(window.getCustomSize());
        }
        driver.manage().window().setPosition(window.getPosition());
    }

    private void setTimeouts(long implicitlyWait, long pageLoadTimeout, long scriptTimeout, TimeUnit timeUnit) {
        WebDriver.Timeouts timeouts = getDriver().manage().timeouts();
        timeouts.implicitlyWait(implicitlyWait, timeUnit);
        timeouts.pageLoadTimeout(pageLoadTimeout, timeUnit);
        timeouts.setScriptTimeout(scriptTimeout, timeUnit);
    }
}

package com.github.paulakimenko.webdriver.service.tests;

import com.github.paulakimenko.webdriver.service.PropertyKey;
import com.github.paulakimenko.webdriver.service.WDProperties;
import com.github.paulakimenko.webdriver.service.WDServiceProvider;
import com.github.paulakimenko.webdriver.service.Driver;
import com.github.paulakimenko.webdriver.service.WDService;
import com.google.common.base.Function;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.internal.WrapsDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

public class WDProviderTests {
    @Test(expectedExceptions = {NullPointerException.class},
            expectedExceptionsMessageRegExp = "WebDriver has been not initialized.*")
    public void baseInitWithNullTest() {
        WDServiceProvider.getInstance().getDriver();
    }

    @Test
    public void baseInitTest() {
        WDService service = WDServiceProvider.getInstance();
        service.init();
        WebDriver driver = service.getDriver();
        assertNotNull(driver);
        assertTrue(driver instanceof FirefoxDriver);
    }

    @Test(expectedExceptions = {RuntimeException.class},
            expectedExceptionsMessageRegExp = "WebDriver has been already initialized.*")
    public void doubleInitTest() {
        WDService service = WDServiceProvider.getInstance();
        service.init();
        service.init();
    }

    @Test
    public void fromBuilderInitTest() {
        WDService service = WDServiceProvider.getInstance();
        service.setProperties(
                new WDProperties.Builder()
                        .driver(Driver.FIREFOX)
                        .build()
        );
        service.init();
        WebDriver driver = service.getDriver();
        assertNotNull(driver);
        assertTrue(driver instanceof FirefoxDriver);
    }

    @Test
    public void fromMapInitTest() {
        WDService service = WDServiceProvider.getInstance();
        Map<String, String> map = new HashMap<>();
        map.put(PropertyKey.DRIVER, "firefox");
        service.setProperties(new WDProperties.Builder(map).build());
        service.init();
        WebDriver driver = service.getDriver();
        assertNotNull(driver);
        assertTrue(driver instanceof FirefoxDriver);
    }

    @Test
    public void fromSysPropsInitTest() {
        WDService service = WDServiceProvider.getInstance();
        System.setProperty(PropertyKey.DRIVER, "htmlunit");
        service.init();
        WebDriver driver = service.getDriver();
        assertNotNull(driver);
        assertTrue(driver instanceof HtmlUnitDriver);
    }

    @Test(expectedExceptions = {NullPointerException.class},
            expectedExceptionsMessageRegExp = "WebDriver has been not initialized.*")
    public void terminateTest() {
        WDService service = WDServiceProvider.getInstance();
        service.init();
        assertNotNull(service.getDriver());
        service.terminate();
        assertNull(service.getDriver());
    }

    @Test
    public void firstWrapDriverTest() {
        WDService service = WDServiceProvider.getInstance();
        service.init();
        assertNotNull(service.getDriver());
        service.wrapWith(MockWrapsWebDriverImpl.class);
        assertTrue(service.getDriver() instanceof WebDriver);
        assertTrue(service.getDriver() instanceof WrapsDriver);
        assertTrue(service.getDriver() instanceof MockWrapsWebDriverImpl);
        assertTrue(((WrapsDriver) service.getDriver()).getWrappedDriver() instanceof FirefoxDriver);
    }

    @Test
    public void secondWrapDriverTest() {
        WDService service = WDServiceProvider.getInstance();
        service.init();
        assertNotNull(service.getDriver());
        service.wrapWith(new Function<WebDriver, WrapsDriver>() {
            @Override
            public WrapsDriver apply(WebDriver input) {
                return new MockWrapsWebDriverImpl(input);
            }
        });
        assertTrue(service.getDriver() instanceof WebDriver);
        assertTrue(service.getDriver() instanceof WrapsDriver);
        assertTrue(service.getDriver() instanceof MockWrapsWebDriverImpl);
        assertTrue(((WrapsDriver) service.getDriver()).getWrappedDriver() instanceof FirefoxDriver);
    }

    @Test
    public void setCustomDriverTest() {
        WDService service = WDServiceProvider.getInstance();
        WebDriver driver = new HtmlUnitDriver();
        service.setCustomDriver(driver);
        assertNotNull(service.getDriver());
        assertEquals(service.getDriver(), driver);
    }

    @Test
    public void getJsExecutorTest() {
        WDService service = WDServiceProvider.getInstance();
        service.init();
        assertNotNull(service.getJsExecutor());
    }

    @Test
    public void getScreenshotMaker() {
        WDService service = WDServiceProvider.getInstance();
        service.init();
        assertNotNull(service.getScreenshotMaker());
    }

    @Test
    public void getDefWebDriverWait() {
        WDService service = WDServiceProvider.getInstance();
        service.init();
        assertNotNull(service.getDefWebDriverWait());
    }

    @Test
    public void singleThreadingInstanceTest() {
        WDService service = WDServiceProvider.getInstance();
        service.init();
        assertEquals(WDServiceProvider.getInstance().getDriver(), service.getDriver());
    }

    @Test
    public void multiThreadingInstanceTest() {
        //TODO: this
    }

    @AfterMethod
    public void tearDown() {
        try {
            WDService service = WDServiceProvider.getInstance();
            service.terminate();
            service.setProperties(null);
            System.clearProperty(PropertyKey.DRIVER);
        } catch (NullPointerException ignored) {}
    }

    /*
    Privates
     */

    public static class MockWrapsWebDriverImpl implements WrapsDriver, WebDriver {
        WebDriver wrappedDriver;

        public MockWrapsWebDriverImpl(WebDriver wrappedDriver) {
            this.wrappedDriver = wrappedDriver;
        }

        @Override
        public void get(String url) {}

        @Override
        public String getCurrentUrl() {
            return null;
        }

        @Override
        public String getTitle() {
            return null;
        }

        @Override
        public List<WebElement> findElements(By by) {
            return null;
        }

        @Override
        public WebElement findElement(By by) {
            return null;
        }

        @Override
        public String getPageSource() {
            return null;
        }

        @Override
        public void close() {
            wrappedDriver.close();
        }

        @Override
        public void quit() {
            wrappedDriver.quit();
        }

        @Override
        public Set<String> getWindowHandles() {
            return null;
        }

        @Override
        public String getWindowHandle() {
            return null;
        }

        @Override
        public TargetLocator switchTo() {
            return null;
        }

        @Override
        public Navigation navigate() {
            return null;
        }

        @Override
        public Options manage() {
            return null;
        }

        @Override
        public WebDriver getWrappedDriver() {
            return wrappedDriver;
        }
    }
}

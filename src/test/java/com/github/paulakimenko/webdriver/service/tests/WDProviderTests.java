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
        WDService factory = WDServiceProvider.getInstance();
        factory.init();
        WebDriver driver = factory.getDriver();
        assertNotNull(driver);
        assertTrue(driver instanceof FirefoxDriver);
    }

    @Test(expectedExceptions = {RuntimeException.class},
            expectedExceptionsMessageRegExp = "WebDriver has been already initialized.*")
    public void doubleInitTest() {
        WDService factory = WDServiceProvider.getInstance();
        factory.init();
        factory.init();
    }

    @Test
    public void fromBuilderInitTest() {
        WDService factory = WDServiceProvider.getInstance();
        factory.setProperties(
                new WDProperties.Builder()
                        .driver(Driver.FIREFOX)
                        .build()
        );
        factory.init();
        WebDriver driver = factory.getDriver();
        assertNotNull(driver);
        assertTrue(driver instanceof FirefoxDriver);
    }

    @Test
    public void fromMapInitTest() {
        WDService factory = WDServiceProvider.getInstance();
        Map<String, String> map = new HashMap<>();
        map.put(PropertyKey.DRIVER, "firefox");
        factory.setProperties(new WDProperties.Builder(map).build());
        factory.init();
        WebDriver driver = factory.getDriver();
        assertNotNull(driver);
        assertTrue(driver instanceof FirefoxDriver);
    }

    @Test
    public void fromSysPropsInitTest() {
        WDService factory = WDServiceProvider.getInstance();
        System.setProperty(PropertyKey.DRIVER, "htmlunit");
        factory.init();
        WebDriver driver = factory.getDriver();
        assertNotNull(driver);
        assertTrue(driver instanceof HtmlUnitDriver);
    }

    @Test(expectedExceptions = {NullPointerException.class},
            expectedExceptionsMessageRegExp = "WebDriver has been not initialized.*")
    public void terminateTest() {
        WDService factory = WDServiceProvider.getInstance();
        factory.init();
        assertNotNull(factory.getDriver());
        factory.terminate();
        assertNull(factory.getDriver());
    }

    @Test
    public void firstWrapDriverTest() {
        WDService factory = WDServiceProvider.getInstance();
        factory.init();
        assertNotNull(factory.getDriver());
        factory.wrapWith(MockWrapsWebDriverImpl.class);
        assertTrue(factory.getDriver() instanceof WebDriver);
        assertTrue(factory.getDriver() instanceof WrapsDriver);
        assertTrue(factory.getDriver() instanceof MockWrapsWebDriverImpl);
        assertTrue(((WrapsDriver) factory.getDriver()).getWrappedDriver() instanceof FirefoxDriver);
    }

    @Test
    public void secondWrapDriverTest() {
        WDService factory = WDServiceProvider.getInstance();
        factory.init();
        assertNotNull(factory.getDriver());
        factory.wrapWith(new Function<WebDriver, WrapsDriver>() {
            @Override
            public WrapsDriver apply(WebDriver input) {
                return new MockWrapsWebDriverImpl(input);
            }
        });
        assertTrue(factory.getDriver() instanceof WebDriver);
        assertTrue(factory.getDriver() instanceof WrapsDriver);
        assertTrue(factory.getDriver() instanceof MockWrapsWebDriverImpl);
        assertTrue(((WrapsDriver) factory.getDriver()).getWrappedDriver() instanceof FirefoxDriver);
    }

    @Test
    public void setCustomDriverTest() {
        WDService factory = WDServiceProvider.getInstance();
        WebDriver driver = new HtmlUnitDriver();
        factory.setCustomDriver(driver);
        assertNotNull(factory.getDriver());
        assertEquals(factory.getDriver(), driver);
    }

    @Test
    public void singleThreadingInstanceTest() {
        WDService factory = WDServiceProvider.getInstance();
        factory.init();
        assertEquals(WDServiceProvider.getInstance().getDriver(), factory.getDriver());
    }

    @Test
    public void multiThreadingInstanceTest() {
        //TODO: this
    }

    @AfterMethod
    public void tearDown() {
        try {
            WDService factory = WDServiceProvider.getInstance();
            factory.terminate();
            factory.setProperties(null);
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

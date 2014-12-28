package com.github.paulakimenko.webdriver.service;

import com.google.common.base.Function;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.internal.WrapsDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Set;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

public class WDProviderTests {
    @Test
    public void baseInitWithNullTest() {
        WDService service = WDServiceProvider.getInstance();
        assertNull(service.getDriver());
        assertNull(service.getJsExecutor());
        assertNull(service.getScreenshotMaker());
        assertNull(service.getDefWebDriverWait());
    }

    @Test
    public void baseInitTest() {
        WDService service = WDServiceProvider.getInstance();
        service.init();
        WebDriver driver = service.getDriver();
        assertEquals(driver.getClass(), FirefoxDriver.class);
    }

    @Test(expectedExceptions = {RuntimeException.class},
            expectedExceptionsMessageRegExp = "WebDriver has been already initialized.*")
    public void doubleInitTest() {
        WDService service = WDServiceProvider.getInstance();
        service.init();
        WebDriver driver = service.getDriver();
        assertEquals(driver.getClass(), FirefoxDriver.class);
        service.init();
    }

    @Test
    public void initWithCustomCapabilitiesTest() {
        Capabilities capabilities = DesiredCapabilities.phantomjs();
        WDService service = WDServiceProvider.getInstance();
        service.setCapabilities(capabilities);
        service.init();
        WebDriver driver = service.getDriver();
        assertEquals(driver.getClass(), PhantomJSDriver.class);
    }

    @Test
    public void fromSysPropsInitTest() {
        System.setProperty(CapabilityType.BROWSER_NAME, BrowserType.PHANTOMJS);
        WDService service = WDServiceProvider.getInstance();
        service.setCapabilities(WDDesiredCapabilities.getFromSystemProperties());
        service.init();
        WebDriver driver = service.getDriver();
        assertEquals(driver.getClass(), PhantomJSDriver.class);
    }

    @Test
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
        assertTrue(service.getDriver() instanceof MockWrapsWebDriverImpl);
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
        assertTrue(service.getDriver() instanceof MockWrapsWebDriverImpl);
    }

    @Test
    public void setCustomDriverTest() {
        WDService service = WDServiceProvider.getInstance();
        service.setCustomDriver(new HtmlUnitDriver());
        assertEquals(service.getDriver().getClass(), HtmlUnitDriver.class);
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
    public void windowTest() {
        String paramsInStr = "1400x900 on 10x15";
        Window window = Window.valueOf(paramsInStr);
        assertNotNull(window);
        assertEquals(window.getSize(), Size.CUSTOM);
        assertEquals(window.getCustomSize().getHeight(), 900);
        assertEquals(window.getCustomSize().getWidth(), 1400);
        assertEquals(window.getPosition().getX(), 10);
        assertEquals(window.getPosition().getY(), 15);
    }

    @Test
    public void windowTest2() {
        String paramsInStr = "maximize on 10x15";
        Window window = Window.valueOf(paramsInStr);
        assertNotNull(window);
        assertEquals(window.getSize(), Size.MAXIMIZE);
        assertNull(window.getCustomSize());
        assertEquals(window.getPosition().getX(), 10);
        assertEquals(window.getPosition().getY(), 15);
    }

    @Test
    public void windowTest3() {
        String paramsInStr = "default";
        Window window = Window.valueOf(paramsInStr);
        assertNotNull(window);
        assertEquals(window.getSize(), Size.DEFAULT);
        assertNull(window.getCustomSize());
        assertEquals(window.getPosition().getX(), 0);
        assertEquals(window.getPosition().getY(), 0);
    }

    @AfterMethod
    public void tearDown() {
        try {
            WDService service = WDServiceProvider.getInstance();
            service.terminate();
            service.setCapabilities(null);
            System.clearProperty(CapabilityType.BROWSER_NAME);
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

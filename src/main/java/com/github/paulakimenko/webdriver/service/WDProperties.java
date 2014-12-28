package com.github.paulakimenko.webdriver.service;

import com.google.common.collect.Sets;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ISuite;
import org.testng.ITestContext;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * Common WebDriver properties.
 */
public final class WDProperties {
    private final Driver driver;
    private final URL remoteAddress;
    private final String remoteBrowser;
    private final TimeUnit timeUnit;
    private final long implicitlyWait;
    private final long pageLoadTimeout;
    private final long scriptTimeout;
    private final long fluentWaitTimeout;
    private final String windowSize;

    private WDProperties(Builder builder) {
        this.driver = builder.driver;
        this.remoteAddress = builder.remoteAddress;
        this.remoteBrowser = builder.remoteBrowser;
        this.timeUnit = builder.timeUnit;
        this.implicitlyWait = builder.implicitlyWait;
        this.pageLoadTimeout = builder.pageLoadTimeout;
        this.scriptTimeout = builder.scriptTimeout;
        this.fluentWaitTimeout = builder.fluentWaitTimeout;
        this.windowSize = builder.windowSize;
    }

    /**
     * Get WebDriver type.
     * @return WebDriver type
     */
    public Driver getDriver() {
        return driver;
    }

    /**
     * Get remote URL (hub of grid).
     * @return remote URL
     */
    public URL getRemoteAddress() {
        return remoteAddress;
    }

    /**
     * Get remote browser name.
     * <p>
     * Method name from org.openqa.selenium.remote.DesiredCapabilities.
     * Example : firefox.
     * @return remote browser name
     */
    public String getRemoteBrowser() {
        DesiredCapabilities.firefox();
        return remoteBrowser;
    }

    /**
     * Get TimeUnit for WebDriver waits.
     * @return TimeUnit for WebDriver waits
     */
    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    /**
     * Get implicitly wait timeout.
     * @return implicitly wait timeout
     */
    public long getImplicitlyWait() {
        return implicitlyWait;
    }

    /**
     * Get page load timeout.
     * @return page load timeout
     */
    public long getPageLoadTimeout() {
        return pageLoadTimeout;
    }

    /**
     * Get script timeout.
     * @return script timeout
     */
    public long getScriptTimeout() {
        return scriptTimeout;
    }

    /**
     * Get default WebDriver fluent wait timeout.
     * <p>
     * Used in com.github.paulakimenko.webdriver.service.getDefWebDriverWait() method.
     * @return default WebDriver fluent wait timeout
     */
    public long getFluentWaitTimeout() {
        return fluentWaitTimeout;
    }

    /**
     * Get default WebDriver browser window size.
     * @return default WebDriver browser window size
     */
    public String getWindowSize() {
        return windowSize;
    }

    /**
     * Get WDProperties instance from java.util.Properties.
     * <p>
     * Uses constants from PropertyKey as keys.
     * @param properties given Properties instance
     * @return WDProperties instance
     */
    public static WDProperties buildFromProperties(Properties properties) {
        Map<String, String> map = new HashMap<>();
        for (String key : properties.stringPropertyNames())
            map.put(key, properties.getProperty(key));

        return new WDProperties.Builder(map).build();
    }

    /**
     * Get WDProperties instance from system properties.
     * <p>
     * Uses constants from PropertyKey as keys.
     * @return WDProperties instance
     */
    public static WDProperties buildFromSystemProperties() {
        return buildFromProperties(System.getProperties());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("WDProperties{");
        sb.append("driver=").append(driver);
        sb.append(", remoteAddress=").append(remoteAddress);
        sb.append(", remoteBrowser='").append(remoteBrowser).append('\'');
        sb.append(", timeUnit=").append(timeUnit);
        sb.append(", implicitlyWait=").append(implicitlyWait);
        sb.append(", pageLoadTimeout=").append(pageLoadTimeout);
        sb.append(", scriptTimeout=").append(scriptTimeout);
        sb.append(", fluentWaitTimeout=").append(fluentWaitTimeout);
        sb.append(", windowSize='").append(windowSize).append('\'');
        sb.append('}');
        return sb.toString();
    }

    /**
     * Builder for WDProperties.
     */
    public static class Builder {
        private Driver driver;
        private URL remoteAddress;
        private String remoteBrowser;
        private TimeUnit timeUnit;
        private long implicitlyWait;
        private long pageLoadTimeout;
        private long scriptTimeout;
        private long fluentWaitTimeout;
        private String windowSize;

        /**
         * Build default properties.
         */
        public Builder() {
            this.driver = Driver.FIREFOX;
            this.remoteBrowser = "firefox";
            this.timeUnit = TimeUnit.SECONDS;
            this.implicitlyWait = 5;
            this.pageLoadTimeout = 5;
            this.scriptTimeout = 5;
            this.fluentWaitTimeout = 5;
            this.windowSize = Window.DEFAULT;
            try {
                this.remoteAddress = new URL("http://localhost:4444/wd/hub");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        /**
         * Build from existed WDProperties instance's copy.
         * @param properties WDProperties instance's copy
         */
        public Builder(WDProperties properties) {
            this.driver = properties.driver;
            this.remoteAddress = properties.remoteAddress;
            this.remoteBrowser = properties.remoteBrowser;
            this.timeUnit = properties.timeUnit;
            this.implicitlyWait = properties.implicitlyWait;
            this.pageLoadTimeout = properties.pageLoadTimeout;
            this.scriptTimeout = properties.scriptTimeout;
            this.fluentWaitTimeout = properties.fluentWaitTimeout;
            this.windowSize = properties.windowSize;
        }

        /**
         * Build from Map.
         * <p>
         * Uses constants from PropertyKey as keys.
         * @param map map with values
         */
        public Builder(Map<String, String> map) {
            this();
            if (map.containsKey(PropertyKey.DRIVER)) driver(map.get(PropertyKey.DRIVER));
            if (map.containsKey(PropertyKey.REMOTE_ADDRESS)) remoteAddress(map.get(PropertyKey.REMOTE_ADDRESS));
            if (map.containsKey(PropertyKey.REMOTE_BROWSER)) remoteBrowser(map.get(PropertyKey.REMOTE_BROWSER));
            if (map.containsKey(PropertyKey.TIME_UNIT)) timeUnit(map.get(PropertyKey.TIME_UNIT));
            if (map.containsKey(PropertyKey.IMPLICITLY_WAIT)) implicitlyWait(map.get(PropertyKey.IMPLICITLY_WAIT));
            if (map.containsKey(PropertyKey.PAGE_LOAD_TIMEOUT)) pageLoadTimeout(map.get(PropertyKey.PAGE_LOAD_TIMEOUT));
            if (map.containsKey(PropertyKey.SCRIPT_TIMEOUT)) scriptTimeout(map.get(PropertyKey.SCRIPT_TIMEOUT));
            if (map.containsKey(PropertyKey.FLUENT_WAIT_TIMEOUT)) fluentWaitTimeout(map.get(PropertyKey.FLUENT_WAIT_TIMEOUT));
            if (map.containsKey(PropertyKey.WINDOW_SIZE)) windowSize(map.get(PropertyKey.WINDOW_SIZE));
        }

        /**
         * Build from suite properties of org.testng.ITestContext.
         * <p>
         * Uses constants from PropertyKey as keys.
         * @param context instance on org.testng.ITestContext
         */
        public Builder(ITestContext context) {
            this(getMapFromSuiteProps(context.getSuite(), Sets.newHashSet(
                    PropertyKey.DRIVER, PropertyKey.REMOTE_ADDRESS, PropertyKey.REMOTE_BROWSER, PropertyKey.TIME_UNIT,
                    PropertyKey.IMPLICITLY_WAIT, PropertyKey.PAGE_LOAD_TIMEOUT, PropertyKey.SCRIPT_TIMEOUT, PropertyKey.FLUENT_WAIT_TIMEOUT,
                    PropertyKey.WINDOW_SIZE
            )));
        }

        /**
         * Set WebDriver type.
         * @param driver WebDriver type
         * @return this Builder instance
         */
        public Builder driver(Driver driver) {
            this.driver = driver;
            return this;
        }

        /**
         * Set WebDriver type.
         * @param driverInString WebDriver type in String (is not case sensitive)
         * @return this Builder instance
         */
        public Builder driver(String driverInString) {
            this.driver = Driver.valueOf(driverInString.toUpperCase());
            return this;
        }

        /**
         * Set remote address URL.
         * @param remoteAddress remote address URL
         * @return this Builder instance
         */
        public Builder remoteAddress(URL remoteAddress) {
            this.remoteAddress = remoteAddress;
            return this;
        }

        /**
         * Set remote address URL.
         * @param remoteAddressInString remote address URL in String
         * @return this Builder instance
         */
        public Builder remoteAddress(String remoteAddressInString) {
            try {
                this.remoteAddress = new URL(remoteAddressInString);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return this;
        }

        /**
         * Set remote browser name.
         * @param remoteBrowser remote browser name
         * @return this Builder instance
         */
        public Builder remoteBrowser(String remoteBrowser) {
            this.remoteBrowser = remoteBrowser;
            return this;
        }

        /**
         * Set TimeUnit for WebDriver waits.
         * @param timeUnit TimeUnit for WebDriver waits
         * @return this Builder instance
         */
        public Builder timeUnit(TimeUnit timeUnit) {
            this.timeUnit = timeUnit;
            return this;
        }

        /**
         * Set TimeUnit for WebDriver waits.
         * @param timeUnitInString TimeUnit for WebDriver waits in String (is not case sensitive)
         * @return this Builder instance
         */
        public Builder timeUnit(String timeUnitInString) {
            this.timeUnit = TimeUnit.valueOf(timeUnitInString.toUpperCase());
            return this;
        }

        /**
         * Set implicitly wait timeout.
         * @param implicitlyWait implicitly wait timeout
         * @return this Builder instance
         */
        public Builder implicitlyWait(long implicitlyWait) {
            this.implicitlyWait = implicitlyWait;
            return this;
        }

        /**
         * Set implicitly wait timeout.
         * @param implicitlyWaitInString implicitly wait timeout in String
         * @return this Builder instance
         */
        public Builder implicitlyWait(String implicitlyWaitInString) {
            this.implicitlyWait = Long.valueOf(implicitlyWaitInString);
            return this;
        }

        /**
         * Set page load timeout.
         * @param pageLoadTimeout page load timeout
         * @return this Builder instance
         */
        public Builder pageLoadTimeout(long pageLoadTimeout) {
            this.pageLoadTimeout = pageLoadTimeout;
            return this;
        }

        /**
         * Set page load timeout.
         * @param pageLoadTimeoutInString page load timeout in String
         * @return this Builder instance
         */
        public Builder pageLoadTimeout(String pageLoadTimeoutInString) {
            this.pageLoadTimeout = Long.valueOf(pageLoadTimeoutInString);
            return this;
        }

        /**
         * Set script timeout.
         * @param scriptTimeout script timeout
         * @return this Builder instance
         */
        public Builder scriptTimeout(long scriptTimeout) {
            this.scriptTimeout = scriptTimeout;
            return this;
        }

        /**
         * Set script timeout.
         * @param scriptTimeoutInString script timeout in String
         * @return this Builder instance
         */
        public Builder scriptTimeout(String scriptTimeoutInString) {
            this.scriptTimeout = Long.valueOf(scriptTimeoutInString);
            return this;
        }

        /**
         * Set default WebDriver fluent wait timeout.
         * <p>
         * Used in com.github.paulakimenko.webdriver.service.getDefWebDriverWait() method.
         * @param fluentWaitTimeout default WebDriver fluent wait timeout
         * @return this Builder instance
         */
        public Builder fluentWaitTimeout(long fluentWaitTimeout) {
            this.fluentWaitTimeout = fluentWaitTimeout;
            return this;
        }

        /**
         * Set default WebDriver fluent wait timeout.
         * <p>
         * Used in com.github.paulakimenko.webdriver.service.WDService.getDefWebDriverWait() method.
         * @param fluentWaitTimeoutInString default WebDriver fluent wait timeout in String
         * @return this Builder instance
         */
        public Builder fluentWaitTimeout(String fluentWaitTimeoutInString) {
            this.fluentWaitTimeout = Long.valueOf(fluentWaitTimeoutInString);
            return this;
        }

        /**
         * Set default WebDriver browser window size.
         * @param windowSize string param for browser window size
         * <p>
         * default - for default size
         * <p>
         * maximize - maximize to fullscreen
         * <p>
         * [x]x[y] - to custom resolution. Example : 1400x900
         * @return this Builder instance
         */
        public Builder windowSize(String windowSize) {
            this.windowSize = windowSize;
            return this;
        }

        /**
         * Build new WDProperties instance
         * @return new WDProperties instance
         */
        public WDProperties build() {
            return new WDProperties(this);
        }

        private static Map<String, String> getMapFromSuiteProps(ISuite suite, Set<String> paramsNames) {
            Map<String, String> map = new HashMap<>();

            for (String paramName : paramsNames) {
                String paramValue = suite.getParameter(paramName);
                if (!isNullOrEmpty(paramValue))
                    map.put(paramName, paramValue);
            }

            return map;
        }
    }
}

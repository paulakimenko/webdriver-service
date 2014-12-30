package com.github.paulakimenko.webdriver.service;

import com.google.common.collect.Sets;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.BrowserType;
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

/**
 * Implementation of WDCapabilities.
 * <p>
 * Used for WDService.
 */
public class WDDesiredCapabilities extends DesiredCapabilities implements WDCapabilities {
    public WDDesiredCapabilities(String browser, String version, Platform platform) {
        super(browser, version, platform);
    }

    /**
     * No-arg constructor.
     */
    public WDDesiredCapabilities() {
        super();
    }

    /**
     * Build from Java Properties.
     * @param properties given properties
     */
    public WDDesiredCapabilities(final Properties properties) {
        this(new HashMap<String, String>() {{
            for (String key : properties.stringPropertyNames())
                put(key, properties.getProperty(key));
        }});
    }

    /**
     * Build from Map String, String.
     * @param stringMap given map
     */
    public WDDesiredCapabilities(Map<String, String> stringMap) {
        Set<String> boolValueKeys = Sets.newHashSet(
                "takesScreenshot", "handlesAlerts", "cssSelectorsEnabled", "javascriptEnabled", "databaseEnabled",
                "locationContextEnabled", "applicationCacheEnabled", "browserConnectionEnabled", "webStorageEnabled",
                "acceptSslCerts", "rotatable", "nativeEvents", "webdriver.remote.quietExceptions", "opera.guess_binary_path",
                "opera.no_restart", "opera.no_quit", "opera.autostart", "opera.idle", "ignoreProtectedModeSettings",
                "ignoreZoomSetting", "enablePersistentHover", "enableElementCacheCleanup", "requireWindowFocus",
                "ie.forceCreateProcessApi", "ie.usePerProcessProxy", "ie.ensureCleanSession", "silent",
                "ie.setProxyByServer", "cleanSession", "skipExtensionInstallation", "webdriver_accept_untrusted_certs",
                "webdriver_assume_untrusted_issuer"
        );
        Set<String> intValueKeys = Sets.newHashSet(
                "elementScrollBehavior", "maxInstances", "opera.display", "opera.port", "browserAttachTimeout",
                "webdriver_firefox_port"
        );

        for (Map.Entry<String, String> entry : stringMap.entrySet()) {
            if (boolValueKeys.contains(entry.getKey())) {
                this.setCapability(entry.getKey(), Boolean.valueOf(entry.getValue()));
            } else if (intValueKeys.contains(entry.getKey())) {
                this.setCapability(entry.getKey(), Integer.valueOf(entry.getValue()));
            } else {
                this.setCapability(entry.getKey(), entry.getValue());
            }
        }
    }

    public void setRemote(boolean remote) {
        setCapability(WDCapabilityType.REMOTE, remote);
    }

    @Override
    public boolean isRemote() {
        return is(WDCapabilityType.REMOTE);
    }

    public void setHubUrl(URL hubUrl) {
        setCapability(WDCapabilityType.HUB_URL, String.valueOf(hubUrl));
    }

    @Override
    public URL getHubUrl() {
        String urlInString = String.valueOf(getCapability(WDCapabilityType.HUB_URL));
        try {
            return new URL(urlInString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        setCapability(WDCapabilityType.TIME_UNIT, timeUnit.toString());
    }

    @Override
    public TimeUnit getTimeUnit() {
        Object capability = getCapability(WDCapabilityType.TIME_UNIT);
        return capability == null
                ? TimeUnit.SECONDS
                : TimeUnit.valueOf(String.valueOf(getCapability(WDCapabilityType.TIME_UNIT)).toUpperCase());
    }

    public void setImplicitlyWait(long implicitlyWait) {
        setCapability(WDCapabilityType.IMPLICITLY_WAIT, String.valueOf(implicitlyWait));
    }

    @Override
    public long getImplicitlyWait() {
        return getDigitsFromString(String.valueOf(getCapability(WDCapabilityType.IMPLICITLY_WAIT)));
    }

    public void setPageLoadTimeout(long pageLoadTimeout) {
        setCapability(WDCapabilityType.PAGE_LOAD_TIMEOUT, String.valueOf(pageLoadTimeout));
    }

    @Override
    public long getPageLoadTimeout() {
        return getDigitsFromString(String.valueOf(getCapability(WDCapabilityType.PAGE_LOAD_TIMEOUT)));
    }

    public void setScriptTimeout(long scriptTimeout) {
        setCapability(WDCapabilityType.SCRIPT_TIMEOUT, String.valueOf(scriptTimeout));
    }

    @Override
    public long getScriptTimeout() {
        return getDigitsFromString(String.valueOf(getCapability(WDCapabilityType.SCRIPT_TIMEOUT)));
    }

    public void setFluentWaitTimeout(long fluentWaitTimeout) {
        setCapability(WDCapabilityType.FLUENT_WAIT_TIMEOUT, String.valueOf(fluentWaitTimeout));
    }

    @Override
    public long getFluentWaitTimeout() {
        return getDigitsFromString(String.valueOf(getCapability(WDCapabilityType.FLUENT_WAIT_TIMEOUT)));
    }

    public void setWindow(Window window) {
        setCapability(WDCapabilityType.WINDOW, window.toString());
    }

    @Override
    public Window getWindow() {
        Object capability = getCapability(WDCapabilityType.WINDOW);
        return Window.valueOf(String.valueOf(capability == null ? "" : capability));
    }

    @Override
    public WDDesiredCapabilities merge(Capabilities capabilities) {
        super.merge(capabilities);
        return this;
    }

    /**
     * Get default capabilities.
     * @return capabilities by default
     */
    public static WDDesiredCapabilities getDefault() {
        WDDesiredCapabilities wdCapabilities = new WDDesiredCapabilities(BrowserType.FIREFOX, "", Platform.ANY);
        wdCapabilities.setCapability(WDCapabilityType.REMOTE, false);
        wdCapabilities.setCapability(WDCapabilityType.HUB_URL, "http://localhost:4444/wd/hub");
        wdCapabilities.setCapability(WDCapabilityType.TIME_UNIT, "seconds");
        wdCapabilities.setCapability(WDCapabilityType.IMPLICITLY_WAIT, "10");
        wdCapabilities.setCapability(WDCapabilityType.PAGE_LOAD_TIMEOUT, "10");
        wdCapabilities.setCapability(WDCapabilityType.SCRIPT_TIMEOUT, "10");
        wdCapabilities.setCapability(WDCapabilityType.FLUENT_WAIT_TIMEOUT, "10");
        wdCapabilities.setCapability(WDCapabilityType.WINDOW, "default");
        return wdCapabilities;
    }

    /**
     * Get Capabilities from system properties.
     * @return capabilities from system properties
     */
    public static WDDesiredCapabilities getFromSystemProperties() {
        return new WDDesiredCapabilities(System.getProperties());
    }

    /**
     * Get from TestNG's ITestContext.
     * @param context given context
     * @param parameterKeys set of expected parameters
     * @return capabilities from ITestContext
     */
    public static WDDesiredCapabilities getFromITestContext(ITestContext context, Set<String> parameterKeys) {
        WDDesiredCapabilities capabilities = new WDDesiredCapabilities();
        ISuite suite = context.getSuite();
        for (String parameterKey : parameterKeys) {
            capabilities.setCapability(parameterKey, suite.getParameter(parameterKey));
        }
        return capabilities;
    }

    private static long getDigitsFromString(String string) {
        String digitsInStr = string.replaceAll("\\D+","");
        if ("".equals(digitsInStr))
            return 0;
        return Long.parseLong(digitsInStr);
    }
}

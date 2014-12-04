[WebDriver]: http://docs.seleniumhq.org/projects/webdriver/
[TestNG]: http://testng.org/doc/index.html

#WebDriver Service

Selenium [WebDriver] service. Provides needed [WebDriver] instance with base properties. Uses different ways to configure.
See JavaDocs here (TBD).

Maven dependency

```xml
<dependency>
    <groupId>com.github.paulakimenko</groupId>
    <artifactId>webdriver-service</artifactId>
    <version>0.1</version>
</dependency>
```

##Supported drivers:

 - Selenium Remote WebDriver;
 - Firefox Driver;
 - Chrome Driver;
 - Safari Driver;
 - Internet Explorer Driver;
 - HTML Unit Driver;
 - PhantomJS Driver;

##Features:

 - Thread Local instance;
 - Building WebDriver settings with WDProperties;
 - Loading of WebDriver settings from different sources ([TestNG]'s ITestContext, Java Properties, System Properties, Map);
 - Wrapping current global WebDriver instance "on the fly";
 - Providing JavascriptExecutor instance;
 - Providing TakesScreenshot instance;
 - Providing default WebDriverWait instance with timeout value from Properties;
 - LocalFileDetector and Augmenter are enabled in RemoteWebDriver by default;

##How-to:

Basic example:

```java
    import com.github.paulakimenko.webdriver.service.WDService;
    import com.github.paulakimenko.webdriver.service.WDServiceProvider;
    import org.openqa.selenium.By;
    import org.openqa.selenium.WebDriver;
    import org.testng.annotations.AfterTest;
    import org.testng.annotations.BeforeTest;
    import org.testng.annotations.Test;

    public class ExampleTest {
        private static WDService service = WDServiceProvider.getInstance();

        @BeforeTest
        public void setUpTest() {
            service.init();
        }

        @Test
        public void test() {
            WebDriver driver = service.getDriver();
            driver.get("http://url.com/");
            driver.findElement(By.id("id")).click();
        }

        @AfterTest
        public void tearDownTest() {
            service.terminate();
        }
    }
```

Using Capabilities in initialization:

```java
    WDService service = WDServiceProvider.getInstance();
    service.init(new DesiredCapabilities());
```

Explicit usage of Properties:

```java
    WDProperties properties = new WDProperties.Builder()
            .driver(Driver.CHROME)
            .implicitlyWait(5)
            .fluentWaitTimeout(10)
            .timeUnit(TimeUnit.SECONDS)
            .build();

    WDService service = WDServiceProvider.getInstance();
    service.setProperties(properties);
    service.init(DesiredCapabilities.chrome());
```

Additional instances example:

```java
    WDService service = WDServiceProvider.getInstance();
    service.init();
    JavascriptExecutor javascriptExecutor = service.getJsExecutor();
    TakesScreenshot takesScreenshot = service.getScreenshotMaker();
    Wait<WebDriver> wait = service.getDefWebDriverWait();
```

Wrapping example:

```java
    WDService service = WDServiceProvider.getInstance();
    service.init();
    service.wrapWith(SomeWebDriverWrapper.class);
```

Another wrapping example (more complex):

```java
    WDService service = WDServiceProvider.getInstance();
    service.init();
    service.wrapWith(new Function<WebDriver, WrapsDriver>() {
        @Override
        public WrapsDriver apply(WebDriver driver) {
            return new SomeWebDriverWrapper(driver, [some args...]);
        }
    });
```

## Contact
Mail: [paulakimenko@gmail.com](mailto:paulakimenko@gmail.com)

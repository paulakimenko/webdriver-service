webdriver-service
=================

Selenium WebDriver service. Provides needed WebDriver instance with base properties. Uses different ways to configure.

###Supported drivers:

 - Selenium Remote WebDriver;
 - Firefox Driver;
 - Chrome Driver;
 - Safari Driver;
 - Internet Explorer Driver;
 - HTML Unit Driver;
 - PhantomJS Driver;

###Features:

 - Thread Local instance;
 - Building WebDriver settings with WDProperties;
 - Loading of WebDriver settings from different sources (TestNG's ITestContext, Java Properties, System Properties, Map);
 - Wrapping current global WebDriver instance "on the fly";
 - Providing JavascriptExecutor instance;
 - Providing TakesScreenshot instance;
 - Providing default WebDriverWait instance with timeout value from Properties;

###How-to:

Basic example:

'''java

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

'''
package solvd.webtest;

import com.solvd.webtest.util.Config;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;

import java.util.List;

public abstract class AbstractTest {
    static {
        System.setProperty("log4j.configurationFile", "src/main/resources/log4j2.xml");
    }

    protected static Logger LOGGER;
    protected ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public AbstractTest() {
        LOGGER = LoggerFactory.getLogger(getClass());
    }

    @BeforeMethod
    @Parameters("browser")
    public void setup(String browser) {
        if (browser.equals("chrome")) {
            ChromeOptions options = new ChromeOptions();

            // Adding argument to disable the AutomationControlled flag
            options.addArguments("--disable-blink-features=AutomationControlled", "--incognito", "--start-maximized");

            // Exclude the collection of enable-automation switches
            options.setExperimentalOption("excludeSwitches", List.of("enable-automation"));

            // Turn-off userAutomationExtension
            options.setExperimentalOption("useAutomationExtension", false);

            driver.set(new ChromeDriver(options));

            ((JavascriptExecutor) driver.get()).executeScript("Object.defineProperty(navigator, 'webdriver', {get: () => undefined})");
        } else if (browser.equals("firefox"))
            driver.set(new FirefoxDriver());

        driver.get().manage().window().maximize();

        LOGGER.info("Setting {} driver", getBrowserDetails());

        Config.loadFile("lol");
    }

    @AfterTest
    public abstract void dispose(ITestResult result);

    public String getBrowserDetails() {
        Capabilities cap = ((RemoteWebDriver) driver.get()).getCapabilities();

        String browserName = cap.getBrowserName();
        String browserVersion = (String) cap.getCapability("browserVersion");
        String osName = cap.getCapability("platformName").toString();

        return browserName + " " + browserVersion + " " + osName;
    }
}

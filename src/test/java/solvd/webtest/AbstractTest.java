package solvd.webtest;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

public abstract class AbstractTest {
    static {
        System.setProperty("log4j.configurationFile", "src/main/resources/log4j2.xml");
    }

    protected static Logger LOGGER;
    protected ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public AbstractTest() {
        LOGGER = LoggerFactory.getLogger(getClass());
    }

    @BeforeTest
    public abstract void setup(String browser);

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

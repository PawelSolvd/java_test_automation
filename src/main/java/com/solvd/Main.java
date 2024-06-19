package com.solvd;

import org.openqa.selenium.ImmutableCapabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class Main {
    private static Properties props = new Properties();
    public static void main(String[] args) throws MalformedURLException {
//        try {
//            props.load(new FileInputStream("src/main/resources/_config.properties"));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

        ImmutableCapabilities capabilities = new ImmutableCapabilities(props);

        ChromeOptions options = new ChromeOptions();
        // Adding argument to disable the AutomationControlled flag
        options.addArguments("--disable-blink-features=AutomationControlled", "--incognito","--start-maximized") ;

        // Exclude the collection of enable-automation switches
        options.setExperimentalOption("excludeSwitches", List.of("enable-automation")) ;

        // Turn-off userAutomationExtension
        options.setExperimentalOption("useAutomationExtension", false);

        //driver = new RemoteWebDriver(new URL("http://127.0.0.1:4444"), capabilities);

        WebDriver driver = new RemoteWebDriver(new URL("https://127.0.0.1:4444"), options);
        ((JavascriptExecutor)driver).executeScript("Object.defineProperty(navigator, 'webdriver', {get: () => undefined})");

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(4));
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(3));

        driver.get("https://www.ebay.com");

        //driver.quit();
    }
}
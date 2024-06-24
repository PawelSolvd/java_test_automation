package com.solvd.webtest.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public abstract class BasePage {
    protected WebDriver driver;
    protected String url;

    protected static Logger LOGGER;


    public BasePage(WebDriver driver, String url) {
        this.driver = driver;
        this.url = url;
        LOGGER = LoggerFactory.getLogger(getClass());
    }

    public void open() {
        driver.get(url);
    }

    public boolean isOpen() {
        LOGGER.info("Url = {}", url);
        LOGGER.info("DriverUrl = {}", driver.getCurrentUrl());

        WebElement body = driver.findElement(By.tagName("body"));
        new WebDriverWait(driver, Duration.ofMillis(500))
                .pollingEvery(Duration.ofMillis(50))
                .withMessage("Page " + driver.getCurrentUrl() + " not loaded properly")
                .until(d -> body.isDisplayed());

        return driver.getCurrentUrl().equals(url);
    }
}

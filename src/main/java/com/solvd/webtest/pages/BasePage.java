package com.solvd.webtest.pages;

import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public abstract class BasePage extends AbstractPage {
    protected String url;

    protected static Logger LOGGER;

    public BasePage(WebDriver driver, String url) {
        super(driver);
        this.url = url;
        setPageAbsoluteURL(url);
        LOGGER = LoggerFactory.getLogger(getClass());
    }

    public boolean isOpen() {
        LOGGER.info("Url = {}", url);
        LOGGER.info("DriverUrl = {}", getDriver().getCurrentUrl());

        WebElement body = getDriver().findElement(By.tagName("body"));
        new WebDriverWait(getDriver(), Duration.ofMillis(500))
                .pollingEvery(Duration.ofMillis(50))
                .withMessage("Page " + getDriver().getCurrentUrl() + " not loaded properly")
                .until(d -> body.isDisplayed());

        return getDriver().getCurrentUrl().equals(url);
    }
}

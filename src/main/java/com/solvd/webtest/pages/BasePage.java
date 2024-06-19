package com.solvd.webtest.pages;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        return driver.getCurrentUrl().equals(url);
    }
}

package com.solvd.webtest.pages;

import org.openqa.selenium.WebDriver;

public class CategoriesPage extends BasePage {
    public CategoriesPage(WebDriver driver) {
        super(driver, "https://www.ebay.com/n/all-categories");
    }

    @Override
    public boolean isOpen() {
        super.isOpen();
        return driver.getCurrentUrl().startsWith(url);
    }
}

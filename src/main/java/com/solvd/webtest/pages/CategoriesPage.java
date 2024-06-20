package com.solvd.webtest.pages;

import com.solvd.webtest.util.Config;
import org.openqa.selenium.WebDriver;

public class CategoriesPage extends BasePage {
    public CategoriesPage(WebDriver driver) {
        super(driver, Config.get("categoriesPage.url"));
    }

    @Override
    public boolean isOpen() {
        super.isOpen();
        return driver.getCurrentUrl().startsWith(url);
    }
}

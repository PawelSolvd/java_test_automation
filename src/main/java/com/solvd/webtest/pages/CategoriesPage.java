package com.solvd.webtest.pages;

import com.zebrunner.carina.utils.R;
import org.openqa.selenium.WebDriver;

public class CategoriesPage extends BasePage {
    public CategoriesPage(WebDriver driver) {
        super(driver, R.CONFIG.get("categoriesPage.url"));
    }

    @Override
    public boolean isOpen() {
        super.isOpen();
        return getDriver().getCurrentUrl().startsWith(url);
    }
}

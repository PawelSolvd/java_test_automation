package com.solvd.webtest.pages;

import com.solvd.webtest.util.Config;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

public class HomePage extends BasePage {
    @FindBy(id = "gh-eb-My")
    private WebElement myEbayButton;

    @FindBy(id = "gh-ac")
    private WebElement searchField;

    //@FindBy(id = "gh-cat")
    private Select searchCategorySelect;

    @FindBy(id = "gh-btn")
    private WebElement searchButton;

    public HomePage(WebDriver driver) {
        super(driver, Config.get("homePage.url"));
        PageFactory.initElements(driver, this);
    }

    @Override
    public void open() {
        super.open();
        searchCategorySelect = new Select(driver.findElement(By.id("gh-cat")));
    }

    public LoginPage clickMyEbay() {
        myEbayButton.click();
        return new LoginPage(driver);
    }

    public SearchResultPage search(String query, String category) {
        searchField.sendKeys(query);

        if (searchCategorySelect.getOptions().stream().map(WebElement::getText).toList().contains(category))
            searchCategorySelect.selectByVisibleText(category);
        searchButton.click();

        LOGGER.info("Searching for '{}' in '{}'", query, category);

        return new SearchResultPage(driver);
    }
}

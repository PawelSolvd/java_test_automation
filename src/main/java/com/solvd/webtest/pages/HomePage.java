package com.solvd.webtest.pages;

import com.zebrunner.carina.utils.R;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import java.time.Duration;

public class HomePage extends BasePage {
    @FindBy(id = "gh-eb-My")
    private ExtendedWebElement myEbayButton;

    @FindBy(id = "gh-ac")
    private ExtendedWebElement searchField;

    @FindBy(id = "gh-cat")
    private ExtendedWebElement searchCategorySelect;

    @FindBy(id = "gh-btn")
    private ExtendedWebElement searchButton;

    public HomePage(WebDriver driver) {
        super(driver, R.CONFIG.get("homePage.url"));
    }

    @Override
    public void open() {
        super.open();
        //searchCategorySelect = new Select(driver.findElement(By.id("gh-cat")));
    }

    public LoginPage clickMyEbay() {
        if (myEbayButton.isElementPresent(Duration.ofMillis(100)))
            myEbayButton.click();
        return new LoginPage(getDriver());
    }

    public SearchResultPage search(String query, String category) {
        searchField.type(query);

        // new WebDriverWait(driver, Duration.ofMillis(500)).until(d -> searchCategorySelect.getWrappedElement().isDisplayed());
        // if (searchCategorySelect.getOptions().stream().map(WebElement::getText).toList().contains(category))

        try {
            if (searchCategorySelect.isElementPresent(Duration.ofMillis(100)))
                searchCategorySelect.select(category);
            searchButton.click();

            LOGGER.info("Searching for '{}' in '{}'", query, category);

        } catch (Exception e) {
            LOGGER.warn("Caught exception {}", e.toString());
        }
        return new SearchResultPage(getDriver());
    }
}

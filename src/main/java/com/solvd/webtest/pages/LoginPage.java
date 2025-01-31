package com.solvd.webtest.pages;

import com.solvd.webtest.util.Config;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage extends BasePage {
    @FindBy(id = "userid")
    private WebElement usernameField;

    @FindBy(id = "signin-continue-btn")
    private WebElement continueButton;

    public LoginPage(WebDriver driver) {
        super(driver, Config.get("loginPage.url"));
        PageFactory.initElements(driver, this);
    }

    @Override
    public boolean isOpen() {
        super.isOpen();
        return driver.getCurrentUrl().startsWith(url);
    }

    public boolean tryLogin(String username) {
        new WebDriverWait(driver, Duration.ofSeconds(5)).until(d -> usernameField.isDisplayed());

        usernameField.sendKeys(username);
        continueButton.click();

        if (!driver.findElements(By.id("signin-error-msg")).isEmpty())
            return false;
        else if (!driver.findElements(By.id("user-info")).isEmpty() && driver.findElement(By.id("user-info")).getText().equals(username))
            return true;

        LOGGER.warn("Sign in problems");
        return false;
    }
}

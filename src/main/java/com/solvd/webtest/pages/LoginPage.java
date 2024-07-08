package com.solvd.webtest.pages;

import com.zebrunner.carina.utils.R;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import java.time.Duration;

public class LoginPage extends BasePage {
    @FindBy(id = "userid")
    private ExtendedWebElement usernameField;

    @FindBy(id = "signin-continue-btn")
    private ExtendedWebElement continueButton;

    @FindBy(id = "signin-error-msg")
    private ExtendedWebElement signinError;

    @FindBy(id = "user-info")
    private ExtendedWebElement userInfo;

    public LoginPage(WebDriver driver) {
        super(driver, R.CONFIG.get("loginPage.url"));
        setUiLoadedMarker(usernameField);
    }

    @Override
    public boolean isOpen() {
        super.isOpen();
        return getDriver().getCurrentUrl().startsWith(url);
    }

    public boolean tryLogin(String username) {
        if (usernameField.isElementPresent(Duration.ofMillis(100)))
            usernameField.type(username);
        continueButton.click();

        if (signinError.isElementPresent(Duration.ofMillis(1000)))
            return false;
        else if (userInfo.isElementPresent(Duration.ofMillis(1000)) && userInfo.getText().equals(username))
            return true;

        LOGGER.warn("Unexpected sign in problems");
        return false;
    }
}

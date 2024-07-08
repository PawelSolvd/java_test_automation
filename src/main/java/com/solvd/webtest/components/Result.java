package com.solvd.webtest.components;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractUIObject;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import java.time.Duration;

public class Result extends AbstractUIObject {
    @FindBy(className = "s-item__title")
    private ExtendedWebElement title;

    @FindBy(className = "s-item__price")
    private ExtendedWebElement price;

    @FindBy(className = "s-item__location")
    private ExtendedWebElement location;

    @FindBy(className = "s-item__shipping")
    private ExtendedWebElement shipping;

    @FindBy(css = ".s-item__sep > span")
    private ExtendedWebElement sponsored;

    public Result(WebDriver driver, SearchContext searchContext) {
        super(driver, searchContext);
    }

    public String getItemTitle() {
        if (title.isElementPresent(Duration.ofMillis(100)))
            return title.getText();
        else
            return "missing";
    }

    public String getPrice() {
        if (!price.isElementPresent(Duration.ofMillis(100)))
            return "0";

        String pr = price.getText().substring(1).replace(",", "");
        if (pr.isEmpty())
            return "0";

        return pr.substring(Math.max(pr.indexOf('$') + 1, 0));
    }

    public String getItemLocation() {
        if (location.isElementPresent(Duration.ofMillis(100))) {
            String loc = location.getElement().getText();

            if (!loc.isEmpty())
                return loc.substring(5);
        }

        return "missing";
    }

    public String getShipping() {
        if (!shipping.isElementPresent(Duration.ofMillis(100)))
            return "0";

        String shp = shipping.getText().replace(",", "");

        if (!shp.isEmpty())
            if (shp.charAt(0) == '+')
                return shp.substring(2, shp.indexOf(' '));

        return "0";
    }

    public String getSponsored() {
        if (sponsored.isElementPresent(Duration.ofMillis(1)))
            return sponsored.getElement().getDomAttribute("style");

        return "missing";
    }

    // toString() causes stack overflow when element is missing
    public String print() {
        return getItemTitle() + " " + getPrice() + " " + getItemLocation() + " " + getShipping() + " " + getSponsored();
    }
}

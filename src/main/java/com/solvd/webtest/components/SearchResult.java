package com.solvd.webtest.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.ArrayList;
import java.util.List;

public class SearchResult {
    private static WebDriver driver;

    @FindBy(css = ".srp-results .s-item__wrapper")
    private List<WebElement> entries;

    private List<Result> results = new ArrayList<>();

    public static class Result {
        @FindBy(className = "s-item__title")
        private WebElement title;

        @FindBy(className = "s-item__price")
        private WebElement price;

        @FindBy(className = "s-item__location")
        private WebElement location;

        // @FindBy(className = "s-item__shipping")
        private WebElement shipping;

        @FindBy(css = ".s-item__sep > span")
        private WebElement sponsored;

        public Result(WebElement element) {
            PageFactory.initElements(element, this);

            if (element.findElements(By.cssSelector(".s-item__shipping")).isEmpty())
                shipping = null;
            else
                shipping = element.findElement(By.cssSelector(".s-item__shipping"));
        }

        public Result(Result other) {
            this.title = other.title;
            this.price = other.price;
            this.location = other.location;
            this.shipping = other.shipping;
            this.sponsored = other.sponsored;
        }

        public String getTitle() {
            if (title == null)
                return "";
            return title.getText();
        }

        public String getPrice() {
            if (price == null)
                return "0";
            String pr = price.getText().substring(1).replace(",", "");
            if (pr.isEmpty())
                return pr;

            return pr.substring(Math.max(pr.indexOf('$') + 1, 0), ((pr.indexOf('$') < 0)) ? pr.length() : pr.length());
        }

        public String getLocation() {
            if (location == null)
                return "";

            String loc = location.getText();

            if (!loc.isEmpty())
                return loc.substring(5);

            return loc;
        }

        public String getShipping() {
            if (shipping == null)
                return "0";

            String shp = shipping.getText().replace(",", "");

            if (!shp.isEmpty())
                if (shp.charAt(0) == '+')
                    return shp.substring(2, shp.indexOf(' '));

            return "0";
        }

        public String getSponsored() {
            if (sponsored != null)
                return sponsored.getDomAttribute("style");

            return "";
        }

        @Override
        public String toString() {
            return getTitle() + " " + getPrice() + " " + getLocation() + " " + getShipping() + " " + getSponsored();
        }
    }

    public SearchResult(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;

        if (!entries.isEmpty())
            for (var e : entries)
                results.add(new Result(e));

        //entries.clear();
    }

    public List<Result> getResults() {
        return results;
    }
}

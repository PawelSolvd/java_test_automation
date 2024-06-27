package com.solvd.webtest.pages;

import com.solvd.webtest.components.Result;
import com.zebrunner.carina.utils.R;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import java.util.*;

public class SearchResultPage extends BasePage {
    @FindBy(css = ".srp-results .s-item__wrapper")
    private List<Result> results;

    @FindBy(xpath = "(//button[@class='fake-menu-button__button btn btn--small btn--secondary'])[4]")
    private ExtendedWebElement sortingMenuBtn;

    @FindBy(xpath = "(//ul[@class='fake-menu__items'])[4]//span")
    private List<ExtendedWebElement> sortOptions;

    public SearchResultPage(WebDriver driver) {
        super(driver, R.CONFIG.get("searchResultPage.url"));
    }

    @Override
    public boolean isOpen() {
        super.isOpen();
        return getDriver().getCurrentUrl().startsWith(url);
    }

    public void printResults() {
        for (var r : results)
            LOGGER.info(r.print());
    }

    public List<Result> getResults() {
        return results;
    }

    public void setSortOption(String option) {
        sortingMenuBtn.click();

        if (!sortOptions.isEmpty())
            for (var o : sortOptions)
                if (o.getText().equals(option)) {
                    o.click();
                    results.clear();
                    for (var e : findExtendedWebElements(By.cssSelector(".srp-results .s-item__wrapper")))
                        results.add(new Result(driver, e.getSearchContext()));
                    break;
                }
    }

    public List<Result> getNoSponsoredResults() {
        Map<String, List<Result>> resultsGrouped = new HashMap<>();

        for (var r : results) {
            resultsGrouped.computeIfAbsent(r.getSponsored(), k -> new ArrayList<>());
            resultsGrouped.get(r.getSponsored()).add(r);
        }

        return resultsGrouped.values().stream().max(Comparator.comparing(List::size)).orElse(new ArrayList<>());
    }
}

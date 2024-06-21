package com.solvd.webtest.pages;

import com.solvd.webtest.components.SearchResult;
import com.solvd.webtest.util.Config;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.*;

public class SearchResultPage extends BasePage {
    private List<SearchResult.Result> results = new ArrayList<>();

    @FindBy(xpath = "(//button[@class='fake-menu-button__button btn btn--small btn--secondary'])[4]")
    private WebElement sortingMenuBtn;

    @FindBy(xpath = "(//ul[@class='fake-menu__items'])[4]//span")
    private List<WebElement> sortOptions;

    public SearchResultPage(WebDriver driver) {
        super(driver, Config.get("searchResultPage.url"));
        PageFactory.initElements(driver, this);
        
        for (var r : new SearchResult(driver).getResults())
            results.add(new SearchResult.Result(r));
    }

    @Override
    public boolean isOpen() {
        super.isOpen();
        return driver.getCurrentUrl().startsWith(url);
    }

    public void printResults() {
        for (var r : results)
            LOGGER.info(r.toString());
    }

    public List<SearchResult.Result> getResults() {
        return results;
    }

    public void setSortOption(String option) {
        sortingMenuBtn.click();

        if (!sortOptions.isEmpty())
            for (var o : sortOptions)
                if (o.getText().equals(option)) {
                    o.click();
                    results.clear();
                    for (var r : new SearchResult(driver).getResults())
                        results.add(new SearchResult.Result(r));
                    break;
                }
    }

    public List<SearchResult.Result> getNoSponsoredResults() {
        Map<String, List<SearchResult.Result>> resultsGrouped = new HashMap<>();

        for (var r : results) {
            resultsGrouped.computeIfAbsent(r.getSponsored(), k -> new ArrayList<>());
            resultsGrouped.get(r.getSponsored()).add(r);
        }

        return resultsGrouped.values().stream().max(Comparator.comparing(List::size)).orElse(new ArrayList<>());
    }
}

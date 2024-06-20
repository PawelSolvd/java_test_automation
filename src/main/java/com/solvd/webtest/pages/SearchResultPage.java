package com.solvd.webtest.pages;

import com.google.common.collect.Iterables;
import com.solvd.webtest.components.SearchResult;
import com.solvd.webtest.util.Config;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchResultPage extends BasePage {
    private List<SearchResult.Result> results = new ArrayList<>();
    private SearchResult searchResult;

    @FindBy(xpath = "(//button[@class='fake-menu-button__button btn btn--small btn--secondary'])[4]")
    private WebElement sortingMenuBtn;

    @FindBy(xpath = "(//ul[@class='fake-menu__items'])[4]//span")
    private List<WebElement> sortOptions;

    public SearchResultPage(WebDriver driver) {
        super(driver, Config.get("searchResultPage.url"));
        PageFactory.initElements(driver, this);

        searchResult = new SearchResult(driver);

        for (var r : searchResult.getResults())
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

        int index = 0;
        for (int i = 1; i < resultsGrouped.values().size(); i++)
            if (Iterables.get(resultsGrouped.values(), i).size() > Iterables.get(resultsGrouped.values(), index).size())
                index = i;

        return Iterables.get(resultsGrouped.values(), index);
    }
}

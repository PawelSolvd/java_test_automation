package solvd.webtest;

import com.solvd.webtest.components.SearchResult;
import com.solvd.webtest.pages.CategoriesPage;
import com.solvd.webtest.pages.HomePage;
import com.solvd.webtest.pages.LoginPage;
import com.solvd.webtest.pages.SearchResultPage;
import com.solvd.webtest.util.Config;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.testng.Assert.*;

public class HomePageTest extends AbstractTest {
    @Test
    public void emptyLoginTest() {
        HomePage homePage = new HomePage(driver.get());
        homePage.open();
        assertTrue(homePage.isOpen(), "HomePage is not opened");

        LoginPage loginPage = homePage.clickMyEbay();
        assertTrue(loginPage.isOpen(), "LoginPage is not opened");

        assertFalse(loginPage.tryLogin(""), "Missing login error message");
    }

    @Test
    public void validLoginTest() {
        HomePage homePage = new HomePage(driver.get());
        homePage.open();
        assertTrue(homePage.isOpen(), "HomePage is not opened");

        LoginPage loginPage = homePage.clickMyEbay();
        assertTrue(loginPage.isOpen(), "LoginPage is not opened");

        assertTrue(loginPage.tryLogin(Config.get("user.login")), "Login error");
    }

    @DataProvider(name = "searchData")
    public Object[][] searchDataProvider() {
        return new Object[][]{
                {"iphone", "Art"},
                {"ipad", "Books"}
        };
    }

    @Test(dataProvider = "searchData", threadPoolSize = 4, invocationCount = 1)
    public void validSearchWithCategoryTest(String query, String category) {
        HomePage homePage = new HomePage(driver.get());
        homePage.open();
        assertTrue(homePage.isOpen(), "HomePage is not opened");

        SearchResultPage searchResultPage = homePage.search(query, category);
        assertTrue(searchResultPage.isOpen(), "SearchResultPage is not opened");

        List<SearchResult.Result> results = searchResultPage.getResults();

        assertFalse(results.isEmpty(), "No search results");
        searchResultPage.printResults();

        List<SearchResult.Result> notMatching = new ArrayList<>();
        assertTrue(
                results.stream()
                        .allMatch(r -> {
                            if (!r.getTitle().toLowerCase().contains(query.toLowerCase())) {
                                notMatching.add(r);
                                return false;
                            } else return true;
                        }) || ((double) notMatching.size() / results.size() <= 0.1), "Product title not containing query" + notMatching);
    }

    @Test
    public void emptySearchTest() {
        HomePage homePage = new HomePage(driver.get());
        homePage.open();
        assertTrue(homePage.isOpen(), "HomePage is not opened");

        homePage.search("", "");
        assertTrue(new CategoriesPage(driver.get()).isOpen(), "CategoriesPage is not opened");
    }

    @Test(dataProvider = "searchData")
    public void searchSortPriceDescTest(String query, String category) {
        HomePage homePage = new HomePage(driver.get());
        homePage.open();
        assertTrue(homePage.isOpen(), "HomePage is not opened");

        SearchResultPage searchResultPage = homePage.search(query, category);
        assertTrue(searchResultPage.isOpen(), "SearchResultPage is not opened");

        List<SearchResult.Result> results = searchResultPage.getResults();
        assertFalse(results.isEmpty(), "No search results");

        searchResultPage.setSortOption("Price + Shipping: highest first");
        results = searchResultPage.getNoSponsoredResults();
        //searchResultPage.printResults();

        List<Double> prices = results.stream().map(r -> Double.parseDouble(r.getPrice()) + Double.parseDouble(r.getShipping())).toList();

        LOGGER.info("Non sponsored prices count = {}", prices.size());
        prices.forEach(p -> LOGGER.info(p.toString()));

        assertEquals(prices.stream().sorted(Comparator.reverseOrder()).toList(), prices, "Results sorted incorrectly");
    }
}

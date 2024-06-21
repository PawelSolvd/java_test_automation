package solvd.webtest;

import com.solvd.webtest.components.SearchResult;
import com.solvd.webtest.pages.CategoriesPage;
import com.solvd.webtest.pages.HomePage;
import com.solvd.webtest.pages.LoginPage;
import com.solvd.webtest.pages.SearchResultPage;
import com.solvd.webtest.util.Config;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Clock;
import java.util.Comparator;
import java.util.List;

public class HomePageTest extends AbstractTest {
    @AfterMethod
    public void dispose(ITestResult result) {
        if (!result.isSuccess()) {
            File file = ((TakesScreenshot) driver.get()).getScreenshotAs(OutputType.FILE);
            try {
                Files.copy(file.toPath(), Path.of("screenshots/img" + Clock.systemUTC().millis() + ".png"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        //driver.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
        driver.get().close();

        LOGGER.info("Closing {} driver", getBrowserDetails());
    }

    @Test
    public void emptyLoginTest() {
        HomePage homePage = new HomePage(driver.get());
        homePage.open();
        Assert.assertTrue(homePage.isOpen());

        LoginPage loginPage = homePage.clickMyEbay();
        //driver.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
        Assert.assertTrue(loginPage.isOpen());

        Assert.assertFalse(loginPage.tryLogin(""));
    }

    @Test
    public void validLoginTest() {
        HomePage homePage = new HomePage(driver.get());
        homePage.open();
        Assert.assertTrue(homePage.isOpen());

        LoginPage loginPage = homePage.clickMyEbay();
        //driver.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
        Assert.assertTrue(loginPage.isOpen());

        Assert.assertTrue(loginPage.tryLogin(Config.get("user.login")));
    }

    @Test(threadPoolSize = 4, invocationCount = 1)
    public void validSearchWithCategoryTest() {
        HomePage homePage = new HomePage(driver.get());
        homePage.open();
        Assert.assertTrue(homePage.isOpen());

        SearchResultPage searchResultPage = homePage.search(Config.get("search.query"), Config.get("search.category"));
        Assert.assertTrue(searchResultPage.isOpen());

        Assert.assertFalse(searchResultPage.getResults().isEmpty());
        searchResultPage.printResults();
    }

    @Test
    public void emptySearchTest() {
        HomePage homePage = new HomePage(driver.get());
        homePage.open();
        Assert.assertTrue(homePage.isOpen());

        homePage.search("", "");
        Assert.assertTrue(new CategoriesPage(driver.get()).isOpen());
    }

    @Test
    public void searchSortPriceDescTest() {
        HomePage homePage = new HomePage(driver.get());
        homePage.open();
        Assert.assertTrue(homePage.isOpen());

        SearchResultPage searchResultPage = homePage.search(Config.get("search.query"), Config.get("search.category"));
        Assert.assertTrue(searchResultPage.isOpen());

        List<SearchResult.Result> results = searchResultPage.getResults();

        Assert.assertFalse(results.isEmpty());

        searchResultPage.setSortOption(Config.get("search.sorting"));

        results = searchResultPage.getNoSponsoredResults();

        //driver.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(4));

        searchResultPage.printResults();

        List<Double> prices = results.stream().map(r -> Double.parseDouble(r.getPrice()) + Double.parseDouble(r.getShipping())).toList();

        LOGGER.info("Non sponsored prices count = {}", prices.size());
        prices.forEach(p -> LOGGER.info(p.toString()));

        Assert.assertEquals(prices.stream().sorted(Comparator.reverseOrder()).toList(), prices);
    }
}

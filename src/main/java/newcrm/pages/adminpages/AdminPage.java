package newcrm.pages.adminpages;

import newcrm.global.GlobalMethods;
import newcrm.pages.Page;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.testng.Assert;
import utils.LogUtils;

import java.util.List;

public class AdminPage extends Page {

    public AdminPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public void refresh() {
        driver.navigate().refresh();
        LogUtils.info("Refresh page");
    }

    @Override
    public void waitLoading() {
        try {
            // Old admin page loading
            fastwait.until((ExpectedCondition<Boolean>) d -> {
                List<WebElement> loaders = d.findElements(By.xpath("//div[contains(@class,'fixed-table-loading') and contains(@style,'display: flex')]"));
                return loaders.isEmpty();
            });
            // New admin page loading
            fastwait.until((ExpectedCondition<Boolean>) d -> {
                List<WebElement> loaders = d.findElements(By.cssSelector("div.ant-spin"));
                return loaders.isEmpty();
            });
        } catch (Exception e) {
            Assert.fail("Page Loader not found");
        }
    }

    // region [ Old Admin Page Functions ]

    public void waitLoadingPageContent() {
        try {
            fastwait.until(driver -> {
                WebElement mainContent = assertElementExists(By.xpath("//div[@id='mainContent']"), "Page Content");
                List<WebElement> children = assertElementsExists(By.xpath("./*"), "Page Content", mainContent);
                return !children.isEmpty();
            });
        }
        catch(Exception e)
        {
            LogUtils.info("Failed to load page content");
        }

        assertVisibleElementsExists(By.cssSelector("div#toolbar"), "Page Toolbar Content");
        assertVisibleElementsExists(By.cssSelector("table#table"), "Page Table Content");
    }

    public void clickSearchBtn()
    {
        WebElement btnEle = assertElementExists(By.cssSelector("div#toolbar button#query"), "Search button");
        triggerClickEvent(btnEle);
        LogUtils.info("Click Search button");
    }

    public void setUserIdSearchCriteria(String userId)
    {
        WebElement userIdEle = assertElementExists(By.cssSelector("th[data-field='user_id'] input.search-input"), "User ID");
        setInputValue(userIdEle, userId);
        GlobalMethods.printDebugInfo("Set Search Criteria User ID: " + userId);
    }

    public boolean checkNoDataContent() {
        WebElement ele = checkElementExists(By.cssSelector("table#table tr.no-records-found"));
        return ele != null;
    }

    // endregion

    // region [ New Admin Page Functions ]

    public void waitLoadingPageContentNew() {
        assertVisibleElementsExists(By.cssSelector("div#root"), "Page Content");
        assertVisibleElementsExists(By.cssSelector("form#business_search"), "Page Search Criteria Content");
        assertVisibleElementsExists(By.cssSelector("div[class*='businessTable_card']"), "Page Table Content");
    }

    public void clickSearchBtnNew()
    {
        WebElement btnEle = assertElementExists(By.cssSelector("form#business_search button"), "Search button", e -> !e.getText().toLowerCase().contains("Search".toLowerCase()));
        triggerClickEvent(btnEle);
        LogUtils.info("Click Search button");
    }

    public void setUserIdSearchCriteriaNew(String userId)
    {
        WebElement userIdEle = assertElementExists(By.cssSelector("input#business_search_userId"), "User ID");
        setInputValue(userIdEle, userId);
        GlobalMethods.printDebugInfo("Set Search Criteria User ID: " + userId);
    }

    public boolean checkNoDataContentNew() {
        WebElement ele = checkElementExists(By.cssSelector("div.ant-table-container table tbody tr.ant-table-placeholder"));
        return ele != null;
    }

    // endregion

}

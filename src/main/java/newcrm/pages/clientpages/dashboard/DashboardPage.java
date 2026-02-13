package newcrm.pages.clientpages.dashboard;

import newcrm.global.GlobalMethods;
import newcrm.pages.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import tools.ScreenshotHelper;
import utils.LogUtils;

import java.time.Duration;
import java.util.List;
import java.util.StringJoiner;

public class DashboardPage extends Page {

    public DashboardPage(WebDriver driver) {
        super(driver);
    }

    protected List<WebElement> getTradeViewTabListEle() {
        return assertElementsExists(By.xpath("//div[@class='HomePage']//div[@role='tablist']/div[@role='tab']"), "Trade View Tab List");
    }

    protected WebElement getTradeViewTabEle(String tabName) {
        return assertElementExists(By.cssSelector("div#tab-" + tabName), "Trade View Tab (" + tabName + ")");
    }

    public void checkTradeViewTabList() {
        List<WebElement> eleLst = getTradeViewTabListEle();
        StringJoiner tabJoiner = new StringJoiner(",");

        for (WebElement ele: eleLst) {
            tabJoiner.add(ele.getText());
        }

        LogUtils.info("Trade View Tab List: " + tabJoiner);
    }

    public void clickTradeViewTab(String tabName) {
        WebElement tab = getTradeViewTabEle(tabName);
        triggerElementClickEvent_withoutMoveElement(tab);
        LogUtils.info("Click Trade View Tab: " + tabName);

        waitTradeViewLoader();
        waitLoadingTradeViewContent();
        ScreenshotHelper.takeFullPageScreenshot(getDriver(), "screenshots", tabName);
    }

    public void waitTradeViewLoader() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            wait.until(driver ->
                    driver.findElements(
                            By.xpath("//div[contains(@class,'el-loading-mask') and not(contains(@style,'display: none'))]")
                    ).isEmpty()
            );
        } catch (TimeoutException e) {
            Assert.fail("Trade View Content Loader not found or timed out while waiting");
        }
    }

    public void waitLoadingTradeViewContent() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            wait.until(driver ->
                    driver.findElements(
                            By.xpath("//div[contains(@class,'el-table__body-wrapper')]//div[@class='el-table__empty-block']")
                    ).isEmpty()
            );
        } catch (Exception ex) {
            Assert.fail("Trade View Content not found or timed out while waiting");
        }
    }

}

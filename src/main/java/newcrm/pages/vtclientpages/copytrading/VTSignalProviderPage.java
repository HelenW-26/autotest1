package newcrm.pages.vtclientpages.copytrading;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.copyTrading.SignalProviderPage;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.LogUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VTSignalProviderPage extends SignalProviderPage {
    public VTSignalProviderPage(WebDriver driver) {
        super(driver);
    }
    HashMap<String, String> copierAccount = new HashMap<>();
    protected List<WebElement> gettableBodyRows_strategyEle(){
        return driver.findElements(By.xpath("//div[@id='pane-strategies']//div[@class='el-card__body']"));
    }
    protected List<WebElement> gettableBodyRows_positionEle(){
        return driver.findElements(By.xpath("//div[@id='pane-position']//div[@class='el-card__body']"));
    }
    protected WebElement getMorebtnEle(){
        return  findVisibleElemntBy(By.xpath("(//div[contains(@class,'more-btn')])[1]"));
    }
    protected WebElement getStrategyUserInfoEle(){
        return assertVisibleElementExists(By.xpath("//div[@class='info_warp info_large']"), "strategy user info");
    }
    public void getCopierAccTitleInfo() {
        waitLoading();
        waitLoadingInCopyTrading();
        //List<WebElement> titleInfoList = findVisibleElemntsBy(By.xpath("//div[@class='data-box']/div[@class='common-data-bar']/div"));
        List<WebElement> titleInfoList = getTitleInfo();
        for (WebElement element : titleInfoList) {
            String titleInfo = element.getText();

            System.out.println("copying trading account title Info:" + titleInfo);
            if (titleInfo == null || !titleInfo.contains("\n")) continue;

            String[] lines = titleInfo.split("\\n");
            if (lines.length < 2) continue;

            String value = lines[0].trim();
            String label = lines[1].toLowerCase().trim();

            if (label.contains("equity")) {
                copierAccount.put("equity",value);
            }else if (label.contains("balance")) {
                copierAccount.put("balance",value);
            } else if (label.contains("credit")) {
                copierAccount.put("credit",value);
            }
        }
        System.out.println("copierAccount balance in signal provide page:" + copierAccount.get("balance"));
    }
    public int getStrategyCount() {
        waitLoadingInCopyTrading();
        List<WebElement> rows = gettableBodyRows_strategyEle();
        GlobalMethods.printDebugInfo("strategy rows size:" + rows.size());
        return rows.size();
    }
    public List<String> getStrategyNameList() {
        List<WebElement> rows = gettableBodyRows_strategyEle();
        List<String> texts = new ArrayList<>();

        if (rows == null || rows.isEmpty()) {
            GlobalMethods.printDebugInfo("No strategy in signal provider page");
            return texts;
        }

        for (WebElement row : rows) {
            if (row == null) continue;
            String t = row.getText();
            if (t != null) {
                t = t.trim();
                if (!t.isEmpty()) {
                    texts.add(t);
                }
            }
        }

        return texts;
    }

    public void clickMoreBtn(){
        waitLoadingInCopyTrading();
        getMorebtnEle().click();
        //findVisibleElemntBy(By.xpath("(//div[contains(@class,'more-btn-wrapper')])[1]")).click();
        waitLoadingInCopyTrading();
        GlobalMethods.printDebugInfo("Click more button at strategy list");
    }
    public void clickMoreBtn(String strategyAccount){
        waitLoadingInCopyTrading();
        getMorebtnEle().click();
       // findVisibleElemntBy(By.xpath("//div[contains(@class,'right-action') and ancestor::div[.//span[contains(normalize-space(.),'"+strategyAccount+"')]]]//div[contains(@class,'ht-drop-down')]")).click();
        GlobalMethods.printDebugInfo("Click more button at strategy list");
    }
    public boolean checkStrategyHomePage(String strategyAcc) {
        waitLoading();

        String currentUrl = driver.getCurrentUrl();
        String userInfoText = getStrategyUserInfoEle().getText();
        String equityTitleText = getStrategyEquityTitleEle().getText();
        String detailTabText = getStrategyDetailTabEle().getText();

        if (!StringUtils.containsIgnoreCase(currentUrl, "discoverDetail")) {
            LogUtils.info("Strategy Home Page Check Failed: URL does not contain 'discoverDetail'. Current URL: " + currentUrl);
            return false;
        }

        if (!checkKeywords("User Info Section", userInfoText,
                strategyAcc)) {
            return false;
        }

        if (!checkKeywords("Equity Title Section", equityTitleText,
                "Strategy Equity", "Equity", "Balance", "Credit")) {
            return false;
        }

        if (!checkKeywords("Detail Tab Section", detailTabText,
                "Overview", "Portfolio")) {
            return false;
        }

        LogUtils.info("Strategy Home Page Check: All checks passed.");
        return true;
    }

}

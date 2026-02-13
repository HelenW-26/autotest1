package newcrm.pages.pugclientpages.copyTrading;

import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.pages.clientpages.LiveAccountsPage;
import newcrm.pages.clientpages.copyTrading.SignalProviderPage;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.LogUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PUSignalProviderPage extends SignalProviderPage {
    private By positionDiv = By.xpath("//div[@class='title-info']//input");
    private By PageTitle = By.xpath("//div[@class='title']");
    protected By tableBodyRows_strategy = By.xpath("//div[@id='pane-strategies']//div[@class='el-card__body']");
    protected By tableBodyRows_position = By.xpath("//div[@id='pane-position']//div[@class='el-card__body']");

    protected WebElement getMorebtnEle(){
        return  assertVisibleElementExists(By.xpath("//div[@class='card_right']//div[contains(@class,'ht-drop-down')]"), "more button");
    }

    protected List<WebElement> gettableBodyRows_strategyEle(){
        return driver.findElements(By.xpath("//div[@id='pane-strategies']//div[@class='el-card__body']"));
    }
    protected List<WebElement> gettableBodyRows_positionEle(){
        return driver.findElements(By.xpath("//div[@id='pane-position']//div[@class='el-card__body']"));
    }
    protected List<WebElement> gettableBodyRows_historyEle(){
        return driver.findElements(By.xpath("//div[@id='pane-history']//div[@class='el-card__body']"));
    }

    protected List<WebElement> getDateEle() {
        return assertVisibleElementsExists(By.xpath("//div[@id='pane-position']//div[contains(@class,'el-date-editor')]/input"), "Data tab");
    }

    protected WebElement moreBtn(String strategyAccount){
        return assertElementExists(By.xpath("//div[contains(@class,'right-action') and ancestor::div[.//span[contains(normalize-space(.),'"+strategyAccount+"')]]]//div[contains(@class,'ht-drop-down')]"),"More button");
    }
    protected List<WebElement> getPostionHistory(){
        return driver.findElements(By.xpath("//div[contains(@class,'signal-history-wrapper')]//div[contains(@class,'el-card__body')]"));
    }
    HashMap<String, String> copierAccount = new HashMap<>();
    public PUSignalProviderPage(WebDriver driver)
    {
        super(driver);
    }

    @Override
    public boolean checkCopierAccount(String accNum)
    {
        //refresh();
        String pageTitle = findVisibleElemntBy(PageTitle).getText();
        System.out.println("SignalProvider page Title:" + pageTitle);

        WebElement positionEle = findVisibleElemntBy(positionDiv);
        js.executeScript("arguments[0].click();",positionEle);
        waitLoading();
        List<WebElement> copierAccountList = driver.findElements(By.xpath("//ul[@class='el-scrollbar__view el-select-dropdown__list']/li[./span[contains(text(),'Copy Trading')]]"));

        for(WebElement e:copierAccountList)
        {
            String accInfo = e.getText();
            System.out.println("accInfo:" + accInfo);
            if(accInfo.contains(accNum)) {
                String[] accInfoArr = accInfo.split("-");

                String accNumInList = accInfoArr[0];
                String accCurrency = accInfoArr[1];
                String accTypeInfo = accInfoArr[2].replaceAll("\\s+", "");

                System.out.println(accNumInList + accCurrency + accTypeInfo + accNum.equalsIgnoreCase(accNumInList) +
                        accCurrency.equalsIgnoreCase(GlobalProperties.CURRENCY.USD.toString()) +
                        StringUtils.containsIgnoreCase(accTypeInfo, "CopyTrading"));

                if (accNum.equalsIgnoreCase(accNumInList) && accCurrency.equalsIgnoreCase(GlobalProperties.CURRENCY.USD.toString())
                        && StringUtils.containsIgnoreCase(accTypeInfo, "CopyTrading")) {
                    copierAccount.put("accNum", accNumInList);
                    copierAccount.put("currency", accCurrency);
                    copierAccount.put("type", accTypeInfo);

                    e.click();
                    return true;
                }
            }
        }
        return false;
    }

    public void getCopierAccTitleInfo() {
        //List<WebElement> titleInfoList = driver.findElements(By.xpath("//div[@class='data-box']/div[@class='common-data-bar']/div"));

        List<WebElement> titleInfoList = getTitleInfo();
        for (WebElement element : titleInfoList) {
            String titleInfo = element.getText();

            System.out.println("titleInfo:" + titleInfo);
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

    //compare copier account info in signal page with account info in liveaccount page
    public boolean compareCopierAccountInfo(LiveAccountsPage.Account liveAccount)
    {
        waitLoading();
        System.out.println("copierAccount.get(\"accNum\")" + copierAccount.get("accNum")+ " liveAccount.getAccountNum():" + liveAccount.getAccountNum());
        boolean accNumCompare = liveAccount.getAccountNum().equalsIgnoreCase(copierAccount.get("accNum"));
        //boolean equityCompare = liveAccount.getEquity().equalsIgnoreCase(copierAccount.get("equity"));
        System.out.println("liveAccount.getBalance()" + liveAccount.getBalance()+ "copierAccount.get(\"balance\")" + copierAccount.get("balance"));
        boolean balanceCompare = liveAccount.getBalance().equalsIgnoreCase(copierAccount.get("balance"));

        GlobalMethods.printDebugInfo("accNumCompare: " + accNumCompare  + " balanceCompare:" +balanceCompare);
        return accNumCompare && balanceCompare;
    }

    public void clickMoreBtn(String strategyAccount){
        waitLoadingInCopyTrading();
        moreBtn(strategyAccount).click();
       // findVisibleElemntBy(By.xpath("//div[contains(@class,'right-action') and ancestor::div[.//span[contains(normalize-space(.),'"+strategyAccount+"')]]]//div[contains(@class,'ht-drop-down')]")).click();
        GlobalMethods.printDebugInfo("Click more button at strategy list");
    }

    public void clickMoreBtn(){
        waitLoadingInCopyTrading();
        findVisibleElemntBy(By.xpath("(//div[@class='card_right']//div[contains(@class,'ht-drop-down')])[1]")).click();
        GlobalMethods.printDebugInfo("Click more button at strategy list");
    }
    public int getStrategyCount() {
        waitLoadingInCopyTrading();
        List<WebElement> rows = gettableBodyRows_strategyEle();
        GlobalMethods.printDebugInfo("strategy rows size:" + rows.size());
        return rows.size();
    }

    public String getStrategyName() {
        List<WebElement> rows = gettableBodyRows_strategyEle();
        if (!rows.isEmpty()) {
            return rows.get(0).getText().trim();
        } else {
            GlobalMethods.printDebugInfo("No strategy in signal provider page");
            return null;
        }
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
                    LogUtils.info("Strategy name:" + t);
                }
            }
        }

        return texts;
    }

    public int getHistoryCount() {
        waitLoadingInCopyTrading();
        List<WebElement> historyRows = gettableBodyRows_historyEle();
        GlobalMethods.printDebugInfo("historyRows size:" + historyRows.size());
        return historyRows.size();
    }

    public int getPositionCount() {
        waitLoadingInCopyTrading();
        List<WebElement> positionRows = gettableBodyRows_positionEle();
        GlobalMethods.printDebugInfo("positionRows size:" + positionRows.size());
        return positionRows.size();
    }

    public boolean checkPositionHistoryDate(){
        waitvisible.until(d -> {
            List<WebElement> ins = getDateEle();
            return ins.size() >= 2 && !ins.get(0).getAttribute("value").isBlank();
        });

        List<WebElement> inputs = getDateEle();
        String startDate = inputs.get(0).getAttribute("value").trim();
        String endDate   = inputs.get(1).getAttribute("value").trim();

        System.out.println(startDate + " -> " + endDate);

        String start = "2025/10/30";
        String end   = "2025/11/06";

        setDateByJS( inputs.get(0), start);
        setDateByJS( inputs.get(1), end);

        inputs.get(1).sendKeys(Keys.ENTER);

        waitLoadingInCopyTrading();

        //List<WebElement> positonHis = driver.findElements(By.xpath("//div[contains(@class,'signal-history-wrapper')]//div[contains(@class,'el-card__body')]"));

        List<WebElement> positonHis = getPostionHistory();
        int positionCount = positonHis.size();

        GlobalMethods.printDebugInfo("Search position history between 2025/10/30 and 2025/11/06, positionCount: " + positionCount);
        return isWithinInclusive(
                LocalDate.now(),
                parseUiDate(startDate),
                parseUiDate(endDate)
        );
    }
}


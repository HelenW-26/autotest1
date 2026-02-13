package newcrm.pages.vtclientpages.copytrading;

import newcrm.business.businessbase.CPMenu;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.pages.clientpages.copyTrading.DiscoverPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class VTDiscoverPage extends DiscoverPage {
    private static String copyAmount = "0";
    public VTDiscoverPage(WebDriver driver) {
        super(driver);
    }
    protected WebElement getSearchEle() {
        return assertVisibleElementExists(By.xpath("(//input[contains(@placeholder,'Search strategy')])[1]"), "Search wrapper");
    }
    protected WebElement getSearchStrategyInputEle() {
        return assertVisibleElementExists(By.xpath("(//input[contains(@placeholder,'Search strategy')])[2]"), "Search strategy input");
    }
    protected WebElement getStrategyUserInfoInDetailsEle() {
        return assertVisibleElementExists(By.xpath("//div[@class='strategy-base-info']"), "Strategy user info (details)");
    }
    protected WebElement getAvailableInvestmentInCopyDialogEle() {
        return assertVisibleElementExists(By.xpath("//div[normalize-space(text())='Available Investment']/following-sibling::div[1]"),
                // return assertVisibleElementExists(By.xpath("//div[@class='form-item-investment-desc'][contains(normalize-space(text()),'Available Investment')]"),
                "Available Investment value");
    }

    protected WebElement getAvailableBalanceInCopyDialogEle() {
        return assertVisibleElementExists(By.xpath("//span[contains(normalize-space(.),'Available Balance')]"),
                // return assertVisibleElementExists(By.xpath("//div[@class='form-item-subtitle-desc'][contains(normalize-space(.), 'Available Balance')]"),
                "Available Balance text");
    }

    public void searchProviderInDiscoverPage(String strategyAcc)
    {
        clickSearch();
        searchProviderInSearchPage(strategyAcc);
    }
    public void clickSearch()
    {
        WebElement searchBar = getSearchEle();
        searchBar.click();
        GlobalMethods.printDebugInfo("Click search");
        waitLoadingInCopyTrading();
    }
    public void searchProviderInSearchPage(String strategyAcc)
    {
        getSearchStrategyInputEle().sendKeys(strategyAcc);
        waitLoading();
        js.executeScript("arguments[0].click()",getSearchBtnEle());
        waitForCopyTrading(2000);
        waitLoading();
        waitLoadingInCopyTrading();
        GlobalMethods.printDebugInfo("Search signal provider: " + strategyAcc);
    }

    public String getStrategyDetailsDisplay() {
        waitLoading();
        return getStrategyUserInfoInDetailsEle().getText();
    }

    public boolean submitCopyRequest(CPMenu menu, String copier, boolean needCopyDetail, boolean  isCopyOpenTrade)
    {
        //because the MTS sync database takes time to sync, so we need to wait for 10s
        waitLoading();
        waitLoadingInCopyTrading();
        waitForCopyTrading(5000);
        WebElement strategyAcc = findClickableElementByXpath("//div[@id='pane-0']//div[./span[contains(text(),'"+ copier +"')]]");
        strategyAccName = strategyAcc.getText();
        strategyAcc.click();
        waitLoading();
        waitLoadingInCopyTrading();

        try {
            waitvisible.until(driver -> {
                try {
                    driver.findElement(By.xpath("//button/span[contains(text(),'Copy')]")).isDisplayed();
                    GlobalMethods.printDebugInfo("Find copy button");
                    return true;
                } catch (Exception ex) {
                    return false;
                }
            });
        } catch (Exception ex) {
            System.out.println("No copy btn display");
        }
        if(driver.findElements(By.xpath("//button/span[contains(text(),'Copy')]")).isEmpty())
        {
            menu.goToMenu(GlobalProperties.CPMenuName.COPIER);
            menu.goToMenu(GlobalProperties.CPMenuName.DISCOVER);
            strategyAcc = findClickableElementByXpath("//div[@id='pane-0']//div[./span[contains(text(),'"+ copier +"')]]");
            strategyAccName = strategyAcc.getText();
            strategyAcc.click();
            waitLoading();
        }
        getCopyBtnEle().click();

        waitLoading();
        //wait 3 sec to submit to avoid error
        waitForCopyTrading(3000);
        waitLoadingInCopyTrading();

        if(needCopyDetail) {

            String returnPcent = getReturnInCopyDialogEle().getText();
            String settlement = getSettlementInCopyDialogEle().getText();
            String profit = getProfitSharingInCopyDialogEle().getText();
            String investment = (String) js.executeScript("return arguments[0].value;", getInvestmentInCopyDialogInputEle());
            //     String usedCredit = getUsedCreditInCopyDialogEle().getText();
            //     String usedBalance = getUsedBalanceInCopyDialogEle().getText();
            copyAmount = investment;
            // String marginMultiple = (String) js.executeScript("return arguments[0].value;", driver.findElement(marginMultiInCopyDialog));
            String stopLoss = getStopLossSliderInCopyDialogEle().getAttribute("aria-valuetext");
            String codeMode = (String) js.executeScript("return arguments[0].value;", getCodeModeInCopyDialogInputEle());
            /*String availableBalance = getAvailableBalanceInCopyDialogEle().getText();
            String availableInvestment = getAvailableInvestmentInCopyDialogEle().getText();

            copyStrategyDetail.put("AvailableInvestment", availableInvestment);
            copyStrategyDetail.put("AvailableBalance", availableBalance);*/
            copyStrategyDetail.put("CodeMode", codeMode);
            copyStrategyDetail.put("StopLoss", stopLoss);
            //  copyStrategyDetail.put("MarginMultiple", marginMultiple);
            // copyStrategyDetail.put("UsedBalance", usedBalance);
            //   copyStrategyDetail.put("UsedCredit", usedCredit);
            copyStrategyDetail.put("Investment", investment);
            copyStrategyDetail.put("Profit", profit);
            copyStrategyDetail.put("Settlement", settlement);
            copyStrategyDetail.put("Return", returnPcent);
        }

        waitLoading();
        waitLoadingInCopyTrading();

        if(isCopyOpenTrade)
        {
            boolean isOpenTrade =getOpenTradeSwitchEle().getAttribute("class").contains("is-checked");
            GlobalMethods.printDebugInfo("Is copy open trades switch: " + isOpenTrade);
            if(!isOpenTrade)
            {
                getOpenTradeSwitchEle().click();
                GlobalMethods.printDebugInfo("Checked copy open trades");
            }
        }


        getSubmitBtnEle().click();
        try {
            waitLoadingInCopyTrading();
            waitvisible.until(driver -> {
                try {
                    getSuccessMsgEle().isDisplayed();
                    GlobalMethods.printDebugInfo("Find success message");
                    return true;
                } catch (Exception ex) {
                    return false;
                }
            });
        } catch (Exception ex) {
            System.out.println("No success message display");
        }

        String submitMsg = getSuccessMsgEle().getText();
        GlobalMethods.printDebugInfo("Submit message is: " + submitMsg);

        //check whether submit successful
        if(submitMsg.contains("Submit Successful"))
        {
            getOkBtnEle().click();
            waitLoading();
        }
        return submitMsg.contains("Submit Successful");

    }

}

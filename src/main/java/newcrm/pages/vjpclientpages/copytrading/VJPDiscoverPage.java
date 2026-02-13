package newcrm.pages.vjpclientpages.copytrading;

import newcrm.business.businessbase.CPMenu;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.pages.clientpages.copyTrading.DiscoverPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

public class VJPDiscoverPage extends DiscoverPage {

    public VJPDiscoverPage(WebDriver driver) {
        super(driver);
    }
    private static String copyAmount = "0";
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
    protected WebElement getUsedBalanceInCopyDialogEle() {
        return assertVisibleElementExists(By.xpath("//div[@class='title_warp' and .//span[contains(normalize-space(.), 'Used Balance')]]/following-sibling::div[@class='value_warp']"),
                "Used Balance value");
    }
    public boolean submitCopyRequest(CPMenu menu, String copier, boolean needCopyDetail, boolean  isCopyOpenTrade) {
        //because the MTS sync database takes time to sync, so we need to wait for 10s
        waitLoading();
        waitLoadingInCopyTrading();
        waitForCopyTrading(5000);
        WebElement strategyAcc = findClickableElementByXpath("//div[@id='pane-0']//div[./span[contains(text(),'" + copier + "')]]");
        strategyAccName = strategyAcc.getText();
        strategyAcc.click();
        waitLoading();
        waitLoadingInCopyTrading();

        try {
            waitvisible.until(driver -> {
                try {
                    getCopyBtnEle().isDisplayed();
                    GlobalMethods.printDebugInfo("Find copy button");
                    return true;
                } catch (Exception ex) {
                    return false;
                }
            });
        } catch (Exception ex) {
            System.out.println("No copy btn display");
        }
        if (driver.findElements(By.xpath("//button/span[contains(text(),'Copy')]")).isEmpty()) {
            menu.goToMenu(GlobalProperties.CPMenuName.COPIER);
            menu.goToMenu(GlobalProperties.CPMenuName.DISCOVER);
            strategyAcc = findClickableElementByXpath("//div[@id='pane-0']//div[./span[contains(text(),'" + copier + "')]]");
            strategyAccName = strategyAcc.getText();
            strategyAcc.click();
            waitLoading();
        }
        getCopyBtnEle().click();

        waitLoading();
        //wait 3 sec to submit to avoid error
        waitForCopyTrading(3000);
        waitLoadingInCopyTrading();

        if (needCopyDetail) {

            String returnPcent = getReturnInCopyDialogEle().getText();
            String settlement = getSettlementInCopyDialogEle().getText();
            String profit = getProfitSharingInCopyDialogEle().getText();
            String investment = (String) js.executeScript("return arguments[0].value;", getInvestmentInCopyDialogInputEle());
            /*String usedCredit = getUsedCreditInCopyDialogEle().getText();
            String usedBalance = getUsedBalanceInCopyDialogEle().getText();*/
            // String marginMultiple = (String) js.executeScript("return arguments[0].value;", driver.findElement(marginMultiInCopyDialog));
            String stopLoss = getStopLossSliderInCopyDialogEle().getAttribute("aria-valuetext");
            String codeMode = (String) js.executeScript("return arguments[0].value;", getCodeModeInCopyDialogInputEle());
            String availableBalance = getAvailableBalanceInCopyDialogEle().getText();
            String availableInvestment = getAvailableInvestmentInCopyDialogEle().getText();

            copyStrategyDetail.put("AvailableInvestment", availableInvestment);
            copyStrategyDetail.put("AvailableBalance", availableBalance);
            copyStrategyDetail.put("CodeMode", codeMode);
            copyStrategyDetail.put("StopLoss", stopLoss);
            //  copyStrategyDetail.put("MarginMultiple", marginMultiple);
            /*copyStrategyDetail.put("UsedBalance", usedBalance);
            copyStrategyDetail.put("UsedCredit", usedCredit);*/
            copyStrategyDetail.put("Investment", investment);
            copyStrategyDetail.put("Profit", profit);
            copyStrategyDetail.put("Settlement", settlement);
            copyStrategyDetail.put("Return", returnPcent);
        }

        waitLoading();
        waitLoadingInCopyTrading();

        if (isCopyOpenTrade) {
            boolean isOpenTrade = getOpenTradeSwitchEle().getAttribute("class").contains("is-checked");
            GlobalMethods.printDebugInfo("Is copy open trades switch: " + isOpenTrade);
            if (!isOpenTrade) {
                getOpenTradeSwitchEle().click();
                GlobalMethods.printDebugInfo("Checked copy open trades");
            }
        }

        getSubmitBtnEle().click();
        waitLoadingInCopyTrading();

        By successBox = By.xpath("//div[@class='success_box']");

        try {
            WebElement box = waitvisible.until(
                    ExpectedConditions.visibilityOfElementLocated(successBox)
            );

            String submitMsg = box.getText().trim();
            GlobalMethods.printDebugInfo("Submit message is: " + submitMsg);

            boolean ok = submitMsg.contains("Submit Successful");
            if (ok) {
                getOkBtnEle().click();
                waitLoading();
            } else {
                Assert.fail("Success message did not contain 'Submit Successful'");
            }

            return ok;

        } catch (Exception e) {
            Assert.fail("Success message not found within timeout", e);
            return false;
        }
    }
}

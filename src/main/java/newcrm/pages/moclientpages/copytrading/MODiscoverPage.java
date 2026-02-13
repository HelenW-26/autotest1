package newcrm.pages.moclientpages.copytrading;

import newcrm.business.businessbase.CPMenu;
import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.copyTrading.DiscoverPage;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.LogUtils;

public class MODiscoverPage extends DiscoverPage {
    public MODiscoverPage(WebDriver driver) {
        super(driver);
    }

    protected WebElement getSubmitBtnEle() {
        return assertVisibleElementExists(By.xpath("//button[@data-testid='button']/span[contains(text(),'Submit')]"), "Submit button");
    }
    public boolean submitCopyRequest(CPMenu menu, String copier, boolean needCopyDetail, boolean isCopyOpenTrade, String copyMode) {
        prepareStrategyDetail(menu, copier);

        getCopyBtnEle().click();
        waitLoading();
        waitForCopyTrading(3000);
        waitLoadingInCopyTrading();

        if (copyMode != null && !StringUtils.containsIgnoreCase(copyMode, "Equivalent Used Margin")) {
            LogUtils.info("Switching to Copy Mode: " + copyMode);
            getCopyModeArrowEle().click();
            WebElement copyModeEle = findVisibleElemntByXpath("//span[contains(text(),'" + copyMode + "')]");
            js.executeScript("arguments[0].click()", copyModeEle);
            //driver.findElement(By.xpath("//span[contains(text(),'" + copyMode + "')]")).click();
            waitLoadingInCopyTrading();
        }

        if (needCopyDetail) {
            collectCopyDetails(copyMode);
        }

        handleOpenTradeSwitch(isCopyOpenTrade);

        return executeSubmitAndCheck();
    }
    protected boolean executeSubmitAndCheck() {
        getSubmitBtnEle().click();
        try {
            waitLoadingInCopyTrading();
            waitvisible.until(d -> getSuccessMsgEle().isDisplayed());
        } catch (Exception ex) {
            GlobalMethods.printDebugInfo("Success message did not appear.");
        }

        String submitMsg = getSuccessMsgEle().getText();
        boolean isSuccess = submitMsg.contains("Submit Successful");

        if (isSuccess) {
            getOkBtnEle().click();
            waitLoading();
        }
        return isSuccess;
    }
}

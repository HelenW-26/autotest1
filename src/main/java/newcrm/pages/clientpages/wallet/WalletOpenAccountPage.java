package newcrm.pages.clientpages.wallet;

import newcrm.global.GlobalMethods;
import newcrm.pages.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.LogUtils;

public class WalletOpenAccountPage extends Page {

    public WalletOpenAccountPage(WebDriver driver) {
        super(driver);
    }

    protected WebElement getActivateBtn() {
        return assertVisibleElementExists(By.xpath("//div[@class='not_open_wallet']//button[@data-testid='button']"), "Activate button");
    }

    protected WebElement getVerifyNowBtn() {
        return assertElementExists(By.xpath("//div[contains(@class, 'kyc_dialog') and not(contains(@style, 'display: none'))]//button[./span[normalize-space() = 'Verify Now']]"), "Verify button");
    }

    public void clickActivateBtn() {
        triggerClickEvent(this.getActivateBtn());
        LogUtils.info("Click Activate button");
    }

    public void clickVerifyNowBtn() {
        triggerClickEvent(this.getVerifyNowBtn());
        LogUtils.info("Click Verify Now button");
    }

    public boolean checkActivationUnderReview() {
        assertVisibleElementExists(By.xpath("//div[contains(@class, 'result_info')]"),"Wallet Activation Response Content");
        WebElement e = checkElementExists(By.xpath("//*[contains(text(),'information is currently under review')]"));

        if (e != null) {
            WebElement response = driver.findElement(By.xpath("//*[contains(text(),'information is currently under review')]"));
            LogUtils.info("WalletOpenAccountPage: Response info: " + response.getText().trim());
            return true;
        }

        return false;
    }

    public void checkAccountActivated() {
        waitLoading();
        assertVisibleElementExists(By.xpath("//div[@class='wallet_home']"),"Wallet Content");
    }

    public void waitLoadingWalletActivationContent() {
        assertVisibleElementExists(By.xpath("//div[contains(@class, 'kyc_dialog') and not(contains(@style, 'display: none'))]"), "Wallet Activation Content");
    }

    public void checkWalletActivateNow(){
        WebElement e = checkElementExists(By.xpath("//span[contains(text(),' Activate Now ')]"));
        if (e != null && e.isDisplayed()){
            triggerClickEvent(e);
            LogUtils.info("click wallet activate now again");
        }else {
            LogUtils.info("do not need click activate now again");
        }
    }
}

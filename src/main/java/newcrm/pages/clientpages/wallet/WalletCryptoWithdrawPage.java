package newcrm.pages.clientpages.wallet;

import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties.WalletWithdrawMethod;
import newcrm.pages.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.SkipException;

import java.util.List;
import java.util.NoSuchElementException;

public class WalletCryptoWithdrawPage extends Page {

    public WalletCryptoWithdrawPage(WebDriver driver) {
        super(driver);
    }

    protected WebElement getCryptoToAddressEle() {
        return assertElementExists(By.xpath("(//div[contains(@class, 'ht-input el-input')])[1]//input[@class='el-input__inner']"), "Withdraw Address");
    }

    protected WebElement getCryptoWithdrawAmountSuffixEle() {
        return assertElementExists(By.xpath("(//div[contains(@class, 'ht-input el-input')])[2]//div[@class='suffix-currency']"), "Withdraw Amount Currency");
    }

    protected WebElement getCryptoWithdrawAmountEle() {
        return assertElementExists(By.xpath("(//div[contains(@class, 'ht-input el-input')])[2]//input[@class='el-input__inner']"), "Withdraw Amount");
    }

    protected WebElement getWalletWithdrawBtn() {
        return assertElementExists(By.xpath("//div[@class='withdraw-footer-confirm']/button"), "Wallet Withdraw button");
    }

    protected WebElement getWalletWithdrawConfirmBtn() {
        return assertElementExists(By.xpath("//div[@class='withdraw-confirm-footer']/button[@data-testid='button']"), "Wallet Withdraw Confirm button");
    }

    protected WebElement getCryptoToAddressErrorMsgEle() {
        return checkElementExists(By.xpath("(//div[contains(@class, 'el-select ht-select')])[2]/preceding-sibling::div[@class='input-desc err-input-desc'][1]"));
    }

    protected WebElement getWithdrawAmountErrorMsgEle() {
        return checkElementExists(By.xpath("//div[@class='amount-input-wrapper']//div[contains(@class, 'amount-err-input-desc err-input-desc')]"));
    }

    protected WebElement getWithdrawAmountInfoMsgEle() {
        return checkElementExists(By.xpath("//div[@class='amount-input-wrapper']//div[contains(@class, 'amount-err-input-desc')]"));
    }

    protected WebElement getCryptoCurrencyEle() {
        return assertElementExists(By.xpath("(//div[contains(@class, 'el-select ht-select')])[1]"), "Crypto Currency List");
    }

    protected WebElement getCryptoNetworkEle() {
        return assertElementExists(By.xpath("(//div[contains(@class, 'el-select ht-select')])[2]"), "Crypto Network List");
    }

    protected String getCryptoNetwork(int idx) {
        return assertElementExists(By.xpath("//div[contains(@class,'el-select-dropdown el-popper') and not(contains(@style, 'display: none'))]//li//div[@class='chain-item-wrapper']/div[" + idx + "]"), "Crypto Network Title").getText();
    }

    protected WebElement getAvailableWithdrawAmountEle() {
        return assertElementExists(By.xpath("//div[contains(@class, 'amount-available-box')]//div[@class='available-amount']"), "Available Withdraw");
    }

    protected WebElement getWithdrawFromAccountEle() {
        return assertElementExists(By.xpath("//div[contains(@class, 'withdraw-from-box')]//div[@class='withdraw-from-value']"), "Withdraw From");
    }

    protected WebElement getWithdrawFeeEle() {
        return assertElementExists(By.xpath("//div[contains(@class, 'withdraw-fee-box')]//div[contains(@class, 'fee-num')]"), "Withdraw Fee");
    }

    protected WebElement getWithdrawFeeFreeEle() {
        return checkElementExists(By.xpath("//div[contains(@class, 'withdraw-fee-box')]//div[contains(@class, 'fee-num is-free-num')]"));
    }

    protected WebElement getWithdrawFeeFreeTagEle() {
        return assertElementExists(By.xpath("//div[contains(@class, 'withdraw-fee-box')]//div[@class='fee-free']"), "Withdraw Fee Free Tag");
    }

    protected WebElement getReceiveAmountEle() {
        return assertElementExists(By.xpath("//div[contains(@class, 'receive-amount-box')]//div[@class='receive-amount']"), "Receive Amount");
    }

    protected WebElement getReviewWithdrawAmountEle() {
        return assertElementExists(By.xpath("//div[contains(@class, 'conform-withdraw-dialog')]//div[div[@class='data-item-key' and normalize-space()='Withdraw Amount']]/div[@class='data-item-value']"), "Withdraw Amount (Review)");
    }

    protected WebElement getReviewWithdrawFromAccountEle() {
        return assertElementExists(By.xpath("//div[contains(@class, 'conform-withdraw-dialog')]//div[div[@class='data-item-key' and normalize-space()='Withdraw From']]/div[@class='data-item-value']"), "Withdraw From (Review)");
    }

    protected WebElement getReviewWithdrawFeeFreeEle() {
        return checkElementExists(By.xpath("//div[contains(@class, 'conform-withdraw-dialog')]//div[contains(@class, 'fee-num is-free-num')]"));
    }

    protected WebElement getReviewWithdrawFeeFreeTagEle() {
        return assertElementExists(By.xpath("//div[contains(@class, 'conform-withdraw-dialog')]//div[div[normalize-space()='Fee']]//div[@class='fee-free']"), "Withdraw Fee Free Tag (Review)");
    }

    protected WebElement getReviewWithdrawFeeEle() {
        return assertElementExists(By.xpath("//div[contains(@class, 'conform-withdraw-dialog')]//div[div[normalize-space()='Fee']]//div[contains(@class, 'fee-num')]"), "Withdraw Fee (Review)");
    }

    protected WebElement getReviewReceiveAmountEle() {
        return assertElementExists(By.xpath("//div[contains(@class, 'conform-withdraw-dialog')]//div[normalize-space()='Receive Amount']/following-sibling::div[1]//div[contains(@class, 'currency-amount')]"), "Receive Amount (Review)");
    }

    protected WebElement getReviewReceiveAmountSuffixEle() {
        return assertElementExists(By.xpath("//div[contains(@class, 'conform-withdraw-dialog')]//div[normalize-space()='Receive Amount' and @class='withdraw-data-title']/following-sibling::div[1]//div[@class='currency-text']"), "Receive Amount (Review)");
    }

    protected WebElement getReviewWithdrawAddressEle() {
        return assertElementExists(By.xpath("//div[contains(@class, 'conform-withdraw-dialog')]//div[div[@class='data-item-key' and normalize-space()='Withdraw Address']]/div[@class='data-item-value']//div[@class='copy-text']"), "Withdraw Address (Review)");
    }

    protected WebElement getReviewWithdrawCryptoNetworkEle() {
        return assertElementExists(By.xpath("//div[contains(@class, 'conform-withdraw-dialog')]//div[div[@class='data-item-key' and normalize-space()='Network']]/div[@class='data-item-value']"), "Crypto Network (Review)");
    }

    protected WebElement getOrderWithdrawCryptoCurrencyEle() {
        return assertElementExists(By.xpath("//div[@class='order-detail-item'][div[@class='detail-item-key' and normalize-space()='Coin']]//div[@class='detail-item-value']//div[@class='currency-text']"), "Withdraw Crypto Currency (Order)");
    }

    protected WebElement getOrderWithdrawCryptoNetworkEle() {
        return assertElementExists(By.xpath("//div[@class='order-detail-item'][div[@class='detail-item-key' and normalize-space()='Network']]//div[@class='detail-item-value']"), "Crypto Network (Order)");
    }

    protected WebElement getOrderWithdrawFromAccountEle() {
        return assertElementExists(By.xpath("//div[@class='order-detail-item'][div[@class='detail-item-key' and normalize-space()='Withdraw From']]//div[@class='detail-item-value']"), "Withdraw From (Order)");
    }

    protected WebElement getOrderWithdrawWalletAddressEle() {
        return assertElementExists(By.xpath("//div[@class='order-detail-item'][div[@class='detail-item-key' and normalize-space()='Withdraw Address']]//div[@class='detail-item-value']//div[@class='copy-text']"), "Withdraw Address (Order)");
    }

    protected WebElement getOrderWithdrawAmountEle() {
        return assertElementExists(By.xpath("//div[@class='order-detail-item'][div[@class='detail-item-key' and normalize-space()='Withdraw Amount']]//div[@class='detail-item-value']"), "Withdraw Amount (Order)");
    }

    protected WebElement getOrderWithdrawReceiveAmountEle() {
        return assertElementExists(By.xpath("//div[@class='order-detail-item'][div[@class='detail-item-key' and normalize-space()='Receive Amount']]//div[@class='detail-item-value']"), "Receive Amount (Order)");
    }

    protected WebElement getOrderWithdrawFeeEle() {
        return assertElementExists(By.xpath("//div[@class='order-detail-item'][div[@class='detail-item-key' and normalize-space()='Fee']]//div[contains(@class, 'fee-num')]"), "Withdraw Fee (Order)");
    }

    protected WebElement getOrderWithdrawFeeFreeTagEle() {
        return assertElementExists(By.xpath("//div[@class='order-detail-item'][div[@class='detail-item-key' and normalize-space()='Fee']]//div[contains(@class, 'fee-free')]"), "Withdraw Fee Tag (Order)");
    }

    protected WebElement getWithdrawAuthenticatorEle() {
        return checkElementExists(By.xpath("//*[contains(text(),'Start the security validator application')]"));
    }

    protected WebElement getWithdrawAuthenticatorConfirmBtnEle() {
        return assertElementExists(By.xpath("//div[@class='withdraw-error-footer']//button[@data-testid='button']"), "Wallet Withdraw Authenticator Confirm button");
    }

    protected WebElement getCryptoNetworkIsFocus(){
        return checkElementExists(By.xpath("//div[@class='el-input el-input--suffix is-focus']"),"Crypto Network is Focus");
    }
    public void checkSelectedCryptoCurrency(WalletWithdrawMethod method) {
        // Click dropdown
        triggerClickEvent_withoutMoveElement(this.getCryptoCurrencyEle());

        // Get dropdown list values
        List<WebElement> allOpenElements = this.getAllOpendElements();
        GlobalMethods.printDebugInfo("Crypto Currency Size: " + allOpenElements.size());

        WebElement selectedElement = null;
        String cryptoCurrencyDesc = method.getCryptoCurrencyDisplayDesc();

        for (WebElement li : allOpenElements) {
            try {
                WebElement span = li.findElement(By.cssSelector("div.currency-text"));
                String spanText = span.getText().trim();

                // Look for li element that match with the cryptocurrency desc
                if (spanText.toLowerCase().contains(cryptoCurrencyDesc.toLowerCase())) {
                    String classAttr = li.getAttribute("class");

                    // check if the matching li element is selected
                    if (classAttr != null && classAttr.contains("selected")) {
                        selectedElement = li;
                        break;
                    } else {
                        Assert.fail("Crypto Currency (" + cryptoCurrencyDesc + ") found but element is NOT selected");
                    }
                }
            } catch (NoSuchElementException e) {
                Assert.fail("Crypto Currency (" + cryptoCurrencyDesc + ") not found");
            }
        }

        // Click dropdown
        triggerClickEvent_withoutMoveElement(this.getCryptoCurrencyEle());

        GlobalMethods.printDebugInfo("Crypto Currency: " + cryptoCurrencyDesc);
    }

    public void setCryptoToAddress(String recipientWalletAddress) {
        setInputValue_withoutMoveElement(this.getCryptoToAddressEle(), recipientWalletAddress);

        GlobalMethods.printDebugInfo("Wallet Address: " + recipientWalletAddress);
    }

    public void setWithdrawAmount(String withdrawAmount) {
        setInputValue_withoutMoveElement(this.getCryptoWithdrawAmountEle(), withdrawAmount);
        waitLoading();
        WebElement outside = driver.findElement(By.tagName("body"));
        outside.click();

        GlobalMethods.printDebugInfo("Withdraw Amount: " + withdrawAmount);
    }

    public String setCryptoNetwork(WalletWithdrawMethod method) {
        // Click dropdown
        triggerElementClickEvent_withoutMoveElement(this.getCryptoNetworkEle());

        // Get dropdown list values
        List<WebElement> allOpenElements = this.getAllOpendElements();
        GlobalMethods.printDebugInfo("Crypto Network Size: " + allOpenElements.size());

        WebElement element = null;
        String cryptoCurrencyNetworkTitle = "";
        String cryptoCurrencyNetworkDesc = method.getCryptoNetworkDisplayDesc();

        for (WebElement li : allOpenElements) {
            try {
                WebElement span = li.findElement(By.cssSelector("div.chain-name"));
                cryptoCurrencyNetworkTitle = span.getText().trim();

                // Look for li element that match with the cryptocurrency desc
                if (cryptoCurrencyNetworkTitle.toLowerCase().contains(cryptoCurrencyNetworkDesc.toLowerCase())) {
                    element = li;
                    triggerElementClickEvent_withoutMoveElement(element);

                    // In headless mode, might not fully trigger the dropdown’s closing behavior
                    WebElement outside = driver.findElement(By.tagName("body"));
                    outside.click();

                    GlobalMethods.printDebugInfo("Crypto Network: " + cryptoCurrencyNetworkTitle);
                    break;
                }
            } catch (NoSuchElementException e) {
                Assert.fail("Crypto Network (" + cryptoCurrencyNetworkDesc + ") not found");
            }
        }

        if (element == null) {
            Assert.fail("Crypto Network (" + cryptoCurrencyNetworkDesc + ") not found");
        }

        return cryptoCurrencyNetworkTitle;
    }

    public WebElement getCryptoWithdrawAmount() {
        return getCryptoWithdrawAmountEle();
    }

    public WebElement getCryptoToAddressErrorMsg() {
        return getCryptoToAddressErrorMsgEle();
    }

    public WebElement getWithdrawAmountErrorMsg() {
        return getWithdrawAmountErrorMsgEle();
    }

    public WebElement getWithdrawAmountInfoMsg() {
        return getWithdrawAmountInfoMsgEle();
    }

    public String getAvailableWithdrawAmount() {
        return getAvailableWithdrawAmountEle().getText().trim();
    }

    public String getWithdrawAmountSuffix() {
        return getCryptoWithdrawAmountSuffixEle().getText().trim();
    }

    public String getReviewWithdrawAmount() {
        return getReviewWithdrawAmountEle().getText().trim();
    }

    public String getWithdrawFromAccount() {
        return getWithdrawFromAccountEle().getText().trim();
    }

    public String getOrderWithdrawCryptoCurrency() {
        return getOrderWithdrawCryptoCurrencyEle().getText().trim();
    }

    public String getOrderWithdrawCryptoNetwork() {
        return getOrderWithdrawCryptoNetworkEle().getText().trim();
    }

    public String getOrderWithdrawWalletAddress() {
        return getOrderWithdrawWalletAddressEle().getText().trim();
    }

    public String getOrderWithdrawFromAccount() {
        return getOrderWithdrawFromAccountEle().getText().trim();
    }

    public String getOrderWithdrawAmount() {
        return getOrderWithdrawAmountEle().getText().trim();
    }

    public String getOrderWithdrawReceiveAmount() {
        return getOrderWithdrawReceiveAmountEle().getText().trim();
    }

    public WebElement getOrderWithdrawFee() {
        return getOrderWithdrawFeeEle();
    }

    public String getOrderWithdrawFeeFreeTag() {
        return getOrderWithdrawFeeFreeTagEle().getText().trim();
    }

    public String getReviewWithdrawFromAccount() {
        return getReviewWithdrawFromAccountEle().getText().trim();
    }

    public String getWithdrawFee() {
        return getWithdrawFeeEle().getText().trim().trim();
    }

    public WebElement getWithdrawFeeFree() {
        return getWithdrawFeeFreeEle();
    }

    public String getReviewWithdrawFee() {
        return getReviewWithdrawFeeEle().getText().trim();
    }

    public WebElement getReviewWithdrawFeeFree() {
        return getReviewWithdrawFeeFreeEle();
    }

    public WebElement getWithdrawFeeFreeTag() {
        return getWithdrawFeeFreeTagEle();
    }

    public WebElement getReviewWithdrawFeeFreeTag() {
        return getReviewWithdrawFeeFreeTagEle();
    }

    public String getReceiveAmount() {
        return getReceiveAmountEle().getText().trim();
    }

    public String getReviewReceiveAmount() {
        return getReviewReceiveAmountEle().getText().trim();
    }

    public String getReviewReceiveAmountSuffix() {
        return getReviewReceiveAmountSuffixEle().getText().trim();
    }

    public String getReviewWithdrawAddress() {
        return getReviewWithdrawAddressEle().getText().trim().trim();
    }

    public String getReviewWithdrawCryptoNetwork() {
        return getReviewWithdrawCryptoNetworkEle().getText().trim().trim();
    }

    public void clickWalletWithdrawBtn() {
        if (getCryptoNetworkIsFocus() != null){
            triggerElementClickEvent_withoutMoveElement(getCryptoNetworkIsFocus());
        }
        triggerElementClickEvent_withoutMoveElement(getWalletWithdrawBtn());
        GlobalMethods.printDebugInfo("Click Wallet Withdraw button");
        this.waitButtonLoader();
    }

    public void clickWalletWithdrawConfirmBtn() {
        // temp
        WebElement closeIcon = driver.findElement(By.cssSelector("svg.ht-icon-close"));
        closeIcon.click();
//        triggerElementClickEvent(getWalletWithdrawConfirmBtn());
//        GlobalMethods.printDebugInfo("Click Wallet Withdraw Confirm button");
//        this.waitButtonLoader();
    }

    public void checkWithdrawAuthenticator() {
        WebElement withdrawalAuthWindow = getWithdrawAuthenticatorEle();

        if (withdrawalAuthWindow != null && withdrawalAuthWindow.isDisplayed()) {
            GlobalMethods.printDebugInfo("Wallet Withdrawal Security Authenticator is enabled.");
            WebElement e = getWithdrawAuthenticatorConfirmBtnEle();
            triggerElementClickEvent(e);
            GlobalMethods.printDebugInfo("Click Wallet Withdrawal Security Authenticator Confirm button.");
            throw new SkipException("Wallet Withdrawal Security Authenticator is enabled. Stop continue...");
        } else {
            GlobalMethods.printDebugInfo("Wallet Withdrawal Security Authenticator is disabled");
        }
    }

    public void waitLoadingWalletWithdrawContent() {
        assertVisibleElementExists(By.xpath("//div[@class='withdraw-main-box']"),"Wallet Withdraw Content");
    }

    public void waitLoadingWalletWithdrawAmountContent() {
        WebElement e = checkElementExists(By.xpath("(//div[contains(@class,'el-step step-item')])[3]//div[@class='el-step__main']/div[contains(@class,'el-step__title is-wait')]"));

        if (e != null) {
            Assert.fail("Wallet Withdraw Amount Content is disabled");
        }

        assertVisibleElementExists(By.xpath("//div[@class='step-description-item last-step-item']"),"Wallet Amount Content");
    }

    public void waitLoadingWalletConfirmWithdrawContent() {
        assertVisibleElementExists(By.xpath("//div[contains(@class, 'conform-withdraw-dialog') and not(contains(@style, 'display: none'))]"),"Withdrawal Review Content");
    }

    public void waitLoadingWalletWithdrawOrderContent() {
        assertVisibleElementExists(By.xpath("//div[@class='order-detail-box']"),"Wallet Withdraw Order Content");
    }

    public void printWithdrawInfo(String withdrawAmount, String withdrawFromAccount, String withdrawFee, String withdrawFeeFreeTagDesc, String receiveAmount, String withdrawCryptoNetwork, String withdrawAddress, String availableWithdrawAmount) {
        System.out.println("************Withdraw Info************");
        System.out.printf("%-20s : %s\n", "Withdraw From", withdrawFromAccount);
        System.out.printf("%-20s : %s\n", "Available Withdraw", availableWithdrawAmount);
        System.out.printf("%-20s : %s\n", "Crypto Network", withdrawCryptoNetwork);
        System.out.printf("%-20s : %s\n", "Wallet Address", withdrawAddress);
        System.out.printf("%-20s : %s\n", "Withdraw Amount", withdrawAmount);
        System.out.printf("%-20s : %s\n", "Withdraw Fee", withdrawFee + (withdrawFeeFreeTagDesc.isEmpty() ? "" : " ("+ withdrawFeeFreeTagDesc +")"));
        System.out.printf("%-20s : %s\n", "Receive Amount", receiveAmount);
    }

    public void printReviewWithdrawInfo(String withdrawAmount, String withdrawFromAccount, String withdrawFee, String withdrawFeeFreeTagDesc, String receiveAmount, String withdrawCryptoNetwork, String withdrawAddress) {
        System.out.println("************Review Withdraw Confirmation************");
        System.out.printf("%-20s : %s\n", "Withdraw From", withdrawFromAccount);
        System.out.printf("%-20s : %s\n", "Crypto Network", withdrawCryptoNetwork);
        System.out.printf("%-20s : %s\n", "Wallet Address", withdrawAddress);
        System.out.printf("%-20s : %s\n", "Withdraw Amount", withdrawAmount);
        System.out.printf("%-20s : %s\n", "Withdraw Fee", withdrawFee + (withdrawFeeFreeTagDesc.isEmpty() ? "" : " ("+ withdrawFeeFreeTagDesc +")"));
        System.out.printf("%-20s : %s\n", "Receive Amount", receiveAmount);
    }

}

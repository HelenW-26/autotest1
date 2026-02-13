package newcrm.pages.clientpages.wallet;

import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties.WalletDepositMethod;
import newcrm.pages.Page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

public class WalletCryptoDepositPage extends Page {

    public WalletCryptoDepositPage(WebDriver driver) {
        super(driver);
    }

    protected WebElement getDepositAddress() {
        return assertElementExists(By.xpath("//div[@class='address-text']//div[@class='copy-text']"), "Wallet Address");
    }
    protected WebElement getCryptoAddressEle() {
        return assertElementExists(By.xpath("//div[@class='address-text']//div[@class='copy-text']"), "Wallet Address");
    }
    // 出金Review-地址
    protected WebElement getCryptoWithdrawAddress() {
        return assertElementExists(By.xpath("//div[text()=' Withdraw Address ']/..//div[@class='copy-text']"), "Wallet Withdraw Address");
    }

    protected WebElement getCryptoAddressCopyBtn() {
        return assertElementExists(By.xpath("//div[@class='copy-container']"), "Wallet Address copy button");
    }

    protected WebElement getCryptoCurrencyEle() {
        return assertElementExists(By.xpath("//div[@class='deposit-selector currency-input']//div[contains(@class, 'el-select ht-select')]"), "Crypto Currency List");
    }

    protected WebElement getCryptoNetworkEle() {
        return assertElementExists(By.xpath("//div[@class='deposit-selector']//div[contains(@class, 'el-select ht-select')]"), "Crypto Network List");
    }

    protected String getCryptoNetworkByIdx(int idx) {
        return assertElementExists(By.xpath("//div[contains(@class,'el-select-dropdown el-popper') and not(contains(@style, 'display: none'))]//li[" + idx + "]//div[@class='info-item-title']"), "Crypto Network Title").getText();
    }

    protected WebElement getCryptoAddressQRCodeEle() {
        return assertElementExists(By.xpath("//div[@class='address']//canvas"), "Wallet Address QR Code");
    }

    public void waitLoadingWalletDepositContent() {
        assertVisibleElementExists(By.xpath("//div[@class='wallet-deposit-content']"),"Wallet Deposit Content");
    }

    public void waitLoadingWalletDepositHistoryContent() {
        assertVisibleElementExists(By.xpath("//div[contains(@class,'deposit-history-box')]"),"Wallet Deposit History Content");
    }

    public void waitLoadingDepositCryptoAddressContent() {
        WebElement e = checkElementExists(By.xpath("(//div[@class='deposit-item'])[3]//div[@class='right']/div[contains(@class,'title grey')]"));

        if (e != null) {
            Assert.fail("Wallet Address Content is disabled");
        }

        assertVisibleElementExists(By.xpath("//div[@class='address']"),"Wallet Deposit Address Content");
    }

    public void waitLoadingDepositFAQContent() {
        assertVisibleElementExists(By.xpath("//div[contains(@class,'deposit-faq-box')]"),"Wallet Deposit FAQ Content");
    }

    public void checkSelectedCryptoCurrency(WalletDepositMethod method) {
        // Click dropdown
        triggerClickEvent_withoutMoveElement(this.getCryptoCurrencyEle());

        // Get dropdown list values
        List<WebElement> allOpenElements = this.getAllOpendElements();
        GlobalMethods.printDebugInfo("Crypto Currency Size: " + allOpenElements.size());

        WebElement selectedElement = null;
        String cryptoCurrencyDesc = method.getCryptoCurrencyDisplayDesc();

        for (WebElement li : allOpenElements) {
            try {
                WebElement span = li.findElement(By.cssSelector("span.text-a"));
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

    public void setCryptoNetwork(WalletDepositMethod method) {
        // Click dropdown
        triggerClickEvent_withoutMoveElement(this.getCryptoNetworkEle());

        // Get dropdown list values
        List<WebElement> allOpenElements = this.getAllOpendElements();
        GlobalMethods.printDebugInfo("Crypto Network Size: " + allOpenElements.size());

        WebElement element = null;
        String cryptoCurrencyNetworkDesc = method.getCryptoNetworkDisplayDesc();

        for (WebElement li : allOpenElements) {
            try {
                WebElement span = li.findElement(By.cssSelector("span.value"));
                String cryptoCurrencyNetworkTitle = span.getText().trim();

                // Look for li element that match with the cryptocurrency desc
                if (cryptoCurrencyNetworkTitle.toLowerCase().contains(cryptoCurrencyNetworkDesc.toLowerCase())) {
                    element = li;
                    triggerClickEvent_withoutMoveElement(element);
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

    }

    public void setCryptoNetwork_Random() {
        // Click dropdown
        triggerClickEvent_withoutMoveElement(this.getCryptoNetworkEle());

        // Get dropdown list values
        List<WebElement> allOpenElements = this.getAllOpendElements();
        GlobalMethods.printDebugInfo("Crypto Network Size: " + allOpenElements.size());

        // Random select crypto network if there is more than one record
        WebElement e;
        int idx = 1;
        if (allOpenElements.size() > 1) {
            // select random element
            idx = new Random().nextInt(allOpenElements.size());
            e = allOpenElements.get(idx);
        } else {
            e = allOpenElements.get(0);
        }

        // Get selected Crypto Network
        String selectedCryptoNetwork = getCryptoNetworkByIdx(idx);

        triggerClickEvent_withoutMoveElement(e);

        GlobalMethods.printDebugInfo("Crypto Network: " + selectedCryptoNetwork);
    }

    public WebElement getCryptoAddressQRCode() {
        return getCryptoAddressQRCodeEle();
    }

    // 获取入金详情-地址
    public String getCryptoAddress() {
        WebElement e = getDepositAddress();
        String cryptoAddress = e.getText();
        GlobalMethods.printDebugInfo("Wallet Address: " + cryptoAddress);
        return cryptoAddress;
    }
    // 获取出金详情-地址
    public String getCryptoWithdrawalAddress() {
        WebElement e = getCryptoWithdrawAddress();
        String cryptoAddress = e.getText();
        GlobalMethods.printDebugInfo("Wallet Address: " + cryptoAddress);
        return cryptoAddress;
    }

    public String clickCryptoAddressCopyBtn() {
        // Click on copy button
        triggerElementClickEvent_withoutMoveElement(getCryptoAddressCopyBtn());

        // Create and append input field
        js.executeScript("""
            const input = document.createElement('input');
            input.type = 'text';
            input.id = 'tempPasteInput';
            input.style.position = 'fixed';
            input.style.top = '0';
            input.style.left = '0';
            input.style.opacity = '0';
            input.style.zIndex = '9999';
            document.body.appendChild(input);
            input.focus();
        """);

        // Focus the input field (in case JS didn't already)
        driver.findElement(By.id("tempPasteInput")).click();

        // Simulate Ctrl+V, to paste copy text into input field
        org.openqa.selenium.interactions.Actions actions = new org.openqa.selenium.interactions.Actions(driver);
        actions.keyDown(org.openqa.selenium.Keys.CONTROL).sendKeys("v").keyUp(org.openqa.selenium.Keys.CONTROL).perform();

        // Wait for paste to complete
        try { Thread.sleep(300); } catch (Exception e) {}

        // Read the pasted value from input field
        String pastedValue = (String) js.executeScript("return document.getElementById('tempPasteInput').value;");

        // Remove the input field from DOM
        js.executeScript("document.getElementById('tempPasteInput').remove();");

        GlobalMethods.printDebugInfo("Copied Wallet Address: " + pastedValue);

        return pastedValue;
    }

}

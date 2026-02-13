package newcrm.pages.ptclientpages.payout;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.WithdrawPage;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.HashMap;
import java.util.Map;

public class PTPayouPage extends WithdrawPage {
    public PTPayouPage(WebDriver driver) {
        super(driver);
    }
    public void gotoProTrading()
    {
        driver.navigate().refresh();
        waitLoading();
        WebElement pt = driver.findElement(By.xpath("//li[@data-testid='menu.propTrade']/div"));
        js.executeScript("arguments[0].click()",pt);
    }

    public void gotoDashboard()
    {
        WebElement dashboard = findVisibleElemntByXpath("//li[@data-testid='menu.propTradeDashboard']");
        dashboard.click();
        waitLoading();
    }

    public boolean clickPayout()
    {
        waitLoading();
        WebElement moreList = findClickableElementByXpath("//div[@class='account-container new-account']//div[@aria-haspopup='list']");
        moreList.click();

        WebElement payoutDiv = findClickableElementByXpath("//ul[contains(@id,'dropdown-menu')]//div[contains(text( ),'Payout')]");
        payoutDiv.click();

        WebElement submitBtn = findClickableElementByXpath("//button[@data-testid='gotoPayout']");
        submitBtn.click();

        String url = driver.getCurrentUrl();
        if(StringUtils.containsIgnoreCase(url,"payout"))
        {
            GlobalMethods.printDebugInfo("go to payout page successfully");
            return true;
        }
        else
        {
            return  false;
        }

    }

    Map<String,String> payoutMethodXpath = new HashMap<String,String>() {{
        put("CreditCard","//input[@value='creditcard']");
       // put("CryptoBitCOIN","//input[@value='cryptocurrencybitcoin']");
        put("CryptoBitCOIN","//label[(contains(@data-testid,'cryptocurrencybitcoin'))]");
        put("CryptoUSDTTRC","//label[(contains(@data-testid,'cryptocurrencyusdt'))]");
        put("CryptoETH","//label[(contains(@data-testid,'cryptocurrencyethcps'))]");
        put("CryptoUSDCERC","//label[(contains(@data-testid,'cryptocurrencyusdccps'))]");
        //put("CryptoETH","//input[@value='cryptocurrencyethcps']");
        //put("CryptoUSDCERC","//input[@value='cryptocurrencyusdccps']");
        put("Ewallet","//input[@value='ewallet']");
        put("EwalletSTICPAY","//input[@value='sticpay']");
        put("EwalletNeteller","//input[@value='Neteller']");
        put("EwalletSkrill","//input[@value='Skrill']");
        put("EwalletPerfectMoney","//input[@value='Perfect Money']");
    }};

    //select payment method and proceed to payment
    public HashMap<String,String> selectPayoutMethod(String pMethod)
    {
            waitLoading();
        {
            boolean isEwallet = pMethod.toLowerCase().contains("ewallet");
            if(isEwallet)
            {
                String xpath = payoutMethodXpath.get("Ewallet");
                WebElement ewalletBtn = driver.findElement(By.xpath(xpath));
                js.executeScript("arguments[0].click()",ewalletBtn);

                xpath = payoutMethodXpath.get(pMethod);
                ewalletBtn = driver.findElement(By.xpath(xpath));
                js.executeScript("arguments[0].click()", ewalletBtn);
                waitLoading();

                if(!pMethod.toLowerCase().contains("sticPay"))
                {
                    WebElement accName = findClickableElementByXpath("//input[@data-testid='accountName']");
                    accName.sendKeys("testaccName");

                    WebElement accNum = findClickableElementByXpath("//input[@data-testid='accountNumber']");
                    accNum.sendKeys("test@test.com");
                }
                else
                {
                    WebElement email = driver.findElement(By.xpath("//input[@type='email']"));
                    email.sendKeys("autotest@test.com");
                }

            }
            else if(pMethod.toLowerCase().contains("crypto"))
            {
                String xpath = payoutMethodXpath.get(pMethod);
                WebElement crypto = driver.findElement(By.xpath(xpath));
                crypto.click();
                waitLoading();

                WebElement address = driver.findElement(By.xpath("//input[@id='address']"));
                if(pMethod.toLowerCase().contains("bitcoin"))
                {
                    address.sendKeys("3HL7Gi1BC2c95KHyi4tQyv6T28psZdCafG");
                }
                if(pMethod.toLowerCase().contains("trc"))
                {
                    address.sendKeys("TQETUR38cL8nY1gQcr43sWgFFwTKSwQjLw");
                }
                if(pMethod.toLowerCase().contains("erc")| pMethod.toLowerCase().contains("eth"))
                {
                    address.sendKeys("0xC6067650a116153E6123Bb252A28252b9ee3eE1c");
                }
            }

        }
        //get refund and payout
        HashMap<String,String> refundAndProfit = new HashMap<>();
        WebElement refund = findVisibleElemntByXpath("//div[@class='payment-detail-box']//div[@class='detail']/ul[@class='detail-list'][2]/li[2]/span[2]");
        String refundM = refund.getText().split("\\s+")[1];

        WebElement profit = findVisibleElemntByXpath("//div[@class='payment-detail-box']//div[@class='detail']/ul[@class='detail-list'][2]/li[1]/span[2]");
        String profitM = profit.getText().split("\\s+")[1];

        refundAndProfit.put("refund",refundM);
        refundAndProfit.put("profit",profitM);

        GlobalMethods.printDebugInfo("refundM: "+ refundM + " profitM: " + profitM);
        return refundAndProfit;
    }

    public boolean proceedPayout()
    {
        WebElement proceedBtn = driver.findElement(By.xpath("//button[@data-testid='confirm']"));
        proceedBtn.click();

        WebElement payoutinfo = findVisibleElemntByXpath("//div[@class='info-container']/div");

        return StringUtils.containsIgnoreCase(payoutinfo.getText(), "Your payout request is being processed");
    }


}

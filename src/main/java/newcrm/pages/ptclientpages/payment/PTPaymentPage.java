package newcrm.pages.ptclientpages.payment;

import newcrm.global.GlobalProperties;
import newcrm.pages.Page;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.HashMap;
import java.util.Map;

public class PTPaymentPage extends Page {

    public PTPaymentPage(WebDriver driver)
    {
        super(driver);
    }
    Map<String,String> paymentMethodXpath = new HashMap<String,String>() {{
       put("CreditCard","//input[@value='creditcard']");
       put("CryptoBitCOIN","//input[@value='BITCOIN']");
       put("CryptoUSDTERC","//input[@value='USDT-ERC20']");
       put("CryptoUSDTTRC","//input[@value='USDT-TRC20']");
       put("CryptoETH","//input[@value='ETH']");
       put("CryptoUSDCERC","//input[@value='USDC-ERC20']");
       put("Ewallet","//input[@value='ewallet']");
       put("EwalletPerfectMoney","//input[@value='Perfect Money']");
       put("EwalletSTICPAY","//input[@value='Sticpay']");
    }};

    //select payout method and proceed to payment
    public void selectPaymentMethod(String pMethod)
    {
        {
            boolean isEwallet = pMethod.toLowerCase().contains("ewallet");
            if(isEwallet)
            {
                String xpath = paymentMethodXpath.get("Ewallet");
                WebElement ewalletBtn = driver.findElement(By.xpath(xpath));
                js.executeScript("arguments[0].click()",ewalletBtn);

                xpath = paymentMethodXpath.get(pMethod);
                ewalletBtn = driver.findElement(By.xpath(xpath));
                js.executeScript("arguments[0].click()",ewalletBtn);
            }
            else if(paymentMethodXpath.containsKey(pMethod))
            {
                String xpath = paymentMethodXpath.get(pMethod);
                WebElement creditcardBtn = driver.findElement(By.xpath(xpath));
                js.executeScript("arguments[0].click()",creditcardBtn);
            }

            }
        proceedPayment();
    }


    protected void proceedPayment()
    {
        WebElement proceedBtn = driver.findElement(By.xpath("(//button[@data-testid='confirm'])[2]"));
        proceedBtn.click();
    }

}

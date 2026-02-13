package newcrm.business.umbusiness;

import newcrm.business.vtbusiness.CPUnionPay;
import newcrm.global.GlobalMethods;
import newcrm.pages.umclientpages.UMCPUnionPayPage;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import java.util.List;

public class UMCPUnionPay extends CPUnionPay {

    public UMCPUnionPay(WebDriver driver)
    {
        super(driver);
        page = new UMCPUnionPayPage(driver);
    }

    public void checkPaymentDetails(String account,String amount)
    {
        amount = GlobalMethods.fmtAmount(amount);

        List<String> PaymentDetail = page.checkPaymentDetail();
        Assert.assertTrue(StringUtils.containsIgnoreCase(PaymentDetail.get(0),account),"The account: "+PaymentDetail.get(0)+" "+account+" in payment details is incorrect");
        Assert.assertTrue(StringUtils.containsIgnoreCase(PaymentDetail.get(1),amount),"The net deposit amount: "+PaymentDetail.get(1)+" "+amount+"in payment details is incorrect");
        GlobalMethods.printDebugInfo(String.format("The account: %s, net deposit amount: %s information are correct", PaymentDetail.get(0),PaymentDetail.get(1)));
    }
}

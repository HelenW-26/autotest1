package newcrm.business.businessbase.deposit;

import newcrm.pages.clientpages.deposit.CreditCardGooglePayPage;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import javax.validation.constraints.AssertTrue;

public class CPCreditCardGooglePay extends CPCreditCardDeposit{

    protected CreditCardGooglePayPage page;

    public CPCreditCardGooglePay(WebDriver driver) {
        super(new CreditCardGooglePayPage(driver));
        this.page = new CreditCardGooglePayPage(driver);
    }

    public CPCreditCardGooglePay(CreditCardGooglePayPage creditcardgooglepaypage) {
        super(creditcardgooglepaypage);
        this.page = creditcardgooglepaypage;
    }

    @Override
    public boolean checkThirdPartyIframe() {
        if (!page.checkIframeExists()) {
            return false;
        } else {
            return true;
        }
    }
}

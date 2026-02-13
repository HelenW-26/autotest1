package newcrm.business.pugbusiness;

import newcrm.business.businessbase.CPInterBankTrans;
import newcrm.global.GlobalProperties;
import org.openqa.selenium.WebDriver;

public class PUCPInterBankTrans extends CPInterBankTrans {

    public PUCPInterBankTrans(WebDriver driver) {
        super(driver);
    }

    @Override
    public void depositNew(String account, String amount, GlobalProperties.DEPOSITMETHOD depositMethod)
    {
        setAccountAndAmount(account, amount);
        setDepositMethod(depositMethod);
        clickContinue();
        uploadNew();
        payNow();
    }

}

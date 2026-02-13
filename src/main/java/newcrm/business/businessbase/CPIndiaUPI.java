package newcrm.business.businessbase;

import newcrm.pages.clientpages.deposit.IndiaUPIPage;
import org.openqa.selenium.WebDriver;

public class CPIndiaUPI extends DepositBase{

    protected IndiaUPIPage iupage;
    public CPIndiaUPI(WebDriver driver)
    {
        super(new IndiaUPIPage(driver));
        this.iupage = new IndiaUPIPage(driver);
    }
    @Override
    public void deposit(String account, String amount, String notes) {
        selectAccount(account);
        setAmount(amount);
        setNotes(notes);
    }

    public void goBack() {
        iupage.goBack();
    }
}

package newcrm.business.businessbase.deposit;

import org.openqa.selenium.WebDriver;

public class CPTaiwanQRDeposit extends CPLocalBankTrans{

	public CPTaiwanQRDeposit(WebDriver driver) {
		super(driver);
	}
	
    @Override
    public void deposit(String account, String amount, String notes, String taxid, String cardnum, String email) {
        page.selectAccount(account);
        page.setAmount(amount);
        page.setSupermarketCode();
        page.setNotes(notes);
    }
}

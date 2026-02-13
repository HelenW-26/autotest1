package newcrm.business.businessbase.deposit;

import org.openqa.selenium.WebDriver;

public class CPKoreaBankDeposit extends CPLocalBankTrans{

	public CPKoreaBankDeposit(WebDriver driver) {
		super(driver);
	}
	
    @Override
    public void deposit(String account, String amount, String notes, String taxid, String cardnum, String email) {
        page.selectAccount(account);
        page.setAmount(amount);
        page.setBankName();
        page.setCardNumber(cardnum);
        page.setNotes(notes);
    }
}

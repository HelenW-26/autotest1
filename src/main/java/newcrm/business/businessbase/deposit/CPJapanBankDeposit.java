package newcrm.business.businessbase.deposit;

import newcrm.business.businessbase.DepositBase;
import newcrm.global.GlobalMethods;
import org.openqa.selenium.WebDriver;

import newcrm.pages.clientpages.deposit.JapanBankTransferPage;

public class CPJapanBankDeposit extends DepositBase {
	
	protected JapanBankTransferPage jpbtpage;
	
	public CPJapanBankDeposit(WebDriver driver)
	{
		super(new JapanBankTransferPage(driver));
		this.jpbtpage = new JapanBankTransferPage(driver);
	}
	
	public void deposit(String account, String amount, String notes, String taxid, String cardnum, String email) {
		selectAccount(account);
		setAmount(amount);

		try {
			jpbtpage.setBankTransferEmail(email);
		}
		catch(Exception e)
		{
			GlobalMethods.printDebugInfo("No need to set email");
		}

	}
}

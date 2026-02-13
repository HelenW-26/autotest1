package newcrm.business.businessbase;


import org.openqa.selenium.WebDriver;
import newcrm.pages.clientpages.deposit.SkrillPayPage;

public class CPEbuyDeposit extends DepositBase {
	protected SkrillPayPage skrillpaypage;

	public CPEbuyDeposit(SkrillPayPage skrillpaypage) {
		super(skrillpaypage);
		this.skrillpaypage = skrillpaypage;
	}
	
	public CPEbuyDeposit(WebDriver driver) {
		super(new SkrillPayPage(driver));
		this.skrillpaypage = new SkrillPayPage(driver);
	}

	@Override
	public void deposit(String account, String amount, String notes) {
		selectAccount(account);
		setAmount(amount);
		//setEmail("testewallet@test.com");
		setNotes(notes);
		submit();
	}
}

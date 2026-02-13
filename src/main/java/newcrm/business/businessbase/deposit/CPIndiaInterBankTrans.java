package newcrm.business.businessbase.deposit;

import newcrm.business.businessbase.CPInterBankTrans;
import newcrm.pages.auclientpages.IndiaInterBankTransPage;
import org.openqa.selenium.WebDriver;

public class CPIndiaInterBankTrans extends CPInterBankTrans {

	protected IndiaInterBankTransPage indibtpage;
	public CPIndiaInterBankTrans(WebDriver driver) {
		super(driver);
		this.indibtpage = new IndiaInterBankTransPage(driver);
	}

	@Override
	public void deposit(String account, String amount, String notes) {
		selectAccount(account);
		upload();
		setAmount(amount);
		setNotes(notes);
	}

	//tick checkbox
	public void checkTnc(){
		indibtpage.checkTnc();
	}

	//Input with iNR, get converted USD amount
	public String getConvertUSDamt(){
        return indibtpage.getConvertUSDamt();
	}
}

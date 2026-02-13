package newcrm.business.businessbase;

import newcrm.global.GlobalMethods;
import org.openqa.selenium.WebDriver;

import newcrm.pages.clientpages.withdraw.SkrillWithdrawPage;
import org.openqa.selenium.WebElement;

import java.util.List;


public class CPSkrillWithdraw extends CPWithdraw{

	protected SkrillWithdrawPage skrillpage;
	
	public CPSkrillWithdraw(SkrillWithdrawPage v_skrillpage) {
		super(v_skrillpage);
		this.skrillpage = v_skrillpage;
	}
	
	public CPSkrillWithdraw(WebDriver driver) {
		super(new SkrillWithdrawPage(driver));
		this.skrillpage = new SkrillWithdrawPage(driver);
	}
	
	public boolean setWithdrawInfoAndSubmit(String email,String notes) {
		skrillpage.setAccountName(email);
		skrillpage.setAccountNumber(email);
		skrillpage.setNotes(notes);
		return skrillpage.submit();
	}

	public void setWithdrawInfo(String email,String notes) {
		skrillpage.setAccountName(email);
		skrillpage.setAccountNumber(email);
		skrillpage.setNotes(notes);
	}

	public void submitWithoutCheck() {
		skrillpage.submitWithoutCheck();
	}
	public boolean submitWithCheck() {
		return skrillpage.submit();
	}
	public boolean setCodeSubmit() {
		return skrillpage.codeConfirm();
	}
	public boolean checkSavedAcctDropdown() {return skrillpage.checkSavedAcctDropdown();}

	public List<String> getSavedEwalletAccount() {
		List<String> accounts = skrillpage.getSavedAccount();
		if(accounts.size() ==0) {
			return null;
		}
		return accounts;
	}

	public boolean chooseSavedEwalletAccount(String account) {
		if(account==null || account.trim()=="") {
			return false;
		}
		return skrillpage.chooseSavedAccount(account);
	}

    public void setAccountNumber(String accountNumber) {
        skrillpage.setAccountNumber(accountNumber);
    }

	public boolean chooseAddNewEwalletAccount() {
		return skrillpage.chooseAddNewAccount();
	}
}

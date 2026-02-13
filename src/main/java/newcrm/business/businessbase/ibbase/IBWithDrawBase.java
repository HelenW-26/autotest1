package newcrm.business.businessbase.ibbase;

import java.util.List;

import org.openqa.selenium.WebDriver;

import newcrm.global.GlobalProperties.DEPOSITMETHOD;
import newcrm.pages.ibpages.DashBoardPage;
import newcrm.pages.ibpages.RebateWithdrawBasePage;
import newcrm.pages.ibpages.RebateWithdrawBasePage.Account;
public class IBWithDrawBase {

	protected RebateWithdrawBasePage page;
	protected DashBoardPage dashboard;
	
	public IBWithDrawBase(RebateWithdrawBasePage v_page) {
		this.page = v_page;
	}
	
	public IBWithDrawBase(WebDriver driver) {
		this.page = new RebateWithdrawBasePage(driver);
		dashboard = new DashBoardPage(driver);
		
	}
	
	public List<Account> getAllRebateAccounts(){
		return page.getAccountInfos();
	}

	public List<Account> getAllRebateAccountsNew(){
		return page.getAccountInfosNew();
	}
	
	public boolean setRebateAccount(String accNum) {
		List<Account> accs = this.getAllRebateAccounts();
		for(Account a: accs) {
			if(a.accNum.equalsIgnoreCase(accNum.trim())) {
				return page.setRebateAccount(accNum.trim());
			}
		}
		return false;
	}

	public boolean setRebateAccountNew(String accNum) {
		List<Account> accs = this.getAllRebateAccountsNew();
		for(Account a: accs) {
			if(a.accNum.equalsIgnoreCase(accNum.trim())) {
				return page.setRebateAccountNew(accNum.trim());
			}
		}
		return false;
	}
	
	public boolean setRebateAccount(Account account) {
		List<Account> accs = this.getAllRebateAccounts();
		if(!accs.contains(account)) {
			return false;
		}
		return page.setRebateAccount(account.accNum);
	}
	
	public void setAmount(Double amount) {
		page.setAmount(amount.toString());
	}
	
	public boolean setWithdrawMethod(DEPOSITMETHOD method) {
		/* Seems not useful..
		 * List<DEPOSITMETHOD> methods = page.getAllMethods();
		 * if(!methods.contains(method)) { return false; }
		 */
		
		page.setMethod(method);
		return true;
	}

	public boolean setWithdrawMethodNew(DEPOSITMETHOD method) {
		page.setMethodNew(method);
		return true;
	}

	public void clickWithdraw() {
		this.dashboard.clickWithDraw();
	}
	
	
	public void setNote(String note) {
		page.setNotes(note);
	}
	public boolean submit() {
		return page.submit();
	}

	public void clickContinue() {
		page.clickContinue();
	}
	
	public void clickLocalTransfer() {
		page.clickLocalTransfer();
	}
	
	public void clickEWallet() {
		page.clickEWallet();
	}
	
	public void clickCrypto() {
		page.clickCrypto();
	}
	
	public void clickIBT() {
		page.clickIBT();
	}
}

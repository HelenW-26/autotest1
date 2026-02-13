package newcrm.business.businessbase;

import java.util.List;

import newcrm.pages.clientpages.withdraw.BankTransferWithdrawPage;

public abstract class CPBankTransferWithdraw extends CPWithdraw {

	protected BankTransferWithdrawPage page;
	public CPBankTransferWithdraw(BankTransferWithdrawPage v_page) {
		super(v_page);
		this.page = v_page;
	}
	
	/**
	 * 
	 * @return If have saved return accounts
	 */
	public List<String> getSavedBankAccount() {
		List<String> accounts = page.getAllBankAccounts();
		if(accounts.size() ==0) {
			return null;
		}
		
		return accounts;
	}

	public List<String> getSavedBankAccountNew() {
		List<String> accounts = page.getAllBankAccountsNew();
		if(accounts.size() ==0) {
			return null;
		}

		return accounts;
	}
	
	public boolean selectSavedBankAccount(String account) {
		if(account==null || account.trim()=="") {
			return false;
		}
		/*
		List<String> accounts = page.getAllBankAccounts();
		
		if(!accounts.contains(account.toLowerCase().trim())) {
			return false;
		}
		*/
		
		return page.chooseBankAccount(account);
		
	}
	public boolean selectSavedBankAccountNew(String account) {
		if(account==null || account.trim()=="") {
			return false;
		}
		return page.chooseBankAccountNew(account);

	}
	
	public boolean selectNewBankAccount(String account) {
		if(account==null || account.trim()=="") {
			return false;
		}
		return page.chooseAddBankAccount();
		
	}
	
	public double getWithdrawAccount() {
		String amount = page.getWithdrawAmount();
		if(amount == null) {
			return -1;
		}
		return Double.valueOf(amount);
	}
}

package newcrm.business.businessbase;

import org.openqa.selenium.WebDriver;
import newcrm.pages.clientpages.withdraw.CryptoWithdrawPage;

import java.util.List;

public class CPCryptoWithdraw extends CPWithdraw{

	protected CryptoWithdrawPage cryptowithdrawpage;
	
	public CPCryptoWithdraw(WebDriver driver) {
		super(new CryptoWithdrawPage(driver));
		this.cryptowithdrawpage = new CryptoWithdrawPage(driver);
	}
	
	public CPCryptoWithdraw(CryptoWithdrawPage w_cryptopage) {
		super(w_cryptopage);
		this.cryptowithdrawpage = w_cryptopage;
	}
	
	public void setCryptoWithdrawalInfo(String walletaddress, String note) {
		cryptowithdrawpage.setWalletAddress(walletaddress);
		cryptowithdrawpage.setNotes(note);
		
		System.out.println("Set Wallet Address: " + walletaddress);

	}

	public void setCryptoWithdrawalInfo(String walletaddress, String usdtchain, String note) {
		cryptowithdrawpage.setUsdtChain(usdtchain);
		cryptowithdrawpage.setWalletAddress(walletaddress);
		cryptowithdrawpage.setNotes(note);
		
		System.out.println("Set USDT Chain: " + usdtchain);
		System.out.println("Set Wallet Address: " + walletaddress);

	}
	public void setCryptoWithdrawalInfoNew(String walletaddress, String note) {
		//cryptowithdrawpage.setUsdtChain(usdtchain);
		cryptowithdrawpage.setWalletAddressNew(walletaddress);
		cryptowithdrawpage.setNotes(note);

		//System.out.println("Set USDT Chain: " + usdtchain);
		System.out.println("Set Wallet Address: " + walletaddress);

	}
	
	public boolean submit() {
		return cryptowithdrawpage.submit();
	}

	public boolean checkSavedAcctDropdown() {return cryptowithdrawpage.checkSavedAcctDropdown();}

	public List<String> getSavedCryptoAccount() {
		List<String> accounts = cryptowithdrawpage.getSavedAccount();
		if(accounts.size() ==0) {
			return null;
		}
		return accounts;
	}

	public boolean chooseSavedCryptoAccount(String account) {
		if(account==null || account.trim()=="") {
			return false;
		}
		return cryptowithdrawpage.chooseSavedAccount(account);
	}

	public boolean chooseAddNewCryptoAccount() {
		return cryptowithdrawpage.chooseAddNewAccount();
	}
}

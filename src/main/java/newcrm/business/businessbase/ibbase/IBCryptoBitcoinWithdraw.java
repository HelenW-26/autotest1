package newcrm.business.businessbase.ibbase;

import org.openqa.selenium.WebDriver;

import newcrm.pages.ibpages.IBCryptBitcoinPage;

public class IBCryptoBitcoinWithdraw extends IBEmailWithdraw {

	protected IBCryptBitcoinPage cryptowithdrawpage;
	
	public IBCryptoBitcoinWithdraw(IBCryptBitcoinPage v_page) {
		super(v_page);
		this.page = v_page;
	}
	
	public IBCryptoBitcoinWithdraw(WebDriver driver) {
		super(new IBCryptBitcoinPage(driver));
		this.cryptowithdrawpage = new IBCryptBitcoinPage(driver);
	}
	
	public void setCryptoWithdrawalInfo(String walletaddress) {
		cryptowithdrawpage.setWithdrawAccount(walletaddress);
	}

	public void setCryptoWithdrawalInfoNew(String walletaddress) {
		cryptowithdrawpage.setWithdrawAccountNew(walletaddress);
	}
	
	public void setCryptoWithdrawalInfo(String walletaddress, String usdtchain) {
		cryptowithdrawpage.setUsdtChain(usdtchain);
		cryptowithdrawpage.setWithdrawAccount(walletaddress);
	}
}

package newcrm.business.businessbase;

import java.util.HashMap;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import newcrm.global.GlobalProperties;
import newcrm.global.GlobalProperties.CURRENCY;
import newcrm.global.GlobalProperties.DEPOSITMETHOD;
import newcrm.pages.clientpages.deposit.CryptoDepositPage;

public class CPCryptoDeposit extends CPSkrillPay {

	protected CryptoDepositPage cryptoDepositPage;

	public CPCryptoDeposit(CryptoDepositPage cryptoDepositPage) {
		super(cryptoDepositPage);
		this.cryptoDepositPage = cryptoDepositPage;
	}
	
	public CPCryptoDeposit(WebDriver driver) {
		super(new CryptoDepositPage(driver));
		this.cryptoDepositPage = new CryptoDepositPage(driver);
	}

	@Override
	public String checkAccount(CURRENCY currency) {
		HashMap<String, String> all_accounts = cryptoDepositPage.getAllAvailableAccounts();
		return this.GetAvailableAccount(CURRENCY.USD, all_accounts);
	}

	@Override
	public String getValidAccount() {
		page.waitLoadingDepositAccountContent();

		HashMap<String, String> all_accounts = page.getAllAccounts();

		if (all_accounts.isEmpty()) {
			return null;
		}

		// select usd currency only
		String account = this.GetAvailableAccountNew(CURRENCY.USD, all_accounts);

		if (account != null) {
			return account;
		}

		return null;
	}

	@Override
	public boolean checkHomeUrl() {
		return cryptoDepositPage.checkHomeUrl();
	}
	
	public void goToDepositMethod(DEPOSITMETHOD method) {
		cryptoDepositPage.goToDepositMethod(method.getCPTestId());
	}

	public WebElement getCryptoType1() {

		return cryptoDepositPage.getCryptoType1();

	}

	public WebElement getCryptoType2() {
		return cryptoDepositPage.getCryptoType2();

	}

	public WebElement getCryptoType3() {
		return cryptoDepositPage.getCryptoType3();
	}

	public WebElement getCryptoType4() {
		return cryptoDepositPage.getCryptoType4();

	}

	
	public String getAmountFromThirdParty(DEPOSITMETHOD method, String amount) {
		switch(method) {
		case CRYPTOBIT: return cryptoDepositPage.getBTCAmountThirdParty(amount);
		case CRYPTOOMNI: return cryptoDepositPage.getUSDTOAmountThirdParty(amount);
		case CRYPTOERC: return cryptoDepositPage.getUSDTEAmountThirdParty(amount);
		case CRYPTOTRC: return cryptoDepositPage.getUSDTTAmountThirdParty(amount);
		default: return null;
		}
	}
	public String getBTCAmountThirdParty(String amount) {
		return cryptoDepositPage.getBTCAmountThirdParty(amount);
	}

	public String getUSDTOAmountThirdParty(String amount) {
		return cryptoDepositPage.getUSDTOAmountThirdParty(amount);
	}

	public String getUSDTEAmountThirdParty(String amount) {
		return cryptoDepositPage.getUSDTEAmountThirdParty(amount);
	}

	public String getUSDTTAmountThirdParty(String amount) {
		return cryptoDepositPage.getUSDTTAmountThirdParty(amount);

	}

	public void deposit(String account, String amount, String notes) {
		selectAccount(account);
		setAmount(amount);
		setNotes(notes);
		submit();
		paymentConfirm(); /*will use this common method after all brands complete crypto UI upgrade*/
	}

	public void confirmButton() {
		cryptoDepositPage.confirmButton();
	}
	public void paymentConfirm() {
		cryptoDepositPage.paymentConfirm();
	}
	public boolean checkIfNavigateToThirdUrl() {

		if (!cryptoDepositPage.checkUrlContains(GlobalProperties.CRYPTOURL)) {
			return false;
		}
		return true;
	}

}

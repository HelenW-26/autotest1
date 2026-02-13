package newcrm.business.businessbase;

import org.openqa.selenium.WebDriver;

import newcrm.global.GlobalProperties;
import newcrm.pages.clientpages.deposit.ThaiBankTransPage;

public class CPThaiBankTransfer extends CPInterBankTrans {

	protected ThaiBankTransPage thaiBankTransPage;

	public CPThaiBankTransfer(ThaiBankTransPage thaiBankTransPage) {

		super(thaiBankTransPage);

		this.thaiBankTransPage = thaiBankTransPage;
	}

	public CPThaiBankTransfer(WebDriver driver) {
		super(new ThaiBankTransPage(driver));
		this.thaiBankTransPage = new ThaiBankTransPage(driver);
	}
	
	public double getAmountInThaiBankUI() {
		double amount1 = thaiBankTransPage.getDepositAmountFromThaiBankUI();
		return amount1;

	}

	public boolean checkIfNavigateToZotaURL() {
		if (thaiBankTransPage.checkUrlContains(GlobalProperties.THAIZotaURL)) {
			return true;
		} else
			return false;
	}

	public boolean checkIfNavigateToThaiEeziePayURL() {
		if (thaiBankTransPage.checkUrlContains(GlobalProperties.THAIEeziePayURL)) {
			return true;
		} else
			return false;
	}

	public boolean checkIfNavigateToThaiPayToDayURL() {
		if (thaiBankTransPage.checkUrlContains(GlobalProperties.THAIPayToDayURL)) {
			return true;
		} else
			return false;
	}

	public boolean checkIfNavigateToThaiXPayURL() {
		if (thaiBankTransPage.checkUrlContains(GlobalProperties.XpayURL)) {
			return true;
		} else
			return false;
	}

	public boolean checkIfNavigateToThaiPAURL() {
		if (thaiBankTransPage.checkUrlContains(GlobalProperties.PaymentAsiaURL)) {
			return true;
		} else
			return false;
	}

	public double getAmountInMalayUI() {
		double amount1 = thaiBankTransPage.getDepositAmountFromThaiBankUI();
		return amount1;

	}

	public boolean compareAmountWithZotapay(int amount) {
		int amount1 = amount;
		int amount2 = thaiBankTransPage.getDepositAmountFromZotaPay();

		if (amount1 == amount2) {
			System.out.println(
					"***Thai Deposit amount in Thai Bank Transfer UI is the same as it in the ZOTAPAY Channel ********");

			return true;
		}
		System.out.println(
				"***Thai Deposit amount in Thai Bank Transfer UI is the different from it in the ZOTAPAY Channel ********");
		return false;
	}

	public boolean compareAmountWithXPay(int amount) {
		int amount1 = amount;
		int amount2 = thaiBankTransPage.getDepositAmountFromXPay();
		if (amount1 == amount2) {
			System.out.println(
					"***Thai Deposit amount in Thai Bank Transfer UI is the same as it in the XPAY Channel ********");

			return true;
		}
		System.out.println(
				"***Thai Deposit amount in Thai Bank Transfer UI is the different from it in the XPAY Channel ********");
		return false;
	}

	public boolean compareAmountWithPayToDAY(int amount) {
		int amount1 = amount;
		int amount2 = thaiBankTransPage.getDepositAmountFromPayToDay();
		if (amount1 == amount2) {
			System.out.println(
					"***Thai Deposit amount in Thai Bank Transfer UI is the same as it in the PAYTODAY Channel ********");

			return true;
		}
		System.out.println(
				"***Thai Deposit amount in Thai Bank Transfer UI is the different from it in the PAYTODAY Channel ********");
		return false;
	}

	public boolean compareAmountWithPA(int amount) {
		int amount1 = amount;
		int amount2 = thaiBankTransPage.getDepositAmountFromThaiPA();
		if (amount1 == amount2) {
			System.out.println(
					"***Thai Deposit amount in Thai Bank Transfer UI is the same as it in the THAI PA Channel ********");

			return true;
		}
		System.out.println(
				"***Thai Deposit amount in Thai Bank Transfer UI is the different from it in the THAI PA Channel ********");
		return false;
	}

	public boolean compareAmountWithEeziePay(int amount) {
		int amount1 = amount;
		int amount2 = thaiBankTransPage.getDepositAmountFromEeziePay();
		if (amount1 == amount2) {
			System.out.println(
					"***Thai Deposit amount in Thai Bank Transfer UI is the same as it in the EEZIEPAY Channel ********");

			return true;
		}
		System.out.println(
				"***Thai Deposit amount in Thai Bank Transfer UI is the different from it in the EEZIEPAY Channel ********");
		return false;
	}

//	public void moveToNotes() {
//		thaiBankTransPage.moveToNotes();
//
//	}

	public void goback() {
		thaiBankTransPage.goBack();
	}

	@Override
	public void submit() {
		thaiBankTransPage.submit();
	}

	@Override
	public void deposit(String account, String amount, String notes) {
		selectAccount(account);
		setAmount(amount);
		setNotes(notes);
	}
}

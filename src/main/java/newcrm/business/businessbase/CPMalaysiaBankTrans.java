package newcrm.business.businessbase;

import org.openqa.selenium.WebDriver;

import newcrm.global.GlobalProperties;
import newcrm.pages.clientpages.deposit.MalaysiaBankTransPage;

public class CPMalaysiaBankTrans extends CPInterBankTrans {

	private MalaysiaBankTransPage malayBankTransPage;

	public CPMalaysiaBankTrans(MalaysiaBankTransPage malaysiaBankTransPage) {

		super(malaysiaBankTransPage);

		this.malayBankTransPage = malaysiaBankTransPage;

	}

	public CPMalaysiaBankTrans(WebDriver driver) {
		super(new MalaysiaBankTransPage(driver));
		this.malayBankTransPage = new MalaysiaBankTransPage(driver);
	}
	
	public void secondaryButtonClick() {

		malayBankTransPage.secondaryButton().click();
	}

	public boolean checkIfNavigateToZotaURL() {
		if (malayBankTransPage.checkUrlContains(GlobalProperties.ZotapayURL)) {
			return true;
		} else
			return false;
	}

	public boolean checkIfNavigateToPayTrustURL() {
		if (malayBankTransPage.checkUrlContains(GlobalProperties.MALAYPAYTRUSTURL)) {
			return true;
		} else
			return false;
	}

	public void submitButton() {
		malayBankTransPage.submit();;
	}

	public double getAmountInMalayUI() {
		double amount1 = malayBankTransPage.getDepositAmountFromMalayBank();
		return amount1;

	}

	public boolean compareAmountWithThirdURL(double amount) {
		double amount1 = amount;
		double amount2 = malayBankTransPage.getDepositAmountFromThirdParty();
		if (amount1 == amount2) {
			System.out.println(
					"***Malaysian Ringgit amount in Malaysia Bank Transfer UI is the same as it in the third URL ********");

			return true;

		}

		System.out.println(
				"***Malaysian Ringgit amount in Malaysia Bank Transfer UI is the different from it in the third URL ********");
		return false;

	}

	@Override
	public void deposit(String account, String amount, String notes) {
		selectAccount(account);
		setAmount(amount);
		setNotes(notes);

	}

	public void moveToNoteBox() {
		malayBankTransPage.moveToNoteBox();
	}

}

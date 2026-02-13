package newcrm.business.businessbase;

import org.openqa.selenium.WebDriver;

import newcrm.pages.clientpages.withdraw.CreditCardWithdrawPage;
import org.openqa.selenium.WebElement;

public class CPCreditCardWithdraw extends CPSkrillWithdraw {

	protected CreditCardWithdrawPage creditCardWithdrawPage;

	public CPCreditCardWithdraw(CreditCardWithdrawPage creditCardWithdrawPage) {
		super(creditCardWithdrawPage);
		this.creditCardWithdrawPage = creditCardWithdrawPage;
	}
	
	public CPCreditCardWithdraw(WebDriver driver) {
		super(new CreditCardWithdrawPage(driver));
		this.creditCardWithdrawPage = new CreditCardWithdrawPage(driver);
	}

	public int getCCAmount1() {

		return creditCardWithdrawPage.getAmount1();

	}

	public int getCCAmount2() {

		return creditCardWithdrawPage.getAmount2();

	}

	public int getCCTotalAmount() {

		return creditCardWithdrawPage.getCCTotalAmount();

	}
	public void handleCreditPopup() {
	creditCardWithdrawPage.handleCreditPopUp();

	}

	public int getSkrillAmount(int amount) {

		int skrillAmount = amount - getCCAmount1() - getCCAmount2();
		System.out.println("Skrill Withdraw Amount is : " + skrillAmount);
		return skrillAmount;
	}

	@Override
	public boolean setWithdrawInfoAndSubmit(String email, String notes) {
		skrillpage.setEmail(email);
		// skrillpage.setNotes(notes);
		return skrillpage.submit();
	}
	public boolean setWithdrawInfoAndSubmitNew(String email, String notes) {
		return skrillpage.submit();
	}

	public String getCCWithdrawTabText() {

		return creditCardWithdrawPage.getPageTextContent();

	}
}

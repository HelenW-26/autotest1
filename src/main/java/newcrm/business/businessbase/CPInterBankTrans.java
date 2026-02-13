package newcrm.business.businessbase;

import newcrm.global.GlobalProperties;
import org.openqa.selenium.WebDriver;
import newcrm.pages.clientpages.deposit.InterBankTransPage;
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.util.List;

public class CPInterBankTrans extends DepositBase {

	protected InterBankTransPage interBankTransPage;

	public CPInterBankTrans(InterBankTransPage interBankTrans_Page) {
		super(interBankTrans_Page);
		this.interBankTransPage = interBankTrans_Page;
	}
	
	public CPInterBankTrans(WebDriver driver) {
		this(new InterBankTransPage(driver));
	}

	public void upload() {
		interBankTransPage.uploadFile();
	}

	public void deposit(String account, String amount, String notes) {
		selectAccount(account);
		upload();
		setAmount(amount);
		setNotes(notes);
		submit();
	}

	public void backHomeButton() {
		interBankTransPage.backHomeButton();
	}

	@Override
	public void depositNew(String account, String amount, GlobalProperties.DEPOSITMETHOD method) {
		setAccountAndAmount(account, amount);
		setDepositMethod(method);
		clickContinue();
		uploadNew();
		payNow();
	}

	public void uploadNew() {
		interBankTransPage.uploadFileNew();
	}

	//获取Deposit Funds 页面默认入金账号信息
	public List<WebElement> getDepositFundsDefaultAccountInfo(String brandName) throws IOException {

		return checkDepositFundsDefaultAccountInfo(brandName);
	}
}

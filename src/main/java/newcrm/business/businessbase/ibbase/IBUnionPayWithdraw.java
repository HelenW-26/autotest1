package newcrm.business.businessbase.ibbase;

import org.openqa.selenium.WebDriver;
import newcrm.pages.ibpages.IBUnionPayWithdrawPage;
import vantagecrm.Utils;

public class IBUnionPayWithdraw extends IBWithDrawBase {
	protected IBUnionPayWithdrawPage unionPage;
	public IBUnionPayWithdraw(WebDriver driver) {
		super(driver);
		unionPage = new IBUnionPayWithdrawPage(driver);
	}
	
	public void setbankInfo(String bankName,String BankAccNum,String branch,String bankAccName,String note) {
		unionPage.setBankName(bankName);
		unionPage.setCardHolderName(bankName);
		unionPage.setNationalID(BankAccNum);
		unionPage.setCardNum(BankAccNum);
		unionPage.setPhoneNum(BankAccNum);
		unionPage.setProvince();
		unionPage.setCity();
		unionPage.setBankBranch(branch);
		
		String filename = "\\src\\main\\resources\\vantagecrm\\doc\\ID_Card.png";
		unionPage.uploadCard(Utils.workingDir + filename);
		unionPage.confirm();
	}
	
	public boolean checkUnionPopOut() {
		return unionPage.checkUnionPopOut();
	}
	
	public void addSuccessPopup() {
		unionPage.addSuccessPopup();
	}
	
	public void unionPayNotification() {
		unionPage.unionPayNotification();
	}
	
	public boolean submit() {
		return unionPage.submit();
	}
}

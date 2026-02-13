package newcrm.business.businessbase.deposit;

import org.openqa.selenium.WebDriver;
import newcrm.pages.clientpages.deposit.BridgePayDepositPage;

public class CPBridgePayDeposit extends CPCreditCardDeposit {

	protected BridgePayDepositPage page;
	public CPBridgePayDeposit(WebDriver driver) {
		super(new BridgePayDepositPage(driver));
		this.page = new BridgePayDepositPage(driver);
	}
	
	public CPBridgePayDeposit(BridgePayDepositPage bridgepaydepositpage) {
		super(bridgepaydepositpage);
		this.page = bridgepaydepositpage;
	}

//	public boolean compareAmountWithThirdURL(Double amount) {
//		double amount1 = amount;
//		double amount2 = bridgepaydepositpage.getAmountFromBridgePay();
//		if(amount1==amount2) {
//			System.out.println(
//					"***Bridge PAY displayed amount correctly ********");
//
//			return true;
//
//		}

//		System.out.println(
//				"***Bridge PAY displayed amount incorrectly ********");
//		return false;
//	}
//	public void moveNoteBox() {

//		page.moveNoteBox();
//	}
//		public double getAmountInBridgePay() {
//			double amount1 = bridgepaydepositpage.getAmountFromBridgePay();
//			return amount1;
//   
//		}

	@Override
	public boolean checkThirdPartyIframe() {
		if (!page.checkIframeExists()) {
			return false;
		} else {
			return true;
		}
	}
}

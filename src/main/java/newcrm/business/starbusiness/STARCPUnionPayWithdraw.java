package newcrm.business.starbusiness;

import newcrm.pages.starclientpages.STARUnionPayWithdrawPage;
import org.openqa.selenium.WebDriver;

import newcrm.business.businessbase.CPUnionPayWithdraw;
import newcrm.pages.clientpages.PaymentDetailsPage.Card;
import newcrm.pages.starclientpages.STARPaymentDetailsPage;
import vantagecrm.Utils;

public class STARCPUnionPayWithdraw extends CPUnionPayWithdraw{

	protected STARPaymentDetailsPage ppage;

	public STARCPUnionPayWithdraw(WebDriver driver) {
		super(driver);
		this.ppage = new STARPaymentDetailsPage(driver);
		unionpage = new STARUnionPayWithdrawPage(driver);
	}
	
	@Override
	public Card addNewUnionCard(String holderName,String id, String cardNum,String mobile,String branch) {
		Card card = ppage.new Card();
		ppage.addPaymentCard();
		ppage.setPaymentMethod();
		card.bankName = ppage.setBankName();
		ppage.setCardHolderName(holderName);
		card.holderName=holderName;
		ppage.setIDCard(id);
		card.ID = id;
		ppage.setBankCardNum(cardNum);
		card.cardNum = cardNum;
		ppage.setMoblieNum(mobile);
		card.branch = ppage.setBranch(branch);
		
		String filename = "\\src\\main\\resources\\vantagecrm\\doc\\ID_Card.png";
		ppage.uploadCard(Utils.workingDir + filename);
		return card;
	}
	
	@Override
	public boolean submitCardApply(String contain) {
		ppage.submit();
		String response = ppage.getResponse();
		if(response.contains(contain)) {
			return true;
		}
		return false;
	}
	
	@Override
	public void goBackHome() {
		ppage.confirm_submit();
	}
	
	@Override
	public boolean check(Card card) {
		Card latest = ppage.getLatestCard();
		return card.equals(latest);
	}
	
	@Override
	public void confirmPopup() {
		ppage.confirmPopup();
	}

	@Override
	public boolean submit() {
		return this.unionpage.submit();
	}
}

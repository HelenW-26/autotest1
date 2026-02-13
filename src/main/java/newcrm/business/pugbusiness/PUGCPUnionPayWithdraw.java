package newcrm.business.pugbusiness;

import org.openqa.selenium.WebDriver;

import newcrm.business.businessbase.CPUnionPayWithdraw;
import newcrm.pages.pugclientpages.PUGPaymentDetailsPage;

public class PUGCPUnionPayWithdraw extends CPUnionPayWithdraw{

	protected PUGPaymentDetailsPage ppage;

	public PUGCPUnionPayWithdraw(WebDriver driver) {
		super(driver);
		super.ppage = new PUGPaymentDetailsPage(driver);
	}

}

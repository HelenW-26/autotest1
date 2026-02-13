package newcrm.business.aubusiness;

import org.openqa.selenium.WebDriver;

import newcrm.business.businessbase.CPBrokerTransfer;
import newcrm.pages.clientpages.deposit.BrokerTransferPage;

public class AuCPBrokerTransfer extends CPBrokerTransfer {
	
	public AuCPBrokerTransfer(WebDriver driver) {
		super(new BrokerTransferPage(driver));

	}

}

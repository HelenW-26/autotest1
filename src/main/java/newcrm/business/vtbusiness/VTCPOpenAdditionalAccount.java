package newcrm.business.vtbusiness;

import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import org.openqa.selenium.WebDriver;

import newcrm.business.businessbase.CPOpenAdditionalAccount;
import newcrm.pages.vtclientpages.VTOpenAdditionalAccountPage;

public class VTCPOpenAdditionalAccount extends CPOpenAdditionalAccount {

	public VTCPOpenAdditionalAccount(WebDriver driver) {
		super(new VTOpenAdditionalAccountPage(driver));
	}
	public void setNote(String note) {
		if(!GlobalMethods.getPlatform().equalsIgnoreCase(GlobalProperties.PLATFORM.MTS.toString())) {
			page.setNote(note);
		}
	}

}

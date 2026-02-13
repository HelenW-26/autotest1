package newcrm.pages.umclient.register;

import newcrm.pages.clientpages.register.RegisterEntryPage;
import org.openqa.selenium.WebDriver;

import newcrm.pages.prosperoclientpages.register.PPRegisterEntryPage;

public class UMRegisterEntryPage extends RegisterEntryPage {

	public UMRegisterEntryPage(WebDriver driver) {
		super(driver);
	}
	
	@Override
	public boolean setCountry(String country) {
		return super.setCountry("France");
	}
}

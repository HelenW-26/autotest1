package newcrm.business.prospero.register;

import org.openqa.selenium.WebDriver;

import newcrm.business.aubusiness.register.ASICCPRegister;
import newcrm.global.GlobalMethods;
import newcrm.pages.prosperoclientpages.register.PPAccountConfigurationPage;
import newcrm.pages.prosperoclientpages.register.PPFinishPage;
import newcrm.pages.prosperoclientpages.register.PPRegisterEntryPage;
import newcrm.pages.prosperoclientpages.register.PPRegisterHomePage;

public class PPRegister extends ASICCPRegister {

	public PPRegister(WebDriver driver,String url) {
		super(driver,url);
	}
	
	@Override
	protected void setUpHomepage() {
		homepage = new PPRegisterHomePage(driver,registerURL);
	}
	
	@Override
	protected void setUpEntrypage() {
		entrypage  = new PPRegisterEntryPage(driver);
	}
	
	@Override
	protected void setUpACpage() {
		this.acpage = new PPAccountConfigurationPage(driver);
	}
	
	@Override
	protected void setUpFinishPage() {
		this.finishpage = new PPFinishPage(driver);
	}
	
	@Override
	public boolean goToIDPage() {
		acpage.next();
		String pagetitle = asic_idpage.getPageTitle();
		if(!pagetitle.trim().equalsIgnoreCase("CONFIRM YOUR ID")) {
			GlobalMethods.printDebugInfo("PPCPRegister: Page title is wrong. It is " + pagetitle + ". It should be " + "VERIFY YOUR IDENTITY" );
			return false;
		}
		GlobalMethods.printDebugInfo("CPRegister: go to CONFIRM YOUR ID page.");
		
		return true;
	}
}

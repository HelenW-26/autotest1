package newcrm.business.prospero.register;

import org.openqa.selenium.WebDriver;


import newcrm.pages.prosperoclientpages.register.PPProdFinishPage;
import newcrm.pages.prosperoclientpages.register.PPProdRegisterEntryPage;
import newcrm.pages.prosperoclientpages.register.PPProdRegisterHomePage;

public class PPProdRegister extends PPRegister {

	public PPProdRegister(WebDriver driver,String url) {
		super(driver,url);
	}
	
	@Override
	protected void setUpHomepage() {
		homepage = new PPProdRegisterHomePage(driver,registerURL);
	}
	
	@Override
	protected void setUpEntrypage() {
		entrypage  = new PPProdRegisterEntryPage(driver);
	}
	/*
	@Override
	protected void setUpFinishPage() {
		this.finishpage = new PPProdFinishPage(driver);
	}*/
	
	/*
	@Override
	public boolean goToFinishPage() {
		idpage.next();
		String result = finishpage.getResponse();
		if(!result.contains("Your application for a live account will not be approved until you successfully complete the suitability questionnaire")) {
			GlobalMethods.printDebugInfo("PPProdCPRegister: finish page get message: " + result);
			return false;
		}
		GlobalMethods.printDebugInfo("PPProdCPRegister: go to finish page.");
		return true;
	}*/
}

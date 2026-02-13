package newcrm.business.pugbusiness.register;

import org.openqa.selenium.WebDriver;

import newcrm.business.businessbase.CPRegister;
import newcrm.pages.pugclientpages.register.PUGAccountConfigurationPage;
import newcrm.pages.pugclientpages.register.PUGFinancialDetailsPage;
import newcrm.pages.pugclientpages.register.PUGFinishPage;
import newcrm.pages.pugclientpages.register.PUGPersonalDetailsPage;
import newcrm.pages.pugclientpages.register.PUGResidentialAddressPage;
import newcrm.pages.vtclientpages.register.VTAccountConfigurationPage;
import newcrm.pages.vtclientpages.register.VTFinancialDetailsPage;
import newcrm.pages.vtclientpages.register.VTFinishPage;
import newcrm.pages.vtclientpages.register.VTPersonalDetailsPage;
import newcrm.pages.vtclientpages.register.VTResidentialAddressPage;

public class FSACPRegister extends CPRegister {

	public FSACPRegister(WebDriver driver,String url) {
		super(driver,url);
		
	}
	
	protected void setUpPDpage() {
		this.pdpage = new PUGPersonalDetailsPage(driver);
	}



	protected void setUpAddresspage() {
		addresspage = new PUGResidentialAddressPage(driver);
	}
	
	@Override
	protected void setUpFDpage() {
		fdpage = new PUGFinancialDetailsPage(driver);
	}
	

	
	
	@Override
	protected void setUpACpage() {
		acpage = new PUGAccountConfigurationPage(driver);
	}
	
	
	@Override
	protected void setUpFinishPage() {
		finishpage = new PUGFinishPage(driver);
	}




























}

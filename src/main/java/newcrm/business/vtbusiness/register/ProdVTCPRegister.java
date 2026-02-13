package newcrm.business.vtbusiness.register;

import newcrm.pages.vtclientpages.VTMenuPage;
import newcrm.pages.vtclientpages.register.ProdVTConfirmIDPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties.USERTYPE;
import newcrm.pages.vtclientpages.register.ProdVTRegisterEntryPage;
import newcrm.pages.vtclientpages.register.ProdVTRegisterHomePage;
import utils.LogUtils;

public class ProdVTCPRegister extends VTCPRegister {

	public ProdVTCPRegister(WebDriver driver, String registerURL) {
		super(driver,registerURL);
	}

	@Override
	protected void setUpIDpage() {
		idpage = new ProdVTConfirmIDPage(driver);
	}
	
	@Override
	public void setTradeUrl(String url) {
		LogUtils.info("Prod enviroment do not need set trade url");
	}
	
	@Override
	public boolean setRegulatorAndCountry(String country,String regulator) {
		return true;
	}

	@Override
	public boolean setUserType(USERTYPE type) {
		LogUtils.info("VT Prod enviroment do not need set user type");
		return true;
	}

	@Override
	public boolean goToPersonalDetailPage() {
		VTMenuPage menu = new VTMenuPage(this.driver);
		menu.closeCouponGuideDialog();
		pdpage.closeImg();
		LogUtils.info("CPRegister: go to personal details page.");
		return true;
	}

	@Override
	protected void setUpHomepage() {
		homepage = new ProdVTRegisterHomePage(driver,registerURL);
	}

	@Override
	protected void setUpEntrypage() {
		entrypage = new ProdVTRegisterEntryPage(driver);
	}

	@Override
	public void fillPersonalDetails(String idNum,String firstName, String lastName,String phone) {
		try
		{
			if(driver.findElement(By.xpath("(//img[@class='closeImg'])[2]")).isDisplayed())
			{
				driver.findElement(By.xpath("(//img[@class='closeImg'])[2]")).click();
			}
		}
		catch(Exception e)
		{
			LogUtils.info("No popup window display");
		}
		userdetails.put("First Name", pdpage.setfirstName(firstName));
		userdetails.put("Last Name", pdpage.setLastName(lastName));
		userdetails.put("Phone Number", pdpage.setPhone(phone));
		userdetails.put("Date Of Birth", pdpage.setBirthDay());
		userdetails.put("gender", pdpage.setGender("Female"));
	}

	@Override
	public boolean goToFinishPage() {
		return true;
	}

	@Override
	public void entrySubmit(String traderURL) {}

}

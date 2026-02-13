package newcrm.business.aubusiness.register;

import org.openqa.selenium.WebDriver;

import newcrm.business.businessbase.CPRegister;
import newcrm.global.GlobalMethods;
import newcrm.pages.auclientpages.Register.ASICConfirmIDPage;
import newcrm.pages.auclientpages.Register.ASICResidentialAddressPage;
import newcrm.pages.auclientpages.Register.CIMAFinancialDetailsPage;
import newcrm.pages.clientpages.register.ConfirmIDPage;
import utils.LogUtils;
import vantagecrm.Utils;


public class ASICCPRegister extends CPRegister {

	protected CIMAFinancialDetailsPage asic_fdpage;
	protected ASICConfirmIDPage asic_idpage;
	public ASICCPRegister(WebDriver driver,String url) {
		super(driver,url);
		asic_fdpage = new CIMAFinancialDetailsPage(driver);
		asic_idpage = new ASICConfirmIDPage(driver);
	}
	
	@Override
	public boolean setRegulatorAndCountry(String country,String regulator) {
		//country = "Australia";//must be Australia in asic
		
		if(!entrypage.setCountry(country)) {
			return false;
		}
		
		if(!entrypage.setRegulator(regulator)) {
			return false;
		}
		userdetails.put("Country", country);
		userdetails.put("Regulator", regulator);
		return true;
	}
	
	@Override
	protected void setUpAddresspage() {
		addresspage = new ASICResidentialAddressPage(driver);
	}
	
	@Override
	public void fillPersonalDetails(String idNum) {
		//userdetails.put("Person Title", pdpage.setPersonTitle());
		pdpage.setMiddleName("Automation Test");
		userdetails.put("Middle Name", "Automation Test");
		userdetails.put("Nationality", pdpage.setNationality());
		userdetails.put("Date Of Birth", pdpage.setBirthDay());
		//userdetails.put("Place Of Birth", pdpage.setBirthPlace());
		//userdetails.put("Identification Type", pdpage.setIdentificationType());
		//pdpage.setIDNumber(idNum);
		//userdetails.put("ID Number", idNum);
		//pdpage.setRefer("Automation Test");
		userdetails.put("referred by", "Automation Test");
	}
	
	@Override
	public void fillFinacialPage() {
		asic_fdpage.answerAllQuestionsWithoutReturn();
	}
	
	@Override
	public boolean goToIDPage() {
		acpage.next();
		String pagetitle = asic_idpage.getPageTitle();
		if(!pagetitle.trim().equalsIgnoreCase("VERIFY YOUR IDENTITY")) {
			LogUtils.info("ASICCPRegister: Page title is wrong. It is " + pagetitle + ". It should be " + "VERIFY YOUR IDENTITY" );
			return false;
		}
		
		asic_idpage.goToUpload();
		pagetitle = asic_idpage.getPageTitle();
		if(!pagetitle.trim().equalsIgnoreCase("VERIFY YOUR IDENTITY")) {
			LogUtils.info("ASICCPRegister: Page title is wrong. It is " + pagetitle + ". It should be " + "VERIFY YOUR IDENTITY" );
			return false;
		}
		LogUtils.info("ASICCPRegister: go to VERIFY YOUR IDENTITY page.");
		
		return true;
	}
	
	@Override
	public void fillIDPage() {
		String fileFront = "\\src\\main\\resources\\vantagecrm\\doc\\ID_Card.png"; //Driver_License2.jpg//Passport.png;Prod_ID.jpeg
		String fileBack = "\\src\\main\\resources\\vantagecrm\\doc\\ID_Card_Back.png";
		String filePOA = "\\src\\main\\resources\\vantagecrm\\doc\\ID_Card.png";
		idpage.uploadID(Utils.workingDir + fileFront);
		idpage.uploadID(Utils.workingDir + fileBack);
		idpage.uploadPOA(Utils.workingDir + filePOA);
		idpage.uploadPOA(Utils.workingDir + filePOA);
	}
	
	@Override
	public boolean goToFinishPage() {
		idpage.next();
		String result = finishpage.getResponse();
		if(!(result.contains("Your live trading account is currently being processed") || result.contains("Your application for a live account will not be approved until you successfully complete the suitability questionnaire") )) {
			LogUtils.info("ASICCPRegister: finish page get message: " + result);
			return false;
		}
		LogUtils.info("ASICCPRegister: go to finish page.");
		return true;
	}
}

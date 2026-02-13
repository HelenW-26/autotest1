package newcrm.business.aubusiness.register;

import java.util.Random;

import newcrm.pages.auclientpages.AUMenuPage;
import newcrm.pages.auclientpages.Register.FCAPersonalDetailsPage;
import newcrm.pages.clientpages.MenuPage;
import org.openqa.selenium.WebDriver;

import newcrm.business.businessbase.CPRegister;
import newcrm.global.GlobalMethods;
import newcrm.pages.auclientpages.Register.FCAFinancialDetailsPage;
import newcrm.pages.auclientpages.Register.FCAResidentialAddressPage;
import newcrm.pages.clientpages.register.ConfirmIDPage;
import utils.LogUtils;
import vantagecrm.Utils;

public class FCACPRegister extends CPRegister {
	private FCAResidentialAddressPage f_addresspage;
	private FCAFinancialDetailsPage f_fdpage;
	public FCACPRegister(WebDriver driver,String url) {
		super(driver,url);
		f_addresspage = new FCAResidentialAddressPage(driver);
		f_fdpage = new FCAFinancialDetailsPage(driver);
        pdpage = new FCAPersonalDetailsPage(driver);
	}
	
	@Override
	public boolean goToAddressPage() {
		pdpage.submit();
		String pageTitle = f_addresspage.getPageTitle();
		if(!pageTitle.trim().equalsIgnoreCase("ADDITIONAL DETAILS")) {
			LogUtils.info("CPRegister: Page title is wrong. It is " + f_addresspage.getPageTitle() + ". It should be " + "ADDITIONAL DETAILS" );
			return false;
		}
		String country = userdetails.get("Country");
		if(!country.trim().equalsIgnoreCase(f_addresspage.getCountry().trim())) {
			LogUtils.info("CPRegister: Country brought in from Home page is wrong. It is " + f_addresspage.getCountry() + ". It should be " + country);
			return false;
		}
		LogUtils.info("CPRegister: go to ADDITIONAL DETAILS page.");
		return true;
	}

    @Override
    public boolean goToIDPage() {
        acpage.next();
        return true;
    }
	
	@Override
	public void fillAddressDetails() {
		
		String streetNum = GlobalMethods.getRandomNumberString(3);
		String streetName = GlobalMethods.getRandomString(15);
		f_addresspage.setAddress(streetNum, streetName);
		userdetails.put("Street Number", streetNum);
		userdetails.put("Street Name", streetName);
		String state = GlobalMethods.getRandomString(6) + " test state";
		String suburb = GlobalMethods.getRandomString(6) + " test suburb";
		String postcode = GlobalMethods.getRandomNumberString(4);
		f_addresspage.setState(state);
		f_addresspage.setSuburb(suburb);
		f_addresspage.setPostcode(postcode);
		userdetails.put("State", state);
		userdetails.put("Suburb", suburb);
		userdetails.put("Postcode", postcode);
		
		String country = this.f_addresspage.getCountry();
		if(country.equalsIgnoreCase("United Kingdom")) {
			String InsID = GlobalMethods.getRandomString(15);
			this.f_addresspage.setInsuranceNumber(InsID);
			userdetails.put("InsuranceID", InsID);
		}
		
		//only in fca
		
		Random random = new Random();
		int year = random.nextInt(10);
        if(year < 4) {
            year = year + 4;
        }
		f_addresspage.setYearsAtAddress(String.valueOf(year));
		userdetails.put("Year of address", String.valueOf(year));
		
		userdetails.put("Tax Country",f_addresspage.setTaxResidencyCountry());
		
		if(year < 4) {
			userdetails.put("Previous Country", f_addresspage.setPreviousCountry());
			String p_streetNum = GlobalMethods.getRandomNumberString(3);
			String p_streetName = GlobalMethods.getRandomString(15);
			f_addresspage.setPreviousAddress(p_streetNum, p_streetName);
			userdetails.put("Previous Street Number", p_streetNum);
			userdetails.put("Previous Street Name", p_streetName);
			String p_state = GlobalMethods.getRandomString(6) + " test previous state";
			String p_suburb = GlobalMethods.getRandomString(6) + " test previous suburb";
			String p_postcode = GlobalMethods.getRandomNumberString(4);
			f_addresspage.setPreviousState(p_state);
			f_addresspage.setPreviousSuburb(p_suburb);
			f_addresspage.setPreviousPostcode(p_postcode);
			userdetails.put("Previous State", p_state);
			userdetails.put("Previous Suburb", p_suburb);
			userdetails.put("Previous Postcode", p_postcode);
		}
	}
	
	@Override
	public void fillFinacialPage() {
		userdetails.put("Employment Status", f_fdpage.setEmploymentStatus());
		userdetails.put("Occupation", f_fdpage.setOccupation());
		userdetails.put("Employment Sector", f_fdpage.setEmploymentSector());
		userdetails.put("Annual Income", f_fdpage.setEstimatedAnnualIncome());
		userdetails.put("Investment Portfolio", f_fdpage.setPortfolioSize());
		userdetails.put("Fund Account", f_fdpage.setIntendToFundAccount());
		userdetails.put("Trade Type", f_fdpage.setTypeOfTrade());
		userdetails.put("Expected Deposit", f_fdpage.setExpectedInitialDeposit());
		userdetails.put("Trade Value", f_fdpage.setDailyTradeValue());
		userdetails.put("Shares", f_fdpage.setShares());
		userdetails.put("Spot FX", f_fdpage.setSpotFX());
		userdetails.put("Equity Derivatives", f_fdpage.setEquityDerivatives());
		userdetails.put("FX CFDs", f_fdpage.setFXCFDs());
		userdetails.put("Trade Knowledge", f_fdpage.setKnowledge());
		userdetails.put("Trade Education", f_fdpage.setEducationOntrade());
	}
	
	@Override
	public void fillIDPage() {
		String fileFront = "\\src\\main\\resources\\vantagecrm\\doc\\ID_Card.png"; //Driver_License2.jpg//Passport.png;Prod_ID.jpeg
		String fileBack = "\\src\\main\\resources\\vantagecrm\\doc\\ID_Card_Back.png";
		String filePOA = "\\src\\main\\resources\\vantagecrm\\doc\\ID_Card.png";
		ConfirmIDPage idpage = new ConfirmIDPage(driver);
		idpage.uploadID(Utils.workingDir + fileFront);
		idpage.uploadID(Utils.workingDir + fileBack);
		idpage.uploadPOA(Utils.workingDir + filePOA);
		idpage.uploadPOA(Utils.workingDir + filePOA);
	}

    @Override
    public void fillPersonalDetails(String idNum,String firstName, String lastName,String phone) {
//        pdpage.setMiddleName("Automation Test");
        userdetails.put("Person Title", pdpage.setPersonTitle());
//        userdetails.put("Middle Name", "Automation Test");
        userdetails.put("Last Name", pdpage.setLastName(lastName));
        userdetails.put("Date Of Birth", pdpage.setBirthDay());
        userdetails.put("Place Of Birth", pdpage.setBirthPlace());
        userdetails.put("Identification Type", pdpage.setIdentificationType());
        pdpage.setIDNumber(idNum);
        userdetails.put("ID Number", idNum);
//        pdpage.setRefer("Automation Test");
        userdetails.put("referred by", "Automation Test");
    }

    @Override
    public boolean goToFinishPage() {
        idpage.next();
        return true;
    }

    @Override
    public boolean goToPersonalDetailPage() {
        //check
        MenuPage menu = new MenuPage(this.driver);
        menu.refresh();
        menu.changeLanguage("English");
        pdpage.goToPersonalDetailsPage();
        String pagetitle = pdpage.getPageTitle();
        if(!pagetitle.trim().equalsIgnoreCase("PERSONAL DETAILS")) {
            LogUtils.info("CPRegister: Page title is wrong. It is " + pdpage.getPageTitle() + ". It should be " + "PERSONAL DETAILS" );
            return false;
        }

        LogUtils.info("CPRegister: go to personal details page.");
        return true;
    }

    @Override
    public boolean goToAdditionalDetailsPage() {
        pdpage.submit();
//        String country = userdetails.get("Country");
//        String t_country = addresspage.getCountry().trim();
//        //special for Hong Kong
//        if(t_country.equalsIgnoreCase("Hong Kong")) {
//            t_country = "HongKong";
//        }
//        if(!country.trim().equalsIgnoreCase(t_country)) {
//            LogUtils.info("CPRegister: Country brought in from Home page is wrong. It is " + t_country + ". It should be " + country);
//            return false;
//        }
//        LogUtils.info("CPRegister: go to MAIN RESIDENTIAL ADDRESS page.");
        return true;
    }



}

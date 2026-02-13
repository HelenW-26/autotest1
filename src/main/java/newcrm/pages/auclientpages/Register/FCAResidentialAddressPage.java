package newcrm.pages.auclientpages.Register;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.register.ResidentialAddressPage;
import utils.LogUtils;

public class FCAResidentialAddressPage extends ResidentialAddressPage {

	public FCAResidentialAddressPage(WebDriver driver) {
		super(driver);
	}

    @Override
    protected WebElement getAddressInput() {
        return this.findVisibleElemntByXpath("//input[@data-testid='streetName']");
    }

    @Override
    protected WebElement getStateInput() {
        return this.findVisibleElemntByXpath("//input[@data-testid='provinceOrState']");
    }

    @Override
    protected WebElement getSuburbInput() {
        return this.findVisibleElemntByXpath("//input[@data-testid='cityOrSuburb']");
    }

	public void setYearsAtAddress(String years) {
		WebElement e = this.findVisibleElemntByXpath("//input[@data-testid='yearsAtAddress']");
		this.setInputValue(e, years);
	}
	
	public String setTaxResidencyCountry() {
		WebElement e = this.findClickableElemntBy(By.xpath("//div[@prop='taxResidencyCountryId'] //input"));
		this.moveElementToVisible(e);
		e.click();
		List<WebElement> els = driver.findElements(By.xpath("//div[@class='el-select-dropdown el-popper ht-select-dropdown' and not(contains(@style,'display'))]//li"));
		String result = this.selectRandomValueFromDropDownList(els);
		GlobalMethods.printDebugInfo("FCAResidentialAddressPage: Set Tax Residency Country to :" + result);
		return result;
	}
	
	public String setPreviousCountry() {
		WebElement e = this.findClickableElemntBy(By.id("previousCountryId"));
		this.moveElementToVisible(e);
		e.click();
		List<WebElement> els = driver.findElements(By.xpath("//div[@class='el-select-dropdown el-popper' and not(contains(@style,'display'))]//li"));
		String result = this.selectRandomValueFromDropDownList(els);
		GlobalMethods.printDebugInfo("FCAResidentialAddressPage: Set Previous Country to :" + result);
		return result;
	}

    @Override
    public String getCountry() {
        WebElement e = this.findVisibleElemntBy(By.xpath("//div[@prop='countryCode'] //input"));
        return e.getAttribute("value");
    }

    @Override
    public void setAddress(String streetnum,String streetname) {
        String country = this.getCountry();
        if(country==null || country.trim().length()==0) {
            LogUtils.info("ERROR: ResidentialAddressPage: Find country Failed");
            throw new NoSuchElementException("ResidentialAddressPage: Find country Failed");
        }

        if(country.trim().equalsIgnoreCase("France") ||country.trim().equalsIgnoreCase("United Kingdom")||country.trim().equalsIgnoreCase("Malaysia") ) {
            this.setInputValue(this.getStreetNumberInput(), streetnum);
            this.setInputValue(this.getAddressInput(), streetname);
        }else {
            this.setInputValue(this.getAddressInput(),streetnum + " "+ streetname);
        }

        LogUtils.info("ResidentialAddressPage: Set Address to :" + streetnum + " "+ streetname);
    }

    @Override
    public void setState(String state) {
        this.setInputValue(this.getStateInput(), state);
        LogUtils.info("ResidentialAddressPage: Set State to :" + state);
    }

	public void setPreviousAddress(String number,String address) {
		WebElement e_country = this.findClickableElemntBy(By.id("previousCountryId"));
		WebElement e_address = this.findVisibleElemntByXpath("//input[@data-testid='previousAddress']");
		
		String country = e_country.getAttribute("value");
		if(country==null || country.trim().length()==0) {
			GlobalMethods.printDebugInfo("ERROR: FCAResidentialAddressPage: Find country Failed");
			throw new NoSuchElementException("FCAResidentialAddressPage: Find country Failed");
		}
		
		if(country.trim().equalsIgnoreCase("France") ||country.trim().equalsIgnoreCase("United Kingdom") ) {
			WebElement e_number = this.findVisibleElemntByXpath("//input[@data-testid='previousStreetNumber']");
			this.setInputValue(e_number, number);
			this.setInputValue(e_address, address);
		}else {
			this.setInputValue(e_address,number + " "+ address);
		}
		
		GlobalMethods.printDebugInfo("FCAResidentialAddressPage: Set Previous Address to :" + number + " "+ address);
	}
	
	public void setPreviousState(String state) {
		WebElement e = this.findVisibleElemntByXpath("//input[@data-testid='previousState']");
		this.setInputValue(e, state);
		GlobalMethods.printDebugInfo("FCAResidentialAddressPage: Set Previous state to :" + state);
	}
	
	public void setPreviousSuburb(String suburb) {
		WebElement e = this.findVisibleElemntByXpath("//input[@data-testid='previousSuburb']");
		this.setInputValue(e, suburb);
		GlobalMethods.printDebugInfo("FCAResidentialAddressPage: Set Previous Suburb to :" + suburb);
	}
	
	public void setPreviousPostcode(String postcode) {
		WebElement e = this.findVisibleElemntByXpath("//input[@data-testid='previousPostcode']");
		this.setInputValue(e, postcode);
		GlobalMethods.printDebugInfo("FCAResidentialAddressPage: Set Previous Postcode to :" + postcode);
	}
	
	private WebElement getINSinput() {
		return this.findVisibleElemntByXpath("//input[@data-testid='nationalInsuranceNumber']");
	}
	
	public void setInsuranceNumber(String number) {
		WebElement e = this.getINSinput();
		this.setInputValue(e, number);
	}
}

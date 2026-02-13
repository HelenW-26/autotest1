package newcrm.pages.clientpages.register;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.NoSuchElementException;

import newcrm.global.GlobalMethods;
import newcrm.pages.Page;
import utils.LogUtils;

/**
 * This class base on vfsc2
 * @author FengLiu 22-10-2021
 *
 */
public class ResidentialAddressPage extends Page {

	public ResidentialAddressPage(WebDriver driver) {
		super(driver);
	}
	//methods for find elements
	
	/**
	 * 
	 * @return when the country is not UK and France, return address input
	 */
	protected WebElement getAddressInput() {
		 return this.findVisibleElemntByXpath("//input[@data-testid='address']");
	}
	
	/**
	 *  only for country is UK or France
	 * @return
	 */
	protected WebElement getStreetNumberInput() {
		return this.findVisibleElemntByXpath("//input[@data-testid='streetNumber']");
	}
	
	protected WebElement getStateInput() {
		return this.findVisibleElemntByXpath("//input[@data-testid='state']");
	}
	
	protected WebElement getSuburbInput() {
		return this.findVisibleElemntByXpath("//input[@data-testid='suburb']");
	}
	
	protected WebElement getPostCodeInput() {
		return this.findVisibleElemntByXpath("//input[@data-testid='postcode']");
	}
	
	protected WebElement getNextButtom() {
		return this.findVisibleElemntByXpath("//button[@data-testid='next']");
	}
	
	public String getCountry() {
		WebElement e = this.findVisibleElemntBy(By.id("countryCode"));
		return e.getAttribute("value");
	}
	
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
	
	public void setState(String state) {
		String country = this.getCountry();
		
		if(country.trim().equalsIgnoreCase("Australia")) {
			WebElement e = this.findClickableElemntBy(By.id("state"));
			this.moveElementToVisible(e);
			e.click();
			List<WebElement> ops = this.getAllOpendElements();
			state = this.selectRandomValueFromDropDownList(ops);
		}else {
			this.setInputValue(this.getStateInput(), state);
		}
		LogUtils.info("ResidentialAddressPage: Set State to :" + state);
	}
	
	public void setSuburb(String suburb) {
		this.setInputValue(this.getSuburbInput(), suburb);
		LogUtils.info("ResidentialAddressPage: Set suburb to :" + suburb);
	}
	
	public void setPostcode(String postcode) {
		this.setInputValue(this.getPostCodeInput(), postcode);
		LogUtils.info("ResidentialAddressPage: Set postcode to :" + postcode);
	}
	
	public void next() {
		WebElement e = this.getNextButtom();
		this.moveElementToVisible(e);
		e.click();
		this.waitLoading();
	}
	
	public String getPageTitle() {
		return this.findVisibleElemntByXpath("//*[self::h2 or self::h3]").getText();
	}
}

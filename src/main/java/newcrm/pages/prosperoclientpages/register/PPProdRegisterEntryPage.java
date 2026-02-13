package newcrm.pages.prosperoclientpages.register;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties.USERTYPE;

public class PPProdRegisterEntryPage extends PPRegisterEntryPage {

	public PPProdRegisterEntryPage(WebDriver driver) {
		super(driver);
	}
	
	@Override
	public List<String> getCountries(){
		ArrayList<String> result = new ArrayList<>();
		result.add("Australia");
		return result;
	}
	
	@Override
	public boolean setCountry(String country) {
		this.findVisibleElemntByXpath("//span[contains(text(),'Australia')]").click();
		GlobalMethods.printDebugInfo("PPProdRegisterEntryPage: set country to : " + country +".");
		return true;
	}
	
	@Override
	public boolean setRegulator(String regulator) {
		GlobalMethods.printDebugInfo("PPProdRegisterEntryPage: Do not set regulator.");
		return true;
	}
	
	@Override
	public List<String> getRegulators(){
		ArrayList<String> result = new ArrayList<>();
		result.add("ASIC");
		return result;
	}
	
	@Override
	public void setActionUrl(String url) {
		GlobalMethods.printDebugInfo("PPProdRegisterEntryPage: Do not set ActionUrl.");
	}
	
	@Override
	protected WebElement getLastNameInput() {
		return this.findVisibleElemntByXpath("//input[@name='lastname']");
	}
	
	@Override
	protected WebElement getFirstNameInput() {
		return this.findVisibleElemntByXpath("(//input[@class='register_input'])[2]");
	}
	
	@Override
	protected WebElement getPhoneInput() {
		return this.findVisibleElemntByXpath("(//input[@class='register_input'])[4]");
	}
	
	@Override
	protected WebElement getEmailInput() {
		return this.findVisibleElemntByXpath("(//input[@class='register_input'])[5]");
	}
	@Override
	public boolean setUserType(USERTYPE type) {
		GlobalMethods.printDebugInfo("PPProdRegisterEntryPage: User type must be Individual");
		return true;
	}
	
	@Override
	protected WebElement getSubmitButton() {
		return this.findClickableElemntBy(By.cssSelector("button.theme-btn.btn-style-one"));
	}
}

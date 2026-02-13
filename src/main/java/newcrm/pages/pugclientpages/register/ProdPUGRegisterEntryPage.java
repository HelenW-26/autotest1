
package newcrm.pages.pugclientpages.register;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import newcrm.pages.clientpages.register.RegisterEntryPage;
import utils.LogUtils;

public class ProdPUGRegisterEntryPage extends RegisterEntryPage {

	public ProdPUGRegisterEntryPage(WebDriver driver) {
		super(driver);
	}

	@Override
	protected WebElement getSendCodeBtnEle() {
		return assertClickableElementExists(By.xpath("//span[text()='Send Code']"), "Send Code button");
	}

	@Override
 
	public List<String> getCountries(){
		ArrayList<String> result = new ArrayList<>();
		this.getCountryInput().click();
		//this.clickElement(this.getCountryInput());
		List<WebElement> countries = this.getCountryElements();
		for(WebElement e:countries) {
			String country = e.getAttribute("innerText");
			if(country != null && country.trim().length()>0) {
				result.add(country.trim());
			}
		}
		this.clickElement(this.getCountryInput());
		return result;
	}
 @Override	
 protected List<WebElement> getCountryElements() {
	 
	 String js = "document.getElementsByClassName('account_opiton')[0].style.display='block';";
	    ((JavascriptExecutor)driver).executeScript(js);
	    this.waitLoading();
	 //String xpath = "//ul[@class='account_opiton']//li";
	 String xpath = "//ul[li[contains(text(),'Albania')]]//li";

	 String countryxPath = "//input[@id='country']";
	 WebElement Country = driver.findElement(By.xpath(countryxPath));
	 Country.click();
	 Country.click();

	 //this.findVisibleElemntByXpath("//*[@class=\"account_opiton\"]");
	 return driver.findElements(By.xpath(xpath));
	 }

	//url redirect according to ip and country： puprime.com or puprime.net
	@Override
	public boolean setCountry(String country) {

		//make redirect a little bit quicker
		driver.findElement(By.xpath("//li[@class='firstName']")).click();

		String currentURL = driver.getCurrentUrl();
		int count = 0;
		try{
			while(currentURL.contains("puprime.net") && count <=10 )
			{
				currentURL =  driver.getCurrentUrl();
				count++;
				waitvisible.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//i[contains(@class,'client-portal-loading')]")));
			}
			LogUtils.info("current URL is:" + currentURL);
		}
		catch(Exception e)
		{
			LogUtils.info("get current url failed");

		}
		waitLoading();
		this.getCountryInput().click();
		List<WebElement> countries = this.getCountryElements();
		for(WebElement e:countries) {
			//String value = e.getAttribute("innterText");
			String value = e.getText();
			if(value != null && value.trim().length()>0) {
				if(value.contains(country.trim())) {
					e.click();
					currentURL =  driver.getCurrentUrl();
					try{
						count = 0;
						while(currentURL.contains("puprime.com") && count <=10 )
						{
							currentURL =  driver.getCurrentUrl();
							count++;
							waitvisible.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//i[contains(@class,'client-portal-loading')]")));
						}
						LogUtils.info("current URL is:" + currentURL);
					}
					catch(Exception exp)
					{
						LogUtils.info("get current url failed");

					}
					LogUtils.info("RegisterEntryPage: set country to: " + country.trim());
					return true;
				}
			}
		}
		LogUtils.info("RegisterEntryPage: Do not find country : " + country +".");
		this.getCountryInput().click();
		return false;
	}

	@Override
	public void sendCode(String code)
	{
		LogUtils.info("Get code, start to send code in input box");
		WebElement emailCode = driver.findElement(By.xpath("//input[@class='el-input__inner']"));
		emailCode.sendKeys(code);
		waitLoading();
		WebElement nextBtn = driver.findElement(By.xpath("//button[@data-testid='changePw']"));
		nextBtn.click();

		waitLoading();
		LogUtils.info("Click nextBtn successfully");
	}

public void setFirstName(String name) {
	this.waitLoading();
	this.setInputValue(this.getFirstNameInput(), name);
	LogUtils.info("RegisterEntryPage: set First Name to: " + name);
}

public void setLastName(String name) {
	this.waitLoading();
	this.setInputValue(this.getLastNameInput(), name);
	LogUtils.info("RegisterEntryPage: set Last Name to: " + name);
}

public void setPhone(String phone) {
	this.waitLoading();
	this.setInputValue(this.getPhoneInput(), phone);
	LogUtils.info("RegisterEntryPage: set phone to: " + phone);
}

public void setEmail(String email) {
	this.waitLoading();
	this.setInputValue(this.getEmailInput(), email);
	LogUtils.info("RegisterEntryPage: set email to: " + email);
}
public WebElement getSubmitButton() {
	return driver.findElement(By.xpath("//button[@id='button']"));
}
}

package newcrm.pages.vtclientpages.register;

import java.util.List;

import newcrm.global.GlobalMethods;
import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import newcrm.pages.clientpages.register.RegisterEntryPage;
import utils.LogUtils;

public class ProdVTRegisterEntryPage extends RegisterEntryPage {

	public ProdVTRegisterEntryPage(WebDriver driver) {
		super(driver);
	}

	public void setIBcode(String code, boolean bIsEnvProd) {
		if(code==null || code.trim().length()<1) {
			return;
		}
		/*if(driver.getCurrentUrl().contains(code)) {
			return;
		}*/
		if (!code.equals("")) {
			String IBurl = driver.getCurrentUrl() +"?affid="+code.trim();
			//driver.navigate().to(IBurl);
			driver.get(IBurl);

			LogUtils.info("RegisterEntryPage: set register url to: " + driver.getCurrentUrl());
		}
	}
	
	@Override
	protected List<WebElement> getCountryElements(){
		String xpath = "//div[@class='country_option']";
		//System.out.println(driver.getCurrentUrl()+"/n"+ driver.getPageSource());
		/*
		waitvisible.until(d->{
			try {
				d.findElement(By.xpath(xpath));
				return true;
			}catch(Exception E) {
				return false;
			}
		});*/
		//this.findVisibleElemntByXpath(xpath);
		//System.out.println(driver.getPageSource());
		//this.waitLoading();
		return driver.findElements(By.xpath(xpath));
	}
	
	
	@Override
	protected WebElement getCountryInput() {
		return assertClickableElementExists(By.xpath("//input[@id='country']"), "Country");
	}

	@Override
	public void next() {
		WebElement button = driver.findElement(By.xpath("//button[@id='button']"));
		button.click();
		this.waitLoadingForCustomise(120);
	}

	@Override
	public void setPhone(String phone) {
		this.setInputValue(this.getPhoneInput(), phone);
		LogUtils.info("RegisterEntryPage: set phone to: " + phone);
	}
	protected WebElement getPhoneInput() {
		return this.findVisibleElemntBy(By.xpath("//input[@data-testid='mobile']"));
	}
	@Override
	public void setEmail(String email) {
		this.setInputValue(this.getEmailInput(), email);
		LogUtils.info("RegisterEntryPage: set email to: " + email);
		setPassword();
		checkUSResident();
	}

	public void setPassword()
	{
		WebElement password = driver.findElement(By.xpath("//input[@id='password']"));
		String upLetters = RandomStringUtils.random(3, 'A', 'Z', true, false);
		String loLetters = RandomStringUtils.random(3, 'a', 'z', true, false);
		String numLetters = RandomStringUtils.random(3, '0', '9', false, true);
		String specialLetters = RandomStringUtils.random(2, 36, 38, false, false);

		String pwd = upLetters
				.concat(loLetters)
				.concat(numLetters)
				.concat(specialLetters);
		LogUtils.info("RegisterEntryPage: set Pwd to: " + pwd);

		password.sendKeys(pwd);
	}
	public void checkUSResident()
	{
		try
		{
			if(driver.findElement(By.xpath("//div[@class='us_resident']]")).isDisplayed()) {
				driver.findElement(By.xpath("//div[@class='us_resident']]")).click();
			}
		}
		catch(Exception e)
		{
			LogUtils.info("non US resident is already checked");
		}
}
	@Override
	public boolean setCountry(String country) {
		WebElement e = getCountryInput();
		e.click();
		setInputValue_withoutMoveElement(e, country);
		LogUtils.info("RegisterEntryPage: set Country to: " + country);
		e.click();
		return true;
	}
}

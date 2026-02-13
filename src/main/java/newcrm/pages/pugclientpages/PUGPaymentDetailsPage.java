package newcrm.pages.pugclientpages;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.PaymentDetailsPage;

public class PUGPaymentDetailsPage extends PaymentDetailsPage{
	WebDriverWait wait03 = new WebDriverWait(driver, Duration.ofSeconds(20));
	
	public PUGPaymentDetailsPage(WebDriver driver) {
		super(driver);
	}

	@Override
	public void setCardHolderName(String name) {
		this.setInputValue(this.findVisibleElemntByXpath("//input[@id='name']"), name);
		GlobalMethods.printDebugInfo("PaymentDetailsPage: set card holder name: "+name);
	}
	
	@Override
	public void setIDCard(String id) {
		this.setInputValue(this.findVisibleElemntByXpath("//input[@id='nationalId']"), id);
		GlobalMethods.printDebugInfo("PaymentDetailsPage: set card national ID: "+id);
	}
	
	@Override
	public void setBankCardNum(String cardNumber) {
		this.setInputValue(this.findVisibleElemntByXpath("//input[@id='cardNumber']"), cardNumber);
		GlobalMethods.printDebugInfo("PaymentDetailsPage: set card number: "+cardNumber);
	}
	
	@Override
	public void setMoblieNum(String cardPhoneNumber) {
		this.setInputValue(this.findVisibleElemntByXpath("//input[@id='cardPhoneNumber']"), cardPhoneNumber);
		GlobalMethods.printDebugInfo("PaymentDetailsPage: set Mobile Phone Number for Bank: "+cardPhoneNumber);
	}
	

	@Override
	public String setBranch(String branch) {
		String item_xpath = "//div[@class='el-select-dropdown el-popper' and not(contains(@style,'display'))]//li";
		WebElement provinces = this.findClickableElementByXpath("//input[@id='province']");
		WebElement cities = this.findClickableElementByXpath("//input[@id='city']");
				
		this.moveElementToVisible(provinces);
		provinces.click();
		wait03.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(item_xpath)));
		List<WebElement> ops = driver.findElements(By.xpath(item_xpath));
		String name = this.selectRandomValueFromDropDownList(ops);
		String result = name;
		
		this.moveElementToVisible(cities);
		cities.click();
		wait03.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(item_xpath)));
		ops = driver.findElements(By.xpath(item_xpath));
		name = this.selectRandomValueFromDropDownList(ops);
		result = result + "-" + name + "-";
		
		this.setInputValue(this.findVisibleElemntByXpath("//input[@id='branchName']"), branch);
		result = result + branch;
		GlobalMethods.printDebugInfo("PaymentDetailsPage: set branch to: " + result);
		return result;
	}
	
	@Override
	public Card getLatestCard() {
		this.findClickableElementByXpath("//div[@class='el-table__body-wrapper is-scrolling-none']/table/tbody/tr[1]/td[3]//a").click();
		Card result = new Card();
		result.bankName = this.findVisibleElemntByXpath("(//form[@class='el-form el-form--label-top']//div//div//span)[1]").getAttribute("innerText").trim();
		result.cardNum = this.findVisibleElemntByXpath("(//form[@class='el-form el-form--label-top']//div//div//span)[2]").getAttribute("innerText").trim();
		result.branch = this.findVisibleElemntByXpath("(//form[@class='el-form el-form--label-top']//div//div//span)[3]").getAttribute("innerText").trim();
		result.holderName = this.findVisibleElemntByXpath("(//form[@class='el-form el-form--label-top']//div//div//span)[4]").getAttribute("innerText").trim();
		result.ID = this.findVisibleElemntByXpath("(//form[@class='el-form el-form--label-top']//div//div//span)[6]").getAttribute("innerText").trim();
		this.refresh();
		return result;
	}
}

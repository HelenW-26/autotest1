package newcrm.pages.clientpages;

import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import newcrm.global.GlobalMethods;
import newcrm.pages.Page;

public class PaymentDetailsPage extends Page {

	public class Card{
		public String bankName;
		public String cardNum;
		public String branch;
		public String holderName;
		public String ID;
		
		@Override
		public boolean equals(Object o) {
			if(!(o instanceof Card)) {
				return false;
			}
			Card t = (Card)o;
			if(this.branch.equals(t.branch) &&
					this.holderName.equals(t.holderName) &&
					this.ID.equals(t.ID)) {
				return true;
			}
			return false;
		}
		
		public void print() {
			System.out.println("* Bank Name: " + this.bankName);
			System.out.println("* Bank Card Number: " + this.cardNum);
			System.out.println("* Bank Branch: " + this.branch);
			System.out.println("* Card Holder Name: " + this.holderName);
			System.out.println("* National ID Card: " + this.ID);	
		}
		
	}
	
	
	public PaymentDetailsPage(WebDriver driver) {
		super(driver);
	}
	
	public String setPaymentMethod() {
		this.waitLoading();
		this.findClickableElementByXpath("//div[@data-testid='paymentMethod']").click();
		String result = this.selectRandomValueFromDropDownList(getAllOpendElements());
		GlobalMethods.printDebugInfo("PaymentDetailsPage: select payment method: "+result);
		return result;
	}
	
	public String setBankName() {
		this.findClickableElementByXpath("//div[@data-testid='bankName']").click();
		String result = this.selectRandomValueFromDropDownList(getAllOpendElements());
		GlobalMethods.printDebugInfo("PaymentDetailsPage: select Bank Name: "+result);
		return result;
	}
	
	public void setCardHolderName(String name) {
		this.setInputValue(this.findVisibleElemntByTestId("cardHolderName"), name);
		GlobalMethods.printDebugInfo("PaymentDetailsPage: set card holder name: "+name);
	}
	
	public void setIDCard(String id) {
		this.setInputValue(this.findVisibleElemntByTestId("nationalId"), id);
		GlobalMethods.printDebugInfo("PaymentDetailsPage: set card national ID: "+id);
	}
	
	public void setBankCardNum(String cardNumber) {
		this.setInputValue(this.findVisibleElemntByTestId("cardNumber"), cardNumber);
		GlobalMethods.printDebugInfo("PaymentDetailsPage: set card number: "+cardNumber);
	}
	
	public void setMoblieNum(String cardPhoneNumber) {
		this.setInputValue(this.findVisibleElemntByTestId("cardPhoneNumber"), cardPhoneNumber);
		GlobalMethods.printDebugInfo("PaymentDetailsPage: set Mobile Phone Number for Bank: "+cardPhoneNumber);
	}
	
	public String setBranch(String branch) {
		Select provinces = new Select(this.findVisibleElemntByXpath("(//*[@class='branchNameDetail'])[1]//select"));
		Select cities = new Select(this.findVisibleElemntByXpath("(//*[@class='branchNameDetail'])[2]//select"));
		int pos = 0;
		Random random = new Random();
		pos = random.nextInt(provinces.getOptions().size());
		provinces.selectByIndex(pos);
		String p = provinces.getFirstSelectedOption().getAttribute("value");
		
		pos = random.nextInt(cities.getOptions().size());
		
		cities.selectByIndex(pos);
		
		String c = cities.getFirstSelectedOption().getAttribute("value");
		this.setInputValue(this.findVisibleElemntByTestId("branchName"), branch);
		String result = p + "-"+c+"-"+branch;
		GlobalMethods.printDebugInfo("PaymentDetailsPage: set branch to: " + result);
		return result;
	}
	
	public void uploadCard(String card) {
		WebElement idinput = driver.findElement(By.xpath("//input[@type='file']"));
		idinput.sendKeys(card);
		GlobalMethods.printDebugInfo("PaymentDetailsPage: upload card: " + card);
		this.waitLoading();
	}
	
	public void submit() {
		this.findClickableElementByXpath("//span[contains(text(),'Submit')]").click();
		this.waitLoading();
	}
	
	public String getResponse() {
		WebElement e = this.findVisibleElemntBy(By.xpath("//div[contains(@class,'success-content') or contains(@class,'success_content')]"));
		String response = e.getAttribute("innerText");
		GlobalMethods.printDebugInfo("PaymentDetailsPage: get reponse: " + response);
		return response;
	}
	
	public void backToHome() {
		this.findClickableElemntByTestId("bkToHm").click();
		this.waitLoading();
	}
	
	public Card getLatestCard() {
		this.findClickableElementByXpath("//div[@class='el-table__body-wrapper is-scrolling-none']/table/tbody/tr[1]/td[3]//a").click();
		Card result = new Card();
		result.bankName = this.findVisibleElemntByXpath("(//div[@id='message_info']//ul/li/span)[1]").getAttribute("innerText").trim();;
		result.cardNum = this.findVisibleElemntByXpath("(//div[@id='message_info']//ul/li/span)[2]").getAttribute("innerText").trim();
		result.branch = this.findVisibleElemntByXpath("(//div[@id='message_info']//ul/li/span)[3]").getAttribute("innerText").trim();
		result.holderName = this.findVisibleElemntByXpath("(//div[@id='message_info']//ul/li/span)[4]").getAttribute("innerText").trim();
		result.ID = this.findVisibleElemntByXpath("(//div[@id='message_info']//ul/li/span)[5]").getAttribute("innerText").trim();
		this.findClickableElementByXpath("//div[@class='el-dialog__wrapper' and not(contains(@style,'none'))]//img[@class='closeImg']").click();
		return result;
	}
	
	public void confirmPopup() {
		try {
			WebElement confirm = driver.findElement(By.xpath("//span[contains(text(),'Confirm')]"));
			js.executeScript("arguments[0].click()", confirm);
		}catch (Exception e){
			GlobalMethods.printDebugInfo("No need to confirm pup up window: " + e.toString());
		}
	}
}

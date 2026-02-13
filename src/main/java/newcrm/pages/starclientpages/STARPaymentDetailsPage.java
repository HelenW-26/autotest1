package newcrm.pages.starclientpages;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.PaymentDetailsPage;

public class STARPaymentDetailsPage extends PaymentDetailsPage{
	WebDriverWait wait03 = new WebDriverWait(driver, Duration.ofSeconds(20));
	public STARPaymentDetailsPage(WebDriver driver) {
		super(driver);
	}
	
	public void addPaymentCard() {
		this.findClickableElementByXpath("//*[contains(text(), 'Payment Card')]").click();
		wait03.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@data-testid='paymentMethod']")));
	}
	
	@Override
	public String setBankName() {
		this.findClickableElementByXpath("//input[@id='bankName']").click();
		String result = this.selectRandomValueFromDropDownList(getAllOpendElements());
		GlobalMethods.printDebugInfo("PaymentDetailsPage: select Bank Name: "+result);
		return result;
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
		WebElement provinces = this.findClickableElemntByTestId("province");
		WebElement cities = this.findClickableElemntByTestId("city");
				
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
	public void submit() {
		this.findClickableElementByXpath("//div[@class='drawer_footer']//span[contains(text(),'CONFIRM')]").click();
		this.waitLoading();
	}
	
	@Override
	public String getResponse() {
		WebElement e = this.findVisibleElemntBy(By.xpath("//div[@class='dialog-container text-center']"));
		String response = e.getAttribute("innerText");
		GlobalMethods.printDebugInfo("PaymentDetailsPage: get reponse: " + response);
		return response;
	}
	
	public void confirm_submit() {
		this.findClickableElemntByTestId("confirm").click();
		this.waitLoading();
	}
	
	@Override
	public Card getLatestCard() {
		this.findClickableElementByXpath("//div[@class='el-table__body-wrapper is-scrolling-none']/table/tbody/tr[1]/td[3]//a").click();
		Card result = new Card();
		result.bankName = this.findVisibleElemntByXpath("//input[@id='bankName']").getAttribute("value").trim();
		result.cardNum = this.findVisibleElemntByXpath("//input[@id='cardNumber']").getAttribute("value").trim();
		result.branch = this.findVisibleElemntByXpath("//input[@id='province']").getAttribute("value").trim() + "-" +
				this.findVisibleElemntByXpath("//input[@id='city']").getAttribute("value").trim() + "-" +
				this.findVisibleElemntByXpath("//input[@id='branchName']").getAttribute("value").trim();
		result.holderName = this.findVisibleElemntByXpath("//input[@id='name']").getAttribute("value").trim();
		result.ID = this.findVisibleElemntByXpath("//input[@id='nationalId']").getAttribute("value").trim();
		this.findClickableElementByXpath("//div[@class='drawer_footer']//span[contains(text(), 'CONFIRM')]").click();
		return result;
	}
	
	@Override
	public void confirmPopup() {
		try {
			WebElement confirm = driver.findElement(By.xpath("//div[@class='el-dialog union-tips']//button[@data-testid='close']"));
			//WebElement confirm = this.findVisibleElemntByXpath("//div[@class='el-dialog union-tips']//button[@data-testid='close']");
			js.executeScript("arguments[0].click()", confirm);
		}catch (Exception e){
			GlobalMethods.printDebugInfo("No need to confirm pup up window: " + e.toString());
		}
	}
}

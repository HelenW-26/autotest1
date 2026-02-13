package newcrm.pages.ibpages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import newcrm.global.GlobalMethods;

public class IBUnionPayWithdrawPage extends RebateWithdrawBasePage {

	public IBUnionPayWithdrawPage(WebDriver driver){
		super(driver);
	}
	
	public void setBankName(String name) {
		this.setInputValue(this.findVisibleElemntByTestId("bankName"), name);
		GlobalMethods.printDebugInfo("IBUnionPayWithdrawPage: set bank name to: " + name);
	}
	
	public void setCardHolderName(String name) {
		this.setInputValue(this.findVisibleElemntByTestId("bankAccountName"), name);
		GlobalMethods.printDebugInfo("IBUnionPayWithdrawPage: Set Card Holder Name to: " + name);
	}
	
	public void setNationalID(String nationalId) {
		this.setInputValue(this.findVisibleElemntByTestId("bankAccountName"), nationalId);
		GlobalMethods.printDebugInfo("IBUnionPayWithdrawPage: Set National ID to: " + nationalId);
	}
	
	public void setCardNum(String cardNumber) {
		this.setInputValue(this.findVisibleElemntByTestId("accountNumber"), cardNumber);
		GlobalMethods.printDebugInfo("IBUnionPayWithdrawPage: Set Card Number to: " + cardNumber);
	}
	
	public void setPhoneNum(String phoneNumber) {
		this.setInputValue(this.findVisibleElemntByTestId("accountNumber"), phoneNumber);
		GlobalMethods.printDebugInfo("IBUnionPayWithdrawPage: Set Phone Number to: " + phoneNumber);
	}
	
	public String setProvince() {
		WebElement div = this.findVisibleElemntByTestId("province");
		this.moveElementToVisible(div);
		div.click();
		String province = this.selectRandomValueFromDropDownList(this.getAllOpendElements());
		GlobalMethods.printDebugInfo("IBUnionPayWithdrawPage: Set Province to: " + province);
		return province;
	}
	
	public String setCity() {
		WebElement div = this.findVisibleElemntByTestId("city");
		this.moveElementToVisible(div);
		div.click();
		
		String city = this.selectRandomValueFromDropDownList(this.getAllOpendElements());
		GlobalMethods.printDebugInfo("IBUnionPayWithdrawPage: Set City to: " + city);
		return city;
	}
	
	public void setBankBranch(String branch) {
		this.setInputValue(this.findVisibleElemntByTestId("branchName"), branch);
		GlobalMethods.printDebugInfo("IBUnionPayWithdrawPage: Set Bank Branch to: " + branch);
	}
	
	public void uploadCard(String card) {
		WebElement idinput = driver.findElement(By.xpath("//input[@type='file']"));
		idinput.sendKeys(card);
		GlobalMethods.printDebugInfo("IBUnionPayWithdrawPage: Upload card: " + card);
		this.waitLoading();
	}
	
	public void confirm() {
		this.findClickableElementByXpath("//div[@class='footer_content']//span[contains(text(),'Confirm')]").click();
		this.waitLoading();
	}
	
	public boolean checkUnionPopOut() {
		try {
			unionPayNotification();
			WebElement bindcardpopup = this.findClickableElementByXpath("//div[@class='el-dialog__footer']//button[@data-testid='submit']");
			js.executeScript("arguments[0].click()", bindcardpopup);
			GlobalMethods.printDebugInfo("*No Unionpay Card, bind new card");
			return true;
		}
		catch (Exception e) {
			return false;
		}	
	}
	
	public void addSuccessPopup() {
		try {
			WebElement confirm = driver.findElement(By.xpath("//div[@class='dialog-footer']//span[contains(text(),'Confirm')]"));
			js.executeScript("arguments[0].click()", confirm);
		}catch (Exception e){
			GlobalMethods.printDebugInfo("No success pop out page: " + e.toString());
		}
	}
	
	public void unionPayNotification() {
		try {
			WebElement confirm = driver.findElement(By.xpath("//div[@class='el-dialog union-tips']//button[@data-testid='close']"));
			js.executeScript("arguments[0].click()", confirm);
		}catch (Exception e){
			GlobalMethods.printDebugInfo("No unionpay nofitication: " + e.toString());
		}
	}
	
	@Override
	public boolean submit() {
		WebElement button = assertClickableElementExists(By.xpath("//button[@data-testid='submit']/span[contains(text(),'Submit')]"), "Submit button");
		triggerClickEvent(button);
		GlobalMethods.printDebugInfo("Submit IB withdrawal");
		this.moveContainerToTop();
		return true;
	}
}

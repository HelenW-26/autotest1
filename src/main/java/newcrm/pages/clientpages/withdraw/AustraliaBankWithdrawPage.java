package newcrm.pages.clientpages.withdraw;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import newcrm.global.GlobalMethods;
import vantagecrm.Utils;

public class AustraliaBankWithdrawPage extends BankTransferWithdrawPage {

	public AustraliaBankWithdrawPage(WebDriver driver) {
		super(driver);
	}
	
	public void setBankName(String name) {
		this.setValue("bankName", name);
	}
	
	public void setBSB(String bsb) {
		this.setValue("bsbCode", bsb);
	}
	
	public void setBeneficiaryName(String bbName) {
		this.setValue("beneficiaryName", bbName);
	}
	
	public void setSwift(String code) {
		this.setValue("swift", code);
	}
	
	public void setBankAccNum(String accNum) {
		this.setValue("accountNumber", accNum);
	}
	
	public void setNotes(String notes) {
		this.setValue("importantNotes", notes);
	}
	
	public void upload() {
		WebElement uploadFile = driver.findElement(By.name("file"));
		this.moveElementToVisible(uploadFile);
		String path  = Utils.workingDir + "\\proof.png";
		uploadFile.sendKeys(path);
		GlobalMethods.printDebugInfo("AustraliaBankWithdrawPage: Upload file: " + path);
	}
	
	public boolean setRegionAsAustralia() {
		return this.setRegion("Australia");
	}
	
	public boolean chooseBankAccount(String account) {
		WebElement div = this.getCardIdDiv();
		
		div.click();
		List<WebElement> cards = this.getAllCardsElement();
		for(WebElement card : cards) {
			String info = card.getAttribute("innerText");
			if(info!=null) {
				//if find the account,click
				if(info.toLowerCase().contains(account.trim().toLowerCase())) {
					GlobalMethods.printDebugInfo(info);
					this.moveElementToVisible(card);
					this.clickElement(card);
					if("Australia".equalsIgnoreCase(this.getRegion())) {
						return true;
					}
					div.click();
				}
			}
		}
		System.out.println("AustraliaBankWithdrawPage: ERROR: Choose bank account " + account + " failed");
		div.click();
		return false;
	}
}

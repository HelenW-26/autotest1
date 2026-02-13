package newcrm.pages.clientpages.withdraw;

import newcrm.global.GlobalMethods;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import newcrm.global.GlobalProperties;
import newcrm.pages.clientpages.WithdrawPage;

public class CryptoWithdrawPage extends WithdrawPage{

	public CryptoWithdrawPage(WebDriver driver) {
		// TODO Auto-generated constructor stub
		super(driver);
	}
	
	public void setWalletAddress(String walletaddress) {
		WebElement input_walletaddress = this.findVisibleElemntByXpath("//input[@data-testid='cryptoWalletAddress']");
		this.moveElementToVisible(input_walletaddress);
		input_walletaddress.sendKeys(walletaddress);
	}
	public void setWalletAddressNew(String walletaddress) {
		WebElement input_walletaddress = this.findVisibleElemntByXpath("//input[@data-testid='accountNumber']");
		this.moveElementToVisible(input_walletaddress);
		input_walletaddress.sendKeys(walletaddress);
	}
	
	public void setNotes(String notes) {
		WebElement input_notes = this.findVisibleElemntByXpath("//input[@data-testid='importantNotes'] | //input[@id='importantNotes']");
		this.moveElementToVisible(input_notes);
		input_notes.sendKeys(notes);
	}
	
	public void setUsdtChain(String usdtchain) {		
		WebElement chaindropdown = this.findVisibleElemntByXpath("//div[@data-testid='chain']");
		this.moveElementToVisible(chaindropdown);
		chaindropdown.click();
		
		if(GlobalProperties.brand.equalsIgnoreCase("pug")) {chaindropdown.click();}
		
		WebElement chainvalue = this.findVisibleElemntByXpath("//li/span[contains(text(),'"+usdtchain+"')]");
		this.moveElementToVisible(chainvalue);
		chainvalue.click();
	}

	@Override
	public void goBack() {
		waitLoadingForCustomise(90);

		// When Payment Summary page countdown timer has reached, it will auto redirect to home page.
		// Skip proceed below when already in home page.
		if (!driver.getCurrentUrl().contains("/home")) {
			WebElement back = driver.findElement(By.xpath("//*[contains(text(),'Back To Home')]"));
			back.click();
			waitLoading();
			GlobalMethods.printDebugInfo("Click Back To Home button");
		}
	}

}
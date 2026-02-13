package newcrm.pages.ibpages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties.DEPOSITMETHOD;


public class IBCryptBitcoinPage extends IBEmailWithdrawPage {

	public IBCryptBitcoinPage(WebDriver driver) {
		super(driver);
	}
	
	@Override
	public void setWithdrawAccount(String walletaddress) {
		WebElement input_walletaddress = this.findVisibleElemntByTestId("accountNumber");
		this.moveElementToVisible(input_walletaddress);
		input_walletaddress.sendKeys(walletaddress);
		GlobalMethods.printDebugInfo("IBCryptoWithdrawPage: Set crypto wallet address to " + walletaddress);
	}

	public void setWithdrawAccountNew(String walletaddress) {
		WebElement input_walletaddress = assertElementExists(By.xpath("//input[@data-testid='accountNumber']"), "Wallet Address");
		setInputValue(input_walletaddress, walletaddress);
		GlobalMethods.printDebugInfo("Set crypto wallet address to " + walletaddress);
	}
	
	public void setUsdtChain(String usdtchain) {
		WebElement chaindropdown = this.findVisibleElemntByTestId("chain");
		this.moveElementToVisible(chaindropdown);
		chaindropdown.click();
		
		WebElement chainvalue = this.findVisibleElemntByXpath("//li/span[contains(text(),'"+usdtchain+"')]");
		this.moveElementToVisible(chainvalue);
		chainvalue.click();
	}
	
	@Override
	public void setMethod(DEPOSITMETHOD method) {
		WebElement e_method = null;
		try {
			e_method = this.findClickableElemntByTestId(method.getIBWithdrawTypeDataTestId());
		}catch(Exception e) {
			GlobalMethods.printDebugInfo("Unable to locate the withdrawal method from dropdown");
			WebElement dropdown = this.findClickableElemntByTestId("withdrawalType");
			dropdown.click();
			e_method = this.findClickableElemntByTestId(method.getIBWithdrawTypeDataTestId());
		}
		this.moveElementToVisible(e_method);
		e_method.click();
		GlobalMethods.printDebugInfo("RebateWithdrawBasePage: Select IB withdraw method : " + method.getWithdrawName());
		this.waitLoading();
	}
}

package newcrm.pages.clientpages.deposit;

import java.time.Duration;

import newcrm.global.GlobalMethods;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import junit.framework.Assert;

public class CryptoDepositPage extends SkrillPayPage {

	public CryptoDepositPage(WebDriver driver) {
		super(driver);
	}

	public boolean checkHomeUrl() {
		try {
			new WebDriverWait(driver, Duration.ofSeconds(10))
					.until(d -> d.getCurrentUrl().contains("/home"));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public void goToDepositMethod(String testid) {
		//this.findClickableElemntByTestId(testid).click();
		this.findClickableElementByXpath("//span[contains(text(),'"+testid+"')]").click();
		this.waitLoading();
	}
	
	public WebElement getCryptoType1() {

		WebElement BITCOIN = this.findClickableElemntByTestId("bitcoin");
		return BITCOIN;
	}

	public WebElement getCryptoType2() {

		WebElement USDTOMNI = this.findClickableElemntByTestId("usdt");
		return USDTOMNI;
	}

	public WebElement getCryptoType3() {

		WebElement USDTERC20 = this.findClickableElemntByTestId("usdt_CPS");
		return USDTERC20;
	}

	public WebElement getCryptoType4() {

		WebElement USDTTRC20 = this.findClickableElemntByTestId("trc20");
		return USDTTRC20;

	}

	public void confirmButton() {

		WebElement confirmButton = this.findClickableElemntByTestId("yes");
		confirmButton.click();

	}
	public void paymentConfirm() {
		WebElement confirmButton = this.findVisibleElemntByXpath("//button[@data-testid='paymentButton']");
		confirmButton.click();
	}

	public String getBTCAmountThirdParty(String amount) {
		super.waitLoading();
		WebElement thirdPartyBTCAmount = driver
				.findElement(By.cssSelector("div.col-xs-12.col-sm-4.col-lg-3.text-center"));
		String USDCurrency = amount;
		String bitCoinAmount = thirdPartyBTCAmount.getText().substring(7);
		System.out.println("BITCOIN Amount is : " + bitCoinAmount);
		System.out.println("USD Amount is : " + USDCurrency);
		Assert.assertTrue(bitCoinAmount.contains("BTC"));
		return bitCoinAmount;

	}

	public String getUSDTOAmountThirdParty(String amount) {
		super.waitLoading();
		WebElement thirdPartyUSDTOAmount = driver
				.findElement(By.cssSelector("div.col-xs-12.col-sm-4.col-lg-3.text-center"));
		String USDCurrency = amount;
		String USDTOmniAmount = thirdPartyUSDTOAmount.getText().substring(7);
		System.out.println("USDTOMNI Amount is : " + USDTOmniAmount);
		System.out.println("USD Amount is : " + USDCurrency);
		Assert.assertTrue(USDTOmniAmount.contains("USDT"));
		int i = USDTOmniAmount.indexOf(".");
		String USDTAmount = USDTOmniAmount.substring(0, i);
		Assert.assertEquals(amount, USDTAmount);
		return USDTOmniAmount;

	}

	public String getUSDTEAmountThirdParty(String amount) {
		super.waitLoading();
		WebElement thirdPartyUSDTEAmount = driver
				.findElement(By.cssSelector("div.col-xs-12.col-sm-4.col-lg-3.text-center"));
		String USDCurrency = amount;
		String USDERC20Amount = thirdPartyUSDTEAmount.getText().substring(7);
		System.out.println("USDTOMNI Amount is : " + USDERC20Amount);
		System.out.println("USD Amount is : " + USDCurrency);
		Assert.assertTrue(USDERC20Amount.contains("USDT"));

		int i = USDERC20Amount.indexOf(".");
		String USDTAmount = USDERC20Amount.substring(0, i);
		Assert.assertEquals(amount, USDTAmount);
		return USDERC20Amount;

	}

	public String getUSDTTAmountThirdParty(String amount) {
		super.waitLoading();
		WebElement thirdPartyUSDTTAmount = driver
				.findElement(By.cssSelector("div.col-xs-12.col-sm-4.col-lg-3.text-center"));
		String USDCurrency = amount;
		String USDTRC20Amount = thirdPartyUSDTTAmount.getText().substring(7);
		System.out.println("USDTOMNI Amount is : " + USDTRC20Amount);
		System.out.println("USD Amount is : " + USDCurrency);
		Assert.assertTrue(USDTRC20Amount.contains("USDT"));
		int i = USDTRC20Amount.indexOf(".");
		String USDTAmount = USDTRC20Amount.substring(0, i);
		Assert.assertEquals(amount, USDTAmount);
		return USDTRC20Amount;
	}

	public void goBack() {
		waitLoadingForCustomise(90);
		WebElement back = driver.findElement(By.xpath("//*[contains(text(),'Back To Home')]"));
		back.click();
		waitLoading();
		GlobalMethods.printDebugInfo("Click Back To Home button");
	}

	public void goBackNew() {
		driver.navigate().back();
		waitLoading();
		GlobalMethods.printDebugInfo("Click Back To Home button");
	}
}
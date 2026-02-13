package newcrm.pages.auclientpages;

import newcrm.global.GlobalMethods;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import newcrm.pages.clientpages.DepositFundsPage;
import newcrm.pages.clientpages.deposit.InterBankTransPage;
import org.testng.Assert;

import java.time.Duration;

public class AuInterBankTransPage extends InterBankTransPage {
	public AuInterBankTransPage(WebDriver driver) {
		super(driver);
	}

	public void payNow() {
		waitLoading();
		boolean isElementPresent = !driver.findElements(By.xpath("//div[./span[@class='popup-text']]")).isEmpty();
		if (isElementPresent) {
			WebElement terms = findVisibleElemntByXpath("//div[./span[@class='popup-text']]");
			terms.click();
		} else {
			System.out.println("no need to agree terms and conditions");
		}

		WebElement payNowNtn = findVisibleElemntBy(By.xpath("//div[@data-testid='payNow']"));
		this.moveElementToVisible(payNowNtn);
		js.executeScript("arguments[0].click()",payNowNtn);

		GlobalMethods.printDebugInfo("Submit Payment");
		waitLoading();

		try{
			boolean uploadErrorInfo = driver.findElement(By.xpath("//div[@class='ant-form-item-explain-error']")).isDisplayed();
			GlobalMethods.printDebugInfo("uploadErrorInfo:" + uploadErrorInfo);
			Assert.assertFalse(uploadErrorInfo);
		}
		catch(NoSuchElementException e)
		{
			GlobalMethods.printDebugInfo("Upload process is successful");
			GlobalMethods.printDebugInfo(e.getMessage());
		}
		waitLoadingForCustomise(120);
		waitPaymentLoader_IBT();
//		if (!driver.getCurrentUrl().equals(initialUrl)) { return; }
//
//		// Click Back Home Button after payment completed
//		driver.switchTo().defaultContent();
//
//		try {
//			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//			wait.until((ExpectedCondition<Boolean>) d -> {
//				try {
//					WebElement eleBackButton = driver.findElement(By.xpath("//button[@data-testid='back_home_button']"));
//					eleBackButton.click();
//					waitLoading();
//					return true;
//				} catch (Exception e) {
//					return false;
//				}
//			});
//			GlobalMethods.printDebugInfo("Button appeared.");
//		} catch (TimeoutException e) {
//			GlobalMethods.printDebugInfo("Button did not appear, continuing anyway.");
//		}
	}
	public void payNowCC() {
		waitLoading();
		boolean isElementPresent = !driver.findElements(By.xpath("//div[./span[@class='popup-text']]")).isEmpty();
		if (isElementPresent) {
			WebElement terms = findVisibleElemntByXpath("//div[./span[@class='popup-text']]");
			terms.click();
		} else {
			System.out.println("no need to agree terms and conditions");
		}

		WebElement payNowNtn = findVisibleElemntBy(By.xpath("//div[@data-testid='payNow']"));
		this.moveElementToVisible(payNowNtn);
		js.executeScript("arguments[0].click()",payNowNtn);

		GlobalMethods.printDebugInfo("Submit Payment");
		waitLoading();

		waitLoadingForCustomise(120);

	}

	public void payNowWithoutUpload() {
		waitLoading();
		boolean isElementPresent = !driver.findElements(By.xpath("//div[./span[@class='popup-text']]")).isEmpty();
		if (isElementPresent) {
			WebElement terms = findVisibleElemntByXpath("//div[./span[@class='popup-text']]");
			terms.click();
		} else {
			System.out.println("no need to agree terms and conditions");
		}

		WebElement payNowNtn = findVisibleElemntBy(By.xpath("//div[@data-testid='payNow']"));
		this.moveElementToVisible(payNowNtn);
		js.executeScript("arguments[0].click()",payNowNtn);

		GlobalMethods.printDebugInfo("Submit Payment");


	}
}


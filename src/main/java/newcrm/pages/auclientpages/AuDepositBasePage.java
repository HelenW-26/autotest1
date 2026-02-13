package newcrm.pages.auclientpages;

import newcrm.pages.clientpages.DepositBasePage;
import newcrm.pages.clientpages.DepositFundsPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import tools.ScreenshotHelper;
import utils.CustomAssert;
import utils.LogUtils;

import java.time.Duration;

public class AuDepositBasePage extends DepositBasePage {
	public AuDepositBasePage(WebDriver driver) {
		super(driver);
	}

	public void fillCCForm(String cardNumber, String expiryDate, String cvv) {
		LogUtils.info("AU Fill Credit Card Form");
		WebElement state=findVisibleElemntByXpath("//*[@id=\"mat-input-2\"]");
		state.click();
		WebElement country=findVisibleElemntByXpath("//*[@id=\"mat-option-0\"]");
		country.click();
		this.setInputValue(this.findVisibleElemntByXpath("//*[@id=\"mat-input-0\"]"), cardNumber);
		this.setInputValue(this.findVisibleElemntByXpath("//*[@id=\"mat-input-1\"]"), expiryDate);
		this.setInputValue(this.findVisibleElemntByXpath("//*[@id=\"mat-input-3\"]"), cvv);
		WebElement submit = this.findClickableElementByXpath("//button[contains(@class, 'btn-submit') and @type='submit']");
		submit.click();
//		this.clickElement( submit);
		// 等待submit按钮文字变成continue表示提交成功
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(
				By.xpath("//*[contains(@class, 'slogan') and contains(., 'Your deposit was successful')]"),
				"Your deposit was successful"));


	}

	public boolean checkDepositKYCNotMetPopWindow() {
		LogUtils.info("检查KYC弹框");
		WebElement popWindow = findVisibleElemntByXpath("//div[contains(@class, 'kyc_permission_dialog')]");
		ScreenshotHelper.highlightElement(driver, popWindow);
		String kycContent = popWindow.getText();
		LogUtils.info("KYC弹框内容：" + kycContent);
		CustomAssert.assertTrue(kycContent.contains("Identity Verification")||
				kycContent.contains("Residency Address Verification")||
				kycContent.contains("Personal Details Verification"), "KYC pop window content is not correct");
		CustomAssert.assertAll();
		boolean isDisplayed =popWindow.isDisplayed();
		return isDisplayed;
	}
}

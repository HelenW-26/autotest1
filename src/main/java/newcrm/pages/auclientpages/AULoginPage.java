package newcrm.pages.auclientpages;

import java.util.List;

import newcrm.pages.clientpages.LoginPage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import utils.LogUtils;

public class AULoginPage extends LoginPage {
	public AULoginPage(WebDriver driver, String url) {
		super(driver, url);
	}

	protected List<WebElement> getEmailCodeInput() {
		return driver.findElements(By.xpath("//input[@class='otp_input']"));
	}

	protected WebElement getSendCodeButton() {
		return this.driver.findElement(By.xpath("//div[@class='opt_send']//button[@data-testid='button']"));
	}

	protected WebElement getEmailCodeSubmitButton() {
		return this.driver.findElement(By.xpath("//div[@class='customer_footer']//button[@data-testid='button']//span[contains(text(), 'Confirm')]"));
	}

	@Override
	public String sendEmailCode(String code) {
		List<WebElement> elements = this.getEmailCodeInput();
		char[] chars = code.toCharArray();

		if (!elements.isEmpty()) {
			for (int i = 0; i < elements.size(); i++) {
				WebElement element = elements.get(i);
				String otp = String.valueOf(chars[i]);

				element.click();
				element.sendKeys(otp);
			}
		}

		LogUtils.info("LoginPage: set Email OTP to: " + code);

		return code;
	}

	@Override
	public void clickEmailCodeBtn() {
		WebElement e = this.getSendCodeButton();
		this.moveElementToVisible(e);
		e.click();
		waitLoading();
		waitButtonLoader();

		LogUtils.info("LoginPage: click send Email OTP");
	}

	@Override
	public void clickEmailCodeSubmitBtn() {
		WebElement e = this.getEmailCodeSubmitButton();
		e.click();
		waitLoading();

		LogUtils.info("LoginPage: submit Email OTP");
	}

	@Override
	public boolean checkOTPDialog()
	{
		try {
			return driver.findElement(By.xpath("//div[contains(@class, 'security_manage_dialog') and not(contains(@style, 'display: none'))]")).isDisplayed();
		}catch (Exception e)
		{
			LogUtils.info("no OTP dialog");
		}

		return false;
	}

//	@Override
//	public boolean submit(String username, GlobalProperties.ENV dbenv, GlobalProperties.BRAND dbBrand, GlobalProperties.REGULATOR dbRegulator) {
//
//		try {
//			this.getSubmit().click();
//			this.waitLoading();
//
//			boolean bIsReqOTP = driver.getCurrentUrl().contains("emailOtpValidate");
//
//			if (bIsReqOTP && checkOTPDialog()) {
//				clickEmailCodeBtn();
//				sendEmailCode(code);
//				clickEmailCodeSubmitBtn();
//			} else {
//				LogUtils.info("No need login otp");
//			}
//
//		} catch(Exception e){
//			System.out.println("Login Failed: "+ e.getMessage());
//			return false;
//		}
//
//		return true;
//	}

}

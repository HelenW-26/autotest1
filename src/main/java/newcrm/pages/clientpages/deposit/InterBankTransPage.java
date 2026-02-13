package newcrm.pages.clientpages.deposit;

import newcrm.global.GlobalMethods;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import newcrm.pages.clientpages.DepositBasePage;
import org.testng.Assert;
import vantagecrm.Utils;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.time.Duration;

public class InterBankTransPage extends DepositBasePage {

	public InterBankTransPage(WebDriver driver) {
		super(driver);
	}
	public void uploadFile() {

		WebElement uploadFile = driver.findElement(By.name("file"));

		uploadFile.sendKeys(Utils.workingDir + "\\proof.png");
		this.waitLoading();
	}

	public void uploadFileNew() {

		try {
			waitLoading();
			fastwait.until(driver -> driver.findElement(By.xpath("//div[@class='upload-item add']")).isDisplayed());
			WebElement uploadFile = driver.findElement(By.xpath("//div[@class='upload-item add']"));
			moveElementToVisible(uploadFile);
			waitLoading();
			driver.switchTo().defaultContent();

			for (int attempt = 0; attempt < 2; attempt++) {

				js.executeScript("document.getElementById('pcs-iframe').contentWindow.postMessage({\n" +
						"      status: 'uploadFile',\n" +
						"        data: [{\n" +
						"            \"status\": \"success\",\n" +
						"            \"name\": \"20241231-194314.jpeg\",\n" +
						"            \"size\": 39343,\n" +
						"            \"uid\": 1736482417558,\n" +
						"            \"response\": {\n" +
						"                \"code\": 0,\n" +
						"                \"data\": \"https://crm-au-alpha.s3.ap-southeast-1.amazonaws.com/other/d57280546def4425892eb45dfa3ea34e.jpeg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20250110T060035Z&X-Amz-SignedHeaders=host&X-Amz-Expires=600&X-Amz-Credential=AKIA6LZROUZKAQU5T4EI%2F20250110%2Fap-southeast-1%2Fs3%2Faws4_request&X-Amz-Signature=ecb7d6c39b5ca22226e9085cda672d853fa692e9ae23256e46c1a350363ce7f7\",\n" +
						"            },\n" +
						"        }]\n" +
						"},'*');");

				try {
					driver.switchTo().frame("pcs-iframe");

					// Wait until an image appears
					WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
					wait.until(ExpectedConditions.presenceOfElementLocated(
							By.xpath("//div[@class='upload-item']//img[@class='upload-item-image']")));

					GlobalMethods.printDebugInfo("Upload succeeded.");

					break;
				} catch (TimeoutException e) {
					if (attempt > 0) {
						driver.switchTo().defaultContent();
						GlobalMethods.printDebugInfo("Upload failed.");
						Assert.fail("File upload failed.");
						break;
					};
					driver.switchTo().defaultContent();
					GlobalMethods.printDebugInfo("Upload failed. Retrying...");
					// Small wait before retry
					try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
				}
			}

		}
		catch(Exception e)
		{
			GlobalMethods.printDebugInfo("cannot upload file");
		}
		this.waitLoading();
	}

	public void typeFilePath(String filePath) throws AWTException {
		Robot robot = new Robot();

		for (char c : filePath.toCharArray()) {
			switch(c) {
				case '\\':
					// Handle backslash explicitly
					robot.keyPress(KeyEvent.VK_BACK_SLASH);
					robot.keyRelease(KeyEvent.VK_BACK_SLASH);
					break;
				case '_':
					// Handle colon explicitly
					robot.keyPress(KeyEvent.VK_SHIFT);
					robot.keyPress(KeyEvent.VK_MINUS);
					robot.keyRelease(KeyEvent.VK_MINUS);
					robot.keyRelease(KeyEvent.VK_SHIFT);
					break;
				case ':':
					// Handle colon explicitly
					robot.keyPress(KeyEvent.VK_SHIFT);
					robot.keyPress(KeyEvent.VK_SEMICOLON);
					robot.keyRelease(KeyEvent.VK_SEMICOLON);
					robot.keyRelease(KeyEvent.VK_SHIFT);
					break;
				default:
					// General character handling
					typeCharacter(robot, c);
			}
		}
		try {
			Thread.sleep(2000);
			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);
		}
		catch(Exception e)
		{
			GlobalMethods.printDebugInfo("Something wrong with click the enter key");
		}
	}

	private void typeCharacter(Robot robot, char c) {
		// Convert character to uppercase for key events if needed
		boolean isUpperCase = Character.isUpperCase(c);
		int keyCode = KeyEvent.getExtendedKeyCodeForChar(c);

		if (isUpperCase) {
			robot.keyPress(KeyEvent.VK_SHIFT);
		}

		robot.keyPress(keyCode);
		robot.keyRelease(keyCode);

		if (isUpperCase) {
			robot.keyRelease(KeyEvent.VK_SHIFT);
		}
	}

	public void backHomeButton() {
		WebElement backHomeButton = super.findClickableElemntByTestId("bkToHm");
	    backHomeButton.click();
	    this.waitLoading();
	}


	public void payNow() {
		waitLoading();
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
//					moveElementToVisible(eleBackButton);
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

	public void waitPaymentLoader_IBT() {
		GlobalMethods.printDebugInfo("Start loading payment page...");

		String initialUrl = driver.getCurrentUrl();
		try {
			fastwait.until(driver -> {
				// Stop waiting if the URL changes
				if (!driver.getCurrentUrl().equals(initialUrl)) {
					return true;
				}

				try {
					WebElement eleBackButton = driver.findElement(By.xpath("//button[@data-testid='back_home_button']"));
					moveElementToVisible(eleBackButton);
					eleBackButton.click();
					GlobalMethods.printDebugInfo("Press Back Button");
					waitLoading();
					return true;
				} catch (Exception e) {
					return false;
				}
			});
		} catch (Exception e) {
			GlobalMethods.printDebugInfo("Did not catch back to home button, continuing anyway.");
		}

		waitLoading();
		GlobalMethods.printDebugInfo("Finish loading payment page...");
	}

}
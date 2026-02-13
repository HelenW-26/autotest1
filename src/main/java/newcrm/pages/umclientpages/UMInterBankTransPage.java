package newcrm.pages.umclientpages;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.deposit.InterBankTransPage;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;

public class UMInterBankTransPage extends InterBankTransPage {
	public UMInterBankTransPage(WebDriver driver) {
		super(driver);
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
					wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".upload-area .upload-item svg use.svg-icon")));

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
}


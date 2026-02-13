package newcrm.pages.vtclientpages.register;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.register.ConfirmIDPage;
import utils.LogUtils;

public class VTConfirmIDPage extends ConfirmIDPage {

	JavascriptExecutor js = (JavascriptExecutor)driver;

	public VTConfirmIDPage(WebDriver driver) {
		super(driver);
	}

	@Override
	protected By getAlertMsgBy() {
		return By.cssSelector("div.el-message.el-message--error > p");
	}

	@Override
	protected WebElement getIdentityVerificationContentEle() {
		return assertElementExists(By.cssSelector("div.main_layout > div > div > div"), "Identity Verification Content");
	}

	@Override
	public String getPageTitle() {
		return this.findVisibleElemntByXpath("//div[@class='main']/strong").getText();
	}

	@Override
	public void nextSec() {
		waitLoading();
		WebElement e = driver.findElement(By.xpath("(//button[@data-testid='next'])[2]"));
		triggerElementClickEvent(e);
	}

	@Override
	public void proceedToIDVerfication()
	{
		WebElement idBtn = driver.findElement(By.xpath("//*[contains(text(),'Proceed to ID verification')]"));
		idBtn.click();
		waitLoading();
	}

	@Override
	public String setIdentificationType() {
		// Locate the <label> whose <div> contains text 'ID Card'
		WebElement e = assertElementExists(By.xpath("//label[contains(@class, 'registerCheckButton') and .//div[normalize-space(text())='ID Card']]"), "ID Type: ID Card");
		// Check if ID Type is selected
		String class_value = e.getAttribute("class");
		if(!class_value.contains("is-checked")) {
			e.click();
		}

		String result = e.getText();
		if (result != null) {
			LogUtils.info("PersonalDetailsPage: set Identification Type to: " + result);
		} else
		{
			LogUtils.info("ERROR: PersonalDetailsPage: set Identification Type failed!" );
			return null;
		}
		return result;
	}

	public void uploadID(String id) {
		try {
			//add sleep time to avoid the ID page webserver 520 error
			Thread.sleep(5000);
			WebElement idinput = driver.findElement(By.xpath("(//input[@type='file'])[1]"));
			idinput.sendKeys(id);
			Thread.sleep(5000);
			this.waitLoading();
		}
		catch (Exception e)
		{
			LogUtils.info("no need to upload ID");
		}

		LogUtils.info("ConfirmIDPage: upload id: " + id);

	}

	public void uploadPOA(String id) {
		try {
			Thread.sleep(5000);
			WebElement idinput = driver.findElement(By.xpath("(//input[@type='file'])[2]"));
			idinput.sendKeys(id);
			Thread.sleep(5000);
			waitLoading();
			LogUtils.info("ConfirmIDPage: upload POA: " + id);
		}
		catch (Exception e)
		{
			LogUtils.info("no need to upload POA");
		}
	}

}

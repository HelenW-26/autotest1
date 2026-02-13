package newcrm.pages.pugclientpages.register;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.register.ConfirmIDPage;
import utils.LogUtils;

public class PUGConfirmIDPage extends ConfirmIDPage {

	public PUGConfirmIDPage(WebDriver driver) {
		super(driver);
	}

	JavascriptExecutor js = (JavascriptExecutor) driver;
	
	@Override
	public String getPageTitle() {
		return this.findVisibleElemntByXpath("//div[@class='main']/strong").getText();
	}


	@Override
	protected WebElement getIDTypeInput() {
		return this.findClickableElemntBy(By.xpath("//div[@data-testid='idType']/div/input"));
	}

	@Override
	public void next() {
		WebElement e = this.findClickableElementByXpath("//button[@data-testid='confirm']");
		triggerElementClickEvent(e);
		LogUtils.info("Click Submit button");

		try {
		WebElement popupNotice = driver.findElement(By.xpath("//form[@class='el-form']/div[4]//button[@class='el-dialog__headerbtn']"));
		js.executeScript("arguments[0].click()",popupNotice);
		this.waitLoading();
		}
		catch(Exception exception)
		{
			LogUtils.info("No pop up notice");
		}
	}

	@Override
	public void uploadBtn() {
		WebElement uBtn = driver.findElement(By.xpath("//button[@data-testid='uploadBtn']"));
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click()",uBtn);
		waitLoading();
		LogUtils.info("Click upload button");
	}

}

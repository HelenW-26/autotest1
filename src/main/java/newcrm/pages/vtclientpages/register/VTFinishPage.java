package newcrm.pages.vtclientpages.register;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.register.FinishPage;
import utils.LogUtils;

public class VTFinishPage extends FinishPage {

	public VTFinishPage(WebDriver driver) {
		super(driver);
	}
	
	@Override
	public void backHome() {
		WebElement a = this.findClickableElementByXpath("//div[@class='form_button']/a");
		this.moveElementToVisible(a);
		a.click();
	}
	
	@Override
	public String getResponse() {
		String result = "";

		try {
			boolean elemtentDisplayed = driver.findElement(By.xpath("(//div[contains(text(),'Your document is under review')])[1]")).isDisplayed();
			if(elemtentDisplayed)
			{
				result = driver.findElement(By.xpath("(//div[contains(text(),'Your document is under review')])[1]")).getText();
			}
		}
		catch(Exception e)
		{
			System.out.println("pane-4 result:" + result);
		}

		LogUtils.info("FinishPage: response message: " + result);
		return result;
	}
}

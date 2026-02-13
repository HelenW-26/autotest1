package newcrm.pages.vtclientpages.register;

import newcrm.global.GlobalMethods;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import newcrm.pages.clientpages.register.PersonalDetailsPage;
import utils.LogUtils;

public class VTPersonalDetailsPage extends PersonalDetailsPage {

	public VTPersonalDetailsPage(WebDriver driver) {
		super(driver);
	}
	
	@Override
	public String getPageTitle() {
		String s = this.findVisibleElemntByXpath("//div[@class='main']/strong").getText();
		return this.findVisibleElemntByXpath("//div[@class='main']/strong").getText();
	}
	
	 @Override
	protected WebElement getDayInput() {
		return this.findClickableElementByXpath("//div[@data-testid='day']/div/input");
	}
	@Override
	public String setGender(String gender) {

		WebElement e = driver.findElement(By.xpath("//input[@id='gender']"));
		e.click();
		String result = this.selectRandomValueFromDropDownList(this.getAllOpendElements());

		LogUtils.info("PersonalDetailsPage: set Gender to: " + result);

		return result;
	}

	@Override
	public void closeImg()
	{
		waitLoading();
		try
		{
			WebElement cImg = driver.findElement(By.xpath("//img[@class='closeImg']"));
			//cImg.click();
			js.executeScript("arguments[0].click()",cImg);
		}
		catch(Exception e)
		{
			LogUtils.info("No image display");
		}
		waitLoading();
	}

}

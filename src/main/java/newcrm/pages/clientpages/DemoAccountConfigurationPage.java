package newcrm.pages.clientpages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import newcrm.global.GlobalMethods;
import newcrm.pages.Page;

public class DemoAccountConfigurationPage extends Page {

	public DemoAccountConfigurationPage(WebDriver driver) {
		super(driver);
	}
	
	public String getPageTitle() {
		WebElement e = this.findVisibleElemntByXpath("//*[self::h2 or self::h3]");
		return e.getText();
	}
	
	public void setValues() {
		String xpath = "//form//ul";
		this.findVisibleElemntByXpath(xpath);
		int numOfQuestions = driver.findElements(By.xpath(xpath)).size();
		for(int i =1; i<= numOfQuestions; i++) {
			List<WebElement> options = driver.findElements(By.xpath("(//form//ul)["+i+"]/li"));
			String result = this.selectRandomValueFromDropDownList(options);
			String q_title = driver.findElement(By.xpath("(//form//p[@class='title'])["+i+"]")).getText();
			GlobalMethods.printDebugInfo("DemoAccountConfigurationPage: "+ q_title + " : " + result );
		}
	}
	
	public WebElement getReponseElement() {
		return this.findVisibleElemntByXpath("//div[@class='result_info text-teal mb-5']/div");
	}
	
	public void demo_submit() {
		WebElement button = this.findClickableElementByXpath("//button[@data-testid='submit']");
		this.moveElementToVisible(button);
		button.click();
		this.waitLoading();
	}
	
	public void backToHome() {
		WebElement a = this.findClickableElementByXpath("//a[@data-testid='bkToHm']");
		this.moveElementToVisible(a);
		a.click();
		this.waitLoading();
	}
}

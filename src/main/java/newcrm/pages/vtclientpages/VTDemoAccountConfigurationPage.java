package newcrm.pages.vtclientpages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.DemoAccountConfigurationPage;

public class VTDemoAccountConfigurationPage extends DemoAccountConfigurationPage {

	public VTDemoAccountConfigurationPage(WebDriver driver) {
		super(driver);
	}
	
	@Override
	public String getPageTitle() {
		WebElement e = this.findVisibleElemntByXpath("//div[@class='contact_title']/h2");
		return e.getText();
	}
	
	@Override
	public void setValues() {
		String xpath = "//div[@class='el-form-item is-required']";
		this.findVisibleElemntByXpath(xpath);
		int numOfQuestions = driver.findElements(By.xpath(xpath)).size();
		for(int i =1; i<= numOfQuestions; i++) {
			List<WebElement> options = driver.findElements(By.xpath("(//div[@class='el-form-item is-required'])["+i+"]/div/div"));
			String result = this.selectRandomValueFromDropDownList(options);
			String q_title = driver.findElement(By.xpath("(//div[@class='labelTitle'])["+i+"]/label")).getText();
			GlobalMethods.printDebugInfo("DemoAccountConfigurationPage: "+ q_title + " : " + result );
		}
	}
	
	@Override
	public WebElement getReponseElement() {
		return this.findVisibleElemntByXpath("//div[@class='success']");
	}
}

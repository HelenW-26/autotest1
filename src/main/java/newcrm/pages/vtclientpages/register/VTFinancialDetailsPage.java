package newcrm.pages.vtclientpages.register;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.register.FinancialDetailsPage;
import utils.LogUtils;

public class VTFinancialDetailsPage extends FinancialDetailsPage {

	public VTFinancialDetailsPage(WebDriver driver) {
		super(driver);
	}
	
	@Override
	public String getPageTitle() {
		return this.findVisibleElemntByXpath("//div[@class='main']/strong").getText();
	}
	
	public void answerAllQuestionsWithoutReturn() {
		this.findVisibleElemntByXpath("//div[@role='radiogroup']");
		List<WebElement> divs = driver.findElements(By.xpath("//div[@role='radiogroup']"));
		int size = divs.size();
		for(int i=1; i<=size; i++) {
			List<WebElement> options = driver.findElements(By.xpath("(//div[@role='radiogroup'])[" + i + "]/label"));
			String result = this.selectRandomValueFromDropDownList(options);
			size = driver.findElements(By.xpath("//div[@role='radiogroup']")).size();
			LogUtils.info("VTFinancialDetailsPage: select question answer: " + result);
		}
	}
}

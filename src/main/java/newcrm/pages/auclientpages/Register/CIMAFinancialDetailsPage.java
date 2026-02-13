package newcrm.pages.auclientpages.Register;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.register.FinancialDetailsPage;

public class CIMAFinancialDetailsPage extends FinancialDetailsPage {
	public CIMAFinancialDetailsPage(WebDriver driver) {
		super(driver);
	}
	
	public void answerAllQuestionsWithoutReturn() {
		this.findVisibleElemntByXpath("//div[@class='question']");
		List<WebElement> divs = driver.findElements(By.xpath("//div[@class='question']"));
		int size = divs.size();
		for(int i=1; i<=size; i++) {
			List<WebElement> options = driver.findElements(By.xpath("(//div[@class='question'])[" +i + "]/div/label"));
			String result ="";
			if(i==size) {
				WebElement  e = options.get(options.size()-1);
				result = e.getAttribute("innerText");
				this.moveElementToVisible(e);
				this.clickElement(e);
			}else {
				result = this.selectRandomValueFromDropDownList(options);
			}
			
			size = driver.findElements(By.xpath("//div[@class='question']")).size();
			GlobalMethods.printDebugInfo("CIMAFinancialDetailsPage: select question answer: " + result);
		}
	}
}

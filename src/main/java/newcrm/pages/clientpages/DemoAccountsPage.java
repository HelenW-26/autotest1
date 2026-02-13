package newcrm.pages.clientpages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties.PLATFORM;
import newcrm.pages.Page;

public class DemoAccountsPage extends Page {

	public class DemoAccount{
		private String accNum;
		private String accType;
		public DemoAccount(String v_accNum,String v_accType) {
			accNum = v_accNum.trim();
			accType = v_accType.trim();
		}
		public String getAccountNumber() {
			return accNum;
		}
		public String getAccountType() {
			return accType;
		}
	}
	public DemoAccountsPage(WebDriver driver) {
		super(driver);
	}
	
	public void openDemoAccount(PLATFORM platform) {
		WebElement e = null;
		if(platform.equals(PLATFORM.MT4)) {
			e = this.findClickableElementByXpath("//a[@data-testid='openDemoAdditAcc4']");
		}else {
			e = this.findClickableElementByXpath("//a[@data-testid='openDemoAdditAcc5']");
		}
		this.moveElementToVisible(e);
		e.click();
		GlobalMethods.printDebugInfo("DemoAccountsPage: Go to create " + platform.toString() + " demo account page.");
		this.waitLoading();
	}
	
	public String getLatestAccount(PLATFORM platform) {
		//this.findVisibleElemntByXpath("(//div[@class='el-table__body-wrapper is-scrolling-left'])[1]/table/tbody");
		List<WebElement> divs = null;
		if(platform.equals(PLATFORM.MT4)) {
			divs = driver.findElements(By.xpath("(//table)[2]/tbody/tr"));
		}else {
			divs = driver.findElements(By.xpath("(//table)[4]/tbody/tr"));
		}
		
		if(divs==null || divs.size()==0) {
			GlobalMethods.printDebugInfo("Do not find any " + platform +" demo account！");
			return "";
		}
		String values[] = divs.get(divs.size()-1).getText().split("\n");
		return values[0];//the first column
	}
}

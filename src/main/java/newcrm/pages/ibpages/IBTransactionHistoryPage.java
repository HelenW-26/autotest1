package newcrm.pages.ibpages;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.global.GlobalProperties.CURRENCY;
import newcrm.global.GlobalProperties.DEPOSITMETHOD;
import newcrm.global.GlobalProperties.STATUS;
import newcrm.pages.Page;
import newcrm.pages.ibpages.RebateWithdrawBasePage.Account;

public class IBTransactionHistoryPage extends Page {

	public class History{
	    public String date;
		public Double amount;
		public String method;
		public STATUS status;
		
		public void printInfo() {
			System.out.printf("%20s  %s\n","Date:",date);
			System.out.printf("%20s  %s\n","Amount:",amount.toString());
			System.out.printf("%20s  %s\n","Destination:",method);
			System.out.printf("%20s  %s\n","Status:",status.toString());
		}
		
		@Override
		public boolean equals(Object o) {
			if(o == this) {
				return true;
			}
			if(!(o instanceof History)) {
				return false;
			}
			History t = (History)o;
			if(t.amount.equals(this.amount)&&t.method.equals(this.method)&&t.status.equals(this.status)) {
				return true;
			}
			return false;
		}
	}
	public IBTransactionHistoryPage(WebDriver driver) {
		super(driver);
	}
	
	protected void clickWithdraw() {
		//this.refresh();
		this.waitLoading();
		WebElement e = this.findClickableElemntByTestId("withdrawHistory");
		this.moveElementToVisible(e);
		e.click();
		this.waitLoading();
		GlobalMethods.printDebugInfo("Click Withdrawal Tab");
	}
	
	public History getLatestWithdrawHistory() {
		this.clickWithdraw();
		//List<WebElement> els = driver.findElements(By.xpath("//div[@class='el-table__body-wrapper is-scrolling-none']/table/tbody/tr"));
		List<WebElement> els = driver.findElements(By.xpath("//div[@id='rebate_payment_history']//div[@class='select_table_box']/ul/li[not(contains(@style,'display: none'))]//table/tbody/tr"));
		if(els==null || els.size()<1) {
			return null;
		}
		String infos = els.get(0).getText();
		String values[] = infos.split("\n");
		History result = new History();
		result.date = values[0].trim();
		result.amount = GlobalMethods.getBalanceFromString(values[1]);
		result.method = values[2].trim();

		for(STATUS s: STATUS.values()) {
			if(s.toString().equalsIgnoreCase(values[3])) {
				result.status = s;
			}
		}
		return result;
	}

	protected WebElement getRebateAccountInput() {
		return this.findClickableElemntByTestId("accountNumber");
	}
	
	public List<String> getAccountInfos(){
		ArrayList<String> result = new ArrayList<>();
		WebElement input = this.getRebateAccountInput();
		this.moveElementToVisible(input);
		input.click();
		List<WebElement> accs = this.getAllOpendElements();
		for(WebElement e:accs) {
			String info = e.getAttribute("innerText");
			if(info!=null && info.trim().length()>0) {
				String accNum = info.trim();
				result.add(accNum);
			}
		}
		input.click();
		return result;
	}
	
	public boolean setRebateAccount(String accNum) {
		WebElement input = this.getRebateAccountInput();
		this.moveElementToVisible(input);
		input.click();
		List<WebElement> accs = this.getAllOpendElements();
		for(WebElement e : accs) {
			String info = e.getAttribute("innerText");
			if(info!=null && info.contains(accNum)) {
				this.moveElementToVisible(e);
				this.clickElement(e);
				this.waitLoading();
				GlobalMethods.printDebugInfo("RebateWithdrawBasePage: Set IB rebate account to : " + accNum);
				return true;
			}
		}
		input.click();
		GlobalMethods.printDebugInfo("RebateWithdrawBasePage: Set IB rebate account to : " + accNum + " failed!");
		return false;
	}
}

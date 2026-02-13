package newcrm.pages.umclientpages;

import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.pages.clientpages.TransactionHistoryPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;


public class UMTransactionHistoryPage extends TransactionHistoryPage {

	public UMTransactionHistoryPage(WebDriver driver) {
		super(driver);
	}

	@Override
	protected List<WebElement> getFirst25DpElements() {
		WebElement d_tab = this.findClickableElementByXpath("//div[@id='tab-deposit']");
		this.moveElementToVisible(d_tab);
		d_tab.click();
		String xpath = "//div[@id='transactionHistory']//table/tbody/tr | //div[@id='transactionHistory']/div[@class='content_box']/ul/li[1]//table/tbody/tr";
		return driver.findElements(By.xpath(xpath));
	}

	@Override
	public List<History> getFirst25Withdraw() {
//		driver.navigate().refresh();
//		driver.navigate().refresh();
		this.waitLoadingForCustomise(120);
		List<WebElement> elements = getFirst25WithdrawElements();
		return getHistory(elements);
	}

	public List<History> getFirst25Deposit() {
		//driver.navigate().refresh();
		//driver.navigate().refresh();
		this.waitLoadingForCustomise(120);
		List<WebElement> elements = getFirst25DpElements();
		return getHistory(elements);
	}

	@Override
	protected List<WebElement> getFirst25WithdrawElements() {
		WebElement w_tab = this.findClickableElementByXpath("//div[@id='tab-withdraw']");
		this.moveElementToVisible(w_tab);
		w_tab.click();
		String xpath = "//div[@id='transactionHistory']//table[@class='el-table__body']//tr";
		this.findVisibleElemntByXpath(xpath);
		return driver.findElements(By.xpath(xpath));
	}

	public void cancelWithdraw()
	{
		//Cancel withdraw in CP
		try {
			WebElement cancelBtn = driver.findElement(By.xpath("(//button[./span[contains(text(),'CANCEL')]])[1]"));
			cancelBtn.click();
			waitLoading();
			WebElement yesBtn = driver.findElement(By.xpath("//button[./span[normalize-space() = 'Yes']]"));
			yesBtn.click();
			waitLoading();
			GlobalMethods.printDebugInfo("Cancelled withdraw in CP");
		}
		catch (Exception e)
		{
			GlobalMethods.printDebugInfo("No withdrawal Cancel button found");
		}
	}

	@Override
	protected List<WebElement> getFirst25TransferElements(){
		WebElement w_tab = this.findClickableElemntByTestId("transfer");
		this.moveElementToVisible(w_tab);
		w_tab.click();
		this.findVisibleElemntByXpath(
				"//div[@id='transactionHistory']/div[2]/div/ul[2]/li[contains(@class,'active')]//table/tbody/tr");
		return driver.findElements(
				By.xpath("//div[@id='transactionHistory']/div[2]/div/ul[2]/li[contains(@class,'active')]//table/tbody/tr"));
	}

	@Override
	protected List<WebElement> getFirstRowTransferElements(){
		WebElement w_tab = this.findClickableElemntByTestId("transfer");
		this.moveElementToVisible(w_tab);
		w_tab.click();
		this.findVisibleElemntByXpath(
				"//div[@id='transactionHistory']/div[2]/div/ul[2]/li[contains(@class,'active')]//table/tbody/tr");
		return driver.findElements(
				By.xpath("//div[@id='transactionHistory']/div[2]/div/ul[2]/li[contains(@class,'active')]//table/tbody/tr[1]"));
	}

	@Override
	public List<TransferHistory> getFirstRowTransfer() {
		driver.navigate().refresh();
		driver.navigate().refresh();
		waitLoadingForCustomise(120);
		List<WebElement> elements = this.getFirstRowTransferElements();
		List<TransferHistory> result = new ArrayList<>();
		for (WebElement element : elements) {
			String info = element.getText();
			String[] values = info.split("\n");

			String date = values[0].trim();
			int pos = date.indexOf(" ");
			if(pos > 0) {
				date = date.substring(0,pos);
			}

			String t_account = values[1].trim();
			String fr_account = t_account.substring(0, t_account.indexOf("(")).trim();
			String currency = t_account.substring(t_account.indexOf("(") + 1, t_account.lastIndexOf(")")).trim();

			t_account = values[2].trim();
			String to_account = t_account.substring(0, t_account.indexOf("(")).trim();

			String method = "";
			String t_amount = values[3].trim();

			String amount = getAmount(t_amount);


			String status = values[4].trim();

			result.add(new TransferHistory(date, fr_account, currency, method, amount, status,to_account));
		}
		return result;
	}

}

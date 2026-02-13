package newcrm.pages.pugclientpages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import newcrm.pages.clientpages.TransactionHistoryPage;

public class PUGTransactionHistoryPage extends TransactionHistoryPage {

	public PUGTransactionHistoryPage(WebDriver driver) {
		super(driver);
	}
	
	@Override
	protected List<WebElement> getFirst25DpElements() {
		WebElement d_tab = this.findClickableElementByXpath("//div[@id='tab-deposit']");
		this.moveElementToVisible(d_tab);
		d_tab.click();
        try {
            return driver.findElements(By.xpath("//div[@id='transactionHistory']//table/tbody/tr"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
	
	@Override
	protected List<WebElement> getFirst25WithdrawElements() {
		WebElement w_tab = this.findClickableElementByXpath("//div[@id='tab-withdraw']");
		this.moveElementToVisible(w_tab);
		w_tab.click();
		String xpath = "//div[@id='transactionHistory']//table/tbody/tr";
		this.findVisibleElemntByXpath(xpath);
		return driver.findElements(By.xpath(xpath));
	}
	
	@Override
	protected List<WebElement> getFirst25TransferElements(){
		WebElement w_tab = this.findClickableElemntByTestId("transfer");
		this.moveElementToVisible(w_tab);
		w_tab.click();
		this.findVisibleElemntByXpath(
				"//div[@class='el-table__body-wrapper is-scrolling-none']/table/tbody");
		return driver.findElements(
				By.xpath("//div[@class='el-table__body-wrapper is-scrolling-none']/table/tbody/tr"));
	}

	@Override
	protected List<WebElement> getFirstRowTransferElements(){
		WebElement w_tab = this.findClickableElemntByTestId("transfer");
		this.moveElementToVisible(w_tab);
		w_tab.click();
		this.findVisibleElemntByXpath(
				"//div[@class='el-table__body-wrapper is-scrolling-none']/table/tbody");
		return driver.findElements(
				By.xpath("//div[@class='el-table__body-wrapper is-scrolling-none']/table/tbody/tr[1]"));
	}
}

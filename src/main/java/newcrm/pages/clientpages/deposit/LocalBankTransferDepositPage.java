package newcrm.pages.clientpages.deposit;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.DepositBasePage;
import vantagecrm.Utils;

public class LocalBankTransferDepositPage extends DepositBasePage {

	public LocalBankTransferDepositPage(WebDriver driver) {
		super(driver);
	}
	
	public void setTaxID (String taxid) {
    	WebElement input_taxid = this.findVisibleElemntByXpath("//input[@data-testid='personal_id'] or [@id='form_item_personal_id']");
		this.moveElementToVisible(input_taxid);
		input_taxid.sendKeys(taxid);
		GlobalMethods.printDebugInfo("Set Personal Tax ID to: " + taxid);
    }
	
	public void setBankName () {
		WebElement bankName = this.findVisibleElemntByXpath("//div[@data-testid='bank_code']  | //input[@data-testid='bank_name' or @data-testid='bankcode']");
		String placeholder = bankName.getAttribute("class");
		//if bank branch is input field
		if(placeholder!=null && placeholder.trim().equalsIgnoreCase("el-input__inner")) 
		{
			String disabled = bankName.getAttribute("disabled");
			if(disabled!=null && disabled.trim().equalsIgnoreCase("disabled")) {
				GlobalMethods.printDebugInfo("The Bank Name element is disabled");
				return;
			}
			this.moveElementToVisible(bankName);
			bankName.sendKeys("test bank name123");
			GlobalMethods.printDebugInfo("Set Bank Name to: test bank name");
			return;
		}
		//else it wil be dropdown
		this.moveElementToVisible(bankName);
		bankName.click();
		List<WebElement> ops = driver.findElements(By.xpath("//div[@class='el-select-dropdown el-popper' and not(contains(@style,'display'))]//li"));
		String name = this.selectRandomValueFromDropDownList(ops);
		GlobalMethods.printDebugInfo("Set Bank Name to: " + name);
    }
	
	public void setCardNumber(String cardnum) {
		WebElement input_cardnum = this.findVisibleElemntByXpath("//input[@data-testid='card_number'] | //input[@data-testid='cardnumber']");
		this.moveElementToVisible(input_cardnum);
		input_cardnum.sendKeys(cardnum);
		GlobalMethods.printDebugInfo("Set Card Number to: " + cardnum);
	}

	public void setLocalDepositor () {
		WebElement localdepositor = this.findVisibleElemntByXpath("//div[@data-testid='local_depositor']");
		this.moveElementToVisible(localdepositor);
		localdepositor.click();
		List<WebElement> ops = driver.findElements(By.xpath("//div[@class='el-select-dropdown el-popper' and not(contains(@style,'display'))]//li"));
		String localdep = this.selectRandomValueFromDropDownList(ops);
		GlobalMethods.printDebugInfo("Set Local Depositor to: " + localdep);
    }

	public void setLocalDepositorNew () {
		WebElement localdepositor = this.findVisibleElemntByXpath("//div[@class='vantage-select' or @class='ultima-select']");
		this.moveElementToVisible(localdepositor);
		localdepositor.click();
		List<WebElement> ops = driver.findElements(By.xpath("//div[contains(@class,'ant-select-item-option-content')]"));
		String localdep = this.selectRandomValueFromDropDownList(ops);
		GlobalMethods.printDebugInfo("Set Local Depositor to: " + localdep);
	}
	
	//um taiwan only
	public void setSupermarketCode () {
		WebElement marketcode = this.findVisibleElemntByXpath("//div[@data-testid='attach_supermarket_code']");
		this.moveElementToVisible(marketcode);
		marketcode.click();
		/* Option provided by CPS more than half hit error
		List<WebElement> ops = driver.findElements(By.xpath("//div[@class='el-select-dropdown el-popper' and not(contains(@style,'display'))]//li"));
		String supermarket = this.selectRandomValueFromDropDownList(ops);
		GlobalMethods.printDebugInfo("Set Supermarket to: " + supermarket);*/
		this.findVisibleElemntByXpath("//span[contains(text(),'IBON')]").click();
		GlobalMethods.printDebugInfo("Set Supermarket to: IBON");
    }
	
	//vjp japan only
	public void setJCBEmail (String email) {
		WebElement jcbemail = this.findVisibleElemntByXpath("//input[@data-testid='email']");
		this.moveElementToVisible(jcbemail);
		jcbemail.sendKeys(email);
		GlobalMethods.printDebugInfo("Set JCB Email to: " + email);
    }

	// PU eubt only
	public void agreeImportantNote() {
		WebElement importantNote_element = driver.findElement(By.xpath("//label[@data-testid='isAgreeImportantNote']"));

		// Select the checkbox when not selected. When checkbox is selected, 'is-checked' class is added to checkbox.
		String classAttribute = importantNote_element.getAttribute("class");
		if (classAttribute != null && !classAttribute.contains("is-checked")) {
			importantNote_element.click();
		}

		GlobalMethods.printDebugInfo("Agree on Important Notes");
	}

}
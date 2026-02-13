package newcrm.pages.clientpages.withdraw;


import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import newcrm.global.GlobalMethods;
import utils.LogUtils;

public class LocalBankWithdrawPage extends BankTransferWithdrawPage {

	public LocalBankWithdrawPage(WebDriver driver) {
		super(driver);
	}
	
	
	public void setBankName() {
		String item_xpath = "div.el-popper:not([style*='display: none']) li";
		WebElement bankName = this.findVisibleElemntByXpath("//div[@data-testid='bankName' or @data-testid='bank_code']  | //input[@data-testid='bank_name' or @data-testid='bankName']");
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
			bankName.sendKeys("test bank name");
			GlobalMethods.printDebugInfo("Set Bank Name to: test bank name");
			return;
		}
		//else it wil be dropdown
		this.moveElementToVisible(bankName);
		bankName.click();
		wait03.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(item_xpath)));
		List<WebElement> ops = driver.findElements(By.cssSelector(item_xpath));
		String name = this.selectRandomValueFromDropDownList(ops);
		GlobalMethods.printDebugInfo("Set Bank Name to: " + name);
	}
	
	public void setBankAddress(String address) {
		WebElement input_address = this.findVisibleElemntByXpath("//input[@data-testid='bankAddress' or @data-testid='bank_address']");
		String disabled = input_address.getAttribute("disabled");
		//if the input element is disabled, return;
		if(disabled!=null && disabled.trim().equalsIgnoreCase("disabled")) {
			GlobalMethods.printDebugInfo("The Bank Address element is disabled");
			return;
		}
		this.moveElementToVisible(input_address);
		input_address.sendKeys(address);
		GlobalMethods.printDebugInfo("Set Bank address to: " + address);
	}
	
	public void setBeneficiaryName(String name) {
		WebElement input_Bname = this.findVisibleElemntByXpath("//input[@data-testid='beneficiaryName']");
		String disabled = input_Bname.getAttribute("disabled");
		//if the input element is disabled, return;
		if(disabled!=null && disabled.trim().equalsIgnoreCase("disabled")) {
			GlobalMethods.printDebugInfo("The beneficiaryName element is disabled");
			return;
		}
		this.moveElementToVisible(input_Bname);
		input_Bname.sendKeys(name);
		GlobalMethods.printDebugInfo("Set Bank beneficiaryName to: " + name);
	}
	
	public void setBankAccountNumber(String account) {
		WebElement input_account = this.findVisibleElemntByXpath("//input[@data-testid='accountNumber']");
		String disabled = input_account.getAttribute("disabled");
		//if the input element is disabled, return;
		if(disabled!=null && disabled.trim().equalsIgnoreCase("disabled")) {
			GlobalMethods.printDebugInfo("The beneficiaryName element is disabled");
			return;
		}
		this.moveElementToVisible(input_account);
		input_account.sendKeys(account);
		GlobalMethods.printDebugInfo("Set Bank account to: " + account);
	}
	
	public void setImportantNotes(String notes) {
		WebElement input_account = findVisibleElemntByXpath("(//input[@data-testid='importantNotes'])[2] | //input[@data-testid='importantNotes']");
		this.moveElementToVisible(input_account);
		input_account.clear();
		input_account.sendKeys(notes);
		GlobalMethods.printDebugInfo("Set Important Notes to: " + notes);
	}
	
	//Combine input and dropdown method to handle certain BT branch field might be ddl or input based on Bank Name
	public void setBankBranch(String branch) {
		LogUtils.info("Adding new bank branch to:"+ branch);
//		WebElement bankBranch = this.findVisibleElemntByXpath("//div[@data-testid='selectedBankBranch' or @data-testid='bankBranch'] | //input[@data-testid='bankBranch'] | //div[@data-testid='bankName' or @data-testid='bank_name'][2]");
		WebElement bankBranch = this.findVisibleElemntByXpath("//input[@data-testid='bankBranch']");

		String placeholder = bankBranch.getAttribute("class");
		LogUtils.info("==============:"+placeholder);
		//if bank branch is input field
		if(placeholder!=null && placeholder.trim().equalsIgnoreCase("el-input__inner"))
		{	LogUtils.info("The Bank Branch element is input");
			String disabled = bankBranch.getAttribute("disabled");
			if(disabled!=null && disabled.trim().equalsIgnoreCase("disabled")) {
				GlobalMethods.printDebugInfo("The Bank Branch element is disabled");
				return;
			}
			this.moveElementToVisible(bankBranch);
			bankBranch.sendKeys(branch);
			LogUtils.info("Set Bank Branch to: " + branch);
			GlobalMethods.printDebugInfo("Set Bank Branch to: " + branch);
			return;
		}
		//else it wil be dropdown
		LogUtils.info("The Bank Branch element is dropdown");
		this.moveElementToVisible(bankBranch);
		bankBranch.click();
		List<WebElement> ops = driver.findElements(By.xpath("//div[@class='el-select-dropdown el-popper' and not(contains(@style,'display'))]//li"));
		String name = this.selectRandomValueFromDropDownList(ops);
		GlobalMethods.printDebugInfo("Set Bank Branch to: " + name);
	}
	
	public void setBankAccountName(String name) {
		WebElement input_account = this.findVisibleElemntByXpath("//input[@data-testid='accountName']");
		String disabled = input_account.getAttribute("disabled");
		//if the input element is disabled, return;
		if(disabled!=null && disabled.trim().equalsIgnoreCase("disabled")) {
			GlobalMethods.printDebugInfo("The accountName element is disabled");
			return;
		}
		this.moveElementToVisible(input_account);
		input_account.sendKeys(name);
		GlobalMethods.printDebugInfo("Set Bank account name to: " + name);
	}
	
	public void setBankCity(String city) {
		WebElement bankCity = this.findVisibleElemntByXpath("//input[@data-testid='bankCity']");
		String disabled = bankCity.getAttribute("disabled");
		//if the input element is disabled, return;
		if(disabled!=null && disabled.trim().equalsIgnoreCase("disabled")) {
			GlobalMethods.printDebugInfo("The Bank City element is disabled");
			return;
		}
		this.moveElementToVisible(bankCity);
		bankCity.sendKeys(city);
		GlobalMethods.printDebugInfo("Set Bank City to: " + city);
	}
	
	public void setBankProvince(String province) {
		WebElement bankProvince = this.findVisibleElemntByXpath("//input[@data-testid='bankProvince' or @data-testid='bank_province']");
		String disabled = bankProvince.getAttribute("disabled");
		//if the input element is disabled, return;
		if(disabled!=null && disabled.trim().equalsIgnoreCase("disabled")) {
			GlobalMethods.printDebugInfo("The Bank Province element is disabled");
			return;
		}
		this.moveElementToVisible(bankProvince);
		bankProvince.sendKeys(province);
		GlobalMethods.printDebugInfo("Set Bank Province to: " + province);
	}	
	
	public void setBankIFSC(String ifsc) {
		WebElement bankIfsc = this.findVisibleElemntByXpath("//input[@data-testid='ifscCode']");
		String disabled = bankIfsc.getAttribute("disabled");
		//if the input element is disabled, return;
		if(disabled!=null && disabled.trim().equalsIgnoreCase("disabled")) {
			GlobalMethods.printDebugInfo("The Bank ifsc element is disabled");
			return;
		}
		this.moveElementToVisible(bankIfsc);
		bankIfsc.sendKeys(ifsc);
		GlobalMethods.printDebugInfo("Set Bank ifsc to: " + ifsc);
	}
	
	public void setAccountType() {
		WebElement accountType = this.findVisibleElemntByXpath("//div[@data-testid='accountType' or @data-testid='attach_account_type'] | //input[@data-testid='accountType']");
		String placeholder = accountType.getAttribute("class");
		//if account type is input field
		if(placeholder!=null && placeholder.trim().toLowerCase().contains("el-input".toLowerCase()))
		{
			String disabled = accountType.getAttribute("disabled");
			if(disabled!=null && disabled.trim().equalsIgnoreCase("disabled")) {
				GlobalMethods.printDebugInfo("The Account Type element is disabled");
				return;
			}
			this.moveElementToVisible(accountType);
			String tagName = accountType.getTagName();
			if (tagName!=null && tagName.trim().equalsIgnoreCase("div")){
				accountType = this.findVisibleElemntByXpath("//div/input[@data-testid='accountType']");
			}
			accountType.sendKeys("accountType");
			GlobalMethods.printDebugInfo("Set Account Type to: accountType");
			return;
		}
		//else it is dropdown
		this.moveElementToVisible(accountType);
		accountType.click();
		List<WebElement> ops = driver.findElements(By.xpath("//div[@class='el-select-dropdown el-popper' and not(contains(@style,'display'))]//li"));
		String name = this.selectRandomValueFromDropDownList(ops);
		GlobalMethods.printDebugInfo("Set account type to: " + name);
	}	
	
	public void setDocumentType() {
		WebElement docType = this.findVisibleElemntByXpath("//div[@data-testid='attach_document_type'] | //input[@data-testid='attach_document_type']");
		String placeholder = docType.getAttribute("class");
		//if account type is input field
		if(placeholder!=null && placeholder.trim().toLowerCase().contains("el-input".toLowerCase()))
		{
			this.moveElementToVisible(docType);
			String tagName = docType.getTagName();
			if (tagName!=null && tagName.trim().equalsIgnoreCase("div")){
				docType = this.findVisibleElemntByXpath("//div/input[@data-testid='accountType']");
			}
			docType.sendKeys("documentType");
			GlobalMethods.printDebugInfo("Set Document Type to: documentType");
			return;
		}
		//else it is dropdown
		this.moveElementToVisible(docType);
		docType.click();
		List<WebElement> ops = driver.findElements(By.xpath("//div[@class='el-select-dropdown el-popper' and not(contains(@style,'display'))]//li"));
		String doctype = this.selectRandomValueFromDropDownList(ops);
		GlobalMethods.printDebugInfo("Set Document Type to: " + doctype);
	}
	
	public void setAccDigit(String accdigit) {
		WebElement accDigit = this.findVisibleElemntByXpath("//input[@data-testid='attach_account_digit']");
		String disabled = accDigit.getAttribute("disabled");
		//if the input element is disabled, return;
		if(disabled!=null && disabled.trim().equalsIgnoreCase("disabled")) {
			GlobalMethods.printDebugInfo("The Account Digit element is disabled");
			return;
		}
		this.moveElementToVisible(accDigit);
		accDigit.sendKeys(accdigit);
		GlobalMethods.printDebugInfo("Set Account Digit to: " + accdigit);
	}
	
	public void setDocId(String docid) {
		WebElement docID = this.findVisibleElemntByXpath("//input[@data-testid='personal_id']");
		String disabled = docID.getAttribute("disabled");
		//if the input element is disabled, return;
		if(disabled!=null && disabled.trim().equalsIgnoreCase("disabled")) {
			GlobalMethods.printDebugInfo("The Document ID element is disabled");
			return;
		}
		this.moveElementToVisible(docID);
		docID.sendKeys(docid);
		GlobalMethods.printDebugInfo("Set Document ID to: " + docid);
	}
	
	public void setBankAccCurrency() {
		WebElement bankAccCurrency = this.findVisibleElemntByXpath("//div[@data-testid='bank_account_currency']");
		this.moveElementToVisible(bankAccCurrency);
		bankAccCurrency.click();
		List<WebElement> ops = driver.findElements(By.xpath("//div[@class='el-select-dropdown el-popper' and not(contains(@style,'display'))]//li"));
		String bankaccCur = this.selectRandomValueFromDropDownList(ops);
		GlobalMethods.printDebugInfo("Set Bank Account Currency to: " + bankaccCur);
	}
	
	public void setBankSwiftCode(String swiftcode) {
		WebElement bankSwiftCode = this.findVisibleElemntByXpath("//input[@data-testid='swift']");
		String disabled = bankSwiftCode.getAttribute("disabled");
		//if the input element is disabled, return;
		if(disabled!=null && disabled.trim().equalsIgnoreCase("disabled")) {
			GlobalMethods.printDebugInfo("The Bank Swift Code element is disabled");
			return;
		}
		this.moveElementToVisible(bankSwiftCode);
		bankSwiftCode.sendKeys(swiftcode);
		GlobalMethods.printDebugInfo("Set Bank Swift Code to: " + swiftcode);
	}
	
	public void setBankRegion() {
		WebElement bankRegion = this.findVisibleElemntByXpath("//div[@data-testid='attach_region']");
		this.moveElementToVisible(bankRegion);
		bankRegion.click();
		List<WebElement> ops = driver.findElements(By.xpath("//div[@class='el-select-dropdown el-popper' and not(contains(@style,'display'))]//li"));
		String bankReg = this.selectRandomValueFromDropDownList(ops);
		GlobalMethods.printDebugInfo("Set Bank Region to: " + bankReg);
	}
	
	public void setLocalDepositor() {
		WebElement localdepositor = this.findVisibleElemntByXpath("//div[@data-testid='local_depositor']");
		this.moveElementToVisible(localdepositor);
		localdepositor.click();
		List<WebElement> ops = driver.findElements(By.xpath("//div[@class='el-select-dropdown el-popper' and not(contains(@style,'display'))]//li"));
		String depositor = this.selectRandomValueFromDropDownList(ops);
		GlobalMethods.printDebugInfo("Set Depositor to: " + depositor);
	}

	public void setBsbCode(String bsbCode) {
		WebElement input_code = this.findVisibleElemntByXpath("//input[@data-testid='bsbCode']");
		String disabled = input_code.getAttribute("disabled");
		//if the input element is disabled, return;
		if(disabled!=null && disabled.trim().equalsIgnoreCase("disabled")) {
			GlobalMethods.printDebugInfo("The customerName element is disabled");
			return;
		}
		this.moveElementToVisible(input_code);
		input_code.sendKeys(bsbCode);
		GlobalMethods.printDebugInfo("Set BSB Code to: " + bsbCode);
	}

	public void setCustomerName(String customerName) {
		WebElement input_name = this.findVisibleElemntByXpath("//input[@data-testid='accountName']");
		String disabled = input_name.getAttribute("disabled");
		//if the input element is disabled, return;
		if(disabled!=null && disabled.trim().equalsIgnoreCase("disabled")) {
			GlobalMethods.printDebugInfo("The customerName element is disabled");
			return;
		}
		this.moveElementToVisible(input_name);
		input_name.sendKeys(customerName);
		GlobalMethods.printDebugInfo("Set Customer Name to: " + customerName);
	}

	public void setHolderAddress(String holderAddress) {
		WebElement bankHolderAddress = this.findVisibleElemntByXpath("//input[@data-testid='holderAddress' or @data-testid='holder_address']");
		String disabled = bankHolderAddress.getAttribute("disabled");
		//if the input element is disabled, return;
		if(disabled!=null && disabled.trim().equalsIgnoreCase("disabled")) {
			GlobalMethods.printDebugInfo("The Account Holder Address element is disabled");
			return;
		}
		this.moveElementToVisible(bankHolderAddress);
		bankHolderAddress.sendKeys(holderAddress);
		GlobalMethods.printDebugInfo("Set Account Holder Address to: " + holderAddress);
	}

	public void setBeneficiaryAddress(String address) {
		WebElement beneficiaryAddress = this.findVisibleElemntByXpath("//input[@data-testid='beneficiary_address']");
		String disabled = beneficiaryAddress.getAttribute("disabled");
		//if the input element is disabled, return;
		if(disabled!=null && disabled.trim().equalsIgnoreCase("disabled")) {
			GlobalMethods.printDebugInfo("The Beneficiary Address element is disabled");
			return;
		}
		this.moveElementToVisible(beneficiaryAddress);
		beneficiaryAddress.sendKeys(address);
		GlobalMethods.printDebugInfo("Set Beneficiary address to: " + address);
	}

	@Override
	public void goBack() {}

}

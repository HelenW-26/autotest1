package newcrm.pages.clientpages.withdraw;

import java.nio.file.Paths;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import newcrm.global.GlobalMethods;
import org.openqa.selenium.support.ui.ExpectedConditions;
import vantagecrm.Utils;

public class InternalBankWithdrawPage extends BankTransferWithdrawPage {

	public InternalBankWithdrawPage(WebDriver driver) {
		super(driver);
	}
	
	// start of IBT 
	public boolean setRegionAsInternal() {
		return this.setRegion("International");
	}
	
	
	public void setBankName(String name) {
		this.setValue("bankName", name);
	}
	
	public void setBankAdress(String address) {
		this.setValue("bankAddress", address);
	}
	
	public void setBeneficiaryName(String beneficiaryName) {
		this.setValue("beneficiaryName", beneficiaryName);
	}
	
	public void setBankAccountNumber(String accountNumber) {
		this.setValue("accountNumber", accountNumber);
	}
	
	public void setLocalDepositorAdress(String address) {
		this.setValue("bank_address", address);
	}
	
	/**
	 * 
	 * @param holderAddress bank holder address
	 */
	public void setHolderAddress(String holderAddress) {
		this.setValue("holderAddress", holderAddress);
	}
	
	public void setSwift(String swift) {
		this.setValue("swift", swift);
	}
	
	public void setSortCode(String sortCode) {
		this.setValue("sortCode", sortCode);
	}
	
	public void setNotes(String importantNotes) {
		this.setValue("importantNotes", importantNotes);
	}
	
	public void setBankAccountName(String name) {
		this.setValue("accountName", name);
	}
	
	public void setBankCity(String city) {
		this.setValue("bankCity", city);
	}
	
	public void setBankProvince(String province) {
		this.setValue("bankProvince", province);
	}
	
	public void setBankIFSC(String ifsc) {
		this.setValue("ifscCode", ifsc);
	}
	
	public void setAccDigit(String accdigit) {
		this.setValue("attach_account_digit", accdigit);
	}
	
	public void setDocId(String docid) {
		this.setValue("personal_id", docid);
	}
	public void setBsbCode(String bsbCode) { this.setValue("bsbCode", bsbCode); 	}
	public void setCustomerName(String customerName) { this.setValue("accountName", customerName); }
	public void uploadStatement() {
		WebElement uploadFile = driver.findElement(By.name("file"));
		this.moveElementToVisible(uploadFile);
		String path = Paths.get(Utils.workingDir,"proof.png").toString();
		uploadFile.sendKeys(path);
		GlobalMethods.printDebugInfo("InternalBankWithdrawPage: Upload file: " + path);
	}
	
	@Override
	public boolean chooseBankAccount(String account) {
		List<WebElement> cards = this.getAllCardsElement();
		for(WebElement card : cards) {
			String info = card.getAttribute("innerText");
			if(info!=null) {
				//if find the account,click
				if(info.toLowerCase().contains(account.trim().toLowerCase())) {
					GlobalMethods.printDebugInfo("InternalBankWithdrawPage: Choose bank account: "+info);
					this.moveElementToVisible(card);
					this.clickElement(card);
					return true;
				}
			}
		}
		return false;
	}
	// end of IBT 
	
	
	//CPS IBT
	public void setCPSBankName(String name) {
		this.setValue("bank_name", name);
	}
	
	public void setCPSBankAdress(String address) {
		this.setValue("bank_address", address);
	}
	
	public void setCPSBeneficiaryName(String beneficiaryName) {
		this.setValue("accountName", beneficiaryName);
	}
	
	public void setCPSHolderAddress(String holderAddress) {
		this.setValue("holder_address", holderAddress);
	}
	
	public void setCPSSortCode(String sortCode) {
		this.setValue("sort_code", sortCode);
	}

	public void setCPSRecipientType() {
		String item_xpath = "//div[@class='el-select-dropdown el-popper' and not(contains(@style,'display'))]//li";
		WebElement recipientType = this.findVisibleElemntByXpath("//div[@data-testid='transaction_attr']");
		this.moveElementToVisible(recipientType);
		recipientType.click();
		wait03.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(item_xpath)));
		List<WebElement> ops = driver.findElements(By.xpath(item_xpath));
		String name = this.selectRandomValueFromDropDownList(ops);
		GlobalMethods.printDebugInfo("Set Recipient Type to: " + name);
	}
	//end of CPS IBT
	
	// start of IB withdrawal
	public void setBankName() {
		WebElement bankName = this.findVisibleElemntByXpath("//input[@data-testid='bankName'] | //div[@data-testid='bankName']");
		String placeholder = bankName.getAttribute("class");
		//if bank branch is input field
		if(placeholder!=null && placeholder.trim().toLowerCase().contains("el-input".toLowerCase()))
		{
			String disabled = bankName.getAttribute("disabled");
			if(disabled!=null && disabled.trim().equalsIgnoreCase("disabled")) {
				GlobalMethods.printDebugInfo("The Bank Name element is disabled");
				return;
			}
			this.moveElementToVisible(bankName);
			String tagName = bankName.getTagName();
			if (tagName!=null && tagName.trim().equalsIgnoreCase("div")){
				bankName = this.findVisibleElemntByXpath("//div/input[@data-testid='bankName']");
				//bankName.click();
			}
			bankName.sendKeys("test bank name");
			GlobalMethods.printDebugInfo("Set Bank Name to: test bank name");
			return;
		}
		//else it wil be dropdown
		this.moveElementToVisible(bankName);
		bankName.click();
		//List<WebElement> ops = driver.findElements(By.xpath("//div[@class='el-select-dropdown el-popper' and not(contains(@style,'display'))]//li"));
		List<WebElement> ops = driver.findElements(By.xpath("//div[@class='el-select-dropdown el-popper ht-select-dropdown' and not(contains(@style,'display'))]//li"));
		String name = this.selectRandomValueFromDropDownList(ops);
		GlobalMethods.printDebugInfo("Set Bank Name to: " + name);
	}
	
	public void setBankBranch(String branch) {
		WebElement bankBranch = this.findVisibleElemntByXpath("//input[@data-testid='bankBranch'] | //div[@data-testid='selectedBankBranch']");
		String placeholder = bankBranch.getAttribute("class");
		//if bank branch is input field
		if(placeholder!=null && placeholder.trim().toLowerCase().contains("el-input".toLowerCase()))
		{
			String disabled = bankBranch.getAttribute("disabled");
			if(disabled!=null && disabled.trim().equalsIgnoreCase("disabled")) {
				GlobalMethods.printDebugInfo("The Bank Branch element is disabled");
				return;
			}
			this.moveElementToVisible(bankBranch);
			String tagName = bankBranch.getTagName();
			if (tagName!=null && tagName.trim().equalsIgnoreCase("div")){
				bankBranch = this.findVisibleElemntByXpath("//div/input[@data-testid='bankBranch']");
			}
			bankBranch.sendKeys(branch);
			GlobalMethods.printDebugInfo("Set Bank Branch to: " + branch);
			return;
		}
		//else it wil be dropdown
		this.moveElementToVisible(bankBranch);
		bankBranch.click();
		List<WebElement> ops = driver.findElements(By.xpath("//div[@class='el-select-dropdown el-popper ht-select-dropdown' and not(contains(@style,'display'))]//li"));
		String name = this.selectRandomValueFromDropDownList(ops);
		GlobalMethods.printDebugInfo("Set Bank Branch to: " + name);
	}
	
	public void setAccountType(String accountType) {
		WebElement accType = this.findVisibleElemntByXpath("//input[@data-testid='accountType'] | //div[@data-testid='accountType']");
		String placeholder = accType.getAttribute("class");
		//if account type is input field
		if(placeholder!=null && placeholder.trim().toLowerCase().contains("el-input".toLowerCase()))
		{
			String disabled = accType.getAttribute("disabled");
			if(disabled!=null && disabled.trim().equalsIgnoreCase("disabled")) {
				GlobalMethods.printDebugInfo("The Account Type element is disabled");
				return;
			}
			this.moveElementToVisible(accType);
			String tagName = accType.getTagName();
			if (tagName!=null && tagName.trim().equalsIgnoreCase("div")){
				accType = this.findVisibleElemntByXpath("//div/input[@data-testid='accountType']");
			}
			accType.sendKeys(accountType);
			GlobalMethods.printDebugInfo("Set Account Type to: "+accountType);
			return;
		}
		//else it is dropdown
		moveElementToVisible(accType);
		accType.click();
		List<WebElement> ops = driver.findElements(By.xpath("//div[@class='el-select-dropdown el-popper ht-select-dropdown' and not(contains(@style,'display'))]//li"));
		String acctype = this.selectRandomValueFromDropDownList(ops);
		GlobalMethods.printDebugInfo("Set Account Type to: " + acctype);
	}
	
	public void setDocumentType(String documentType) {
		WebElement docType = this.findVisibleElemntByXpath("//input[@data-testid='attach_document_type'] | //div[@data-testid='attach_document_type']");
		String placeholder = docType.getAttribute("class");

		//if document type is input field
		if(placeholder!=null && placeholder.trim().toLowerCase().contains("el-input".toLowerCase()))
		{
			String disabled = docType.getAttribute("disabled");
			if(disabled!=null && disabled.trim().equalsIgnoreCase("disabled")) {
				GlobalMethods.printDebugInfo("The Document Type element is disabled");
				return;
			}
			this.moveElementToVisible(docType);
			String tagName = docType.getTagName();
			if (tagName!=null && tagName.trim().equalsIgnoreCase("div")){
				docType = this.findVisibleElemntByXpath("//div/input[@data-testid='attach_document_type']");
			}
			docType.sendKeys(documentType);
			GlobalMethods.printDebugInfo("Set Document Type to: "+documentType);
			return;
		}
		//else it is dropdown
		this.moveElementToVisible(docType);
		docType.click();
		List<WebElement> ops = driver.findElements(By.xpath("//div[@class='el-select-dropdown el-popper ht-select-dropdown' and not(contains(@style,'display'))]//li"));
		String doctype = this.selectRandomValueFromDropDownList(ops);
		GlobalMethods.printDebugInfo("Set Document Type to: " + doctype);
	}
	
	public void setBankAccCurrency() {
		WebElement bankAccCurrency = this.findVisibleElemntByXpath("//div[@data-testid='bank_account_currency']");
		this.moveElementToVisible(bankAccCurrency);
		bankAccCurrency.click();
		List<WebElement> ops = driver.findElements(By.xpath("//div[@class='el-select-dropdown el-popper ht-select-dropdown' and not(contains(@style,'display'))]//li"));
		String bankaccCur = this.selectRandomValueFromDropDownList(ops);
		GlobalMethods.printDebugInfo("Set Bank Account Currency to: " + bankaccCur);
	}
	
	public void setBankRegion() {
		WebElement bankRegion = this.findVisibleElemntByXpath("//div[@data-testid='attach_region']");
		this.moveElementToVisible(bankRegion);
		bankRegion.click();
		List<WebElement> ops = driver.findElements(By.xpath("//div[@class='el-select-dropdown el-popper ht-select-dropdown' and not(contains(@style,'display'))]//li"));
		String bankReg = this.selectRandomValueFromDropDownList(ops);
		GlobalMethods.printDebugInfo("Set Bank Region to: " + bankReg);
	}
	
	public void setLocalDepositor() {
		WebElement localdepositor = this.findVisibleElemntByXpath("//div[@data-testid='local_depositor']");
		this.moveElementToVisible(localdepositor);
		localdepositor.click();
		List<WebElement> ops = driver.findElements(By.xpath("//div[@class='el-select-dropdown el-popper ht-select-dropdown' and not(contains(@style,'display'))]//li"));
		String depositor = this.selectRandomValueFromDropDownList(ops);
		GlobalMethods.printDebugInfo("Set Depositor to: " + depositor);
	}

	@Override
	public boolean submit() {
		waitLoading();
		WebElement submit = null;
		try {
			submit = this.findClickableElementByXpath("//span[contains(text(),'Submit') or contains(text(),'SUBMIT')] | (//button[@data-testid='submit'])[2]");
        }catch(Exception e) {
			GlobalMethods.printDebugInfo("WithdrawBasePage: Failed to find the submit button.");
			return false;
		}

		this.moveElementToVisible(submit);
		js.executeScript("arguments[0].click()",submit);
        GlobalMethods.printDebugInfo("Submit withdrawal");
		this.waitLoadingForCustomise(120);;
		this.moveContainerToTop();

		WebElement response = this.findVisibleElemntByXpath("//*[contains(text(),'withdrawal request was successful') or contains(text(),'withdrawal request has been submitted successfully')]");
		String response_info = response.getText();
		if(response_info != null) {
			GlobalMethods.printDebugInfo("WithdrawBasePage: Response info: " + response_info.trim());
			if(response_info.toLowerCase().contains("successful")) {
				driver.switchTo().defaultContent();
				//goBack();
				this.waitLoading();
				return true;
			}
		}

		System.out.println("WithdrawBasePage: ERROR: withdraw request is failed");
		return false;
	}

}

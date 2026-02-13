package newcrm.pages.clientpages.withdraw;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import newcrm.global.GlobalProperties;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.WithdrawPage;
import org.testng.Assert;
import utils.LogUtils;


public abstract class BankTransferWithdrawPage extends WithdrawPage {
	WebDriverWait wait03 = new WebDriverWait(driver, Duration.ofSeconds(20));
	
	public BankTransferWithdrawPage(WebDriver driver) {
		super(driver);
	}
	
	public String getWithdrawAmount() {
		String amount = null;
		try {
			WebElement e_amount = this.findVisibleElemntByXpath("//input[@data-testid='availableAmount']");
			amount = e_amount.getAttribute("value");
		}catch(Exception e) {
			System.out.println("BankTransferWithdrawPage: ERROR: do not find the withdraw amount");
			return null;
		}
		return amount;
	}
	
	protected WebElement getCardIdDiv() {
		String xpath = "//div[@data-testid='selectedCardID']";
		wait03.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
		//WebElement result =  this.findClickableElementByXpath(xpath);
		//this.moveElementToVisible(result);
		WebElement result = driver.findElement(By.xpath(xpath));
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click()", result);
		return result;
	}
	
	protected List<WebElement> getAllCardsElement(){
		//observed the "Add New Account" selection will be selected automatically sometimes
		String xpath = "//div[contains(@class,'el-select-dropdown el-popper') and not(contains(@style,'display'))]//li/span | //div[contains(@class,'el-select-dropdown el-popper') and not(contains(@style,'display'))]//li";
		String bankaccountxpath = "//div[@data-testid='selectedCardID']";
		try {
			wait03.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
			List<WebElement> result = driver.findElements(By.xpath(xpath));
			return result;
		}catch(Exception e) {
			GlobalMethods.printDebugInfo("BankTransferWithdrawPage: Dropdown close jor");
			wait03.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(bankaccountxpath)));
			WebElement element = driver.findElement(By.xpath(bankaccountxpath));
			js.executeScript("arguments[0].click()",element);
			List<WebElement> result = driver.findElements(By.xpath(xpath));
			return result;
		}
	}

	protected List<WebElement> getAllCardsElementNew(){
		//observed the "Add New Account" selection will be selected automatically sometimes
		String xpath = "//div[contains(@class,'el-select-dropdown el-popper') and not(contains(@style,'display'))]//li";
		String bankaccountxpath = "//div[@data-testid='selectedCardID']";
		try {
			wait03.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
			List<WebElement> result = driver.findElements(By.xpath(xpath));
			return result;
		}catch(Exception e) {
			GlobalMethods.printDebugInfo("BankTransferWithdrawPage: Dropdown close jor");
			wait03.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(bankaccountxpath)));
			WebElement element = driver.findElement(By.xpath(bankaccountxpath));
			js.executeScript("arguments[0].click()",element);
			List<WebElement> result = driver.findElements(By.xpath(xpath));
			return result;
		}
	}
	
	/**
	 * 
	 * @return all available bank accounts
	 */
	public List<String> getAllBankAccounts(){
		List<String> result = new ArrayList<>();
		List<WebElement> cards = null;		
		WebElement div = this.getCardIdDiv();
		//div.click();
		//js.executeScript("arguments[0].click()",div);
		//waitLoading();
		
		//In case not succeeded in clicking CardID in PUG
		try {
			cards = this.getAllCardsElement();
		} catch (Exception e) { 
			//div.click();
			js.executeScript("arguments[0].click()",div);
			cards = this.getAllCardsElement();
		}

		for(WebElement card : cards) {
			String info = card.getAttribute("innerText");
			GlobalMethods.printDebugInfo("BankTransferWithdrawPage Card List Info: " + info);
			if(info!=null) {
				if(!info.toLowerCase().contains("add")) {
					GlobalMethods.printDebugInfo("BankTransferWithdrawPage Find card: " + info);
					result.add(info.trim());
				}
			}
		}
		
		return result;
	}
	public List<String> getAllBankAccountsNew(){
		waitLoading();
		List<String> result = new ArrayList<>();
		List<WebElement> cards = null;
		WebElement div = this.getCardIdDiv();
		//div.click();
		//js.executeScript("arguments[0].click()",div);
		waitLoading();

		//In case not succeeded in clicking CardID in PUG
		try {
			cards = this.getAllCardsElementNew();
		} catch (Exception e) {
			//div.click();
			js.executeScript("arguments[0].click()",div);
			cards = this.getAllCardsElementNew();
		}

		for(WebElement card : cards) {
			String info = card.getAttribute("innerText");
			String valueClass = card.getAttribute("class");
			if(!valueClass.trim().toLowerCase().contains("disable".toLowerCase())) {
				if (info != null) {
					if (!info.toLowerCase().contains("add")) {
						GlobalMethods.printDebugInfo("BankTransferWithdrawPage Find card: " + info);
						result.add(info.trim());
					}
				}
			}
		}

		return result;
	}
	
	public boolean chooseAddBankAccount() {
		//WebElement div = this.getCardIdDiv();
		//div.click();
		List<WebElement> cards = this.getAllCardsElement();
		
		for(WebElement card : cards) {
			String info = card.getAttribute("innerText");
			if(info!=null) {
				GlobalMethods.printDebugInfo("BankTransferWithdrawPage Find bank accounts info: " + info);
				if(info.toLowerCase().contains("add")) {
					this.moveElementToVisible(card);
					this.clickElement(card);
					//stable withdrawal fail after select add bank account
					//wait03.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='cpsForm']/form/div[2]")));
					return true;
				}
			}
		}
		System.out.println("BankTransferWithdrawPage ERROR: Choose add bank account failed");
		//div.click();
		return false;
	}

	public boolean chooseAddBankAccountNew() {
		//WebElement div = this.getCardIdDiv();
		//div.click();
		List<WebElement> cards = this.getAllCardsElementNew();

		for(WebElement card : cards) {
			String info = card.getAttribute("innerText");
			if(info!=null) {
				GlobalMethods.printDebugInfo("BankTransferWithdrawPage Find bank accounts info: " + info);
				if(info.toLowerCase().contains("add")) {
					this.moveElementToVisible(card);
					this.clickElement(card);
					//stable withdrawal fail after select add bank account
					//wait03.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='cpsForm']/form/div[2]")));
					return true;
				}
			}
		}
		System.out.println("BankTransferWithdrawPage ERROR: Choose add bank account failed");
		//div.click();
		return false;
	}

	public boolean chooseBankAccount(String account) {
		//WebElement div = this.getCardIdDiv();
		//div.click();
		List<WebElement> cards = this.getAllCardsElement();
		for(WebElement card : cards) {
			String info = card.getAttribute("innerText");
			if(info!=null) {
				//if find the account,click
				if(info.toLowerCase().contains(account.trim().toLowerCase())) {
					GlobalMethods.printDebugInfo(info);
					this.moveElementToVisible(card);
					card.click();
					GlobalMethods.printDebugInfo("BankTransferWithdrawPage Choose Bank Account: " + info);
					return true;
				}
			}
		}
		System.out.println("ERROR BankTransferWithdrawPage: Choose bank account " + account + " failed");
		//div.click();
		return false;
	}

	public boolean chooseBankAccountNew(String account) {
		//WebElement div = this.getCardIdDiv();
		//div.click();
		List<WebElement> cards = this.getAllCardsElementNew();
		for(WebElement card : cards) {
			String info = card.getAttribute("innerText");
			if(info!=null) {
				//if find the account,click
				if(info.toLowerCase().contains(account.trim().toLowerCase())) {
					GlobalMethods.printDebugInfo(info);
					this.moveElementToVisible(card);
					card.click();
					GlobalMethods.printDebugInfo("BankTransferWithdrawPage Choose Bank Account: " + info);
					return true;
				}
			}
		}
		System.out.println("ERROR BankTransferWithdrawPage: Choose bank account " + account + " failed");
		//div.click();
		return false;
	}
	
	protected void setValue(String testID, String value) {
        try {
			WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(5));
			WebElement input_element=webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@data-testid='"+testID+"'] | //input[contains(@id,'"+testID+"')] | //textarea[@data-testid='"+testID+"']")));
			input_element.click();
            input_element.clear();

			String disabled = input_element.getAttribute("disabled");
			//if the input element is disabled, return;
			if(disabled!=null && disabled.trim().equalsIgnoreCase("disabled")) {
				GlobalMethods.printDebugInfo("BankTransferWithdrawPage: The " + testID +" element is disabled");
				return;
			}

			this.moveElementToVisible(input_element);
			input_element.sendKeys(value);
			GlobalMethods.printDebugInfo("BankTransferWithdrawPage: Set " +testID +" to : " +value );
        } catch (Exception e) {
           e.printStackTrace();
        }

	}
	
	//the following funtions is used by bank transfer
	protected WebElement getRegionInput() {
		WebElement div_region;
		try {
			div_region= driver.findElement(By.xpath("//div[@data-testid='country_region']/div/input"));
			div_region.isDisplayed();
			GlobalMethods.printDebugInfo("Find country region field");
			this.moveElementToVisible(div_region);
			return div_region;
		}
		catch (Exception e)
		{
			GlobalMethods.printDebugInfo("Country region field not found");
			return null;
		}

	}
	
	protected List<WebElement> getRegionElements(){
		String xpath = "//div[contains(@class,'el-select-dropdown el-popper' ) and not(contains(@style,'display'))]//li/span";
		this.findVisibleElemntByXpath(xpath);
		List<WebElement> result = driver.findElements(By.xpath(xpath));
		return result;
	}
	
	protected boolean setRegion(String region) {
		WebElement input_region = this.getRegionInput();
		if(input_region == null) {
			GlobalMethods.printDebugInfo("BankTransferWithdrawPage: Country/Region field not found");
			return false;
		}
		String disabled = input_region.getAttribute("disabled");
		//if the input element is disabled, return;
		if(disabled!=null && disabled.trim().equalsIgnoreCase("disabled")) {
			GlobalMethods.printDebugInfo("BankTransferWithdrawPage: The Country/Region element is disabled");
			return true;
		}
		
		input_region.click();
		List<WebElement> regions = this.getRegionElements();
		for(WebElement e_region: regions) {
			String info = e_region.getAttribute("innerText");
			if(info != null) {
				GlobalMethods.printDebugInfo("Find Region: " + info);
				if(info.toLowerCase().trim().contains(region.toLowerCase().trim())) {
					this.moveElementToVisible(e_region);
					e_region.click();
					GlobalMethods.printDebugInfo("Set Region: " + info);
					return true;
				}
			}
		}
		input_region.click();
		GlobalMethods.printDebugInfo("BankTransferWithdrawPage: Do not Find the region: " + region);
		return false;
	}
	
	protected String getRegion() {
		String result = null;
		/*
		List<WebElement> regions = driver.findElements(By.xpath("(//ul[@class='el-scrollbar__view el-select-dropdown__list'])[4]/li"));
		for(WebElement region : regions) {
			String info = region.getAttribute("class");
			if(info != null) {
				if(info.toLowerCase().contains("selected")) {
					result = region.getAttribute("innerText").trim();
					GlobalMethods.printDebugInfo("BankTransferWithdrawPage: Region is: " + result);
				}
			}
		}*/
		WebElement region = driver.findElement(By.xpath("//div[@data-testid='country_region']/div/input"));
		result = region.getAttribute("value");
		return result.trim();
	}
}

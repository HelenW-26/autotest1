package newcrm.pages.clientpages;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import newcrm.global.GlobalMethods;
import newcrm.pages.Page;
import newcrm.pages.clientpages.elements.TransferPageElements;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

public class TransferPage extends Page {

	
	public TransferPage(WebDriver driver) {
		super(driver);
	}
	
	public class Account{
		private String account_number;
		private String currency;
		private String balance;
		
		public Account(String acc_number,String v_currency,String v_balance) {
			this.account_number = acc_number.trim().toLowerCase();
			this.currency = v_currency.trim().toLowerCase();
			this.balance = v_balance.trim().toLowerCase();
		}
		
		public String getAccNumber() {
			return this.account_number;
		}
		
		public String getCurrency() {
			return this.currency;
		}
		
		public String getBalance() {
			return this.balance;
		}
	}
	
	public void checkElement() {
		TransferPageElements p = PageFactory.initElements(driver, TransferPageElements.class);
		p.e_acc_from.click();
		p.e_acc_to.click();
		p.e_amount.sendKeys("100");
		p.e_submit.click();
	}

	/**
	 * Get all accounts (Both MT accounts and Crypto Wallet accounts)
	 */
	private List<Account> getAccounts(List<WebElement> els){
		List<Account> result = new ArrayList<Account>();
		if(els ==null || els.size()==0) {
			return result;
		}
		for(WebElement element:els) {
			String info = element.getAttribute("innerText");
			if(info!=null && info.trim() !="") {
				GlobalMethods.printDebugInfo("TransferPage Find an account: " + info);
				String values[] = info.split("-");
				String accNum = values[0].trim();
				String b_c = values[1].trim();
				List<String> cur_amount = GlobalMethods.getCurrencyAndAmount(b_c);
				Account account = new Account(accNum,cur_amount.get(0),cur_amount.get(1));
				result.add(account);
			}
		}
		return result;
	}


	/**
	 * Get MT from accounts only(exclude Crypto Wallet accounts)
	 */
	public List<Account> getFromAccounts(){
		waitLoading();
		TransferPageElements p = PageFactory.initElements(driver, TransferPageElements.class);
		this.moveElementToVisible(p.e_acc_from);
		p.e_acc_from.click();
		List<WebElement> els = this.getAllOpendElements();
		List<Account> result =  getMTAccounts(els);
		p.e_acc_from.click();
		return result;
	}

	public List<Account> getFromAccountsNew() {
		waitLoading();
		TransferPageElements p = PageFactory.initElements(driver, TransferPageElements.class);
		this.moveElementToVisible(p.e_acc_from);
		p.e_acc_from.click();
		GlobalMethods.printDebugInfo("Open Transfer From Account List");

		List<WebElement> els = this.getAllOpendElements();
		List<Account> result =  getMTAccountsNew(els);

		return result;
	}

	/**
	 * Get MT to accounts only(exclude Crypto Wallet accounts)
	 */
	public List<Account> getToAccount(){
		waitLoading();
		TransferPageElements p = PageFactory.initElements(driver, TransferPageElements.class);
		this.moveElementToVisible(p.e_acc_to);
		p.e_acc_to.click();
		waitLoading();
		List<WebElement> els = this.getAllOpendElements();
		List<Account> result =  getMTAccounts(els);
		p.e_acc_to.click();
		waitLoading();
		return result;
	}

	public List<Account> getToAccountNew() {
		waitLoading();
		TransferPageElements p = PageFactory.initElements(driver, TransferPageElements.class);
		this.moveElementToVisible(p.e_acc_to);
		p.e_acc_to.click();
		GlobalMethods.printDebugInfo("Open Transfer To Account List");

		List<WebElement> els = this.getAllOpendElements();
		List<Account> result =  getMTAccountsNew(els);

		return result;
	}

	/**
	 * Get MT Accounts only(exclude Crypto Wallet accounts)
	 */
	private List<Account> getMTAccounts(List<WebElement> els){
		List<Account> result = new ArrayList<Account>();
		if(els ==null || els.size()==0) {
			return result;
		}
		for(WebElement element:els) {
			String info = element.getAttribute("innerText");
			if(info!=null && info.trim() !="" && !info.trim().contains("Wallet")) {
				GlobalMethods.printDebugInfo("TransferPage Find an account: " + info);
				String values[] = info.split("-");
				String accNum = values[0].trim();
				String b_c = values[1].trim();
				List<String> cur_amount = GlobalMethods.getCurrencyAndAmount(b_c);
				Account account = new Account(accNum,cur_amount.get(0),cur_amount.get(1));
				result.add(account);
			}
		}
		return result;
	}

	private List<Account> getMTAccountsNew(List<WebElement> accounts_elements) {
		List<Account> result = new ArrayList<Account>();

		if(accounts_elements ==null || accounts_elements.isEmpty()) {
			return result;
		}

		for(WebElement element:accounts_elements) {
			String info = element.getText();

			if(info != null && info.trim() != "" && !info.trim().contains("Wallet")) {
				String inner_text_trim = info.trim();
				String values[] = inner_text_trim.split("-");
				String accNum = values[0].trim();
				String b_c = values[1].trim();

				List<String> cur_amount = GlobalMethods.getCurrencyAndAmount(b_c);
				Account account = new Account(accNum, cur_amount.get(0), cur_amount.get(1));

				result.add(account);
			}
		}

		return result;
	}

	public void setAmount(String amount) {
		TransferPageElements p = PageFactory.initElements(driver, TransferPageElements.class);
		this.moveElementToVisible(p.e_amount);
		p.e_amount.clear();
		p.e_amount.sendKeys(amount);
		waitLoading();
		GlobalMethods.printDebugInfo("Set Transfer Amount : " + amount);
	}
	
	public void selectFromAccount(String account) {
		TransferPageElements p = PageFactory.initElements(driver, TransferPageElements.class);
		this.moveElementToVisible(p.e_acc_from);
		p.e_acc_from.click();
		waitLoading();
		List<WebElement> els = this.getAllOpendElements();
		for(WebElement e:els) {
			String info = e.getAttribute("innerText");
			if(info !=null && !info.trim().equals("") && info.contains(account)) {
				//this.moveElementToVisible(e);
				e.click();
				waitLoading();
				GlobalMethods.printDebugInfo("TransferPage - set Transfer From account : " + info);
				return;
			}
		}
		GlobalMethods.printDebugInfo("TransferPage - set Transfer From account : " + account + " failed!");
		p.e_acc_from.click();
		waitLoading();
	}

	public void selectFromAccountNew(String account) {
		WebElement element = assertElementExists(By.xpath("//div[contains(@class, 'el-select-dropdown el-popper') and not(contains(@style, 'display: none'))]//li[@data-testid='" + account + "']"), "Account Number: " + account);
		String info = element.getAttribute("innerText").trim();
		triggerClickEvent(element);
		System.out.println("Select Transfer From Account: " + info);
	}

	public void selectToAccount(String account) {
		TransferPageElements p = PageFactory.initElements(driver, TransferPageElements.class);
		this.moveElementToVisible(p.e_acc_to);
		p.e_acc_to.click();
		List<WebElement> els = this.getAllOpendElements();
		for(WebElement e:els) {
			String info = e.getAttribute("innerText");
			if(info !=null && !info.trim().equals("") && info.contains(account)) {
				//this.moveElementToVisible(e);
				e.click();
				GlobalMethods.printDebugInfo("TransferPage - set Transfer To account : " + info);
				return;
			}
		}
		GlobalMethods.printDebugInfo("TransferPage - set Transfer To account : " + account);
		p.e_acc_to.click();
	}

	public void selectToAccountNew(String account) {
		WebElement element = assertElementExists(By.xpath("//div[contains(@class, 'el-select-dropdown el-popper') and not(contains(@style, 'display: none'))]//li[@data-testid='" + account + "']"), "Account Number: " + account);
		String info = element.getAttribute("innerText").trim();
		triggerClickEvent(element);
		System.out.println("Select Transfer To Account: " + info);
	}

    public void selectWalletAccount(String walletType){
        WebElement element = assertElementExists(By.xpath("//div[contains(@class, 'el-select-dropdown el-popper') and not(contains(@style, 'display: none'))]//li[contains(@data-testid,'" + walletType + "')]"), "wallet type: " + walletType);
        String info = element.getAttribute("innerText").trim();
        triggerClickEvent(element);
        System.out.println("Select Transfer wallet Account: " + info);
    }

    public void clickAccFrom(){
        TransferPageElements p = PageFactory.initElements(driver, TransferPageElements.class);
        this.moveElementToVisible(p.e_acc_from);
        p.e_acc_from.click();
    }

    public void clickAccTo(){
        TransferPageElements p = PageFactory.initElements(driver, TransferPageElements.class);
        this.moveElementToVisible(p.e_acc_to);
        p.e_acc_to.click();
    }

	public void submit() {
		TransferPageElements p = PageFactory.initElements(driver, TransferPageElements.class);
		this.moveElementToVisible(p.e_submit);
		p.e_submit.click();
		this.waitLoading();
	}

    public void submitTransfer(){
        TransferPageElements p = PageFactory.initElements(driver, TransferPageElements.class);
        this.moveElementToVisible(p.e_submit);
        p.e_submit.click();
        this.waitLoading();
    }

    public void confirmTransfer(){
        WebElement confirmBtn = findClickableElementByXpath("//div[@class='el-dialog__footer']//button[.//span[normalize-space(text())='Confirm transfer']]");
        confirmBtn.click();
    }

    public void giveUpCreditBtn(){
        WebElement confirmBtn = findClickableElementByXpath("//div[@class='el-dialog__footer']//button[.//span[normalize-space(text())='GIVE UP']]");
        confirmBtn.click();
    }

    public void clickUseCreditBtn(){
        return;
//        WebElement confirmBtn = findClickableElementByXpath("(//div[@class='el-dialog__footer']//button[.//span[normalize-space(text())='TRANSFER']])[2]");
//        confirmBtn.click();
    }

    public List<List<String>> getCreditList(){
        List<List<String>> returnList = new ArrayList<>();

        List<WebElement> rows = driver.findElements(
                By.xpath("//div[@role='dialog']//table[@class='el-table__body']/tbody/tr"));

        for (int i = 1; i <= rows.size(); i++) {
            List<String> rowData = new ArrayList<>();
            List<WebElement> cols = driver.findElements(
                    By.xpath("//div[@role='dialog']//table[@class='el-table__body']/tbody/tr[" + i + "]/td//div[@class='cell']")
            );
            for (WebElement col : cols) {
                rowData.add(col.getText().trim());
            }
            returnList.add(rowData);
        }

        return returnList;

    }

    public void gotoHistory(){
        this.waitLoading();
        WebElement confirmBtn = findClickableElementByXpath("//button[@data-testid='button']/span[translate(normalize-space(text()),'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')='view the order']");
        confirmBtn.click();
    }


    public Map<String,String> getTransferReviewValue(){
        Map<String, String> result = new HashMap<>();

        // get from account and to account
        List<WebElement> items = driver.findElements(By.xpath("//div[@class='exchange-item']"));
        for (WebElement item : items) {
            String label = item.findElement(By.className("exchange-item__label")).getText().trim();

            String value = item.findElement(By.xpath(".//span[@class='value']")).getText().trim();

            result.put(label, value);
        }

        // get transfer value
        List<WebElement> list = driver.findElements(By.xpath("//ul[@class='info-warp']/li"));

        for (WebElement item : list) {
            String label = item.findElement(By.xpath(".//span[contains(@class,'label')]"))
                    .getText().trim();

            String value = item.findElement(By.xpath(".//span[contains(@class,'value')]"))
                    .getText().trim();

            result.put(label, value);
        }

        return result;

    }

	public void waitLoadingTransferFromAccountContent() {
		waitLoading();

		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
			TransferPageElements p = PageFactory.initElements(driver, TransferPageElements.class);

			wait.until(driver -> {
				try {
					return p.e_acc_from.isDisplayed();

				} catch (Exception ex) {
					return false;
				}
			});
		} catch (Exception ex) {
			Assert.fail("No Transfer From account available or timeout waiting for account listing");
		}
	}

	public void waitLoadingTransferToAccountContent() {
		waitLoading();

		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
			TransferPageElements p = PageFactory.initElements(driver, TransferPageElements.class);

			wait.until(driver -> {
				try {
					return p.e_acc_to.isDisplayed();

				} catch (Exception ex) {
					return false;
				}
			});
		} catch (Exception ex) {
			Assert.fail("No Transfer To account available or timeout waiting for account listing");
		}
	}

	//return non-copy trading accounts
	private List<Account> getNonCPAccounts(List<WebElement> accounts_elements) {
		List<Account> result = new ArrayList<Account>();

		if(accounts_elements ==null || accounts_elements.isEmpty()) {
			return result;
		}

		for(WebElement element:accounts_elements) {
			String info = element.getText();

			if (info != null && !info.trim().isEmpty() && !StringUtils.containsIgnoreCase(info.trim(), "MTS") &&!StringUtils.containsIgnoreCase(info.trim(), "Copy Trading") && !StringUtils.containsIgnoreCase(info.trim(), "Wallet")) {
			    String inner_text_trim = info.trim();
				String values[] = inner_text_trim.split("-");
				String accNum = values[0].trim();
				String b_c = values[1].trim();

				List<String> cur_amount = GlobalMethods.getCurrencyAndAmount(b_c);
				Account account = new Account(accNum, cur_amount.get(0), cur_amount.get(1));

				result.add(account);
			}
		}

		return result;
	}

	//return copy trading accounts
	private List<Account> getCPAccounts(List<WebElement> accounts_elements) {
		List<Account> result = new ArrayList<Account>();

		if(accounts_elements ==null || accounts_elements.isEmpty()) {
			return result;
		}

		for(WebElement element:accounts_elements) {
			String info = element.getText();

			if(info != null && info.trim() != "" && (info.trim().contains("MTS") || info.trim().contains("Copy Trading"))) {
				String inner_text_trim = info.trim();
				String values[] = inner_text_trim.split("-");
				String accNum = values[0].trim();
				String b_c = values[1].trim();

				List<String> cur_amount = GlobalMethods.getCurrencyAndAmount(b_c);
				Account account = new Account(accNum, cur_amount.get(0), cur_amount.get(1));

				result.add(account);
			}
		}

		return result;
	}

	//get non copytrading accounts for transfer
	public List<Account> getFromNonCPAccounts() {
		waitLoading();
		TransferPageElements p = PageFactory.initElements(driver, TransferPageElements.class);
		this.moveElementToVisible(p.e_acc_from);
		p.e_acc_from.click();
		GlobalMethods.printDebugInfo("Open Transfer From Account List");

		List<WebElement> els = this.getAllOpendElements();
		List<Account> result =  getNonCPAccounts(els);

		return result;
	}

	//get non copytrading accounts for transfer
	public List<Account> getToNonCPAccounts() {
		waitLoading();
		TransferPageElements p = PageFactory.initElements(driver, TransferPageElements.class);
		this.moveElementToVisible(p.e_acc_to);
		p.e_acc_to.click();
		GlobalMethods.printDebugInfo("Open Transfer To Account List");

		List<WebElement> els = this.getAllOpendElements();
		List<Account> result =  getNonCPAccounts(els);

		return result;
	}

	//get copytrading accounts for transfer
	public List<Account> getToCPAccount() {
		waitLoading();
		TransferPageElements p = PageFactory.initElements(driver, TransferPageElements.class);
		this.moveElementToVisible(p.e_acc_to);
		p.e_acc_to.click();
		GlobalMethods.printDebugInfo("Open Transfer To Account List");

		List<WebElement> els = this.getAllOpendElements();
		List<Account> result =  getCPAccounts(els);

		return result;
	}

}

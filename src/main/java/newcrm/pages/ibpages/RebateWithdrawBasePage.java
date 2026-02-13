package newcrm.pages.ibpages;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.*;

import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties.CURRENCY;
import newcrm.global.GlobalProperties.DEPOSITMETHOD;
import newcrm.pages.Page;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.testng.Assert;
import tools.ScreenshotHelper;
import utils.LogUtils;

public class RebateWithdrawBasePage extends Page {

	public class Account{
		public String accNum;
		public CURRENCY currency;
		public Double balance;

		public Account(String accNum,CURRENCY currency, Double balance) {
			this.accNum = accNum.trim();
			this.currency = currency;
			this.balance = balance;
		}
	}

	WebDriverWait wait03;

	public RebateWithdrawBasePage(WebDriver driver) {
		super(driver);
		this.wait03 = new WebDriverWait(driver, Duration.ofSeconds(20));
	}

	protected WebElement getRebateAccountInput() {
		return this.findClickableElemntByTestId("rebateAccount");
	}

	protected WebElement getRebateAccountInputNew() {
		return assertElementExists(By.xpath("//div[@data-testid='formAccount']"), "IB rebate account");
	}

	public List<Account> getAccountInfos(){
		ArrayList<Account> result = new ArrayList<>();
		WebElement input = this.getRebateAccountInput();
		this.moveElementToVisible(input);
		input.click();
		List<WebElement> accs = this.getAllOpendElements();
		for(WebElement e:accs) {
			String info = e.getAttribute("innerText");
			if(info!=null && info.trim().length()>0) {
				String accNum = info.substring(0,info.trim().indexOf("-")).trim();
				String currency = info.substring(info.trim().lastIndexOf(" ") +1);
				String t_balance = info.substring(info.trim().indexOf("-")+1, info.trim().lastIndexOf(" "));
				Double balance = Double.valueOf(t_balance);
				CURRENCY v_currency  = null;
				for(CURRENCY c : CURRENCY.values()) {
					if(c.toString().equals(currency.trim())){
						v_currency = c;
					}
				}
				result.add(new Account(accNum,v_currency,balance));
			}
		}
		input.click();
		return result;
	}

	public void checkForAvailableAccount() {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
			wait.until(driver -> {
				try {
					return !driver.findElements(By.xpath("//div[@data-testid='formAccount']")).isEmpty();

				} catch (Exception ex) {
					return false;
				}
			});
		} catch (Exception ex) {
			Assert.fail("No account available or timeout waiting for account listing");
		}
	}

	public List<Account> getAccountInfosNew(){
		checkForAvailableAccount();

		ArrayList<Account> result = new ArrayList<>();
		WebElement input = this.getRebateAccountInputNew();
//		this.moveElementToVisible(input);
		input.click();
		List<WebElement> accs = this.getAllOpendElements();
		for(WebElement e:accs) {
			String info = e.getText();
			if(info!=null && info.trim().length()>0) {
				String values[] = info.split("-");
				String accNum = values[0].trim();
				String b_c = values[1].trim();

				List<String> currAmount = GlobalMethods.getCurrencyAndAmount(b_c);
				Double balance = Double.valueOf(currAmount.get(1));
				CURRENCY currency  = null;
				for(CURRENCY c : CURRENCY.values()) {
					if(c.toString().equals(currAmount.get(0))){
						currency = c;
						break;
					}
				}
				result.add(new Account(accNum,currency,balance));
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
				GlobalMethods.printDebugInfo("RebateWithdrawBasePage: Set IB rebate account to : " + accNum);
				return true;
			}
		}
		input.click();
		GlobalMethods.printDebugInfo("RebateWithdrawBasePage: Set IB rebate account to : " + accNum + " failed!");
		return false;
	}

	public boolean setRebateAccountNew(String accNum) {
		WebElement input = this.getRebateAccountInputNew();
//		this.moveElementToVisible(input);
		input.click();
		waitLoading();
		List<WebElement> accs = this.getAllOpendElements();
		for(WebElement e : accs) {
			String info = e.getText();
			if(info!=null && info.contains(accNum)) {
				this.moveElementToVisible(e);
				this.clickElement(e);
				waitLoading();
				GlobalMethods.printDebugInfo("RebateWithdrawBasePage: Set IB rebate account to : " + accNum);
				return true;
			}
		}

        input.click();
        waitLoading();
        GlobalMethods.printDebugInfo("RebateWithdrawBasePage: Set IB rebate account to : " + accNum + " failed!");
        return false;
    }

    public void setAmount(String amount) {
        WebElement amount_input = assertElementExists(By.xpath("//input[@data-testid='numericInput']"), "Amount");
        setInputValue_withoutMoveElement(amount_input, amount);
        GlobalMethods.printDebugInfo("RebateWithdrawBasePage: Set IB rebate withdraw amount to " + amount);
    }

    public boolean submit() {
        // 等待并找到元素
        this.waitLoading();
		WebElement confirmbutton =driver.findElements(By.cssSelector("[data-testid='submit']"))
				.stream()
				.filter(e -> e.getText().toLowerCase().contains("submit"))
				.findFirst()
				.orElseThrow();
        System.out.println("Element found: " + confirmbutton);

        // 滚动到元素并确保可见
        this.moveElementToVisible(confirmbutton); // 使用Page类中的方法

        // 强制等待一小段时间确保页面稳定
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // 使用Page类中已有的clickElement方法
        this.clickElement(confirmbutton);

        this.waitLoading();
        GlobalMethods.printDebugInfo("Submit IB withdrawal");
        this.moveContainerToTop();
        return true;
    }

	public void clickContinue() {
		WebElement button = assertClickableElementExists(By.xpath("(//button[@data-testid='submit'])[1]"), "Submit button");
//		this.moveElementToVisible(button);
		js.executeScript("arguments[0].click()", button);
		this.waitLoading();
	}

	public List<DEPOSITMETHOD> getAllMethods(){
		ArrayList<DEPOSITMETHOD> result = new ArrayList<>();
		List<WebElement> methods = driver.findElements(By.xpath("//label[@role='radio']/span[2]"));
		for(WebElement e : methods) {
			String method = e.getAttribute("innerText");
			if(method !=null && method.trim().length()>0) {
				for(DEPOSITMETHOD d : DEPOSITMETHOD.values()) {
					if(d.getIBWithdrawName().toLowerCase().contains(method.trim().toLowerCase())) {
						result.add(d);
					}
				}
			}
		}
		return result;
	}

	public void setMethod(DEPOSITMETHOD method) {
		WebElement e_method = this.findClickableElemntByTestId(method.getIBWithdrawTypeDataTestId());
		this.moveElementToVisible(e_method);

		GlobalMethods.printDebugInfo("RebateWithdrawBasePage: Select IB withdraw method : " + method.getWithdrawName());
		e_method.click();
		this.waitLoading();
	}

	public void setMethodNew(DEPOSITMETHOD method) {
		WebElement m_element = this.getMethodDiv();

		if(m_element == null) {
			System.out.println("RebateWithdrawBasePage: Failed to find IB withdrawal type.");
			Assert.fail("Failed to find IB withdrawal type.");
		}

		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click()",m_element);
		waitLoading();

		WebElement element = getWithdrawMethodElementNew(method);

		try {
			this.moveElementToVisible(element);
			js.executeScript("arguments[0].click()",element);
			waitLoading();
			System.out.println("RebateWithdrawBasePage: Select IB withdraw method: " + method.getIBWithdrawTypeDataTestId());
		}catch(Exception e) {
			Assert.fail("Could not find the IB withdraw method: " + method.getIBWithdrawTypeDataTestId());
		}

		this.moveContainerToTop();
	}

	public void setNotes(String note) {
		WebElement e = assertElementExists(By.xpath("(//input[@data-testid='importantNotes'])[2] | //input[@data-testid='importantNotes']"), "Important Notes");
		setInputValue_withoutMoveElement(e, note);
		GlobalMethods.printDebugInfo("RebateWithdrawBasePage: Set IB rebate withdraw note to " + note);
	}

	/*
	 * It's very tricky the xpath varies with headless on and off(different screen resolution).
	 * There are also two same elements of //div[@id='tab-localTransfer'].
	 * Therefore use for loop to make sure the clicking succeed.
	 */
	public void clickLocalTransfer() {
		List<WebElement> result = driver.findElements(By.xpath("//div[@id='tab-localTransfer']"));
		for(WebElement tab : result) {
			this.moveElementToVisible(tab);
			try {
				tab.click();
			}catch (Exception e) {
			}
		}

	}

	public void clickEWallet() {
		List<WebElement> result = driver.findElements(By.xpath("//div[@id='tab-eWallet']"));
		for(WebElement tab : result) {
			this.moveElementToVisible(tab);
			try {
				tab.click();
			}catch (Exception e) {
			}
		}
	}

	public void clickCrypto() {
		List<WebElement> result = driver.findElements(By.xpath("//div[@id='tab-cryptoCurrency']"));
		for(WebElement tab : result) {
			this.moveElementToVisible(tab);
			try {
				tab.click();
			}catch (Exception e) {
			}
		}
	}

	public void clickIBT() {
		List<WebElement> result = driver.findElements(By.xpath("//div[@id='tab-bankTransfer']"));
		for(WebElement tab : result) {
			this.moveElementToVisible(tab);
			try {
				tab.click();
			}catch (Exception e) {
			}
		}
	}
	protected WebElement getMethodDiv() {
		WebElement method = null;

		try {
			String divIdName = "withdrawalType";
			method = this.findClickableElemntByTestId(divIdName);
		}catch(Exception e) {
			System.out.println("RebateWithdrawBasePage: Could not find IB withdraw methods.");
			return null;
		}

		return method;
	}

	protected WebElement getWithdrawMethodElementNew(DEPOSITMETHOD method) {
		waitLoading();
		WebElement w_method = null;

		String xpath = "//li//*[contains(text(), '"+method.getIBWithdrawTypeDataTestId()+"')] | (//span[contains(normalize-space(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')), '" + method.getIBWithdrawTypeDataTestId().toLowerCase() + "')])[1]";
		LogUtils.info("RebateWithdrawBasePage: Get IB withdraw method element: " + xpath);
		try {
			wait03.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
			moveElementToVisible(driver.findElement(By.xpath(xpath)));
			w_method =  driver.findElement(By.xpath(xpath));
		}catch(Exception e) {
			Assert.fail("Could not find the IB withdraw method: " + method.getIBWithdrawTypeDataTestId());
		}

		return w_method;
	}

	protected WebElement getWithdrawMethodElementNewPlus(DEPOSITMETHOD method) {
		waitLoading();
		WebElement w_method = null;

		String xpath = "//*[@id=\"rebateWithdraw\"]//form//div[contains(@class,'el-scrollbar__view')]//li";
		LogUtils.info("RebateWithdrawBasePage: Get IB withdraw method element: " + xpath);
		try {
			List<WebElement> elements = driver.findElements(By.xpath(xpath)); wait03.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
			for(WebElement e : elements) {
				String methodName = e.getText();
				if(methodName.contains(method.getIBWithdrawTypeDataTestId())) {
					LogUtils.info("RebateWithdrawBasePage: Get IB withdraw method element: " + methodName);
					ScreenshotHelper.highlightElement(driver, e);
					w_method = e;
					break;
				}
			}

		}catch(Exception e) {
			Assert.fail("Could not find the IB withdraw method: " + method.getIBWithdrawTypeDataTestId());
		}

		return w_method;
	}

	//Check if the file upload is done
	public void checkFileUploadingCompleted(){
		WebElement uploadFile = driver.findElement(By.name("file"));
		this.moveElementToVisible(uploadFile);
		assertVisibleElementExists(By.xpath("//ul/li[@class='el-upload-list__item is-success']"), "File Uploaded");
	}

}

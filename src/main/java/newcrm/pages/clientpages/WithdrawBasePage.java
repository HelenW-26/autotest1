package newcrm.pages.clientpages;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import newcrm.global.GlobalProperties;
import org.openqa.selenium.*;

import newcrm.global.GlobalMethods;
import newcrm.pages.Page;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import tools.ScreenshotHelper;
import utils.CustomAssert;

public abstract class WithdrawBasePage extends Page {
	public WithdrawBasePage(WebDriver driver) {
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
	
	protected WebElement getAccountInput() {
		waitLoading();
		WebElement dropdown_account = this.findClickableElemntBy(By.xpath("//div[@data-testid='formAccount']"));
		return dropdown_account;
	}
	
	protected List<WebElement> getAccountElements(){
		//this.findVisibleElemntBy(By.cssSelector("ul.el-scrollbar__view.el-select-dropdown__list li span"));
		List<WebElement> result =  driver.findElements(By.xpath("//div[contains(@class, 'el-select-dropdown el-popper') and not(contains(@style,'display'))]//li//span"));
		return result;
	}

	public WebElement getAccountEle(String accNum) {
		return assertElementExists(By.xpath("//li[@data-testid='" + accNum + "']"), "Account Number: " + accNum);
	}

	protected List<Account> getAccounts(List<WebElement> accounts_elements){
		List<Account> result = new ArrayList<>();
		String b_c;

		for(WebElement element:accounts_elements) {
			String info = element.getAttribute("innerText");
			GlobalMethods.printDebugInfo("WithdrawBasePage Find an account: " + info);
			if(info!=null && info.trim() !="") {
				String values[] = info.split("-");
				//String accNum = info.substring(0,info.trim().indexOf("-")).trim();
				String accNum = values[0].trim();
				//String currency = info.substring(info.trim().lastIndexOf(" ") +1);
				System.out.println("values[1]:" + values[1]);
				System.out.println("values[0]:" + values[0]);

				List<String> brands = Arrays.asList("vfx","um","mo","star","vjp","pug", "vt");
				if ( brands.contains(GlobalProperties.brand.toLowerCase())){
					b_c = values[1].trim();// add account type in the middle: 804394524 - Hedge ECN - ￡100.00 GBP
					//b_c is A$444.43 AUD
				}
				else
				{
					b_c = values[2].trim();

				}

				List<String> cur_amount = GlobalMethods.getCurrencyAndAmount(b_c);
				Account account = new Account(accNum,cur_amount.get(0),cur_amount.get(1));

				result.add(account);
			}
		}

		return result;
	}

	protected List<Account> getAccountsNew(List<WebElement> accounts_elements) {
		List<Account> result = new ArrayList<>();

		for(WebElement element:accounts_elements) {
			String info = element.getText();

			if(info != null && info.trim() != "") {
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

	public void waitLoadingWithdrawalAccountContent() {
		waitLoading();

		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
			wait.until(driver -> {
				try {
					boolean inputPresent = !driver.findElements(By.xpath("//div[@data-testid='accountNumber']")).isEmpty();
					boolean imagePresent = !driver.findElements(By.xpath("//div[@data-testid='formAccount']")).isEmpty();
					return inputPresent || imagePresent;

				} catch (Exception ex) {
					return false;
				}
			});
		} catch (Exception ex) {
			Assert.fail("No account available or timeout waiting for account listing");
		}
	}

	public List<Account> getAllAccounts(){
		this.getAccountInput().click();
		List<Account> result = this.getAccounts(getAccountElements());
		this.getAccountInput().click();
		return result;
	}

	public List<Account> getAllAccountsNew() {
		this.getAccountInput().click();
		waitLoading();

		List<Account> result = this.getAccountsNew(getAllOpendElements());

		return result;
	}

	public Account getSpecificAccount(String accNum) {
		this.getAccountInput().click();
		waitLoading();

		List<WebElement> accounts_elements = new ArrayList<>();
		accounts_elements.add(getAccountEle(accNum));

		List<Account> result = this.getAccountsNew(accounts_elements);

		return result.get(0);
	}

	public String setAccount(String accNum) {
		this.getAccountInput().click();
		waitLoading();
		List<WebElement> elements = this.getAccountElements();
		for(WebElement element: elements) {
			if(element.getAttribute("innerText").contains(accNum)) {
				this.moveElementToVisible(element);
				String info = element.getAttribute("innerText");
				this.clickElement(element);
				waitLoading();
				System.out.println("WithdrawBasePage: Select account: " + info );
				return accNum;
			}
		}

		System.out.println("WithdrawBasePage: Do not find the specific account number: " + accNum);
		return null;
	}

	public void setAccountNew(String accNum) {
		WebElement element = assertElementExists(By.xpath("//li[@data-testid='" + accNum + "']"), "Account Number: " + accNum);
		String info = element.getAttribute("innerText").trim();
		triggerClickEvent(element);
		System.out.println("Select account: " + info);
	}

	public void setAmount(String amount) {
		WebElement input_amount = assertVisibleElementExists(By.xpath("//input[@data-testid='numericInput']"), "Amount");
		input_amount.sendKeys(amount);
	}
	
	public void clickContinue() {
		this.findClickableElementByXpath("(//button[@data-testid='submit'])[1]").click();
        waitLoading();
	}
	
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
		GlobalMethods.printDebugInfo("Submit Payment");
		//submit.click();
		this.waitLoadingForCustomise(120);;
		//WebElement response = this.findVisibleElemntByXpath("//div[contains(text(),'withdrawal request')]");
		//Update the xpath element as AT brand unable to detect
		WebElement response = this.findVisibleElemntByXpath("//*[contains(text(),'withdrawal request was successful') or contains(text(),'withdrawal request has been submitted successfully')]");
		String response_info = response.getText();
		if(response_info != null) {
			GlobalMethods.printDebugInfo("WithdrawBasePage: Response info: " + response_info.trim());
			if(response_info.toLowerCase().contains("successful")) {
				//withdraw request is succeed. return to home page
				//this.findClickableElementByXpath("//*[@data-testid='bkToHm']").click();
				driver.switchTo().defaultContent();
				goBack();
				this.waitLoading();
				return true;
			}
		}
		
		System.out.println("WithdrawBasePage: ERROR: withdraw requestion is failed");
		return false;
	}

	public void submitWithoutCheck() {
		WebElement submit = null;
		try {
			//submit = this.findClickableElementByXpath("//span[contains(text(),'Submit') or contains(text(),'SUBMIT')]");
			submit = this.findClickableElementByXpath("span[contains(text(),'Submit') or contains(text(),'SUBMIT')] | (//button[@data-testid='submit'])[2]");

		}catch(Exception e) {
			GlobalMethods.printDebugInfo("WithdrawBasePage: Failed to find the submit button.");
		}

		this.moveElementToVisible(submit);

		js.executeScript("arguments[0].click()",submit);
		//submit.click();
		this.waitLoadingForCustomise(120);;

	}


	public boolean codeConfirm() {
		WebElement confirmBtn = null;
		try {
			confirmBtn = this.findClickableElementByXpath("(//button[@data-testid='changePw'])[2]");
		}catch(Exception e) {
			GlobalMethods.printDebugInfo("WithdrawBasePage: Failed to find the confirm button.");
			return false;
		}

		this.moveElementToVisible(confirmBtn);

		js.executeScript("arguments[0].click()",confirmBtn);
		//submit.click();
		this.waitLoadingForCustomise(120);;
		//WebElement response = this.findVisibleElemntByXpath("//div[contains(text(),'withdrawal request')]");
		//Update the xpath element as AT brand unable to detect
		WebElement response = this.findVisibleElemntByXpath("//*[contains(text(),'withdrawal request was successful')]");
		String response_info = response.getText();
		if(response_info != null) {
			GlobalMethods.printDebugInfo("WithdrawBasePage: Response info: " + response_info.trim());
			if(response_info.toLowerCase().contains("successful")) {
				//withdraw request is succeed. return to home page
				this.findClickableElementByXpath("//*[@data-testid='bkToHm']").click();
				this.waitLoading();
				return true;
			}
		}

		System.out.println("WithdrawBasePage: ERROR: withdraw requestion is failed");
		return false;
	}

	public void waitSpinnerLoading() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("el_loading-spinner")));
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("el_loading-spinner")));
		} catch (Exception e) {
			System.out.println(" Element located by By.className el_loading-spinner didn't show");
		}
	}


	public void cancelWithdrawalOTPWindow() {
		try {
			waitLoading();
			WebElement withdrawalAuthWindow = driver.findElement(By.xpath("//*[contains(text(),'Please enable Security Authenticator APP before proceed.')]"));
			boolean showsWindow = withdrawalAuthWindow.isDisplayed();
			if (showsWindow) {
				this.findVisibleElemntByXpath("//div[@class='footer_body']//*[contains(text(),'Cancel')]").click();
				GlobalMethods.printDebugInfo("Closed Withdrawal Security Authenticator window.");
			}
		} catch (Exception e) {
			GlobalMethods.printDebugInfo("Withdrawal Security Authenticator no shows.");
		}
	}



    public void creditOK(){

        try {
            WebElement confirmBtn = findClickableElementByXpath("//div[@class='el-dialog__footer']//button[.//span[normalize-space(text())='OK']]");
            confirmBtn.click();
        } catch (Exception  e) {
            GlobalMethods.printDebugInfo("no credit in withdraw page.");
        }
    }

    public String getVerifyMsgEnable2FA(){
        try {
            this.waitLoading();
            WebElement webElement = findVisibleElemntByXpath("//div[@id='ConfirmationComponent']//div[@class='customer_body']");

            return webElement.getText();
        } catch (Exception e) {
            GlobalMethods.printDebugInfo("get verification message failed when enable withdraw 2FA...");
            return null;
        }
    }


    public void goToSecurityMageTab(){
        try {
            this.waitLoading();
            WebElement securityTab = findVisibleElemntByXpath("//div[@id='tab-/profile/securityManagement']");
            securityTab.click();
        } catch (Exception e) {
            GlobalMethods.printDebugInfo("no security management tab");
        }
    }

    public void clickPwdModifyConfirmBtn(){
        try {
            this.waitLoading();
            WebElement clickBtn = findClickableElementByXpath("//button[@data-testid='button']//span[contains(translate(text(),'Confirm','CONFIRM'),'CONFIRM')]");
            clickBtn.click();
        } catch (Exception e) {
            GlobalMethods.printDebugInfo("no confirm change password dialog");
        }

    }

    public void clickVerify2faConfirmBtn(){
        try {
            this.waitLoading();
            WebElement clickBtn = findClickableElementByXpath("//button[@data-testid='button']//span[contains(translate(text(),'Confirm','CONFIRM'),'CONFIRM')]");
            clickBtn.click();
        } catch (Exception e) {
            GlobalMethods.printDebugInfo("no confirm 2fa dialog");
        }

    }

    public void clickConfirmBtn(int index){
        WebElement clickBtn = findClickableElementByXpath("(*//button[@data-testid='changePw'])["+ index +"]");
        clickBtn.click();
    }

    public void closePwdUpdateDialog(){
        try {
            waitLoading();
            WebElement securityTab = findVisibleElemntByXpath("(//div[@class='ht-dialog__close']//*[local-name()='svg'])[1]");
            securityTab.click();
        } catch (Exception e) {
            GlobalMethods.printDebugInfo("No dialog");
        }

    }

    public void inputPass(String pwdId,String pwd){
        WebElement webElement = findVisibleElemntByXpath("//div[@prop='" + pwdId + "']//input");
        webElement.clear();
        webElement.sendKeys(pwd);
    }

    public void inputVerifyCode(String code){
        return;
    }

    public void inputModifyPass(String pwdId,String pwd){
        WebElement webElement = findVisibleElemntByXpath("//div[@prop='" + pwdId + "']//input");
        webElement.clear();
        webElement.sendKeys(pwd);
    }

    public void clickSendCode(){
        WebElement webElement = findVisibleElemntByXpath("//div[@prop='code']//span");
        webElement.click();
    }

    public String getLimitMsg(){
        try {
            this.waitLoading();
            WebElement titleElement = driver.findElement(By.xpath("//div[@role='dialog']//p[contains(text(),'Due to')]"));

            return titleElement.getText();
        } catch (Exception e) {
            GlobalMethods.printDebugInfo("get limit message failed...");
            return null;
        }
    }

    public void clickIUnderstandBtn(){
        try {
            this.waitLoading();
            WebElement webElement = findVisibleElemntByXpath("//button//span[contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'understand')]");
            webElement.click();
        } catch (Exception e) {
            GlobalMethods.printDebugInfo("click understand button failed");
        }
    }

    public void clickPwdModifyBtn(){
        try {
            this.waitLoading();
            WebElement clickBtn = findClickableElementByXpath("(//button[@data-testid='modify'])[2]");
            clickBtn.click();
        } catch (Exception e) {
            GlobalMethods.printDebugInfo("modify password");
        }

    }

    public void clickEmailModifyBtn(){
        try {
            this.waitLoading();
            WebElement clickBtn = findClickableElementByXpath("(//button[@data-testid='modify'])[1]");
            clickBtn.click();
        } catch (Exception e) {
            GlobalMethods.printDebugInfo("modify email");
        }

    }

    public void clickPhoneModifyBtn(){
        try {
            this.waitLoading();
            WebElement clickBtn = findClickableElementByXpath("(//button[span[normalize-space(text())='Modify']])[2]");
            clickBtn.click();
        } catch (Exception e) {
            GlobalMethods.printDebugInfo("click phone modify button failed!");
        }

    }

    public void click2faModifyBtn(){
        try {
            this.waitLoading();
            WebElement clickBtn = findClickableElementByXpath("(//button[@data-testid='modify'])[3]");
            clickBtn.click();
        } catch (Exception e) {
            GlobalMethods.printDebugInfo("click security authenticator App modify button failed!");
        }

    }

    public boolean click2faEnableBtn(){
        try {
            this.waitLoading();
            List<WebElement> element = driver.findElements(By.xpath("//button[@data-testid='enable']"));
            if(element.isEmpty()){
                return false;
            }
            element.get(0).click();
            return true;
        } catch (Exception e) {
            GlobalMethods.printDebugInfo("click 2fa enable button failed!");
            return false;
        }

    }

    public void inputAuthCode(String code){
        WebElement webElement = findVisibleElemntByXpath("//div[@class='main_verification']//input");
        webElement.clear();
        webElement.sendKeys(code);
        clickConfirmBtn(2);
        waitLoading();
    }



    public void clickConfirmDisclaimer(){
        try {
            WebElement clickBtn = findClickableElementByXpath("//div[@data-testid='tncConfirm']");
            clickBtn.click();
        } catch (Exception e) {
            GlobalMethods.printDebugInfo("There isn't confirm disclaimer");
        }
    }
    public void clickSubmitAuth(){
        WebElement clickBtn = findClickableElementByXpath("//button[@data-testid='submitAuth']");
        clickBtn.click();
    }
    public void clickEditAuthConfirm(){
        try {
            WebElement clickBtn = findClickableElementByXpath("//button[span[normalize-space(text())='Confirm']]");
            clickBtn.click();
        } catch (Exception e) {
            GlobalMethods.printDebugInfo("no confirm modify edit auth dialog");
        }

    }


	/**
	 * Get withdrawal limit amount
	 * @return
	 */
	public int getWithdrawalLimitAmount(){
        WebElement titleElement = null;
        try {
            titleElement = findVisibleElemntByXpath("//*[@id=\"withdraw\"]//div[contains(@class,'kyc-verify')]");
        } catch (Exception e) {
            throw new NoSuchElementException("No withdrawal limit amount display,please check!");
        }

		ScreenshotHelper.highlightElement(driver, titleElement);
		String amount=titleElement.getText();
		// 定义正则表达式匹配数字
		Pattern pattern = Pattern.compile("\\d+");
		Matcher matcher = pattern.matcher(amount);
		int number = 0;
		// 查找匹配的数字
		if (matcher.find()) {
			String numberStr = matcher.group();
			number = Integer.parseInt(numberStr);
			System.out.println("提取到的数值: " + number);
		} else {
			System.out.println("未找到数值");
		}
		return number;
	}
	/**
	 * 获取汇率信息
	 * @return 汇率数值，如果无法获取或解析则返回-1
	 */
	public double getChangeRate() {
		try {
			WebElement changeRateElement = findVisibleElemntByXpath("//*[@id=\"withdrawalPayment\"]//div[contains(@class,'exchange-rate')]");
			ScreenshotHelper.highlightElement(driver, changeRateElement);
			String rateText = changeRateElement.getText();

			// 检查文本是否包含冒号分隔符
			if (rateText.contains(":")) {
				String[] parts = rateText.split(":");
				// 确保分割后有足够的部分
				if (parts.length > 1) {
					String rateValue = parts[1].trim();
					// 提取数字部分（包括小数点）
					Pattern pattern = Pattern.compile("[0-9]+(\\.[0-9]+)?");
					Matcher matcher = pattern.matcher(rateValue);
					if (matcher.find()) {
						return Double.parseDouble(matcher.group());
					}
				}
			}
		} catch (Exception e) {
			GlobalMethods.printDebugInfo("获取汇率信息失败: " + e.getMessage());
		}

		// 如果无法获取或解析汇率，返回-1作为错误标识
		return -1;
	}
	// 获取提现详情
	public String getWithdrawalDetails(){
		WebElement withdrawalDetailsElement = findVisibleElemntByXpath("//*[@id=\"result_wrapper\"]//div[contains(@class,'detailBlock')] | //*[@class=\"withdraw-result\"] ");
		ScreenshotHelper.highlightElement(driver, withdrawalDetailsElement);
		ScreenshotHelper.takeScreenshot(driver, withdrawalDetailsElement, "screenshots", "LBT_Withdrawal_Details");
		return withdrawalDetailsElement.getText();
	}
}

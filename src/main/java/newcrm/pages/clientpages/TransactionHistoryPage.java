package newcrm.pages.clientpages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.utils.StringUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import newcrm.pages.Page;
import tools.ScreenshotHelper;
import utils.LogUtils;

public class TransactionHistoryPage extends Page {
	public TransactionHistoryPage(WebDriver driver) {
		super(driver);
	}

	public class History {
		protected String account;
		protected String currency;
		protected String amount;
		protected String method;
		protected String status;
		protected String deposit_date;

		public String getAccount() {
			return this.account;
		}
		public String getStatus() {
			return this.status;
		}

		public History(String deposit_date, String account, String currency, String method, String amount,
				String status) {
			this.account = account;
			this.currency = currency;
			this.amount = amount;
			this.method = method;
			this.status = status;
			this.deposit_date = deposit_date;
		}

		public boolean checkHistory(String v_date, String v_account, String v_method, String v_amount,
				String v_status) {
			List<String> sucStatus = Arrays.asList("SUCCESSFUL","PAID","ACTIVE","SUBMITTED");
			List<String> statusSet = Arrays.asList("SUCCESSFUL","SUBMITTED","PROCESSING");
			// Do not use date at moment
			if (account.equalsIgnoreCase(v_account.trim()) && StringUtil.containsIgnoreCase(v_method.trim(),method)) {

				//Only compare the status for successful transaction
				if(sucStatus.contains(v_status)) {
					if (Double.valueOf(amount).equals(Double.valueOf(v_amount)) && statusSet.contains(status.toUpperCase())) {
						cancelWithdraw();
						return true;
					}
				}else {
					if (Double.valueOf(amount).equals(Double.valueOf(v_amount) )) {
						return true;
					}
				}
				
			}

			return false;
		}

		public void printHistory() {
			System.out.println("****************************");
			System.out.printf("%-20s : %s\n", "Date", deposit_date);
			System.out.printf("%-20s : %s\n", "account", account);
			System.out.printf("%-20s : %s\n", "currency", currency);
			System.out.printf("%-20s : %s\n", "amount", amount);
			System.out.printf("%-20s : %s\n", "method", method);
			System.out.printf("%-20s : %s\n", "status", status);
		}
	}
	
	public class TransferHistory extends History{

		private String toAccount;
		
		public TransferHistory(String deposit_date, String account, String currency, String method, String amount,
				String status) {
			super(deposit_date, account, currency, method, amount, status);
			// TODO Auto-generated constructor stub
		}
		
		public TransferHistory(String deposit_date, String account, String currency, String method, String amount,
				String status,String toAccount) {
			super(deposit_date, account, currency, method, amount, status);
			this.toAccount = toAccount;
			this.status = status;
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public void printHistory() {
			System.out.println("****************************");
			System.out.printf("%-20s : %s\n", "Date", deposit_date);
			System.out.printf("%-20s : %s\n", "from account", account);
			System.out.printf("%-20s : %s\n", "currency", currency);
			System.out.printf("%-20s : %s\n", "amount", amount);
			System.out.printf("%-20s : %s\n", "to account", toAccount);
			System.out.printf("%-20s : %s\n", "status", status);
		}
		
		@Override
		public boolean checkHistory(String v_date, String v_account, String v_toAccount, String v_amount,
				String v_status) {
			// Do not use date at moment
			if (account.equalsIgnoreCase(v_account.trim()) && toAccount.equalsIgnoreCase(v_toAccount.trim())) {
				GlobalMethods.printDebugInfo("The first row of amount:" + amount + " and the status：" + status);
				int count = 0;
				while(!v_status.equalsIgnoreCase(status) && count <= 3)
				{
					refresh();
					getFirstRowTransfer();
					status = getStatus();
					GlobalMethods.printDebugInfo("getStatus: " + status);
					waitLoading();
					count++;
				}
				if (Double.valueOf(amount).equals(Double.valueOf(v_amount)) && v_status.equalsIgnoreCase(status)) {
					return true;
				}
			}
			return false;
		}
        public boolean checkMT2Wallethistory(String v_account, String v_toAccount, String v_amount,
                                             String v_status){
            if (account.equalsIgnoreCase(v_account.trim()) && toAccount.equalsIgnoreCase(v_toAccount.trim())) {
                GlobalMethods.printDebugInfo("The first row of amount:" + amount + " and the status：" + status);
                int count = 0;
                while(!v_status.equalsIgnoreCase(status) && count <= 3)
                {
                    refresh();
                    getFirstRowTransferNew();
//                    getFirstRowTransfer();
                    status = getStatus();
                    GlobalMethods.printDebugInfo("getStatus: " + status);
                    waitLoading();
                    count++;
                }
                if (Double.valueOf(amount).equals(Double.valueOf(v_amount)) && v_status.equalsIgnoreCase(status)) {
                    return true;
                }
            }
            return false;
        }
	}
	protected List<WebElement> getFirst25DpElements() {
		WebElement d_tab = this.findClickableElemntByTestId("deposit");
		this.moveElementToVisible(d_tab);
		d_tab.click();
        List<WebElement> dpElements;
        try {
            dpElements = driver.findElements(By
                    .xpath("//div[@id='transactionHistory']//table/tbody/tr"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return dpElements;
	}

	protected List<WebElement> getFirst25WithdrawElements() {
		WebElement w_tab = this.findClickableElemntByTestId("withdraw");
		this.moveElementToVisible(w_tab);
		w_tab.click();
		this.findVisibleElemntByXpath(
				"(//div[@id='transactionHistory']/div[1]/div/ul[2]/li[contains(@class,'active')]//table)[2]/tbody");
		return driver.findElements(
				By.xpath("(//div[@id='transactionHistory']/div[1]/div/ul[2]/li[contains(@class,'active')]//table)[2]/tbody/tr"));
	}
	
	protected List<WebElement> getFirst25TransferElements(){
		WebElement w_tab = this.findClickableElemntByTestId("transfer");
		this.moveElementToVisible(w_tab);
		w_tab.click();
		this.findVisibleElemntByXpath(
				"(//div[@id='transactionHistory']/div[1]/div/ul[2]/li[contains(@class,'active')]//table)[2]/tbody");
		return driver.findElements(
				By.xpath("(//div[@id='transactionHistory']/div[1]/div/ul[2]/li[contains(@class,'active')]//table)[2]/tbody/tr"));
	}

	protected List<WebElement> getFirstRowTransferElements(){
		WebElement w_tab = this.findClickableElemntByTestId("transfer");
		this.moveElementToVisible(w_tab);
		w_tab.click();
		this.findVisibleElemntByXpath(
				"(//div[@id='transactionHistory']/div[1]/div/ul[2]/li[contains(@class,'active')]//table)[2]/tbody");
		return driver.findElements(
				By.xpath("(//div[@id='transactionHistory']/div[1]/div/ul[2]/li[contains(@class,'active')]//table)[2]/tbody/tr[1]"));
	}

    protected List<WebElement> getFirstRowTransferElementsById(){
        WebElement w_tab = findClickableElementByXpath("*//div[@id='tab-transfer']");
        this.moveElementToVisible(w_tab);
        w_tab.click();

        return driver.findElements(By.xpath("//div[@class='transfer-history']//table[contains(@class,'el-table__body')]//tbody/tr[1]"));
    }

	protected String getAmount(String value) {
		
		value = value.replace(",", "");
		String regEx = "(([1-9][0-9]*)+(.[0-9]{1,4})?$)";
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(value);
		String amount = "Nothing";
		if (matcher.find()) {
			amount = matcher.group(1);
		}
		return amount;
	}

	protected List<History> getHistory(List<WebElement> els) {
		String date;
		String t_account;
		String account = "";
		String currency = "";
		String method;
		String t_amount;
		String amount;
		String status;

		List<History> result = new ArrayList<>();
		for (WebElement element : els) {
			try {
				String info = element.getText();
				LogUtils.info("TransactionHistoryPage: " + info);
				String[] values = info.split("\n");

				if (GlobalProperties.isWeb) {
					date = values.length > 0 ? values[0].trim() : "";
					t_account = values.length > 1 ? values[1].trim() : "";

					// 安全地解析账户和货币信息
					if (t_account.contains("(") && t_account.contains(")")) {
						account = t_account.substring(0, t_account.indexOf("(")).trim();
						currency = t_account.substring(t_account.indexOf("(") + 1, t_account.lastIndexOf(")")).trim();
					} else {
						account = t_account;
						currency = "";
					}

					method = values.length > 2 ? values[2].trim() : "";
					t_amount = values.length > 3 ? values[3].trim() : "";
					amount = getAmount(t_amount);
					ScreenshotHelper.takeScreenshot(driver, element, "screenshots", "TransactionHistoryPage_" + ".png");
					status = values.length > 4 ? values[4].trim() : "";
				} else {
					// 移动端逻辑保持不变，但增加边界检查
					date = values.length > 3 ? values[3].split(" ")[0] : "";
					t_account = values.length > 0 ? values[0] : "";

					if (t_account.contains("(") && t_account.contains(")")) {
						account = values.length > 0 ? values[0].split(" ")[0] : "";
						currency = t_account.substring(t_account.indexOf("(") + 1, t_account.lastIndexOf(")")).trim();
					} else {
						account = values.length > 0 ? values[0].split(" ")[0] : "";
						currency = "";
					}

					method = values.length > 3 ? values[3].split(" ")[1].trim() : "";
					t_amount = values.length > 2 ? values[2].trim() : "";
					amount = getAmount(t_amount);
					status = values.length > 1 ? values[1].trim() : "";
				}

				result.add(new History(date, account, currency, method, amount, status));
			} catch (Exception e) {
				LogUtils.error("Error parsing transaction history: ",e);
				// 添加默认值历史记录以避免完全失败
				result.add(new History("", "", "", "", "0.0", "ERROR"));
			}
		}
		return result;
	}
	public List<History> getFirst25Withdraw() {
		
//		driver.navigate().refresh();
//		driver.navigate().refresh();
		this.waitLoading();
		List<WebElement> elements = getFirst25WithdrawElements();

		return getHistory(elements);

	}

	public List<History> getFirst25Deposit() {
		//driver.navigate().refresh();
		//driver.navigate().refresh();
		this.waitLoading();
		List<WebElement> elements = getFirst25DpElements();
		if (elements == null || elements.isEmpty()) {
			LogUtils.info("No transaction history found.");
//			return new ArrayList<>();
		}
		for (WebElement element : elements){
			LogUtils.info("TransactionHistoryPage: " + element.getText());
		}
		return getHistory(elements);
	}
	
	public List<TransferHistory> getFirst25Transfer() {
		driver.navigate().refresh();
		driver.navigate().refresh();
		this.waitLoading();
		List<WebElement> elements = this.getFirst25TransferElements();
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

	public List<TransferHistory> getFirstRowTransfer() {
		driver.navigate().refresh();
		this.waitLoading();
		driver.navigate().refresh();
		this.waitLoading();
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

    public List<TransferHistory> getFirstRowTransferNew() {
//        driver.navigate().refresh();
//        this.waitLoading();
//        driver.navigate().refresh();
        this.waitLoading();
        List<WebElement> elements = this.getFirstRowTransferElementsById();
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

	public void cancelWithdraw()
	{
		//Cancel withdraw in CP
		try {
			WebElement cancelBtn = driver.findElement(By.xpath("//button[./span[contains(text(),'CANCEL')]]"));
			if (GlobalProperties.env.equalsIgnoreCase("PROD") && cancelBtn.isDisplayed()){
				cancelBtn.click();
				this.waitLoading();
			}

			WebElement yesBtn = driver.findElement(By.xpath("//button[./span[contains(text(),'Yes')]]"));
			if (GlobalProperties.env.equalsIgnoreCase("PROD") && yesBtn.isDisplayed()){
				yesBtn.click();
				this.waitLoading();
			}

			GlobalMethods.printDebugInfo("Cancelled withdraw in CP");
		}
		catch (Exception e)
		{
			GlobalMethods.printDebugInfo("No withdrawal Cancel button found");
		}
	}
}

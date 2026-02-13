package newcrm.business.businessbase;

import static org.testng.Assert.assertTrue;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import newcrm.utils.api.HyTechUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.http.util.LangUtils;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import newcrm.global.GlobalProperties.DEPOSITMETHOD;
import newcrm.global.GlobalProperties.STATUS;
import newcrm.pages.clientpages.TransactionHistoryPage;
import newcrm.utils.http.CRMHttpClient;
import newcrm.utils.api.HyTechUrl;
import utils.LogUtils;
import vantagecrm.Utils;

public class CPTransactionHistory {
	protected CRMHttpClient httpClient = new CRMHttpClient();
	protected HashMap<String,String> header = new HashMap<>();
	protected String url;
	protected WebDriver driver;
	protected TransactionHistoryPage historypage;
	
	public CPTransactionHistory(TransactionHistoryPage v_history) {
		historypage = v_history;
	}
	
	public CPTransactionHistory(WebDriver driver) {
		this.historypage = new TransactionHistoryPage(driver);
		this.driver = driver;
	}
	
	public void printDepositistory() {
		List<TransactionHistoryPage.History> all_history =  historypage.getFirst25Deposit();
		for(TransactionHistoryPage.History history: all_history) {
			history.printHistory();
		}
	}
	
	public boolean checkDeposit(String account, DEPOSITMETHOD method,String amount, STATUS status) {
		DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String today  = sdf.format(new Date());
		LogUtils.info(String.format("Date: %s, Account: %s, Method: %s, Amount: %s, Status: %s",
				today, account, method.getWebName(), amount, status.toString()));
		for (int attempt = 0; attempt < 3; attempt++) {
			List<TransactionHistoryPage.History> all_history =  historypage.getFirst25Deposit();
			LogUtils.info("************打印所有入金记录************");
			System.out.println(all_history);
			LogUtils.info("************打印所有入金记录************");

			for(TransactionHistoryPage.History history: all_history) {
				LogUtils.info("************查找特定的入金记录************");
				history.printHistory();
				if(history.getAccount().equals(account)) {

					if(history.checkHistory(today, account, method.getWebName(), amount, status.toString())) {
						System.out.println("************Find specific deposit************");
						LogUtils.info("************Find specific deposit************");
						history.printHistory();
						return true;
					}
				}
			}

			if (attempt < 2) {
				// Delay in transaction status update, retry to get transaction status again
				System.out.println("***Retry fetching transaction update status***");
				driver.navigate().refresh();
			}
		}

		System.out.println("************Have not Found the deposit history**************");
		System.out.printf("%-20s : %s\n", "Date",today);
		System.out.printf("%-20s : %s\n", "Account",account);
		System.out.printf("%-20s : %s\n", "Method",method.getWebName());
		System.out.printf("%-20s : %s\n", "Amount",amount);
		System.out.printf("%-20s : %s\n", "Status",status);
		return false;
	}
	
	public boolean checkWithdraw(String account, DEPOSITMETHOD method,String amount, STATUS status) {
		DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String today  = sdf.format(new Date());

		for (int attempt = 0; attempt < 3; attempt++) {
			List<TransactionHistoryPage.History> all_history =  historypage.getFirst25Withdraw();

			for(TransactionHistoryPage.History history: all_history) {
				if(history.getAccount().equals(account)) {
					if(history.checkHistory(today, account, method.getWithdrawHistoryName(), amount, status.toString())) {
						System.out.println("************Find specific withdraw************");
						history.printHistory();
						return true;
					}
				}
			}

			if (attempt < 2) {
				// Delay in transaction status update, retry to get transaction status again
				System.out.println("***Retry fetching transaction update status***");
				driver.navigate().refresh();
			}

		}

		System.out.println("************Have not Found the withdraw history**************");
		System.out.printf("%-20s : %s\n", "Date",today);
		System.out.printf("%-20s : %s\n", "Account",account);
		System.out.printf("%-20s : %s\n", "Method",method);
		System.out.printf("%-20s : %s\n", "Amount",amount);
		System.out.printf("%-20s : %s\n", "Status",status);
		return false;
	}
	
	public boolean checkWithdrawAndCancelOrder(String account, DEPOSITMETHOD method,String amount, STATUS status) {
		DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String today  = sdf.format(new Date());

		for (int attempt = 0; attempt < 3; attempt++) {
			List<TransactionHistoryPage.History> all_history =  historypage.getFirst25Withdraw();

			for(TransactionHistoryPage.History history: all_history) {
				if(history.getAccount().equals(account)) {
					if(history.checkHistory(today, account, method.getWithdrawHistoryName(), amount, status.toString())) {
						System.out.println("************Find specific withdraw************");
						history.printHistory();
						this.cancelSubmittedWithdrawals(this.driver);
						return true;
					}
				}
			}

			if (attempt < 2) {
				// Delay in transaction status update, retry to get transaction status again
				System.out.println("Retry fetching the transaction update status");
				driver.navigate().refresh();
			}
		}

		System.out.println("************Have not Found the withdraw history**************");
		System.out.printf("%-20s : %s\n", "Date",today);
		System.out.printf("%-20s : %s\n", "Account",account);
		System.out.printf("%-20s : %s\n", "Method",method);
		System.out.printf("%-20s : %s\n", "Amount",amount);
		System.out.printf("%-20s : %s\n", "Status",status);
		this.cancelSubmittedWithdrawals(this.driver);
		return false;
	}
	
	public boolean checkTransfer(String account, String to_account,String amount, STATUS status) {
		List<TransactionHistoryPage.TransferHistory> all_history =  historypage.getFirstRowTransfer();
		DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String today  = sdf.format(new Date());
		for(TransactionHistoryPage.TransferHistory history: all_history) {
			if(history.getAccount().equals(account)) {
				if(history.checkHistory(today, account, to_account, amount, status.toString())) {
					System.out.println("************Find specific Transfer************");
					history.printHistory();
					return true;
				}
			}
		}
		System.out.println("************Have not Found the transfer history**************");
		System.out.printf("%-20s : %s\n", "Date",today);
		System.out.printf("%-20s : %s\n", "From account",account);
		System.out.printf("%-20s : %s\n", "To account",to_account);
		System.out.printf("%-20s : %s\n", "Amount",amount);
		System.out.printf("%-20s : %s\n", "Status",status);
		return false;
	}

    public boolean checkTransferMT2Wallet(String account, String to_account,String amount, STATUS status) {
        List<TransactionHistoryPage.TransferHistory> all_history =  historypage.getFirstRowTransferNew();
        DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String today  = sdf.format(new Date());
        for(TransactionHistoryPage.TransferHistory history: all_history) {
            if(history.getAccount().equals(account)) {
                if(history.checkMT2Wallethistory(account, to_account, amount, status.toString())) {
                    System.out.println("************Find specific Transfer************");
                    history.printHistory();
                    return true;
                }
            }
        }
        System.out.println("************Have not Found the transfer history**************");
        System.out.printf("%-20s : %s\n", "Date",today);
        System.out.printf("%-20s : %s\n", "From account",account);
        System.out.printf("%-20s : %s\n", "To account",to_account);
        System.out.printf("%-20s : %s\n", "Amount",amount);
        System.out.printf("%-20s : %s\n", "Status",status);
        return false;
    }

    public void cancelSubmittedWithdrawals(WebDriver driver){
		for(Cookie coo : driver.manage().getCookies()) {
			if (coo.toString().contains("JsId")) {
				header.put("Cookie", coo.toString());
			}
		}
		this.url = Utils.ParseInputURL(driver.getCurrentUrl());
		String fullPath = this.url + HyTechUrl.WD_TRANS_HISTORY;

		header.put("Content-Type", "application/x-www-form-urlencoded");
		HyTechUtils.genXSourceHeader(header);

		// Login Token required in each API request
		String xToken = "", cf = "", headerCookie = "";
		Cookie cookieToken = driver.manage().getCookieNamed("xtoken");
		Cookie cookieCF = driver.manage().getCookieNamed("__cf_bm");

		if (cookieCF != null) {
			String cookieDomain = cookieCF.getDomain();
			cf = cookieCF.getValue();
			headerCookie += "__cf_bm=" + cf + ";";
		}

		if (cookieToken != null) {
			xToken = cookieToken.getValue();
			headerCookie += "xtoken=" + xToken + ";";
		}

		if (!headerCookie.isEmpty()) {
			header.put("Cookie", headerCookie);
		} else {
			System.out.println("Cookie not found.");
		}

		String body = "pageNo=1&pageSize=10";

		try {
			HttpResponse response = httpClient.getPostResponse(fullPath, header, body);
			JSONObject message = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			Integer rescode = message.getInteger("code"); 
			
			if(rescode.equals(0)) {
				JSONArray history =  message.getJSONObject("data").getJSONArray("withdrawHistory");
				for (Object item : history) {  
					String id = ((JSONObject) item).getString("id");
					String status = ((JSONObject) item).getString("status");
					Boolean processedByOp = ((JSONObject) item).getBoolean("processedByOp");
					if (status.equals("5") && processedByOp==null)//Submitted and not be claimed
					{
						//Send cancel withdrawal API by id
						sendWithdrawCancel(id);
					}
				}
			}else {
				System.out.println("!!!Query withdraw history failed or no withdrawal history.");
			}
		}catch(Exception e) {
			System.out.println("!!!Query withdraw history failed.");
			e.printStackTrace();
		}
	}
	

    /* Cancel withdrawal by or_id.
     * @parameter or_id - withdrawal order id
     * */
    private void sendWithdrawCancel(String or_id) {
		/*String path = "web-api/api/withdrawal/cancelWithdrawalOrder?withdrawalId="+or_id;*/
		String path = "hgw/rw/payment-api/cp/api/withdrawal/cancelWithdrawalOrder?withdrawalId="+or_id;
		header.put("Accept", "application/json, text/plain, */*");
		HyTechUtils.genXSourceHeader(header);

		try {		
			JSONObject result = sendCPAPIrequest(path, "");
			Integer rescode = result.getInteger("code"); 
			JSONObject data = result.getJSONObject("data");
			System.out.println("Going to cancel withdrawal: "+data.getString("id"));
			
			//Status 11 = cancelled
			assertTrue(rescode.equals(0) && data.getString("id").equals(or_id) && 
					data.getString("status").equals("11"), "Withdraw cancellation failed!\n" + result);

		} catch (Exception e) {
			System.out.println("Withdraw cancellation failed! ID: "+or_id);
		}
    }

	/**
	 * Send CP api request
	 * @param path url path(should not start with /), String request body
	 * @return json response
	 */
    public JSONObject sendCPAPIrequest(String path, String body) {
		String fullPath = this.url + path;
		try {
			HttpResponse response = httpClient.getPostResponse(fullPath, header,body);
			JSONObject message = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			return message;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}

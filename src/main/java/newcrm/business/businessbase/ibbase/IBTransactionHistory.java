package newcrm.business.businessbase.ibbase;

import static org.testng.Assert.assertTrue;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.time.LocalDate;

import newcrm.business.businessbase.CPMenu;
import newcrm.utils.StringUtil;
import newcrm.utils.api.HyTechUrl;
import newcrm.utils.api.HyTechUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties.DEPOSITMETHOD;
import newcrm.global.GlobalProperties.STATUS;
import newcrm.pages.ibpages.IBTransactionHistoryPage;
import newcrm.pages.ibpages.IBTransactionHistoryPage.History;
import newcrm.pages.ibpages.RebateWithdrawBasePage.Account;
import newcrm.utils.http.CRMHttpClient;
import vantagecrm.Utils;

public class IBTransactionHistory {
	protected CRMHttpClient httpClient = new CRMHttpClient();
	protected HashMap<String,String> header = new HashMap<>();
	protected String url;
	protected String rebatAcc = "";
	protected IBTransactionHistoryPage page;
	protected WebDriver driver;

	public IBTransactionHistory(IBTransactionHistoryPage ibtranshistory) {
		this.page = ibtranshistory;
	}

	public IBTransactionHistory(WebDriver driver) {
		this.page = new IBTransactionHistoryPage(driver);
		this.driver = driver;
	}
	
	//Cancel all submitted withdrawals after checking history
	public boolean compareWithdraw(Double amount, DEPOSITMETHOD method, CPMenu menu) {

		DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String today  = sdf.format(new Date());

		for (int attempt = 0; attempt < 3; attempt++) {
			History history = page.getLatestWithdrawHistory();

			if(amount.equals(history.amount) && StringUtil.containsIgnoreCase(method.getIBWithdrawName().trim(), history.method)
					&& ((STATUS.SUBMITTED.equals(history.status))|| (STATUS.PROCESSED.equals(history.status))||(STATUS.PROCESSING.equals(history.status)))) {
				GlobalMethods.printDebugInfo("*******Found the withdraw history***************");
				history.printInfo();
				GlobalMethods.printDebugInfo("***********************************************");
				this.cancelSubmittedWithdrawals(this.driver);
				return true;
			}

			if (attempt < 2) {
				// Delay in transaction status update, retry to get transaction status again
				System.out.println("***Retry fetching transaction update status***");
				driver.navigate().refresh();
				menu.closeIBNotificationDialog();
			}
		}

		GlobalMethods.printDebugInfo("*******Does not found the withdraw history***************");
		System.out.printf("%20s  %s\n","Date:", today);
		System.out.printf("%20s  %s\n","Amount:", amount);
		System.out.printf("%20s  %s\n","Destination:", method);
		System.out.printf("%20s  %s\n","Status:", STATUS.SUBMITTED);
		GlobalMethods.printDebugInfo("***********************************************");
		this.cancelSubmittedWithdrawals(this.driver);
		return false;
	}

	public List<String> getAllRebateAccounts(){
		return page.getAccountInfos();
	}
	
	public boolean setRebateAccount(String accNum) {
		rebatAcc = accNum.trim();
		return true;
/*		List<String> accs = this.getAllRebateAccounts();
		for(String a: accs) {
			if(a.equalsIgnoreCase(accNum.trim())) {
				return page.setRebateAccount(accNum.trim());
			}
		}
		return false;*/
	}
	
	
	public void cancelSubmittedWithdrawals(WebDriver driver){
		for(Cookie coo : driver.manage().getCookies()) {
			if (coo.toString().contains("JsId")) {
				header.put("Cookie", coo.toString());
			}
		}
		this.url = Utils.ParseInputURL(driver.getCurrentUrl());
		String fullPath = this.url + HyTechUrl.IBWD_REBATE_HISTORY;

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

		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// Get current date
		LocalDate today = LocalDate.now();
		// Get the first day of the current month
		LocalDate firstDayOfMonth = today.withDayOfMonth(1);
		String body = "pageNo=1&limit=10&qUserId=0&qAccount="+rebatAcc+"&qStartTime=" + firstDayOfMonth + "&qEndTime="+today;
		//System.out.println(body);
		try {
			HttpResponse response = httpClient.getPostResponse(fullPath, header, body);
			JSONObject message = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			Integer rescode = message.getInteger("code"); 
			
			if(rescode.equals(0)&& message.getJSONArray("data")!=null) {
				JSONArray history =  message.getJSONArray("data");
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
		String path = "web-api/api/withdrawal/cancelWithdrawalOrder?withdrawalId="+or_id;
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
			// TODO Auto-generated catch block
			e.printStackTrace();
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

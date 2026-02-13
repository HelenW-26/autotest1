package newcrm.testcases.admintestcases;

import com.alibaba.fastjson.JSONObject;
import newcrm.adminapi.AdminAPIUserAccount;
import newcrm.global.GlobalProperties.PLATFORM;
import org.testng.annotations.Test;


public class AdminAccountTestCases {
	protected AdminAPIUserAccount adminAcctAPI;

	// trading account search and update
	@Test
	public void apiAdminTradingAcctPage()  {
		JSONObject tradAcct = adminAcctAPI.apiTradingAcctSearch("", "", PLATFORM.MT5, "", "", "");

		JSONObject firstAcctResult = tradAcct.getJSONArray("rows").getJSONObject(0);
		String pIds = firstAcctResult.getString("p_ids");
		String[] pIdsArray = pIds.split(",");
		String latestPId = pIdsArray[pIdsArray.length - 1];

		adminAcctAPI.apiTradingAcctUpdate(firstAcctResult.getString("mt4_account"), firstAcctResult.getString("group"),
				firstAcctResult.getString("mt4_account_type"), firstAcctResult.getString("owner"), latestPId, firstAcctResult.getString("group"));
	}

	// rebate account search and update
	@Test
	public void apiAdminRebateAcctPage()  {
		JSONObject rebateAcct = adminAcctAPI.apiRebateAcctSearch();

		JSONObject firstAcctResult = rebateAcct.getJSONArray("rows").getJSONObject(0);
		String pIds = firstAcctResult.getString("p_ids");
		String[] pIdsArray = pIds.split(",");
		String latestPId = pIdsArray[pIdsArray.length - 1];

		adminAcctAPI.apiRebateAcctUpdate(firstAcctResult.getString("mt4_account"), firstAcctResult.getString("group"),
				firstAcctResult.getString("mt4_account_type"), firstAcctResult.getString("owner"), latestPId, firstAcctResult.getString("ibGroup"));
	}

	//client, lead, external user search
	@Test
	public void apiUserPage()  {
		adminAcctAPI.apiClientSearch();
		adminAcctAPI.apiLeadWebsiteUserType();
		adminAcctAPI.apiLeadSearch();
		adminAcctAPI.apiIBUserType();
		adminAcctAPI.apiExternalUserSearch();
	}

}

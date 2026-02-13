package newcrm.business.adminbusiness;

import java.util.*;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import newcrm.adminapi.AdminAPI;
import newcrm.business.dbbusiness.AccountDB;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.global.GlobalProperties.BRAND;
import newcrm.global.GlobalProperties.ENV;
import newcrm.global.GlobalProperties.PLATFORM;
import newcrm.global.GlobalProperties.REGULATOR;
import newcrm.utils.AlphaServerEnv;
import newcrm.utils.Bot.TeamsNotifier;
import utils.LogUtils;
import vantagecrm.Utils;

public class AdminAPIBusiness {

	private AdminAPI admin;
	private ENV env;
	private BRAND brand;
	private REGULATOR regulator;
	private String url;
	TeamsNotifier teamsNT = new TeamsNotifier();
	
	public AdminAPIBusiness(String url,REGULATOR regulator,String adminUser,String password,ENV env,BRAND brand) {
		admin = new AdminAPI(url, regulator, adminUser, password, brand, env);
		this.env = env;
		this.brand = brand;
		this.regulator = regulator;
		this.url = url;
	}
	
	/**
	 * 
	 * @param userId 用户user ID
	 * @param platform 平台
	 * @return 成功返回true，失败返回false
	 */
	public Map.Entry<Boolean, String> auditMainAccount(String userId,PLATFORM platform) throws Exception {
		AccountDB db = new AccountDB(env,brand,regulator);

		// Wait for 5s
		Thread.sleep(5000);

		JSONObject account = db.getFirstAccount(userId);

		if(account == null) {
			LogUtils.info("No account found for User ID: " + userId);
			return new AbstractMap.SimpleEntry<>(false, "No account found for User ID: " + userId);
		}

		String accNum = account.getString("mt4_account");
		if(!(accNum==null||"".equals(accNum)||"null".equalsIgnoreCase(accNum))) {
			LogUtils.info("The trading account has already been audited, no audit is needed.");
			return new AbstractMap.SimpleEntry<>(true, "The trading account has already been audited, no audit is needed.");
		}

		String accountType = account.getString("mt4_account_type");
		String idps = account.getString("p_ids");

		String ids[] = idps.split(",");
		String ownerId = ids[ids.length-1];
		Random random = new Random();
		//获取affid/rebate Account
		List<String> rebateAccs  = admin.getRebateAccounts(ownerId);
		String rebateAcc = "";
		if(rebateAccs == null) {
			LogUtils.info("No account owner found for Owner Id: " + ownerId);
			rebateAcc = "";
		}else {
			rebateAcc = rebateAccs.get(random.nextInt(rebateAccs.size()));
		}

		//获取commissionCode
		List<String> commission = admin.getCommissionCode(ownerId, rebateAcc);
		String comm  = "";
		if(commission==null) {
			LogUtils.info("No Commission Code found");
		}else{
			comm = commission.get(random.nextInt(commission.size()));
		}

		// Get available servers
		JSONArray servers = admin.getServers(ownerId, rebateAcc);
		if(servers == null) {
			LogUtils.info("No server found");
			return new AbstractMap.SimpleEntry<>(false, "No server found");
		}

		// Get expected server list
		List<String> expectedServerList = GlobalMethods.getTestServers(platform, env, brand);
		JSONObject server = null;
		ArrayList<JSONObject> filtedServer = new ArrayList<>();

		// Add to filter list when server found in expected server list
		servers.forEach(obj -> {
			// Get server id
			JSONObject s = JSON.parseObject(obj.toString());
			String serverId = s.getString("id");

			if (expectedServerList.stream().anyMatch(m -> m.equalsIgnoreCase(serverId))) {
				filtedServer.add(s);
			}
		});

		// Get first available server if found
		if (!filtedServer.isEmpty()) {
			server = filtedServer.get(0);
			LogUtils.info("Choose Server: " + server.toJSONString());
		} else {
			LogUtils.info(String.format("No matching server found for %s platform. Expected Test Servers ID: ", platform, servers));
			return new AbstractMap.SimpleEntry<>(false, String.format("No matching server found for %s platform. Expected Test Servers ID: ", platform, servers));
		}

		// Get account group by server id
		List<String> groups = admin.getGroups(server.getString("id"), accountType, userId, accountType);
		if(groups == null) {
			LogUtils.info("No account group found for Server ID: " + server.getString("id"));
			return new AbstractMap.SimpleEntry<>(false, "No account group found for Server ID: " + server.getString("id"));
		}

		// Filter test group currency
		String currency = account.getString("apply_currency");
		LogUtils.info("currency:" + currency);
		groups.removeIf(g ->{
			String t_group = g.toLowerCase();
			if(!t_group.contains("test")||!t_group.contains(currency.toLowerCase())||t_group.contains("_re_")) {
				return true;
			}else {
				return false;
			}
		});

		// Check test account group available
		if(groups.isEmpty()) {
			LogUtils.info(String.format("No test account group found for Server ID: %s, Currency: %s", server, currency));
			return new AbstractMap.SimpleEntry<>(false, String.format("No test account group found for Server ID: %s, Currency: %s", server, currency));
		}

		String group = groups.get(random.nextInt(groups.size()));
		LogUtils.info("Random select Account Group: " + group);

		// Audit account
		//acid,user_id,mt4_account,mt4_account_type,apply_currency,real_name,email,phone_num,p_ids
		boolean result = admin.auditAccount(userId, account.getString("acid"),"", ownerId, rebateAcc, account.getString("real_name"),
				server.getString("id"), group, accountType, ((brand.equals(BRAND.VFX) && regulator.equals(REGULATOR.ASIC)) ? "30" : "100"), comm, currency);

		if(result) {
			account = db.getFirstAccount(userId);
			LogUtils.info("Account Audit Success: " + account.toJSONString());
			return new AbstractMap.SimpleEntry<>(true, "Account Audit Success: " + account.toJSONString());
		}else {
			LogUtils.info("Account Audit Failed!");
			return new AbstractMap.SimpleEntry<>(false, "Account Audit Failed!");
		}
	}

	public boolean checkAutoAuditMainAccount(String userId) throws Exception {
		AccountDB db = new AccountDB(env, brand, regulator);

		// Wait for 5s
		Thread.sleep(5000);

		JSONObject account = db.getFirstAccount(userId);

		if(account == null) {
			LogUtils.info("Account not found. User ID: " + userId);
			return false;
		}

		String accNum = account.getString("mt4_account");

		if (accNum == null || accNum.isEmpty() || "null".equalsIgnoreCase(accNum)) {
			LogUtils.info("Account number is empty. User ID: " + userId);
			return false;
		}

		LogUtils.info("Account Auto Audit Success: " + account.toJSONString());
		return true;
	}

	/* by AlexL 05/06/2023
	 * Migrated from legacy RestAPI.java to new AdminAPIBusiness
	 * */
    public void APIUpdateTradingAccountToTestGroup()
	{		
		//Brands that do not require commission id for updating account group
		BRAND[] no_comm_Brands = {BRAND.VT, BRAND.PUG, BRAND.AT};
		Random random = new Random();
		JSONObject objRow;
		String accountNo = "",groupName="",commId="",newGroupName="",currency="",userEmail="";
		String accountName="",ds_id="",actBalance = "0"; 

	
		//e.g.[{"name":"VFX AU","id":"5"},{"name":"VFX UK","id":"2"}]
		JSONArray dataSources = admin.getServers("0","");	
		
		//testcrm's commission code
		String ownerId = admin.getPRODTestCRMUserId(brand);

		for (Object ds : dataSources) {  
			ds_id = ((JSONObject) ds).getString("id");
			//Skip for 3 - MXT and 999 - MT and 998 - MT-UK (VFX)
			if (Arrays.asList("3", "999", "998").contains(ds_id)) {
				continue;
			}
			try {			
				System.out.println("Going to search data source: " + ds_id + " - " + ((JSONObject) ds).getString("name"));
				
				//query trading account list under specified datasource id and name contains testcrm
				JSONArray testcrmAccInfo = admin.queryTradingAccListbyName(ds_id, Utils.testcrmPrefix);
				
				for(int j=0; j<testcrmAccInfo.size(); j++)
				{
					objRow=(JSONObject) testcrmAccInfo.get(j);
					
					try {
						groupName = objRow.get("group").toString();
					}catch(NullPointerException e) {
						groupName = "UNKNOWN";
						System.out.println("Group UNKNOW! Will not be replaced by TEST group");
					}
					
					userEmail = objRow.get("email_sub").toString();
					accountName = objRow.get("real_name").toString();
					actBalance = objRow.get("balance").toString();
			    	userEmail = admin.testPostDecryptEmail(userEmail,this.url);
			    	
			    	if (!admin.isTestGroup(groupName.toLowerCase()) && Utils.isTestcrmUser(accountName) && !groupName.equals("UNKNOWN")){
			    		
						System.out.println("\n!!! Found non-test group "+groupName + " , Client Name: " + accountName+ " , Email: " + userEmail);
						//accountNo = objRow.get("login").toString();
						accountNo = objRow.get("mt4_account").toString();
						currency = objRow.get("currency").toString();
						
						//no_comm_Brands contains VT,PUG,AT,etc
						if(!Arrays.asList(no_comm_Brands).contains(this.brand)) {
							//获取commissionCode for testcrm(ownerId)
							List<String> commission = admin.getCommissionCode(ownerId, "");
							if(commission==null) {
								LogUtils.info("Have not found Commission code id.");
							}else{
								commId = commission.get(random.nextInt(commission.size()));								
							}
						}

						
						//Going to find group matches specified currency
						newGroupName = filteringTradingGroups(ds_id, currency);
						
						//Update account group 
						admin.testPostUpdateAccountGroup(url, accountNo, commId, newGroupName, groupName, ownerId);
						if(!actBalance.equalsIgnoreCase("0.0"))								
						{
							System.out.println(accountNo + " + " + groupName + " has balance. Please manually check the group had been changed. Balance is " + actBalance );
							
							//Alert to Teams
							String alert_msg = ((JSONObject) ds).getString("name") + " - " +accountNo+ " " + groupName + " (" + this.regulator +") has balance. Please manually check the group has been changed. Balance is " + actBalance;
							//System.out.printf(alert_msg);
							teamsNT.SendNotificationToTeams(GlobalProperties.AUTOMATION_UpdateGroups_webhookUrl,alert_msg);
						}
					}
					
				}
				
			} catch (Exception e) {
			    // Handle the exception if any key not exists
				System.out.println("Not able to handle data source: " + ds_id + " - " + ((JSONObject) ds).getString("name")+"\n"+e.getMessage());
				continue; // Continue to the next iteration of the loop
			}
		}	
	} 
    
    
    //Find the first group that matches the currency or else return first usd group
    public String filteringTradingGroups(String ds_id, String currency)
    {
    	String rgroup="";     	   	
		List<String> groups = admin.getGroups(ds_id, "", "", "");
		
		//Remove non-test/re groups(return true)
		groups.removeIf(g ->{
			String t_group = g.toLowerCase();
			if(!t_group.contains("test")||t_group.contains("_re_")) {
				return true;
			}else {
				return false;
			}
		});
		
		//Find the first group that matches the currency or else return first usd group
		rgroup = groups.stream()
		        .filter(group -> group.toLowerCase().contains(currency.toLowerCase()))
		        .findFirst()
		        .orElseGet(() -> groups.stream()
		                .filter(group -> group.toLowerCase().contains("usd"))
		                .findFirst()
		                .orElse(null));
    	
    	return rgroup;
    }
    
    
	/* by AlexL 01/06/2023
	 * Updating rebate group to test group
	 * */
    public void APIUpdateRebateAccountToTestGroup()
	{		
		//Brands that do not require commission id for updating account group
		BRAND[] no_comm_Brands = {BRAND.VT, BRAND.PUG, BRAND.AT};
		Random random = new Random();
		JSONObject objRow;
		String accountNo = "",groupName="",commId="",newGroupName="",currency="",userEmail="";
		String accountName="",ds_id="",actBalance = "0"; 

	
		//e.g.[{"name":"VFX AU","id":"5"},{"name":"VFX UK","id":"2"}]
		JSONArray dataSources = admin.getServers("0","");	
		
		//testcrm's commission code
		String ownerId = admin.getPRODTestCRMUserId(brand);

		for (Object ds : dataSources) {  
			ds_id = ((JSONObject) ds).getString("id");
			//Skip for 3 - MXT and 999 - MT
			if (Arrays.asList("3", "999").contains(ds_id)) {
				continue;
			}
			try {			
				System.out.println("Going to search data source: " + ds_id + " - " + ((JSONObject) ds).getString("name"));
				
				//query trading account list under specified datasource id and name contains testcrm
				JSONArray testcrmAccInfo = admin.queryRebateAccListbyName(ds_id, Utils.testcrmPrefix);
				
				for(int j=0; j<testcrmAccInfo.size(); j++)
				{
					objRow=(JSONObject) testcrmAccInfo.get(j);
					
					try {
						groupName = objRow.get("group").toString();
					}catch(NullPointerException e) {
						groupName = "UNKNOWN";
						System.out.println("Group UNKNOW! Will not be replaced by TEST group");
					}
					
					userEmail = objRow.get("email_sub").toString();
					accountName = objRow.get("real_name").toString();
					actBalance = objRow.get("balance").toString();
			    	userEmail = admin.testPostDecryptEmail(userEmail,this.url);
			    	
			    	if (!admin.isTestGroup(groupName.toLowerCase()) && Utils.isTestcrmUser(accountName) && !groupName.equals("UNKNOWN")){
			    		
						System.out.println("\n!!! Found non-test group "+groupName + " , Client Name: " + accountName+ " , Email: " + userEmail);
						//accountNo = objRow.get("login").toString();
						accountNo = objRow.get("mt4_account").toString();
						currency = objRow.get("currency").toString();
						
						//no_comm_Brands contains VT,PUG,AT,etc
						if(!Arrays.asList(no_comm_Brands).contains(this.brand)) {
							//获取commissionCode for testcrm(ownerId)
							List<String> commission = admin.getCommissionCode(ownerId, "");
							if(commission==null) {
								LogUtils.info("Have not found Commission code id.");
							}else{
								commId = commission.get(random.nextInt(commission.size()));								
							}
						}

						
						//Going to find group matches specified currency
						newGroupName = filteringRebateGroups(ds_id, currency);

						//Update account group 
						admin.testPostUpdateAccountGroup(url, accountNo, commId, newGroupName, groupName, ownerId);
						if(!actBalance.equalsIgnoreCase("0.0"))								
						{
							System.out.println(accountNo + " + " + groupName + " has balance. Please manually check the group had been changed. Balance is " + actBalance );
							
							//Alert to Teams
							String alert_msg = ((JSONObject) ds).getString("name") + " - " +accountNo+ " " + groupName + " (" + this.regulator +") has balance. Please manually check the group has been changed. Balance is " + actBalance;
							//System.out.printf(alert_msg);
							teamsNT.SendNotificationToTeams(GlobalProperties.AUTOMATION_UpdateGroups_webhookUrl,alert_msg);
						}
					}
					
				}
				
			} catch (Exception e) {
			    // Handle the exception if any key not exists
				System.out.println("Not able to handle data source: " + ds_id + " - " + ((JSONObject) ds).getString("name")+"\n"+e.getMessage());
				continue; // Continue to the next iteration of the loop
			}
		}	
	} 
    
    //Find the first group that matches the currency or else return first usd group
    public String filteringRebateGroups(String ds_id, String currency)
    {
    	String rgroup="";     	   	
		List<String> groups = admin.getGroups(ds_id, "", "", "");
		
		//Remove non-test/re groups(return true)
		groups.removeIf(g ->{
			String t_group = g.toLowerCase();
			if(!t_group.contains("test") && !t_group.startsWith("t_")) {
				return true;
			}else {
				return false;
			}
		});
		
		//Find the first group that matches the currency or else return first usd group
		rgroup = groups.stream()
		        .filter(group -> group.toLowerCase().contains(currency.toLowerCase()))
		        .findFirst()
		        .orElseGet(() -> groups.stream()
		                .filter(group -> group.toLowerCase().contains("usd"))
		                .findFirst()
		                .orElse(null));
    	
    	return rgroup;
    }
}

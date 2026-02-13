package newcrm.business.dbbusiness;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.global.GlobalProperties.BRAND;
import newcrm.global.GlobalProperties.ENV;
import newcrm.global.GlobalProperties.REGULATOR;
import newcrm.utils.Bot.TeamsNotifier;
import newcrm.utils.db.DbUtils;
import utils.LogUtils;

public class EmailDB {

	private DbUtils db;
	private final REGULATOR regulator;
	TeamsNotifier teamsNT = new TeamsNotifier();
	
	public EmailDB( ENV env,BRAND brand,REGULATOR regulator) {
		db = new DbUtils(env,brand,regulator);
		this.regulator = regulator;
	}

	public REGULATOR getRegulator() {
		return regulator;
	}
	
	//Get OTP code from tb_mail_send_log in DB
	public JSONObject getCodeRecord(GlobalProperties.ENV env, GlobalProperties.BRAND brand, GlobalProperties.REGULATOR regulator, String clientName){

		String sql = "select * from tb_mail_send_log where vars like '%" + clientName + "%' and vars like '%CODE%' and create_time >= NOW() - INTERVAL 2 DAY ORDER by create_time DESC LIMIT 1";
		GlobalMethods.printDebugInfo("sql: " + sql);
			
		db= new DbUtils(env, brand, regulator);		
		JSONArray array = db.queryRegulatorDB(sql);
		if(array.size()>0) {
			return array.getJSONObject(0);
		}
		return null;
	}
	
	//Search for keyword in tb_mail_send_log
	public void getKeywordFromEmailDB(GlobalProperties.ENV env, GlobalProperties.BRAND brand, GlobalProperties.REGULATOR regulator, String keyword){
		//System.out.println();
		String sql = "select create_time,template_invoke_name,id,vars from tb_mail_send_log where vars like '%" + keyword + "%' and create_time >= '"+funcDateGenerator("GMT+2")+"';";
		GlobalMethods.printDebugInfo("sql: " + sql);
			
		db= new DbUtils(env, brand, regulator);		
		JSONArray array = db.queryRegulatorDB(sql);
		if(array.size()>0) {
			GlobalMethods.printDebugInfo("!!!Record found:");
			//return array;
			for(Object o: array){
				System.out.println(o.toString());
			}
			
			//Alert to Teams
			String alert_msg = "***Record found: " + sql;
			teamsNT.SendNotificationToTeams(GlobalProperties.AUTOMATION_CryptoW_Monitor_webhookUrl,alert_msg);

		}else {
			GlobalMethods.printDebugInfo("No record found.\n");
		}
	}

	public JSONArray getVarFromEmailDB(GlobalProperties.ENV env, GlobalProperties.BRAND brand, GlobalProperties.REGULATOR regulator, String keyword){
		String sql = "select * from tb_mail_send_log where vars like '%" + keyword + "%'";
		GlobalMethods.printDebugInfo("sql: " + sql);

		db= new DbUtils(env, brand, regulator);
		return db.queryRegulatorDB(sql);

	}
	
	
	//Generate time of one hour back from current GMT time
	String funcDateGenerator(String GMT) {
		//one hour back from current time
		Date currentTime = new Date(System.currentTimeMillis() - 3600 * 1000);
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
        //Transform to specific GMT timezone
		dateFormat.setTimeZone(TimeZone.getTimeZone(GMT));
		System.out.println(GMT+ " time: " + dateFormat.format(currentTime));
		return dateFormat.format(currentTime);
	}

	//Return Risk approval/reject link from tb_mail_send_log in DB
	public String getRiskApprovalLink(ENV env, BRAND brand, REGULATOR regulator, String account, String flag){
		LogUtils.info(String.format("Getting risk approval/reject link from email DB...:%s,%s,%s,%s,%s", env, brand, regulator, account, flag));
		String sql = "select vars from tb_mail_send_log where vars like '%" + account + "%' and subject like '%Risk%Withdraw%APPROVAL%' ORDER by create_time DESC LIMIT 1";
		GlobalMethods.printDebugInfo("sql: " + sql);

		db= new DbUtils(env, brand, regulator);
		JSONArray array = db.queryRegulatorDB(sql);
		if(!array.isEmpty()) {
			switch (flag) {
				case "APPROVE_URL":
					return array.getJSONObject(0).getJSONObject("vars").getString("APPROVE_URL").replace("\\u003d", "=");
				case "REJECT_URL":
					return array.getJSONObject(0).getJSONObject("vars").getString("REJECT_URL").replace("\\u003d", "=");
				default:
					GlobalMethods.printDebugInfo("Invalid input for risk audit link flag!");
			}
		}
		return null;
	}

	public String getResetPwdLink(ENV env, BRAND brand, REGULATOR regulator, String email, String flag) {

		String sql = "select vars from tb_mail_send_log where vars like '%" + email + "%' and template_invoke_name like '%Reset_Profile_Password%' and create_time >= NOW() - INTERVAL 2 DAY ORDER by create_time DESC LIMIT 1;";
		GlobalMethods.printDebugInfo("sql: " + sql);

		db= new DbUtils(env, brand, regulator);
		JSONArray array = db.queryRegulatorDB(sql);
		if(!array.isEmpty()) {
            if (flag.equals("CHANGE_LINK")) {
				GlobalMethods.printDebugInfo("Record found in db. Data: " + array.getJSONObject(0).getJSONObject("vars").getString("CHANGE_LINK") + ", \n"+ array.toJSONString());
                return array.getJSONObject(0).getJSONObject("vars").getString("CHANGE_LINK").replace("\\u003d", "=");
            } else {
                GlobalMethods.printDebugInfo("Invalid input for reset password link flag!");
            }
		}
		return null;
	}

}

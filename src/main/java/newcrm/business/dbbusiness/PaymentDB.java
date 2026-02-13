package newcrm.business.dbbusiness;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.global.GlobalProperties.BRAND;
import newcrm.global.GlobalProperties.ENV;
import newcrm.global.GlobalProperties.REGULATOR;
import newcrm.utils.db.DbUtils;
import utils.LogUtils;

import java.util.Objects;

public class PaymentDB {

private DbUtils db;
	
	public PaymentDB( ENV env,BRAND brand,REGULATOR regulator) {
		db = new DbUtils(env,brand,regulator);
	}
	
	
	public JSONObject getLatestDeposit(String account) {
		String sql = "select * from tb_payment_deposit where mt4_account="+account + " order by id desc limit 1;";
		JSONArray result = db.queryRegulatorDB(sql);
		
		if(result!=null && result.size()>0) {
			return result.getJSONObject(0);
		}
		return null;
	}


	//Return withdrawal status from tb_payment_withdraw in DB
	public Integer getWithdrawStatus(GlobalProperties.ENV env, GlobalProperties.BRAND brand, GlobalProperties.REGULATOR regulator, String account){

		String sql = "select status from tb_payment_withdraw where mt4_account= " + account + " ORDER by create_time DESC LIMIT 1";
		GlobalMethods.printDebugInfo("sql: " + sql);

		db= new DbUtils(env, brand, regulator);
		JSONArray array = db.queryRegulatorDB(sql);
		if(!array.isEmpty()) {
			GlobalMethods.printDebugInfo("return latest withdraw transaction status: " + array.getJSONObject(0).getInteger("status"));
			return array.getJSONObject(0).getInteger("status");
		}
		return null;
	}

	//Return count of record from tb_payment_deposit based on username and status
	public JSONArray getDPRecordCountByNameStatus(String userName, String status, String toDate){

		//String sql = "select COUNT(*) AS count from tb_payment_deposit tpd "
		String sql = "select * from tb_payment_deposit tpd " +
				"JOIN tb_user tu ON tpd.user_id = tu.id " +
				"where tu.real_name LIKE '%" + userName + "%' " +
				"AND tpd.create_time between '2017-01-01' and '"+toDate+"'" +
				"AND tpd.status = "+status;
		GlobalMethods.printDebugInfo("SQL: " + sql);

		JSONArray result = db.queryRegulatorDB(sql);
		if(result!=null && result.size()>0) {
			//return result.getJSONObject(0).getInteger("count");
			return result;
		}
		return null;
	}

	//Return count of record from tb_payment_withdraw based on username / status / fromDate / toDate
	public JSONArray getWDRecordCountByNameStatus(String userName, String status, String fromDate, String toDate){

		//String sql = "select COUNT(*) AS count " +
		String sql = "select * " +
				"from tb_payment_withdraw tpw " +
				"JOIN tb_user tu ON tpw.user_id = tu.id " +
				"where tu.real_name LIKE '%" + userName + "%' " +
				"AND tpw.create_time between '"+fromDate+"' and '"+toDate+"'" +
				"AND tpw.status = '" + status + "'" +
				"AND tpw.is_del = 0";
		GlobalMethods.printDebugInfo("SQL: " + sql);

		JSONArray result = db.queryRegulatorDB(sql);
		if(result!=null && result.size()>0) {
			//return result.getJSONObject(0).getInteger("count");
			return result;
		}
		return null;
	}


    public JSONArray getWDRecordCountByUserIdStatus(String userId,String status, String fromDate, String toDate){

        //String sql = "select COUNT(*) AS count " +
        String sql = "select * " +
                "from tb_payment_withdraw tpw " +
                "JOIN tb_user tu ON tpw.user_id = tu.id " +
                "where tu.id = '" + userId + "' " +
                "AND tpw.create_time between '"+fromDate+"' and '"+toDate+"'" +
                "AND tpw.status = '" + status + "'" +
                "AND tpw.is_del = 0";
        GlobalMethods.printDebugInfo("SQL: " + sql);

        JSONArray result = db.queryRegulatorDB(sql);
        if(result!=null && result.size()>0) {
            //return result.getJSONObject(0).getInteger("count");
            return result;
        }
        return null;
    }

	//Return record for Financial Information Audit based on username and card type
	public JSONArray getCCUnionpayByNameType(String userName, String type){

		String sql = "select * " +
				"from (" +
				"    SELECT user_id,payment_type" +
				"    FROM tb_cps_union_card" +
				"    UNION ALL" +
				"    SELECT user_id,payment_type" +
				"    FROM tb_credit_card" +
				"	) a " +
				"LEFT JOIN tb_user tu ON a.user_id = tu.id " +
				"where tu.real_name LIKE '%" + userName + "%' " +
				"and a.payment_type = "+type;
		GlobalMethods.printDebugInfo("SQL: " + sql);

		JSONArray result = db.queryRegulatorDB(sql);
		if(result!=null && result.size()>0) {
			return result;
		}
		return null;
	}

	//Return record from tb_payment_transfer based on username / status / fromDate / toDate
	public JSONArray getTransferRecordByNameStatus(String userName, String status, String fromDate, String toDate){

		String sql = "select * " +
				"from tb_payment_transfer tpw " +
				"JOIN tb_user tu ON tpw.user_id = tu.id " +
				"where tu.real_name LIKE '%" + userName + "%' " +
				"and tpw.create_time between '"+fromDate+"' and '"+toDate+"' " +
				"and tpw.status = "+status;
		GlobalMethods.printDebugInfo("SQL: " + sql);

		JSONArray result = db.queryRegulatorDB(sql);
		if(result!=null && result.size()>0) {
			return result;
		}
		return null;
	}

	//Return record from tb_payment_card_transaction based on date
	public JSONArray getCCTransactionRecordByDate(String fromDate, String toDate){

		String sql = "select * " +
				"from tb_payment_card_transaction tpw " +
				"where tpw.create_time between '"+fromDate+"' and '"+toDate+"' " +
				"and is_disabled = 0";
		GlobalMethods.printDebugInfo("SQL: " + sql);

		JSONArray result = db.queryRegulatorDB(sql);
		if(result!=null && result.size()>0) {
			return result;
		}
		return null;
	}

	//Return record from tb_card_audit based on date
	public JSONArray getCCArchiveRecordByName(String userName, String toDate){

		String sql = "select * " +
				"from tb_card_audit tpw " +
				"JOIN tb_user tu ON tpw.user_id = tu.id " +
				"where tu.real_name LIKE '%" + userName + "%' " +
				"and tpw.create_time between '2017-01-01 00:00:00' and '"+toDate+"' "+
				"and is_del = 0";
		GlobalMethods.printDebugInfo("SQL: " + sql);

		JSONArray result = db.queryRegulatorDB(sql);
		if(result!=null && result.size()>0) {
			return result;
		}
		return null;
	}

	//Return record from tb_transaction_record based on account and record's status
	public JSONArray getCARecordByAcctStatus(String accNum, String status, String fromDate, String toDate){

		String sql = "select * " +
				"from tb_transaction_record tpw " +
				"where tpw.transaction_invocation_date between '"+fromDate+"' and '"+toDate+"' " +
				"and tpw.sender_mt4_account_id = "+accNum+" "+
				"and tpw.status = "+status;
		GlobalMethods.printDebugInfo("SQL: " + sql);

		JSONArray result = db.queryRegulatorDB(sql);
		if(result!=null && result.size()>0) {
			return result;
		}
		return null;
	}

	//Return cash adjusment record's status from tb_transaction_record
	public Integer getCARecordStatus(String accNum){

		String sql = "select status " +
				"from tb_transaction_record tpw " +
				"where tpw.sender_mt4_account_id = "+accNum+" "+
				"order by transaction_id desc LIMIT 1";
		GlobalMethods.printDebugInfo("SQL: " + sql);

		JSONArray result = db.queryRegulatorDB(sql);
		if(result!=null && result.size()>0) {
			GlobalMethods.printDebugInfo("return: " + result.getJSONObject(0).getInteger("status"));
			return result.getJSONObject(0).getInteger("status");
		}
		return null;
	}

	public Integer getCARecordStatus(String accNum, String fromDate, String toDate) {

		String sql = "select status " +
				"from tb_transaction_record tpw " +
				"where tpw.transaction_invocation_date between '"+fromDate+"' and '"+toDate+"' " +
				"and tpw.sender_mt4_account_id = "+accNum+" "+
				"order by transaction_id desc LIMIT 1";

		GlobalMethods.printDebugInfo("SQL: " + sql);

		JSONArray result = db.queryRegulatorDB(sql);
		if(result!=null && result.size()>0) {
			GlobalMethods.printDebugInfo("return: " + result.getJSONObject(0).getInteger("status"));
			return result.getJSONObject(0).getInteger("status");
		}
		return null;
	}

	//Return record from tb_payment_fbw_transaction based on user ID
	public JSONArray getLBTRecordByUserID(String userID){

		String sql = "select * from tb_payment_fbw_transaction where user_id = "+userID;
		GlobalMethods.printDebugInfo("SQL: " + sql);

		JSONArray result = db.queryRegulatorDB(sql);
		if(result!=null && result.size()>0) {
			return result;
		}
		return null;
	}

	//Update withdraw status of tb_payment_withdraw in DB
	public boolean updateWithdrawStatus(GlobalProperties.ENV env, GlobalProperties.BRAND brand, GlobalProperties.REGULATOR regulator, String account, String status){

		String sql = "UPDATE tb_payment_withdraw SET status = "+ status +" WHERE mt4_account = "+ account+" ORDER BY create_time desc LIMIT 1;";

		GlobalMethods.printDebugInfo("sql: " + sql);

		db= new DbUtils(env, brand, regulator);

		if (db.updateRegulatorDB(sql) > 0) {
			return true;
		} else {
			return false;
		}
	}

	//查看SRC出金流程开关是否开启
	public boolean getSRCWithdrawStatus(GlobalProperties.ENV env, GlobalProperties.BRAND brand, GlobalProperties.REGULATOR regulator) {
		// 参数校验
		if (env == null || brand == null || regulator == null) {
			GlobalMethods.printDebugInfo("Parameters cannot be null: env=" + env + ", brand=" + brand + ", regulator=" + regulator);
			return false;
		}

		String sql = "SELECT value FROM tb_pcs_function_prop WHERE function_code = 7";
		GlobalMethods.printDebugInfo("sql: " + sql);

		db = new DbUtils(env, brand, regulator);
		JSONArray array = db.queryGlobalDB( sql);
		if (!array.isEmpty()) {
			boolean value = array.getJSONObject(0).getBooleanValue("value");
			GlobalMethods.printDebugInfo("Return SRC withdraw switch value: " + value);
			return value;
		}
		GlobalMethods.printDebugInfo("No SRC withdraw switch configuration found");
		return false;
	}

	//判断SRC开关是否开启及账户userId、countryCode是否在白名单中
	public boolean judgeSRCWithdrawStatus(GlobalProperties.ENV env, GlobalProperties.BRAND brand, GlobalProperties.REGULATOR regulator, String userId, String countryCode) {
		// 参数校验
		if (env == null || brand == null || regulator == null || userId == null || countryCode == null) {
			GlobalMethods.printDebugInfo("Parameters cannot be null: env=" + env + ", brand=" + brand + ", regulator=" + regulator + ", userId=" + userId+", countryCode="+countryCode);
			return false;
		}

		String sql = "SELECT\n" +
				"    CASE\n" +
				"        WHEN (SELECT value FROM tb_pcs_function_prop WHERE function_code = 7) = 'true' THEN TRUE\n" +
				"        WHEN (SELECT value FROM tb_pcs_function_prop WHERE function_code = 7) = 'false' AND (\n" +
				"            EXISTS (SELECT 1 FROM tb_pcs_function_prop WHERE function_code = 8 AND value = '%s')\n" +
				"            OR\n" +
				"            EXISTS (SELECT 1 FROM tb_pcs_function_prop WHERE function_code = 12 AND value = '%s')\n" +
				"        ) THEN TRUE\n" +
				"        ELSE FALSE\n" +
				"    END AS is_need_src_withdraw;\n" +
				"    ";
		sql = String.format(sql, userId, countryCode);
		GlobalMethods.printDebugInfo("sql: " + sql);

		db = new DbUtils(env, brand, regulator);
		JSONArray array = db.queryGlobalDB(sql);
		LogUtils.info("sql query result: " + array);
		if (!array.isEmpty()) {
			String value = array.getJSONObject(0).getString("is_need_src_withdraw");
			GlobalMethods.printDebugInfo("Return account's SRC Risk status value: " + value);

			// 检查账户是否在白名单中
			if (Objects.equals(value, "1")) {
				GlobalMethods.printDebugInfo("Flow to SRC ");
				return true;
			} else {
				return false;
			}
		}
		return false;
	}


}

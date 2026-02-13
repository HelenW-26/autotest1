package newcrm.business.dbbusiness;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import newcrm.global.GlobalProperties.BRAND;
import newcrm.global.GlobalProperties.ENV;
import newcrm.global.GlobalProperties.REGULATOR;
import newcrm.utils.db.DbUtils;

public class AccountDB {

	private DbUtils db;
	
	public AccountDB( ENV env,BRAND brand,REGULATOR regulator) {
		db = new DbUtils(env,brand,regulator);
	}
	
	/**
	 * 
	 * @param userId
	 * @return {acid,user_id,mt4_account,mt4_group,mt4_account_type,apply_currency,real_name,email,phone_num,p_ids}pids is client p_ids not account
	 */
	public JSONObject getFirstAccount(String userId) {
		String sql = "select A.id as acid,A.user_id, A.mt4_account,A.mt4_group, A.mt4_account_type,A.apply_currency, U.real_name,U.email,U.phone_num,R.p_ids from "
				+ "tb_account_mt4 A join tb_user U on A.user_id = U.id "
				+ "join tb_user_relation R on U.id = R.user_id "
				+ "where A.user_id="+userId + " order by A.id limit 1;";
		JSONArray results = db.queryRegulatorDB(sql);
		
		if(results==null || results.size()==0) {
			return null;
		}
		return results.getJSONObject(0);
	}

	/**
	 *
	 * @param user_id
	 * @return accounts,datasource_ids
	 */
	public JSONArray getAllAccounts(String user_id) {
		String sql = "select mt4_account,mt4_datasource_id from tb_account_mt4 where user_id = \""+user_id+"\" and is_del !=1 and mt4_account IS NOT NULL and (is_archive != 1 OR is_archive IS NULL);";
		return db.queryRegulatorDB(sql);
	}
}

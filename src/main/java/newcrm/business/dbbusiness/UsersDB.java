package newcrm.business.dbbusiness;

import com.alibaba.fastjson.JSONArray;

import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties.BRAND;
import newcrm.global.GlobalProperties.ENV;
import newcrm.global.GlobalProperties.REGULATOR;
import newcrm.utils.db.DbUtils;
import newcrm.utils.encryption.DesUtils;

import java.sql.Timestamp;

public class UsersDB {

	/**
	 * 
	 * @param email user email
	 * @param env test enviroment
	 * @param brand 
	 * @param regulator
	 * @return [(userId,wcStatus,idStatus,addrStatus,vars(Email log))]
	 */
	public JSONArray getUserRegistrationInfo(String email,ENV env,BRAND brand,REGULATOR regulator) {
		String encEmail = DesUtils.encode(email);
		if(ENV.PROD.equals(env)) {
			encEmail = encEmail.replace("+", "%2B");
		}
		String sql = "select U.id as userId, M.status as wcStatus, I.status as idStatus,AP.status as addrStatus,E.vars as vars from tb_user U left join tb_account_mt4 M on U.id = M.user_id left join tb_id_proof I on U.id = I.user_id left join tb_address_proof AP on U.id = AP.user_id left join tb_mail_send_log E on U.email = E.to_mail and E.template_invoke_name = \"welcome_mail\"\r\n"
				+ " where U.email = \""+encEmail+"\" and U.is_del !=1;";

		GlobalMethods.printDebugInfo("sql: " + sql);

		DbUtils db = new DbUtils(env, brand, regulator);
		
		return db.queryRegulatorDB(sql);
	}

	public JSONArray getPTUserRegistrationInfo(String email,ENV env,BRAND brand,REGULATOR regulator) {
		String encEmail = DesUtils.encode(email);
		if(ENV.PROD.equals(env)) {
			encEmail = encEmail.replace("+", "%2B");
		}
		String sql = "select * from tb_user where email = \""+encEmail+"\" and is_del !=1;";

		DbUtils db = new DbUtils(env, brand, regulator);

		return db.queryRegulatorDB(sql);
	}

	public JSONArray getSalt(String email,ENV env,BRAND brand,REGULATOR regulator) {
		String encEmail = DesUtils.encodeWithBrand(email,brand.toString());
		if(ENV.PROD.equals(env)) {
			encEmail = encEmail.replace("+", "%2B");
		}
		String sql = "select * from tb_user_login where email = \""+encEmail+"\" and is_del !=1;";

		DbUtils db = new DbUtils(env, brand, regulator);

		return db.queryGlobalDB(sql);
	}

	public boolean updatePassword(String email, String emailPassword,ENV env,BRAND brand,REGULATOR regulator) {
		String encEmail = DesUtils.encodeWithBrand(email,brand.toString());
		if(ENV.PROD.equals(env)) {
			encEmail = encEmail.replace("+", "%2B");
		}
		String sql = "update tb_user_login set email_password = '"+emailPassword+"' where email ='"+encEmail+"';";

		GlobalMethods.printDebugInfo("sql: " + sql);
		DbUtils db = new DbUtils(env, brand, regulator);

		if (db.updateGlobalDB(sql) > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 *
	 * @param email
	 * @param env
	 * @param brand
	 * @return user_id,regulator
	 */
	public JSONArray getUserBasicInfo(String email,ENV env,BRAND brand) {
		String encEmail = DesUtils.encodeWithBrand(email,brand.toString());

		String sql = "select user_id,regulator from tb_user_login where email = \""+encEmail+"\" and is_del !=1;";

		DbUtils db = new DbUtils(env, brand, REGULATOR.GLOBAL);

		return db.queryGlobalDB(sql);
	}

	public boolean insertWhitelist(String account, String datasourceId,ENV env,BRAND brand,REGULATOR regulator) {
		Timestamp time = new Timestamp(System.currentTimeMillis());
		String sql = "INSERT INTO tb_account_change_whitelist " +
				"(account, type, datasource_id, regulator, create_time, is_del) " +
				"VALUES ("+account+", 1, "+datasourceId+", \""+regulator.toString()+"\", \""+time.toString()+"\", 0);";

		GlobalMethods.printDebugInfo("sql: " + sql);
		DbUtils db = new DbUtils(env, brand, regulator);

		if (db.updateGlobalDB(sql) > 0) {
			return true;
		} else {
			return false;
		}
	}
}

package newcrm.business.dbbusiness;

import com.alibaba.fastjson.JSONArray;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.utils.db.DbUtils;

import java.math.BigDecimal;

public class PTDB {
    public JSONArray getSessionFromDB(GlobalProperties.ENV env, GlobalProperties.BRAND brand, GlobalProperties.REGULATOR regulator, String keyword){
        String sql = "select * from tb_pt_session where user_id='"+keyword+"' order by id desc";
        GlobalMethods.printDebugInfo("sql: " + sql);

        DbUtils db= new DbUtils(env, brand, regulator);
        return db.queryGlobalDB(sql);
    }

    public JSONArray getAccountFromDB(GlobalProperties.ENV env, GlobalProperties.BRAND brand, GlobalProperties.REGULATOR regulator, String keyword){
        String sql = "select * from tb_pt_account where session_id=(select id from  tb_pt_session where session_number = '"+keyword+"') order by id asc limit 1;";
        GlobalMethods.printDebugInfo("sql: " + sql);

        DbUtils db= new DbUtils(env, brand, regulator);
        return db.queryGlobalDB(sql);
    }

    public JSONArray getLiveAccountFromDB(GlobalProperties.ENV env, GlobalProperties.BRAND brand, GlobalProperties.REGULATOR regulator, String keyword){
        String sql = "select * from tb_pt_account where session_id=(select id from  tb_pt_session where session_number = '"+keyword+"') and is_new = 1;";
        GlobalMethods.printDebugInfo("sql: " + sql);

        DbUtils db= new DbUtils(env, brand, regulator);
        return db.queryGlobalDB(sql);
    }

    public boolean updatestageidforsession(GlobalProperties.ENV env, GlobalProperties.BRAND brand, GlobalProperties.REGULATOR regulator, String keyword){
        String sql = "UPDATE tb_pt_session SET stage_id = '2', status = '1' WHERE session_number = '"+keyword+"'";
        GlobalMethods.printDebugInfo("sql: " + sql);

        DbUtils db= new DbUtils(env, brand, regulator);
        if (db.updateGlobalDB(sql) > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean updatestageidforaccount(GlobalProperties.ENV env, GlobalProperties.BRAND brand, GlobalProperties.REGULATOR regulator, String keyword){
        String sql = "UPDATE tb_pt_account SET stage_id = '2', status = '1',create_time=date_sub(now(),interval 60 day),end_time=date_sub(now(),interval 30 day),update_time=date_sub(now(),interval 30 day),is_new='0' WHERE session_id=(select id from  tb_pt_session where session_number = '"+keyword+"')";
        GlobalMethods.printDebugInfo("sql: " + sql);

        DbUtils db= new DbUtils(env, brand, regulator);
        if (db.updateGlobalDB(sql) > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean updateBalanceforAccount(GlobalProperties.ENV env, GlobalProperties.BRAND brand, GlobalProperties.REGULATOR regulator, String sessionNum,String balance,String liveaccount){
        Double d =  Double.parseDouble(balance);
        d = d/10;
        BigDecimal bd = new BigDecimal(d);
        String sql = "UPDATE tb_pt_account SET daily_initial_balance = '"+ bd +"',create_time= date_sub(now(),interval 10 day) WHERE mt4_account= '"+liveaccount+"'";
        GlobalMethods.printDebugInfo("sql: " + sql);

        DbUtils db= new DbUtils(env, brand, regulator);
        if (db.updateGlobalDB(sql) > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean updateBananceForSession(GlobalProperties.ENV env, GlobalProperties.BRAND brand, GlobalProperties.REGULATOR regulator, String session,String balance){
        Double d =  Double.parseDouble(balance);
        d = d/10;
        BigDecimal bd = new BigDecimal(d);
        String sql = "UPDATE tb_pt_session SET initial_balance = '"+ bd +"' WHERE session_number = '"+session+"'";
        GlobalMethods.printDebugInfo("sql: " + sql);

        DbUtils db= new DbUtils(env, brand, regulator);
        if (db.updateGlobalDB(sql) > 0) {
            return true;
        } else {
            return false;
        }
    }
}

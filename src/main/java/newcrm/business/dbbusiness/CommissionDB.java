package newcrm.business.dbbusiness;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import newcrm.global.GlobalProperties.BRAND;
import newcrm.global.GlobalProperties.ENV;
import newcrm.global.GlobalProperties.REGULATOR;
import newcrm.utils.db.DbUtils;

public class CommissionDB {

	private DbUtils db;
	
	public CommissionDB( ENV env,BRAND brand,REGULATOR regulator) {
		db = new DbUtils(env,brand,regulator);
	}
	
	public List<String> getAccountUnderRebate(String rebateAccount){
		String sql = "SELECT ur.mt4_account as account, acc.mt4_datasource_id,u.real_name ,acc.mt4_group FROM tb_user_account_mt4_relation as ur "
				+ "join tb_account_mt4 as acc on acc.mt4_account = ur.mt4_account "
				+ "join tb_user as u on u.id = acc.user_id "
				+ "where rebate_account="+ rebateAccount+" "
				+ "and acc.is_del=0 "
				+ "and acc.accountDealType=1 "
				+ "and (acc.is_archive is null or acc.is_archive=0);";
		
		JSONArray result = db.queryRegulatorDB(sql);
		if(result == null || result.size()==0) {
			return null;
		}
		ArrayList<String> list = new ArrayList<>();
		for(int i=0; i< result.size();i++) {
			list.add(result.getJSONObject(i).getString("account"));
		}
		return list;
	}
	
	/**
	 * 
	 * @param dataId
	 * @param date
	 * @return [{price,symbol}]
	 */
	public JSONObject getRates(String dataId,String date) {
		String sql = "SELECT price,symbol FROM tb_mt4_prices where date=\""+date+"\" and serverId = "+dataId+" group by symbol order by id desc;";
		JSONArray result = db.queryGlobalDB(sql);
		if(result == null || result.size()==0) {
			return null;
		}
		String json = "{";
		for(int i =0;i<result.size();i++) {
			JSONObject obj = result.getJSONObject(i);
			json = json + "\""+obj.getString("symbol")+"\":{\"price\":"+obj.getString("price")+"},";
		}
		
		json = json +"\"USDUSD\":{\"price\":1},\"USDUSC\":{\"price\":100},\"USCUSD\":{\"price\":0.01}}"; 
		
		
		return JSON.parseObject(json);
	}
	
	/**
	 * 
	 * @param symbol
	 * @param tradingGroup
	 * @param rebateAccount
	 * @param user_id
	 * @return {commission_type,coefficient,contract_size,point_size,currency,group_name}
	 */
	public JSONArray getCommissionInfo(String symbol,String tradingGroup,String rebateAccount,String user_id) {
		String sql = "select cr.commission_type,cf.coefficient, p.contract_size,p.point_size,p.currency,tg.group_name from tb_commission_rules as cr "
				+ "join tb_commision_coefficient as cf on cr.id = cf.rule_id "
				+ "join tb_trading_group as tg on cr.trading_group_id = tg.id "
				+ "join tb_trading_group_symbol tgs on tgs.trading_group_id = tg.id "
				+ "join tb_pip_setting as p on tgs.pip_setting_id = P.id "
				+ "where tgs.is_del = 0 and (p.status = 0 or p.status is null) "
				+ "and tgs.symbol=\""+symbol+"\" and cr.mt4_group=\""+tradingGroup+"\" and cr.agentAccount="+rebateAccount+" and cf.uid="+user_id+";";
		JSONArray result = db.queryRegulatorDB(sql);
		if(result == null || result.size()==0) {
			return null;
		}
		
		return result;
	}
	
	public static void main(String args[]) {
		CommissionDB db = new CommissionDB(ENV.ALPHA,BRAND.VFX,REGULATOR.VFSC2);
		
		//db.getAccountUnderRebate("805391309");
		System.out.println(db.getRates("5", "2022-06-15"));
		System.out.println(db.getCommissionInfo("XAGUSD", "TEST_VFX_GBP", "805391309", "1025891").toJSONString());
	}
}

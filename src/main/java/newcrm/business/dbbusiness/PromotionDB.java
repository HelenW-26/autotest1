package newcrm.business.dbbusiness;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import newcrm.global.GlobalProperties.BRAND;
import newcrm.global.GlobalProperties.ENV;
import newcrm.global.GlobalProperties.PROMOTION;
import newcrm.global.GlobalProperties.REGULATOR;
import newcrm.utils.db.DbUtils;

public class PromotionDB {

	private DbUtils db;
	
	private ENV env;
	
	public PromotionDB( ENV env,BRAND brand,REGULATOR regulator) {
		db = new DbUtils(env,brand,regulator);
		this.env = env;
	}
	
	/**
	 * 
	 * @param promotion 
	 * @param userId
	 * @return {user_id,opt_in,last_opt_in_time,name,campaign_id} or null;
	 */
	public JSONObject getStatus(PROMOTION promotion,String userId) {
		String sql = "select U.user_id, U.opt_in,U.last_opt_in_time,C.name, U.campaign_id from tb_campaign_user U join tb_campaign C on U.campaign_id = C.id "
				+ "where C.campaign_type = "+promotion.getCampaignType(env)+" and U.user_id = "+userId+" order by U.last_opt_in_time desc;";
		
		JSONArray result = db.queryRegulatorDB(sql);
		if(result !=null && result.size()>0) {
			return result.getJSONObject(0);
		}else {
			return null;
		}
	}
	/**
	 * 
	 * @param refererId 推荐人userid
	 * @param refereeId 被推荐人userid
	 * @return {id,referrer_id,referee_id,participant_id,refer_code,create_time}
	 */
	public JSONObject getRafLink(String refererId,String refereeId) {
		String sql = "select * from tb_campaign_reference where referrer_id = "+refererId+" and referee_id = " + refereeId+";";
		JSONArray result = db.queryRegulatorDB(sql);
		if(result !=null && result.size()>0) {
			return result.getJSONObject(0);
		}else {
			return null;
		}
	}
	
	
	/**
	 * 
	 * @param participantId
	 * @return
	 */
	public JSONArray getVouchers(String participantId) {
		String sql = "select participant_id, user_id, amount,currency, create_time, status from tb_campaign_voucher where participant_id = " + participantId + ";";
		JSONArray result = db.queryRegulatorDB(sql);
		if(result !=null && result.size()>0) {
			return result;
		}else {
			return null;
		}
	}
	
	/**
	 *  获取用户一个活动下所有的voucher
	 * @param userId
	 * @param campaignID
	 * @return
	 */
	public JSONArray getVouchers(String userId,Integer campaignID) {
		String sql = "select V.participant_id, V.user_id, V.amount,V.currency, V.create_time, V.status from tb_campaign_voucher V join tb_campaign_participant P"
				+ " on v.participant_id = P.id where P.user_id="+userId+ " and P.campaign_id="+campaignID+";";
		JSONArray result = db.queryRegulatorDB(sql);
		if(result !=null && result.size()>0) {
			return result;
		}else {
			return null;
		}
	}
	
	
}

package newcrm.utils.app;

import com.alibaba.fastjson.JSONObject;

public class Leverage extends APIBase {

	public Leverage(String host,String regulator,String brand) {
		super(host,"/leverage/leverage_apply",regulator,brand);
	}
	
	/**
	 * 
	 * @param mt4Account 账号
	 * @param leverage 申请的杠杆
	 * @param oldLeverage 旧杠杆
	 * @param remark 备注
	 * @return
	 */
	public String changeLeverage(Integer mt4Account,Integer leverage,Integer oldLeverage,String remark,String uid) {
		JSONObject obj = new JSONObject();
		obj.put("mt4Account", mt4Account);
		obj.put("leverage", leverage);
		obj.put("oldLeverage", oldLeverage);
		obj.put("remark", remark);
		obj.put("userId", uid);

		return this.send(obj);
	}
}

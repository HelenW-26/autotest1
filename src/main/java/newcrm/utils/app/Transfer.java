package newcrm.utils.app;

import com.alibaba.fastjson.JSONObject;

public class Transfer extends APIBase {
	
	/**
	 * 
	 * @param host app server url from yannibot
	 * @param regulator regulator
	 */
	public Transfer(String host,String regulator,String brand) {
		super(host, "/transfer/transfer", regulator,brand);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 
	 * @param formMt4Account 转出账户
	 * @param transferAmount 转账金额
	 * @param applicationNotes 申请备注
	 * @param toMt4Account 转入账户
	 * @return
	 */
	public  String sendTransfer(String formMt4Account,String transferAmount,String applicationNotes,String toMt4Account,String uid) {

		JSONObject obj = new JSONObject();
		obj.put("formMt4Account", formMt4Account);
		obj.put("transferAmount", transferAmount);
		obj.put("applicationNotes", applicationNotes);
		obj.put("toMt4Account", toMt4Account);
		obj.put("userId", uid);

		return this.send(obj);
	}

}

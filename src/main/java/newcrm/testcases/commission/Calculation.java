package newcrm.testcases.commission;

import static org.testng.Assert.assertNotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
//import com.mysql.fabric.xmlrpc.base.Array;

import newcrm.adminapi.AdminAPI;
import newcrm.business.dbbusiness.CommissionDB;
import newcrm.global.GlobalProperties.BRAND;
import newcrm.global.GlobalProperties.ENV;
import newcrm.global.GlobalProperties.REGULATOR;
import newcrm.utils.AlphaServerEnv;

public class Calculation {

	private ENV env;
	private BRAND brand;
	private REGULATOR regulator;
	private AdminAPI admin;
	private CommissionDB db;
	
	private HashMap<String, JSONObject> summary; 
	@BeforeMethod
	@Parameters(value= {"TestEnv","Brand","Regulator","Server"})
	public void initalEnv(String env,String brand,String regulator,String serverName) {
		for(ENV e: ENV.values()) {
			if(e.toString().equalsIgnoreCase(env)) {
				this.env = e;
			}
		}
		for(BRAND e: BRAND.values()) {
			if(e.toString().equalsIgnoreCase(brand)) {
				this.brand = e;
			}
		}
		for(REGULATOR e: REGULATOR.values()) {
			if(e.toString().equalsIgnoreCase(regulator)) {
				this.regulator = e;
			}
		}
		String adminUser = "";
		String password = "";
		if(this.env.equals(ENV.ALPHA)) {
			adminUser = "cmatest";
			password = "123Qwe";
		}else {
			adminUser = "Test CRM";
			password = "BDAVJECnh4hovtZLV33N";
		}
		String adminUrl = AlphaServerEnv.getAdminUrl(serverName);;
		
		
		admin = new AdminAPI(adminUrl,this.regulator,adminUser,password,this.brand,this.env);
		db = new CommissionDB(this.env,this.brand,this.regulator);
		summary = new HashMap<>();
	}
	
	/**
	 * 查找汇率
	 * @param from 基础currency
	 * @param to 转换的currency
	 * @param rates 所有的rates
	 * @return
	 */
	private Double getRates(String from, String to, JSONObject rates) {
		Double result = 0.0;
		String symbol = from+to;
		if(from.equalsIgnoreCase(to)) {
			return 1.0;
		}
		if(rates.containsKey(symbol)) {
			result = rates.getJSONObject(symbol).getDouble("price");
		}else {//查找到反向汇率，需要取倒数
			symbol = to+from;
			if(rates.containsKey(symbol)) {
				result = rates.getJSONObject(symbol).getDouble("price");
				result = 1.0/result;
			}else {//当没有直接转换时，通过USD转
				Double fromUSD = rates.getJSONObject(from+"USD").getDouble("price");
				Double toUSD = rates.getJSONObject(to+"USD").getDouble("price");
				result = fromUSD/toUSD;
			}
		}
		//System.out.println(from+to+"  rate: " + result);
		return result;
	}
	
	/**
	 * 计算每一笔交易所产生的佣金
	 * @param history
	 * @param commConfig
	 * @param accInfo
	 * @param rebateAccInfo
	 * @param rates
	 * @param summaryKey
	 * @return
	 */
	private Double calculateTrade(JSONObject history,JSONArray commConfig,JSONObject accInfo,JSONObject rebateAccInfo,JSONObject rates,String summaryKey) {
		Double closePrice = history.getDouble("closePrice");
		Double volume = history.getDouble("volume");
		JSONObject infos = null;
		if(summary.containsKey(summaryKey)) {
			infos = summary.get(summaryKey);
		}else {
			infos = JSON.parseObject("{\"total\":0.0,\"groups\":{},\"accounts\":{}}");
		}
		//System.out.println(history.toJSONString());
		//System.out.println(commConfig.toJSONString());
		JSONObject groups = infos.getJSONObject("groups");
		JSONObject accounts = infos.getJSONObject("accounts");
		Double total = infos.getDouble("total");
		
		Double subTotal = 0.0;
		Double t_cc = 0.0;
		
		String rebateCur = rebateAccInfo.getString("currency");
		String accCur = accInfo.getString("currency");
		String t_group = "";
		String t_symbol = history.getString("symbol");
		String t_ticket = history.getString("ticket");
		
		System.out.println("********************"+summaryKey + "-" +t_symbol+"**************************");
		for(JSONObject config:commConfig.toJavaList(JSONObject.class)) {
			//config: commission_type,coefficient,contract_size,point_size,currency,group_name
			//history: symbol,closePrice,volume,ticket
			
			String symbolCur = config.getString("currency");
			Double coe = config.getDouble("coefficient");
			Double contract_size = config.getDouble("contract_size");
			Double point_size = config.getDouble("point_size");
			t_group = config.getString("group_name");
			Integer comm_type = config.getInteger("commission_type");
			
			String sub_info = "";
			if(comm_type.equals(0) || comm_type.equals(3)) {
				//by pip setting
				t_cc = volume*contract_size*point_size*coe*getRates(symbolCur,rebateCur,rates);
				if(comm_type.equals(0)) {
					sub_info = t_symbol + " - " + t_ticket + " get rebate by Pip " + t_cc;
				}else {
					sub_info = t_symbol + " - " + t_ticket + " get rebate by Outer Pip " + t_cc;
				}
			}else if(comm_type.equals(1) || comm_type.equals(4)) {
				//by  $
				t_cc = volume*coe*getRates(accCur,rebateCur,rates);
				if(comm_type.equals(1)) {
					sub_info = t_symbol + " - " + t_ticket + " get rebate by Outer $ " + t_cc;
				}else {
					sub_info = t_symbol + " - " + t_ticket + " get rebate by $ " + t_cc;
				}
			}else {
				//by million
				Double notionalValue = closePrice*contract_size*volume;
				t_cc = notionalValue * coe * getRates(symbolCur,rebateCur,rates) /1000000;
				sub_info = t_symbol + " - " + t_ticket + " get rebate by Million " + t_cc;
			}

			System.out.println(sub_info);
			subTotal = subTotal + t_cc; 
		}
		System.out.println("******************** "+summaryKey + " - " +t_symbol+" FINISH **************************");
		
		//处理产品分类数据
		String acc = accInfo.getString("account");
		String info =  t_symbol +"- " + t_ticket  + " - total: " + subTotal;
		JSONObject subGroup = null;
		if(groups.containsKey(t_group)) {
			subGroup = groups.getJSONObject(t_group);
		}else {
			subGroup = JSON.parseObject("{\"total\":0.0,\"infos\":[]}");
		}
		
		subGroup.put("total", subTotal+subGroup.getDoubleValue("total"));
		subGroup.getJSONArray("infos").add(info);
		
		groups.put(t_group, subGroup);
		
		
		JSONObject subAccout = null;
		
		if(accounts.containsKey(acc)) {
			subAccout = accounts.getJSONObject(acc);
		}else {
			subAccout = JSON.parseObject("{\"total\":0.0,\"infos\":[]}");
		}
		
		subAccout.put("total", subTotal+subAccout.getDoubleValue("total"));
		subAccout.getJSONArray("infos").add(info);
		
		accounts.put(acc, subAccout);
		
		//处理总和
		infos.put("total",total+subTotal);
		infos.put("groups",groups);
		infos.put("accounts", accounts);
				
				
		this.summary.put(summaryKey, infos);
		return subTotal;
	}
	
	
	@Test
	@Parameters(value= {"RebateAccout","Date"})
	public void calculateByRebateAccount(String rebateAccount,String date) {
		List<String> tradingAccount = db.getAccountUnderRebate(rebateAccount);//获得rebate下可返佣的交易账户
		assertNotNull(tradingAccount,"have not found any trading account");
		
		JSONObject rebateAcc = admin.getRebateAccountInfo(rebateAccount);
		assertNotNull(rebateAcc,"have not found rebate account information");
		
		String ownerIds[] = rebateAcc.getString("p_ids").split(",");//获得rebate account上级owner的id
		Double total = 0.0;
		for(String acc: tradingAccount) {//计算每个trading account产生的佣金
			JSONObject accInfo = admin.getAccountInfo(acc);
			assertNotNull(accInfo,"have not found trading account information - "+acc);
			
			JSONArray historys = admin.getTradingHistory(accInfo, date);
			if(historys==null) {//如果没有交易记录，则跳过
				continue;
			}
			
			JSONObject rates = db.getRates(accInfo.getString("dsId"), date);
			assertNotNull(rates,"have not found rates on data source id - "+accInfo.getString("dsId"));
			
			Double total_account = 0.0;
			for(JSONObject history:historys.toJavaList(JSONObject.class)) {
				JSONArray commConfig = db.getCommissionInfo(history.getString("symbol"), accInfo.getString("group"), rebateAccount, rebateAcc.getString("user_id"));
				if(commConfig == null) {//没有找到对应组别对应symbol的commission 配置
					continue;
				}
				Double bySymbol = calculateTrade(history,commConfig,accInfo,rebateAcc,rates,rebateAccount);
				total_account = total_account+bySymbol;
			}
			total = total + total_account;
		}
		
		
		printSummary();
	}
	
	private void printSummary() {
		System.out.println("\n\n=======================SUMMARY=======================\n");
		for(Map.Entry<String, JSONObject> e: this.summary.entrySet()) {
			System.out.println(e.getKey() + " Total Rebate: " + e.getValue().getDoubleValue("total"));
			System.out.println("--------------SUMMARY BY PRODUCT GROUP-------------------");
			JSONObject groups = e.getValue().getJSONObject("groups");
			for(String k: groups.keySet()) {
				System.out.printf("%5s%s\n"," ","- Product Group - " + k + " - Total: " + groups.getJSONObject(k).getString("total"));
				for(String info: groups.getJSONObject(k).getJSONArray("infos").toJavaList(String.class)) {
					System.out.printf("%10s%s\n"," ","- " +info);
				}
				System.out.printf("%20s\n","-");
			}
			System.out.println("--------------SUMMARY BY TRADING ACCOUNT-------------------");
			JSONObject accs = e.getValue().getJSONObject("accounts");
			for(String k: accs.keySet()) {
				System.out.printf("%5s%s\n"," ","- Account - " + k + " - Total: " + accs.getJSONObject(k).getString("total"));
				for(String info: accs.getJSONObject(k).getJSONArray("infos").toJavaList(String.class)) {
					System.out.printf("%10s%s\n"," ","- " +info);
				}
				System.out.printf("%20s\n","-");
			}
			
		}
	}
	
	public static void main(String args[]) {
		Calculation cal = new Calculation();
		cal.initalEnv("alpha", "VFX", "VFSC2", "earth");
		cal.calculateByRebateAccount("805315222", "2022-08-30");
		
		//cal.printSummary();
	}
}

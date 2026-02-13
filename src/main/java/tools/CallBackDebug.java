package tools;

import newcrm.utils.DepositCallBack;
import newcrm.utils.DepositCallBack.CHANNEL;
/**
 * 用来测试添加的callback
 * @author FengLiu
 *
 */
public class CallBackDebug {

	public static void main(String args[]) {

		//填写入金的基本信息，每个入金渠道所需要的信息不一样
		//String url = "https://openapi-thor.crm-alpha.com";

		//String url = "https://openapi-taurus-ex.crm-alpha.com";
		//String url = "openapi-thor.crm-alpha.com";
		//String url = "https://openapi-aquarius-ex.crm-alpha.com";
		//String url = "https://openapi-staging-vt-ex.crm-alpha.com";
		//String url ="https://openapi-cps06.crm-alpha.com";
		String url ="https://openapi-core-au.crm-alpha.com";

		String amount = "100.01";
		//Double amount = 100.00;
		//get currency code from https://www.iban.com/currency-codes
		String currency = "USD";
		String ordernum = "VU2193120260114051151";
		
		//card
		String first6num = "545432";
		String last4num = "1345";
		String year = "2026";
		String month = "08";
		//brand should be: VFX,VT,PUG,UM,MO,STAR.VJP
		String brand = "VFX";
		String regulator = "VFSC";
		
		
		//根据callback url，品牌和监管创建一个实例
		DepositCallBack callback = new DepositCallBack(url,brand,regulator);
		//调用generateCallback方法构造一个callback请求
		callback.generateCallback(DepositCallBack.CHANNEL.BRIDGE_CARDPAY,ordernum,currency,amount,first6num,last4num,year,month);
		//callback.generateCallback(CHANNEL.WORLDCARD,ordernum,currency,amount,first6num,last4num,year,month);
		//callback.generateCallback(CHANNEL.CARDPAY,ordernum,currency,amount,first6num,last4num,year,month);
		//callback.generateCallback(CHANNEL.XPAY,ordernum,currency,amount,first6num,last4num,year,month);
		//callback.generateCallback(CHANNEL.POWERCASH,ordernum,currency,amount,first6num,last4num,year,month);
		//callback.generateCallback(CHANNEL.SOLIDPAY,ordernum,currency,amount,first6num,last4num,year,month);
		//callback.generateCallback(CHANNEL.ZOTAPAY,ordernum,currency,amount,first6num,last4num,year,month);
		//callback.generateCallback(CHANNEL.CREDITCARD_VIRTUALPAY,ordernum,currency,amount,first6num,last4num,year,month);
		//callback.generateCallback(CHANNEL.SKRILLPAY,ordernum,currency,amount,first6num,last4num,year,month);
		//callback.generateCallback(CHANNEL.FASAPAY,ordernum,currency,amount,first6num,last4num,year,month);
		//callback.generateCallback(CHANNEL.STICPAY,ordernum,currency,amount,first6num,last4num,year,month);
		//callback.generateCallback(CHANNEL.NETELLERPAY,ordernum,currency,amount,first6num,last4num,year,month);
		//callback.generateCallback(CHANNEL.ASTROPAY,ordernum,currency,amount,first6num,last4num,year,month);
		//callback.generateCallback(CHANNEL.BRAZIL_PAGSMILE,ordernum,currency,amount,first6num,last4num,year,month);
		//callback.generateCallback(CHANNEL.EBUY,ordernum,currency,amount,first6num,last4num,year,month);
		//callback.generateCallback(CHANNEL.PERFECTMONEY,ordernum,currency,amount,first6num,last4num,year,month);
		//callback.generateCallback(CHANNEL.BITWALLET,ordernum,currency,amount,first6num,last4num,year,month);
		
		//callback.generateCallback(CHANNEL.EEZIEPAY,ordernum,currency,amount,first6num,last4num,year,month);
		//callback.generateCallback(CHANNEL.USDTERC,ordernum,currency,amount,first6num,last4num,year,month);
		//callback.generateCallback(CHANNEL.USDTTRC,ordernum,currency,amount,first6num,last4num,year,month);

		//callback.generateCallback(CHANNEL.UNIONCPS,ordernum,currency,amount,first6num,last4num,year,month);
		/*callback.generateCallback(CHANNEL.CPSCallback,ordernum,currency,amount,first6num,last4num,year,month);*/
		
		//发送之前构造的callback请求
		callback.sendCallback();
	}
}

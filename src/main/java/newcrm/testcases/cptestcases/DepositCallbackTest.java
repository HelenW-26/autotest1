package newcrm.testcases.cptestcases;

import newcrm.global.GlobalMethods;
import newcrm.utils.DepositCallBack;
import newcrm.utils.DepositCallBack.CHANNEL;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
public class DepositCallbackTest {

	@Test(alwaysRun = true)
	@Parameters(value = {"OpenAPIURL","Brand","Regulator","Channel","OrderNum","Currency","Amount","First6num","Last4num","Year","Month"})
	public void testCallback(String url,String brand,String regulator,String channel,String orderNum, String currency, String amount,@Optional("545455")String first6num,
			@Optional("1234")String last4num,@Optional("2027")String year,@Optional("09")String month) {
		
		
		GlobalMethods.printDebugInfo("OpenAPI: " + url);
		GlobalMethods.printDebugInfo("Brand: " + brand);
		GlobalMethods.printDebugInfo("Regulator: " + regulator);
		GlobalMethods.printDebugInfo("Channel: " + channel);
		GlobalMethods.printDebugInfo("OrderNum: " + orderNum);
		GlobalMethods.printDebugInfo("Currency: " + currency);
		GlobalMethods.printDebugInfo("Amount: " + amount);
		GlobalMethods.printDebugInfo("First6num: " + first6num);
		GlobalMethods.printDebugInfo("Last4num: " + last4num);
		GlobalMethods.printDebugInfo("Year: " + year);
		GlobalMethods.printDebugInfo("Month: " + month);
		
		
		DepositCallBack callback = new DepositCallBack(url,brand,regulator);
		
		for(CHANNEL c:CHANNEL.values()) {
			if(c.toString().equalsIgnoreCase(channel)) {
				callback.generateCallback(c, orderNum, currency, Double.valueOf(amount), first6num, last4num, year, month);
				break;
			}
		}
		
		String result = callback.sendCallback();
		assertNotNull(result);
		if(!result.contains("OK")&&!result.contains("SUCCESS")) {
			assertTrue(false, result);
		}
	}
}

package newcrm.testcases.others;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import newcrm.business.dbbusiness.AccountDB;
import newcrm.business.dbbusiness.UsersDB;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;


import newcrm.business.dbbusiness.EmailDB;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties.BRAND;
import newcrm.global.GlobalProperties.ENV;
import newcrm.global.GlobalProperties.REGULATOR;

import static org.testng.Assert.*;


/**
 * @author ShanL
 *
 */
public class OtherFunctionalTests {
	protected EmailDB emailDB;

	@BeforeClass(alwaysRun = true)
	@Parameters(value= {"Brand"})
	public void initiEnv(String brand, ITestContext context) throws Exception {
		GlobalMethods.setEnvValues(brand);
	}

	@Test
	@Parameters(value= {"Brand","email"})
	private void whitelistAccountsByEmail(String brand,String email) throws Exception {
		brand = brand.toUpperCase();
		UsersDB udb = new UsersDB();
		email = email.toLowerCase();
		GlobalMethods.printDebugInfo("User email: " + email);

		JSONArray jsArray = udb.getUserBasicInfo(email, ENV.ALPHA, BRAND.valueOf(brand));
		assertNotNull(jsArray);
        assertFalse(jsArray.isEmpty(), "User not found by email: " + email);

		JSONObject obj = jsArray.getJSONObject(0);
		String user_id= obj.getString("user_id");
		String regulator= obj.getString("regulator");
		GlobalMethods.printDebugInfo("user_id: " + user_id);
		GlobalMethods.printDebugInfo("regulator: " + regulator);

		AccountDB accdb = new AccountDB(ENV.ALPHA,BRAND.valueOf(brand),REGULATOR.valueOf(regulator));
		JSONArray accInfo = accdb.getAllAccounts(user_id);
		GlobalMethods.printDebugInfo("accInfo: " + accInfo);

		for(int i=0; i< accInfo.size(); i++) {
			//Add to whitelist
			if(accInfo.getJSONObject(i).getString("mt4_account")!=null){
				udb.insertWhitelist(
						accInfo.getJSONObject(i).getString("mt4_account"),
						accInfo.getJSONObject(i).getString("mt4_datasource_id"),
						ENV.ALPHA,BRAND.valueOf(brand),REGULATOR.valueOf(regulator));
			}
		}

	}

	
}

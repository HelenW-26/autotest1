package newcrm.testcases.crmapi;



import static org.testng.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;

import newcrm.testcases.cptestcases.RegisterTestcases;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONObject;

import newcrm.cpapi.CPAPIBase;
import newcrm.cpapi.CPAPIRegister;
import newcrm.factor.Factor;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties.BRAND;

public class CPAPIRegistrationTestcases {
	private final BRAND[] autoAuditBrands = {BRAND.VT, BRAND.UM, BRAND.PUG, BRAND.VFX};
	
	
	@BeforeClass(alwaysRun = true)
	@Parameters(value= {"Brand"})
	public void initiEnv(String brand, ITestContext context) throws Exception {
		GlobalMethods.setEnvValues(brand);
	}


	@Test
	@Parameters(value= {"TestEnv","Brand","Regulator","TraderURL"})
	public void apiRegistration(String TestEnv, String brand, String regulator, String host) throws Exception {
		Factor myfactor = new Factor(TestEnv,brand,regulator,host);
		CPAPIRegister reg = myfactor.newInstance(CPAPIRegister.class);
			
		String email = GlobalMethods.getRandomString(8)+GlobalMethods.getRandomNumberString(3)+"@apiautotest.com";
		email = email.toLowerCase();
		
		String lastName = "ApiAutomotion";
		String firstName = "testcrm"+GlobalMethods.getRandomString(6);
		String mobile = GlobalMethods.getRandomNumberString(11);
		String idNo = "API"+GlobalMethods.getRandomString(5)+GlobalMethods.getRandomNumberString(6);
		String phoneCode = "66";
		String countryCode = "4101";
		//France has another prod website for VT
		String country = "Italy";
		String platform = "mt4";
		String currency = "USD";

		RegisterTestcases register = new RegisterTestcases();
		String affid = register.getPRODIBCode(brand);

		String encryptS = reg.createUser(email, affid, "", firstName, lastName, country, mobile, "VFX", "FALSE", phoneCode, "", "","", "Individual", regulator);
		
		//Get encryptA and set cookie
		String encryptA = reg.postRegister(encryptS);
		
		//Login with A
		HashMap<String,String> header = reg.loginWithA(encryptA);
		
		//Step1
		reg.step1(firstName, lastName, email, mobile, phoneCode, countryCode,idNo);		
		
		//Step2 and Step3 been removed since registration revamp
		//reg.step2(countryCode);
		
		//Step3
		//reg.step3(regulator);
		
		//Step4
		reg.step4(platform, currency);		
		
		//Step5
		reg.step5(countryCode, idNo);
		
		Thread.sleep(2000);
		
		//Verify auto audit
		CPAPIBase api = new CPAPIBase(host, header);
		JSONObject accInfo = api.queryMetaTraderAccountDetails();
		
		reg.printUserFundsInfo(brand, accInfo.getString("account"),email);
		
		if(Arrays.asList(autoAuditBrands).contains(BRAND.valueOf(brand))) {
			assertTrue(accInfo.getString("account")!=null,"Account auto audit failed!");
		}

	}


}

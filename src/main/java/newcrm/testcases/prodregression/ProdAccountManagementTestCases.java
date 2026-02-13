package newcrm.testcases.prodregression;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

import newcrm.adminapi.AdminAPIUserAccount;
import newcrm.business.businessbase.*;
import newcrm.business.businessbase.copyTrading.CPCopyTrading;
import newcrm.global.GlobalProperties;
import newcrm.utils.testCaseDescUtils;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import newcrm.business.aubusiness.AuCPlogin;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties.ACCOUNTTYPE;
import newcrm.global.GlobalProperties.CPMenuName;
import newcrm.global.GlobalProperties.CURRENCY;
import newcrm.global.GlobalProperties.PLATFORM;
import newcrm.global.GlobalProperties.BRAND;
import newcrm.pages.clientpages.TransferPage;
import newcrm.pages.clientpages.TransferPage.Account;
import newcrm.testcases.TestDataProvider;
import newcrm.testcases.cptestcases.AccountManagementTestCases;
import vantagecrm.Utils;

public class ProdAccountManagementTestCases extends AccountManagementTestCases {

	@Override
	public void beforMethod(String TestEnv, String headless, String Brand, String Regulator, String TraderURL,
			String TraderName, String TraderPass, String AdminURL, String AdminName, String AdminPass, String Debug,@Optional("")String server,
			ITestContext context) {
		// TODO Auto-generated method stub
		//launchBrowser( TestEnv,  headless,  Brand,  Regulator, TraderURL, TraderName, TraderPass, AdminURL,  AdminName, AdminPass,  Debug, context);
	}

	@Test(dataProvider="addAccountUserProvider",dataProviderClass=TestDataProvider.class)
	public void openOneAdditionalAccount(@Optional("alpha")String TestEnv, @Optional("False") String headless, String Brand, String Regulator, 
			@Optional("")String TraderURL, @Optional("")String TraderName, @Optional("")String TraderPass,
			@Optional("")String AdminURL, @Optional("")String AdminName, @Optional("")String AdminPass, @Optional("True")String Debug,
				              ITestContext context) {
		launchBrowser( TestEnv,  headless,  Brand,  Regulator, TraderURL, TraderName, TraderPass, AdminURL,  AdminName, AdminPass,  Debug, context);
		
//		funcAddAcc(AdminURL, AdminName, AdminPass,false);
		
	}
	
	@Test(dataProvider="leverageUserProvider", dataProviderClass=TestDataProvider.class, description = testCaseDescUtils.CPFUNDTRANS)
	public void testTransfer(@Optional("alpha")String TestEnv, @Optional("False") String headless, String Brand, String Regulator, 
			@Optional("")String TraderURL, @Optional("")String TraderName, @Optional("")String TraderPass,
			@Optional("")String AdminURL, @Optional("")String AdminName, @Optional("")String AdminPass, @Optional("True")String Debug,
				              ITestContext context) {
		launchBrowser( TestEnv,  headless,  Brand,  Regulator, TraderURL, TraderName, TraderPass, AdminURL,  AdminName, AdminPass,  Debug, context);
		
		funcTransfer();
	}
	public static void main(String args[]) {
		WebDriver driver = null; 
		driver = Utils.funcSetupDriver(driver, "chrome", "false");
		driver.get("https://secure-taurus-ex.crm-alpha.com/");
		CPLogin login = new AuCPlogin(driver, "https://secure-taurus-ex.crm-alpha.com");
		login.inputUserName("wb10@test.com");
		login.inputPassWord("123Qwe");
		login.submitOld();
		
		driver.get("https://secure-taurus-ex.crm-alpha.com/transferFunds");
		
		TransferPage tp = new TransferPage(driver);
		List<Account> accounts = tp.getFromAccounts();
		
		tp.selectFromAccount(accounts.get(0).getAccNumber());
		tp.selectToAccount(tp.getToAccount().get(0).getAccNumber());
		tp.setAmount("100");
		
		
	}
}

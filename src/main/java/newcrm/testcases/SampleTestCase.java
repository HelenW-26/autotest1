package newcrm.testcases;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import newcrm.business.businessbase.CPDepositFunds;
import newcrm.business.businessbase.CPLogin;
import newcrm.business.businessbase.CPMenu;
import newcrm.business.businessbase.CPNetellerPay;

import newcrm.business.businessbase.CPTransactionHistory;
import newcrm.global.GlobalProperties.CPMenuName;
import newcrm.global.GlobalProperties.CURRENCY;
import newcrm.global.GlobalProperties.DEPOSITMETHOD;
import newcrm.global.GlobalProperties.STATUS;



public class SampleTestCase extends BaseTestCase {
	@Test(priority=0)
	public void cpLogout() throws Exception
	{
		//use "this" keyword to get properties,use myfactor get correct instance
		CPLogin login = myfactor.newInstance(CPLogin.class,TraderURL);
		//use assert to check result
		assertTrue(login.logout(), "Logout Failed at cplogout method");
	}
	@Test(priority=0)
	public void checkDepositMethod() throws InterruptedException {
		//CPDepositFunds deposit_funds = myfactor.newInstance(CPDepositFunds.class);
		
	}
	
	@Test(priority=0)
	public void testNetellerPay() {
		String amount = String.valueOf((int) (50 + Math.random() * 950));
		String email = "Netellertest@test.com";
		String notes = "auto test";
		String account = null;

		// Instances
		CPDepositFunds deposit_funds = myfactor.newInstance(CPDepositFunds.class);
		CPNetellerPay neteller = myfactor.newInstance(CPNetellerPay.class);
		CPTransactionHistory history = myfactor.newInstance(CPTransactionHistory.class);
		CPMenu menu = myfactor.newInstance(CPMenu.class);

		// switch to cp portal if the account is IB type
		// Navigate to neteller deposit
		menu.goToMenu(CPMenuName.CPPORTAL);

		menu.goToMenu(CPMenuName.DEPOSITFUNDS);
		deposit_funds.navigateToMethod(DEPOSITMETHOD.NETELLER);

		// select an available account with any currency under this method
		for (CURRENCY currency : CURRENCY.values()) {
			account = neteller.checkAccount(currency);
			if (account != null) {
				break;
			}
		}
		assertNotNull(account, "Failed: do not have this currency type account");

		// deposit and check
		neteller.deposit(account, amount, email, notes);
		Assert.assertTrue(neteller.checkIfNavigateToThirdUrl(amount),
				"Submit failure or error occours! Please make sure correct Neteller info configured in disconf (biz_payment.properties)");

		// check deposit history
		neteller.goBack();
		menu.goToMenu(CPMenuName.TRANSACTIONHISTORY);
		Assert.assertTrue(
				history.checkDeposit(account, DEPOSITMETHOD.NETELLER, String.valueOf(amount), STATUS.INCOMPLETE),
				"Do not Find the deposit in history page");
		System.out.println("***Test Neteller deposit succeed!!********");
	}
	@BeforeClass
	@Override
	@Parameters(value= {"TestEnv","headless","Brand","Regulator","TraderURL", "TraderName", "TraderPass","AdminURL","AdminName","AdminPass","Debug"})
	public void beforMethod(@Optional("alpha")String TestEnv, @Optional("False") String headless, String Brand, String Regulator, 
			@Optional("")String TraderURL, @Optional("")String TraderName, @Optional("")String TraderPass,
			@Optional("")String AdminURL, @Optional("")String AdminName, @Optional("")String AdminPass, @Optional("True")String Debug,@Optional("")String server,
				              ITestContext context) {
		// TODO Auto-generated method stub
		launchBrowser( TestEnv,  headless,  Brand,  Regulator, TraderURL, TraderName, TraderPass, AdminURL,  AdminName, AdminPass,  Debug, context);
	}

}

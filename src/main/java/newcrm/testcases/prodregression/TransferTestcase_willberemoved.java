package newcrm.testcases.prodregression;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.List;
import java.util.Random;

import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import newcrm.business.businessbase.CPMenu;
import newcrm.business.businessbase.CPTransactionHistory;
import newcrm.business.businessbase.CPTransfer;
import newcrm.global.GlobalProperties.CPMenuName;
import newcrm.global.GlobalProperties.CURRENCY;
import newcrm.global.GlobalProperties.STATUS;
import newcrm.pages.clientpages.TransferPage.Account;
import newcrm.testcases.BaseTestCase;
import newcrm.testcases.TestDataProvider;

public class TransferTestcase_willberemoved extends BaseTestCase {
	
	private String fr_account;
	private String to_account;
	private String amount;
	
	/**
	 * 覆盖初始方法，按需启动
	 */
	@Override
	public void beforMethod(@Optional("alpha")String TestEnv, @Optional("False") String headless, String Brand, String Regulator, 
			@Optional("")String TraderURL, @Optional("")String TraderName, @Optional("")String TraderPass,
			@Optional("")String AdminURL, @Optional("")String AdminName, @Optional("")String AdminPass, @Optional("True")String Debug,@Optional("")String server,
				              ITestContext context) {
		//do nothing
		//launchBrowser( TestEnv,  headless,  Brand,  Regulator, TraderURL, TraderName, TraderPass, AdminURL,  AdminName, AdminPass,  Debug, context);
	}
	
	protected void funcTransfer() {
		CPMenu menu = myfactor.newInstance(CPMenu.class);
		menu.goToMenu(CPMenuName.TRANSFERACCOUNTS);
		
		CPTransfer tf = myfactor.newInstance(CPTransfer.class);
		CPTransactionHistory history = myfactor.newInstance(CPTransactionHistory.class);
		Random random = new Random();
		int f_size = 0;
		int t_size = 0;
		Boolean find = false;
		CURRENCY currency = null;
		/**
		 * 查找两个相同币种的账号，选择余额多的作为转出账号，余额小的作为转入账号
		 */
		for(CURRENCY c: CURRENCY.values()) {
			List<Account> accs = tf.getFromAccountByCurrency(c);
			f_size = accs.size();
			//need two same currency account
			if(f_size > 1) {
				Double max = 0.0;
				for(Account acc:accs) {
					Double balance =Double.valueOf(acc.getBalance()); 
					if(balance>max) {
						currency = c;
						max = balance;
						fr_account = acc.getAccNumber();
						int t_amount =0;
						while((t_amount = random.nextInt(balance.intValue()+1))==0) {
							//do nothing
						};
						amount = String.valueOf(t_amount)+".00";
						find = true;
					}
				}
			}
			if(find) {
				break;
			}
		}
		assertTrue(find,"Do not find available account");
		tf.setFromAccount(fr_account);
		List<Account> to_accs = tf.getToAccount();
		t_size = to_accs.size();
		assertEquals(f_size, t_size+1,"Find from account("+currency+"): " + f_size + ", and to account size: " + t_size );
		
		to_account = to_accs.get(random.nextInt(t_size)).getAccNumber();
		
		
		tf.setToAccount(to_account);
		tf.setAmount(amount);
		tf.submit();
		
		//need check if success
		
		menu.goToMenu(CPMenuName.TRANSACTIONHISTORY);
		assertTrue(history.checkTransfer(fr_account, to_account, amount, STATUS.PAID));
	}
	
	
	@Test(dataProvider="leverageUserProvider",dataProviderClass=TestDataProvider.class)
	public void testTransfer(@Optional("alpha")String TestEnv, @Optional("False") String headless, String Brand, String Regulator, 
			@Optional("")String TraderURL, @Optional("")String TraderName, @Optional("")String TraderPass,
			@Optional("")String AdminURL, @Optional("")String AdminName, @Optional("")String AdminPass, @Optional("True")String Debug,
				              ITestContext context) {
		launchBrowser( TestEnv,  headless,  Brand,  Regulator, TraderURL, TraderName, TraderPass, AdminURL,  AdminName, AdminPass,  Debug, context);
		
		funcTransfer();
	}
	
	@AfterMethod
	public void QuitDriver() {
		driver.quit();
	}
	
}

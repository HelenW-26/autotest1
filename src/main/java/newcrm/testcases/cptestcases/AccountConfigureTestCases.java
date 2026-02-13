package newcrm.testcases.cptestcases;

import static org.testng.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import newcrm.business.businessbase.CPLiveAccounts;
import newcrm.business.businessbase.CPLogin;
import newcrm.factor.Factor;
import newcrm.global.GlobalProperties;
import newcrm.testcases.BaseTestCaseNew;
import org.apache.commons.lang3.StringUtils;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import newcrm.business.businessbase.CPMenu;
import newcrm.global.GlobalProperties.CPMenuName;
import newcrm.global.GlobalProperties.PLATFORM;
import newcrm.pages.clientpages.LiveAccountsPage.Account;
import newcrm.utils.testCaseDescUtils;
import utils.LogUtils;

public class AccountConfigureTestCases extends BaseTestCaseNew {

	public CPMenu menu;
	CPLiveAccounts liveAccounts;
	private AccountManagementTestCases accMgmtTC = new AccountManagementTestCases();
    private Factor myfactor;
    private CPLogin login;
    protected WebDriver driver;

	@BeforeMethod(groups = {"CP_ChangeLeverage"})
	protected void initMethod(Method method) {

        login = getLogin();
        myfactor = getFactorNew();
        driver = getDriverNew();

		menu = myfactor.newInstance(CPMenu.class);
//		checkValidLoginSession();
		login.goToCpHome();
		menu.goToMenu(CPMenuName.CPPORTAL);
		menu.changeLanguage("English");
		menu.goToMenu(CPMenuName.HOME);
	}

	@AfterMethod(alwaysRun=true)
	public void tearDown(ITestResult result) {
		// Close previous left open dialog if any
		if (liveAccounts != null) {
			liveAccounts.checkExistsDialog();
		}
	}

	public void testChangeAccountLeverage(PLATFORM platform) {
		liveAccounts = myfactor.newInstance(CPLiveAccounts.class);
		GlobalProperties.platform = platform.toString();

		System.out.println("***Change Account Leverage***");
		List<Account> accountList = accMgmtTC.redirectToLivePage(platform, menu, liveAccounts);

		// Random select an account
		Account acc = liveAccounts.randomSelectAccount(accountList);

		// Set leverage
		liveAccounts.clickAccountLeverageBtn(platform, acc.getAccountNum());
		String oldLeverage = acc.getLeverage().replace(" ", "");
		String newLeverage = liveAccounts.setAccountLeverage(oldLeverage).replace(" ", "");

		// Submit
		Map.Entry<Boolean, String> resp = liveAccounts.submitLeverage();
		assertTrue(resp.getKey(), "Failed to change leverage for account " + acc.getAccountNum() + ". Resp Msg: " + resp.getValue());

		// Close leverage dialog
		liveAccounts.closeDialog();

		// Check new value update status
		System.out.println("***Check Account Leverage Update Status***");

		int maxRetries = 3;

		for (int attempt = 1; attempt <= maxRetries; attempt++) {

			menu.refresh();
			menu.waitLoading();

			accountList = accMgmtTC.redirectToLivePage(platform, menu, liveAccounts);

			for(Account c: accountList) {
				String accNum = c.getAccountNum();
				if(c.getAccountNum().equals(acc.getAccountNum())) {
					String accLatestLeverage = c.getLeverage().replace(" ", "");
					System.out.println("Account: " + accNum + ", Leverage: " + accLatestLeverage + ", Expected Leverage: " + newLeverage);

					if(StringUtils.containsIgnoreCase(accLatestLeverage, newLeverage)) {
						System.out.println("Account Latest Info:");
						c.printAccount();
						System.out.println("***Test Change " + platform + " leverage succeed!!********");
						return;
					}
					break;
				}
			}

			if (attempt != maxRetries) {
				LogUtils.info("Retry fetching leverage update status on attempt " + attempt);
			}
		}

		LogUtils.info("Slow update from server. Please manually verify the leverage value again in a short while.");
		Assert.fail("Slow update from server. Please manually verify the leverage value again in a short while.");
	}

}

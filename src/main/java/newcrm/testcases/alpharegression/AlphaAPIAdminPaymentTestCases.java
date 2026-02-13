package newcrm.testcases.alpharegression;

import newcrm.adminapi.AdminAPIPayment;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.listeners.AllureTestListener;
import newcrm.testcases.TestDataProvider;
import newcrm.testcases.admintestcases.AdminPaymentTestCases;
import newcrm.utils.testCaseDescUtils;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;


public class AlphaAPIAdminPaymentTestCases extends AdminPaymentTestCases {
	private AllureTestListener allureTestListener = new AllureTestListener();
	@BeforeTest(alwaysRun = true)
	@Parameters({"Brand", "Server"})
	public void initEnv(String brand, String server, ITestContext context) {
		brand = GlobalMethods.setEnvValues(brand);

		adminData = TestDataProvider.getAlphaRegUsersData(brand, server);
		ccData = TestDataProvider.getAlphaAPPRegUsersData(brand, server);
		lbtData = TestDataProvider.getAlphaLBTUsersData(brand);
		wdUserData = TestDataProvider.getAlphaAPIWithdrawUsersData(brand,server);
		transferData = TestDataProvider.getAlphaAPIAccTransferUsersData(brand,server);
		adminPaymentAPI = new AdminAPIPayment((String) adminData[0][4], GlobalProperties.REGULATOR.valueOf((String)adminData[0][0]),(String)adminData[0][7],(String)adminData[0][8],GlobalProperties.BRAND.valueOf(brand.toUpperCase()), GlobalProperties.ENV.valueOf("ALPHA"));
	}
	@AfterMethod
	public void tearDown(ITestResult result) {
		// 在测试结束后上报错误信息
		allureTestListener.logTestFailure(result);
	}
	@Test(priority = 0, description = testCaseDescUtils.APAPIPAYMENT_DEPOSITAUDIT,groups={"API_Payment_Deposit"})
	public void testAPIAdminDepositAudit() {
		try {
			this.apiAdminDepositAuditPage();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Test(priority = 0, description = testCaseDescUtils.APAPIPAYMENT_WITHDRAWAUDIT,groups={"API_Payment_Withdraw"})
	@Parameters({"Brand"})
	public void testAPIAdminWithdrawalAudit(String brand) {
		try {
			brand = GlobalMethods.setEnvValues(brand);

			this.apiAdminWithdrawAuditPage(GlobalProperties.ENV.valueOf("ALPHA"), GlobalProperties.BRAND.valueOf(brand.toUpperCase()), GlobalProperties.REGULATOR.valueOf((String)adminData[0][0]));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Test(priority = 0, description = testCaseDescUtils.APAPIPAYMENT_DPWDREPORT,groups={"API_Admin_PaymentReport"})
	public void testAPIAdminDepWithReport() {
		try {
			this.apiAdminDepWithReportPage();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Test(priority = 0, description = testCaseDescUtils.APAPIPAYMENT_FINANCIALINFOAUDIT)
	@Parameters({"Brand"})
	public void testAPIAdminFinancialInfoAudit(String brand) {
		try {
			this.apiAdminFinancialInfoAuditPage(brand);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Test(priority = 0, description = testCaseDescUtils.APAPIPAYMENT_ACCTRANSFERAUDIT)
	public void testAPIAdminAcctTransferAudit() {
		try {
			this.apiAdminAccountTransferAuditPage();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Test(priority = 0, description = testCaseDescUtils.APAPIPAYMENT_CCTRANSACTIONAUDIT)
	public void testAPIAdminCCTransactionAudit() {
		try {
			this.apiAdminCCTransactionAuditPage();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Test(priority = 0, description = testCaseDescUtils.APAPIPAYMENT_CCARCHIVEAUDIT)
	public void testAPIAdminCCArchiveAudit() {
		try {
			this.apiAdminCCArchiveAuditPage();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Test(priority = 0, description = testCaseDescUtils.APAPIPAYMENT_CASHADJUSTMENTAUDIT)
	public void testAPIAdminCashAdjustmentAudit() {
		try {
			this.apiAdminCashAdjustmentAuditPage();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Test(priority = 0, description = testCaseDescUtils.APAPIPAYMENT_LBTTRANSACTIONAUDIT)
	public void testAPIAdminLBTTransactionAudit() {
		try {
			this.apiAdminLBTTransactionAuditPage();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Test(priority = 0, description = testCaseDescUtils.APAPIPAYMENT_UNIONPAYWITHDRAWAL)
	@Parameters({"Brand"})
	public void testAPIAdminUnionPayWithdrawal(String brand) {
		if(unionPayBrand.contains(brand.toUpperCase())) {
			try {
				this.apiAdminUnionPayWithdrawalPage();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}else {
			throw new SkipException("Brand: "+ brand.toUpperCase() +" do not support Unionpay, skipping this test intentionally.");
		}
	}

	@Test(priority = 0, description = testCaseDescUtils.APAPIPAYMENT_CACHEMANAGEMENT)
	public void testAPIAdminPaymentCacheMgmt() {
		try {
			this.apiAdminPaymentCacheMgmtPage();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Test(priority = 0, description = testCaseDescUtils.APAPIPAYMENT_AUTOAUDITSWITCH)
	@Parameters({"Brand"})
	public void testAPIAdminAutoAuditSwitch(String brand) {
		try {
			this.apiAdminAutoAuditSwitchPage(brand);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Test(priority = 0, description = testCaseDescUtils.APAPIPAYMENT_EXCHANGERATE)
	public void testAPIAdminExchangeRate() {
		try {
			this.apiAdminExchangeRatePage();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Test(priority = 0, description = testCaseDescUtils.APAPIPAYMENT_DAVINCI)
	public void testAPIAdminDavinciPayment() {
		try {
			this.apiAdminDavinciPage();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}

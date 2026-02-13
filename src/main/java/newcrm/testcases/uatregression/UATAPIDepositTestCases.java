package newcrm.testcases.uatregression;

import newcrm.adminapi.AdminAPIPayment;
import newcrm.cpapi.PCSAPIDeposit;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.testcases.TestDataProvider;
import newcrm.testcases.UATTestDataProvider;
import newcrm.testcases.crmapi.CPAPIDepositTestcases;
import newcrm.utils.testCaseDescUtils;
import org.testng.ITestContext;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import utils.Listeners.MethodTracker;

public class UATAPIDepositTestCases extends CPAPIDepositTestcases {

	@BeforeTest(alwaysRun = true)
	@Parameters({"Brand", "Server"})
	public void initEnv(String brand, String server, ITestContext context) {
		this.initbrand = GlobalMethods.setEnvValues(brand);
		this.initserver = server;

		data = UATTestDataProvider.getUATAPIDepositUsersData(this.initbrand, this.initserver);
		pcsapi = new PCSAPIDeposit((String) data[0][3],(String) data[0][1],(String) data[0][2]);
		Object[][] adminData = UATTestDataProvider.getUATRegUsersData(brand, server);

		adminPaymentAPI = new AdminAPIPayment((String) adminData[0][4], GlobalProperties.REGULATOR.valueOf((String)adminData[0][0]),(String)adminData[0][7],(String)adminData[0][8],GlobalProperties.BRAND.valueOf(initbrand.toUpperCase()), GlobalProperties.ENV.valueOf("ALPHA"));
		adminPaymentAPI.apiDisableLoginWithdrawal2FA();
	}

	@Test(priority = 5,groups={"API_Payment_Deposit"})
	@Parameters(value= {"Brand"})
	public void testWEBApiDeposit(String brand) {

		try {
			// MethodTracker for 多层API call
			if("mo".equalsIgnoreCase(brand)) {
				MethodTracker.trackMethodExecution(this, "apiVoletDepositNew", true, null);
				MethodTracker.trackMethodExecution(this, "apiMsiaBTXpayDepositNew", true, null);
//				MethodTracker.trackMethodExecution(this, "apiUnionPayDepositNew", true, null);
			}
			else if("um".equalsIgnoreCase(brand))
			{
				MethodTracker.trackMethodExecution(this, "apiThaiBTDepositNew", true, null);
				MethodTracker.trackMethodExecution(this, "apiCryptoTRCDepositNew", true, null);
				MethodTracker.trackMethodExecution(this, "apiUnionPayDepositNew", true, null);
			}
			else if("vjp".equalsIgnoreCase(brand))
			{
				//uat missing, this.apiJapanEwalletNew();
				//uat missing, this.apiSlashDepositNew();

//				MethodTracker.trackMethodExecution(this, "apiBitWalletDepositNew", true, null);
				MethodTracker.trackMethodExecution(this, "apiJapanBTDepositNew", true, null);
				MethodTracker.trackMethodExecution(this, "apiJapanEmoneyDepositNew", true, null);
			}
			else if("star".equalsIgnoreCase(brand))
			{
				//missing suddenly this.apiBrazilBTPagsmileDeposit();
				MethodTracker.trackMethodExecution(this, "apiSticpayDepositNew", true, null);
				MethodTracker.trackMethodExecution(this, "apiBanxaDepositNew", true, null);
				MethodTracker.trackMethodExecution(this, "apiUnionPayDepositNew", true, null);
			}
			else if("vfx".equalsIgnoreCase(brand) | "au".equalsIgnoreCase(brand))
			{
				//change msia to vietnam, to philipine
//				MethodTracker.trackMethodExecution(this, "apiSkrillpayDepositNew", true, null);
				MethodTracker.trackMethodExecution(this, "apiTygaPayDepositNew", true, null);
//				MethodTracker.trackMethodExecution(this, "apiBitWalletDepositNew", true, null);
				MethodTracker.trackMethodExecution(this, "apiPhpBTDepositNew", true, null);
			}
			else if ("vt".equalsIgnoreCase(brand))
			{
//				MethodTracker.trackMethodExecution(this, "apiSkrillpayDepositNew", true, null);
//				MethodTracker.trackMethodExecution(this, "apiNetellerDepositNew", true, null);
				MethodTracker.trackMethodExecution(this, "apiMsiaEwalletNew", true, null);
				MethodTracker.trackMethodExecution(this, "apiMsiaFXpayDepositNew", true, null);
				MethodTracker.trackMethodExecution(this, "apiUnionPayDepositNew", true, null);
				//missing LBT - this.apiMsiaBTXpayDepositNew();
			}
			else { //PU
				MethodTracker.trackMethodExecution(this, "apiSkrillpayDepositNew", true, null);
				MethodTracker.trackMethodExecution(this, "apiNetellerDepositNew", true, null);
				MethodTracker.trackMethodExecution(this, "apiVietBTDepositNew", true, null);
				//sudden missing this.apiSticpayDepositNew();
				//sudden missing this.apiAirTMDepositNew();
				//unable submit, this.apiVietMomoDepositNew();
			}

			MethodTracker.checkResultFail();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Test(priority = 0, description = testCaseDescUtils.CPAPIDEPOSIT_HISTORY)
	public void testAPICPDepositOthers() throws Exception {
		apiDPOthersInfo();
	}

}

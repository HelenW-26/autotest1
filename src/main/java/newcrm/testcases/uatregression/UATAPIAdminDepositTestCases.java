package newcrm.testcases.uatregression;

import newcrm.adminapi.AdminAPIPayment;
import newcrm.cpapi.PCSAPIDeposit;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.testcases.UATTestDataProvider;
import newcrm.testcases.crmapi.CPAPIDepositTestcases;
import newcrm.utils.testCaseDescUtils;
import org.testng.ITestContext;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import utils.Listeners.MethodTracker;

public class UATAPIAdminDepositTestCases extends CPAPIDepositTestcases {
    @BeforeTest(alwaysRun = true)
    @Parameters({"Brand", "Server"})
    public void initEnv(String brand, String server, ITestContext context) {
        this.initbrand = GlobalMethods.setEnvValues(brand);
        this.initserver = server;

        data = UATTestDataProvider.getUATAPIAdminDepositUsersData(this.initbrand, this.initserver);
        pcsapi = new PCSAPIDeposit((String) data[0][3],(String) data[0][1],(String) data[0][2]);
        Object[][] adminData = UATTestDataProvider.getUATRegUsersData(brand, server);

        adminPaymentAPI = new AdminAPIPayment((String) adminData[0][4], GlobalProperties.REGULATOR.valueOf((String)adminData[0][0]),(String)adminData[0][7],(String)adminData[0][8],GlobalProperties.BRAND.valueOf(initbrand.toUpperCase()), GlobalProperties.ENV.valueOf("UAT"));
        adminPaymentAPI.apiDisableLoginWithdrawal2FA();
    }

    @Test(priority = 1,description = testCaseDescUtils.CPAPIDEPOSIT_APPROVE_DEPOSIT_FAIL_PENDING)
    @Parameters(value= {"Brand"})
    public void testAPICPPendingOrFailedDeposit(String  brand) {
        //pending or failed  deposit
        try {
            // MethodTracker for 多层API call
            if("mo".equalsIgnoreCase(brand)) {
                MethodTracker.trackMethodExecution(this, "apiPendingOrFailedVoletDepositNew", true, null);
                MethodTracker.trackMethodExecution(this, "apiMsiaBTXpayPendingOrFailedDepositNew", true, null);
                MethodTracker.trackMethodExecution(this, "apiPendingOrFailedUnionPayDepositNew", true, null);
            }
            else if("um".equalsIgnoreCase(brand))
            {
                MethodTracker.trackMethodExecution(this, "apiPendingOrFailedThaiBTDepositNew", true, null);
                MethodTracker.trackMethodExecution(this, "apiPendingOrFailedCryptoTRCDepositNew", true, null);
                MethodTracker.trackMethodExecution(this, "apiPendingOrFailedUnionPayDepositNew", true, null);
            }
            else if("vjp".equalsIgnoreCase(brand))
            {

                MethodTracker.trackMethodExecution(this, "apiPendingOrFailedBitWalletDepositNew", true, null);
                MethodTracker.trackMethodExecution(this, "apiPendingOrFailedJapanBTDepositNew", true, null);
                MethodTracker.trackMethodExecution(this, "apiPendingOrFailedJapanEmoneyDepositNew", true, null);
            }
            else if("star".equalsIgnoreCase(brand))
            {
                MethodTracker.trackMethodExecution(this, "apiPendingOrFailedSticpayDepositNew", true, null);
                MethodTracker.trackMethodExecution(this, "apiPendingOrFailedBanxaDepositNew", true, null);
                MethodTracker.trackMethodExecution(this, "apiPendingOrFailedUnionPayDepositNew", true, null);

            }
            else if("vfx".equalsIgnoreCase(brand) || "au".equalsIgnoreCase(brand))
            {

                MethodTracker.trackMethodExecution(this, "apiTygaPayPendingOrFailingDepositNew", true, null);
                MethodTracker.trackMethodExecution(this, "apiPendingOrFailedBitWalletDepositNew", true, null);
                MethodTracker.trackMethodExecution(this, "apiPendingOrFailedPhpBTDepositNew", true, null);
            }
            else if ("vt".equalsIgnoreCase(brand))
            {
                MethodTracker.trackMethodExecution(this, "apiPendingOrFailedMsiaFXpayDepositNew", true, null);
                MethodTracker.trackMethodExecution(this, "apiPendingOrFailedMsiaEwalletNew", true, null);
                MethodTracker.trackMethodExecution(this, "apiPendingOrFailedUnionPayDepositNew", true, null);



            }
            else { //PU
                MethodTracker.trackMethodExecution(this, "apiPendingOrFailedSkrillpayDepositNew", true, null);
                MethodTracker.trackMethodExecution(this, "apiPendingOrFailedNetellerDepositNew", true, null);
                MethodTracker.trackMethodExecution(this, "apiPendingOrFailedVietBTDepositNew", true, null);

            }

            MethodTracker.checkResultFail();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Test(priority = 1, description = testCaseDescUtils.CPAPIDEPOSIT_APPROVE_DEPOSIT_SUCCESS)
    @Parameters(value= {"Brand"})
    public void testAPICPSuccessDeposit(String  brand) {
        //success deposit
        try {
            // MethodTracker for 多层API call
            if("mo".equalsIgnoreCase(brand)) {
                MethodTracker.trackMethodExecution(this, "apiAuditSuccessVoletDepositNew", true, null);
                MethodTracker.trackMethodExecution(this, "apiAuditSuccessMsiaBTXpayDepositNew", true, null);
                MethodTracker.trackMethodExecution(this, "apiAuditSuccessUnionPayDepositNew", true, null);
            }
            else if("um".equalsIgnoreCase(brand))
            {
                MethodTracker.trackMethodExecution(this, "apiAuditSuccessThaiBTDepositNew", true, null);
                MethodTracker.trackMethodExecution(this, "apiAuditSuccessCryptoTRCDepositNew", true, null);
                MethodTracker.trackMethodExecution(this, "apiAuditSuccessUnionPayDepositNew", true, null);
            }
            else if("vjp".equalsIgnoreCase(brand))
            {

                MethodTracker.trackMethodExecution(this, "apiAuditSuccessBitWalletDepositNew", true, null);
                MethodTracker.trackMethodExecution(this, "apiAuditSuccessJapanBTDepositNew", true, null);
                MethodTracker.trackMethodExecution(this, "apiAuditSuccessJapanEmoneyDepositNew", true, null);
            }
            else if("star".equalsIgnoreCase(brand))
            {
                MethodTracker.trackMethodExecution(this, "apiAuditSuccessSticpayDepositNew", true, null);
                MethodTracker.trackMethodExecution(this, "apiAuditSuccessBanxaDepositNew", true, null);
                MethodTracker.trackMethodExecution(this, "apiAuditSuccessUnionPayDepositNew", true, null);

            }
            else if("vfx".equalsIgnoreCase(brand) || "au".equalsIgnoreCase(brand))
            {

                MethodTracker.trackMethodExecution(this, "apiAuditSuccessTygaPayDepositNew", true, null);
                MethodTracker.trackMethodExecution(this, "apiAuditSuccessBitWalletDepositNew", true, null);
                MethodTracker.trackMethodExecution(this, "apiAuditSuccessBrazilBTXpayDepositNew", true, null);

//                MethodTracker.trackMethodExecution(this, "apiAuditSuccessPhpBTDepositNew", true, null);
            }
            else if ("vt".equalsIgnoreCase(brand))
            {
                MethodTracker.trackMethodExecution(this, "apiAuditSuccessMsiaFXpayDepositNew", true, null);
                MethodTracker.trackMethodExecution(this, "apiAuditSuccessMsiaEwalletNew", true, null);
                MethodTracker.trackMethodExecution(this, "apiAuditSuccessUnionPayDepositNew", true, null);



            }
            else { //PU
                MethodTracker.trackMethodExecution(this, "apiAuditSuccessSkrillpayDepositNew", true, null);
                MethodTracker.trackMethodExecution(this, "apiAuditSuccessNetellerDepositNew", true, null);
                MethodTracker.trackMethodExecution(this, "apiAuditSuccessVietBTDepositNew", true, null);

            }

            MethodTracker.checkResultFail();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test(priority = 2, description = testCaseDescUtils.CPAPIDEPOSIT_CASH_ADJUSTMENT)
    public void testAPICashAdjustmentDeposit() throws Exception {
        apiCrashAdjustmentDeposit();
    }

    @Test(priority = 2, description = testCaseDescUtils.CPAPIDEPOSIT_CREDIT_ADJUSTMENT)
    public void testAPICashAdjustmentCreditIn() throws Exception {
        apiCreditAdjustmentDeposit();
    }

}

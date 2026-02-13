package newcrm.testcases.alpharegression;

import newcrm.cpapi.CPAPIPaymentOthers;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties.*;
import newcrm.listeners.AllureTestListener;
import newcrm.testcases.TestDataProvider;
import newcrm.testcases.crmapi.CPAPIPaymentOthersTestCases;
import newcrm.utils.testCaseDescUtils;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class AlphaAPIPaymentOthersTestCases extends CPAPIPaymentOthersTestCases {
    private AllureTestListener allureTestListener = new AllureTestListener();
    @BeforeTest(alwaysRun = true)
    @Parameters({"Brand", "Server"})
    public void initEnv(String brand, String server, ITestContext context) {
        this.initbrand = GlobalMethods.setEnvValues(brand);
        this.initserver = server;

        data = TestDataProvider.getAlphaAPIAccTransferUsersData(this.initbrand, this.initserver);
        ibData = TestDataProvider.getAlphaAPIWithdrawTransferUsersDataIB(this.initbrand, this.initserver);
        cpAPIPaymentOthers = new CPAPIPaymentOthers((String) data[0][2], (String) data[0][0], (String) data[0][1]);
        ibAPIPaymentOthers = new CPAPIPaymentOthers((String) ibData[0][3], (String) ibData[0][1], (String) ibData[0][2], BRAND.getBRAND(initbrand), REGULATOR.getREGULATOR((String) ibData[0][0]));
    }
    @AfterMethod
    public void tearDown(ITestResult result) {
        // 在测试结束后上报错误信息
        allureTestListener.logTestFailure(result);
    }
    @Test(priority = 0, description = testCaseDescUtils.CPAPITRANSFER_TRADINGACCOUNT)
    public void testAPICPTransfer() throws Exception {
        apiTransfer();
        apiTransferOthers();
    }

    @Test(priority = 0, description = testCaseDescUtils.IBAPITRANSFER_OWNANDSUBIB_OTHERSAPI)
    public void testAPIIBTransfer() throws Exception {
        apiTransferOwnIB();
        apiTransferSubIBClientIB();
        apiTransferOthersIB();
    }
}


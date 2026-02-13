package newcrm.testcases.uatregression.IB.Account;

import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.listeners.AllureTestListener;
import newcrm.testcases.TestDataProvider;
import newcrm.testcases.UATTestDataProvider;
import newcrm.testcases.ibapi.IBPAPIAgreementTestcases;
import newcrm.utils.testCaseDescUtils;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class UATIBAPIAgreementTestCases extends IBPAPIAgreementTestcases {
    private AllureTestListener allureTestListener = new AllureTestListener();

    private String initbrand;
    protected String ibLogin, ibPass;

    @BeforeTest(alwaysRun = true)
    @Parameters({"Brand", "Server"})
    public void initEnv(String brand, String server, ITestContext context) {
        this.initbrand = GlobalMethods.setEnvValues(brand);
        this.initserver = server;

        data = UATTestDataProvider.getUATAPIIBAgreementUsersData(this.initbrand, this.initserver);

    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        // 在测试结束后上报错误信息
        allureTestListener.logTestFailure(result);
    }



    @Test(priority = 2, description = testCaseDescUtils.API_IB_REBATE_AGREEMENT)
    public void testApiSignedIBRebateAgreement() throws Exception {
        apiSignedIBRebateAgreement(GlobalProperties.ENV.UAT,initbrand);
    }
}

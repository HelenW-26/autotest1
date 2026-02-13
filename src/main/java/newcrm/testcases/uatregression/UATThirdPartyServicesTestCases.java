package newcrm.testcases.uatregression;

import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties.ENV;
import newcrm.global.GlobalProperties.BRAND;
import newcrm.listeners.AllureTestListener;
import newcrm.testcases.UATTestDataProvider;
import newcrm.testcases.crmapi.APIThirdPartyServiceTestcases;
import newcrm.utils.testCaseDescUtils;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class UATThirdPartyServicesTestCases extends APIThirdPartyServiceTestcases {
    private AllureTestListener allureTestListener = new AllureTestListener();

    private String initbrand;
    private ENV env;

    @BeforeTest(alwaysRun = true)
    @Parameters({"Brand", "Server"})
    public void initEnv(String brand, String server, ITestContext context) {
        this.initbrand = GlobalMethods.setEnvValues(brand);
        this.initserver = server;

        env = ENV.UAT;
        data = UATTestDataProvider.getUATAPIThirdPartyUserData(this.initbrand, this.initserver);
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        // 在测试结束后上报错误信息
        allureTestListener.logTestFailure(result);
    }



    @Test(priority = 2, description = testCaseDescUtils.THIRDPARTY_MSGCENTER_PHONEOTP,groups={"API_ThirdParty_MessageCenter"})
    public void testMsgCenterSendAndVerifyPhoneOTP() throws Exception {
        apiMsgCenterSendAndVerifyPhoneOTP(env, BRAND.valueOf(initbrand));
    }
}

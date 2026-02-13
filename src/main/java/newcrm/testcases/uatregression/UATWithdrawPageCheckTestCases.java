package newcrm.testcases.uatregression;

import newcrm.adminapi.AdminAPIPayment;
import newcrm.cpapi.CPAPIWithdraw;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.testcases.UATTestDataProvider;
import newcrm.testcases.cptestcases.WithdrawTestCases;
import newcrm.utils.testCaseDescUtils;
import org.testng.ITestContext;
import org.testng.SkipException;
import org.testng.annotations.*;
import utils.LogUtils;

import static org.testng.Assert.assertNotNull;

public class UATWithdrawPageCheckTestCases extends WithdrawTestCases {
    Object data[][];
    Object adminData[][];
    @Override
    public void beforMethod(@Optional("uat")String TestEnv, @Optional("False") String headless, String Brand, String Regulator,
                            @Optional("")String TraderURL, @Optional("")String TraderName, @Optional("")String TraderPass,
                            @Optional("")String AdminURL, @Optional("")String AdminName, @Optional("")String AdminPass, @Optional("True")String Debug, @Optional("")String server,
                            ITestContext context) {
        launchBrowser( TestEnv,  headless,  Brand,  Regulator, TraderURL, TraderName, TraderPass, AdminURL,  AdminName, AdminPass,  Debug, context);
    }

    @BeforeClass(alwaysRun = true)
    @Parameters(value= {"Brand","Server"})
    public void initiEnv(String brand,String server, ITestContext context) {
        brand = GlobalMethods.setEnvValues(brand);
        Brand = brand;
        data = UATTestDataProvider.getUATPageUsersData(brand, server);
        assertNotNull(data);
        setData( data);
        adminData = UATTestDataProvider.getUATPageUsersData(brand, server);

        adminPaymentAPI = new AdminAPIPayment((String) data[0][4], GlobalProperties.REGULATOR.valueOf((String)data[0][0]),(String)data[0][7],(String)data[0][8],GlobalProperties.BRAND.valueOf(brand.toUpperCase()), GlobalProperties.ENV.valueOf("UAT"));;
        adminPaymentAPI.apiDisableLoginWithdrawal2FA();
        //取消所有出金
        new CPAPIWithdraw((String)data[0][3],(String)data[0][1],(String)data[0][2]);
        launchBrowser("uat","true",brand,(String)data[0][0],(String)data[0][3],(String)data[0][1],(String)data[0][2],"","","","True",context);

    }

    @AfterClass(alwaysRun = true)
    @Parameters(value= {"Brand","Server"})
//    public void teardown(String brand,String server, ITestContext context) {
//        brand = GlobalMethods.setEnvValues(brand);
//        Brand = brand;
//        data = UATTestDataProvider.getUATPageUsersData(brand, server);
//        assertNotNull(data);
//        Object[][] adminData = UATTestDataProvider.getUATPageUsersData(brand, server);
//
//
//        dbenv = GlobalProperties.ENV.UAT;
//
//        adminPaymentAPI = new AdminAPIPayment((String) data[0][4],
//                GlobalProperties.REGULATOR.valueOf((String)data[0][0]),
//                (String)data[0][7],(String)data[0][8],
//                GlobalProperties.BRAND.valueOf(brand.toUpperCase()),
//                GlobalProperties.ENV.valueOf("UAT"));;
//        //批量更新出金risk audit状态为Accepted
//        adminPaymentAPI.batchUpdateSRCRiskRecord(GlobalProperties.ENV.valueOf("UAT"),
//                GlobalProperties.BRAND.valueOf(brand.toUpperCase()),
//                GlobalProperties.REGULATOR.valueOf((String)adminData[0][0]));
//    }

    @Test(priority = 3, description = testCaseDescUtils.CPWITHDRAW_PROCESSFLOW_GLOBAL_MAX_AMOUNT_DESC)
    public void testWithdrawDeductedMaxAmount() {
        if(Brand.equalsIgnoreCase("au")||Brand.equalsIgnoreCase("vfx")){
            withdrawDeductedMaxAmount(Brand);
        }else {
            throw new SkipException("Skipping this test intentionally.");
        }

    }
    @Test(description = testCaseDescUtils.CPAPIWITHDRAW_WITHDRAW_DETAIL_INFO_CHECK)
    public void testWithDrawPageChangeRateCheck() {
        if(!Brand.equalsIgnoreCase("star")){
            withDrawPageChangeRateCheck();
        }else {
            throw new SkipException("Skipping this test intentionally.");
        }

    }

}

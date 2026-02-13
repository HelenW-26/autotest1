package newcrm.testcases.uatregression.IB.Account;

import newcrm.global.GlobalMethods;
import newcrm.testcases.BaseTestCaseNew;
import newcrm.testcases.UATTestDataProvider;
import newcrm.testcases.ibtestcases.IBAccountFCATestCases;
import newcrm.testcases.ibtestcases.IBAccountTestCases;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

public class UATIBAccountFCATestCases extends IBAccountFCATestCases {

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
//        registerGoldTestcases.data= TestDataProvider.getAlphaIBReportUsersData(brand, server);
//        Object[][] data = registerGoldTestcases.data;
//        data = TestDataProvider.getAlphaIBReportUsersData(brand, server);
//        assertNotNull(data);
//        launchBrowser("alpha","false", brand, (String)data[0][0], (String)data[0][3], (String)data[0][1], (String)data[0][2], (String)data[0][4], (String)data[0][7], (String)data[0][8], "True", context);

        data= UATTestDataProvider.getUATIBReportUsersData(brand, server);
        data = data;

        UserConfig config = new UserConfig();
        BaseTestCaseNew.OWSURL = (String)data[0][7];
        OWSName = (String)data[0][10];
        OWSPass = (String)data[0][11];

        initTestData(brand,server,context,"uat","true", data);
        launchBrowser(testEnv,headless, brand, (String)data[0][15], (String)data[0][3], (String)data[0][16], (String)data[0][17], (String)data[0][4], (String)data[0][8], (String)data[0][9], "True", context);

    }

}
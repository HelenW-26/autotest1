package newcrm.testcases.uatregression;

import newcrm.cpapi.CPAPIWalletBase;
import newcrm.global.GlobalMethods;
import newcrm.testcases.TestDataProvider;
import newcrm.testcases.UATTestDataProvider;
import newcrm.testcases.cptestcases.AccountManagementTestCases;
import newcrm.utils.testCaseDescUtils;
import org.testng.ITestContext;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;

public class UATTrasferWalletTestCases extends AccountManagementTestCases {

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
		Object data[][] = UATTestDataProvider.getUATWithdrawLimitUsersData(brand, server);
        assertNotNull(data);
    
        cpWalletapi = new CPAPIWalletBase((String) data[0][3], (String) data[0][1], (String) data[0][2]);
        getFromAccountByAPI();

        launchBrowser("uat","true",brand,(String)data[0][0],(String)data[0][3],(String)data[0][1],(String)data[0][2],"","","","True",context);
    }

    @Test(priority = 2, description = testCaseDescUtils.CPMT2WALLETTRANS)
    @Parameters(value= {"Brand"})
    public void transMT2Wallet(String brand) {
        if ("AU".equalsIgnoreCase(brand) || "VFX".equalsIgnoreCase(brand)){
            transAccMT2Wallet();
        }else{
            throw new SkipException("Brand: "+ brand.toUpperCase() +" do not support wallet, skipping this test intentionally.");
        }

    }

    @Test(priority = 2, description = testCaseDescUtils.CPWALLE2MTTTRANS)
    @Parameters(value= {"Brand"})
    public void transAccWallet2MT(String brand) {
        if ("AU".equalsIgnoreCase(brand) || "VFX".equalsIgnoreCase(brand)){
            transAccWallet2MT();
        }else{
            throw new SkipException("Brand: "+ brand.toUpperCase() +" do not support wallet, skipping this test intentionally.");
        }

    }

    @Test(priority = 2, description = testCaseDescUtils.CPMT2MTWITHCREDIT)
    @Parameters(value= {"Brand"})
    public void transAccMT2MTWithCredit(String brand) {
        if ("AU".equalsIgnoreCase(brand) || "VFX".equalsIgnoreCase(brand) || "UM".equalsIgnoreCase(brand)){
            transAcMT2MTWithCredit();
        }else{
            throw new SkipException("Brand: "+ brand.toUpperCase() +" do not support transfer between account and account, skipping this test intentionally.");
        }

    }
}

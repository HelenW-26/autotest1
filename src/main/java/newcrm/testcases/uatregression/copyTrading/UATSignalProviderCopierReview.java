package newcrm.testcases.uatregression.copyTrading;

import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.testcases.UATTestDataProvider;
import newcrm.testcases.cptestcases.copyTrading.CopyTradingTestCases;
import newcrm.utils.testCaseDescUtils;
import org.testng.ITestContext;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.assertNotNull;

public class UATSignalProviderCopierReview extends CopyTradingTestCases {

    @Override
    public void beforMethod(@Optional("alpha")String TestEnv, @Optional("False") String headless, String Brand, String Regulator,
                            @Optional("")String TraderURL, @Optional("")String TraderName, @Optional("")String TraderPass,
                            @Optional("")String AdminURL, @Optional("")String AdminName, @Optional("")String AdminPass, @Optional("True")String Debug, @Optional("")String server,
                            ITestContext context) {
        launchBrowser( TestEnv,  headless,  Brand,  Regulator, TraderURL, TraderName, TraderPass, AdminURL,  AdminName, AdminPass,  Debug, context);
    }

    @BeforeClass(alwaysRun = true)
    @Parameters(value= {"Brand","Server"})
    public void initiEnv(String brand,String server, ITestContext context) {
        brand = GlobalMethods.setEnvValues(brand);
        Object data[][] = UATTestDataProvider.getUATCopyTradingSignalPrvCopierReviewUserData(brand, server);
        assertNotNull(data);
        launchBrowser("alpha","true",brand,(String)data[0][0],(String)data[0][4],(String)data[0][1],(String)data[0][2],"","","","True",context);
    }

    @Test(description = testCaseDescUtils.CPCOPYTRADING_SIGNALPROVIDER_COPIER_REVIEW, groups = {"CP_CopyTrading_COPYERRIEW"})
    public void signalProviderCopierRvTest() {
        //目前只有AU有copier reviewer,其它品牌加了之后再陆续添加
        List<String> br = Arrays.asList(GlobalProperties.BRAND.VFX.toString());
        if(Brand.equalsIgnoreCase(GlobalProperties.BRAND.VFX.toString()))
        {
            testSignalProviderCopierReview();
        }
        else {
            throw new SkipException("Skipping this test intentionally.");
        }

    }
}

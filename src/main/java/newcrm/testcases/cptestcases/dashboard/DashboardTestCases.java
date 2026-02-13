package newcrm.testcases.cptestcases.dashboard;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import newcrm.business.businessbase.CPLiveAccounts;
import newcrm.business.businessbase.CPLogin;
import newcrm.business.businessbase.CPMenu;
import newcrm.business.businessbase.dashboard.CPDashboard;
import newcrm.cpapi.CPAPIAccount;
import newcrm.factor.Factor;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.testcases.BaseTestCaseNew;
import newcrm.utils.testCaseDescUtils;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class DashboardTestCases extends BaseTestCaseNew {

    protected Object data[][];
    private CPMenu menu;
    private CPLogin login;
    private Factor myfactor;
    private WebDriver driver;
    protected List<String> dashboardExcludeBrands = Arrays.asList(GlobalProperties.BRAND.VT.toString(), GlobalProperties.BRAND.MO.toString());;

    @BeforeMethod(groups= {"CP_Dashboard_Acc_Check", "CP_Dashboard_TradeView_Check"})
    protected void initMethod(Method method) {
        checkBrandAccessible();
        driver = getDriverNew();
        myfactor = getFactorNew();
        login = getLogin();
//        checkValidLoginSession();
        login.goToCpHome();
        menu = myfactor.newInstance(CPMenu.class);
        menu.goToMenu(GlobalProperties.CPMenuName.CPPORTAL);
        menu.changeLanguage("English");
        menu.goToMenu(GlobalProperties.CPMenuName.HOME);
    }

    public void dashboardAccountCheck() throws Exception {
        CPLiveAccounts liveAccounts = myfactor.newInstance(CPLiveAccounts.class);
        CPAPIAccount api = new CPAPIAccount((String)data[0][3], driver);

        // Account Assets
        dashboardAssetsCheckProcess(liveAccounts, api);
        // Trading Account List
        dashboardAccountCheckProcess(liveAccounts, api);

        System.out.println("***Test Dashboard Account Check succeed!!********");
    }

    public void dashboardTradeViewCheck() throws Exception {
        CPDashboard dashboard = myfactor.newInstance(CPDashboard.class);

        dashboard.checkTradeViewContent();

        System.out.println("***Test Dashboard Trade View Check succeed!!********");
    }

    private void dashboardAssetsCheckProcess(CPLiveAccounts liveAccounts, CPAPIAccount api) throws Exception {
        // Get profile user id
        String userId = liveAccounts.getProfileUserId();

        System.out.println("***Account Assets Check***");

        // Get account assets display info
        String assetsCurrencyImgSrc = liveAccounts.getAssetsCurrencyImg();
        String assetsAmount = liveAccounts.getAssetsAmount();
        String assetsCurrency = liveAccounts.getAssetsCurrency();

        if (assetsCurrency == null || assetsCurrency.trim().isEmpty()) {
            Assert.fail("Assets Currency value is empty");
        }

        if (assetsCurrencyImgSrc == null || assetsCurrencyImgSrc.trim().isEmpty()) {
            Assert.fail("Assets Currency Image Data Source value is empty");
        }
        // Get account assets info
        JSONObject result = null;
        try {
            result = api.queryClientTotalEquity(userId, assetsCurrency);
        } catch (Exception ex) {
            Assert.fail("Failed to query account info");
        }

        // Validate account assets info
        liveAccounts.validateAssetsInfo(result, assetsAmount, assetsCurrency, assetsCurrencyImgSrc);
    }

    private void dashboardAccountCheckProcess(CPLiveAccounts liveAccounts, CPAPIAccount api) throws Exception {
        System.out.println("***Trading Account Check***");

        // Get account details
        JSONObject result = api.queryMetaTraderAccountDetail();

        // Validate trading account info
        liveAccounts.validateTradingAccountInfo(result);
    }

    private void checkBrandAccessible() {
        if (dashboardExcludeBrands.contains(GlobalMethods.getBrand().toUpperCase())) {
            throw new SkipException("Skipping this test intentionally.");
        }
    }

}

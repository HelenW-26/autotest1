package newcrm.testcases.cptestcases.download;

import com.alibaba.fastjson.JSONObject;
import newcrm.business.businessbase.CPLogin;
import newcrm.business.businessbase.CPMenu;
import newcrm.business.businessbase.download.CPDownload;
import newcrm.cpapi.CPAPIAccount;
import newcrm.factor.Factor;
import newcrm.global.GlobalProperties.CPMenuName;
import newcrm.global.GlobalProperties.PLATFORM;
import newcrm.testcases.BaseTestCaseNew;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import utils.LogUtils;

import java.lang.reflect.Method;

public class DownloadTestCases extends BaseTestCaseNew {

    protected Object data[][];
    private CPMenu menu;
    private CPLogin login;
    private Factor myfactor;
    private WebDriver driver;

    @BeforeMethod(groups= {"CP_Download"})
    protected void initMethod(Method method) {
        LogUtils.info("**Proceed Before Method Action**");

        if (driver ==null){
            driver = getDriverNew();
        }
        if (myfactor == null){
            myfactor = getFactorNew();
        }
        if (login == null){
            login = getLogin();
        }
//        checkValidLoginSession();
        login.goToCpHome();
        menu = myfactor.newInstance(CPMenu.class);
        menu.goToMenu(CPMenuName.CPPORTAL);
        menu.changeLanguage("English");
        menu.goToMenu(CPMenuName.HOME);
    }

    public void downloadInfoCheck() {
        CPDownload cpDownload = myfactor.newInstance(CPDownload.class);
        CPAPIAccount api = new CPAPIAccount((String)data[0][3], driver);

        System.out.println("***Download Info Check***");
        menu.goToMenu(CPMenuName.DOWNLOADS);

        // Get account assets info
        JSONObject result = null;
        try {
            result = api.querySystemInfo();
        } catch (Exception ex) {
            Assert.fail("Failed to query system info");
        }

        String domainWebSite = result.getString("domainWebSite");
        LogUtils.info("Domain Website: " + domainWebSite);
        cpDownload.setDomainWebSite(domainWebSite);

        System.out.println("***MetaTrader 4 Download Info Check***");
        cpDownload.mt4DownloadInfoCheck(PLATFORM.MT4, dbBrand);

        System.out.println("***MetaTrader 5 Download Info Check***");
        cpDownload.mt5DownloadInfoCheck(PLATFORM.MT5, dbBrand);

        cpDownload.proTraderDownloadInfoCheck(PLATFORM.MT5, dbBrand);

        System.out.println("***Test Download Info Check succeed!!********");
    }

}

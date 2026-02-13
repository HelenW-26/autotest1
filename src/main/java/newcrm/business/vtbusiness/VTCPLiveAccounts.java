package newcrm.business.vtbusiness;

import newcrm.business.businessbase.CPLiveAccounts;
import newcrm.global.GlobalProperties;
import newcrm.pages.clientpages.LiveAccountsPage;
import newcrm.pages.vtclientpages.VTLiveAccountsPage;
import org.openqa.selenium.WebDriver;

import java.util.List;

public class VTCPLiveAccounts extends CPLiveAccounts {

    public VTCPLiveAccounts(WebDriver driver) {
        super(new VTLiveAccountsPage( driver));
        this.page = new VTLiveAccountsPage(driver);
    }

    public VTCPLiveAccounts(VTLiveAccountsPage v_page) {
        super(v_page);
    }

    // 获取账号区域显示的信息
    @Override
    public String getAccountInfo() {

        return page.getAccountInfo();
    }

    @Override
    public void clickDemoAccountBalanceBtn(GlobalProperties.PLATFORM platform, String accNum) {
        page.clickAccountSettingBtn(platform, accNum, true);
        page.clickDemoAccountBalanceBtn(platform, accNum);
    }

    public void clickChgAccountPwdBtn(GlobalProperties.PLATFORM platform, String accNum) {
        page.clickAccountSettingBtn(platform, accNum, false);
        page.clickAccountChangePwdBtn(platform);
        page.submitChgAccountPassword();
    }
    public List<LiveAccountsPage.Account> getCopyTradingAccountWithBalance(GlobalProperties.PLATFORM platform){
        List<LiveAccountsPage.Account> accounts = page.getFirstPageMTSAccountsWithBalance(platform);

        return accounts;
    }

}

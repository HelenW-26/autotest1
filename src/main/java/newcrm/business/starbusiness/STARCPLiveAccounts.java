package newcrm.business.starbusiness;

import newcrm.business.businessbase.CPLiveAccounts;
import newcrm.pages.starclientpages.STARLiveAccountsPage;
import org.openqa.selenium.WebDriver;

public class STARCPLiveAccounts extends CPLiveAccounts {

    public STARCPLiveAccounts(WebDriver driver) {
        super(new STARLiveAccountsPage(driver));
        this.page = new STARLiveAccountsPage(driver);
    }

    // 获取账号区域显示的信息
    public String getAccountInfo() {
        return page.getAccountInfo();
    }

    @Override
    public void setViewContentGridMode() {
        waitLoadingAccountListContent();
    }

    @Override
    public String getAssetsCurrency() {
        page.clickAssetsCurrencyDropdown();
        String currency = page.getAssetsCurrency();
        page.clickAssetsCurrencyDropdown();

        return currency;
    }

    @Override
    public String getProfileUserId() {
        String userId = page.getProfileUserId();

        return userId;
    }

}

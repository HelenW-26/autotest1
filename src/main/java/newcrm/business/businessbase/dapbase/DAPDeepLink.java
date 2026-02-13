package newcrm.business.businessbase.dapbase;

import newcrm.pages.dappages.DAPDashboardPage;
import newcrm.pages.dappages.DAPDeepLinkPage;
import org.openqa.selenium.WebDriver;

public class DAPDeepLink {

    protected DAPDeepLinkPage dapDeepLinkPage;

    public DAPDeepLink(WebDriver driver) {
        this.dapDeepLinkPage = new DAPDeepLinkPage(driver);
    }

    public DAPDeepLink(DAPDeepLinkPage dapDeepLinkPage) {
        this.dapDeepLinkPage = dapDeepLinkPage;
    }

    public String getLiveAccRegistrationDeepLink()  {
        return dapDeepLinkPage.getLiveAccRegistrationDeepLink();
    }


}

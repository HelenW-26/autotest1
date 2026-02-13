package newcrm.business.businessbase.dapbase;

import newcrm.pages.dappages.DAPClientListPage;
import newcrm.pages.dappages.DAPDeepLinkPage;
import org.openqa.selenium.WebDriver;

public class DAPClientList {

    protected DAPClientListPage dapClientListPage;

    public DAPClientList(WebDriver driver) {
        this.dapClientListPage = new DAPClientListPage(driver);
    }

    public DAPClientList(DAPClientListPage dapClientListPage) {
        this.dapClientListPage = dapClientListPage;
    }

    public void verifyClientList()  {
        dapClientListPage.verifyClientList();
    }


}

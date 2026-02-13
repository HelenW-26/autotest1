package newcrm.business.businessbase.dapbase;

import newcrm.pages.dappages.DAPDeepLinkPage;
import newcrm.pages.dappages.DAPPostbackTrackerPage;
import org.openqa.selenium.WebDriver;

public class DAPPostbackTracker {

    protected DAPPostbackTrackerPage dapPostbackTrackerPage;

    public DAPPostbackTracker(WebDriver driver) {
        this.dapPostbackTrackerPage = new DAPPostbackTrackerPage(driver);
    }

    public DAPPostbackTracker(DAPPostbackTrackerPage dapPostbackTrackerPage) {
        this.dapPostbackTrackerPage = dapPostbackTrackerPage;
    }

    public void verifyPostbackURLEventType(String selectedPostbackEventType)  {
        dapPostbackTrackerPage.verifyPostbackURLEventType(selectedPostbackEventType);
    }


}

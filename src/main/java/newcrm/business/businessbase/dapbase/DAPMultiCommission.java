package newcrm.business.businessbase.dapbase;

import newcrm.pages.dappages.DAPCommissionPlanPage;
import newcrm.pages.dappages.DAPMultiCommissionPage;
import org.openqa.selenium.WebDriver;

import java.util.List;
import java.util.Map;

public class DAPMultiCommission {

    protected DAPMultiCommissionPage dapMultiCommissionPage;

    public DAPMultiCommission(WebDriver driver) {
        this.dapMultiCommissionPage = new DAPMultiCommissionPage(driver);
    }

    public DAPMultiCommission(DAPMultiCommissionPage dapMultiCommissionPage) {
        this.dapMultiCommissionPage = dapMultiCommissionPage;
    }

    public Map<String, List<String>> verifyCommissionPlanPage()  {
        return dapMultiCommissionPage.verifyMultiCommissionPage();
    }



}

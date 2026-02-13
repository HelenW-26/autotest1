package newcrm.business.businessbase.dapbase;

import newcrm.pages.dappages.DAPCommissionPlanPage;
import newcrm.pages.dappages.DAPCommissionReportPage;
import org.openqa.selenium.WebDriver;

import java.util.List;
import java.util.Map;

public class DAPCommissionPlan {

    protected DAPCommissionPlanPage dapCommissionPlanPage;

    public DAPCommissionPlan(WebDriver driver) {
        this.dapCommissionPlanPage = new DAPCommissionPlanPage(driver);
    }

    public DAPCommissionPlan(DAPCommissionPlanPage dapCommissionPlanPage) {
        this.dapCommissionPlanPage = dapCommissionPlanPage;
    }

    public void verifyCommissionPlanPage()  {
        dapCommissionPlanPage.verifyCommissionPlanPage();
    }



}

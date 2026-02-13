package newcrm.business.businessbase.dapbase;

import newcrm.pages.dappages.DAPClientListPage;
import newcrm.pages.dappages.DAPCommissionReportPage;
import org.openqa.selenium.WebDriver;

import java.util.List;
import java.util.Map;

public class DAPCommissionReport {

    protected DAPCommissionReportPage dapCommissionReportPage;

    public DAPCommissionReport(WebDriver driver) {
        this.dapCommissionReportPage = new DAPCommissionReportPage(driver);
    }

    public DAPCommissionReport(DAPCommissionReportPage dapCommissionReportPage) {
        this.dapCommissionReportPage = dapCommissionReportPage;
    }

    public void filterCommissionReport_Today()  {
        dapCommissionReportPage.filterCommissionReport_Today();
    }

    public void filterCommissionReport_OneYear()  {
        dapCommissionReportPage.filterCommissionReport_OneYear();
    }

    public List<Double> getCommissionList(){
        return dapCommissionReportPage.getCommissionList();
    }

    public List<String> getStatusList(){
        return dapCommissionReportPage.getStatusList();
    }

    public Map<String, Double> getStatusCommissionMap(){
        return dapCommissionReportPage.getStatusCommissionMap();
    }


}

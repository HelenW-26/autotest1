package newcrm.business.businessbase.owsbase;

import newcrm.pages.owspages.*;
import org.openqa.selenium.WebDriver;


public class OWSCommissionList {

    protected static WebDriver driver;
    protected OWSDashboardPage owsDashboardPage;
    protected OWSCommissionListPage owsCommissionListPage;


    public OWSCommissionList(WebDriver driver) {
        this.owsCommissionListPage = new OWSCommissionListPage(driver);
    }

    public OWSCommissionList(OWSCommissionListPage owsCommissionListPage) {
        this.owsCommissionListPage = owsCommissionListPage;
    }

    public void commissionAdjustment(String cpaName)  {
        owsCommissionListPage.commissionAdjustment(cpaName);
    }




}

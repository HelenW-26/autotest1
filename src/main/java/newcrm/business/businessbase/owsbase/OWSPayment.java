package newcrm.business.businessbase.owsbase;

import newcrm.pages.dappages.DAPPaymentsPage;
import newcrm.pages.owspages.OWSCommissionListPage;
import newcrm.pages.owspages.OWSDashboardPage;
import newcrm.pages.owspages.OWSPaymentPage;
import org.openqa.selenium.WebDriver;


public class OWSPayment {

    protected static WebDriver driver;
    protected OWSPaymentPage owsPaymentsPage;


    public OWSPayment(WebDriver driver) {
        this.owsPaymentsPage = new OWSPaymentPage(driver);
    }

    public OWSPayment(OWSPaymentPage owsPaymentsPage) {
        this.owsPaymentsPage = owsPaymentsPage;
    }

    public void searchPayment_Pending(String cpaName){
        owsPaymentsPage.searchPayment_Pending(cpaName);
    }

    public void auditPayment_Approve()  {
        owsPaymentsPage.auditPayment_Approve();
    }

    public void auditPayment_Reject()  {
        owsPaymentsPage.auditPayment_Reject();
    }



}

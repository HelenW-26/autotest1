package newcrm.business.businessbase.dapbase;

import newcrm.pages.dappages.DAPCommissionReportPage;
import newcrm.pages.dappages.DAPPaymentsPage;
import org.openqa.selenium.WebDriver;

import java.util.List;
import java.util.Map;

public class DAPPayments {

    protected DAPPaymentsPage dapPaymentsPage;

    public DAPPayments(WebDriver driver) {
        this.dapPaymentsPage = new DAPPaymentsPage(driver);
    }

    public DAPPayments(DAPPaymentsPage dapPaymentsPage) {
        this.dapPaymentsPage = dapPaymentsPage;
    }

    public Map<String, Double> getTotalCommissionByType()  {
        return dapPaymentsPage.getTotalCommissionByType();
    }

    public void requestPayment(){
        dapPaymentsPage.requestPayment();
    }


}

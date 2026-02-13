package newcrm.business.businessbase.owsbase;

import newcrm.global.GlobalProperties;
import newcrm.pages.ibpages.IBDemoAccountPage;
import newcrm.pages.owspages.OWSKYCRecordsPage;
import org.openqa.selenium.WebDriver;


public class OWSKYCRecords {

    protected static WebDriver driver;
    protected OWSKYCRecordsPage owsKYCRecordsPage;


    public OWSKYCRecords(WebDriver driver) {
        this.owsKYCRecordsPage = new OWSKYCRecordsPage(driver);
    }

    public OWSKYCRecords(OWSKYCRecordsPage owsKYCRecordsPage) {
        this.owsKYCRecordsPage = owsKYCRecordsPage;
    }

//    //Pass one of the three parameters will be sufficient
//    public void auditTradingAccountFlow(String userId,String clientName, String email)  {
//        owsKYCRecordsPage.searchKYCRecords(userId, clientName, email);
//        owsKYCRecordsPage.auditTradingAccount();
//    }
//
//    //Pass one of the three parameters will be sufficient
//    public void auditPOIFlow(String userId,String clientName, String email)  {
//        owsKYCRecordsPage.searchKYCRecords(userId, clientName, email);
//        owsKYCRecordsPage.auditPOI();
//    }
//
//    //Pass one of the three parameters will be sufficient
//    public void auditPOAFlow(String userId,String clientName, String email)  {
//        owsKYCRecordsPage.searchKYCRecords(userId, clientName, email);
//        owsKYCRecordsPage.auditPOA();
//    }




}

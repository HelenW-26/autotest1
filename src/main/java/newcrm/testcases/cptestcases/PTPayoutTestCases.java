package newcrm.testcases.cptestcases;

import newcrm.business.adminbusiness.AdminMenu;
import newcrm.business.businessbase.CPRegister;
import newcrm.business.ptbusiness.payout.PTCPPayout;
import newcrm.business.ptbusiness.register.PTCPRegister;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.pages.adminpages.IDPOAAuditPage;
import newcrm.pages.adminpages.PTManagementPage;
import newcrm.testcases.BaseTestCase;
import org.testng.annotations.BeforeMethod;

public class PTPayoutTestCases extends BaseTestCase {
    protected String email;
    protected String code;
    CPRegister cp;

    PTRegisterPaymentAndPayoutTestCases register = new PTRegisterPaymentAndPayoutTestCases();
    @Override
    protected void login() {
        GlobalMethods.printDebugInfo("Do not need to login");
    }

    @Override
    @BeforeMethod(alwaysRun = true)
    public void goToCpHomePage() {
        GlobalMethods.printDebugInfo("Do not need go to home page");
    }

    public void ptPayoutTestCases(String sessionNumber,String email, String pwd,String balance, GlobalProperties.PTPaymentMethod paymentMethod,GlobalProperties.ACCOUNTTYPE accountType, GlobalProperties.CURRENCY currency, GlobalProperties.PLATFORM platform) {
        cp = myfactor.newInstance(PTCPRegister.class);
        PTCPPayout payout = myfactor.newInstance(PTCPPayout.class);
        AdminMenu menu = myfactor.newInstance(AdminMenu.class);
        IDPOAAuditPage idpoaAuditPage = new IDPOAAuditPage(driver);
        PTManagementPage ptManagementPage = new PTManagementPage(driver);
        dbBrand = GlobalProperties.BRAND.valueOf(dbBrand.toString().substring(0, dbBrand.toString().length() - 2));
        //instance
        register.updatedb(payout,sessionNumber);

        //set POI/POA
        register.setPOIPOA(accountType, currency, platform,email,pwd);

        // POI/POA audit
        register.ppAudit(menu, idpoaAuditPage,email);

        //risk audit in admin portal
        register.riskAudit(menu, ptManagementPage);

        //updatedb after risk audit
        register.updatedbAfterRiskAudit(payout,balance);

        //payout
        register.payout(payout,paymentMethod,balance,email,pwd);
    }
}


package newcrm.testcases.cptestcases;

import newcrm.business.adminbusiness.AdminLogin;
import newcrm.business.adminbusiness.AdminMenu;
import newcrm.business.businessbase.*;
import newcrm.business.ptbusiness.payout.PTCPPayout;
import newcrm.business.ptbusiness.register.PTCPRegister;
import newcrm.business.pugbusiness.PUGCPLogin;
import newcrm.business.pugbusiness.register.SVGCPRegister;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.pages.adminpages.IDPOAAuditPage;
import newcrm.pages.adminpages.PTManagementPage;
import newcrm.testcases.BaseTestCase;
import newcrm.utils.DepositCallBack;
import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;

import java.util.HashMap;

public class PTRegisterPaymentAndPayoutTestCases extends BaseTestCase {
    protected String code;

    CPRegister cp;
    SVGCPRegister ptCP;

    //data
    String firstName = "autotestPT" + GlobalMethods.getRandomString(10);
    String lastName = "TestCRMPPT";
    String phone = GlobalMethods.getRandomNumberString(10);
    String idnum = "TESTID"+GlobalMethods.getRandomNumberString(10);
    String email = ("autotestPT" + GlobalMethods.getRandomString(8) + "@testcrmautomation.com").toLowerCase();
    String account = null;
    String liveAccount = null;

    @Override
    protected void login() {
        GlobalMethods.printDebugInfo("Do not need to login");
    }

    @Override
    @BeforeMethod(alwaysRun = true)
    public void goToCpHomePage() {
        GlobalMethods.printDebugInfo("Do not need go to home page");
    }

    public HashMap<String, String> ptRegisterPaymentAndPayout(String ibCode, String rafCode, String partnerNum, GlobalProperties.PLATFORM platform, String country
            , GlobalProperties.ACCOUNTTYPE accountType, GlobalProperties.CURRENCY currency, String balance, GlobalProperties.PTPaymentMethod paymentMethod, String openApi, boolean ifPayout,GlobalProperties.PTPaymentMethod payoutMethod) {
        //instance
        cp = myfactor.newInstance(PTCPRegister.class);
       PTCPPayout payout = myfactor.newInstance(PTCPPayout.class);
       AdminMenu menu = myfactor.newInstance(AdminMenu.class);
       IDPOAAuditPage idpoaAuditPage = new IDPOAAuditPage(driver);
       PTManagementPage ptManagementPage = new PTManagementPage(driver);
       dbBrand = GlobalProperties.BRAND.valueOf(dbBrand.toString().substring(0, dbBrand.toString().length() - 2));

        //you couldn't use ibcode and rafcode at same time
        if (ibCode.trim().length() > 0) {
            cp.setIBcode(ibCode.trim(), BaseTestCase.TestEnv.equalsIgnoreCase(GlobalProperties.ENV.PROD.toString()));
        } else {
            if (rafCode.trim().length() > 0) {
                cp.setRAFCode(rafCode);
            }
            else {
                if (partnerNum.trim().length() > 0) {
                    cp.setPartnerNum(partnerNum);
                }
            }
        }
        cp.setTradeUrl(BaseTestCase.TraderURL);

        //input userinfo
        cp.setCountry(country);
        cp.setPTUserInfo(firstName, lastName, phone, email);

        //open an account
        Assert.assertTrue(cp.goToAccountPageForPT(), "Go to Account page failed!");
        cp.fillAccountPage(platform, accountType, currency,balance);

        //payment
        Assert.assertTrue(cp.goToPaymentPageForPT(), "Go to Payment page failed!");
        cp.choosePaymentMethod(paymentMethod.toString());

        //callback
        cp.checkUserInfo(email,dbenv,dbBrand ,dbRegulator);
        ptCallback(openApi,paymentMethod);

        if(ifPayout)
        {
            //Adjustment 2 DB tables, in order to let the account status change to “Verifying”
            updatedb(payout,cp.userdetails.get("sessionNum"));

            //set POI/POA
            setPOIPOA(accountType, currency, platform,cp.userdetails.get("email"),cp.userdetails.get("password"));

            // POI/POA audit
            ppAudit(menu, idpoaAuditPage,email);

            //risk audit in admin portal
            riskAudit(menu, ptManagementPage);

            //update db after risk audit
            updatedbAfterRiskAudit(payout,balance);

            //payout
            payout(payout,payoutMethod,balance,cp.userdetails.get("email"),cp.userdetails.get("password"));

            //payout audit
            //payoutAudit(menu,ptManagementPage);

        }

        //print user info
        GlobalMethods.printDebugInfo("Userid:" + cp.userdetails.get("UserID") + " Password:" + cp.userdetails.get("Password") + " Session Number: " + cp.userdetails.get("sessionNum")
                + " account:" + cp.userdetails.get("account") + " liveAccount:" + cp.userdetails.get("liveAccount")  + " email:" + email);
        return cp.userdetails;
    }

    public void ptCallback( String openApi,GlobalProperties.PTPaymentMethod paymentMethod)
    {
        if(!"".equals(openApi)) {
            DepositCallBack callback = new DepositCallBack(openApi,Brand,Regulator);
            Double fee = Double.parseDouble(cp.userdetails.get("registrationFee"));


            if(StringUtils.containsIgnoreCase(paymentMethod.toString(),"crypto"))
            {
                fee = fee *1.08;
            }

            callback.generateCallback(DepositCallBack.CHANNEL.PTCallBack,cp.userdetails.get("sessionNum"),"",fee,"","","","");
            String response = callback.sendCallback().toLowerCase();
            GlobalMethods.printDebugInfo("response:" + response);
            Assert.assertEquals(response,"ok");
        }
    }

    public void updatedb(PTCPPayout payout,String sessionNum)
    {
        payout.updateDBinfo(dbenv, dbBrand, dbRegulator, sessionNum);

        account =  (payout.getAccountInfo(dbenv, dbBrand, dbRegulator,cp.userdetails.get("sessionNum"))).getJSONObject(0).getString("mt4_account");
        cp.userdetails.put("account",account);
        GlobalMethods.printDebugInfo("account:" + cp.userdetails.get("account"));
    }

    public void setPOIPOA(GlobalProperties.ACCOUNTTYPE accountType, GlobalProperties.CURRENCY currency,GlobalProperties.PLATFORM platform,String email,String password)
    {
        ptCP = new SVGCPRegister(driver,TraderURL);
        login = myfactor.newInstance(PUGCPLogin.class,TraderURL);
        try {
            login.loginOld(email, password);
        }
        catch(Exception e)
        {
            GlobalMethods.printDebugInfo("no need to input username and password to login");
        }

        ptCP.fillPersonalDetailsForPT(idnum, firstName, lastName, phone);

        Assert.assertTrue(ptCP.goToAccountPageNew(), "Go to Account page failed!");
        if (accountType != null && currency != null) {
            ptCP.fillAccountPage(platform, accountType, currency);
        } else {
            ptCP.fillAccountPage(platform);
        }

        //Level 2 & 3
        Assert.assertTrue(ptCP.goToIDPage(), "Go to ID page failed!");
        ptCP.fillIDPage();

        Assert.assertTrue(ptCP.goToFinishPage(), "Go to Finish page failed");
    }

    public void ppAudit(AdminMenu menu,IDPOAAuditPage idpoaAuditPage,String email)
    {
        AdminLogin apLogin = myfactor.newInstance(AdminLogin.class,AdminURL);
        Assert.assertTrue(apLogin.login(AdminName, AdminPass),"Login Failed at launchAdminBrowser method.");

        menu.goToMenu(GlobalProperties.AdminMenuName.POI_POA_AUDIT);
        idpoaAuditPage.poipoaAudit(email);
    }

    public void riskAudit(AdminMenu menu,PTManagementPage ptManagementPage)
    {
        menu.goToMenu(GlobalProperties.AdminMenuName.PT_MGMT);

        ptManagementPage.gotoPTRiskAudit();
        ptManagementPage.approveRiskaudit(cp.userdetails.get("sessionNum"));
    }

    public void updatedbAfterRiskAudit(PTCPPayout payout, String balance) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        liveAccount =  (payout.getLiveAccountInfo(dbenv, dbBrand, dbRegulator,cp.userdetails.get("sessionNum"))).getJSONObject(0).getString("mt4_account");

        cp.userdetails.put("liveAccount",liveAccount);
        GlobalMethods.printDebugInfo("liveAccountHW : " + liveAccount);
        payout.updateDBAfterRiskAudit(dbenv, dbBrand, dbRegulator, cp.userdetails.get("sessionNum"),balance,liveAccount);
    }

    public void payout(PTCPPayout payout,GlobalProperties.PTPaymentMethod payoutMethod,String balance,String email,String password)
    {
        ptCP = new SVGCPRegister(driver,TraderURL);
        login = myfactor.newInstance(PUGCPLogin.class,TraderURL);
        try {
            login.loginOld(email, password);
        }
        catch(Exception e)
        {
            GlobalMethods.printDebugInfo("no need to input username and password to login");
        }
        payout.gotoProTrading();
        payout.gotoDashboard();

        Assert.assertTrue(payout.gotoPayout(),"go to payout page failed");
        payout.choosePayoutMethod(payoutMethod.toString()).get("refund");

        Assert.assertTrue(payout.submitPayout(),"request payout failed");
    }

    public void payoutAudit(AdminMenu menu,PTManagementPage ptManagementPage)
    {
        //admin payout risk audit profit split
        AdminLogin apLogin = myfactor.newInstance(AdminLogin.class,AdminURL);
        try {
            apLogin.login(AdminName, AdminPass);
        }
        catch(Exception e)
        {
            GlobalMethods.printDebugInfo("no need to input username and password to login");
        }

        menu.goToMenu(GlobalProperties.AdminMenuName.PT_MGMT);
        ptManagementPage.gotoPTRiskAudit();
        ptManagementPage.approveRiskProfitSplit(cp.userdetails.get("sessionNum"));

        //reject payout/refund
        ptManagementPage.gotoPTPayout();
        ptManagementPage.rejectPayoutandRefund(cp.userdetails.get("sessionNum"));
    }
}


package newcrm.business.ptbusiness.payout;

import com.alibaba.fastjson.JSONArray;
import newcrm.business.businessbase.CPWithdrawBase;
import newcrm.business.dbbusiness.PTDB;
import newcrm.global.GlobalProperties;
import newcrm.pages.ptclientpages.payout.PTPayouPage;
import org.openqa.selenium.WebDriver;

import java.util.HashMap;


public class PTCPPayout extends CPWithdrawBase {

    protected PTPayouPage payoutPage;
    PTDB ptdb = new PTDB();
    public PTCPPayout(WebDriver driver) {
        super(new PTPayouPage(driver));
        this.payoutPage= new PTPayouPage(driver);
    }

    //Adjustment 2 DB tables, in order to let the account status change to “Verifying”
    public void updateDBinfo(GlobalProperties.ENV env, GlobalProperties.BRAND brand, GlobalProperties.REGULATOR regulator,String sessionNumber)
    {
        ptdb.updatestageidforsession(env,brand,regulator,sessionNumber);
        ptdb.updatestageidforaccount(env,brand,regulator,sessionNumber);

    }

    public void updateDBAfterRiskAudit(GlobalProperties.ENV env, GlobalProperties.BRAND brand, GlobalProperties.REGULATOR regulator,String sessionNum,String balance,String liveAccount)
    {
        ptdb.updateBalanceforAccount(env,brand,regulator,sessionNum,balance,liveAccount);
        ptdb.updateBananceForSession(env,brand,regulator,sessionNum,balance);

    }

    //get account number from sessionNumber
    public JSONArray getAccountInfo(GlobalProperties.ENV env, GlobalProperties.BRAND brand, GlobalProperties.REGULATOR regulator,String sessionNumber)
    {
        return ptdb.getAccountFromDB(env,brand,regulator,sessionNumber);

    }

    public JSONArray getLiveAccountInfo(GlobalProperties.ENV env, GlobalProperties.BRAND brand, GlobalProperties.REGULATOR regulator,String sessionNumber)
    {
        return ptdb.getLiveAccountFromDB(env,brand,regulator,sessionNumber);

    }

    public void gotoProTrading() {
        payoutPage.gotoProTrading();
    }

    public void gotoDashboard() {
        payoutPage.gotoDashboard();
    }

    public boolean gotoPayout() {
        return payoutPage.clickPayout();
    }

    public HashMap<String, String> choosePayoutMethod(String pMethod)
    {
       return payoutPage.selectPayoutMethod(pMethod);
    }

    public boolean submitPayout()
    {
        return payoutPage.proceedPayout();
    }



}

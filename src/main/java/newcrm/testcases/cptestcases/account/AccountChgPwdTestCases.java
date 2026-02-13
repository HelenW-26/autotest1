package newcrm.testcases.cptestcases.account;

import newcrm.business.businessbase.CPLiveAccounts;
import newcrm.business.businessbase.CPLogin;
import newcrm.business.businessbase.CPMenu;
import newcrm.factor.Factor;
import newcrm.global.GlobalProperties;
import newcrm.global.GlobalProperties.CPMenuName;
import newcrm.global.GlobalProperties.PLATFORM;
import newcrm.testcases.BaseTestCaseNew;
import newcrm.testcases.cptestcases.AccountManagementTestCases;
import newcrm.utils.testCaseDescUtils;
import newcrm.pages.clientpages.LiveAccountsPage.Account;

import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.LogUtils;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertTrue;

public class AccountChgPwdTestCases extends BaseTestCaseNew {

    protected Object data[][];
    private Factor myfactor;
    private CPMenu menu;
    private CPLiveAccounts liveAccounts;
    private CPLogin login;
    private AccountManagementTestCases accMgmtTC = new AccountManagementTestCases();

    @BeforeMethod(groups = {"CP_Live_Acc_Chg_Pwd"})
    protected void initMethod(Method method) {

        myfactor = getFactorNew();
        login = getLogin();

//        checkValidLoginSession();
        login.goToCpHome();
        menu = myfactor.newInstance(CPMenu.class);
        menu.goToMenu(CPMenuName.CPPORTAL);
        menu.changeLanguage("English");
        menu.goToMenu(CPMenuName.HOME);
    }

    @AfterMethod(groups = {"CP_Live_Acc_Chg_Pwd"})
    public void tearDown(ITestResult result) {
        // Close previous left open dialog if any
        if (liveAccounts != null) {
            liveAccounts.checkExistsDialog();
        }
    }

    public void liveAccountChgPwd(PLATFORM platform) {
        liveAccounts = myfactor.newInstance(CPLiveAccounts.class);
        GlobalProperties.platform = platform.toString();
        String currPwd = "", newPwd = "", errMsg = "";

        System.out.println("***Change Live Account Password***");
        List<Account> accountList = accMgmtTC.redirectToLivePage(platform, menu, liveAccounts);

        // Random select account
        Account acc = liveAccounts.randomSelectAccount(accountList);

        // Change password
        liveAccounts.clickChgAccountPwdBtn(platform, acc.getAccountNum());

        // Default password might not be updated back to original password on last request. Hence, check for which password is current password.
        List<String> pwdList = List.of((String)data[0][2], "123Qwe!!");
        for (int i = 0; i < pwdList.size(); i++) {
            currPwd = pwdList.get(i);
            newPwd = (i == 0 ? pwdList.get(i + 1) : pwdList.get(0));

            // Set current password
            errMsg = liveAccounts.setAccountCurrentPwd(currPwd);

            if (errMsg.isEmpty()) break;

            LogUtils.info("Current Password verified failed. Error Msg: " + errMsg);

            if (i != pwdList.size() - 1) {
                LogUtils.info("Original password has been changed. Retry using another password...");
            }
        }

        if (!errMsg.isEmpty()) {
            Assert.fail("Failed to change password for trading account " + acc.getAccountNum() + ". Error Msg: " + errMsg);
        }

        // Set new password
        liveAccounts.setAccountNewPwd(newPwd);
        liveAccounts.setAccountConfirmNewPwd(newPwd);

        // Submit changes
        liveAccounts.submitChgAccountPassword(acc.getAccountNum());

        // Set back to original password
        System.out.println("***Set back to original account password***");
        liveAccounts.clickChgAccountPwdBtn(platform, acc.getAccountNum());
        liveAccounts.setAccountCurrentPwd(newPwd);
        liveAccounts.setAccountNewPwd(currPwd);
        liveAccounts.setAccountConfirmNewPwd(currPwd);

        // Submit changes
        liveAccounts.submitChgAccountPassword(acc.getAccountNum());
    }

}

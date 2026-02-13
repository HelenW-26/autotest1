package newcrm.testcases.ibtestcases;

import newcrm.business.businessbase.CPMenu;
import newcrm.business.businessbase.ibbase.IBTransfer;
import newcrm.factor.Factor;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties.CURRENCY;
import newcrm.global.GlobalProperties.CPMenuName;
import newcrm.pages.ibpages.IBRebateTransferPage.Account;
import newcrm.testcases.IBBaseTestCaseNew;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;

import java.util.List;
import java.util.Random;

import static org.testng.Assert.assertTrue;

public class IBAccountManagementTestCases extends IBBaseTestCaseNew {
    private Factor myfactor;

    @BeforeMethod(alwaysRun = true)
    public void initMethod(){
        if (myfactor == null){
            myfactor = getFactorNew();
        }
    }
    protected void funcTransfer() {
        String fr_account="";
        String to_account="";
        String amount="";

        CPMenu menu = myfactor.newInstance(CPMenu.class);
        IBTransfer tf = myfactor.newInstance(IBTransfer.class);

        //menu.refresh();
        menu.goToMenu(CPMenuName.IBDASHBOARD);
        tf.clickTransfer();

        Random random = new Random();
        int f_size = 0;
        int t_size = 0;
        Boolean bIsFromAccAvailable = false, bIsAccAvailable = false;

        for(CURRENCY c: CURRENCY.values()) {
            // Find from account based on currency
            List<Account> accs = tf.getFromAccountByCurrency(c);
            f_size = accs.size();

            if(f_size == 0) {
                GlobalMethods.printDebugInfo("IB does not have the account (from) of this currency: " + c);
                continue;
            }

            // Find to account based on currency
            List<Account> to_accs = tf.getToAccountByCurrency(c);
            t_size = to_accs.size();

            if (t_size == 0) {
                GlobalMethods.printDebugInfo("IB does not have the account (to) of this currency: " + c);
                continue;
            }

            // When from & to accounts with the same currency found
            for(Account acc:accs) {
                Double balance = Double.valueOf(acc.getBalance());

                // Check from account has minimum balance
                if(balance > 1) {
                    fr_account = acc.getAccNumber();
                    bIsFromAccAvailable = true;
                    GlobalMethods.printDebugInfo("Find the account(from): " + fr_account + " Currency: " + c);
                    break;
                }

                GlobalMethods.printDebugInfo("Insufficient Balance. Account(from): " + fr_account + " Currency: " + c + " Balance: " + acc.getBalance());
            }

            // Find next currency if current currency account list do not have sufficient balance
            if (!bIsFromAccAvailable) { continue;}

            to_account = to_accs.get(random.nextInt(t_size)).getAccNumber();
            GlobalMethods.printDebugInfo("Find the account(to): " + to_account + " Currency: " + c);

            bIsAccAvailable = true;
            break;
        }

        assertTrue(bIsAccAvailable,"Did not find available account");

        tf.setFromAccount(fr_account);
        tf.setToAccount(to_account);
        tf.setAmount("1.00");

        Assert.assertTrue(tf.submit(), "Submit failure or error occurs!");
        System.out.println("***Test IB Funds Transfer succeed!!********");
    }

}

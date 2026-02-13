package newcrm.business.businessbase.ibbase;

import java.util.List;

import org.openqa.selenium.WebDriver;

import newcrm.global.GlobalProperties.CURRENCY;
import newcrm.pages.ibpages.IBRebateTransferPage;
import newcrm.pages.ibpages.IBRebateTransferPage.Account;
import newcrm.pages.ibpages.DashBoardPage;

public class IBTransfer {

    protected DashBoardPage dashboard;
    protected IBRebateTransferPage tp;

    public IBTransfer(WebDriver driver) {
        this.dashboard = new DashBoardPage(driver);
        this.tp = new IBRebateTransferPage(driver);
    }

    public IBTransfer(WebDriver driver, IBRebateTransferPage ibRebateTransferPage) {
        this.dashboard = new DashBoardPage(driver);
        this.tp = ibRebateTransferPage;
    }

    public void clickTransfer() {
        this.dashboard.clickTransfer();
    }

    public boolean submit() {
        return tp.submit();
    }

    public List<Account> getFromAccountByCurrency(CURRENCY currency){
        List<Account> result = tp.getFromAccounts();
        result.removeIf(account ->{
            if(account.getCurrency().equalsIgnoreCase(currency.toString())) {
                return false;
            }else {
                return true;
            }
        });
        return result;
    }

    public List<Account> getToAccountByCurrency(CURRENCY currency){
        List<Account> result = tp.getToAccount();
        result.removeIf(account ->{
            if(account.getCurrency().equalsIgnoreCase(currency.toString())) {
                return false;
            }else {
                return true;
            }
        });
        return result;
    }

    public List<Account> getToAccount(){
        return tp.getToAccount();
    }

    public void setFromAccount(String account) {
        tp.selectFromAccount(account);
    }

    public void setToAccount(String account) {
        tp.selectToAccount(account);
    }

    public void setAmount(String amount) {
        tp.setAmount(amount);
    }

}

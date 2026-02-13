package newcrm.pages.ibpages;

import newcrm.global.GlobalMethods;
import newcrm.pages.Page;

import newcrm.pages.ibpages.elements.IBTransferPageElements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IBRebateTransferPage extends Page {

    WebDriverWait wait03;

    public IBRebateTransferPage(WebDriver driver) {
        super(driver);
        this.wait03 = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    public boolean submit() {
        try {
            IBTransferPageElements p = PageFactory.initElements(driver, IBTransferPageElements.class);
            this.moveElementToVisible(p.e_submit);
            p.e_submit.click();
            waitLoading();
            this.moveContainerToTop();
            return true;

        } catch(Exception e) {
            GlobalMethods.printDebugInfo("IBTransferPage: Failed to find the submit button.");
            return false;
        }
    }

    public class Account{
        private String account_number;
        private String currency;
        private String balance;

        public Account(String acc_number,String v_currency,String v_balance) {
            this.account_number = acc_number.trim().toLowerCase();
            this.currency = v_currency.trim().toLowerCase();
            this.balance = v_balance.trim().toLowerCase();
        }

        public String getAccNumber() {
            return this.account_number;
        }

        public String getCurrency() {
            return this.currency;
        }

        public String getBalance() {
            return this.balance;
        }
    }

    /**
     * Get all accounts (Both MT accounts and Crypto Wallet accounts)
     */
    private List<Account> getAccounts(List<WebElement> els){
        List<Account> result = new ArrayList<Account>();
        if(els ==null || els.size()==0) {
            return result;
        }
        for(WebElement element:els) {
            String info = element.getAttribute("innerText");
            if(info!=null && info.trim() !="") {
                GlobalMethods.printDebugInfo("IBTransferPage Find an account: " + info);
                String values[] = info.split("-");
                String accNum = values[0].trim();
                String b_c = values[1].trim();
                List<String> cur_amount = GlobalMethods.getCurrencyAndAmount(b_c);
                Account account = new Account(accNum,cur_amount.get(0),cur_amount.get(1));
                result.add(account);
            }
        }
        return result;
    }

    /**
     * Get MT from accounts only(exclude Crypto Wallet accounts)
     */
    public List<Account> getFromAccounts(){
        waitLoading();
        IBTransferPageElements p = PageFactory.initElements(driver, IBTransferPageElements.class);
        this.moveElementToVisible(p.e_acc_from);
        p.e_acc_from.click();
        List<WebElement> els = this.getAllOpendElements();
        List<Account> result =  getMTAccounts(els);
        p.e_acc_from.click();
        return result;
    }

    /**
     * Get MT to accounts only(exclude Crypto Wallet accounts)
     */
    public List<Account> getToAccount(){
        waitLoading();
        IBTransferPageElements p = PageFactory.initElements(driver, IBTransferPageElements.class);
        this.moveElementToVisible(p.e_acc_to);
        p.e_acc_to.click();
        List<WebElement> els = this.getAllOpendElements();
        List<Account> result = getMTToAccounts(els);
        p.e_acc_to.click();
        return result;
    }

    /**
     * Get MT Accounts only(exclude Crypto Wallet accounts)
     */
    private List<Account> getMTAccounts(List<WebElement> els){
        List<Account> result = new ArrayList<Account>();

        if(els ==null || els.size()==0) {
            return result;
        }

        for(WebElement element:els) {
            String info = element.getAttribute("innerText");

            if(info != null && info.trim() != "" && !info.trim().contains("Wallet")) {
                String values[] = info.split("-");
                String accNum = values[0].trim();
                String b_c = values[1].trim();

                List<String> cur_amount = GlobalMethods.getCurrencyAndAmount(b_c);
                Account account = new Account(accNum,cur_amount.get(0),cur_amount.get(1));
                result.add(account);
            }
        }

        return result;
    }

    private List<Account> getMTToAccounts(List<WebElement> els){
        List<Account> result = new ArrayList<Account>();

        if(els ==null || els.size()==0) {
            return result;
        }

        for(WebElement element:els) {
            String info = element.getAttribute("innerText");

            if(info != null && info.trim() != "" && !info.trim().contains("Wallet")) {
                String values[] = info.split("-");
                String accNum = values[0].trim();
                values[1] = values[1].trim();
                String currency = values[1].substring(0, values[1].indexOf(" "));

                Account account = new Account(accNum, currency, "0.00");
                result.add(account);
            }
        }

        return result;
    }

    public void setAmount(String amount) {
        IBTransferPageElements p = PageFactory.initElements(driver, IBTransferPageElements.class);
        this.moveElementToVisible(p.e_amount);
        p.e_amount.clear();
        p.e_amount.sendKeys(amount);
        waitLoading();
        GlobalMethods.printDebugInfo("IBTransferPage - set transfer amount : " + amount);
    }

    public void selectFromAccount(String account) {
        IBTransferPageElements p = PageFactory.initElements(driver, IBTransferPageElements.class);
        this.moveElementToVisible(p.e_acc_from);
        p.e_acc_from.click();
        List<WebElement> els = this.getAllOpendElements();
        for(WebElement e:els) {
            String info = e.getAttribute("innerText");
            if(info !=null && !info.trim().equals("") && info.contains(account)) {
                //this.moveElementToVisible(e);
                e.click();
                GlobalMethods.printDebugInfo("IBTransferPage - set transfer from account : " + info);
                return;
            }
        }
        GlobalMethods.printDebugInfo("IBTransferPage - set transfer from account : " + account + " failed!");
        p.e_acc_from.click();
    }

    public void selectToAccount(String account) {
        IBTransferPageElements p = PageFactory.initElements(driver, IBTransferPageElements.class);
        this.moveElementToVisible(p.e_acc_to);
        p.e_acc_to.click();
        List<WebElement> els = this.getAllOpendElements();
        for(WebElement e:els) {
            String info = e.getAttribute("innerText");
            if(info !=null && !info.trim().equals("") && info.contains(account)) {
                //this.moveElementToVisible(e);
                e.click();
                GlobalMethods.printDebugInfo("IBTransferPage - set transfer to account : " + info);
                return;
            }
        }
        GlobalMethods.printDebugInfo("IBTransferPage - set transfer to account : " + account);
        p.e_acc_to.click();
    }

}

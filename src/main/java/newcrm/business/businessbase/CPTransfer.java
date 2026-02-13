package newcrm.business.businessbase;

import java.util.List;
import java.util.Comparator;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;
import newcrm.global.GlobalMethods;
import org.openqa.selenium.WebDriver;

import newcrm.global.GlobalProperties.CURRENCY;
import newcrm.pages.clientpages.TransferPage;
import newcrm.pages.clientpages.TransferPage.Account;
import utils.LogUtils;

public class CPTransfer {

	protected TransferPage tp;
	
	public CPTransfer(WebDriver driver) {
		this.tp = new TransferPage(driver);
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

	public String getTransferFromAccount() {
		tp.waitLoadingTransferFromAccountContent();
		List<Account> accounts = tp.getFromAccountsNew();
		return getValidAccount(accounts, true);
	}

    public String getTransferRandomFromAccount(){
        tp.waitLoadingTransferFromAccountContent();
        List<Account> accounts = tp.getFromAccountsNew();
        return getValidAccount(accounts, false);
    }

	public String getTransferToAccount() {
		tp.waitLoadingTransferToAccountContent();
		List<Account> accounts = tp.getToAccountNew();
		return getValidAccount(accounts, false);
	}

	public String getValidAccount(List<Account> accounts, boolean bIsTransferFrom) {

		// select an available account with any currency under this method
		for (CURRENCY currency : CURRENCY.values()) {

			String account;

			if (!bIsTransferFrom) {
				account = this.GetTransferToAvailableAccount(currency, accounts);
			} else {
				account = this.GetTransferFromAvailableAccount(currency, accounts);
			}


			if (account != null) {
				return account;
			}
		}

		return null;
	}

	protected String GetTransferFromAvailableAccount(CURRENCY currency, List<Account> accounts) {

		if (accounts.isEmpty()) {
			GlobalMethods.printDebugInfo("Client does not have the account of this currency: " + currency);
			return null;
		}

		// Filter record based on currency
		List<Account> filtered = accounts.stream()
				.filter(acc -> currency.toString().equalsIgnoreCase(acc.getCurrency()))
				.toList();

		if (filtered.isEmpty()) {
			GlobalMethods.printDebugInfo("Client does not have the account of this currency: " + currency);
			return null;
		}

		// Check if there is at least 2 accounts with the same currency
		if (filtered.size() < 2) {
			GlobalMethods.printDebugInfo("Client does not have multiple accounts of this currency: " + currency);
			return null;
		}

		// Find account with the most balance among the same currency account
		Account maxBalanceAccount = filtered.stream()
				.max(Comparator.comparing(acc -> BigDecimal.valueOf(Double.parseDouble(acc.getBalance()))))
				.orElse(null);

		if (maxBalanceAccount != null) {
			String accNum = maxBalanceAccount.getAccNumber();
			GlobalMethods.printDebugInfo("Found valid transfer from account, Account: " + accNum + ", Currency: " + currency);
			return accNum;
		}

        return null;
	}

	protected String GetTransferToAvailableAccount(CURRENCY currency, List<Account> accounts) {

		if (accounts.isEmpty()) {
			GlobalMethods.printDebugInfo("Client does not have the account of this currency: " + currency);
			return null;
		}

		// Filter record based on currency
		List<Account> filtered = accounts.stream()
				.filter(acc -> currency.toString().equalsIgnoreCase(acc.getCurrency()))
				.toList();

		if (filtered.isEmpty()) {
			GlobalMethods.printDebugInfo("Client does not have the account of this currency: " + currency);
			return null;
		}

		// Randomly select one account
		Random rand = new Random();
		Account randomAccount = filtered.get(rand.nextInt(filtered.size()));
		String accNum = randomAccount.getAccNumber();
		GlobalMethods.printDebugInfo("Found valid transfer to account, Account: " + accNum + ", Currency: " + currency);

		return accNum;
	}

	//for copy trading
	public String getTransferFromNonCPAccount() {
		tp.waitLoading();
		tp.waitLoadingTransferFromAccountContent();
		List<Account> accounts = tp.getFromNonCPAccounts();

		// Randomly select one account
		Random rand = new Random();
		Account randomAccount = accounts.get(rand.nextInt(accounts.size()));
		String accNum = randomAccount.getAccNumber();
		GlobalMethods.printDebugInfo("Found valid transfer to account, Account: " + accNum + ", Currency: " + randomAccount.getCurrency());

		return accNum;
	}

	//for copy trading
	public String getTransferToCPAccount() {
		tp.waitLoadingTransferToAccountContent();
		List<Account> accounts = tp.getToCPAccount();
		// Randomly select one account
		Random rand = new Random();
		Account randomAccount = accounts.get(rand.nextInt(accounts.size()));
		String accNum = randomAccount.getAccNumber();
		GlobalMethods.printDebugInfo("Found valid transfer to account, Account: " + accNum + ", Currency: " + randomAccount.getCurrency());

		return accNum;
	}

    public void setAccountAndAmount(String fromAccount,String toAccount, String amount) {

        tp.clickAccFrom();
        setFromAccountNew(fromAccount);
        tp.clickAccTo();
        setWalletAccount(toAccount);
        setAmount(amount);
        submit();
    }

    public boolean checkCredit(){
        List<List<String>> creditList = this.getCreditList();

        if (creditList.size() != 2){
            return false;
        }

        List<String> useCreditList = creditList.get(0);
        List<String> giveupCreditList = creditList.get(1);

        LogUtils.info("credit list: " + new Gson().toJson(useCreditList));

        boolean amount = Double.parseDouble(useCreditList.get(1)) == Double.parseDouble(giveupCreditList.get(1));
        boolean credit = Double.parseDouble(useCreditList.get(2))>= 0;
        boolean deductCredit = Double.parseDouble(useCreditList.get(3)) == 0;

        return amount && credit && deductCredit;

    }

	public List<Account> getToAccount(){
		return tp.getToAccount();
	}
	
	public void setAmount(String amount) {
		tp.setAmount(amount);
	}
	
	public void submit() {
		tp.submit();
	}

    public void submitTransfer(){
        tp.submitTransfer();
    }
    public void confirmTransfer(){
        tp.confirmTransfer();
    }
    public void giveUpCreditBtn(){
        tp.giveUpCreditBtn();
    }
    public void clickUseCreditBtn(){
        tp.clickUseCreditBtn();
    }

    public List<List<String>> getCreditList(){
       return tp.getCreditList();
    }

    public void gotoHistory(){
        tp.gotoHistory();
    }
    public void clickTo(){
        tp.clickAccTo();
    }
    public void clickFrom(){
        tp.clickAccFrom();
    }

	public void setFromAccount(String account) {
		tp.selectFromAccount(account);
	}

	public void setFromAccountNew(String account) {
		tp.selectFromAccountNew(account);
	}

	public void setToAccount(String account) {
		tp.selectToAccount(account);
	}

	public void setToAccountNew(String account) {
		tp.selectToAccountNew(account);
	}
    public void setWalletAccount(String walletType){
        tp.selectWalletAccount(walletType);
    }
}

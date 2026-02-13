package newcrm.business.businessbase;

import java.math.BigDecimal;
import java.util.*;

import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties.CURRENCY;
import newcrm.pages.clientpages.WithdrawBasePage;
import newcrm.utils.db.DbUtils;

public abstract class CPWithdrawBase {

	protected WithdrawBasePage withdrawpage;
	protected DbUtils db=null;
	public CPWithdrawBase(WithdrawBasePage page) {
		this.withdrawpage = page;
	}

	/**
	 * check account by currency, if the account exist and balance is more than 0
	 * @param currency
	 * @return return the first account number and balance
	 */
	public String checkAccount(CURRENCY currency) {
		withdrawpage.waitLoadingWithdrawalAccountContent();

		List<WithdrawBasePage.Account> accounts = withdrawpage.getAllAccounts();
		for(WithdrawBasePage.Account account : accounts) {
			if(account.getCurrency().equalsIgnoreCase(currency.toString())) {
				Double balance = Double.valueOf(account.getBalance());
				if(balance > 0) {
					return account.getAccNumber();
				}
			}
		}
		return null;
	}

	public WithdrawBasePage.Account getValidAccount() {
		withdrawpage.waitLoadingWithdrawalAccountContent();

		List<WithdrawBasePage.Account> accounts = withdrawpage.getAllAccountsNew();

		if (accounts.isEmpty()) {
			return null;
		}

		// select an available account with any currency under this method
		for (CURRENCY currency : CURRENCY.values()) {
			WithdrawBasePage.Account accSelected = GetAvailableAccount(currency, accounts);

			if (accSelected != null) {
				return accSelected;
			}
		}

		return null;
	}

	protected WithdrawBasePage.Account GetAvailableAccount(CURRENCY currency, List<WithdrawBasePage.Account> accounts) {

		// Filter record based on currency
		List<WithdrawBasePage.Account> filtered = accounts.stream()
				.filter(acc -> currency.toString().equalsIgnoreCase(acc.getCurrency()))
				.toList();

		if (filtered.isEmpty()) {
			GlobalMethods.printDebugInfo("Client does not have the account of this currency: " + currency);
			return null;
		}

//		// Filter record based on account balance > 0
//		filtered = filtered.stream()
//				.filter(acc -> {
//					try {
//						return Double.parseDouble(acc.getBalance()) > 0;
//					} catch (NumberFormatException e) {
//						return false;
//					}
//				})
//				.toList();

//		if (filtered.isEmpty()) {
//			GlobalMethods.printDebugInfo("Client account of this currency: " + currency + " does not have any balance left");
//			return null;
//		}

//		// Randomly select one account
//		Random rand = new Random();
//		WithdrawBasePage.Account randomAccount = filtered.get(rand.nextInt(filtered.size()));
//		GlobalMethods.printDebugInfo("Found valid account, Account: " + randomAccount.getAccNumber() + ", Currency: " + currency);
//
//		return randomAccount;

		// Find account with the most balance among the same currency account
		WithdrawBasePage.Account maxBalanceAccount = filtered.stream()
				.max(Comparator.comparing(acc -> BigDecimal.valueOf(Double.parseDouble(acc.getBalance()))))
				.orElse(null);

		if (maxBalanceAccount != null) {
			GlobalMethods.printDebugInfo("Found valid account, Account: " + maxBalanceAccount.getAccNumber() + ", Currency: " + currency);
		}

		return maxBalanceAccount;

	}

	public double getBalance(String accNum) {
		List<WithdrawBasePage.Account> accounts = withdrawpage.getAllAccounts();
		for(WithdrawBasePage.Account account : accounts) {
			if(account.getAccNumber().equalsIgnoreCase(accNum.trim())) {
				Double balance = Double.valueOf(account.getBalance());
				return balance.doubleValue();
			}
		}
		return 0;
	}

	public double getBalanceNew(String accNum) {
		withdrawpage.waitLoadingWithdrawalAccountContent();

		WithdrawBasePage.Account acc = withdrawpage.getSpecificAccount(accNum);
		return Double.parseDouble(acc.getBalance());
	}

	public boolean setAccountAndAmount(String account,double amount) {
		if(account==null || amount == 0) {
			return false;
		}

		withdrawpage.setAccount(account);
		waitSpinnerLoading();
		withdrawpage.setAmount(String.valueOf(amount));

		System.out.println("Set Account: " + account);
		System.out.println("Set Amount: " + amount);

		return true;
	}

	public boolean setAccountAndAmountNew(String account,double amount) {
		if(account==null || amount == 0) {
			return false;
		}

		withdrawpage.setAccountNew(account);
		waitSpinnerLoading();
		withdrawpage.setAmount(String.valueOf(amount));
		System.out.println("Set Amount to: " + amount);

		return true;
	}

	public void clickContinue() {
		waitSpinnerLoading();
		withdrawpage.clickContinue();
	}
	public void waitSpinnerLoading()
	{
		//withdrawpage.waitSpinnerLoading();
		withdrawpage.waitLoading();
	}

	//A new security api added cause withftaw setMethod need more waiting time
	public void waitCustomiseLoading(int sec)
	{
		withdrawpage.waitLoadingForCustomise(120);
	}

	//Click cancel button, to close withdrawal security authenticator pop-up window if it's showing
	public void closeWithdrawalOTPWindow(){
		withdrawpage.cancelWithdrawalOTPWindow();
	}

	//Get withdrawal limit amount
	public int getWithdrawalLimitAmount() {
		;
        return withdrawpage.getWithdrawalLimitAmount();
    }
	//Get change rate
	public double getChangeRate() {
		;
        return withdrawpage.getChangeRate();
    }

}

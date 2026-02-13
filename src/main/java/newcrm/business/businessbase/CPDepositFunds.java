package newcrm.business.businessbase;

import java.util.List;

import org.openqa.selenium.WebDriver;

import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties.DEPOSITMETHOD;
import newcrm.pages.clientpages.DepositFundsPage;

public class CPDepositFunds {
	protected DepositFundsPage deposit_funds;
	public CPDepositFunds(DepositFundsPage deposit_funds) {
		this.deposit_funds = deposit_funds;
	}
	
	public CPDepositFunds(WebDriver driver) {
		this.deposit_funds = new DepositFundsPage(driver);
	}
	/***
	 * check if the client could use the specific deposit method
	 * @param method deposit method
	 * @return true: yes, false: no
	 */
	public boolean checkMethod(DEPOSITMETHOD method) {
		List<String> methods = deposit_funds.getAvailableDepositMethodsName();
		methods.removeIf(name->{
			if(method.getCPTestId().toLowerCase().contains(name.toLowerCase())) {
				return false;
			}else {
				return true;
			}
		});
		return methods.size()>0;
	}
	
	public boolean navigateToMethod(DEPOSITMETHOD method) {
		if(!checkMethod(method)) {
			GlobalMethods.printDebugInfo("CPDepositFunds: Did not find the deposit method: " + method.toString());
			return false;
		}
		return deposit_funds.navigateTo(method);
	}
}

package newcrm.business.aubusiness.register;

import org.openqa.selenium.WebDriver;

import newcrm.business.businessbase.CPRegister;

public class VFSCCPRegister extends CPRegister {

	public VFSCCPRegister(WebDriver driver, String url) {
		super(driver,url);
	}
	
	@Override
	public boolean setRegulatorAndCountry(String country,String regulator) {
		if(!entrypage.setCountry(country)) {
			return false;
		}
		
		if(!entrypage.setRegulator(regulator)) {
			return false;
		}
		userdetails.put("Country", country);
		userdetails.put("Regulator", regulator);
		return true;
	}
}

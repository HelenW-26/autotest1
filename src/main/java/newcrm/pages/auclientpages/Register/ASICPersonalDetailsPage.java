package newcrm.pages.auclientpages.Register;

import org.openqa.selenium.WebDriver;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.register.PersonalDetailsPage;

public class ASICPersonalDetailsPage extends PersonalDetailsPage {

	public ASICPersonalDetailsPage(WebDriver driver) {
		super(driver);
	}
	
	@Override
	public String setPersonTitle() {
		String result = "This is ASIC, do not need set title";
		GlobalMethods.printDebugInfo(result);
		return result;
	}
	
	@Override
	public void setIDNumber(String idnum) {
		GlobalMethods.printDebugInfo("ASICPersonalDetailsPage: This is ASIC, do not need  set ID number");
	}
	
	@Override
	public String setIdentificationType() {
		String result = "This is ASIC, do not need set ID type";
		GlobalMethods.printDebugInfo(result);
		return result;
	}
	
	@Override
	public String setBirthPlace() {
		String result = "This is ASIC, do not need set birth place";
		GlobalMethods.printDebugInfo(result);
		return result;
	}
}

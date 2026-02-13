package newcrm.business.aubusiness;
import org.openqa.selenium.WebDriver;

import newcrm.business.businessbase.CPMenu;
import newcrm.pages.auclientpages.AUMenuPage;

public class AuCPMenu extends CPMenu {

	public AuCPMenu(WebDriver driver) {
		
		super(new AUMenuPage(driver));
		// TODO Auto-generated constructor stub
	}
	
}
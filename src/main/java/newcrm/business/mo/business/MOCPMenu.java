package newcrm.business.mo.business;
import org.openqa.selenium.WebDriver;

import newcrm.business.businessbase.CPMenu;
import newcrm.pages.moclientpages.MOMenuPage;

public class MOCPMenu extends CPMenu {

	public MOCPMenu(WebDriver driver) {
		
		super(new MOMenuPage(driver));
		// TODO Auto-generated constructor stub
	}
	
}
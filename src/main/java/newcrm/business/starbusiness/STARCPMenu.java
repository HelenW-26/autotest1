package newcrm.business.starbusiness;
import org.openqa.selenium.WebDriver;

import newcrm.business.businessbase.CPMenu;
import newcrm.pages.starclientpages.STARMenuPage;

public class STARCPMenu extends CPMenu {

	public STARCPMenu(WebDriver driver) {
		
		super(new STARMenuPage(driver));
		// TODO Auto-generated constructor stub
	}

}
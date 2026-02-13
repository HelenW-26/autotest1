package newcrm.pages.clientpages.elements;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;

public class LogoutElements {

	@CacheLookup
	@FindAll({
		@FindBy(xpath="//div[@data-testid='dropdown']"),
		@FindBy(xpath="//div[@class='login_inner el-dropdown-selfdefine']")
		}
	)
	public WebElement dropDown;
	
	@CacheLookup
	@FindAll({
		@FindBy(xpath="//li[@data-testid='logout']")
		}
	)
	
	public WebElement logOut;
}

package newcrm.pages.clientpages.elements;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;

public class MenuElements {

	@CacheLookup
	@FindAll({
		@FindBy(xpath="//div[@data-testid='dropdownFlag']"),
		@FindBy(css="div.el-dropdown:nth-child(1)")
		}
	)
	public WebElement flag;
	
	
	
	@CacheLookup
	@FindAll({
		@FindBy(xpath="//li[@data-testid='menu.promotion']")//VT,PUG,VFX
		}
	)
	public WebElement promotion;
	
	@CacheLookup
	@FindAll({
		@FindBy(xpath="//li[@data-testid='menu.depositBonus']")//VT,PUG,VFX
		}
	)
	public WebElement dpb;
}

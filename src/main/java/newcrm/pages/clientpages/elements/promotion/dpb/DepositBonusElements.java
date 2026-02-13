package newcrm.pages.clientpages.elements.promotion.dpb;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;

public class DepositBonusElements {

	
	@FindAll({
		@FindBy(xpath="//span[contains(@class,'el-checkbox__input')]")
		}
	)
	public WebElement t_c;
	
	@FindAll({
		@FindBy(xpath="//button[@data-testid='OptInNow']"),//AU
		@FindBy(xpath="//button[@data-testid='opt']")//VT,PUG
		}
	)
	public WebElement button_opt;
	
}

package newcrm.pages.clientpages.promotions;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import newcrm.pages.Page;
import newcrm.pages.clientpages.elements.promotion.dpb.DepositBonusElements;

public class DepositBonusPage extends Page {

	public DepositBonusPage(WebDriver driver) {
		super(driver);
	}
	
	public void tickTandC(boolean tick) {
		//only vt need untick before sign out
		DepositBonusElements els = PageFactory.initElements(driver,DepositBonusElements.class);
		//WebElement t_c = this.findClickableElementByXpath("//label[contains(@class,'el-checkbox d-inline-block')]");
		String status = els.t_c.getAttribute("class");
		if(tick) {
			//勾选选择框
			if(status.contains("is-checked")) {
				return;
			}
			this.moveElementToVisible(els.t_c);
			els.t_c.click();
		}else {
			//取消tick
			if(!status.contains("is-checked")) {
				return;
			}
			this.moveElementToVisible(els.t_c);
			els.t_c.click();
		}
		
		
	}
	
	public void clickOpt() {
		DepositBonusElements els = PageFactory.initElements(driver,DepositBonusElements.class);
		this.moveElementToVisible(els.button_opt);
		els.button_opt.click();
	}
	
	
	public String getMessage() {
		WebElement e = this.findVisibleElemntByXpath("//div[@role='alert']/p");
		if(e!=null) {
			return e.getText();
		}
		return "";
	}
}

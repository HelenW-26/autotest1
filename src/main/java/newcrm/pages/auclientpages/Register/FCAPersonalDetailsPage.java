package newcrm.pages.auclientpages.Register;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.register.FinancialDetailsPage;
import newcrm.pages.clientpages.register.PersonalDetailsPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class FCAPersonalDetailsPage extends PersonalDetailsPage {

	public FCAPersonalDetailsPage(WebDriver driver) {
		super(driver);
	}

    @Override
    protected WebElement getTitleElement() {
        return this.findClickableElementByXpath("//input[@name='title']");
    }

    @Override
    protected WebElement getBirthPlaceInput() {
        return this.findClickableElemntBy(By.xpath("//input[@name='placeOfBirth']"));
    }

    @Override
    protected WebElement getIDTypeInput() {
        return this.findClickableElemntBy(By.xpath("//input[@name='idType']"));
    }

    @Override
    public void goToPersonalDetailsPage() {
        WebElement e = this.findVisibleElemntByXpath("//div[@class='register_guide_content_box']/button");
        triggerElementClickEvent_withoutMoveElement(e);
    }
}

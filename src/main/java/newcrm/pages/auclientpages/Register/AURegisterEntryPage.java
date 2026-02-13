package newcrm.pages.auclientpages.Register;

import newcrm.pages.clientpages.register.RegisterEntryPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class AURegisterEntryPage extends RegisterEntryPage {

    public AURegisterEntryPage(WebDriver driver)
    {
        super(driver);
    }

   /* @Override
    protected WebElement getActionInput() {
        return this.findClickableElemntBy(By.id("actionInfo"));
    }

    protected WebElement getActionButton() {
        return this.findClickableElemntBy(By.id("effect"));
    }

    @Override
    protected WebElement getSubmitButton() {
        return this.findClickableElemntBy(By.id("sub-open"));
    }*/


}

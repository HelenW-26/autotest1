package newcrm.pages.ibpages.elements;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;

public class IBTransferPageElements {

    @CacheLookup
    @FindAll({
            @FindBy(xpath="//div[@data-testid='rebateAccount']/div/input"),
            @FindBy(id="fromAccount")
    }
    )
    public WebElement e_acc_from;

    @CacheLookup
    @FindAll({
            @FindBy(xpath="//div[@data-testid='tradingAccount']/div/input"),
            @FindBy(id="toAccount")
    }
    )
    public WebElement e_acc_to;

    @CacheLookup
    @FindAll({
            @FindBy(xpath="//input[@data-testid='numericInput']")
    }
    )
    public WebElement e_amount;

    @CacheLookup
    @FindAll({
            @FindBy(xpath="//button[@data-testid='submit']")
    }
    )
    public WebElement e_submit;

}

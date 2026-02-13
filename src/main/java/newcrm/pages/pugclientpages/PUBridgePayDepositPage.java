package newcrm.pages.pugclientpages;

import newcrm.pages.clientpages.deposit.BridgePayDepositPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;

public class PUBridgePayDepositPage extends BridgePayDepositPage {

    public PUBridgePayDepositPage(WebDriver driver) {
        super(driver);
    }

    public void tickTickBox() {
        WebElement checkbox = assertElementExists(By.cssSelector("input.ant-checkbox-input"), "Agreement");
        boolean isChecked = checkbox.isSelected();

        if (!isChecked) {
            triggerElementClickEvent(checkbox);
        }
    }

}

package newcrm.pages.auclientpages;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.withdraw.LocalBankWithdrawPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class AUAzupayWithdrawPage extends LocalBankWithdrawPage {

    public AUAzupayWithdrawPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean submit(){
        waitLoading();
        WebElement submit = null;
        try {
            submit = this.findClickableElementByXpath("//span[contains(text(),'Submit') or contains(text(),'SUBMIT')]");
        }catch(Exception e) {
            GlobalMethods.printDebugInfo("WithdrawBasePage: Failed to find the submit button.");
            return false;
        }

        this.moveElementToVisible(submit);

        js.executeScript("arguments[0].click()",submit);
        GlobalMethods.printDebugInfo("Submit Payment");
        this.waitLoadingForCustomise(120);;

        WebElement response = this.findVisibleElemntByXpath("//*[contains(text(),'Your withdrawal request has been successfully submitted.')]");
        String response_info = response.getText();
        if(response_info != null) {
            GlobalMethods.printDebugInfo("WithdrawBasePage: Response info: " + response_info.trim());
            if(response_info.toLowerCase().contains("successful")) {
                //withdraw request is succeed. return to home page
                this.findClickableElementByXpath("//*[@data-testid='bkToHm']").click();
                this.waitLoading();
                return true;
            }
        }

        System.out.println("AUAzupayPage: ERROR: withdraw requestion is failed");
        return false;
    }

}
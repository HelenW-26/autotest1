package newcrm.pages.umclientpages;

import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.pages.clientpages.WithdrawPage;
import newcrm.pages.clientpages.withdraw.InternalBankWithdrawPage;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class UMInternalBankWithdrawPage extends InternalBankWithdrawPage {


    public UMInternalBankWithdrawPage(WebDriver driver) {
        super(driver);
    }

    public String setWithrawMethod(GlobalProperties.DEPOSITMETHOD method) {
        WebElement m_element = this.getMethodDiv();
        if(m_element==null) {
            System.out.println("NonCCWithdrawPage: Find Withdraw Type Element Failed: Could not find the element.");
            return null;
        }
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click()",m_element);
        //m_element.click();
        WebElement element = getWithdrawMethodElement(method);
        try {
            this.moveElementToVisible(element);
            //element.click();
            js.executeScript("arguments[0].click()",element);
            System.out.println("Select withdraw method: " + method.getWithdrawName());
            return method.getWithdrawName();
        }catch(Exception e) {
            System.out.println("Could not find the withdraw method: " + method.getWithdrawName());
        }
        return null;
    }

    public void clickContinue() {
        this.findClickableElementByXpath("(//button[@data-testid='submit'])[1]").click();
//        waitLoading();
//        try {
//            this.findClickableElementByXpath("//div[@class='dialog_content']/button").click();
//        }
//        catch(Exception e)
//        {
//            GlobalMethods.printDebugInfo("No credit deducted dialog pop up");
//        }
    }
}

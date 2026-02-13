package newcrm.pages.auibpages;

import newcrm.global.GlobalMethods;
import newcrm.pages.Page;
import newcrm.pages.ibpages.IBAccountReportPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.util.List;

public class AUIBAccountReportPage extends IBAccountReportPage {

    public AUIBAccountReportPage(WebDriver driver) {
        super(driver);
    }

    protected WebElement getArchivedClientsTabEle() {
        return assertElementExists(By.xpath("//div[@id='tab-/archivedClients']"), "Archived Clients Tab");
    }

//    @Override
//    //Verify the Archived Client Tab
//    public void verifyLeadsAndArchivedClientsTab(){
//        triggerElementClickEvent_withoutMoveElement(this.getArchivedClientsTabEle());
//        String userID = this.getAccountReportFirstRowFirstColumnEle("Archived Clients").getText();
//        if (!userID.isEmpty()){
//            GlobalMethods.printDebugInfo("Archived Clients First Record: " + userID);
//        } else {
//            Assert.fail("Archived Clients First Record is EMPTY!");
//        }
//    }

}

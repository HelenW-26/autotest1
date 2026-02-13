package newcrm.business.businessbase.support;

import newcrm.pages.clientpages.support.SupportPage;
import org.openqa.selenium.WebDriver;
import vantagecrm.Utils;

import java.nio.file.Path;
import java.nio.file.Paths;

public class CPSupport {

    protected SupportPage supportPage;

    public CPSupport(WebDriver driver) {
        this.supportPage = new SupportPage(driver);
    }

    public void waitLoadingSupportTicketContent() {
        supportPage.waitLoadingSupportTicketContent();
    }

    public void createSupportTicket() {
        Path parent = Paths.get("src", "main", "resources", "vantagecrm", "doc");
        String fileAttachment = Paths.get(parent.toString(), "ID_Card_Gen.jpg").toString();

        supportPage.clickCreateBtn();
        supportPage.waitLoadingCreateTicketDialog();
        supportPage.setTicketSubject("TestCRM Automation Test Subject");
        supportPage.setTicketContent("TestCRM Automation Test Content");
        supportPage.uploadAttachment(Paths.get(Utils.workingDir, fileAttachment).toString());
        supportPage.clickSubmitBtn();
    }

}

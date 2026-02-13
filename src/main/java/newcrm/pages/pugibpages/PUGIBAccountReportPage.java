package newcrm.pages.pugibpages;

import newcrm.global.GlobalMethods;
import newcrm.pages.ibpages.IBAccountReportPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

public class PUGIBAccountReportPage extends IBAccountReportPage {

    public PUGIBAccountReportPage(WebDriver driver) {
        super(driver);
    }

    @Override
    protected WebElement getAccountReportFirstRowCampaignSourceEle() {
        return assertElementExists(By.xpath("//div[@class='ht-table table_warp'] //tbody/tr[1]/td[6]/div"), "Account Report First Row - Campaign Source");
    }

}
package newcrm.pages.auibpages;

import newcrm.global.GlobalMethods;
import newcrm.pages.Page;
import newcrm.pages.ibpages.IBReferralLinksPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.testng.Assert;

import java.util.List;

public class AuASICIBReferralLinksPage extends IBReferralLinksPage {

    public AuASICIBReferralLinksPage(WebDriver driver) {
        super(driver);
    }

    @Override
    protected String ibRefLinkEnglishDomain(){
        return "https://www.vantagemarkets.com/en-au/";
    }

}

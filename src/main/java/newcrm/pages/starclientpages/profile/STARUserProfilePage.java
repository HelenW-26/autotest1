package newcrm.pages.starclientpages.profile;

import newcrm.pages.clientpages.profile.UserProfilePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class STARUserProfilePage extends UserProfilePage {

    public STARUserProfilePage(WebDriver driver) {
        super(driver);
    }

    @Override
    protected WebElement getProfileBoxContentEle() {
        return assertVisibleElementExists(By.cssSelector("div.starProfile_box"), "Profile Info Content");
    }

    @Override
    protected WebElement getProfileTabListEle() {
        return assertVisibleElementExists(By.cssSelector(".starProfile_box + div.ht-tabs"), "Profile Tab List");
    }

    @Override
    protected WebElement getProfileEmailEle() {
        return assertElementExists(By.cssSelector("div.profile_details_section svg.ht-icon-email + p"), "Profile Email");
    }

    @Override
    protected WebElement getProfilePhoneEle() {
        return assertElementExists(By.cssSelector("div.profile_details_section svg.ht-icon-phone + p"), "Profile Email");
    }

}

package newcrm.pages.umclientpages.profile;

import newcrm.pages.clientpages.profile.UserProfilePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class UMUserProfilePage extends UserProfilePage {

    public UMUserProfilePage(WebDriver driver) {
        super(driver);
    }

    @Override
    protected WebElement getProfileBoxContentEle() {
        return assertVisibleElementExists(By.cssSelector("div.userInfo"), "Profile Info Content");
    }

    @Override
    protected WebElement getProfileTabListEle() {
        return assertVisibleElementExists(By.cssSelector(".userInfo + div.ht-tabs"), "Profile Tab List");
    }

    @Override
    protected WebElement getProfileEmailEle() {
        return assertElementExists(By.cssSelector("div.userInfo svg.ht-icon-email + span"), "Profile Email");
    }

    @Override
    protected WebElement getProfilePhoneEle() {
        return assertElementExists(By.cssSelector("div.userInfo svg.ht-icon-phone + span"), "Profile Email");
    }

}

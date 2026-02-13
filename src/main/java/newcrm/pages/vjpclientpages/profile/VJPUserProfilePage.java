package newcrm.pages.vjpclientpages.profile;

import newcrm.pages.clientpages.profile.UserProfilePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class VJPUserProfilePage extends UserProfilePage {

    public VJPUserProfilePage(WebDriver driver) {
        super(driver);
    }

    @Override
    protected WebElement getProfileTabListEle() {
        return assertVisibleElementExists(By.cssSelector(".ht-title+ div.ht-tabs"), "Profile Tab List");
    }

}

package newcrm.pages.pugclientpages.profile;

import newcrm.pages.clientpages.profile.UserProfilePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PUUserProfilePage extends UserProfilePage {

    public PUUserProfilePage(WebDriver driver) {
        super(driver);
    }

    @Override
    protected WebElement getProfileEmailEle() {
        return assertElementExists(By.cssSelector("div.myProfile_box div.wrapper_item:first-of-type div.item_text > p"), "Profile Email");
    }

    @Override
    protected WebElement getProfilePhoneEle() {
        return assertElementExists(By.cssSelector("div.myProfile_box div.wrapper_item:last-of-type div.item_text > p"), "Profile Email");
    }

}

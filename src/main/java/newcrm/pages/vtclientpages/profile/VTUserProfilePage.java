package newcrm.pages.vtclientpages.profile;

import newcrm.pages.clientpages.profile.UserProfilePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class VTUserProfilePage extends UserProfilePage {

    public VTUserProfilePage(WebDriver driver) {
        super(driver);
    }

    @Override
    protected WebElement getProfileBoxContentEle() {
        return assertVisibleElementExists(By.xpath("//*[@id='myProfile']//*[@class='main']"), "Profile Info Content");
    }

    @Override
    protected WebElement getProfileEmailEle() {
        return assertElementExists(By.xpath("//*[@id='myProfile']//*[@class='main']//*[contains(@class,'profile_title') and normalize-space(text())='Email Address']/following-sibling::*[1]"), "Profile Email");
    }

    @Override
    protected WebElement getProfilePhoneEle() {
        return assertElementExists(By.xpath("//*[@id='myProfile']//*[@class='main']//*[contains(@class,'profile_title') and normalize-space(text())='Phone Number']/following-sibling::*[1]"), "Profile Email");
    }

}

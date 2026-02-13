package newcrm.pages.auclientpages.profile;

import newcrm.pages.clientpages.profile.UserProfilePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class AUUserProfilePage extends UserProfilePage {

    public AUUserProfilePage(WebDriver driver) {
        super(driver);
    }

    @Override
    protected List<WebElement> getProfileVTabContentListEle(WebElement parentEle, String desc) {
        return assertElementsExists(By.cssSelector("div.el-collapse-item"), "Verification (" + desc + ") Content Item", parentEle);
    }

    @Override
    protected WebElement getProfileVTabContentItemTitleEle(WebElement parentEle, String desc) {
        return assertElementExists(By.cssSelector("div.verification_level_info_title_name"), "Verification (" + desc + ") Content Item Title", parentEle);
    }

}

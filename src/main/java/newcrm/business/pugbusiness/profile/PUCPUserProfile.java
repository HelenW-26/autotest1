package newcrm.business.pugbusiness.profile;

import newcrm.business.businessbase.profile.CPUserProfile;
import newcrm.pages.pugclientpages.profile.PUUserProfilePage;
import org.openqa.selenium.WebDriver;

public class PUCPUserProfile extends CPUserProfile {

    public PUCPUserProfile(WebDriver driver) {
        super(new PUUserProfilePage(driver));
    }

    @Override
    public String getProfilePhoneMaskedRegex() {
        return "\\*{3}\\d{3}$";
    }

}

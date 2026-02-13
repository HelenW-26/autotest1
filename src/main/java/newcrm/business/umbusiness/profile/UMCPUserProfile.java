package newcrm.business.umbusiness.profile;

import newcrm.business.businessbase.profile.CPUserProfile;
import newcrm.pages.umclientpages.profile.UMUserProfilePage;
import org.openqa.selenium.WebDriver;

public class UMCPUserProfile extends CPUserProfile {

    public UMCPUserProfile(WebDriver driver) {
        super(new UMUserProfilePage(driver));
    }

    @Override
    public String getProfilePhoneMaskedRegex() {
        return "\\*{3}\\d{3}$";
    }

}

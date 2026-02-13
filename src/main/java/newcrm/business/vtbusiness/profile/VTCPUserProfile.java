package newcrm.business.vtbusiness.profile;

import newcrm.business.businessbase.profile.CPUserProfile;
import newcrm.pages.vtclientpages.profile.VTUserProfilePage;
import org.openqa.selenium.WebDriver;

public class VTCPUserProfile extends CPUserProfile {

    public VTCPUserProfile(WebDriver driver) {
        super(new VTUserProfilePage(driver));
    }

    @Override
    public void checkProfileMainInfoMasking() {}

    @Override
    public String getProfilePhoneMaskedRegex() {
        return "\\*{3}\\d{3}$";
    }

}

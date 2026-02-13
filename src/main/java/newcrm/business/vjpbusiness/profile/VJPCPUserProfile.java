package newcrm.business.vjpbusiness.profile;

import newcrm.business.businessbase.profile.CPUserProfile;
import newcrm.pages.vjpclientpages.profile.VJPUserProfilePage;
import org.openqa.selenium.WebDriver;

public class VJPCPUserProfile extends CPUserProfile {

    public VJPCPUserProfile(WebDriver driver) {
        super(new VJPUserProfilePage(driver));
    }

    @Override
    public void checkProfileBoxInfoMasking() {}

}

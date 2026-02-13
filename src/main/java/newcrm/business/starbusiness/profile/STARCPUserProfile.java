package newcrm.business.starbusiness.profile;

import newcrm.business.businessbase.profile.CPUserProfile;
import newcrm.pages.starclientpages.profile.STARUserProfilePage;
import org.openqa.selenium.WebDriver;

public class STARCPUserProfile extends CPUserProfile {

    public STARCPUserProfile(WebDriver driver) {
        super(new STARUserProfilePage(driver));
    }

}

package newcrm.business.aubusiness.profile;

import newcrm.business.businessbase.profile.CPUserProfile;
import newcrm.pages.auclientpages.profile.AUUserProfilePage;
import org.openqa.selenium.WebDriver;

public class AUCPUserProfile extends CPUserProfile {

    public AUCPUserProfile(WebDriver driver) {
        super(new AUUserProfilePage(driver));
    }

    @Override
    public void checkProfileMainInfoMasking() {
//        userProfilePage.clickProfileSecurityMgmtTab();
        userProfilePage.waitLoadingProfileSecurityMgmtContent();
        checkProfileSecurityEmail();
        checkProfileSecurityPhone();
    }

    @Override
    public void checkVerificationTabContentBeforeCompleteLv1() {
        clickProfileVerificationTab();
        userProfilePage.checkProfileVStandardContent(new boolean[]{true, true, true}, new boolean[]{true, false, false});
        userProfilePage.checkProfileVPlusContent(new boolean[]{true, true}, new boolean[]{false, false});
    }

    @Override
    public void checkVerificationTabContentAfterCompleteLv1() {
        clickProfileVerificationTab();
        userProfilePage.checkProfileVStandardContent(new boolean[]{false, true, true}, new boolean[]{false, true, true});
        userProfilePage.checkProfileVPlusContent(new boolean[]{true, true}, new boolean[]{true, true});
    }

    @Override
    public void checkVerificationTabContentAfterCompleteLv2(boolean bIsUseSumsub) {
        clickProfileVerificationTab();
        userProfilePage.checkProfileVStandardContent(new boolean[]{false, true, true}, new boolean[]{false, false, true});
    }

    @Override
    public void checkVerificationTabContentAfterCompleteLv3() {
        clickProfileVerificationTab();
        userProfilePage.checkProfileVStandardContent(new boolean[]{false, false, true}, new boolean[]{false, false, true});
    }

    @Override
    public void checkVerificationTabContentAfterCompleteAdvance() {
        clickProfileVerificationTab();

        boolean[] visibleFlags = new boolean[]{true, true};
        boolean[] enableFlags = new boolean[]{false, true};

        userProfilePage.checkProfileVPlusContent(visibleFlags, enableFlags);
    }

}

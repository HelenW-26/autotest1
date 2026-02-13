package newcrm.business.businessbase.profile;

import newcrm.pages.clientpages.profile.UserProfilePage;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

public class CPUserProfile {

    protected UserProfilePage userProfilePage;

    public CPUserProfile(WebDriver driver) {
        this.userProfilePage = new UserProfilePage(driver);
    }

    public CPUserProfile(UserProfilePage v_page) {
        this.userProfilePage = v_page;
    }

    public void waitLoadingProfileTabListContent() {
        userProfilePage.waitLoadingProfileTabListContent();
        userProfilePage.waitLoading();
        userProfilePage.waitLoader();
        userProfilePage.waitLoading();
    }

    public void checkVerificationTabContentBeforeCompleteLv1() {
        clickProfileVerificationTab();
        userProfilePage.checkProfileVStandardContent(new boolean[]{true, true}, new boolean[]{true, false});
    }

    public void checkVerificationTabContentAfterCompleteLv1() {
        clickProfileVerificationTab();
        userProfilePage.checkProfileVStandardContent(new boolean[]{false, true}, new boolean[]{false, true});
    }

    public void checkVerificationTabContentAfterCompleteLv2(boolean bIsUseSumsub) {
        clickProfileVerificationTab();

        boolean[] visibleFlags = !bIsUseSumsub ? new boolean[]{false, true, true} : new boolean[]{false, true};
        boolean[] enableFlags = !bIsUseSumsub ? new boolean[]{false, false, true} : new boolean[]{false, true};

        userProfilePage.checkProfileVStandardContent(visibleFlags, enableFlags);
    }

    public void checkVerificationTabContentAfterCompleteAdvance() {}

    public void checkVerificationTabContentAfterCompleteLv3() {}

    public void clickProfileVerificationTab() {
        userProfilePage.clickProfileVerificationTab();
        userProfilePage.waitLoadingProfileVerificationContent();
    }

    public void clickProfileVTablv1VerifyBtn() {
        userProfilePage.clickProfileVStandardTab();
        userProfilePage.clickProfileVStandardTabVerifyBtn(0);
    }

    public void clickProfileVTablv2VerifyBtn() {
        userProfilePage.clickProfileVStandardTab();
        userProfilePage.clickProfileVStandardTabVerifyBtn(1);
    }

    public void clickProfileVTablv3VerifyBtn() {
        userProfilePage.clickProfileVStandardTab();
        userProfilePage.clickProfileVStandardTabVerifyBtn(2);
    }

    public void clickProfileVTabAdvanceVerifyBtn() {
        userProfilePage.clickProfileVPlusTab();
        userProfilePage.clickProfileVPlusTabVerifyBtn(0);
    }

    public void checkProfileBoxInfoMasking() {
        userProfilePage.waitLoadingProfileBoxContent();
        checkProfileEmail();
        checkProfilePhone();
    }

    public void checkProfileMainInfoMasking() {
//        userProfilePage.clickProfileSecurityMgmtTab();
        userProfilePage.waitLoadingProfileSecurityMgmtContent();
        checkProfileSecurityEmail();
    }

    public void checkProfileEmail() {
        String email = userProfilePage.getProfileEmail();
        checkMaskedContent(email, "Profile Email", getProfileEmailMaskedRegex());
    }

    public void checkProfilePhone() {
        String phone = userProfilePage.getProfilePhone();
        checkMaskedContent(phone, "Profile Phone", getProfilePhoneMaskedRegex());
    }

    public void checkProfileSecurityEmail() {
        String securityEmail = userProfilePage.getProfileSecurityEmail();
        checkMaskedContent(securityEmail, "Profile Email (Security)", getProfileSecurityEmailMaskedRegex());
    }

    public void checkProfileSecurityPhone() {
        String securityPhone = userProfilePage.getProfileSecurityPhone();
        checkMaskedContent(securityPhone, "Profile Email (Phone)", getProfilePhoneMaskedRegex());
    }

    public void checkMaskedContent(String maskedVal, String desc, String regex) {
        if (!maskedVal.matches(regex)) {
            Assert.fail(String.format("Invalid %s (%s) masked format", desc, maskedVal));
        }
    }

    public String getProfileEmailMaskedRegex() {
        return ".\\*{3}.@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    }

    public String getProfileSecurityEmailMaskedRegex() {
        return ".\\*{4}.@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    }

    public String getProfilePhoneMaskedRegex() {
        return "\\+\\d{1,3} \\*{3}\\d{3}$";
    }

}

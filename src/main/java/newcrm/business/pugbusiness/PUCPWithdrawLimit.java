package newcrm.business.pugbusiness;

import newcrm.business.businessbase.CPWithdrawLimit;
import newcrm.global.GlobalMethods;
import newcrm.pages.pugclientpages.PUWithdrawPage;
import org.openqa.selenium.WebDriver;

public class PUCPWithdrawLimit extends CPWithdrawLimit {

    public PUCPWithdrawLimit(WebDriver driver) {
        super(driver);
        this.wp = new PUWithdrawPage(driver);
    }

//    @Override
//    public boolean authApp(String pwd ){
//        try {
//            gotoSecurityMageTab();
//            wp.click2faEnableBtn();
//
//            wp.waitLoading();
//            String otpauthUri = wp.getQRcodefromCanvas();
//            enableApp(otpauthUri);
//
//            wp.click2faModifyBtn();
//            wp.clickPwdModifyConfirmBtn();
//
//            otpauthUri = wp.getQRcodefromCanvas();
//
//            enableApp(otpauthUri);
//            return true;
//        } catch (Exception e) {
//            GlobalMethods.printDebugInfo("Modify 2fa app failed");
//            return false;
//        }
//
//    }

}

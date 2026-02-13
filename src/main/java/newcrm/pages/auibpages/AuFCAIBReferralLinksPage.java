package newcrm.pages.auibpages;

import newcrm.global.GlobalMethods;
import newcrm.pages.ibpages.IBReferralLinksPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WindowType;
import org.testng.Assert;

public class AuFCAIBReferralLinksPage extends IBReferralLinksPage {

    public AuFCAIBReferralLinksPage(WebDriver driver) {
        super(driver);
    }

//    @Override
//    protected String ibDemoAccountDomain(){
//        return "https://www.vantagemarkets.co.uk/open-demo-account-crm/";
//    }

    @Override
    public void verifyReferralLinks(String affId, String testEnv){
        //FCA only verify Malay
        String originalHandle = driver.getWindowHandle();

        String expectedDownloadAppLink = "";
        if(testEnv.equalsIgnoreCase("alpha")){
            //Verify Long Links - Download App
            expectedDownloadAppLink = ibRefDownloadAppLink() + affId;
            if(!getIBRefPageDownloadAppLinkEle().getText().equals(expectedDownloadAppLink)){
                Assert.fail("Incorrect Download App link - Expected: " + expectedDownloadAppLink + " ; Actual: " + getIBRefPageDownloadAppLinkEle().getText());
            }
            GlobalMethods.printDebugInfo("Verified Download App link");
        }


            //Verify Long Links - Live, Demo, Home are correct
            if(getShortLinkToggleStatusEle().getAttribute("class").contains("is-checked")){
                triggerElementClickEvent_withoutMoveElement(getShortLinkToggleEle());
                waitLoading();
            }
            String currentLanguage = "Malay";

            //Verify Long Link - Live Account
            String expectedLiveAccLink = retrieveCurrentLanguageDomain(currentLanguage) + ibRefLiveAccountLinkComponent() + affId;
            if(!getIBRefPageLiveAccountLinkEle().getText().equals(expectedLiveAccLink)){
                Assert.fail("Incorrect Live Account Link - Expected: " + expectedLiveAccLink + " ; Actual: " + getIBRefPageLiveAccountLinkEle().getText());
            }

            //Verify Long Link - Demo Account
            String expectedDemoAccLink = retrieveCurrentLanguageDomain(currentLanguage) + ibRefDemoAccountLinkComponent() + affId;
            if(!getIBRefPageDemoAccountLinkEle().getText().equals(expectedDemoAccLink)){
                Assert.fail("Incorrect Demo Account Link - Expected: " + expectedDemoAccLink + " ; Actual: " + getIBRefPageDemoAccountLinkEle().getText());
            }

            //Verify Long Link - Home Page
            String expectedHomePageLink = retrieveCurrentLanguageDomain(currentLanguage) + ibRefHomeLinkComponent() + affId;
            if(!getIBRefPageHomePageLinkEle().getText().equals(expectedHomePageLink)){
                Assert.fail("Incorrect Demo Account Link for " + currentLanguage + "- Expected: " + expectedHomePageLink + " ; Actual: " + getIBRefPageHomePageLinkEle().getText());
            }

            //Verify if Short Link radio button is turned ON
            if(!getShortLinkToggleStatusEle().getAttribute("class").contains("is-checked")){
                triggerElementClickEvent_withoutMoveElement(getShortLinkToggleEle());
                waitLoading();
            }

            String liveAccShortLink = getIBRefPageLiveAccountLinkEle().getText();
            String demoAccShortLink = getIBRefPageDemoAccountLinkEle().getText();
            String homePageShortLink = getIBRefPageHomePageLinkEle().getText();

            //Verify Short Link - Live Account
            driver.switchTo().newWindow(WindowType.TAB);
            driver.get(liveAccShortLink);
            if(!getCurrentURL().equals(expectedLiveAccLink)){
                driver.get(liveAccShortLink);
                GlobalMethods.printDebugInfo("Short Live Account Link for "+currentLanguage+" redirected to wrong site. Attempting second time.");
                if(!getCurrentURL().equals(expectedLiveAccLink)){
                    Assert.fail("Short Live Account Link for "+currentLanguage+" redirected to wrong site - Expected: " + expectedLiveAccLink + " ; Actual (Short Link): " + liveAccShortLink + " ; Actual (re-directed): " + getCurrentURL());
                }else{
                    GlobalMethods.printDebugInfo("Able to be redirected to correct site on second attempt");
                }
            }
            driver.close();
            driver.switchTo().window(originalHandle);

            //Verify Short Link - Demo Account
            driver.switchTo().newWindow(WindowType.TAB);
            driver.get(demoAccShortLink);
            if(!getCurrentURL().equals(expectedDemoAccLink)){
                Assert.fail("Short Demo Account Link for "+currentLanguage+" redirected to wrong site - Expected: " + expectedDemoAccLink + " ; Actual (Short Link): " + demoAccShortLink + " ; Actual (re-directed): " + getCurrentURL());
            }
            driver.close();
            driver.switchTo().window(originalHandle);

            //Verify Short Link - Home Page
            driver.switchTo().newWindow(WindowType.TAB);
            driver.get(homePageShortLink);
            expectedHomePageLink = retrieveCurrentLanguageDomain(currentLanguage) + ibRefHomeLinkComponent() + affId.replace("=","%3D");
            if(!getCurrentURL().equals(expectedHomePageLink)){
                Assert.fail("Short Home Page Link for "+currentLanguage+" redirected to wrong site - Expected: " + expectedHomePageLink + " ; Actual (Short Link): " + homePageShortLink + " ; Actual (re-directed): " + getCurrentURL());
            }
            driver.close();
            driver.switchTo().window(originalHandle);

            GlobalMethods.printDebugInfo(currentLanguage + " - verified Live Account, Demo Account, Home Page links");

        if(testEnv.equalsIgnoreCase("alpha")){
            //Verify Short Link - Download App
            String downloadAppShortLink = getIBRefPageDownloadAppLinkEle().getText();
            driver.switchTo().newWindow(WindowType.TAB);
            driver.get(downloadAppShortLink);
            expectedDownloadAppLink = ibRefDownloadAppLink() + affId.replace("=","%3D");
//        expectedDownloadAppLink = expectedDownloadAppLink.replace("==","%3D%3D");
            if(!getCurrentURL().contains(expectedDownloadAppLink)){
                Assert.fail("Short Download App Link redirected to wrong site - Expected: " + expectedDownloadAppLink + " ; Actual: " + getCurrentURL());
            }
            driver.close();
            driver.switchTo().window(originalHandle);
        }

    }

}

package newcrm.pages.ibpages;

import newcrm.global.GlobalMethods;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import newcrm.pages.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import utils.LogUtils;

import java.util.List;

public class IBProfilePage extends Page {

    public IBProfilePage(WebDriver driver) {
        super(driver);
    }


    protected WebElement getIbAgreementTabEle() {
        return assertElementExists(By.xpath("//div[@id='tab-/ibagreement']"), "IB Agreement Tab");
    }

    protected WebElement getIBAgreementAccountEle() {
        return assertElementExists(By.xpath("//div[@class='ib-agreement-content']/div/div[2]/div"), "IB Agreement Account Number");
    }

    protected WebElement getIBAgreementSignedDateEle() {
        return assertElementExists(By.xpath("//div[@class='ib-agreement-content'] //span[@class='agreement-text']/span"), "IB Agreement Signed Date");
    }


    public void verifyIBAgreementDate(String ibAcc){
        triggerElementClickEvent_withoutMoveElement(getIbAgreementTabEle());
        String signedText = getIBAgreementSignedDateEle().getText().trim();
        String signedDate = signedText.split(" ")[0];

        //Get today's date
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = today.format(formatter);

        if(signedDate.equals(formattedDate)){
            LogUtils.info("IB Agreement signed date is correct: " + signedDate);
        } else {
            Assert.fail("IB Agreement signed date is incorrect. Expected: " + formattedDate + ", Actual: " + signedDate);
        }

        String agreementStatus = signedText.split(" ")[1].trim();

        if(agreementStatus.equals("Signed")){
            LogUtils.info("IB Agreement status is correct: " + agreementStatus);
        } else {
            Assert.fail("IB Agreement status is incorrect. Expected: Signed, Actual: " + agreementStatus);
        }

        String ibAgreementAccount = getIBAgreementAccountEle().getText().trim();

        if(ibAgreementAccount.equals(ibAcc)){
            LogUtils.info("IB Agreement Account Number is correct: " + ibAgreementAccount);
        } else {
            Assert.fail("IB Agreement Account Number is incorrect. Expected: " + ibAcc +", Actual: " + ibAgreementAccount);
        }

    }



}

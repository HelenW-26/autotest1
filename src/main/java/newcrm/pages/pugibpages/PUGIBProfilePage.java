package newcrm.pages.pugibpages;

import newcrm.pages.ibpages.IBProfilePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import utils.LogUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PUGIBProfilePage extends IBProfilePage {

    public PUGIBProfilePage(WebDriver driver) {
        super(driver);
    }


    protected WebElement getIBAgreementAccountNumberSignedDateEle() {
        return assertElementExists(By.xpath("//div[@class='info']"), "IB Agreement Account Number & Signed Date");
    }

    @Override
    public void verifyIBAgreementDate(String ibAcc){
        moveElementToVisible(getIBAgreementAccountNumberSignedDateEle());
        String signedText = getIBAgreementAccountNumberSignedDateEle().getText().trim();
        Integer signedTextLength = signedText.split(" ").length;
        String ibAgreementAccount = signedText.split(" ")[2].replace(".","");
        String signedDate = signedText.split(" ")[signedTextLength-1];

        //Get today's date
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyyy");
        String formattedDate = today.format(formatter);

        if(signedDate.equals(formattedDate)){
            LogUtils.info("IB Agreement signed date is correct: " + signedDate);
        } else {
            Assert.fail("IB Agreement signed date is incorrect. Expected: " + formattedDate + ", Actual: " + signedDate);
        }

        String agreementStatus = signedText.split(" ")[3].trim();

        if(agreementStatus.equals("Signed")){
            LogUtils.info("IB Agreement status is correct: " + agreementStatus);
        } else {
            Assert.fail("IB Agreement status is incorrect. Expected: Signed, Actual: " + agreementStatus);
        }

        if(ibAgreementAccount.equals(ibAcc)){
            LogUtils.info("IB Agreement Account Number is correct: " + ibAgreementAccount);
        } else {
            Assert.fail("IB Agreement Account Number is incorrect. Expected: " + ibAcc +", Actual: " + ibAgreementAccount);
        }

    }


}

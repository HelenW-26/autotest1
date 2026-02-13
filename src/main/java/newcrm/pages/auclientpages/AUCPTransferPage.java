package newcrm.pages.auclientpages;

import com.google.gson.Gson;
import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.TransferPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AUCPTransferPage extends TransferPage {

    public AUCPTransferPage(WebDriver driver)
    {
        super(driver);
    }

    @Override
    public void submit() {
        WebElement submit = driver.findElement(By.xpath("//button[@data-testid='submit']"));
        submit.click();
        this.waitLoading();
        this.findVisibleElemntByXpath("//div[contains(@class, 'result_info')]");
    }


    @Override
    public void clickUseCreditBtn(){
        WebElement confirmBtn = findClickableElementByXpath("//div[@class='el-dialog__footer']//button[.//span[normalize-space(text())='Transfer to accounts']]");
        confirmBtn.click();
    }

    public boolean checkTransferReview(String fromAccount,String toAccount, double amount,double processFee,Double finalAmount){
        this.waitLoading();

        Map<String, String> reviewValueMap = this.getTransferReviewValue();

        GlobalMethods.printDebugInfo("The value is: "+ new Gson().toJson(reviewValueMap));
        if(reviewValueMap.isEmpty()){
            return false;
        }

        String from = reviewValueMap.get("From Account");
        String to = reviewValueMap.get("To Account");

        if (!from.contains(fromAccount) || !to.contains(toAccount)){
            return false;
        }

        String transferAmount = getAmount(reviewValueMap.get("Transfer amount"));
        String processingFee = getAmount(reviewValueMap.get("Processing Fee"));

        if(finalAmount != null){
            String final_amount = getAmount(reviewValueMap.get("Final Amount"));
            if (!Double.valueOf(final_amount).equals(finalAmount)){
                return false;
            }
        }

        return Double.valueOf(transferAmount).equals(amount) && Double.valueOf(processingFee).equals(processFee);
    }

    protected String getAmount(String value) {

        value = value.replace(",", "");
        String regEx = "^(\\d+(?:\\.\\d{1,2})?)(?:\\s*[A-Z]{3,4})?$";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(value);
        String amount = "Nothing";
        if (matcher.find()) {
            amount = matcher.group(1);
        }
        return amount;
    }

}

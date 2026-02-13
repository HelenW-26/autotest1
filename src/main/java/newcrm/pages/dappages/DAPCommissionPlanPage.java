package newcrm.pages.dappages;

import newcrm.pages.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import utils.LogUtils;

import java.util.List;

public class DAPCommissionPlanPage extends Page {

    public DAPCommissionPlanPage(WebDriver driver) {
        super(driver);
    }


    protected WebElement getCommissionModeEle() {
        return assertElementExists(By.xpath("//div[text()='Commission model']/following-sibling::div/span"), "Commission Mode");
    }

    protected List<WebElement> getCommissionModelDescriptionListEle() {
        return assertElementsExists(By.xpath("//div[@class='mode-wrapper'] //tbody/tr/td[1]/div/span"), "Commission Model - Description List");
    }

    protected List<WebElement> getCommissionModelFromAmountListEle() {
        return assertElementsExists(By.xpath("//div[@class='mode-wrapper'] //tbody/tr/td[2]/div/span"), "Commission Model - 'From' Amount List");
    }

    protected List<WebElement> getCommissionModelToAmountListEle() {
        return assertElementsExists(By.xpath("//div[@class='mode-wrapper'] //tbody/tr/td[3]/div/div"), "Commission Model - 'To' Amount List");
    }

    protected List<WebElement> getCommissionModelCommissionAmountListEle() {
        return assertElementsExists(By.xpath("//div[@class='mode-wrapper'] //tbody/tr/td[4]/div/span"), "Commission Model - Commission Amount List");
    }

    protected WebElement getPrimaryQualificationNetDepositEle() {
        return assertElementExists(By.xpath("(//div[@class='eligibility-wrapper'] //div[@class='condition__detail-value'])[1]"), "(Primary) Qualification - Net Deposit");
    }

    protected WebElement getPrimaryQualificationIndicesLotEle() {
        return assertElementExists(By.xpath("(//div[@class='eligibility-wrapper'] //div[@class='condition__detail-value'])[2]"), "(Primary) Qualification - Indices Lot");
    }

    protected WebElement getPrimaryQualificationNonIndicesLotEle() {
        return assertElementExists(By.xpath("(//div[@class='eligibility-wrapper'] //div[@class='condition__detail-value'])[3]"), "(Primary) Qualification - Non Indices Lot");
    }

    protected WebElement getPrimaryQualificationPositionCountEle() {
        return assertElementExists(By.xpath("(//div[@class='eligibility-wrapper'] //div[@class='condition__detail-value'])[4]"), "(Primary) Qualification - Position Count");
    }

    protected WebElement getMultipleQualificationNetDepositEle() {
        return assertElementExists(By.xpath("(//div[@class='transfer-eligibility-wrapper'] //div[@class='condition__detail-value'])[1]"), "Multiple Qualification - Net Deposit");
    }

    protected WebElement getMultipleQualificationIndicesLotEle() {
        return assertElementExists(By.xpath("(//div[@class='transfer-eligibility-wrapper'] //div[@class='condition__detail-value'])[2]"), "Multiple Qualification - Indices Lot");
    }

    protected WebElement getMultipleQualificationNonIndicesLotEle() {
        return assertElementExists(By.xpath("(//div[@class='transfer-eligibility-wrapper'] //div[@class='condition__detail-value'])[3]"), "Multiple Qualification - Non Indices Lot");
    }

    protected WebElement getMultipleQualificationPositionCountEle() {
        return assertElementExists(By.xpath("(//div[@class='transfer-eligibility-wrapper'] //div[@class='condition__detail-value'])[4]"), "Multiple Qualification - Position Count");
    }

    protected WebElement getExtraBonusQualifyClientRangeEle() {
        return assertElementExists(By.xpath("//div[@class='extra-reward-wrapper'] //tr[@class='ht-table__row']/td[2]/div/div"), "Extra Bonus - Qualify Client Range");
    }

    protected WebElement getExtraBonusCommissionAmountEle() {
        return assertElementExists(By.xpath("//div[@class='extra-reward-wrapper'] //tr[@class='ht-table__row']/td[3]/div/span"), "Extra Bonus - Commission Amount");
    }

    public void verifyCommissionPlanPage(){
        verifyCommissionModel();
        verifyQualification();
        verifyMultipleQualification();
        verifyExtraBonus();
    }

    public void verifyCommissionModel(){
        //Verify Commission Model Section
        String commissionMode = getCommissionModeEle().getText();

        Assert.assertTrue(commissionMode.equalsIgnoreCase("ftd amount tiers"));

        List<String> commissionModelDescriptionListStr = getCommissionModelDescriptionListEle().stream().map(WebElement::getText).map(s -> s.replace("$", "").trim()).toList();
        Assert.assertTrue(!commissionModelDescriptionListStr.equals("0.00") && !commissionModelDescriptionListStr.contains("0"), "Commission Model - Description List contains invalid value - 0");
        Assert.assertTrue(getCommissionModelDescriptionListEle().size() == 5, "Commission Model - Description List count mismatch, expected to be 5 tier; Actual: " + getCommissionModelDescriptionListEle().size());

        List<String> commissionModelFromAmountListStr = getCommissionModelFromAmountListEle().stream().map(WebElement::getText).map(s -> s.replace("$", "").trim()).toList();
        Assert.assertTrue(!commissionModelFromAmountListStr.equals("0.00") && !commissionModelFromAmountListStr.contains("0"), "Commission Model - From Amount List contains invalid value - 0");
        Assert.assertTrue(getCommissionModelFromAmountListEle().size() == 5, "Commission Model - From Amount List count mismatch, expected to be 5 tier; Actual: " + getCommissionModelFromAmountListEle().size());

        List<String> commissionModelToAmountListStr = getCommissionModelToAmountListEle().stream().map(WebElement::getText).map(s -> s.replace("$", "").trim()).toList();
        Assert.assertTrue(!commissionModelToAmountListStr.equals("0.00") && !commissionModelToAmountListStr.contains("0"), "Commission Model - To Amount List contains invalid value - 0");
        Assert.assertTrue(getCommissionModelToAmountListEle().size() == 5, "Commission Model - To Amount List count mismatch, expected to be 5 tier; Actual: " + getCommissionModelToAmountListEle().size());

        List<String> commissionModelCommissionAmountListStr = getCommissionModelCommissionAmountListEle().stream().map(WebElement::getText).map(s -> s.replace("$", "").trim()).toList();
        Assert.assertTrue(!commissionModelCommissionAmountListStr.equals("0.00") && !commissionModelCommissionAmountListStr.contains("0"), "Commission Model - Commission Amount List contains invalid value - 0");
        Assert.assertTrue(getCommissionModelCommissionAmountListEle().size() == 5, "Commission Model - Commission Amount List count mismatch, expected to be 5 tier; Actual: " + getCommissionModelCommissionAmountListEle().size());

        LogUtils.info("Verified Commission Model Section, Commission Mode: " + commissionMode + ", with 5 tiers.");
    }

    public void verifyQualification(){
        //Verify (Primary) Qualification Section
        String primaryNetDeposit = getPrimaryQualificationNetDepositEle().getText().replace("$", "").trim();
        Assert.assertTrue(!primaryNetDeposit.equals("0.00") && !primaryNetDeposit.equals("0"), "(Primary) Qualification - Net Deposit has invalid value - 0");

        String primaryIndicesLot = getPrimaryQualificationIndicesLotEle().getText().trim();
        Assert.assertTrue(!primaryIndicesLot.equals("0.00") && !primaryIndicesLot.equals("0"), "(Primary) Qualification - Indices Lot has invalid value - 0");

        String primaryNonIndicesLot = getPrimaryQualificationNonIndicesLotEle().getText().trim();
        Assert.assertTrue(!primaryNonIndicesLot.equals("0.00") && !primaryNonIndicesLot.equals("0"), "(Primary) Qualification - Non Indices Lot has invalid value - 0");

        String primaryPositionCount = getPrimaryQualificationPositionCountEle().getText().trim();
        Assert.assertTrue(!primaryPositionCount.equals("0.00") && !primaryPositionCount.equals("0"), "(Primary) Qualification - Position Count has invalid value - 0");

        LogUtils.info("Verified (Primary) Qualification Section");
    }

    public void verifyMultipleQualification(){
        //Verify Multiple Qualification Section
        String multipleNetDeposit = getMultipleQualificationNetDepositEle().getText().replace("$", "").trim();
        Assert.assertTrue(!multipleNetDeposit.equals("0.00") && !multipleNetDeposit.equals("0"), "Multiple Qualification - Net Deposit has invalid value - 0");

        String multipleIndicesLot = getMultipleQualificationIndicesLotEle().getText().trim();
        Assert.assertTrue(!multipleIndicesLot.equals("0.00") && !multipleIndicesLot.equals("0"), "Multiple Qualification - Indices Lot has invalid value - 0");

        String multipleNonIndicesLot = getMultipleQualificationNonIndicesLotEle().getText().trim();
        Assert.assertTrue(!multipleNonIndicesLot.equals("0.00") && !multipleNonIndicesLot.equals("0"), "Multiple Qualification - Non Indices Lot has invalid value - 0");

        String multiplePositionCount = getMultipleQualificationPositionCountEle().getText().trim();
        Assert.assertTrue(!multiplePositionCount.equals("0.00") && !multiplePositionCount.equals("0"), "Multiple Qualification - Position Count has invalid value - 0");

        LogUtils.info("Verified Multiple Qualification Section");
    }

    public void verifyExtraBonus(){
        //Verify Extra Bonus Section
        String extraBonusQualifyClientRange = getExtraBonusQualifyClientRangeEle().getText().replace("$", "").trim();

        String[] parts = extraBonusQualifyClientRange.split("-");

        String first = parts[0].trim();
        String second = parts[1].trim();

        Assert.assertTrue(!first.equals("0.00") && !first.equals("0"), "Extra Bonus Range has invalid starting value - 0");
        Assert.assertTrue(!second.equals("0.00") && !second.equals("0"), "Extra Bonus Range has invalid ending value - 0");

        String extraBonusCommissionAmount = getExtraBonusCommissionAmountEle().getText().replace("$", "").trim();
        Assert.assertTrue(!extraBonusCommissionAmount.equals("0.00") && !extraBonusCommissionAmount.equals("0"), "Extra Bonus' Commission Amount has invalid value - 0");
        LogUtils.info("Verified Extra Bonus Section");
    }




}

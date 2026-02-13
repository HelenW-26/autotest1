package newcrm.business.businessbase;

import newcrm.pages.clientpages.PaymentDetailsPage;
import newcrm.pages.clientpages.withdraw.UnionPayWithdrawPage;
import org.openqa.selenium.WebDriver;
import vantagecrm.Utils;

public class CPUnionPayWithdraw extends CPWithdraw {

    protected PaymentDetailsPage ppage;
    protected UnionPayWithdrawPage unionpage;

    public CPUnionPayWithdraw(WebDriver driver) {
        super(new UnionPayWithdrawPage(driver));
        ppage = new PaymentDetailsPage(driver);
        unionpage = new UnionPayWithdrawPage(driver);
    }

    public PaymentDetailsPage.Card addNewUnionCard(String holderName, String id, String cardNum, String mobile, String branch) {
        PaymentDetailsPage.Card card = ppage.new Card();
        ppage.setPaymentMethod();
        card.bankName = ppage.setBankName();
        ppage.setCardHolderName(holderName);
        card.holderName=holderName;
        ppage.setIDCard(id);
        card.ID = id;
        ppage.setBankCardNum(cardNum);
        card.cardNum = cardNum;
        ppage.setMoblieNum(mobile);
        card.branch = ppage.setBranch(branch);

        String filename = "\\src\\main\\resources\\vantagecrm\\doc\\ID_Card.png";
        ppage.uploadCard(Utils.workingDir + filename);
        return card;
    }

    public boolean submitCardApply(String contain) {
        ppage.submit();
        String response = ppage.getResponse();
        if(response.contains(contain)) {
            return true;
        }
        return false;
    }

    public boolean check(PaymentDetailsPage.Card card) {
        ppage.waitLoading();
        PaymentDetailsPage.Card latest = ppage.getLatestCard();
        return card.equals(latest);
    }

    public void goBackHome() {
        ppage.backToHome();
    }

    public boolean hasCard() {
        return unionpage.hasCard();
    }

    public String selectCard() {
        return unionpage.selectCard();
    }

    public boolean submit() {
        return this.withdrawpage.submit();
    }

    public void confirmPopup() {
        ppage.confirmPopup();
    }

}

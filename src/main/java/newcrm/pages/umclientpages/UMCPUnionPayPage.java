package newcrm.pages.umclientpages;


import newcrm.pages.clientpages.DepositBasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.util.ArrayList;
import java.util.List;

public class UMCPUnionPayPage extends DepositBasePage {

	public UMCPUnionPayPage(WebDriver driver) {
		super(driver);
	}

	public List<String> checkPaymentDetail()
	{
		waitLoading();
		List<String> paymentDetail = new ArrayList<>();
		WebElement accountDetail = findClickableElementByXpath("//div[@data-testid='payToAccount']");
		moveElementToVisible(accountDetail);
		paymentDetail.add(accountDetail.getText());
		WebElement netDepositAmount = findClickableElementByXpath("//div[@data-testid='netDepositAmount']");
		moveElementToVisible(accountDetail);
		paymentDetail.add(netDepositAmount.getText());
		return paymentDetail;
	}
}

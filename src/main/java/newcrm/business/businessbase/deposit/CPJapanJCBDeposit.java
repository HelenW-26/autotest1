package newcrm.business.businessbase.deposit;

import newcrm.pages.vjpclientpages.VJPJapanJCBDepositPage;
import org.openqa.selenium.WebDriver;

public class CPJapanJCBDeposit extends CPLocalBankTrans{

	protected VJPJapanJCBDepositPage vjpjcppage;

	public CPJapanJCBDeposit(WebDriver driver) {
        super(driver);
        this.vjpjcppage = new VJPJapanJCBDepositPage(driver);
	}
	
    @Override
    public void deposit(String account, String amount, String notes, String taxid, String cardnum, String email) {
        vjpjcppage.selectAccount(account);
        vjpjcppage.setAmount(amount);
        vjpjcppage.setJCBEmail(email);
        vjpjcppage.setNotes(notes);
    }

    @Override
    public void submit(){
        vjpjcppage.submit();
    }
}

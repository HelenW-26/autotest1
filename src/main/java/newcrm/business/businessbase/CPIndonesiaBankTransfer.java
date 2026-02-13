package newcrm.business.businessbase;

import newcrm.pages.auclientpages.AuIndonesiaBankTransferPage;
import newcrm.pages.clientpages.deposit.IndonesiaBankTransferPage;
import org.openqa.selenium.WebDriver;

public class CPIndonesiaBankTransfer extends DepositBase{
    protected IndonesiaBankTransferPage indonesiaBankTransferPage;

    public CPIndonesiaBankTransfer(WebDriver driver)
    {
        super(new IndonesiaBankTransferPage(driver));
        this.indonesiaBankTransferPage = new IndonesiaBankTransferPage(driver);
    }

    public CPIndonesiaBankTransfer(AuIndonesiaBankTransferPage auIndonesiaBankTransferPage) 
    {
		super(auIndonesiaBankTransferPage);

		this.indonesiaBankTransferPage = auIndonesiaBankTransferPage;
	}

	@Override
    public void deposit(String account, String amount, String notes) {
        selectAccount(account);
        setAmount(amount);
        setNotes(notes);
    }

    public void selectBankTrasfer(String channel)
    {
        indonesiaBankTransferPage.selectBankTrasfer(channel);
    }

    public void setBank()
    {
        indonesiaBankTransferPage.setBank();
    }
}

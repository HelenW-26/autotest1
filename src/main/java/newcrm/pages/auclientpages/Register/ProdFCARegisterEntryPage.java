package newcrm.pages.auclientpages.Register;

import org.openqa.selenium.WebDriver;

import newcrm.pages.clientpages.register.RegisterEntryPage;

public class ProdFCARegisterEntryPage extends RegisterEntryPage {

	public ProdFCARegisterEntryPage(WebDriver driver) {
		super(driver);
	}
	@Override
	public void submit() {
		this.findClickableElementByXpath("//*[@id='cookieIdYes']").click();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.submit();
	}
}

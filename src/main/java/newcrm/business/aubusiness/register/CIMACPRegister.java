package newcrm.business.aubusiness.register;

import org.openqa.selenium.WebDriver;

import newcrm.business.businessbase.CPRegister;
import newcrm.pages.auclientpages.Register.CIMAFinancialDetailsPage;
import newcrm.pages.clientpages.register.ConfirmIDPage;
import vantagecrm.Utils;

public class CIMACPRegister extends CPRegister {
	private CIMAFinancialDetailsPage cima_fdpage;
	public CIMACPRegister(WebDriver driver, String url) {
		super(driver,url);
		cima_fdpage = new CIMAFinancialDetailsPage(driver);
	}
	
	@Override
	public void fillIDPage() {
		String fileFront = "\\src\\main\\resources\\vantagecrm\\doc\\ID_Card.png"; //Driver_License2.jpg//Passport.png;Prod_ID.jpeg
		String fileBack = "\\src\\main\\resources\\vantagecrm\\doc\\ID_Card_Back.png";
		String filePOA = "\\src\\main\\resources\\vantagecrm\\doc\\ID_Card.png";
		ConfirmIDPage idpage = new ConfirmIDPage(driver);
		idpage.uploadID(Utils.workingDir + fileFront);
		idpage.uploadID(Utils.workingDir + fileBack);
		idpage.uploadPOA(Utils.workingDir + filePOA);
		idpage.uploadPOA(Utils.workingDir + filePOA);
	}
	@Override
	public void fillFinacialPage() {
		cima_fdpage.answerAllQuestionsWithoutReturn();
	}
	
}

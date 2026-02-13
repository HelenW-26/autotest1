package newcrm.business.umbusiness;

import newcrm.business.businessbase.CPInternationalBankWithdraw;
import newcrm.business.businessbase.CPWithdraw;
import newcrm.pages.umclientpages.UMInternalBankWithdrawPage;
import org.openqa.selenium.WebDriver;

public class UMCPInternationalBankWithdraw extends CPInternationalBankWithdraw {
    public UMCPInternationalBankWithdraw(WebDriver driver)
    {
        super(new UMInternalBankWithdrawPage(driver));
    }

    @Override
    public void checkFileUploadingCompleted(){
        page.findVisibleElemntByXpath("//div[@class='upload-item-container']/img");
    }

    @Override
    public boolean setWithdrawInfo(String bank_name,String address,String bbName, String iban,
                                   String holderaddress,String swiftcode,String sortcode, String notes) {
		/*if(!page.chooseAddBankAccount()) {
			return false;
		}*/
//        if(!page.setRegionAsInternal()) {
//            return false;
//        }
        page.setBankName(bank_name);
        page.setBankAdress(address);
        page.setBeneficiaryName(bbName);
        page.setBankAccountNumber(iban);
        page.setHolderAddress(holderaddress);
        page.setSwift(swiftcode);
        page.setSortCode(sortcode);
        page.setNotes(notes);
        page.uploadStatement();

        return true;
    }

}

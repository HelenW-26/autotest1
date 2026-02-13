package newcrm.business.vtbusiness;

import newcrm.business.businessbase.CPInternationalBankWithdraw;
import newcrm.pages.vtclientpages.VTLocalBankWithdrawPage;
import newcrm.pages.vtclientpages.VTWithdrawBasePage;
import newcrm.pages.vtclientpages.VTWithdrawPage;
import org.openqa.selenium.WebDriver;

public class VTCPInternationalBankWithdraw extends CPInternationalBankWithdraw {

    VTLocalBankWithdrawPage vtLocalBankWithdrawPage;

    public VTCPInternationalBankWithdraw(WebDriver driver) {
        super(driver);
        this.not_cc_page = new VTWithdrawPage(driver);
        this.withdrawpage = new VTWithdrawBasePage(driver);
        vtLocalBankWithdrawPage = new VTLocalBankWithdrawPage(driver);
    }

    @Override
    public boolean setWithdrawInfo(String bank_name,String address,String bbName, String iban,
                                   String holderaddress,String swiftcode,String sortcode, String notes) {
        /*if(!page.chooseAddBankAccount()) {
            return false;
        }
        if(!page.setRegionAsInternal()) {
            return false;
        }*/
        page.setCPSBankName(bank_name);
        page.setCPSBankAdress(address);
        page.setCPSBeneficiaryName(bbName);
        page.setBankAccountNumber(iban);
        page.setCPSHolderAddress(holderaddress);
        page.setSwift(swiftcode);
        page.setCPSSortCode(sortcode);
        page.uploadStatement();
        vtLocalBankWithdrawPage.setImportantNotes(notes);

        return true;
    }

}

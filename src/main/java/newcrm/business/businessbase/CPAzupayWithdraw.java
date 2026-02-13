package newcrm.business.businessbase;

import newcrm.pages.auclientpages.AUAzupayWithdrawPage;

import java.util.List;

public class CPAzupayWithdraw extends CPLocalBankWithdraw {

    public CPAzupayWithdraw(AUAzupayWithdrawPage driver) {
        super(driver);
    }

    //Bank account drop down list is unavailable
    @Override
    public List<String> getSavedBankAccount() {
        return null;
    }

    @Override
    public boolean addNewBankAccount(String bank_branch, String acc_name, String acc_number, String city, String province, String ifsc, String notes, String accdigit, String docid, String swift_code) {
        page.setAccountType();
        page.setBsbCode(accdigit);
        page.setCustomerName("Test Customer Name");
        page.setBankAccountNumber(acc_number);
        page.setImportantNotes(notes);
        return true;
    }

    @Override
    public boolean submit() {
        return page.submit();
    }
}

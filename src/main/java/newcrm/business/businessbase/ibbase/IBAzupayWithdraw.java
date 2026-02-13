package newcrm.business.businessbase.ibbase;

import org.openqa.selenium.WebDriver;

import java.util.List;

public class IBAzupayWithdraw extends IBLocalBankTransfer{

    public IBAzupayWithdraw(WebDriver driver) {
        super(driver);
    }

    //Bank account drop down list is unavailable
    @Override
    public List<String> getSavedBankAccount() {
        return null;
    }

    @Override
    public boolean addNewBankAccount(String bank_branch, String acc_name, String acc_number, String city, String province,
                                     String ifsc, String notes, String accdigit, String docid, String swift_code,
                                     String docType, String accType, String bankName) {
        page.setAccountType(accType);
        page.setBsbCode(accdigit);
        page.setCustomerName("Test Customer Name");
        page.setBankAccountNumber(acc_number);
        page.setNotes(notes);
        return true;
    }


    public boolean submit() {
        return page.submit();
    }

}

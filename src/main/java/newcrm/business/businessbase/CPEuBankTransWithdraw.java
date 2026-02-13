package newcrm.business.businessbase;

import org.openqa.selenium.WebDriver;

public class CPEuBankTransWithdraw extends CPLocalBankWithdraw {

    public CPEuBankTransWithdraw(WebDriver driver) {
        super(driver);
    }

    public boolean setWithdrawInfoNew(String bankAddress,String bankBeneName, String bankAccNum, String bankHolderAddress, String bankSwiftCode, String notes) {
        if(!page.chooseAddBankAccount()) {
            return false;
        }

        page.setBankSwiftCode(bankSwiftCode);
        page.setBankName();
        page.setBankAddress(bankAddress);
        page.setHolderAddress(bankHolderAddress);
        page.setBankAccountName(bankBeneName);
        page.setBankAccountNumber(bankAccNum);
        page.setImportantNotes(notes);
        return true;
    }

    public boolean addNewBankAccount(String bank_branch, String acc_name, String acc_number, String city, String province, String ifsc, String notes, String accdigit, String docid, String swift_code) {
        page.setBankSwiftCode(swift_code);
        page.setBankName();
        page.setBankAddress(city);
        page.setHolderAddress(city);
        page.setBankAccountName(acc_name);
        page.setBankAccountNumber(acc_number);
        page.setImportantNotes(notes);
        return true;
    }

}

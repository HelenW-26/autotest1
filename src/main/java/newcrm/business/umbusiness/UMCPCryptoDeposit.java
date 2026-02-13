package newcrm.business.umbusiness;

import newcrm.business.businessbase.CPCryptoDeposit;
import newcrm.global.GlobalProperties;
import newcrm.pages.clientpages.deposit.CryptoDepositPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.HashMap;

public class UMCPCryptoDeposit extends CPCryptoDeposit {

	public UMCPCryptoDeposit(CryptoDepositPage cryptoDepositPage) {
            super(cryptoDepositPage);
            this.cryptoDepositPage = cryptoDepositPage;
        }

	public UMCPCryptoDeposit(WebDriver driver) {
            super(new CryptoDepositPage(driver));
            this.cryptoDepositPage = new CryptoDepositPage(driver);
        }

        public void goToDepositMethod(GlobalProperties.DEPOSITMETHOD method) {
            cryptoDepositPage.goToDepositMethod(method.getCPTestId());
        }
        public WebElement getCryptoType1() {

            return cryptoDepositPage.getCryptoType1();

        }

        public WebElement getCryptoType2() {
            return cryptoDepositPage.getCryptoType2();

        }

        public WebElement getCryptoType3() {
            return cryptoDepositPage.getCryptoType3();
        }

        public WebElement getCryptoType4() {
            return cryptoDepositPage.getCryptoType4();

        }


        public String getAmountFromThirdParty(GlobalProperties.DEPOSITMETHOD method, String amount) {
            switch(method) {
                case CRYPTOBIT: return cryptoDepositPage.getBTCAmountThirdParty(amount);
                case CRYPTOOMNI: return cryptoDepositPage.getUSDTOAmountThirdParty(amount);
                case CRYPTOERC: return cryptoDepositPage.getUSDTEAmountThirdParty(amount);
                case CRYPTOTRC: return cryptoDepositPage.getUSDTTAmountThirdParty(amount);
                default: return null;
            }
        }
        public String getBTCAmountThirdParty(String amount) {
            return cryptoDepositPage.getBTCAmountThirdParty(amount);
        }

        public String getUSDTOAmountThirdParty(String amount) {
            return cryptoDepositPage.getUSDTOAmountThirdParty(amount);
        }

        public String getUSDTEAmountThirdParty(String amount) {
            return cryptoDepositPage.getUSDTEAmountThirdParty(amount);
        }

        public String getUSDTTAmountThirdParty(String amount) {
            return cryptoDepositPage.getUSDTTAmountThirdParty(amount);

        }

        public void depositNew(String account, String amount, GlobalProperties.DEPOSITMETHOD depositMethod)
        {
            setAccountAndAmount(account, amount);
            setDepositMethod(depositMethod);
            clickContinue();
            checkPaymentDetailsNoDepositAmount(account,amount);
            payNow();
        }

        public void confirmButton() {
            cryptoDepositPage.confirmButton();
        }
        public void paymentConfirm() {
            cryptoDepositPage.paymentConfirm();
        }
        public boolean checkIfNavigateToThirdUrl() {

            if (!cryptoDepositPage.checkUrlContains(GlobalProperties.CRYPTOURL)) {
                return false;
            }
            return true;
        }
}

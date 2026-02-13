package newcrm.business.businessbase.wallet;

import newcrm.pages.clientpages.wallet.WalletCryptoHomePage;
import org.openqa.selenium.WebDriver;

import java.util.List;
import java.util.Map;

public class CPWalletCryptoHome {

    protected WalletCryptoHomePage walletHomePage;

    public CPWalletCryptoHome(WebDriver driver) {
        this.walletHomePage = new WalletCryptoHomePage(driver);
    }
    public void checkBalanceView() {walletHomePage.checkBalanceView();}
    public void checkBalanceViewUAT() {walletHomePage.checkBalanceViewUAT();}
    public void checkBalanceBtn() {walletHomePage.checkBalanceBtn();}
    public void checkBalanceBtnUAT() {walletHomePage.checkBalanceBtnUAT();}
    public void wallectIntoDeposit(){walletHomePage.wallectIntoDeposit();}
    public void validateWalletTable(){walletHomePage.validateWalletTable();}
    public void validateWalletTableFunding(){walletHomePage.validateWalletTableFunding();}
    public void validateCryptoData(String cryptoName){walletHomePage.validateCryptoData(cryptoName);}
    public void validateCryptoDataUAT(String cryptoName){walletHomePage.validateCryptoDataUAT(cryptoName);}
    public void checkBalanceBtnDeposit(){walletHomePage.checkBalanceBtnDeposit();}
    public void checkBalanceBtnDepositUAT(){walletHomePage.checkBalanceBtnDepositUAT();}
    public void selectCrypto(String cryptoName){ walletHomePage.selectCrypto(cryptoName);}
    public void selectCryptoUAT(String cryptoName){ walletHomePage.selectCryptoUAT(cryptoName);}
    public void selectCryptoEqualValue(String coinName){ walletHomePage.selectCryptoEqualValue(coinName);}
    public void selectCryptoEqualValueUAT(String coinName){ walletHomePage.selectCryptoEqualValueUAT(coinName);}
    public void wallectDepositOptions(String coinName, String networkName){ walletHomePage.wallectDepositOptions(coinName, networkName);}
    public void wallectIntoWithdraw(){walletHomePage.wallectIntoWithdraw();}
    public void wallectWithdrawCreatOrder(String coinName, String Address, String chainName, String amount){ walletHomePage.wallectWithdrawCreatOrder(coinName, Address, chainName, amount);}
    public void checkWithdrawReviewFrom(String coinName, String Address, String chainName, String amount){ walletHomePage.checkWithdrawReviewFrom(coinName, Address, chainName, amount);}
    public void walletFundingPage(){walletHomePage.walletFundingPage();}
    public Map<String,String> getCryptoAvailableBalance(){return walletHomePage.getCryptoAvailableBalance();}
    public void wallectWithdrawCreatRealOrderUSDT() {
        walletHomePage.wallectWithdrawCreatOrder("USDT", "0x416aa0bd8f255ecd91064b88a72eeeec65e780ca", "BSC（BEP20）", "30");
        walletHomePage.checkWithdrawReviewFrom("V-Wallet (USDT)", "0x416aa0bd8f255ecd91064b88a72eeeec65e780ca", "BSC（BEP20）", "30.0000 USDT");
    }
    public void walletOverview(){
        walletHomePage.checkVWalletOverview();
    }
    public void walletFunding(){
        walletHomePage.checkVWalletFunding();
    }
    public void walletEarn(){
        walletHomePage.checkVWalletEarn();
    }
}

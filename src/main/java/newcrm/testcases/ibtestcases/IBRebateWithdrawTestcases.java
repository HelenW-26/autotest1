package newcrm.testcases.ibtestcases;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.List;

import newcrm.business.businessbase.CPLogin;
import newcrm.factor.Factor;
import newcrm.testcases.IBBaseTestCaseNew;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONObject;

import newcrm.business.businessbase.CPMenu;
import newcrm.business.businessbase.ibbase.*;
import newcrm.business.dbbusiness.EmailDB;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.global.GlobalProperties.CPMenuName;
import newcrm.global.GlobalProperties.DEPOSITMETHOD;
import newcrm.utils.testCaseDescUtils;
import newcrm.pages.ibpages.RebateWithdrawBasePage.Account;

public class IBRebateWithdrawTestcases extends IBBaseTestCaseNew {

	protected EmailDB emailDB;
	private String rebateAcc;
    private Factor myfactor;
    private CPLogin login;
    private WebDriver driver;
    private String TraderName;
    @BeforeMethod(alwaysRun = true)
    public void initMethod(){

        driver = getDriverNew();
        myfactor = getFactorNew();
        login = getLogin();
        TraderName = getConfigNew().TraderName;

    }
	private Double fillWithdrawbasicInfo(DEPOSITMETHOD method) {		
		IBEmailWithdraw withdraw = myfactor.newInstance(IBEmailWithdraw.class);
		String note = "test note " + method.getIBWithdrawName();
		
		if(emailDB==null) {
			emailDB = new EmailDB(dbenv, dbBrand, dbRegulator);
		}
		
		Double amount = this.filleBankTransfer(method);
		this.selectEWalletWithdrawMethod(withdraw, method);
		if(GlobalProperties.brand.equalsIgnoreCase("mo") || GlobalProperties.brand.equalsIgnoreCase("vjp"))
		{
			setCode(emailDB);
		}
		return amount;
	}

	private Double fillWithdrawbasicInfoNew(DEPOSITMETHOD method) {
		IBEmailWithdraw withdraw = myfactor.newInstance(IBEmailWithdraw.class);

        Double amount = this.filleBankTransferNew(method);
		withdraw.clickContinue();
		this.selectEWalletWithdrawMethodNew(withdraw, method);

		return amount;
	}
	
	/**Click withdraw
	 * Set rebate account
	 * Set amount
	 */	
	private Double filleBankTransfer(DEPOSITMETHOD method) {
		//instance
		CPMenu menu = myfactor.newInstance(CPMenu.class);
		IBEmailWithdraw withdraw = myfactor.newInstance(IBEmailWithdraw.class);
		//data
		Account acc = null;
				
		//Refresh for getting rid of Ads
		menu.refresh();
		menu.goToMenu(CPMenuName.IBDASHBOARD);
		withdraw.clickWithdraw();
			
		List<Account> accounts = withdraw.getAllRebateAccounts();
		double amount = 0.0;
		for(Account a : accounts) {
			if(a.balance>0) {
				assertTrue(withdraw.setRebateAccount(a.accNum),"Set Account failed");
				rebateAcc = a.accNum;
				int balance = (int) Math.floor(a.balance);
				if(balance>30) {
					//amount=random.nextInt(balance-30)+30;
					amount = (int) (100 + Math.random() * 100);
				}else {
						amount=a.balance;
					}
						
				withdraw.setAmount(amount);
				acc = a;
				break;
			}
		}
		assertNotNull(acc,"Have not found an available account");
		return amount;
	}

	private Double filleBankTransferNew(DEPOSITMETHOD method) {
		//instance
		CPMenu menu = myfactor.newInstance(CPMenu.class);
		IBEmailWithdraw withdraw = myfactor.newInstance(IBEmailWithdraw.class);
		//data
		Account acc = null;

		//Refresh for getting rid of Ads
		//menu.refresh();
		login.goToCpHome();
		menu.goToMenu(CPMenuName.IBDASHBOARD);
		withdraw.clickWithdraw();
		List<Account> accounts = withdraw.getAllRebateAccountsNew();
		double amount = 0.0;
		for(Account a : accounts) {
			if(a.balance>0) {
				assertTrue(withdraw.setRebateAccountNew(a.accNum),"Set Account failed");
				rebateAcc = a.accNum;
				int balance = (int) Math.floor(a.balance);
				if(balance>30) {
					//amount=random.nextInt(balance-30)+30;
					amount = (int) (100 + Math.random() * 100);
				}else {
					amount=a.balance;
				}

				withdraw.setAmount(amount);
				acc = a;
				break;
			}
		}
		assertNotNull(acc,"Have not found an available account");
		return amount;
	}
	
	/**
	 * @author ShanL
	 * There are four types of withdrawal in PU
	 * This is for localBank(including Union Pay and Local Bank Transfer)	 
	 * */
	private void selectLocalTransferWithdrawMethod(IBEmailWithdraw withdraw, DEPOSITMETHOD method) {
		if(GlobalProperties.brand.equalsIgnoreCase("pug"))
		{
			withdraw.clickLocalTransfer();
		}		
		assertTrue(withdraw.setWithdrawMethod(method));
	}

	private void selectLocalTransferWithdrawMethodNew(IBEmailWithdraw withdraw, DEPOSITMETHOD method) {
		assertTrue(withdraw.setWithdrawMethodNew(method));
	}
	
	/**
	 * @author ShanL
	 * There are four types of withdrawal in PU
	 * This is for eWallet(including Fasapay, Neteller, Astropay, etc.)	 
	 * */
	private void selectEWalletWithdrawMethod(IBEmailWithdraw withdraw, DEPOSITMETHOD method) {
		if(GlobalProperties.brand.equalsIgnoreCase("pug"))
		{
			withdraw.clickEWallet();
		}		
		assertTrue(withdraw.setWithdrawMethod(method));
	}

	private void selectEWalletWithdrawMethodNew(IBEmailWithdraw withdraw, DEPOSITMETHOD method) {
		assertTrue(withdraw.setWithdrawMethodNew(method));
	}
	
	private void selectCryptoWithdrawMethod(IBEmailWithdraw withdraw, DEPOSITMETHOD method) {
		if(GlobalProperties.brand.equalsIgnoreCase("pug"))
		{
			withdraw.clickCrypto();
		}		
		assertTrue(withdraw.setWithdrawMethod(method));
	}

	private void selectCryptoWithdrawMethodNew(IBEmailWithdraw withdraw, DEPOSITMETHOD method) {
		assertTrue(withdraw.setWithdrawMethodNew(method));
	}
	
	private void selectIBTWithdrawMethod(IBEmailWithdraw withdraw, DEPOSITMETHOD method) {
		if(GlobalProperties.brand.equalsIgnoreCase("pug"))
		{
			withdraw.clickIBT();
		}		
		assertTrue(withdraw.setWithdrawMethod(method));
	}

	private void selectIBTWithdrawMethodNew(IBEmailWithdraw withdraw, DEPOSITMETHOD method) {
		assertTrue(withdraw.setWithdrawMethodNew(method));
	}

	private boolean checkHistory(Double amount,DEPOSITMETHOD method, String accNum) {
		IBTransactionHistory history = myfactor.newInstance(IBTransactionHistory.class);
		CPMenu menu = myfactor.newInstance(CPMenu.class);
		menu.goToMenu(CPMenuName.IBTRANSACTIONHISTORY);
		history.setRebateAccount(accNum);
		return history.compareWithdraw(amount, method, menu);
	}
	
	private void testOtherBankNewAccountRWitdraw(IBLocalBankTransfer bank_w, DEPOSITMETHOD wType) {
		Double amount = this.filleBankTransfer(wType);
		IBEmailWithdraw withdraw = myfactor.newInstance(IBEmailWithdraw.class);
		this.selectLocalTransferWithdrawMethod(withdraw, wType);
		
		if(emailDB==null) {
			emailDB = new EmailDB(dbenv, dbBrand, dbRegulator);
		}
		
		//testdata
		String branch = "autotest Branch";
		String accName = "autotest accountName";
		String accNumber = "3463821905673";
		String city = "autotest city";
		String province = "autotest province";
		String notes = "autotest notes";
		String ifsc = "test ifsc";
		String accdigit = "123123";
		String docid = "123123";
		String swift_code = "test swift code";
		String docType = "test doc type";
		String accType = "test acct type";
		String bankName = "test bank name";
		List<String> sel_bank = null;
		
		sel_bank = bank_w.getSavedBankAccount();

		// if has saved bank account,use it. otherwise add a new one
		if (sel_bank == null) {
			// add new bank account
			bank_w.addNewBankAccount(branch, accName, accNumber, city, province, ifsc, notes, accdigit, docid, swift_code, docType, accType, bankName);
		} else {
			bank_w.selectNewBankAccount(sel_bank.get(0));
			bank_w.addNewBankAccount(branch, accName, accNumber, city, province, ifsc, notes, accdigit, docid, swift_code, docType, accType, bankName);
		}
	
		//if(GlobalProperties.brand.equalsIgnoreCase("mo")||GlobalProperties.brand.equalsIgnoreCase("pug"))
		if(GlobalProperties.brand.equalsIgnoreCase("mo"))
		{
			setCode(emailDB);
		}
		
		Assert.assertTrue(withdraw.submit(),
				"Submit failure or error occurs! Please make sure correct withdraw info configured in disconf (biz_withdraw.properties)");

		
		assertTrue(this.checkHistory(amount, wType, rebateAcc),"Have not found the withdraw history");
		GlobalMethods.printDebugInfo("*****Test " + wType.getWithdrawName() + " new account withdraw succeed.");
		
	}
	
	private void testOtherBankRWitdraw(IBLocalBankTransfer bank_w, DEPOSITMETHOD wType) {
		Double amount = this.filleBankTransfer(wType);
		IBEmailWithdraw withdraw = myfactor.newInstance(IBEmailWithdraw.class);
		this.selectLocalTransferWithdrawMethod(withdraw, wType);
		
		if(emailDB==null) {
			emailDB = new EmailDB(dbenv, dbBrand, dbRegulator);
		}
		
		//testdata
		String branch = "test Branch";
		String accName = "Test account name";
		String accNumber = "3463821905673";
		String city = "test city";
		String province = "test province";
		String notes = "autotest notes";
		String ifsc = "test ifsc";
		String accdigit = "123123";
		String docid = "123123";
		String swift_code = "test swift code";
		String docType = "test doc type";
		String accType = "test acct type";
		String bankName = "test bank name";
		List<String> sel_bank = null;
		
		if(!wType.name().equals("LOCALDEPOSITOR"))
		{
			sel_bank = bank_w.getSavedBankAccount();
		}

		// if has saved bank account,use it. otherwise add a new one
		if (sel_bank == null) {
			// add new bank account
			bank_w.addNewBankAccount(branch, accName, accNumber, city, province, ifsc, notes, accdigit, docid, swift_code, docType, accType, bankName);
		} else {
			bank_w.selectSavedBankAccount(sel_bank.get(0));
		}
	
		/*if(GlobalProperties.brand.equalsIgnoreCase("mo"))
		{
			setCode(emailDB);
		}*/
		
		Assert.assertTrue(withdraw.submit(),
				"Submit failure or error occurs! Please make sure correct withdraw info configured in disconf (biz_withdraw.properties)");
		
		assertTrue(this.checkHistory(amount, wType, rebateAcc),"Have not found the withdraw history");
		GlobalMethods.printDebugInfo("*****Test " + wType.getWithdrawName() + " withdraw succeed.");
		
	}
	
	@Test(priority = 0)
	public void testEbuyWithdraw() {
		IBCPSEwalletWithdraw ewallet_withdraw = myfactor.newInstance(IBCPSEwalletWithdraw.class);
		Double amount = this.fillWithdrawbasicInfo(DEPOSITMETHOD.EBuyTRANSFER);
		ewallet_withdraw.setWithdrawAccount("cpsEwallet@test.com");
		ewallet_withdraw.submit();
		
		//check		
		assertTrue(this.checkHistory(amount, DEPOSITMETHOD.EBuyTRANSFER, rebateAcc),"Have not found the withdraw history");
		GlobalMethods.printDebugInfo("Test IB Ebuy withdraw succeed.");
	}
	
	@Test(priority = 0)
	public void testPerfectMoneyWithdraw() {
		IBCPSEwalletWithdraw ewallet_withdraw = myfactor.newInstance(IBCPSEwalletWithdraw.class);
		Double amount = this.fillWithdrawbasicInfo(DEPOSITMETHOD.PERFECTMONEY);
		ewallet_withdraw.setWithdrawAccount("cpsEwallet@test.com");
		ewallet_withdraw.submit();
		
		//check		
		assertTrue(this.checkHistory(amount, DEPOSITMETHOD.PERFECTMONEY, rebateAcc),"Have not found the withdraw history");
		GlobalMethods.printDebugInfo("Test IB PERFECT MONEY withdraw succeed.");
	}

	@Test(priority = 0)
	public void testAdvcashWithdraw() {
		IBCPSEwalletWithdraw ewallet_withdraw = myfactor.newInstance(IBCPSEwalletWithdraw.class);
		Double amount = this.fillWithdrawbasicInfo(DEPOSITMETHOD.ADVCASH);
		ewallet_withdraw.setWithdrawAccount("cpsEwallet@test.com");
		ewallet_withdraw.submit();
		
		//check		
		assertTrue(this.checkHistory(amount, DEPOSITMETHOD.ADVCASH, rebateAcc),"Have not found the withdraw history");
		GlobalMethods.printDebugInfo("Test IB ADVCASH withdraw succeed.");
	}

	@Test(priority = 0)
	public void testAstropayWithdraw() {
		IBCPSEwalletWithdraw ewallet_withdraw = myfactor.newInstance(IBCPSEwalletWithdraw.class);
		Double amount = this.fillWithdrawbasicInfo(DEPOSITMETHOD.ASTROPAY);
		ewallet_withdraw.setWithdrawAccount("cpsEwallet@test.com");
		ewallet_withdraw.submit();
		
		//check		
		assertTrue(this.checkHistory(amount, DEPOSITMETHOD.ASTROPAY, rebateAcc),"Have not found the withdraw history");
		GlobalMethods.printDebugInfo("Test IB ASTROPAY withdraw succeed.");
	}

	@Test(priority = 0)
	public void testFasaPayWithdraw() {
		IBCPSEwalletWithdraw ewallet_withdraw = myfactor.newInstance(IBCPSEwalletWithdraw.class);
		Double amount = this.fillWithdrawbasicInfo(DEPOSITMETHOD.FASAPAY);
		ewallet_withdraw.setWithdrawAccount("cpsEwallet@test.com");
		ewallet_withdraw.submit();
		
		//check		
		assertTrue(this.checkHistory(amount, DEPOSITMETHOD.FASAPAY, rebateAcc),"Have not found the withdraw history");
		GlobalMethods.printDebugInfo("Test IB FASAPAY withdraw succeed.");
	}
				
	@Test(priority = 0)
	public void testSkrillWithdraw() {
		IBCPSEwalletWithdraw ewallet_withdraw = myfactor.newInstance(IBCPSEwalletWithdraw.class);
		Double amount = this.fillWithdrawbasicInfo(DEPOSITMETHOD.SKRILL);
		ewallet_withdraw.setWithdrawAccount("cpsEwallet@test.com");
		ewallet_withdraw.submit();
		
		//check		
		assertTrue(this.checkHistory(amount, DEPOSITMETHOD.SKRILL, rebateAcc),"Have not found the withdraw history");
		GlobalMethods.printDebugInfo("Test Skrill withdraw succeed.");
	}
	
	@Test(priority = 0)
	public void testNetellerWithdraw() {
		IBCPSEwalletWithdraw ewallet_withdraw = myfactor.newInstance(IBCPSEwalletWithdraw.class);
		Double amount = this.fillWithdrawbasicInfo(DEPOSITMETHOD.NETELLER);
		ewallet_withdraw.setWithdrawAccount("cpsEwallet@test.com");
		ewallet_withdraw.submit();
	
		//check
		assertTrue(this.checkHistory(amount, DEPOSITMETHOD.NETELLER, rebateAcc),"Have not found the withdraw history");
		GlobalMethods.printDebugInfo("Test Neteller withdraw succeed.");
	}
	
	@Test(priority = 0)
	public void testSticPayWithdraw() {
		IBCPSEwalletWithdraw ewallet_withdraw = myfactor.newInstance(IBCPSEwalletWithdraw.class);
		Double amount = this.fillWithdrawbasicInfo(DEPOSITMETHOD.STICPAY);
		ewallet_withdraw.setWithdrawAccount("ibsticpay@test.com");
		ewallet_withdraw.submit();
		
		//check
		assertTrue(this.checkHistory(amount, DEPOSITMETHOD.STICPAY, rebateAcc),"Have not found the withdraw history");
		GlobalMethods.printDebugInfo("Test sticpay withdraw succeed.");
	}

	@Test(priority = 0)
	public void testAirTmWithdraw() {
		IBCPSEwalletWithdraw ewallet_withdraw = myfactor.newInstance(IBCPSEwalletWithdraw.class);
		Double amount = this.fillWithdrawbasicInfo(DEPOSITMETHOD.AirTM);
		ewallet_withdraw.setWithdrawAccount("ibairtm@test.com");
		ewallet_withdraw.submit();
		
		//check
		assertTrue(this.checkHistory(amount, DEPOSITMETHOD.AirTM, rebateAcc),"Have not found the withdraw history");
		GlobalMethods.printDebugInfo("Test AirTM withdraw succeed.");
	}
	
	@Test(priority = 0)
	public void testFXIRWithdraw() {
		IBFXIRWithdraw ewallet_withdraw = myfactor.newInstance(IBFXIRWithdraw.class);
		Double amount = this.fillWithdrawbasicInfo(DEPOSITMETHOD.FXIR);
		ewallet_withdraw.setWithdrawAccount("ibfxir@test.com");
		ewallet_withdraw.submit();
		
		//check
		assertTrue(this.checkHistory(amount, DEPOSITMETHOD.FXIR, rebateAcc),"Have not found the withdraw history");
		GlobalMethods.printDebugInfo("Test FX-IR withdraw succeed.");
	}
	
	@Test(priority = 0)
	public void testJapanBTEmailWithdraw() {
		IBCPSEwalletWithdraw ewallet_withdraw = myfactor.newInstance(IBCPSEwalletWithdraw.class);
		Double amount = this.fillWithdrawbasicInfo(DEPOSITMETHOD.JAPANBTEMAIL);
		ewallet_withdraw.setWithdrawAccount("cpsEwallet@test.com");
		ewallet_withdraw.submit();
	
		//check
		assertTrue(this.checkHistory(amount, DEPOSITMETHOD.JAPANBTEMAIL, rebateAcc),"Have not found the withdraw history");
		GlobalMethods.printDebugInfo("Test Japan Bank Transfer (Email) withdraw succeed.");
	}
	
	@Test(priority = 0)
	public void testCryptoCurrencyWithdraw() {
		testCommonIBCryptoWithdraw(DEPOSITMETHOD.CRYPTOBIT);
		testCommonIBCryptoWithdraw(DEPOSITMETHOD.CRYPTOERC);
		if (GlobalProperties.brand.equalsIgnoreCase("pug"))
		{
			testCommonIBCryptoWithdraw(DEPOSITMETHOD.ETH);
			testCommonIBCryptoWithdraw(DEPOSITMETHOD.USDC);
		}
		else if (GlobalProperties.brand.equalsIgnoreCase("star"))
		{
			testCommonIBCryptoWithdraw(DEPOSITMETHOD.ETH);
		}
	}
	
	public void testCommonIBCryptoWithdraw(DEPOSITMETHOD wType) {
		Double amount = this.filleBankTransfer(wType);
		IBCryptoBitcoinWithdraw crypto_withdraw = myfactor.newInstance(IBCryptoBitcoinWithdraw.class);
		this.selectCryptoWithdrawMethod(crypto_withdraw, wType);
				
		if (wType.name().equals("CRYPTOBIT")) {
			crypto_withdraw.setCryptoWithdrawalInfo("3BWjWCkDBDuzB3bW3dTSEwCWht1EocvZct");
		}
		else if (wType.name().equals("CRYPTOERC")) {
			crypto_withdraw.setCryptoWithdrawalInfo("0x8E6fd509F491152bD377854ec3CeD86e96c2e94e","ERC20");
		}
		else if (wType.name().equals("ETH")) {
			crypto_withdraw.setCryptoWithdrawalInfo("0xC6067650a116153E6123Bb252A28252b9ee3eE1c");
		}
		else if (wType.name().equals("USDC")) {
			crypto_withdraw.setCryptoWithdrawalInfo("0x6dba6f6b122038854e299c3033757aa681ec2170");
		}
		
		if(!GlobalProperties.brand.equalsIgnoreCase("pug") && !GlobalProperties.brand.equalsIgnoreCase("vfx") && !GlobalProperties.brand.equalsIgnoreCase("vt"))
		{
			if(emailDB==null) {
				emailDB = new EmailDB(dbenv, dbBrand, dbRegulator);
			}
			setCode(emailDB);
		}
		
		Assert.assertTrue(crypto_withdraw.submit(),"Submit failure or error occurs! Please make sure correct withdraw info configured in disconf (biz_withdraw.properties)");
		Assert.assertTrue(this.checkHistory(amount, wType, rebateAcc),"Have not found the withdraw history");
		GlobalMethods.printDebugInfo("***Test " + wType.getIBWithdrawName() + " withdraw succeed!!********");
	}
	
	@Test(priority = 0)
	public void testBitwalletWithdraw() {
		IBCPSEwalletWithdraw ewallet_withdraw = myfactor.newInstance(IBCPSEwalletWithdraw.class);
		Double amount = this.fillWithdrawbasicInfo(DEPOSITMETHOD.BITWALLET);
		ewallet_withdraw.setWithdrawAccount("ibbitwallet@test.com");
		ewallet_withdraw.submit();
		//check
		assertTrue(this.checkHistory(amount, DEPOSITMETHOD.BITWALLET, rebateAcc),"Have not found the withdraw history");
		GlobalMethods.printDebugInfo("*  Test Bitwallet withdraw succeed.");
	}

	@Test(priority = 0)
	public void testUnionPayWithdraw() {
		IBUnionPayWithdraw union_withdraw = myfactor.newInstance(IBUnionPayWithdraw.class);
		Double amount = this.fillWithdrawbasicInfo(DEPOSITMETHOD.UNIONPAY);
		
		if(union_withdraw.checkUnionPopOut()) {
			union_withdraw.setbankInfo("中国银行", "5454334242535", "朝阳区支行", "自动化测试", "union pay 出金测试");
			union_withdraw.addSuccessPopup();
			union_withdraw.setWithdrawMethod(DEPOSITMETHOD.UNIONPAY);
		}else {
			GlobalMethods.printDebugInfo("*Already have Unionpay Card, no need bind card");
			union_withdraw.unionPayNotification();
			union_withdraw.submit();
			//check
			assertTrue(this.checkHistory(amount, DEPOSITMETHOD.UNIONPAY, rebateAcc),"Have not found the withdraw history");
		}
		GlobalMethods.printDebugInfo("*  Test Unionpay withdraw succeed.");
	}
	
	@Test(priority = 0)
	public void testMalaysiaBankWithdraw() {
		IBMalaysiaBankTransfer bank_w = myfactor.newInstance(IBMalaysiaBankTransfer.class);
		testOtherBankRWitdraw(bank_w, DEPOSITMETHOD.MALAYINSTANT);
	}
	
	@Test(priority = 0)
	public void testMalaysiaBankWithdrawNewAccount() {
		IBMalaysiaBankTransfer bank_w = myfactor.newInstance(IBMalaysiaBankTransfer.class);
		testOtherBankNewAccountRWitdraw(bank_w, DEPOSITMETHOD.MALAYINSTANT);
	}
	
	//PM confirmed that Ghana input fields between PUG and VFX will be different
	@Test(priority = 0)
	public void testGhanaBankWithdraw() {
		if(GlobalProperties.brand.equalsIgnoreCase("pug")) {
			IBKoreaBankTransfer bank_w = myfactor.newInstance(IBKoreaBankTransfer.class);
			testOtherBankRWitdraw(bank_w, DEPOSITMETHOD.GHANAINSTANT);
		}else {
			IBMalaysiaBankTransfer bank_w = myfactor.newInstance(IBMalaysiaBankTransfer.class);
			testOtherBankRWitdraw(bank_w, DEPOSITMETHOD.GHANAINSTANT);
		}
	}
	
	@Test(priority = 0)
	public void testGhanaBankWithdrawNewAccount() {
		if(GlobalProperties.brand.equalsIgnoreCase("pug")) {
			IBKoreaBankTransfer bank_w = myfactor.newInstance(IBKoreaBankTransfer.class);
			testOtherBankNewAccountRWitdraw(bank_w, DEPOSITMETHOD.GHANAINSTANT);
		}else {
			IBMalaysiaBankTransfer bank_w = myfactor.newInstance(IBMalaysiaBankTransfer.class);
			testOtherBankNewAccountRWitdraw(bank_w, DEPOSITMETHOD.GHANAINSTANT);
		}
	}
	
	@Test(priority = 0)
	public void testRwandaBankWithdraw() {
		IBKoreaBankTransfer bank_w = myfactor.newInstance(IBKoreaBankTransfer.class);
		testOtherBankRWitdraw(bank_w, DEPOSITMETHOD.RWANDAINSTANT);
	}
	
	@Test(priority = 0)
	public void testRwandaBankWithdrawNewAccount() {
		IBKoreaBankTransfer bank_w = myfactor.newInstance(IBKoreaBankTransfer.class);
		testOtherBankNewAccountRWitdraw(bank_w, DEPOSITMETHOD.RWANDAINSTANT);
	}

	@Test(priority = 0)
	public void testTanzaniaBankWithdraw() {
		IBMalaysiaBankTransfer bank_w = myfactor.newInstance(IBMalaysiaBankTransfer.class);
		testOtherBankRWitdraw(bank_w, DEPOSITMETHOD.TANZANIAINSTANT);
	}
	
	@Test(priority = 0)
	public void testTanzaniaBankWithdrawNewAccount() {
		IBMalaysiaBankTransfer bank_w = myfactor.newInstance(IBMalaysiaBankTransfer.class);
		testOtherBankNewAccountRWitdraw(bank_w, DEPOSITMETHOD.TANZANIAINSTANT);
	}

	//PM confirmed that Uganda input fields between PUG and VFX will be different
	@Test(priority = 0)
	public void testUgandaBankWithdraw() {
		if(GlobalProperties.brand.equalsIgnoreCase("pug")) {
			IBKoreaBankTransfer bank_w = myfactor.newInstance(IBKoreaBankTransfer.class);
			testOtherBankRWitdraw(bank_w, DEPOSITMETHOD.UGANDAINSTANT);
		}else {
			IBMalaysiaBankTransfer bank_w = myfactor.newInstance(IBMalaysiaBankTransfer.class);
			testOtherBankRWitdraw(bank_w, DEPOSITMETHOD.UGANDAINSTANT);
		}
	}
	
	@Test(priority = 0)
	public void testUgandaBankWithdrawNewAccount() {
		if(GlobalProperties.brand.equalsIgnoreCase("pug")) {
			IBKoreaBankTransfer bank_w = myfactor.newInstance(IBKoreaBankTransfer.class);
			testOtherBankNewAccountRWitdraw(bank_w, DEPOSITMETHOD.UGANDAINSTANT);
		}else {
			IBMalaysiaBankTransfer bank_w = myfactor.newInstance(IBMalaysiaBankTransfer.class);
			testOtherBankNewAccountRWitdraw(bank_w, DEPOSITMETHOD.UGANDAINSTANT);
		}
	}

	@Test(priority = 0)
	public void testCameroonBankWithdraw() {
		IBMalaysiaBankTransfer bank_w = myfactor.newInstance(IBMalaysiaBankTransfer.class);
		testOtherBankRWitdraw(bank_w, DEPOSITMETHOD.CAMEROONINSTANT);
	}
	
	@Test(priority = 0)
	public void testCameroonBankWithdrawNewAccount() {
		IBMalaysiaBankTransfer bank_w = myfactor.newInstance(IBMalaysiaBankTransfer.class);
		testOtherBankNewAccountRWitdraw(bank_w, DEPOSITMETHOD.CAMEROONINSTANT);
	}

	//PM confirmed that Kenya input fields between PUG and VFX will be different
	@Test(priority = 0)
	public void testKenyaBankWithdraw() {
		if(GlobalProperties.brand.equalsIgnoreCase("pug")) {
			IBMalaysiaBankTransfer bank_w = myfactor.newInstance(IBMalaysiaBankTransfer.class);
			testOtherBankRWitdraw(bank_w, DEPOSITMETHOD.KENYAINSTANT);
		}else {
			IBKoreaBankTransfer bank_w = myfactor.newInstance(IBKoreaBankTransfer.class);
			testOtherBankRWitdraw(bank_w, DEPOSITMETHOD.KENYAINSTANT);
		}
	}
	
	@Test(priority = 0)
	public void testKenyaBankWithdrawNewAccount() {
		if(GlobalProperties.brand.equalsIgnoreCase("pug")) {
			IBMalaysiaBankTransfer bank_w = myfactor.newInstance(IBMalaysiaBankTransfer.class);
			testOtherBankNewAccountRWitdraw(bank_w, DEPOSITMETHOD.KENYAINSTANT);
		}else {
			IBKoreaBankTransfer bank_w = myfactor.newInstance(IBKoreaBankTransfer.class);
			testOtherBankNewAccountRWitdraw(bank_w, DEPOSITMETHOD.KENYAINSTANT);
		}
	}
	
	@Test(priority = 0)
	public void testKoreaBankWithdraw() {
		IBKoreaBankTransfer bank_w = myfactor.newInstance(IBKoreaBankTransfer.class);
		testOtherBankRWitdraw(bank_w, DEPOSITMETHOD.KOREAINSTANT);
	}
	
	@Test(priority = 0)
	public void testKoreaBankWithdrawNewAccount() {
		IBKoreaBankTransfer bank_w = myfactor.newInstance(IBKoreaBankTransfer.class);
		testOtherBankNewAccountRWitdraw(bank_w, DEPOSITMETHOD.KOREAINSTANT);
	}
	
	@Test(priority = 0)
	public void testTaiwanBankWithdraw() {
		IBKoreaBankTransfer bank_w = myfactor.newInstance(IBKoreaBankTransfer.class);
		testOtherBankRWitdraw(bank_w, DEPOSITMETHOD.TAIWANINSTANT);
	}
	
	@Test(priority = 0)
	public void testTaiwanBankWithdrawNewAccount() {
		IBKoreaBankTransfer bank_w = myfactor.newInstance(IBKoreaBankTransfer.class);
		testOtherBankNewAccountRWitdraw(bank_w, DEPOSITMETHOD.TAIWANINSTANT);
	}
	
	@Test(priority = 0)
	public void testIndonesiaBankWithdraw() {
		IBMalaysiaBankTransfer bank_w = myfactor.newInstance(IBMalaysiaBankTransfer.class);
		testOtherBankRWitdraw(bank_w, DEPOSITMETHOD.INDONESIAINSTANT);
	}
	
	@Test(priority = 0)
	public void testIndonesiaBankWithdrawNewAccount() {
		IBMalaysiaBankTransfer bank_w = myfactor.newInstance(IBMalaysiaBankTransfer.class);
		testOtherBankNewAccountRWitdraw(bank_w, DEPOSITMETHOD.INDONESIAINSTANT);
	}
	
	@Test(priority = 0)
	public void testSouthAfricaBankWithdraw() {
		IBMalaysiaBankTransfer bank_w = myfactor.newInstance(IBMalaysiaBankTransfer.class);
		testOtherBankRWitdraw(bank_w, DEPOSITMETHOD.SOUTHAFRICAINSTANT);
	}
	
	@Test(priority = 0)
	public void testSouthAfricaBankWithdrawNewAccount() {
		IBMalaysiaBankTransfer bank_w = myfactor.newInstance(IBMalaysiaBankTransfer.class);
		testOtherBankNewAccountRWitdraw(bank_w, DEPOSITMETHOD.SOUTHAFRICAINSTANT);
	}
	
	@Test(priority = 0)
	public void testThaiBankWithdraw() {
		IBMalaysiaBankTransfer bank_w = myfactor.newInstance(IBMalaysiaBankTransfer.class);
		testOtherBankRWitdraw(bank_w, DEPOSITMETHOD.THAIINSTANT);
	}
	
	@Test(priority = 0)
	public void testThaiBankWithdrawNewAccount() {
		IBMalaysiaBankTransfer bank_w = myfactor.newInstance(IBMalaysiaBankTransfer.class);
		testOtherBankNewAccountRWitdraw(bank_w, DEPOSITMETHOD.THAIINSTANT);
	}
	
	@Test(priority = 0)
	public void testVietBankWithdraw() {
		IBMalaysiaBankTransfer bank_w = myfactor.newInstance(IBMalaysiaBankTransfer.class);
		testOtherBankRWitdraw(bank_w, DEPOSITMETHOD.VIETNAMINSTANT);
	}
	
	@Test(priority = 0)
	public void testVietBankWithdrawNewAccount() {
		IBMalaysiaBankTransfer bank_w = myfactor.newInstance(IBMalaysiaBankTransfer.class);
		testOtherBankNewAccountRWitdraw(bank_w, DEPOSITMETHOD.VIETNAMINSTANT);
	}	
	
	@Test(priority = 0)
	public void testIndiaBankWithdraw() {
		IBIndiaBankTransfer bank_w = myfactor.newInstance(IBIndiaBankTransfer.class);
		testOtherBankRWitdraw(bank_w, DEPOSITMETHOD.INDIAIAINSTANT);
	}
	
	@Test(priority = 0)
	public void testIndiaBankWithdrawNewAccount() {
		IBIndiaBankTransfer bank_w = myfactor.newInstance(IBIndiaBankTransfer.class);
		testOtherBankNewAccountRWitdraw(bank_w, DEPOSITMETHOD.INDIAIAINSTANT);
	}

	@Test(priority = 0)
	public void testPhilippinesBankWithdraw() {
		IBMalaysiaBankTransfer bank_w = myfactor.newInstance(IBMalaysiaBankTransfer.class);
		testOtherBankRWitdraw(bank_w, DEPOSITMETHOD.PHILIPPINESINSTANT);
	}
	
	@Test(priority = 0)
	public void testPhilippinesBankWithdrawNewAccount() {
		IBMalaysiaBankTransfer bank_w = myfactor.newInstance(IBMalaysiaBankTransfer.class);
		testOtherBankNewAccountRWitdraw(bank_w, DEPOSITMETHOD.PHILIPPINESINSTANT);
	}

	@Test(priority = 0)
	public void testMexicoBankWithdraw() {
		IBMexicoBankTransfer bank_w = myfactor.newInstance(IBMexicoBankTransfer.class);
		testOtherBankRWitdraw(bank_w, DEPOSITMETHOD.MEXICOINSTANT);
	}
	
	@Test(priority = 0)
	public void testMexicoBankWithdrawNewAccount() {
		IBMexicoBankTransfer bank_w = myfactor.newInstance(IBMexicoBankTransfer.class);
		testOtherBankNewAccountRWitdraw(bank_w, DEPOSITMETHOD.MEXICOINSTANT);
	}
	
	@Test(priority = 0)
	public void testBrazilBankWithdraw() {
		IBBrazilBankTransfer bank_w = myfactor.newInstance(IBBrazilBankTransfer.class);
		testOtherBankRWitdraw(bank_w, DEPOSITMETHOD.BRAZILINSTANT);
	}
	
	@Test(priority = 0)
	public void testBrazilBankWithdrawNewAccount() {
		IBBrazilBankTransfer bank_w = myfactor.newInstance(IBBrazilBankTransfer.class);
		testOtherBankNewAccountRWitdraw(bank_w, DEPOSITMETHOD.BRAZILINSTANT);
	}
	
	@Test(priority = 0)
	public void testBrazilPIXWithdraw() {
		IBBrazilPIXTransfer bank_w = myfactor.newInstance(IBBrazilPIXTransfer.class);
		testOtherBankRWitdraw(bank_w, DEPOSITMETHOD.BRAZILPIX);
	}
	
	@Test(priority = 0)
	public void testBrazilPIXWithdrawNewAccount() {
		IBBrazilPIXTransfer bank_w = myfactor.newInstance(IBBrazilPIXTransfer.class);
		testOtherBankNewAccountRWitdraw(bank_w, DEPOSITMETHOD.BRAZILPIX);
	}
	
	@Test(priority = 0)
	public void testHongKongBankWithdraw() {
		IBHongKongBankTransfer bank_w = myfactor.newInstance(IBHongKongBankTransfer.class);
		testOtherBankRWitdraw(bank_w, DEPOSITMETHOD.HONGKONGINSTANT);
	}
	
	@Test(priority = 0)
	public void testHongKongBankWithdrawNewAccount() {
		IBHongKongBankTransfer bank_w = myfactor.newInstance(IBHongKongBankTransfer.class);
		testOtherBankNewAccountRWitdraw(bank_w, DEPOSITMETHOD.HONGKONGINSTANT);
	}
	
	@Test(priority = 0)
	public void testJapanBankWithdraw() {
		IBJapanBankTransfer bank_w = myfactor.newInstance(IBJapanBankTransfer.class);
		testOtherBankRWitdraw(bank_w, DEPOSITMETHOD.JAPANIAINSTANT);
	}
	
	@Test(priority = 0)
	public void testJapanBankWithdrawNewAccount() {
		IBJapanBankTransfer bank_w = myfactor.newInstance(IBJapanBankTransfer.class);
		testOtherBankNewAccountRWitdraw(bank_w, DEPOSITMETHOD.JAPANIAINSTANT);
	}
	
	@Test(priority = 0)
	public void testNigeriaBankWithdraw() {
		IBKoreaBankTransfer bank_w = myfactor.newInstance(IBKoreaBankTransfer.class);
		testOtherBankRWitdraw(bank_w, DEPOSITMETHOD.NIGERIAINSTANT);
	}
	
	@Test(priority = 0)
	public void testNigeriaBankWithdrawNewAccount() {
		IBKoreaBankTransfer bank_w = myfactor.newInstance(IBKoreaBankTransfer.class);
		testOtherBankNewAccountRWitdraw(bank_w, DEPOSITMETHOD.NIGERIAINSTANT);
	}
	
	@Test(priority = 0)
	public void testChileBankWithdraw() {
		IBMexicoBankTransfer bank_w = myfactor.newInstance(IBMexicoBankTransfer.class);
		testOtherBankRWitdraw(bank_w, DEPOSITMETHOD.CHILEINSTANT);
	}
	
	@Test(priority = 0)
	public void testChileBankWithdrawNewAccount() {
		IBMexicoBankTransfer bank_w = myfactor.newInstance(IBMexicoBankTransfer.class);
		testOtherBankNewAccountRWitdraw(bank_w, DEPOSITMETHOD.CHILEINSTANT);
	}
	
	@Test(priority = 0)
	public void testColombiaBankWithdraw() {
		IBMexicoBankTransfer bank_w = myfactor.newInstance(IBMexicoBankTransfer.class);
		testOtherBankRWitdraw(bank_w, DEPOSITMETHOD.COLOMBIAINSTANT);
	}
	
	@Test(priority = 0)
	public void testColombiaBankWithdrawNewAccount() {
		IBMexicoBankTransfer bank_w = myfactor.newInstance(IBMexicoBankTransfer.class);
		testOtherBankNewAccountRWitdraw(bank_w, DEPOSITMETHOD.COLOMBIAINSTANT);
	}
	
	@Test(priority = 0)
	public void testPeruBankWithdraw() {
		IBPeruBankTransfer bank_w = myfactor.newInstance(IBPeruBankTransfer.class);
		testOtherBankRWitdraw(bank_w, DEPOSITMETHOD.PERUINSTANT);
	}
	
	@Test(priority = 0)
	public void testPeruBankWithdrawNewAccount() {
		IBPeruBankTransfer bank_w = myfactor.newInstance(IBPeruBankTransfer.class);
		testOtherBankNewAccountRWitdraw(bank_w, DEPOSITMETHOD.PERUINSTANT);
	}
	
	@Test(priority = 0)
	public void testLaosBankWithdraw() {
		IBKoreaBankTransfer bank_w = myfactor.newInstance(IBKoreaBankTransfer.class);
		testOtherBankRWitdraw(bank_w, DEPOSITMETHOD.LAOSINSTANT);
	}
	
	@Test(priority = 0)
	public void testLaosBankWithdrawNewAccount() {
		IBKoreaBankTransfer bank_w = myfactor.newInstance(IBKoreaBankTransfer.class);
		testOtherBankNewAccountRWitdraw(bank_w, DEPOSITMETHOD.LAOSINSTANT);
	}
	
	@Test(priority = 0)
	public void testMongoliaBankWithdraw() {
		IBKoreaBankTransfer bank_w = myfactor.newInstance(IBKoreaBankTransfer.class);
		testOtherBankRWitdraw(bank_w, DEPOSITMETHOD.MONGOLIAINSTANT);
	}
	
	@Test(priority = 0)
	public void testMongoliaBankWithdrawNewAccount() {
		IBKoreaBankTransfer bank_w = myfactor.newInstance(IBKoreaBankTransfer.class);
		testOtherBankNewAccountRWitdraw(bank_w, DEPOSITMETHOD.MONGOLIAINSTANT);
	}
	
	@Test(priority = 0)
	public void testZambialiaBankWithdraw() {
		IBKoreaBankTransfer bank_w = myfactor.newInstance(IBKoreaBankTransfer.class);
		testOtherBankRWitdraw(bank_w, DEPOSITMETHOD.ZAMBIAINSTANT);
	}
	
	@Test(priority = 0)
	public void testZambiaBankWithdrawNewAccount() {
		IBKoreaBankTransfer bank_w = myfactor.newInstance(IBKoreaBankTransfer.class);
		testOtherBankNewAccountRWitdraw(bank_w, DEPOSITMETHOD.ZAMBIAINSTANT);
	}
	
	@Test(priority = 0)
	public void testLocalDepositorWithdraw() {
		IBLocalDepositorWithdraw bank_w = myfactor.newInstance(IBLocalDepositorWithdraw.class);
		testOtherBankRWitdraw(bank_w, DEPOSITMETHOD.LOCALDEPOSITOR);
	}
	
	@Test(priority = 0)
	public void testLocalDepositorWithdrawNewAccount() {
		IBLocalDepositorWithdraw bank_w = myfactor.newInstance(IBLocalDepositorWithdraw.class);
		testOtherBankNewAccountRWitdraw(bank_w, DEPOSITMETHOD.LOCALDEPOSITOR);
	}
	
	@Test(priority = 0)
	public void testUAEBankWithdraw() {
		IBUAEBankTransfer bank_w = myfactor.newInstance(IBUAEBankTransfer.class);
		testOtherBankRWitdraw(bank_w, DEPOSITMETHOD.UAEINSTANT);
	}
	
	@Test(priority = 0)
	public void testUAEBankWithdrawNewAccount() {
		IBUAEBankTransfer bank_w = myfactor.newInstance(IBUAEBankTransfer.class);
		testOtherBankNewAccountRWitdraw(bank_w, DEPOSITMETHOD.UAEINSTANT);
	}

	@Test(priority = 0)
	public void testAzupayWithdraw() {
		IBAzupayWithdraw bank_w = myfactor.newInstance(IBAzupayWithdraw.class);
		testOtherBankRWitdraw(bank_w, DEPOSITMETHOD.AZUPAY);
	}

	@Test(priority = 0, description = testCaseDescUtils.IBWITHDRAW_INTER_BANK_TRANS)
	public void testInternationalBankNewAccountWitdraw() {
		Double amount = this.filleBankTransfer(DEPOSITMETHOD.I12BANKTRANSFER);
		IBInternationalBankTransfer bank_w = myfactor.newInstance(IBInternationalBankTransfer.class);
		IBEmailWithdraw withdraw = myfactor.newInstance(IBEmailWithdraw.class);
		//testdata
		String bankName = "Test Bank Name";
		String address = "10 perfect street";
		String BBname = "Test Beneficiary name";
		String bank_acc_num = "3465673";
		String holder_address = "99 test Address";
		String swift = "30033";
		String sortCode = "8888";
		String notes = "auto test";
		
		selectIBTWithdrawMethod(withdraw, DEPOSITMETHOD.I12BANKTRANSFER);
		Assert.assertTrue(bank_w.setWithdrawInfo(bankName, address, BBname, bank_acc_num, holder_address, swift,
				sortCode, notes), "Invalid Information");
		
		bank_w.submit();
		
		assertTrue(this.checkHistory(amount, DEPOSITMETHOD.I12BANKTRANSFER, rebateAcc),"Have not found the withdraw history");
		GlobalMethods.printDebugInfo("*  Test International Bank new account withdraw succeed.");
		
	}

	@Test(priority = 0, description = testCaseDescUtils.IBWITHDRAW_INTER_BANK_TRANS)
	public void testInternationalBankNewAccountWitdrawNew() throws InterruptedException {
		Double amount = this.filleBankTransferNew(DEPOSITMETHOD.I12BANKTRANSFER_New);
		IBInternationalBankTransfer bank_w = myfactor.newInstance(IBInternationalBankTransfer.class);
		IBEmailWithdraw withdraw = myfactor.newInstance(IBEmailWithdraw.class);

		//testdata
		String bankName = "Test Bank Name";
		String address = "10 perfect street";
		String BBname = "Test Beneficiary name";
		String bank_acc_num = "3465673";
		String holder_address = "99 test Address";
		String swift = "30033";
		String sortCode = "8888";
		String notes = "auto test";

		withdraw.clickContinue();
		selectIBTWithdrawMethodNew(withdraw, DEPOSITMETHOD.I12BANKTRANSFER_New);
		Assert.assertTrue(bank_w.setWithdrawInfo(bankName, address, BBname, bank_acc_num, holder_address, swift,
				sortCode, notes), "Invalid Information");

		bank_w.submit();

		assertTrue(this.checkHistory(amount, DEPOSITMETHOD.I12BANKTRANSFER_New, rebateAcc),"Have not found the withdraw history");
		GlobalMethods.printDebugInfo("*  Test International Bank new account withdraw succeed.");
	}

	@Test(priority = 0, description = testCaseDescUtils.IBWITHDRAW_INTER_BANK_TRANS)
	public void testInternationalBankSavedAccountWitdraw() {
		Double amount = this.filleBankTransfer(DEPOSITMETHOD.I12BANKTRANSFER);
		IBInternationalBankTransfer bank_w = myfactor.newInstance(IBInternationalBankTransfer.class);
		IBEmailWithdraw withdraw = myfactor.newInstance(IBEmailWithdraw.class);
		
		
		selectIBTWithdrawMethod(withdraw, DEPOSITMETHOD.I12BANKTRANSFER);
		//testdata
		String notes = "auto test,IB international bank saved account";
		List<String> sel_bank = null;
		
		sel_bank = bank_w.getSavedBankAccount();
		Assert.assertNotNull(sel_bank,"The user does not have any saved account!");
		
		Boolean hasAccount = false;
		for (String accNum : sel_bank) {
			if (bank_w.selectSavedBankAccount(accNum)) {
				hasAccount = true;
				break;
			}
		}
		Assert.assertTrue(hasAccount,"The user does not have any saved international bank accout!");
		
		bank_w.setNotes(notes);
		bank_w.submit();
		
		assertTrue(this.checkHistory(amount, DEPOSITMETHOD.I12BANKTRANSFER, rebateAcc),"Have not found the withdraw history");
		GlobalMethods.printDebugInfo("*  Test International Bank saved account withdraw succeed.");
	}
	
	@Test(priority = 0)
	public void testAustraliaBankNewAccountWitdraw() {
		Double amount = this.filleBankTransfer(DEPOSITMETHOD.AUBANKTRANSFER);
		IBAustraliaBankWithdraw bank_w = myfactor.newInstance(IBAustraliaBankWithdraw.class);
		IBEmailWithdraw withdraw = myfactor.newInstance(IBEmailWithdraw.class);
		//testdata
		String bankName = "Test Bank Name";
		String BSB = "34543";
		String BBname = "Test Beneficiary name";
		String bank_acc_num = "3465673";
		String swift = "30033";
		String notes = "Auto test Australia Bank Withdraw";
		
		selectIBTWithdrawMethod(withdraw, DEPOSITMETHOD.AUBANKTRANSFER);
		Assert.assertTrue(bank_w.setWithdrawInfo(bankName, BSB, BBname, swift, bank_acc_num, notes),
				"Invalid Information");
		
		withdraw.submit();
		
		assertTrue(this.checkHistory(amount, DEPOSITMETHOD.AUBANKTRANSFER, rebateAcc),"Have not found the withdraw history");
		GlobalMethods.printDebugInfo("*  Test Australia Bank new account withdraw succeed.");
		
	}
	
	@Test(priority = 0)
	public void testAustraliaBankSavedAccountWitdraw() {
		Double amount = this.filleBankTransfer(DEPOSITMETHOD.AUBANKTRANSFER);
		IBAustraliaBankWithdraw bank_w = myfactor.newInstance(IBAustraliaBankWithdraw.class);
		IBEmailWithdraw withdraw = myfactor.newInstance(IBEmailWithdraw.class);
		
		
		selectIBTWithdrawMethod(withdraw, DEPOSITMETHOD.AUBANKTRANSFER);
		//testdata
		String notes = "auto test,IB Australia bank saved account";
		List<String> sel_bank = null;
		
		sel_bank = bank_w.getSavedBankAccount();
		Assert.assertNotNull(sel_bank,"The user does not have any saved account!");
		
		Boolean hasAccount = false;
		for (String accNum : sel_bank) {
			if (bank_w.selectSavedBankAccount(accNum)) {
				hasAccount = true;
				break;
			}
		}
		Assert.assertTrue(hasAccount,"The user does not have any saved Australia bank accout!");
		
		withdraw.setNote(notes);
		withdraw.submit();
		
		assertTrue(this.checkHistory(amount, DEPOSITMETHOD.AUBANKTRANSFER, rebateAcc),"Have not found the withdraw history");
		GlobalMethods.printDebugInfo("*  Test Australia Bank saved account withdraw succeed.");
	}
	
	public void setCode(EmailDB instance)
	{
		try 
		{
			WebElement codeBtn = driver.findElement(By.xpath("//button[@data-testid='code-button']"));
			codeBtn.click();
			try 
			{
				Thread.sleep(1000);
			} 
			catch (InterruptedException e) 
			{
				throw new RuntimeException(e);
			}
			JSONObject obj = instance.getCodeRecord(dbenv, dbBrand, dbRegulator,TraderName);
			System.out.println(obj.getJSONObject("vars").getString("CODE")+ ", \n"+ obj.toJSONString());
			String code = obj.getJSONObject("vars").getString("CODE");
			
			WebElement codeInput = waitUtilVisibility(By.xpath("//input[@data-testid='code']"));
			codeInput.sendKeys(code);
		}
		catch(Exception e)
		{
			GlobalMethods.printDebugInfo("No require to enter verification code");
		}
	}
	
	@Test(priority = 0)
	public void testInteracRWithdraw() {
		IBEmailWithdraw withdraw = myfactor.newInstance(IBEmailWithdraw.class);
		String note = "test note " + DEPOSITMETHOD.INTERAC.getIBWithdrawName();
		
		if(emailDB==null) {
			emailDB = new EmailDB(dbenv, dbBrand, dbRegulator);
		}
		
		Double amount = this.filleBankTransfer(DEPOSITMETHOD.INTERAC);
		this.selectLocalTransferWithdrawMethod(withdraw, DEPOSITMETHOD.INTERAC);
		withdraw.setNote(note);
		
		
		if(GlobalProperties.brand.equalsIgnoreCase("mo"))
		{
			setCode(emailDB);
		}

		withdraw.submit();
		
		assertTrue(this.checkHistory(amount, DEPOSITMETHOD.INTERAC, rebateAcc),"Have not found the withdraw history");
		GlobalMethods.printDebugInfo("*  Test Interact withdraw succeed.");
	}

	@Test(priority = 0, description = testCaseDescUtils.IBWITHDRAW_EWALLET_STICPAY)
	public void testSticPayWithdrawNew() {
		IBCPSEwalletWithdraw ewallet_withdraw = myfactor.newInstance(IBCPSEwalletWithdraw.class);
		Double amount=0.0;
		amount = this.fillWithdrawbasicInfoNew(DEPOSITMETHOD.STICPAY);

//		if(!Brand.equalsIgnoreCase("pug")){
//			 amount = this.fillWithdrawbasicInfoNew(DEPOSITMETHOD.STICPAY);
//
//		}else {
//			amount = this.fillWithdrawbasicInfoNew(DEPOSITMETHOD.NETELLER);
//		}
		ewallet_withdraw.setWithdrawAccount("ibsticpay@test.com");
		ewallet_withdraw.submit();
		//check
		assertTrue(this.checkHistory(amount, DEPOSITMETHOD.STICPAY, rebateAcc),"Have not found the withdraw history");
//		if(!Brand.equalsIgnoreCase("pug")){
//			assertTrue(this.checkHistory(amount, DEPOSITMETHOD.STICPAY, rebateAcc),"Have not found the withdraw history");
//
//		}else{
//			assertTrue(this.checkHistory(amount, DEPOSITMETHOD.NETELLER, rebateAcc),"Have not found the withdraw history");
//		}
		GlobalMethods.printDebugInfo("Test SticPay withdraw succeed.");
	}

	@Test(priority = 0)
	public void testBitwalletWithdrawNew() {
		IBCPSEwalletWithdraw ewallet_withdraw = myfactor.newInstance(IBCPSEwalletWithdraw.class);
		Double amount = this.fillWithdrawbasicInfoNew(DEPOSITMETHOD.BITWALLET);
		ewallet_withdraw.setWithdrawAccount("ibbitwallet@test.com");
		ewallet_withdraw.submit();
		assertTrue(this.checkHistory(amount, DEPOSITMETHOD.BITWALLET, rebateAcc),"Have not found the withdraw history");
		GlobalMethods.printDebugInfo("*  Test Bitwallet withdraw succeed.");
	}

	@Test(priority = 0)
	public void testSkrillWithdrawNew() {
		IBCPSEwalletWithdraw ewallet_withdraw = myfactor.newInstance(IBCPSEwalletWithdraw.class);
		Double amount = this.fillWithdrawbasicInfoNew(DEPOSITMETHOD.SKRILL);
		ewallet_withdraw.setWithdrawAccount("cpsEwallet@test.com");
		ewallet_withdraw.submit();
		assertTrue(this.checkHistory(amount, DEPOSITMETHOD.SKRILL, rebateAcc),"Have not found the withdraw history");
		GlobalMethods.printDebugInfo("Test Skrill withdraw succeed.");
	}

	@Test(priority = 0, description = testCaseDescUtils.IBWITHDRAW_EWALLET_Neteller)
	public void testNetellerWithdrawNew() {
		IBCPSEwalletWithdraw ewallet_withdraw = myfactor.newInstance(IBCPSEwalletWithdraw.class);
		Double amount = this.fillWithdrawbasicInfoNew(DEPOSITMETHOD.NETELLER);
		ewallet_withdraw.setWithdrawAccount("cpsEwallet@test.com");
		ewallet_withdraw.submit();
		assertTrue(this.checkHistory(amount, DEPOSITMETHOD.NETELLER, rebateAcc),"Have not found the withdraw history");
		GlobalMethods.printDebugInfo("Test Neteller withdraw succeed.");
	}

	private void testOtherBankRWitdrawNew(IBLocalBankTransfer bank_w, DEPOSITMETHOD wType) {
		IBEmailWithdraw withdraw = myfactor.newInstance(IBEmailWithdraw.class);
		Double amount = this.filleBankTransferNew(wType);
		withdraw.clickContinue();
		this.selectLocalTransferWithdrawMethodNew(withdraw, wType);

		//testdata
		String branch = "autotest Branch";
		String accName = "autotest accountName";
		String accNumber = "3463821905673";
		String city = "autotest city";
		String province = "autotest province";
		String notes = "autotest notes";
		String ifsc = "test ifsc";
		String accdigit = "123123";
		String docid = "123123";
		String swift_code = "test swift code";
		String docType = "test doc type";
		String accType = "test acct type";
		String bankName = "test bank name";
		List<String> sel_bank = null;

		sel_bank = bank_w.getSavedBankAccount();

		// if does not have saved bank acc then add new one, else use saved bank info
		if (sel_bank == null) {
			System.out.println("********No saved bank account info found********");
			bank_w.addNewBankAccount(branch, accName, accNumber, city, province, ifsc, notes, accdigit, docid, swift_code, docType, accType, bankName);
		} else {
			System.out.println("*********Found saved bank account info*********");
			bank_w.selectSavedBankAccount(sel_bank.get(0));
		}

		assertTrue(withdraw.submit(), "Submit failed or could not find submit button. Please check!! Thanks.");
		assertTrue(this.checkHistory(amount, wType, rebateAcc),"Have not found the withdraw history");
		GlobalMethods.printDebugInfo("*****Test " + wType.getIBWithdrawTypeDataTestId() + " new account withdraw succeed.");
	}

	private void testOtherBankNewAccountRWitdrawNew(IBLocalBankTransfer bank_w, DEPOSITMETHOD wType) {
		IBEmailWithdraw withdraw = myfactor.newInstance(IBEmailWithdraw.class);
		Double amount = this.filleBankTransferNew(wType);
		withdraw.clickContinue();
		this.selectLocalTransferWithdrawMethodNew(withdraw, wType);

		//testdata
		String branch = "autotest Branch";
		String accName = "autotest accountName";
		String accNumber = "3463821905673";
		String city = "autotest city";
		String province = "autotest province";
		String notes = "autotest notes";
		String ifsc = "test ifsc";
		String accdigit = "123123";
		String docid = "123123";
		String swift_code = "test swift code";
		String docType = "test doc type";
		String accType = "test acct type";
		String bankName = "test bank name";
		List<String> sel_bank = null;

		sel_bank = bank_w.getSavedBankAccount();

		// regardless got saved bank account or not, also proceed to add new
		if (sel_bank == null) {
			System.out.println("********No saved bank account info found********");
			bank_w.addNewBankAccount(branch, accName, accNumber, city, province, ifsc, notes, accdigit, docid, swift_code, docType, accType, bankName);
		} else {
			System.out.println("*********Found saved bank account info BUT proceed with add new bank account =)*********");
			bank_w.addNewBankAccount(branch, accName, accNumber, city, province, ifsc, notes, accdigit, docid, swift_code, docType, accType, bankName);
		}

		assertTrue(withdraw.submit(), "Submit failed or could not find submit button. Please check!! Thanks.");
		assertTrue(this.checkHistory(amount, wType, rebateAcc),"Have not found the withdraw history");
		GlobalMethods.printDebugInfo("*****Test " + wType.getIBWithdrawTypeDataTestId() + " new account withdraw succeed.");
	}

	@Test(priority = 0)
	public void testKoreaBankWithdrawNew() {
		IBKoreaBankTransfer bank_w = myfactor.newInstance(IBKoreaBankTransfer.class);
		testOtherBankRWitdrawNew(bank_w, DEPOSITMETHOD.KOREAINSTANT);
	}

	@Test(priority = 0)
	public void testKoreaBankWithdrawNewAccountNew() {
		IBKoreaBankTransfer bank_w = myfactor.newInstance(IBKoreaBankTransfer.class);
		testOtherBankNewAccountRWitdrawNew(bank_w, DEPOSITMETHOD.KOREAINSTANT);
	}

	@Test(priority = 0)
	public void testMalaysiaBankWithdrawNew() {
		IBMalaysiaBankTransfer bank_w = myfactor.newInstance(IBMalaysiaBankTransfer.class);
		testOtherBankRWitdrawNew(bank_w, DEPOSITMETHOD.MALAYINSTANT);
	}

	@Test(priority = 0)
	public void testMalaysiaBankWithdrawNewAccountNew() {
		IBMalaysiaBankTransfer bank_w = myfactor.newInstance(IBMalaysiaBankTransfer.class);
		testOtherBankNewAccountRWitdrawNew(bank_w, DEPOSITMETHOD.MALAYINSTANT);
	}

	@Test(priority = 0)
	public void testSouthAfricaBankWithdrawNew() {
		IBMalaysiaBankTransfer bank_w = myfactor.newInstance(IBMalaysiaBankTransfer.class);
		testOtherBankRWitdrawNew(bank_w, DEPOSITMETHOD.SOUTHAFRICAINSTANT);
	}

	@Test(priority = 0)
	public void testSouthAfricaBankWithdrawNewAccountNew() {
		IBMalaysiaBankTransfer bank_w = myfactor.newInstance(IBMalaysiaBankTransfer.class);
		testOtherBankNewAccountRWitdrawNew(bank_w, DEPOSITMETHOD.SOUTHAFRICAINSTANT);
	}

	@Test(priority = 0)
	public void testVietBankWithdrawNew() {
		IBVietnamBankTransfer bank_w = myfactor.newInstance(IBVietnamBankTransfer.class);
		testOtherBankRWitdrawNew(bank_w, DEPOSITMETHOD.VIETNAMINSTANT);
	}

	@Test(priority = 0)
	public void testVietBankWithdrawNewAccountNew() {
		IBVietnamBankTransfer bank_w = myfactor.newInstance(IBVietnamBankTransfer.class);
		testOtherBankNewAccountRWitdrawNew(bank_w, DEPOSITMETHOD.VIETNAMINSTANT);
	}

	@Test(priority = 0)
	public void testIndiaBankWithdrawNew() {
		IBIndiaBankTransfer bank_w = myfactor.newInstance(IBIndiaBankTransfer.class);
		testOtherBankRWitdrawNew(bank_w, DEPOSITMETHOD.INDIAIAINSTANT);
	}

	@Test(priority = 0)
	public void testIndiaBankWithdrawNewAccountNew() {
		IBIndiaBankTransfer bank_w = myfactor.newInstance(IBIndiaBankTransfer.class);
		testOtherBankNewAccountRWitdrawNew(bank_w, DEPOSITMETHOD.INDIAIAINSTANT);
	}

	@Test(priority = 0)
	public void testPhilippinesBankWithdrawNew() {
		IBMalaysiaBankTransfer bank_w = myfactor.newInstance(IBMalaysiaBankTransfer.class);
		testOtherBankRWitdrawNew(bank_w, DEPOSITMETHOD.PHILIPPINESINSTANT);
	}

	@Test(priority = 0)
	public void testPhilippinesBankWithdrawNewAccountNew() {
		IBMalaysiaBankTransfer bank_w = myfactor.newInstance(IBMalaysiaBankTransfer.class);
		testOtherBankNewAccountRWitdrawNew(bank_w, DEPOSITMETHOD.PHILIPPINESINSTANT);
	}

	@Test(priority = 0)
	public void testIndonesiaBankWithdrawNew() {
		IBMalaysiaBankTransfer bank_w = myfactor.newInstance(IBMalaysiaBankTransfer.class);
		testOtherBankRWitdrawNew(bank_w, DEPOSITMETHOD.INDONESIAINSTANT);
	}

	@Test(priority = 0)
	public void testIndonesiaBankWithdrawNewAccountNew() {
		IBMalaysiaBankTransfer bank_w = myfactor.newInstance(IBMalaysiaBankTransfer.class);
		testOtherBankNewAccountRWitdrawNew(bank_w, DEPOSITMETHOD.INDONESIAINSTANT);
	}

	@Test(priority = 0)
	public void testJapanBankWithdrawNew() {
		IBJapanBankTransfer bank_w = myfactor.newInstance(IBJapanBankTransfer.class);
		testOtherBankRWitdrawNew(bank_w, DEPOSITMETHOD.JAPANIAINSTANT);
	}

	@Test(priority = 0)
	public void testJapanBankWithdrawNewAccountNew() {
		IBJapanBankTransfer bank_w = myfactor.newInstance(IBJapanBankTransfer.class);
		testOtherBankNewAccountRWitdrawNew(bank_w, DEPOSITMETHOD.JAPANIAINSTANT);
	}

	@Test(priority = 0)
	public void testBrazilBankWithdrawNew() {
		IBBrazilBankTransfer bank_w = myfactor.newInstance(IBBrazilBankTransfer.class);
		testOtherBankRWitdrawNew(bank_w, DEPOSITMETHOD.BRAZILINSTANT);
	}

	@Test(priority = 0)
	public void testBrazilBankWithdrawNewAccountNew() {
		IBBrazilBankTransfer bank_w = myfactor.newInstance(IBBrazilBankTransfer.class);
		testOtherBankNewAccountRWitdrawNew(bank_w, DEPOSITMETHOD.BRAZILINSTANT);
	}

	@Test(priority = 0)
	public void testBrazilPIXWithdrawNew() {
		IBBrazilPIXTransfer bank_w = myfactor.newInstance(IBBrazilPIXTransfer.class);
		testOtherBankRWitdrawNew(bank_w, DEPOSITMETHOD.BRAZILPIX);
	}

	@Test(priority = 0)
	public void testBrazilPIXWithdrawNewAccountNew() {
		IBBrazilPIXTransfer bank_w = myfactor.newInstance(IBBrazilPIXTransfer.class);
		testOtherBankNewAccountRWitdrawNew(bank_w, DEPOSITMETHOD.BRAZILPIX);
	}

	@Test(priority = 0)
	public void testUgandaBankWithdrawNew() {
		IBMalaysiaBankTransfer bank_w = myfactor.newInstance(IBMalaysiaBankTransfer.class);
		testOtherBankRWitdrawNew(bank_w, DEPOSITMETHOD.UGANDAINSTANT);
	}

	@Test(priority = 0)
	public void testUgandaBankWithdrawNewAccountNew() {
		IBMalaysiaBankTransfer bank_w = myfactor.newInstance(IBMalaysiaBankTransfer.class);
		testOtherBankNewAccountRWitdrawNew(bank_w, DEPOSITMETHOD.UGANDAINSTANT);
	}

	@Test(priority = 0)
	public void testNigeriaBankWithdrawNew() {
		IBKoreaBankTransfer bank_w = myfactor.newInstance(IBKoreaBankTransfer.class);
		testOtherBankRWitdrawNew(bank_w, DEPOSITMETHOD.NIGERIAINSTANT);
	}

	@Test(priority = 0)
	public void testNigeriaBankWithdrawNewAccountNew() {
		IBKoreaBankTransfer bank_w = myfactor.newInstance(IBKoreaBankTransfer.class);
		testOtherBankNewAccountRWitdrawNew(bank_w, DEPOSITMETHOD.NIGERIAINSTANT);
	}

	@Test(priority = 0)
	public void testGhanaBankWithdrawNew() {
		IBMalaysiaBankTransfer bank_w = myfactor.newInstance(IBMalaysiaBankTransfer.class);
		testOtherBankRWitdrawNew(bank_w, DEPOSITMETHOD.GHANAINSTANT);
	}

	@Test(priority = 0)
	public void testGhanaBankWithdrawNewAccountNew() {
		IBMalaysiaBankTransfer bank_w = myfactor.newInstance(IBMalaysiaBankTransfer.class);
		testOtherBankNewAccountRWitdrawNew(bank_w, DEPOSITMETHOD.GHANAINSTANT);
	}

	@Test(priority = 0)
	public void testKenyaBankWithdrawNew() {
		IBKoreaBankTransfer bank_w = myfactor.newInstance(IBKoreaBankTransfer.class);
		testOtherBankRWitdrawNew(bank_w, DEPOSITMETHOD.KENYAINSTANT);
	}

	@Test(priority = 0)
	public void testKenyaBankWithdrawNewAccountNew() {
		IBKoreaBankTransfer bank_w = myfactor.newInstance(IBKoreaBankTransfer.class);
		testOtherBankNewAccountRWitdrawNew(bank_w, DEPOSITMETHOD.KENYAINSTANT);
	}

	@Test(priority = 0)
	public void testMongoliaBankWithdrawNew() {
		IBKoreaBankTransfer bank_w = myfactor.newInstance(IBKoreaBankTransfer.class);
		testOtherBankRWitdrawNew(bank_w, DEPOSITMETHOD.MONGOLIAINSTANT);
	}

	@Test(priority = 0)
	public void testMongoliaBankWithdrawNewAccountNew() {
		IBKoreaBankTransfer bank_w = myfactor.newInstance(IBKoreaBankTransfer.class);
		testOtherBankNewAccountRWitdrawNew(bank_w, DEPOSITMETHOD.MONGOLIAINSTANT);
	}

	@Test(priority = 0)
	public void testChileBankWithdrawNew() {
		IBMexicoBankTransfer bank_w = myfactor.newInstance(IBMexicoBankTransfer.class);
		testOtherBankRWitdrawNew(bank_w, DEPOSITMETHOD.CHILEINSTANT);
	}

	@Test(priority = 0)
	public void testChileBankWithdrawNewAccountNew() {
		IBMexicoBankTransfer bank_w = myfactor.newInstance(IBMexicoBankTransfer.class);
		testOtherBankNewAccountRWitdrawNew(bank_w, DEPOSITMETHOD.CHILEINSTANT);
	}

	@Test(priority = 0)
	public void testColombiaBankWithdrawNew() {
		IBMexicoBankTransfer bank_w = myfactor.newInstance(IBMexicoBankTransfer.class);
		testOtherBankRWitdrawNew(bank_w, DEPOSITMETHOD.COLOMBIAINSTANT);
	}

	@Test(priority = 0)
	public void testColombiaBankWithdrawNewAccountNew() {
		IBMexicoBankTransfer bank_w = myfactor.newInstance(IBMexicoBankTransfer.class);
		testOtherBankNewAccountRWitdrawNew(bank_w, DEPOSITMETHOD.COLOMBIAINSTANT);
	}

	@Test(priority = 0)
	public void testZambiaBankWithdrawNew() {
		IBKoreaBankTransfer bank_w = myfactor.newInstance(IBKoreaBankTransfer.class);
		testOtherBankRWitdrawNew(bank_w, DEPOSITMETHOD.ZAMBIAINSTANT);
	}

	@Test(priority = 0)
	public void testZambiaBankWithdrawNewAccountNew() {
		IBKoreaBankTransfer bank_w = myfactor.newInstance(IBKoreaBankTransfer.class);
		testOtherBankNewAccountRWitdrawNew(bank_w, DEPOSITMETHOD.ZAMBIAINSTANT);
	}

	@Test(priority = 0)
	public void testLaosBankWithdrawNew() {
		IBKoreaBankTransfer bank_w = myfactor.newInstance(IBKoreaBankTransfer.class);
		testOtherBankRWitdrawNew(bank_w, DEPOSITMETHOD.LAOSINSTANT);
	}

	@Test(priority = 0)
	public void testLaosBankWithdrawNewAccountNew() {
		IBKoreaBankTransfer bank_w = myfactor.newInstance(IBKoreaBankTransfer.class);
		testOtherBankNewAccountRWitdrawNew(bank_w, DEPOSITMETHOD.LAOSINSTANT);
	}

	@Test(priority = 0)
	public void testLocalDepositorWithdrawNew() {
		IBLocalDepositorWithdraw bank_w = myfactor.newInstance(IBLocalDepositorWithdraw.class);
		testOtherBankRWitdrawNew(bank_w, DEPOSITMETHOD.LOCALDEPOSITOR);
	}

	@Test(priority = 0, description = testCaseDescUtils.IBWITHDRAW_CRYPTO_ETH)
	public void testCryptoCRYPTOETHNew() {
		testCommonIBCryptoWithdrawNew(DEPOSITMETHOD.ETH_New);
	}

	@Test(priority = 0, description = testCaseDescUtils.IBWITHDRAW_CRYPTO_USDT_ERC20)
	public void testCryptoCRYPTOERCNew() {
		testCommonIBCryptoWithdrawNew(DEPOSITMETHOD.CRYPTOERCNew);
	}

	@Test(priority = 0, description = testCaseDescUtils.IBWITHDRAW_CRYPTO_ETH)
	public void testCryptoBTCNew() {
		testCommonIBCryptoWithdrawNew(DEPOSITMETHOD.CRYPTOBTC);
	}

	@Test(priority = 0, description = testCaseDescUtils.IBWITHDRAW_CRYPTO_USDT_ERC20)
	public void testCryptoTRCNew() {
		testCommonIBCryptoWithdrawNew(DEPOSITMETHOD.CRYPTOTRCNew);
	}

	@Test(priority = 0, description = testCaseDescUtils.IBWITHDRAW_CRYPTO_USDT_ERC20)
	public void testCryptoBEP20New() {
		testCommonIBCryptoWithdrawNew(DEPOSITMETHOD.CRYPTOBEPNew);
	}

	public void testCommonIBCryptoWithdrawNew(DEPOSITMETHOD method) {
		Double amount = this.filleBankTransferNew(method);
		System.out.println("filleBankTransferNew: " + System.currentTimeMillis());
		String walletaddress = null;

		IBCryptoBitcoinWithdraw crypto_withdraw = myfactor.newInstance(IBCryptoBitcoinWithdraw.class);
		crypto_withdraw.clickContinue();
		this.selectCryptoWithdrawMethodNew(crypto_withdraw, method);
		if (method == DEPOSITMETHOD.CRYPTOBTC || method == DEPOSITMETHOD.CRYPTOBIT)
		{
			walletaddress  = "3BWjWCkDBDuzB3bW3dTSEwCWht1EocvZct";
			crypto_withdraw.setCryptoWithdrawalInfoNew(walletaddress);
		}
		else if (method == DEPOSITMETHOD.CRYPTOERCNew || method == DEPOSITMETHOD.CRYPTOERC)
		{
			walletaddress = "0x8E6fd509F491152bD377854ec3CeD86e96c2e94e";
			crypto_withdraw.setCryptoWithdrawalInfoNew(walletaddress);
		}
		else if (method == DEPOSITMETHOD.CRYPTOTRCNew || method == DEPOSITMETHOD.CRYPTOTRC)
		{
			walletaddress = "TQETUR38cL8nY1gQcr43sWgFFwTKSwQjLw";
			crypto_withdraw.setCryptoWithdrawalInfoNew(walletaddress);
		}
		else if (method == DEPOSITMETHOD.ETH_New || method == DEPOSITMETHOD.ETH)
		{
			walletaddress = "0xC6067650a116153E6123Bb252A28252b9ee3eE1c";
			crypto_withdraw.setCryptoWithdrawalInfoNew(walletaddress);
		}
		else if (method == DEPOSITMETHOD.USDCNew || method == DEPOSITMETHOD.USDC)
		{
			walletaddress = "0x6dba6f6b122038854e299c3033757aa681ec2170";
			crypto_withdraw.setCryptoWithdrawalInfoNew(walletaddress);
		}
		else if (method == DEPOSITMETHOD.CRYPTOBEPNew || method == DEPOSITMETHOD.CRYPTOBEP)
		{
			walletaddress = "0x85fdb5595095403c4df0b6327b79c7f77d30cef9";
			crypto_withdraw.setCryptoWithdrawalInfoNew(walletaddress);
		}
		Assert.assertTrue(crypto_withdraw.submit(),"Submit failure or error occurs! Please make sure correct withdraw info configured in disconf (biz_withdraw.properties)");
		Assert.assertTrue(this.checkHistory(amount, method, rebateAcc),"Have not found the withdraw history");
		GlobalMethods.printDebugInfo("***Test " + method.getIBWithdrawName() + " withdraw succeed!!********");
	}

	@Test(priority = 0)
	public void testAustraliaBanktWithdrawNew() {
		IBAustraliaBankWithdraw bank_w = myfactor.newInstance(IBAustraliaBankWithdraw.class);
		IBEmailWithdraw withdraw = myfactor.newInstance(IBEmailWithdraw.class);
		Double amount = this.filleBankTransferNew(DEPOSITMETHOD.AUBANKTRANSFER);
		withdraw.clickContinue();

		//testdata
		String bankName = "Test Bank Name";
		String BSB = "34543";
		String BBname = "Test Beneficiary name";
		String bank_acc_num = "3465673";
		String swift = "30033";
		String notes = "Auto test Australia Bank Withdraw";

		selectIBTWithdrawMethodNew(withdraw, DEPOSITMETHOD.AUBANKTRANSFER);
		Assert.assertTrue(bank_w.setWithdrawInfo(bankName, BSB, BBname, swift, bank_acc_num, notes), "Invalid Information");

		withdraw.submit();

		assertTrue(this.checkHistory(amount, DEPOSITMETHOD.AUBANKTRANSFER, rebateAcc),"Have not found the withdraw history");
		GlobalMethods.printDebugInfo("*  Test Australia Bank new account withdraw succeed.");

	}
}

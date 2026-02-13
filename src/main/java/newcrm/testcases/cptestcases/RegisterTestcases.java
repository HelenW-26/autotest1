package newcrm.testcases.cptestcases;

import static org.testng.Assert.assertTrue;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import newcrm.adminapi.AdminAPIUserAccount;
import newcrm.business.adminbusiness.AdminIDPOAAudit;
import newcrm.business.adminbusiness.AdminLogin;
import newcrm.business.adminbusiness.AdminMenu;
import newcrm.business.aubusiness.register.AUProdVFSCCPRegisterGold;
import newcrm.business.businessbase.*;
import newcrm.business.businessbase.owsbase.OWSDashboard;
import newcrm.business.businessbase.profile.CPUserProfile;
import newcrm.business.businessbase.register.CPRegisterDemo;
import newcrm.business.dbbusiness.EmailDB;
import newcrm.factor.Factor;
import newcrm.global.GlobalProperties;
import newcrm.global.GlobalProperties.CPMenuName;
import newcrm.global.GlobalProperties.AdminMenuName;
import newcrm.testcases.BaseTestCaseNew;
import newcrm.utils.UATServerEnv;
import newcrm.utils.api.RsaUtil;
import newcrm.utils.testCaseDescUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WindowType;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;

import newcrm.business.adminbusiness.AdminAPIBusiness;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties.ACCOUNTTYPE;
import newcrm.global.GlobalProperties.BRAND;
import newcrm.global.GlobalProperties.CURRENCY;
import newcrm.global.GlobalProperties.ENV;
import newcrm.global.GlobalProperties.PLATFORM;
import newcrm.global.GlobalProperties.USERTYPE;
import org.testng.annotations.Optional;
import utils.LogUtils;
import vantagecrm.AdminAPITest;

public class RegisterTestcases extends BaseTestCaseNew {

	protected Object data[][];
	public String email, code, ibCode, firstName, lastName, phone, pwd, idnum;
	protected EmailDB emailDB;
    private Factor myfactor;
    protected WebDriver driver;
    private static String AdminName;
    private static String AdminPass;
    protected OWSLogin owsLogin;
    protected OWSDashboard owsDashboard;
    protected String branchVer;
    protected AdminMenu adminMenu;
    protected AdminIDPOAAudit adminIDPOAAudit;

	@Override
	protected void login() {
		LogUtils.info("Do not need to login");
	}

	@Override
	@BeforeMethod(groups = {"CP_Register", "CP_Register_ASIC", "CP_Live_Register", "CP_Demo_Register", "CP_Auto_Register", "CP_Profile_Verification"})
	public void goToCpHomePage() {
		//login = myfactor.getCPLoginInstance(TraderURL);
		LogUtils.info("Do not need go to home page");
	}

    @BeforeMethod(groups = {"CP_Register", "CP_Register_ASIC", "CP_Live_Register", "CP_Demo_Register", "CP_Auto_Register", "CP_Profile_Verification"})
    public void initMethod(){
        if (AdminName==null || AdminPass==null){
            BaseTestCaseNew.UserConfig user = getConfigNew();
            AdminName = user.AdminName;
            AdminPass = user.AdminPass;
        }
        if (driver ==null){
            driver = getDriverNew();
        }
        if (myfactor == null){
            myfactor = getFactorNew();
        }
    }
	@AfterMethod(groups = {"CP_Register", "CP_Profile_Verification"})
	public void tearDown(ITestResult result) {
		if (Brand.equalsIgnoreCase(BRAND.VT.toString())) {
			clearLoginSession();
			driver.navigate().refresh();
			LogUtils.info("Refresh page");
		}
	}
	public String getCountry(String country, boolean bIsUseSumsub) {
		// Refer to Apollo配置: 	sumsub.black.list
		if(country.equalsIgnoreCase("")) {
			ENV env = ENV.valueOf(TestEnv.toUpperCase());
			GlobalProperties.REGULATOR reg = GlobalProperties.REGULATOR.valueOf(Regulator.toUpperCase());

			if((Brand.equalsIgnoreCase(BRAND.PUG.toString()) && !Regulator.equalsIgnoreCase(GlobalProperties.REGULATOR.ASIC.toString())) ||
					Brand.equalsIgnoreCase(BRAND.VT.toString()) ||
					(Brand.equalsIgnoreCase(BRAND.STAR.toString()) && !Regulator.equalsIgnoreCase(GlobalProperties.REGULATOR.ASIC.toString())))
			{
				country = bIsUseSumsub ? "Italy" : "Malta";
			}
			else if(Brand.equalsIgnoreCase(BRAND.VJP.toString()))
			{
				country = bIsUseSumsub ? "Japan" : "Malta";
			}
			else {
				switch(reg) {
					case ASIC:
						country = "Australia";
						break;
					case VFSC:
						country = "Malaysia";
						break;
					case VFSC2,SVG:
						if (env == ENV.PROD) {
							country = "France";
							break;
						}

						country = bIsUseSumsub ? "Estonia" : "Latvia";
						break;
					default:
						country = "France";
						break;
				}
			}
		}

		return country;
	}

	public void navigateTraderUrl(CPRegister cp, String ibCode, String rafCode, String campaignCode) {
		//you couldn't use ibcode and rafcode at same time
		if(ibCode.trim().length()>0) {
			cp.setIBcode(ibCode.trim(), TestEnv.equalsIgnoreCase(ENV.PROD.toString()));
		}else {
			if(rafCode.trim().length()>0) {
				cp.setRAFCode(rafCode);
			}else {
				if(campaignCode.trim().length()>0) {
					cp.setCampaignCode(campaignCode);
				}
			}
		}

		cp.setTradeUrl(TraderURL);
	}

	public void navigateTraderUrlDemo(CPRegister cp, String ibCode, String rafCode, String campaignCode) {
		//you couldn't use ibcode and rafcode at same time
		if(ibCode.trim().length()>0) {
			cp.setIBcode(ibCode.trim(), TestEnv.equalsIgnoreCase(ENV.PROD.toString()));
		}else {
			if(rafCode.trim().length()>0) {
				cp.setRAFCode(rafCode);
			}else {
				if(campaignCode.trim().length()>0) {
					cp.setCampaignCode(campaignCode);
				}
			}
		}
	}

	public void generateUserTestData(CPRegister cp) throws Exception {
		emailDB = new EmailDB(dbenv, dbBrand, dbRegulator);

		firstName = "autotest" + GlobalMethods.getRandomString(10);
		lastName = "TestCRM";
		email = ("autotest"+GlobalMethods.getRandomString(8)+"@testcrmautomation.com").toLowerCase();
		phone = "0000"+GlobalMethods.getRandomNumberString(10); //发送短信API，必须添加4个零为手机号前缀避免收费
		idnum = "TESTID"+GlobalMethods.getRandomNumberString(10);
		pwd = GlobalMethods.generatePassword();
		code = "987654";
	}

	public void generateDemoUserTestData(CPRegister cp) throws Exception {
		emailDB = new EmailDB(dbenv, dbBrand, dbRegulator);

		firstName = "autodemo" + GlobalMethods.getRandomString(10);
		lastName = "AutoCRM";
		email = ("autodemo"+GlobalMethods.getRandomString(8)+"@nqmo.com").toLowerCase();
		phone = "0000"+GlobalMethods.getRandomNumberString(10); //发送短信API，必须添加4个零为手机号前缀避免收费
		idnum = "TESTID"+GlobalMethods.getRandomNumberString(10);
		pwd = GlobalMethods.generatePassword();
		code = "987654";
	}

	public void generateUserAutoTestData(CPRegister cp) throws Exception {
		emailDB = new EmailDB(dbenv, dbBrand, dbRegulator);

		firstName = "autoCRM" + GlobalMethods.getRandomString(10);
		lastName = "autoCRM";
		email = ("autoCRM"+GlobalMethods.getRandomString(8)+"@crmautomation.com").toLowerCase();
		phone = "0000"+GlobalMethods.getRandomNumberString(10); //发送短信API，必须添加4个零为手机号前缀避免收费
		idnum = "TESTID"+GlobalMethods.getRandomNumberString(10);
		pwd = GlobalMethods.generatePassword();
		code = "987654";
	}

	public HashMap<String,String> register(String ibCode, String rafCode, PLATFORM platform, String country, boolean check
			, ACCOUNTTYPE accountType, CURRENCY currency) throws Exception {
		//instance
		CPRegister cp = myfactor.newInstance(CPRegister.class);
		CPLiveAccounts liveAccounts = myfactor.newInstance(CPLiveAccounts.class);
		CPMenu menu = myfactor.newInstance(CPMenu.class);

		//data
		String firstName = "autotest" + GlobalMethods.getRandomString(10);
		String lastName = "TestCRM";
		String email = "autotest"+GlobalMethods.getRandomString(8)+"@testcrmautomation.com";
		email= email.toLowerCase();
		String phone = GlobalMethods.getRandomNumberString(10);
		String idnum = "TESTID"+GlobalMethods.getRandomNumberString(10);
		//cp.openLiveAccount();

		//you couldn't use ibcode and rafcode at same time
		if(ibCode.trim().length()>0) {
			cp.setIBcode(ibCode.trim(), TestEnv.equalsIgnoreCase(GlobalProperties.ENV.PROD.toString()));
		}else {
			if(rafCode.trim().length()>0) {
				cp.setRAFCode(rafCode);
			}
		}

		cp.setTradeUrl(TraderURL);
		//Random random = new Random();
		//List<String> countries = cp.getAllCountriesOnEntryPage();

		//change country to fit regulator
		if(country.equalsIgnoreCase("")) {
			//不带国家，使用默认国家的情况
			switch(Regulator.toLowerCase()) {
				case "asic": country = "Australia"; break;
				case "vfsc": country = "Malaysia"; break;
				case "vfsc2": country = "France"; break;
				case "cima":
					if(Brand.equalsIgnoreCase("VT")) {
						country = "China";
					}
					break;
				//如果是prod环境，除特殊监管外使用随机国家注册
				default: if(TestEnv.equalsIgnoreCase("prod")) {
					Random random = new Random();
					//List<String> countries = cp.getAllCountriesOnEntryPage();
					//country = countries.get(random.nextInt(countries.size()));
					country="France";
				}else {
					country = "France";
				}
			}
		}else {
			//带国家，但是选择国家不符合指定的监管的情况

		}
		if(TestEnv.equalsIgnoreCase("prod") )
		{
			cp.openLiveAccount();
			if(Brand.equalsIgnoreCase("VFX") || Brand.equalsIgnoreCase("VT")) {
				cp.setEmail(email);
			}
		}

		assertTrue(cp.setRegulatorAndCountry(country, Regulator), "Set Country and regulator failed!");

		cp.setUserInfo(firstName, lastName, phone, email);
		cp.setUserType(USERTYPE.Individual);
        cp.entrySubmit(TraderURL);

		Assert.assertTrue(cp.goToPersonalDetailPage(),"Go to Personal Details page failed!");
		cp.fillPersonalDetails(idnum);

		Assert.assertTrue(cp.goToAddressPage(),"Go to Address page failed!");
		cp.fillAddressDetails();

		Assert.assertTrue(cp.goToFinancialPage(),"Go to Financial page failed!");
		cp.fillFinacialPage();

		Assert.assertTrue(cp.goToAccountPage(),"Go to Account page failed!");
		if(accountType!=null && currency!=null) {
			cp.fillAccountPage(platform, accountType, currency);
		}else {
			cp.fillAccountPage(platform);
		}

		Assert.assertTrue(cp.goToIDPage(),"Go to ID page failed!");
		cp.fillIDPage();

		Assert.assertTrue(cp.goToFinishPage(),"Go to Finish page failed");
		//cp.backtoHome();

		//check info
		if(check) {
			cp.checkUserInfo(email,dbenv,dbBrand,dbRegulator);
			//Assert.assertTrue(cp.checkInfoAfterFinish(firstName, BaseTestCase.Regulator, BaseTestCase.TestEnv,BaseTestCase.Brand), "Failed at check user info");
			if((dbenv.equals(ENV.ALPHA) || dbenv.equals(ENV.UAT)) && !dbBrand.equals(BRAND.AT)) {
				if(auditMainAccount(cp.userdetails.get("User ID"),platform))
				{
					System.out.println("Account Audit Passed");
				}
				Assert.assertTrue(auditMainAccount(cp.userdetails.get("User ID"),platform),"Account Audit Failed");
			}
			else
			{
				menu.goToMenu(CPMenuName.HOME);
				Assert.assertTrue(liveAccounts.checkAccountExist(),"No account exist");
			}
		}
		else
		{
			LogUtils.info("No check for account");
		}
		cp.printUserRegisterInfo();

		return cp.userdetails;
	}

	public HashMap<String,String> registerNew(String ibCode, String rafCode, String campaignID, PLATFORM platform, String country, boolean check
			, ACCOUNTTYPE accountType, CURRENCY currency, boolean bIsUseSumsub) throws Exception {

		//instance
		CPRegister cp = myfactor.newInstance(CPRegister.class);

		// Test Data
		generateUserTestData(cp);
		navigateTraderUrl(cp, ibCode, rafCode, campaignID);
		country = getCountry(country, bIsUseSumsub);

		//for brands already complete registration enhancement
		if(TestEnv.equalsIgnoreCase(ENV.PROD.toString()) )
		{
			List<String> skipBrands = Arrays.asList(BRAND.VJP.toString(),BRAND.PUG.toString(),BRAND.UM.toString(),BRAND.VFX.toString(),BRAND.STAR.toString(),BRAND.MO.toString());
			if(!skipBrands.contains(dbBrand.toString().toUpperCase())) {
				cp.openLiveAccount();
			}
			switch(Brand.toUpperCase()) {
				case "VT":
					cp.setUserInfo(firstName,country,email,pwd);
					break;
				case "MO", "VFX", "PUG":
					cp.setUserInfo(firstName,country,email,pwd);
					cp.entrySubmit(TraderURL);
					break;
				case "VJP":
					cp.setEmail(email);
					setCode(cp,emailDB);
					cp.setUserInfo(firstName,country,email,pwd);
					break;
                case "UM":
					cp.setUserInfo(firstName,country,email,pwd);
					setCode(cp,emailDB);
					cp.entrySubmit(TraderURL);
					break;
				case "STAR":
					cp.setUserInfo(firstName,country,email,pwd);
					setCode(cp,emailDB);
					break;
			}

			//Level 1
			goToPersonalDetailPage(cp);
			fillPersonalDetails(cp);

			// Setup account
			setupAccount(cp, platform, accountType, currency);

			// Level 2 POI
			goToIDPage(cp);
			uploadPOI(cp, country, bIsUseSumsub);

			goToFinishPage(cp);
		}
		else
		{
			//registration api V1 is: /api/registration/register, V2 is:  /api/registrationV2/register
			List<String> v2Brand = Arrays.asList(BRAND.VFX.toString(),BRAND.VT.toString(),BRAND.PUG.toString(),BRAND.UM.toString(),
					BRAND.STAR.toString(),BRAND.VJP.toString(),BRAND.MO.toString());

			if(v2Brand.contains(Brand.toUpperCase())) {
				testSiteRegistrationInfo(cp, country);

				// VFSC - Level 1 Personal Details & Setup Account, Level 2 POI
				// ASIC - Step 1 Personal Details > Step 2 Employment & Financial Details > Step 3 Account Configuration > Step 4 POI & POA > Step 5 Quiz
				// Level 1
				goToPersonalDetailPage(cp);
				fillPersonalDetails(cp);

				if ((Brand.equalsIgnoreCase(BRAND.VFX.toString()) || Brand.equalsIgnoreCase(BRAND.STAR.toString()) || Brand.equalsIgnoreCase(BRAND.PUG.toString())) &&
						Regulator.equalsIgnoreCase(GlobalProperties.REGULATOR.ASIC.toString())) {

					Assert.assertTrue(cp.goToAddressPage(),"Go to Address page failed!");
					cp.fillAddressDetails();

					Assert.assertTrue(cp.goToFinancialPage(),"Go to Financial page failed!");
					cp.fillFinacialPage();

				}else if(Brand.equalsIgnoreCase(BRAND.VFX.toString()) && Regulator.equalsIgnoreCase(GlobalProperties.REGULATOR.FCA.toString())){
                    cp.goToAdditionalDetailsPage();
                    cp.fillAddressDetails();

                    cp.goToFinancialPage();
                    cp.fillFinacialPage();
                }

				// Setup account
				setupAccount(cp, platform, accountType, currency);

				// Level 2 POI
				goToIDPage(cp);
				uploadPOI(cp, country, bIsUseSumsub);

				if ((Brand.equalsIgnoreCase(BRAND.VFX.toString()) || Brand.equalsIgnoreCase(BRAND.STAR.toString()) || Brand.equalsIgnoreCase(BRAND.PUG.toString())) &&
						Regulator.equalsIgnoreCase(GlobalProperties.REGULATOR.ASIC.toString())) {

					Assert.assertTrue(cp.goToQuizPage(), "Go to Quiz page failed!");
					cp.fillQuizPage();
				}

				goToFinishPage(cp);
			}
//			else
//			{
//				cp.setRegulatorAndCountry(country, Regulator, "V1");
//				//cp.sendCode(code);
//				cp.setUserInfo(firstName, lastName, phone, email, pwd, regBrand);
//                cp.entrySubmit(TraderURL);
//				Assert.assertTrue(cp.goToPersonalDetailPage(),"Go to Personal Details page failed!");
//				cp.waitLoading();
//				cp.fillPersonalDetails(idnum);
//
//				Assert.assertTrue(cp.goToAddressPage(),"Go to Address page failed!");
//				cp.fillAddressDetails();
//
//				Assert.assertTrue(cp.goToFinancialPage(),"Go to Financial page failed!");
//				cp.fillFinacialPage();
//
//				Assert.assertTrue(cp.goToAccountPage(),"Go to Account page failed!");
//				if(accountType!=null && currency!=null) {
//					cp.fillAccountPage(platform, accountType, currency);
//				}else {
//					cp.fillAccountPage(platform);
//				}
//
//				Assert.assertTrue(cp.goToIDPage(),"Go to ID page failed!");
//				cp.fillIDPage();
//
//				Assert.assertTrue(cp.goToFinishPage(),"Go to Finish page failed");
//			}
		}

		// Audit account
		HashMap<String,String> userDetails = checkAndAuditAccount(cp, check, platform);

		return userDetails;
	}

    public HashMap<String,String> registerNew_WithoutCheckURL(String ibCode, String rafCode, String campaignID, PLATFORM platform, String country, boolean check
            , ACCOUNTTYPE accountType, CURRENCY currency) throws Exception {

        //instance
        CPRegister cp = myfactor.newInstance(CPRegister.class);
        CPLiveAccounts liveAccounts = myfactor.newInstance(CPLiveAccounts.class);
        CPMenu menu = myfactor.newInstance(CPMenu.class);
        emailDB = new EmailDB(dbenv, dbBrand, dbRegulator);
		boolean bIsUseSumsub = false;

        //data
        String firstName = "autotest" + GlobalMethods.getRandomString(10);
        String lastName = "TestCRM";
        email = ("autotest"+GlobalMethods.getRandomString(8)+"@testcrmautomation.com").toLowerCase();
        String phone = GlobalMethods.getRandomNumberString(10);
        String idnum = "TESTID"+GlobalMethods.getRandomNumberString(10);
        pwd = GlobalMethods.generatePassword();
        code = "987654";
        String regBrand = GlobalMethods.getRegisterBrand(Brand.toUpperCase());

        //you couldn't use ibcode and rafcode at same time
        if(ibCode.trim().length()>0) {
            cp.setIBcode(ibCode.trim(), TestEnv.equalsIgnoreCase(GlobalProperties.ENV.PROD.toString()));
        }else {
            if(rafCode.trim().length()>0) {
                cp.setRAFCode(rafCode);
            }else {
                if(campaignID.trim().length()>0) {
                    cp.setCampaignCode(campaignID);
                }
            }
        }

        cp.setTradeUrl(TraderURL);

        //change country to fit regulator
		country = getCountry(country, bIsUseSumsub);

        //for brands already complete registration enhancement
        if(TestEnv.equalsIgnoreCase(ENV.PROD.toString()) )
        {
            List<String> skipBrands = Arrays.asList(BRAND.VJP.toString(),BRAND.PUG.toString(),BRAND.UM.toString(),BRAND.VFX.toString(),BRAND.STAR.toString(),BRAND.MO.toString());
            if(!skipBrands.contains(dbBrand.toString().toUpperCase())) {
                cp.openLiveAccount();
            }
            switch(Brand.toUpperCase()) {
                case ("VT"):
                    cp.setUserInfo(firstName,country,email,pwd);
                    break;
                case ("MO"), ("VFX"):
                    cp.setUserInfo(firstName,country,email,pwd);
                    cp.entrySubmit(TraderURL);
                    break;
                case ("PUG"):
                    cp.setUserInfo(firstName,country,email,pwd);
                    cp.entrySubmit(TraderURL);
                    setCode(cp,emailDB);
                    break;
                case ("VJP"):
                    cp.setEmail(email);
                    setCode(cp,emailDB);
                    cp.setUserInfo(firstName,country,email,pwd);
                    break;
                case ("UM"):
                    cp.setUserInfo(firstName,country,email,pwd);
                    setCode(cp,emailDB);
                    cp.entrySubmit(TraderURL);
                    break;
                case ("STAR"):
                    cp.setUserInfo(firstName,country,email,pwd);
                    setCode(cp,emailDB);
                    break;
            }

            //Level 1
            Assert.assertTrue(cp.goToPersonalDetailPage(), "Go to Personal Details page failed！");
            cp.waitLoading();

            // Replace with newly created account. Otherwise, next test case with same login will be using newly created account login session
            prevExecuteCPAccEmail = email;

            cp.fillPersonalDetails(idnum, firstName, lastName, phone);

            Assert.assertTrue(cp.goToAccountPageNew(), "Go to Account page failed!");
            if (accountType != null && currency != null) {
                cp.fillAccountPage(platform, accountType, currency);
            } else {
                cp.fillAccountPage(platform);
            }

            //Level 2 & 3
            Assert.assertTrue(cp.goToIDPage(), "Go to ID page failed!");

			LogUtils.info("Require Sumsub Verification: " + bIsUseSumsub);

			// Check exists on Sumsub content
            cp.bIDSumsub = cp.checkSumsubExists();

            if (bIsUseSumsub) {
				if (!cp.bIDSumsub) {
					Assert.fail(String.format("Not able to proceed Sumsub flow due to %s country does not support Sumsub Verification. Please use another country", country));
				}
                cp.fillIDPage_withSumsub(country);
            } else {
				if (cp.bIDSumsub) {
					Assert.fail(String.format("Not able to proceed manual flow due to %s country only support Sumsub Verification. Please use another country", country));
				}
                cp.fillIDPage();
            }

            Assert.assertTrue(cp.goToFinishPage(), "Go to Finish page failed");
        }
        else
        {
            //registration api V1 is: /api/registration/register, V2 is:  /api/registrationV2/register
            List<String> v2Brand = Arrays.asList(BRAND.VFX.toString(),BRAND.VT.toString(),BRAND.PUG.toString(),BRAND.UM.toString(),
                    BRAND.STAR.toString(),BRAND.VJP.toString(),BRAND.MO.toString());

            if(v2Brand.contains(Brand.toUpperCase())) {
                cp.setRegulatorAndCountry(country, Regulator, "V2");
                cp.sendCode(code);
                cp.setUserInfo(firstName, lastName, phone, email, pwd, regBrand);
                cp.setWid(Brand.toString());
                cp.entrySubmit_WithoutCheckURL();

                // VFSC - Level 1 Personal Details & Setup Account, Level 2 POI
                // ASIC - Step 1 Personal Details > Step 2 Employment & Financial Details > Step 3 Account Configuration > Step 4 POI & POA > Step 5 Quiz
                // Level 1
                Assert.assertTrue(cp.goToPersonalDetailPage(), "Go to Personal Details page failed!");
                cp.waitLoading();

                // Replace with newly created account. Otherwise, next test case with same login will be using newly created account login session
                prevExecuteCPAccEmail = email;

                cp.fillPersonalDetails(idnum, firstName, lastName, phone);

                if ((Brand.equalsIgnoreCase(BRAND.VFX.toString()) || Brand.equalsIgnoreCase(BRAND.STAR.toString()) || Brand.equalsIgnoreCase(BRAND.PUG.toString())) &&
                        Regulator.equalsIgnoreCase(GlobalProperties.REGULATOR.ASIC.toString())) {

                    Assert.assertTrue(cp.goToAddressPage(),"Go to Address page failed!");
                    cp.fillAddressDetails();

                    Assert.assertTrue(cp.goToFinancialPage(),"Go to Financial page failed!");
                    cp.fillFinacialPage();

                }else if(Brand.equalsIgnoreCase(BRAND.VFX.toString()) && Regulator.equalsIgnoreCase(GlobalProperties.REGULATOR.FCA.toString())){
                    cp.goToAdditionalDetailsPage();
                    cp.fillAddressDetails();

                    cp.goToFinancialPage();
                    cp.fillFinacialPage();
                }

                Assert.assertTrue(cp.goToAccountPageNew(), "Go to Account page failed!");
                if (accountType != null && currency != null) {
                    cp.fillAccountPage(platform, accountType, currency);
                } else {
                    cp.fillAccountPage(platform);
                }

                // Level 2 POI
                Assert.assertTrue(cp.goToIDPage(), "Go to ID page failed!");

				LogUtils.info("Require Sumsub Verification: " + bIsUseSumsub);

				// Check exists on Sumsub content
                cp.bIDSumsub = cp.checkSumsubExists();

                if (bIsUseSumsub) {
					if (!cp.bIDSumsub) {
						Assert.fail(String.format("Not able to proceed Sumsub flow due to %s country does not support Sumsub Verification. Please use another country", country));
					}
                    cp.fillIDPage_withSumsub(country);
                } else {
					if (cp.bIDSumsub) {
						Assert.fail(String.format("Not able to proceed manual flow due to %s country only support Sumsub Verification. Please use another country", country));
					}
                    cp.fillIDPage();
                }

                if ((Brand.equalsIgnoreCase(BRAND.VFX.toString()) || Brand.equalsIgnoreCase(BRAND.STAR.toString()) || Brand.equalsIgnoreCase(BRAND.PUG.toString())) &&
                        Regulator.equalsIgnoreCase(GlobalProperties.REGULATOR.ASIC.toString())) {

                    Assert.assertTrue(cp.goToQuizPage(), "Go to Quiz page failed!");
                    cp.fillQuizPage();
                }

                Assert.assertTrue(cp.goToFinishPage(), "Go to Finish page failed");
            }
            else
            {
                cp.setRegulatorAndCountry(country, Regulator, "V1");
                //cp.sendCode(code);
                cp.setUserInfo(firstName, lastName, phone, email, pwd, regBrand);
                cp.entrySubmit(TraderURL);
                Assert.assertTrue(cp.goToPersonalDetailPage(),"Go to Personal Details page failed!");
                cp.waitLoading();
                cp.fillPersonalDetails(idnum);

                Assert.assertTrue(cp.goToAddressPage(),"Go to Address page failed!");
                cp.fillAddressDetails();

                Assert.assertTrue(cp.goToFinancialPage(),"Go to Financial page failed!");
                cp.fillFinacialPage();

                Assert.assertTrue(cp.goToAccountPage(),"Go to Account page failed!");
                if(accountType!=null && currency!=null) {
                    cp.fillAccountPage(platform, accountType, currency);
                }else {
                    cp.fillAccountPage(platform);
                }

                Assert.assertTrue(cp.goToIDPage(),"Go to ID page failed!");
                cp.fillIDPage();

                Assert.assertTrue(cp.goToFinishPage(),"Go to Finish page failed");
            }
        }
        //check info
        if(check) {
            LogUtils.info("Check account exists");

            cp.checkUserInfo(email,dbenv,dbBrand,dbRegulator);
            //Assert.assertTrue(cp.checkInfoAfterFinish(firstName, BaseTestCase.Regulator, BaseTestCase.TestEnv,BaseTestCase.Brand), "Failed at check user info");
            if((dbenv.equals(ENV.ALPHA) || dbenv.equals(ENV.UAT)) && !dbBrand.equals(BRAND.AT)) {
                if(auditMainAccount(cp.userdetails.get("User ID"),platform))
                {
                    System.out.println("Account Audit Passed");
                }
                Assert.assertTrue(auditMainAccount(cp.userdetails.get("User ID"),platform),"Account Audit Failed");
            }
            else
            {
                menu.goToMenu(CPMenuName.LIVEACCOUNTS);

                liveAccounts.waitLoadingAccountListContent();
                liveAccounts.setViewContentGridMode();

                Assert.assertTrue(liveAccounts.checkAccountExist(),"No account exist");
            }
        }
        else
        {
            LogUtils.info("Skip account existence check");
        }
        cp.printUserRegisterInfo();

        return cp.userdetails;
    }

	public HashMap<String,String> registerNew_Temp(String ibCode, String rafCode, PLATFORM platform, String country, boolean check
			, ACCOUNTTYPE accountType, CURRENCY currency) throws Exception {

		//instance
		CPRegister cp = myfactor.newInstance(CPRegister.class);
		AUProdVFSCCPRegisterGold cpRegGoldTemp = myfactor.newInstance(AUProdVFSCCPRegisterGold.class);
		CPLiveAccounts liveAccounts = myfactor.newInstance(CPLiveAccounts.class);
		CPMenu menu = myfactor.newInstance(CPMenu.class);
		emailDB = new EmailDB(dbenv, dbBrand, dbRegulator);

		//data
		String firstName = "autotest" + GlobalMethods.getRandomString(10);
		String lastName = "TestCRM";
		email = ("autotest"+GlobalMethods.getRandomString(8)+"@testcrmautomation.com").toLowerCase();
		String phone = GlobalMethods.getRandomNumberString(10);
		String idnum = "TESTID"+GlobalMethods.getRandomNumberString(10);
		pwd = GlobalMethods.generatePassword();
		code = "987654";
		String regBrand = GlobalMethods.getRegisterBrand(Brand.toUpperCase());

		//you couldn't use ibcode and rafcode at same time
		if(ibCode.trim().length()>0) {
			cp.setIBcode(ibCode.trim(), TestEnv.equalsIgnoreCase(GlobalProperties.ENV.PROD.toString()));
		}else {
			if(rafCode.trim().length()>0) {
				cp.setRAFCode(rafCode);
			}
		}

//		cp.setTradeUrl(BaseTestCase.TraderURL);

		//change country to fit regulator
		if(country.equalsIgnoreCase("")) {
			if((Brand.equalsIgnoreCase(BRAND.PUG.toString()) && !Regulator.equalsIgnoreCase(GlobalProperties.REGULATOR.ASIC.toString())) ||
					Brand.equalsIgnoreCase(BRAND.VT.toString()) ||
					(Brand.equalsIgnoreCase(BRAND.STAR.toString()) && !Regulator.equalsIgnoreCase(GlobalProperties.REGULATOR.ASIC.toString())))
			{
				country = "Italy";
			}
			else if(Brand.equalsIgnoreCase(BRAND.VJP.toString()))
			{
				country = "Japan";
			}
			else {
				switch(Regulator.toLowerCase()) {
					case "asic":
						country = "Australia";
						break;
					case "vfsc":
						country = "Malaysia";
						break;
					case "vfsc2":
						country = "France";
						break;
					//如果是prod环境，除特殊监管外使用随机国家注册
					default:
						country = "France";
						break;
				}
			}
		}
		//for brands already complete registration enhancement
		if(TestEnv.equalsIgnoreCase(ENV.PROD.toString()) )
		{
			List<String> skipBrands = Arrays.asList(BRAND.VJP.toString(),BRAND.PUG.toString(),BRAND.UM.toString(),BRAND.VFX.toString(),BRAND.STAR.toString(),BRAND.MO.toString());
			if(!skipBrands.contains(dbBrand.toString().toUpperCase())) {
				cp.openLiveAccount();
			}
			switch(Brand.toUpperCase()) {
				case ("VT"):
					cp.setUserInfo(firstName,country,email,pwd);
					break;
				case ("MO"), ("VFX"):
					cp.setUserInfo(firstName,country,email,pwd);
					cp.entrySubmit(TraderURL);
					break;
				case ("PUG"):
					cp.setUserInfo(firstName,country,email,pwd);
					cp.entrySubmit(TraderURL);
					setCode(cp,emailDB);
					break;
				case ("VJP"):
					cp.setEmail(email);
					setCode(cp,emailDB);
					cp.setUserInfo(firstName,country,email,pwd);
					break;
				case ("UM"):
					cp.setUserInfo(firstName,country,email,pwd);
					setCode(cp,emailDB);
					cp.entrySubmit(TraderURL);
					break;
				case ("STAR"):
					cp.setUserInfo(firstName,country,email,pwd);
					setCode(cp,emailDB);
					break;
			}

			//Level 1
			Assert.assertTrue(cp.goToPersonalDetailPage(), "Go to Personal Details page failed！");
			cp.waitLoading();

			// Replace with newly created account. Otherwise, next test case with same login will be using newly created account login session
			prevExecuteCPAccEmail = email;

			cp.fillPersonalDetails(idnum, firstName, lastName, phone);

			Assert.assertTrue(cp.goToAccountPageNew(), "Go to Account page failed!");
			if (accountType != null && currency != null) {
				cp.fillAccountPage(platform, accountType, currency);
			} else {
				cp.fillAccountPage(platform);
			}

			//Level 2 & 3
			Assert.assertTrue(cp.goToIDPage(), "Go to ID page failed!");

			cp.bIDSumsub = cp.checkSumsubExists();
			LogUtils.info("Require Sumsub Verification: " + cp.bIDSumsub);

			if (cp.bIDSumsub) {
				cp.fillIDPage_withSumsub(country);
			} else {
				cp.fillIDPage();
			}

			Assert.assertTrue(cp.goToFinishPage(), "Go to Finish page failed");
		}
		else
		{
			//registration api V1 is: /api/registration/register, V2 is:  /api/registrationV2/register
			List<String> v2Brand = Arrays.asList(BRAND.VFX.toString(),BRAND.VT.toString(),BRAND.PUG.toString(),BRAND.UM.toString(),
					BRAND.STAR.toString(),BRAND.VJP.toString(),BRAND.MO.toString());

			if(v2Brand.contains(Brand.toUpperCase())) {
				// AU alpha temp registration. Wait 17.11 all brand 双端统一 go live then remove
				cpRegGoldTemp.registerLiveAccountASIC_Temp((String)data[0][3], dbRegulator);
				cpRegGoldTemp.setEmail(email);
				cp.sendCode("999999");
				cpRegGoldTemp.setPwd(pwd);
				cpRegGoldTemp.checkASICUNonUS();
				cpRegGoldTemp.entrySubmit_Temp();

				cpRegGoldTemp.setASICFirstName(firstName);
				cpRegGoldTemp.setASICLastName(lastName);
				cpRegGoldTemp.setMobile("0000"+GlobalMethods.getRandomNumberString(5));
				cpRegGoldTemp.entrySubmit(TraderURL);

				// VFSC - Level 1 Personal Details & Setup Account, Level 2 POI
				// ASIC - Step 1 Personal Details > Step 2 Employment & Financial Details > Step 3 Account Configuration > Step 4 POI & POA > Step 5 Quiz
				// Level 1
				Assert.assertTrue(cp.goToPersonalDetailPage(), "Go to Personal Details page failed!");
				cp.waitLoading();

				// Replace with newly created account. Otherwise, next test case with same login will be using newly created account login session
				prevExecuteCPAccEmail = email;

				cp.fillPersonalDetails(idnum, firstName, lastName, phone);

				if ((Brand.equalsIgnoreCase(BRAND.VFX.toString()) || Brand.equalsIgnoreCase(BRAND.STAR.toString()) || Brand.equalsIgnoreCase(BRAND.PUG.toString())) &&
						Regulator.equalsIgnoreCase(GlobalProperties.REGULATOR.ASIC.toString())) {

					Assert.assertTrue(cp.goToAddressPage(),"Go to Address page failed!");
					cp.fillAddressDetails();

					Assert.assertTrue(cp.goToFinancialPage(),"Go to Financial page failed!");
					cp.fillFinacialPage();
				}

				Assert.assertTrue(cp.goToAccountPageNew(), "Go to Account page failed!");
				if (accountType != null && currency != null) {
					cp.fillAccountPage(platform, accountType, currency);
				} else {
					cp.fillAccountPage(platform);
				}

				// Level 2 POI
				Assert.assertTrue(cp.goToIDPage(), "Go to ID page failed!");

				cp.bIDSumsub = cp.checkSumsubExists();
				LogUtils.info("Require Sumsub Verification: " + cp.bIDSumsub);

				if (cp.bIDSumsub) {
					cp.fillIDPage_withSumsub(country);
				} else {
					cp.fillIDPage();
				}

				if ((Brand.equalsIgnoreCase(BRAND.VFX.toString()) || Brand.equalsIgnoreCase(BRAND.STAR.toString()) || Brand.equalsIgnoreCase(BRAND.PUG.toString())) &&
						Regulator.equalsIgnoreCase(GlobalProperties.REGULATOR.ASIC.toString())) {

					Assert.assertTrue(cp.goToQuizPage(), "Go to Quiz page failed!");
					cp.fillQuizPage();
				}

				Assert.assertTrue(cp.goToFinishPage(), "Go to Finish page failed");
			}
			else
			{
				cp.setRegulatorAndCountry(country, Regulator, "V1");
				//cp.sendCode(code);
				cp.setUserInfo(firstName, lastName, phone, email, pwd, regBrand);
				cp.entrySubmit(TraderURL);
				Assert.assertTrue(cp.goToPersonalDetailPage(),"Go to Personal Details page failed!");
				cp.waitLoading();
				cp.fillPersonalDetails(idnum);

				Assert.assertTrue(cp.goToAddressPage(),"Go to Address page failed!");
				cp.fillAddressDetails();

				Assert.assertTrue(cp.goToFinancialPage(),"Go to Financial page failed!");
				cp.fillFinacialPage();

				Assert.assertTrue(cp.goToAccountPage(),"Go to Account page failed!");
				if(accountType!=null && currency!=null) {
					cp.fillAccountPage(platform, accountType, currency);
				}else {
					cp.fillAccountPage(platform);
				}

				Assert.assertTrue(cp.goToIDPage(),"Go to ID page failed!");
				cp.fillIDPage();

				Assert.assertTrue(cp.goToFinishPage(),"Go to Finish page failed");
			}
		}
		//check info
		if(check) {
			LogUtils.info("Check account exists");

			cp.checkUserInfo(email,dbenv,dbBrand,dbRegulator);
			//Assert.assertTrue(cp.checkInfoAfterFinish(firstName, BaseTestCase.Regulator, BaseTestCase.TestEnv,BaseTestCase.Brand), "Failed at check user info");
			if((dbenv.equals(ENV.ALPHA) || dbenv.equals(ENV.UAT)) && !dbBrand.equals(BRAND.AT)) {
				if(auditMainAccount(cp.userdetails.get("User ID"),platform))
				{
					System.out.println("Account Audit Passed");
				}
				Assert.assertTrue(auditMainAccount(cp.userdetails.get("User ID"),platform),"Account Audit Failed");
			}
			else
			{
				menu.goToMenu(CPMenuName.LIVEACCOUNTS);

				liveAccounts.waitLoadingAccountListContent();
				liveAccounts.setViewContentGridMode();

				Assert.assertTrue(liveAccounts.checkAccountExist(),"No account exist");
			}
		}
		else
		{
			LogUtils.info("Skip account existence check");
		}
		cp.printUserRegisterInfo();

		return cp.userdetails;
	}

	public HashMap<String,String> registerDemo(String ibCode, String rafCode, PLATFORM platform, String country, boolean check
			, ACCOUNTTYPE accountType, CURRENCY currency, boolean bIsUseSumsub) throws Exception {

		//instance
		CPRegister cp = myfactor.newInstance(CPRegister.class);
		CPRegisterDemo cpDemo = myfactor.newInstance(CPRegisterDemo.class);

		// Test Data
		generateDemoUserTestData(cp);
		navigateTraderUrlDemo(cp, ibCode, rafCode,"");
		country = getCountry(country, bIsUseSumsub);

		// Demo account registration
		String url = (String)data[0][3];

		if (Brand.equalsIgnoreCase(BRAND.PUG.toString())) {
			if(url.lastIndexOf("/") != url.length() - 1) {
				url = url + "/";
			}
		}

		cpDemo.openDemoAccount(url);
		cpDemo.fillDemoEntryPage(email, firstName, lastName, country, phone, pwd, branchVer);
		cpDemo.fillDemoAccountPage(platform, accountType, currency, "100", "1000", branchVer);
        cpDemo.entrySubmit(TraderURL);

		// VFSC - Level 1 Personal Details & Setup Account, Level 2 POI
		// ASIC - Step 1 Personal Details > Step 2 Employment & Financial Details > Step 3 Account Configuration > Step 4 POI & POA > Step 5 Quiz
		// Level 1
		goToPersonalDetailPage(cp);

		LogUtils.info("Check Demo Account Details");
		cpDemo.checkDemoAccountDetails();
		cpDemo.proceedToOpenLiveAccount();
		fillPersonalDetails(cp);

		// Setup account
		setupAccount(cp, platform, accountType, currency);

		// Level 2 POI
		goToIDPage(cp);
		uploadPOI(cp, country, bIsUseSumsub);

		goToFinishPage(cp);

		// Check account audit status
		HashMap<String,String> userDetails = checkAccountAuditStatus(cp, check, platform, accountType, currency, country);

		return userDetails;
	}

	public HashMap<String,String> registerAuto(String ibCode, String rafCode, PLATFORM platform, String country
			, ACCOUNTTYPE accountType, CURRENCY currency, boolean bIsUseSumsub) throws Exception {

		//instance
		CPRegister cp = myfactor.newInstance(CPRegister.class);

		// Test Data
		generateUserAutoTestData(cp);
		navigateTraderUrl(cp, ibCode, rafCode, "");
		country = getCountry(country, bIsUseSumsub);

		// Fill up registration info (before redirect to CP)
		testSiteRegistrationInfo(cp, country);


		// VFSC - Level 1 Personal Details & Setup Account, Level 2 POI
		// ASIC - Step 1 Personal Details > Step 2 Employment & Financial Details > Step 3 Account Configuration > Step 4 POI & POA > Step 5 Quiz
		// Level 1
		goToPersonalDetailPage(cp);
		fillPersonalDetails(cp);

		// Setup account
		setupAccount(cp, platform, accountType, currency);

		// Level 2 POI
		goToIDPage(cp);
		uploadPOI(cp, country, bIsUseSumsub);

		goToFinishPage(cp);

		// Check account audit status
		HashMap<String,String> userDetails = checkAccountAuditStatus(cp, true, platform, accountType, currency, country);

		return userDetails;
	}

	public HashMap<String,String> profileVerification(String ibCode, String rafCode, PLATFORM platform, String country,
														   ACCOUNTTYPE accountType, CURRENCY currency, boolean bIsUseSumsub) throws Exception {

		//instance
		CPRegister cp = myfactor.newInstance(CPRegister.class);
		CPMenu menu = myfactor.newInstance(CPMenu.class);
		CPUserProfile userProfile = myfactor.newInstance(CPUserProfile.class);

		// Test Data
		generateUserAutoTestData(cp);
		navigateTraderUrl(cp, ibCode, rafCode, "");
		country = getCountry(country, bIsUseSumsub);

		// Fill up registration info (before redirect to CP)
		testSiteRegistrationInfo(cp, country);

		// VFSC - Level 1 Personal Details & Setup Account, Level 2 POI
		// ASIC - Step 1 Personal Details > Step 2 Employment & Financial Details > Step 3 Account Configuration > Step 4 POI & POA > Step 5 Quiz
		// Level 1
		goToPersonalDetailPage(cp);

		if (!Brand.equalsIgnoreCase(BRAND.MO.toString())) {
			cp.closeOpenAccountDialog();
			menu.closeCouponGuideDialog();
		}

		// Check Profile Verification Tab Content
		profileVerificationLv1PersonalDetails(cp, menu, userProfile, accountType, currency, platform);
		profileVerificationLv2POIUpload(cp, menu, userProfile, country, bIsUseSumsub);

		if (!bIsUseSumsub) {
			profileVerificationLv3POAUpload(cp, menu, userProfile, country, bIsUseSumsub);
		}

		return null;
	}

	private void profileVerificationLv1PersonalDetails(CPRegister cp, CPMenu menu, CPUserProfile userProfile,
													   ACCOUNTTYPE accountType, CURRENCY currency, PLATFORM platform) throws Exception {
		// Go to profile page
		System.out.println("***Check Profile Verification Tab Content before complete lv 1***");
		menu.goToMenu(CPMenuName.PROFILES);
		userProfile.waitLoadingProfileTabListContent();

		// Verify Verification Tab Content before completed lv 1 Personal Details Verification
		userProfile.checkVerificationTabContentBeforeCompleteLv1();

		// Complete lv 1 Personal Details Verification
		System.out.println("***Proceed Lv 1 Personal Details Verification***");
		userProfile.clickProfileVTablv1VerifyBtn();

		fillPersonalDetails(cp);

		// Setup account
		setupAccount(cp, platform, accountType, currency);

		// Level 2 POI
		goToIDPage(cp);

		if (Brand.equalsIgnoreCase(BRAND.MO.toString())) {
			// Go to profile page
			menu.goToMenu(CPMenuName.PROFILES);
			userProfile.waitLoadingProfileTabListContent();
		} else {
			cp.closeOpenAccountDialog();
		}

		System.out.println("***Check Profile Verification Tab Content after complete lv 1***");

		// Verify Verification Tab Content after completed lv 1 Personal Details Verification
		userProfile.checkVerificationTabContentAfterCompleteLv1();

		System.out.println("***Completed Lv 1 Personal Details Verification***");
	}

	private void profileVerificationLv2POIUpload(CPRegister cp, CPMenu menu, CPUserProfile userProfile, String country, boolean bIsUseSumsub) {
		System.out.println("***Proceed Lv 2 Identity Verification***");
		userProfile.clickProfileVTablv2VerifyBtn();

		// Upload POI
		uploadPOI(cp, country, bIsUseSumsub);

		if (Brand.equalsIgnoreCase(BRAND.MO.toString())) {
			// Go to profile page
			menu.goToMenu(CPMenuName.PROFILES);
			userProfile.waitLoadingProfileTabListContent();
		} else {
			cp.closeOpenAccountDialog();
		}

		System.out.println("***Check Profile Verification Tab Content after complete lv 2***");

		// Verify Verification Tab Content after completed lv 2 Identity Verification
		userProfile.checkVerificationTabContentAfterCompleteLv2(bIsUseSumsub);

		System.out.println("***Completed Lv 2 Identity Verification***");
	}

	private void profileVerificationLv3POAUpload(CPRegister cp, CPMenu menu, CPUserProfile userProfile, String country, boolean bIsUseSumsub) {
		System.out.println("***Proceed Lv 3 Residency Address Verification***");
		userProfile.clickProfileVTablv3VerifyBtn();

		// Upload POA
		uploadPOA(cp, country, bIsUseSumsub);

		if (Brand.equalsIgnoreCase(BRAND.MO.toString())) {
			// Go to profile page
			menu.goToMenu(CPMenuName.PROFILES);
			userProfile.waitLoadingProfileTabListContent();
		} else {
			cp.closeOpenAccountDialog();
		}

		System.out.println("***Completed Lv 3 Residency Address Verification***");
	}


	private void testSiteRegistrationInfo(CPRegister cp, String country) {
		cp.setRegulatorAndCountry(country, Regulator, "V2");
		cp.sendCode(code);
		cp.setUserInfo(firstName, lastName, phone, email, pwd, GlobalMethods.getRegisterBrand(Brand.toUpperCase()));
		cp.setWid(Brand);
		cp.setBranchVersion(branchVer);
		cp.entrySubmit(TraderURL);
	}

	private void goToPersonalDetailPage(CPRegister cp) {
		Assert.assertTrue(cp.goToPersonalDetailPage(), "Go to Personal Details page failed!");
		cp.waitLoading();

		// Replace with newly created account. Otherwise, next test case with same login will be using newly created account login session
		prevExecuteCPAccEmail = email;
	}

	private void goToIDPage(CPRegister cp) {
		Assert.assertTrue(cp.goToIDPage(), "Go to ID page failed!");
	}

	private void goToFinishPage(CPRegister cp) {
		Assert.assertTrue(cp.goToFinishPage(), "Go to Finish page failed");
	}

	private void setupAccount(CPRegister cp, PLATFORM platform, ACCOUNTTYPE accountType, CURRENCY currency) throws Exception {
		Assert.assertTrue(cp.goToAccountPageNew(), "Go to Account page failed!");
		if (accountType != null && currency != null) {
			cp.fillAccountPage(platform, accountType, currency);
		} else {
			cp.fillAccountPage(platform);
		}
	}

	public void fillPersonalDetails(CPRegister cp) {
		cp.fillPersonalDetails(idnum, firstName, lastName, phone);
	}

	private void uploadPOI(CPRegister cp, String country, boolean bIsUseSumsub) {
		LogUtils.info("Require Sumsub Verification: " + bIsUseSumsub);

		// Check exists on Sumsub content
		cp.bIDSumsub = cp.checkSumsubExists();

		if (bIsUseSumsub) {
			if (!cp.bIDSumsub) {
				Assert.fail(String.format("Not able to proceed Sumsub flow due to %s country does not support Sumsub Verification. Please use another country", country));
			}
			cp.fillIDPage_withSumsub(country);
		} else {
			if (cp.bIDSumsub) {
				Assert.fail(String.format("Not able to proceed manual flow due to %s country only support Sumsub Verification. Please use another country", country));
			}
			cp.fillIDPage();
		}
	}

	private void uploadPOA(CPRegister cp, String country, boolean bIsUseSumsub) {
		LogUtils.info("Require Sumsub Verification: " + bIsUseSumsub);

		// Check exists on Sumsub content
		boolean bIsExistsSumsub = cp.checkSumsubExists();

		if (bIsUseSumsub) {
			if (!bIsExistsSumsub) {
				Assert.fail(String.format("Not able to proceed POA Sumsub flow due to %s country does not support Sumsub Verification. Please use another country", country));
			}
			cp.fillAddressDetails_withSumsub(country);
		} else {
			if (bIsExistsSumsub) {
				Assert.fail(String.format("Not able to proceed POA manual flow due to %s country only support Sumsub Verification. Please use another country", country));
			}
			cp.fillAddressDetails();
		}
	}

	private HashMap<String,String> checkAndAuditAccount(CPRegister cp, boolean bIsReqCheckAcc, PLATFORM platform) throws Exception {
		// Proceed account checking when require
		if(bIsReqCheckAcc) {
			// Check exists user via db
			cp.checkUserInfo(email,dbenv,dbBrand,dbRegulator);

			if(dbenv.equals(ENV.ALPHA) || dbenv.equals(ENV.UAT)) {
				// Audit account
				auditMainAccount(cp, platform);
			}
			else
			{
				// For production, check if there is any account created in CP account page
				CPMenu menu = myfactor.newInstance(CPMenu.class);
				menu.goToMenu(GlobalProperties.CPMenuName.LIVEACCOUNTS);

				CPLiveAccounts liveAccounts = myfactor.newInstance(CPLiveAccounts.class);
				liveAccounts.waitLoadingAccountListContent();
				liveAccounts.setViewContentGridMode();

				Assert.assertTrue(liveAccounts.checkAccountExist(),"No account exist");
			}
		}
		else
		{
			LogUtils.info("Skip account existence check");
		}

		cp.printUserRegisterInfo();

		return cp.userdetails;
	}

	private HashMap<String,String> checkAccountAuditStatus(CPRegister cp, boolean bIsReqCheckAcc, PLATFORM platform,
														   ACCOUNTTYPE accountType, CURRENCY currency, String country) throws Exception {
		// Proceed account checking when require
		if(bIsReqCheckAcc) {
			// Check exists user via db
			cp.checkUserInfo(email,dbenv,dbBrand,dbRegulator);

			if(dbenv.equals(ENV.ALPHA) || dbenv.equals(ENV.UAT)) {

				// Check exists user
				cp.checkUserInfo(email,dbenv,dbBrand,dbRegulator);

				// Check exists account after auto audit
				String userId = cp.userdetails.get("User ID");
				if(!checkAutoAuditMainAccount(userId))
				{
					Assert.fail(String.format("Account Auto Audit Failed. Please manually check record audit status and auto audit country settings. User ID: %s, Country: %s", userId, country));
				}

				cp.printUserRegisterInfo();

				// Verify account info and update to test group
				checkAccountInfoAndUpdateToTestGroup(platform, userId, email, accountType, currency);
			}
			else
			{
				// For production, check if there is any account created in CP account page
				CPMenu menu = myfactor.newInstance(CPMenu.class);
				menu.goToMenu(GlobalProperties.CPMenuName.LIVEACCOUNTS);

				CPLiveAccounts liveAccounts = myfactor.newInstance(CPLiveAccounts.class);
				liveAccounts.waitLoadingAccountListContent();
				liveAccounts.setViewContentGridMode();

				Assert.assertTrue(liveAccounts.checkAccountExist(),"No account exist");

				cp.printUserRegisterInfo();
			}
		}
		else
		{
			LogUtils.info("Skip account existence check");
			cp.printUserRegisterInfo();
		}

		return cp.userdetails;
	}

	private void checkAccountInfoAndUpdateToTestGroup(PLATFORM platform, String userId, String email, ACCOUNTTYPE accountType, CURRENCY currency) {
		System.out.println("***Check auto register account info***");

		AdminAPIUserAccount adminAcctAPI = new AdminAPIUserAccount((String) data[0][4], GlobalProperties.REGULATOR.valueOf((String)data[0][0]), (String)data[0][7], (String)data[0][8], BRAND.valueOf(String.valueOf(dbBrand)), ENV.valueOf(String.valueOf(dbenv)));

		// Search trading account info
		JSONObject obj = adminAcctAPI.apiTradingAcctSearch("", RsaUtil.getAdminEmailEncrypt(email), platform, userId, "auto", "ALL");
		JSONArray rows = obj.getJSONArray("rows");

		if(rows==null || rows.isEmpty()) {
			Assert.fail("Auto Register trading account not found. User ID: " + userId);
		}

		JSONObject tradingAcct = rows.getJSONObject(0);
		String accNum = tradingAcct.getString("mt4_account");
		String mt4AccountType = tradingAcct.getString("mt4_account_type");
		String mt4AccountTypeDisplay = tradingAcct.getString("mt4_account_type_display");
		String serverId = tradingAcct.getString("mt4_datasource_id");
		String accLeverage = tradingAcct.getString("leverage");
		String accGroup = tradingAcct.getString("group");
		String accCurrency = tradingAcct.getString("currency");
		String owner = tradingAcct.getString("owner");
		String pIds = tradingAcct.getString("p_ids");
		String[] pIdsArray = pIds.split(",");
		String latestPId = pIdsArray[pIdsArray.length - 1];
		JSONObject dataSrc = tradingAcct.getJSONObject("dataSource");
		String serverPlatform = "";
		String defaultLeverage = Brand.equalsIgnoreCase(BRAND.VJP.toString()) ? "1000" : "500";

		LogUtils.info(String.format("Auto Register trading account found. Account: %s, Leverage: %s, Currency: %s, Account Type: %s (%s)", accNum, accLeverage, currency, mt4AccountTypeDisplay, mt4AccountType));

		// Cross-check system data stored with submission info
		// Platform
		if (dataSrc != null) {
			serverPlatform = dataSrc.getString("category");

			LogUtils.info(String.format("Trading Platform: %s", serverPlatform));

			if (serverPlatform == null || serverPlatform.isEmpty()) {
				Assert.fail(String.format("Auto Register Platform is empty. Account: %s", accNum));
			}

			if (!serverPlatform.equals(platform.getPlatformDesc())) {
				Assert.fail(String.format("Auto Register Platform mismatch. Account: %s, Expected: %s, Actual: %s", accNum, platform.getPlatformDesc(), serverPlatform));
			}
		} else {
			Assert.fail(String.format("Auto Register Platform is empty. Account: %s", accNum));
		}

		// Check default leverage
		if (accLeverage == null || accLeverage.isEmpty()) {
			Assert.fail("Auto Register account leverage is empty, Account: " + accNum);
		}

		if (!defaultLeverage.equals(accLeverage.trim())) {
			Assert.fail(String.format("Auto Register default account leverage is incorrect, Account: %s, Expected: %s, Actual: %s", accNum, defaultLeverage, accLeverage));
		}

		// Account Type
		if (mt4AccountType == null || mt4AccountType.isEmpty()) {
			Assert.fail(String.format("Auto Register Account Type Code is empty. Account: %s", accNum));
		}

		if (!mt4AccountType.equals(accountType.getAccountTypeCode())) {
			Assert.fail(String.format("Auto Register Account Type Code mismatch. Account: %s, Expected: %s (%s), Actual: %s (%s)", accNum, accountType.getLiveAccountName(), accountType.getAccountTypeCode(), mt4AccountTypeDisplay, mt4AccountType));
		}

		// Currency
		if (accCurrency == null || accCurrency.isEmpty()) {
			Assert.fail(String.format("Auto Register Currency is empty. Account: %s", accNum));
		}

		if (!accCurrency.equals(currency.getCurrencyDesc())) {
			Assert.fail(String.format("Auto Register Currency mismatch. Account: %s, Expected: %s, Actual: %s", accNum, currency.getCurrencyDesc(), accCurrency));
		}

		System.out.println("***Update account to test group***");

		// Get server's account test group
		List<String> groups = adminAcctAPI.getGroups(serverId, "1", userId, mt4AccountType);
		if(groups==null) {
			Assert.fail("Failed to update account to test group. Reason: No account test group is available for Server ID " + serverId);
		}

		List<String> filtered = groups.stream()
				.filter(groupName -> (groupName.contains("test") || groupName.startsWith("t_")) && groupName.contains(currency.getCurrencyDesc()))
				.toList();

		String newGroup = "";

		if (filtered.isEmpty()) {
			Assert.fail("Failed to update account to test group. Reason: No account test group is available for Server ID " + serverId);
		} else {
			Random random = new Random();
			newGroup = filtered.get(random.nextInt(filtered.size()));
			LogUtils.info("Randomly select account test group: " + newGroup + ", Server ID: " + serverId);
		}

		// Update account to test group
		adminAcctAPI.apiTradingAcctUpdate(accNum, accGroup, mt4AccountType, owner, latestPId, newGroup);
		LogUtils.info("Update account to test group, Account: " + accNum + ", Default Group: " + accGroup + ", New Group: " + newGroup);
	}

	//for Jenkins
	@Test(priority = 0)
	@Parameters(value= {"testMethod","IBCode","Country"})
	public void testRegistLivingAccount(String method,String code,@Optional("")String country) throws Exception {
		switch(method) {
			case "testRegistMT4LiveAccount": testRegistMT4LiveAccount(country); break;
			case "testRegistMT4LiveAccountWithIBCode": testRegistMT4LiveAccountWithIBCode(code,country); break;
			case "testRegistMT4LiveAccountWithRAFCode": testRegistMT4LiveAccountWithRAFCode(code,country); break;
			case "testRegistMT5LiveAccount": testRegistMT5LiveAccount(country); break;
			case "testRegistMT5LiveAccountWithIBCode": testRegistMT5LiveAccountWithIBCode(code,country); break;
			case "testRegistMT5LiveAccountWithRAFCode": testRegistMT5LiveAccountWithRAFCode(code,country); break;
		}
	}
	//MT4

	public HashMap<String,String> createUserForTradingBonus(String country) throws Exception {
		HashMap<String,String> infos = register("","",PLATFORM.MT4,country,true,ACCOUNTTYPE.VIP_STP,CURRENCY.USD);
		HashMap<String,String> user = new HashMap<>();
		user.put("userId", infos.get("User ID"));
		user.put("email", infos.get("Email"));
		user.put("password", infos.get("Password"));
		return user;
	}

	@Test(priority = 0,groups= {"AuRegression"}, description = testCaseDescUtils.CPACC_REGISTER_MT4)
	@Parameters(value= {"Country"})
	public void testRegistMT4LiveAccount(@Optional("")String country) throws Exception {
		register("","",PLATFORM.MT4,country,true,null,null);
	}

	@Test(priority = 0)
	@Parameters(value= {"IBCode","Country"})
	public void testRegistMT4LiveAccountWithIBCode(String IBCode,@Optional("")String country) throws Exception {
		register(IBCode,"",PLATFORM.MT4,country,true,null,null);
	}

	@Test(priority = 0)
	@Parameters(value= {"RAFCode","Country"})
	public String testRegistMT4LiveAccountWithRAFCode(String RAFCode,@Optional("")String country) throws Exception {
		return register("",RAFCode,PLATFORM.MT4,country,true,null,null).get("User ID");
	}

	//MT5
	@Test(priority = 0, description = testCaseDescUtils.CPACC_REGISTER_MT5)
	@Parameters(value= {"Country"})
	public void testRegistMT5LiveAccount(@Optional("")String country) throws Exception {
		register("","",PLATFORM.MT5,country,true,null,null);
	}

	@Test(priority = 0)
	@Parameters(value= {"IBCode","Country"})
	public void testRegistMT5LiveAccountWithIBCode(String IBCode,@Optional("")String country) throws Exception {
		register(IBCode,"",PLATFORM.MT5,country,true,null,null);
	}

	@Test(priority = 0)
	@Parameters(value= {"RAFCode","Country"})
	public void testRegistMT5LiveAccountWithRAFCode(String RAFCode,@Optional("")String country) throws Exception {
		register("",RAFCode,PLATFORM.MT5,country,true,null,null);
	}

	@Test(priority=1)
	public void APIUpdateTestAccGroup() throws Exception {
		/* Updated by AlexL. Had refactored adminapi.APIUpdateTestAccGroup to adapt brand and regulator
		 * String v_Brand = ""; v_Brand =
		 * GlobalMethods.getPreVBrand(BaseTestCase.Regulator, BaseTestCase.Brand);
		 */
		AdminAPITest adminapi = new AdminAPITest();
		adminapi.APIUpdateTestAccGroup(AdminURL, AdminName, AdminPass, TestEnv, Brand, Regulator, null);
	}

	@Test(groups= {"AuRegression", "CP_Live_Register"}, description = testCaseDescUtils.CPACC_REGISTER_MT4_WITHOUT_CHECK)
	@Parameters(value= {"Country"})
	public void testRegistMT4LiveAccountWithoutCheck(@Optional("")String country) throws Exception {
		BRAND[] newRegisterBrands = {BRAND.VFX, BRAND.VT, BRAND.UM, BRAND.PUG, BRAND.VJP,BRAND.STAR,BRAND.MO};
		ibCode = GlobalMethods.getRegAffID(dbBrand.toString(), ENV.PROD);
		//Brands in newRegisterBrands will follow new registration flow while others stay old one.
		if (Arrays.asList(newRegisterBrands).contains(BRAND.valueOf(dbBrand.toString()))) {
			registerNew(ibCode, "", "", GlobalProperties.PLATFORM.MT4, country, false, GlobalProperties.ACCOUNTTYPE.valueOf("STANDARD_STP"), GlobalProperties.CURRENCY.USD, true);
		} else {
			//open account audit check
			register(ibCode, "", PLATFORM.MT4, country, false, null, null);
		}
	}

	@Test(groups= {"AuRegression", "CP_Live_Register"}, description = testCaseDescUtils.CPACC_REGISTER_MT5_WITHOUT_CHECK)
	@Parameters(value= {"Country"})
	public void testRegistMT5LiveAccountWithoutCheck(@Optional("")String country) throws Exception {
		BRAND[] newRegisterBrands = {BRAND.VFX, BRAND.VT, BRAND.UM, BRAND.PUG, BRAND.VJP,BRAND.STAR,BRAND.MO};
		ibCode = GlobalMethods.getRegAffID(dbBrand.toString(), ENV.PROD);
		//Brands in newRegisterBrands will follow new registration flow while others stay old one.
		if (Arrays.asList(newRegisterBrands).contains(BRAND.valueOf(dbBrand.toString()))) {
			registerNew(ibCode, "", "", GlobalProperties.PLATFORM.MT5, country, false, GlobalProperties.ACCOUNTTYPE.valueOf("STANDARD_STP"), GlobalProperties.CURRENCY.USD, true);
		} else {
			//open account audit check
			register(ibCode, "", PLATFORM.MT5, country, false, null, null);
		}
	}

	//assign value to affid according to brand
	public String getPRODIBCode(String brand)
	{
		HashMap<String,String>  brandIBCode = new HashMap<>();
		brandIBCode.put("vfx","NBMjYzODM5Mw==");
		brandIBCode.put("pug","NBNjI0MTkw" + "&time=" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
		brandIBCode.put("vt","NBODA1NjI=");
		brandIBCode.put("um","NBODMwNDI5");
		brandIBCode.put("mo","NBNTY3MTc=");
		brandIBCode.put("star","NBMjcwMDE1OQ==");

		if(!brandIBCode.containsKey(brand.toLowerCase()))
		{
			return("");
		}
		return brandIBCode.get(brand.toLowerCase());
	}

	public boolean auditMainAccount(String userId,PLATFORM platform) throws Exception {
		String user = data[0][7].toString();
		String password = data[0][8].toString();
		String OWSName = data[0][9].toString();
		String OWSPass = data[0][10].toString();

		if(dbenv.equals(ENV.PROD)) {
			user =  "Test CRM";
			password = "Hc8P4RxuKMMmGKSgEZim";
		}

        // UAT use OWS to audit
        if(dbenv.equals(GlobalProperties.ENV.UAT)) {
			LogUtils.info("**Account Audit via OWS**");

            if (driver ==null){
                driver = getDriverNew();
            }

            if (myfactor == null){
                myfactor = getFactorNew();
            }

            String ows_url = UATServerEnv.getOWSUrl(GlobalProperties.ENV.UAT.toString());

            String originalHandle = driver.getWindowHandle();
            driver.switchTo().newWindow(WindowType.TAB);
            owsDashboard = myfactor.newInstance(OWSDashboard.class);
            owsLogin = myfactor.newInstance(OWSLogin.class, ows_url);
            owsLogin.login(OWSName,OWSPass);
            boolean result = owsDashboard.auditTradingAccountFlow(userId,"","");
            driver.close();
            driver.switchTo().window(originalHandle);

            return result;
        }else{
            AdminAPIBusiness admin = new AdminAPIBusiness(AdminURL,dbRegulator,user,password,dbenv,dbBrand);
            if(platform.toString().equalsIgnoreCase("MTS"))
            {
                return admin.auditMainAccount(userId, platform).getKey();
            }
            else {
                return admin.auditMainAccount(userId, platform).getKey();
            }
        }
	}

	public void auditMainAccount(CPRegister cp, PLATFORM platform) throws Exception {
		String userId = cp.userdetails.get("User ID");

		if(dbenv.equals(ENV.UAT)) {
			// Account Audit via OWS
			if (!auditAccountViaOWS(userId)) {
				Assert.fail("Account audit via OWS failed.");
			}
		}else{
			// Account Audit via Admin API
			Map.Entry<Boolean, String> resp = auditAccountViaAdmin(userId, platform);

			if (!resp.getKey()) {
				Assert.fail("Account audit via Admin API failed. Reason: " + resp.getValue());
			}
		}
	}

	public Map.Entry<Boolean, String> auditAccountViaAdmin(String userId, PLATFORM platform) throws Exception {
		// Account Audit via Admin API
		String user = data[0][7].toString();
		String password = data[0][8].toString();

		if(dbenv.equals(ENV.PROD)) {
			user =  "Test CRM";
			password = "Hc8P4RxuKMMmGKSgEZim";
		}

		// Audit Account
		AdminAPIBusiness admin = new AdminAPIBusiness(AdminURL, dbRegulator, user, password, dbenv, dbBrand);
		return admin.auditMainAccount(userId, platform);
	}

	public boolean auditAccountViaOWS(String userId) {
		// Account Audit via OWS
		String OWSName = data[0][9].toString();
		String OWSPass = data[0][10].toString();

		LogUtils.info("**Account Audit via OWS**");

		if (driver ==null){
			driver = getDriverNew();
		}

		if (myfactor == null){
			myfactor = getFactorNew();
		}

		// Open OWS in new tab
		String ows_url = UATServerEnv.getOWSUrl(GlobalProperties.ENV.UAT.toString());
		String originalHandle = driver.getWindowHandle();
		driver.switchTo().newWindow(WindowType.TAB);
		owsDashboard = myfactor.newInstance(OWSDashboard.class);
		owsLogin = myfactor.newInstance(OWSLogin.class, ows_url);

		// OWS Login
		owsLogin.login(OWSName,OWSPass);

		// Audit Account
		boolean result = owsDashboard.auditTradingAccountFlow(userId,"","");

		// Close OWS Tab
		driver.close();
		driver.switchTo().window(originalHandle);

		return result;
	}

    public boolean auditMainAccountAndPOI(String userId, PLATFORM platform) throws InterruptedException, Exception {
        String user = data[0][7].toString();
        String password = data[0][8].toString();
		String OWSName = data[0][9].toString();
		String OWSPass = data[0][10].toString();

        if(dbenv.equals(ENV.PROD)) {
            user =  "Test CRM";
            password = "Hc8P4RxuKMMmGKSgEZim";
        }

        // UAT use OWS to audit
        if(dbenv.equals(ENV.UAT)) {
            if (driver ==null){
                driver = getDriverNew();
            }

            if (myfactor == null){
                myfactor = getFactorNew();
            }

            String ows_url = UATServerEnv.getOWSUrl(GlobalProperties.ENV.UAT.toString());

            String originalHandle = driver.getWindowHandle();
            driver.switchTo().newWindow(WindowType.TAB);
            owsDashboard = myfactor.newInstance(OWSDashboard.class);
            owsLogin = myfactor.newInstance(OWSLogin.class, ows_url);
            owsLogin.login(OWSName,OWSPass);
            boolean accResult = owsDashboard.auditTradingAccountFlow(userId,"","");
            boolean poiResult = owsDashboard.auditPOI(userId,"","");
            driver.close();
            driver.switchTo().window(originalHandle);

            Boolean result = true;
            if(accResult == false || poiResult == false){
                result = false;
            }

            return result;
        }else{
            Boolean auditResult;
            AdminAPIBusiness admin = new AdminAPIBusiness(AdminURL, dbRegulator, user, password, dbenv, dbBrand);
            if(platform.toString().equalsIgnoreCase("MTS"))
            {
                auditResult = admin.auditMainAccount(userId, platform).getKey();
            }
            else
            {
                auditResult = admin.auditMainAccount(userId, platform).getKey();
            }
            if(auditResult!=false) {
                //Admin POI
                String originalHandle = driver.getWindowHandle();
                driver.switchTo().newWindow(WindowType.TAB);
                BaseTestCaseNew.UserConfig config = getConfigNew();
                AdminLogin apLogin = myfactor.newInstance(AdminLogin.class,AdminURL);
                apLogin.login(config.AdminName, config.AdminPass);

                adminMenu = myfactor.newInstance(AdminMenu.class);
                adminIDPOAAudit = myfactor.newInstance(AdminIDPOAAudit.class);
                adminMenu.changeRegulator(Regulator);
				adminMenu.goToMenu(AdminMenuName.ACCOUNT_AUDIT);
                adminIDPOAAudit.clientPoiAudit(email);
                driver.close();
                driver.switchTo().window(originalHandle);
            }
            return auditResult;
        }
    }

	public boolean checkAutoAuditMainAccount(String userId) throws Exception {
		String user = data[0][7].toString();
		String password = data[0][8].toString();
		if(dbenv.equals(ENV.PROD)) {
			user =  "Test CRM";
			password = "Hc8P4RxuKMMmGKSgEZim";
		}

		AdminAPIBusiness admin = new AdminAPIBusiness(AdminURL, dbRegulator, user, password, dbenv, dbBrand);
		return admin.checkAutoAuditMainAccount(userId);
	}

	public void setCode(CPRegister cp,EmailDB emailDB)
	{
		try {
			cp.clickCodeBtn();
			code = getCode(emailDB);
			cp.sendCode(code);
		}
		catch(Exception e)
		{
			LogUtils.info("no need to enter code");
		}
	}

	public String getCode(EmailDB instance)
	{
		JSONObject obj = null;
		Map<String, GlobalProperties.REGULATOR> regulatorMap = Map.of(
				"vfsc", GlobalProperties.REGULATOR.VFSC,
				"vfsc2", GlobalProperties.REGULATOR.VFSC2,
				"svg", GlobalProperties.REGULATOR.SVG,
				"fsa", GlobalProperties.REGULATOR.FSA
		);

		GlobalProperties.REGULATOR regulator = regulatorMap.getOrDefault(Regulator.toLowerCase(), null);

		try {
			if (regulator != null) {
				obj = instance.getCodeRecord(dbenv, dbBrand, regulator, email);
			} else {
				obj = instance.getCodeRecord(dbenv, dbBrand, dbRegulator, email);
			}
		} catch (Exception e) {
			LogUtils.info("An error occurred when retrieve data from db. Error Msg: " + e.getMessage());
		}

		LogUtils.info(obj.getJSONObject("vars").getString("CODE")+ ", \n"+ obj.toJSONString());
		String code = obj.getJSONObject("vars").getString("CODE");
		return code;
	}

	
	//@Parameters(value= {"IBCode","RAFCode","Country","Platform"})
	//public void registerwithCode(String IBCode, String RAFCode, String country, PLATFORM platform) {
	@Test(priority = 0)
	@Parameters(value= {"Country","Platform"})
	public void registerwithCode(String country, PLATFORM platform) throws Exception {
		//registerNew(IBCode, RAFCode, platform, country, true, GlobalProperties.ACCOUNTTYPE.valueOf("STANDARD_STP"), GlobalProperties.CURRENCY.USD);
		if(GlobalMethods.getBrand().equalsIgnoreCase("star") || GlobalMethods.getBrand().equalsIgnoreCase("um") || GlobalMethods.getBrand().equalsIgnoreCase("mo")|| GlobalMethods.getBrand().equalsIgnoreCase("vjp"))
		{
			registerNew("","", "", platform,country,false, GlobalProperties.ACCOUNTTYPE.valueOf("STANDARD_STP"),GlobalProperties.CURRENCY.USD, true);
		}
		else 
		{
			registerNew("", "", "", platform, country, true, GlobalProperties.ACCOUNTTYPE.valueOf("STANDARD_STP"), GlobalProperties.CURRENCY.USD, true);
		}
	}
}

package newcrm.testcases.cptestcases.account;

import newcrm.business.businessbase.CPLiveAccounts;
import newcrm.business.businessbase.CPLogin;
import newcrm.business.businessbase.CPMenu;
import newcrm.business.businessbase.CPOpenAdditionalAccount;
import newcrm.factor.Factor;
import newcrm.global.GlobalProperties.CPMenuName;
import newcrm.global.GlobalProperties.ACCOUNTTYPE;
import newcrm.global.GlobalProperties.CURRENCY;
import newcrm.global.GlobalProperties.PLATFORM;
import newcrm.testcases.BaseTestCaseNew;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import utils.LogUtils;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

public class AdditionalAccTestCases extends BaseTestCaseNew {

    public Object data[][];
    private Factor myfactor;
    private CPLogin login;
    private WebDriver driver;

    private CPMenu menu;
    private CPLiveAccounts liveAccounts;
    private CPOpenAdditionalAccount additionalAccount;

    @BeforeMethod(groups = {"CP_Open_Additional_Acc_Config_Check"})
    protected void initMethod(Method method) {
        myfactor = getFactorNew();
        login = getLogin();
        driver = getDriverNew();

        login.goToCpHome();
        menu = myfactor.newInstance(CPMenu.class);
        menu.goToMenu(CPMenuName.CPPORTAL);
        menu.changeLanguage("English");
        menu.goToMenu(CPMenuName.HOME);
    }

    @AfterMethod(groups = {"CP_Open_Additional_Acc_Config_Check"})
    public void tearDown(ITestResult result) {
        // Close previous left open dialog if any
        if (liveAccounts != null) {
            liveAccounts.checkExistsDialog();
        }
    }

    public void openAddAccConfigCheck(PLATFORM platform, @Optional("")String country) throws Exception {
        additionalAccount = myfactor.newInstance(CPOpenAdditionalAccount.class);
        liveAccounts = myfactor.newInstance(CPLiveAccounts.class);

        menu.goToMenu(CPMenuName.ADDACCOUNT);
        additionalAccount.waitLoadingAccConfigContent();

        switch (country.trim().toLowerCase()) {
            case "thailand":
                // Check exists platform
                additionalAccount.checkPlatForm(platform, true);
                // Set platform
                additionalAccount.setPlatForm(platform);
                // Check platform each account type and currency listing based on country
                accTypeAndCurrencyConfigCheck_TH(platform);
                break;
            case "united kingdom":
                boolean bIsCheckVisible = platform != PLATFORM.MTS;
                // Check exists platform
                // this country does not have MTS platform, exclude checking on MTS
                additionalAccount.checkPlatForm(platform, bIsCheckVisible);
                if (bIsCheckVisible) {
                    // Set platform
                    additionalAccount.setPlatForm(platform);
                    // Check platform each account type and currency listing based on country
                    accTypeAndCurrencyConfigCheck_UK(platform);
                }
                break;
            default:
                // Check exists platform
                additionalAccount.checkPlatForm(platform, true);
                // Set platform
                additionalAccount.setPlatForm(platform);
                // Check platform each account type and currency listing based on country
                accTypeAndCurrencyConfigCheck(platform);
                break;
        }


        System.out.println("***Test Open Additional Account Config Check succeed!!********");
    }

    private void accTypeAndCurrencyConfigCheck(PLATFORM platform) {
        // Get expected account type by brand
        List<ACCOUNTTYPE> expectedAccTypeList = switch (dbBrand) {
            case VFX -> switch (platform) {
                case MT4 -> List.of(ACCOUNTTYPE.STANDARD_STP, ACCOUNTTYPE.RAW_ECN, ACCOUNTTYPE.ISLAMIC_STP, ACCOUNTTYPE.ISLAMIC_ECN, ACCOUNTTYPE.PAMM_INVESTOR, ACCOUNTTYPE.PERPETUAL);
                case MT5 -> List.of(ACCOUNTTYPE.HEDGE_STP, ACCOUNTTYPE.HEDGE_ECN, ACCOUNTTYPE.MT5_ISLAMIC_STP, ACCOUNTTYPE.MT5_ISLAMIC_ECN, ACCOUNTTYPE.MT5_PERPETUAL, ACCOUNTTYPE.MT5_TRADING_VIEW, ACCOUNTTYPE.MT5_PAMM);
                case MTS -> List.of(ACCOUNTTYPE.MTS_HEDGE_STP, ACCOUNTTYPE.MTS_HEDGE_ECN, ACCOUNTTYPE.MTS_ISLAMIC_STP, ACCOUNTTYPE.MTS_ISLAMIC_ECN);
            };
            default -> List.of();
        };

        // Verify Account Type Content
        checkAccountTypeContent(expectedAccTypeList, platform);

        for (int i = 0; i < expectedAccTypeList.size(); i++) {
            // Click each account type to get currency list
            ACCOUNTTYPE accType = expectedAccTypeList.get(i);
            additionalAccount.setAccountType(accType);

            // Get expected currency by brand & account type
            List<CURRENCY> expectedCurrencyList = switch (dbBrand) {
                case VFX -> switch (platform) {
                    case MT4 -> switch (accType) {
                        case STANDARD_STP, RAW_ECN, ISLAMIC_STP, ISLAMIC_ECN, PAMM_INVESTOR ->
                                List.of(CURRENCY.USD, CURRENCY.GBP, CURRENCY.CAD, CURRENCY.AUD, CURRENCY.EUR, CURRENCY.SGD, CURRENCY.NZD, CURRENCY.HKD, CURRENCY.JPY, CURRENCY.PLN);
                        case PERPETUAL ->
                                List.of(CURRENCY.USD, CURRENCY.GBP, CURRENCY.CAD, CURRENCY.AUD, CURRENCY.EUR, CURRENCY.SGD, CURRENCY.NZD, CURRENCY.HKD, CURRENCY.USDT);
                        default ->
                                List.of();
                    };
                    case MT5 -> switch (accType) {
                        case HEDGE_STP ->
                                List.of(CURRENCY.USD, CURRENCY.GBP, CURRENCY.CAD, CURRENCY.AUD, CURRENCY.EUR, CURRENCY.SGD, CURRENCY.NZD, CURRENCY.HKD, CURRENCY.JPY, CURRENCY.AED, CURRENCY.BTC, CURRENCY.ETH, CURRENCY.PLN);
                        case HEDGE_ECN, MT5_ISLAMIC_STP, MT5_ISLAMIC_ECN ->
                                List.of(CURRENCY.USD, CURRENCY.GBP, CURRENCY.CAD, CURRENCY.AUD, CURRENCY.EUR, CURRENCY.SGD, CURRENCY.NZD, CURRENCY.HKD, CURRENCY.JPY, CURRENCY.AED, CURRENCY.PLN);
                        case MT5_PERPETUAL ->
                                List.of(CURRENCY.USD, CURRENCY.GBP, CURRENCY.CAD, CURRENCY.AUD, CURRENCY.EUR, CURRENCY.SGD, CURRENCY.NZD, CURRENCY.HKD, CURRENCY.USDT, CURRENCY.AED, CURRENCY.BTC, CURRENCY.ETH);
                        case MT5_TRADING_VIEW ->
                                List.of(CURRENCY.USD, CURRENCY.GBP, CURRENCY.AUD, CURRENCY.EUR, CURRENCY.AED);
                        case MT5_PAMM ->
                                List.of(CURRENCY.USD, CURRENCY.GBP, CURRENCY.EUR, CURRENCY.BTC, CURRENCY.ETH);
                        default ->
                                List.of();
                    };
                    case MTS -> switch (accType) {
                        case MTS_HEDGE_STP, MTS_HEDGE_ECN, MTS_ISLAMIC_STP, MTS_ISLAMIC_ECN ->
                                List.of(CURRENCY.USD, CURRENCY.EUR, CURRENCY.HKD, CURRENCY.JPY);
                        default ->
                                List.of();
                    };
                };
                default -> List.of();
            };

            // Verify Currency Content
            checkCurrencyContent(expectedCurrencyList);

//            for (int j = 0; j < expectedCurrencyList.size(); j++) {
//                // Click on each currency
//                CURRENCY currency = expectedCurrencyList.get(j);
//                additionalAccount.setCurrency(currency);
//            }
        }
    }

    private void accTypeAndCurrencyConfigCheck_TH(PLATFORM platform) {
        // Get expected account type by brand
        List<ACCOUNTTYPE> expectedAccTypeList = switch (dbBrand) {
            case VFX -> switch (platform) {
                case MT4 -> List.of(ACCOUNTTYPE.STANDARD_STP, ACCOUNTTYPE.RAW_ECN, ACCOUNTTYPE.ISLAMIC_STP, ACCOUNTTYPE.ISLAMIC_ECN, ACCOUNTTYPE.PAMM_INVESTOR, ACCOUNTTYPE.PREMIUM_STP, ACCOUNTTYPE.PERPETUAL);
                case MT5 -> List.of(ACCOUNTTYPE.HEDGE_STP, ACCOUNTTYPE.HEDGE_ECN, ACCOUNTTYPE.MT5_ISLAMIC_STP, ACCOUNTTYPE.MT5_ISLAMIC_ECN, ACCOUNTTYPE.MT5_PERPETUAL, ACCOUNTTYPE.MT5_TRADING_VIEW_STP, ACCOUNTTYPE.MT5_STANDARD_STP_CENT, ACCOUNTTYPE.MT5_ISLAMIC_STP_CENT, ACCOUNTTYPE.MT5_RAW_ECN_CENT, ACCOUNTTYPE.MT5_ISLAMIC_ECN_CENT, ACCOUNTTYPE.MT5_PAMM);
                case MTS -> List.of(ACCOUNTTYPE.MTS_HEDGE_STP, ACCOUNTTYPE.MTS_HEDGE_ECN, ACCOUNTTYPE.MTS_ISLAMIC_STP, ACCOUNTTYPE.MTS_ISLAMIC_ECN, ACCOUNTTYPE.MTS_STANDARD_STP_CENT, ACCOUNTTYPE.MTS_ISLAMIC_STP_CENT, ACCOUNTTYPE.MTS_RAW_ECN_CENT, ACCOUNTTYPE.MTS_ISLAMIC_ECN_CENT);
            };
            default -> List.of();
        };

        // Verify Account Type Content
        checkAccountTypeContent(expectedAccTypeList, platform);

        for (int i = 0; i < expectedAccTypeList.size(); i++) {
            // Click each account type to get currency list
            ACCOUNTTYPE accType = expectedAccTypeList.get(i);
            additionalAccount.setAccountType(accType);

            // Get expected currency by brand & account type
            List<CURRENCY> expectedCurrencyList = switch (dbBrand) {
                case VFX -> switch (platform) {
                    case MT4 -> switch (accType) {
                        case STANDARD_STP, RAW_ECN, ISLAMIC_STP, ISLAMIC_ECN, PAMM_INVESTOR ->
                                List.of(CURRENCY.USD, CURRENCY.GBP, CURRENCY.CAD, CURRENCY.AUD, CURRENCY.EUR, CURRENCY.SGD, CURRENCY.NZD, CURRENCY.HKD, CURRENCY.JPY);
                        case PREMIUM_STP ->
                                List.of(CURRENCY.USD, CURRENCY.GBP, CURRENCY.EUR, CURRENCY.HKD, CURRENCY.JPY);
                        case PERPETUAL ->
                                List.of(CURRENCY.USD, CURRENCY.GBP, CURRENCY.CAD, CURRENCY.AUD, CURRENCY.EUR, CURRENCY.SGD, CURRENCY.NZD, CURRENCY.HKD, CURRENCY.USDT);
                        default ->
                                List.of();
                    };
                    case MT5 -> switch (accType) {
                        case HEDGE_STP ->
                                List.of(CURRENCY.USD, CURRENCY.GBP, CURRENCY.CAD, CURRENCY.AUD, CURRENCY.EUR, CURRENCY.SGD, CURRENCY.NZD, CURRENCY.HKD, CURRENCY.JPY, CURRENCY.AED, CURRENCY.BTC, CURRENCY.ETH);
                        case HEDGE_ECN, MT5_ISLAMIC_STP, MT5_ISLAMIC_ECN ->
                                List.of(CURRENCY.USD, CURRENCY.GBP, CURRENCY.CAD, CURRENCY.AUD, CURRENCY.EUR, CURRENCY.SGD, CURRENCY.NZD, CURRENCY.HKD, CURRENCY.JPY, CURRENCY.AED);
                        case MT5_PERPETUAL ->
                                List.of(CURRENCY.USD, CURRENCY.GBP, CURRENCY.CAD, CURRENCY.AUD, CURRENCY.EUR, CURRENCY.SGD, CURRENCY.NZD, CURRENCY.HKD, CURRENCY.USDT, CURRENCY.AED, CURRENCY.BTC, CURRENCY.ETH);
                        case MT5_TRADING_VIEW_STP ->
                                List.of(CURRENCY.USD, CURRENCY.HKD, CURRENCY.AED);
                        case MT5_STANDARD_STP_CENT, MT5_ISLAMIC_STP_CENT, MT5_RAW_ECN_CENT, MT5_ISLAMIC_ECN_CENT ->
                                List.of(CURRENCY.USC);
                        case MT5_PAMM ->
                                List.of(CURRENCY.USD, CURRENCY.GBP, CURRENCY.EUR, CURRENCY.BTC, CURRENCY.ETH);
                        default ->
                                List.of();
                    };
                    case MTS -> switch (accType) {
                        case MTS_HEDGE_STP, MTS_HEDGE_ECN, MTS_ISLAMIC_STP, MTS_ISLAMIC_ECN ->
                                List.of(CURRENCY.USD, CURRENCY.EUR, CURRENCY.HKD, CURRENCY.JPY);
                        case MTS_STANDARD_STP_CENT, MTS_ISLAMIC_STP_CENT, MTS_RAW_ECN_CENT, MTS_ISLAMIC_ECN_CENT ->
                                List.of(CURRENCY.USC);
                        default ->
                                List.of();
                    };
                };
                default -> List.of();
            };

            // Verify Currency Content
            checkCurrencyContent(expectedCurrencyList);
        }
    }

    private void accTypeAndCurrencyConfigCheck_UK(PLATFORM platform) {
        // Get expected account type by brand
        List<ACCOUNTTYPE> expectedAccTypeList = switch (dbBrand) {
            case VFX -> switch (platform) {
                case MT4 -> List.of(ACCOUNTTYPE.STANDARD_STP, ACCOUNTTYPE.RAW_ECN, ACCOUNTTYPE.ISLAMIC_STP, ACCOUNTTYPE.ISLAMIC_ECN, ACCOUNTTYPE.PERPETUAL);
                case MT5 -> List.of(ACCOUNTTYPE.HEDGE_STP, ACCOUNTTYPE.HEDGE_ECN, ACCOUNTTYPE.MT5_ISLAMIC_STP, ACCOUNTTYPE.MT5_ISLAMIC_ECN, ACCOUNTTYPE.MT5_PERPETUAL, ACCOUNTTYPE.MT5_TRADING_VIEW);
                case MTS -> List.of();
            };
            default -> List.of();
        };

        // Verify Account Type Content
        checkAccountTypeContent(expectedAccTypeList, platform);

        for (int i = 0; i < expectedAccTypeList.size(); i++) {
            // Click each account type to get currency list
            ACCOUNTTYPE accType = expectedAccTypeList.get(i);
            additionalAccount.setAccountType(accType);

            // Get expected currency by brand & account type
            List<CURRENCY> expectedCurrencyList = switch (dbBrand) {
                case VFX -> switch (platform) {
                    case MT4 -> switch (accType) {
                        case STANDARD_STP, RAW_ECN, ISLAMIC_STP, ISLAMIC_ECN ->
                                List.of(CURRENCY.USD, CURRENCY.GBP, CURRENCY.CAD, CURRENCY.AUD, CURRENCY.EUR, CURRENCY.SGD, CURRENCY.NZD, CURRENCY.HKD, CURRENCY.JPY, CURRENCY.PLN);
                        case PERPETUAL ->
                                List.of(CURRENCY.USD, CURRENCY.GBP, CURRENCY.CAD, CURRENCY.AUD, CURRENCY.EUR, CURRENCY.SGD, CURRENCY.NZD, CURRENCY.HKD, CURRENCY.USDT);
                        default ->
                                List.of();
                    };
                    case MT5 -> switch (accType) {
                        case HEDGE_STP ->
                                List.of(CURRENCY.USD, CURRENCY.GBP, CURRENCY.CAD, CURRENCY.AUD, CURRENCY.EUR, CURRENCY.SGD, CURRENCY.NZD, CURRENCY.HKD, CURRENCY.JPY, CURRENCY.AED, CURRENCY.BTC, CURRENCY.ETH, CURRENCY.PLN);
                        case HEDGE_ECN, MT5_ISLAMIC_STP, MT5_ISLAMIC_ECN ->
                                List.of(CURRENCY.USD, CURRENCY.GBP, CURRENCY.CAD, CURRENCY.AUD, CURRENCY.EUR, CURRENCY.SGD, CURRENCY.NZD, CURRENCY.HKD, CURRENCY.JPY, CURRENCY.AED, CURRENCY.PLN);
                        case MT5_PERPETUAL ->
                                List.of(CURRENCY.USD, CURRENCY.GBP, CURRENCY.CAD, CURRENCY.AUD, CURRENCY.EUR, CURRENCY.SGD, CURRENCY.NZD, CURRENCY.HKD, CURRENCY.USDT, CURRENCY.AED, CURRENCY.BTC, CURRENCY.ETH);
                        case MT5_TRADING_VIEW ->
                                List.of(CURRENCY.USD, CURRENCY.GBP, CURRENCY.AUD, CURRENCY.EUR, CURRENCY.AED);
                        default ->
                                List.of();
                    };
                    case MTS -> List.of();
                };
                default -> List.of();
            };

            // Verify Currency Content
            checkCurrencyContent(expectedCurrencyList);
        }
    }

    private void checkAccountTypeContent(List<ACCOUNTTYPE> expectedAccTypeList, PLATFORM platform) {
        // Get account type from UI
        List<ACCOUNTTYPE> currAccTypeList = additionalAccount.getAccountTypeList(platform);

        // Check if any expected account type is not found in UI list
        List<ACCOUNTTYPE> missingInCurrent = expectedAccTypeList.stream()
                .filter(e -> !currAccTypeList.contains(e))
                .toList();

        if (!missingInCurrent.isEmpty()) {
            Assert.fail("Account Type is expected but does not found in available listing. Account Type: " + missingInCurrent.stream()
                    .map(ACCOUNTTYPE::name)
                    .collect(Collectors.joining(", ")));
        }

        // Check if UI contains account type which does not found in expected account type list
        List<ACCOUNTTYPE> unexpectedInCurrent = currAccTypeList.stream()
                .filter(c -> !expectedAccTypeList.contains(c))
                .toList();

        if (!unexpectedInCurrent.isEmpty()) {
            Assert.fail("Account Type is not expected but found in available listing. Account Type: " + unexpectedInCurrent.stream()
                    .map(ACCOUNTTYPE::name)
                    .collect(Collectors.joining(", ")));
        }

    }

    private void checkCurrencyContent(List<CURRENCY> expectedCurrencyList) {
        // Get currency from UI
        List<CURRENCY> currCurrencyList = additionalAccount.getCurrencyList();

        // Check if any expected currency is not found in UI list
        List<CURRENCY> missingInCurrent = expectedCurrencyList.stream()
                .filter(e -> !currCurrencyList.contains(e))
                .toList();

        if (!missingInCurrent.isEmpty()) {
            Assert.fail(String.format("Currency is expected but does not found in available listing. Expected Currency: %s, Current Currency: %s",
                    expectedCurrencyList.stream().map(CURRENCY::name).collect(Collectors.joining(", ")),
                    currCurrencyList.stream().map(CURRENCY::name).collect(Collectors.joining(", "))
            ));
        }

        // Check if UI contains currency which does not found in expected currency list
        List<CURRENCY> unexpectedInCurrent = currCurrencyList.stream()
                .filter(c -> !expectedCurrencyList.contains(c))
                .toList();

        if (!unexpectedInCurrent.isEmpty()) {
            Assert.fail(String.format("Currency is not expected but found in available listing. Expected Currency: %s, Current Currency: %s",
                    expectedCurrencyList.stream().map(CURRENCY::name).collect(Collectors.joining(", ")),
                    currCurrencyList.stream().map(CURRENCY::name).collect(Collectors.joining(", "))
            ));
        }

        LogUtils.info("Currency: " + expectedCurrencyList.stream()
                .map(CURRENCY::name)
                .collect(Collectors.joining(", ")));
    }

}

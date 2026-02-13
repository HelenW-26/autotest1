package newcrm.testcases;

import newcrm.utils.UATServerEnv;
import org.testng.annotations.DataProvider;

import newcrm.utils.AlphaServerEnv;

/**
 * 根据测试需要提供不同的测试账号
 *
 * @author FengLiu
 */
public class TestDataProvider {

    @DataProvider(name = "leverageUserProvider")
    public static Object[][] getDataFromDataprovider() {
        String TestEnv = "Prod";
        String pug_cp_url = "https://myaccount.puprime.com";
        String headless = "True";
        String admin_url = "https://admin.puprime.com";
        String debug = "True";
        return new Object[][]{
                {TestEnv, headless, "PUG", "SVG", pug_cp_url, "autotestnabmkocl@testcrmautomation.com", "123Qwe", admin_url, "feng", "123Qwe", debug}
                ,
                {TestEnv, headless, "VT", "SVG", "https://myaccount.vtmarkets.com", "autotestreudbqsq@testcrmautomation.com", "123Qwe", "https://admin.vtmarkets.com", "feng", "123Qwe", debug}
                ,
                {TestEnv, headless, "VFX", "VFSC2", "https://secure.vantagemarkets.com", "autotestuthpiags@testcrmautomation.com", "123Qwe", "https://admin.vantagemarkets.com", "feng", "123Qwe", debug}
                ,
                {TestEnv, headless, "UM", "SVG", "https://myaccount.ultimamarkets.com", "autotestitabj@testautomation.com", "123Qwe", "https://admin.ultimarkets.com", "CRM Test", "BDAVJECnh4hovtZLV33N", debug}

        };
    }

    @DataProvider(name = "alphaData")
    public static Object[][] getalphaUsers() {
        String TestEnv = "alpha";
        String pug_cp_url = "https://myaccount.puprime.com";
        String headless = "false";
        String admin_url = "https://admin.puprime.com";
        String debug = "True";
        return new Object[][]{
                /*{TestEnv,headless,"PUG","SVG","https://secure-mars-ex.crm-alpha.com","ndb3@test.com","123Qwe",admin_url,"feng","123Qwe",debug}
                ,
                {TestEnv,headless,"VT","SVG","https://secure-taurus-ex.crm-alpha.com","wb10@test.com","123Qwe","https://admin.vtmarkets.com","feng","123Qwe",debug}
                ,*/
                {TestEnv, headless, "VFX", "VFSC2", "https://secure-thor.crm-alpha.com", "usdt10@test.com", "123Qwe", "https://admin.vantagemarkets.com", "feng", "123Qwe", debug}
        };
    }

    @DataProvider(name = "addAccountUserProvider")
    public static Object[][] getAddAccountUserDataprovider() {
        String TestEnv = "Prod";
        String pug_cp_url = "https://myaccount.puprime.com";
        String headless = "True";
        String admin_url = "https://admin.puprime.com";
        String debug = "True";
        return new Object[][]{
                {TestEnv, headless, "PUG", "SVG", pug_cp_url, "autotestgcwksiuj@testcrmautomation.com", "123Qwe", admin_url, "CRM Test", "BDAVJECnh4hovtZLV33N", debug}
                ,
                {TestEnv, headless, "VT", "SVG", "https://myaccount.vtmarkets.com", "autotesteisfkcgf@testcrmautomation.com", "123Qwe", "https://admin.vtmarkets.com", "CRM Test", "BDAVJECnh4hovtZLV33N", debug}
                ,
                {TestEnv, headless, "VFX", "VFSC", "https://secure.vantagemarkets.com", "autotestmxbik@mailinator.com", "123Qwe", "https://admin.vantagemarkets.com", "CRM Test", "BDAVJECnh4hovtZLV33N", debug}
                ,
                {TestEnv, headless, "UM", "SVG", "https://myaccount.ultimamarkets.com", "autotestitabj@testautomation.com", "123Qwe", "https://admin.ultimarkets.com", "CRM Test", "BDAVJECnh4hovtZLV33N", debug}

        };
    }


    /**
     * 入金限额弹框测试账号
     *
     * @param brand
     * @param server
     * @return regulator，username，password，cp_url,admin_url,open_api,ib_url
     */
    public static Object[][] getAlphaPopUsersData(String brand, String server) {
        String cp_url = AlphaServerEnv.getCpUrl(server);
        String admin_url = AlphaServerEnv.getAdminUrl(server);
        String open_api = AlphaServerEnv.getOpenApiUrl(server);
        String ib_url = AlphaServerEnv.getIBUrl(server);
        brand = brand.trim();
        if ("vt".equalsIgnoreCase(brand)) {
            //return new Object[][] {{"SVG","testcrm_donttouchperformancefee@test.com","123Qwe",cp_url,admin_url,open_api,ib_url}};
            return new Object[][]{{"SVG", "testcrm_wdunionusertest@testcrm.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe", "testcrm_ibwdauto1@testcrm.com", "123Qwe@@"}};

        }


        if ("um".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrm_wdunionuser@testcrm.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe", "testcrm_ibwdauto1@testcrm.com", "123Qwe@@"}};
        }
        if ("mo".equalsIgnoreCase(brand)) {
            return new Object[][]{{"VFSC", "testcrm_donttouch_cpschinaclient@test.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe", "testcrm_ibwdauto1@testcrm.com", "123Qwe@@"}};
        }

        return null;
    }

    /**
     * 出入金进阶审核弹窗账号
     *
     * @param brand
     * @param server
     * @return regulator，username，password，cp_url,admin_url,open_api,ib_url
     */
    public static Object[][] getAlpha2FAPopUpUsersData(String brand, String server) {
        String cp_url = AlphaServerEnv.getCpUrl(server);
        String admin_url = AlphaServerEnv.getAdminUrl(server);
        String open_api = AlphaServerEnv.getOpenApiUrl(server);
        String ib_url = AlphaServerEnv.getIBUrl(server);
        brand = brand.trim();
        if ("vt".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrmidpofnotapporve@mailinator.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe", "testcrmvtidnotapprove@mailinator.com", "123Qwe@@"}};

        }
        if ("star".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrmstidpofnotapprove@mailinator.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe", "testcrmstpofnotapprove@mailinator.com", "123Qwe@@"}};
//            return new Object[][]{{"SVG", "testcrmstpofnotapprove@mailinator.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe", "testcrmstpofnotapprove@mailinator.com", "123Qwe@@"}};

        }

        if ("um".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrmumidpofnotapprove@mailinator.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe", "testcrmumpofnotapprove@mailinator.com", "123Qwe@@"}};
        }
        if ("mo".equalsIgnoreCase(brand)) {
            return new Object[][]{{"VFSC", "testcrmmoidpoanotapprove01@mailinator.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe", "testcrmmopoanotapprove@mailinator.com", "123Qwe@@"}};
        }
        if ("au".equalsIgnoreCase(brand)||"vfx".equalsIgnoreCase(brand)) {
            return new Object[][]{{"VFSC2", "testcrmauidpofnotapprove@mailinator.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe", "testcrmaupofnotapprove@mailinator.com", "123Qwe@@"}};
        }
        if ("pug".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrmpuidpoanotapprove@mailinator.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe", "testcrmpupoanotaprove@mailinator.com", "123Qwe@@"}};
        }
        if ("vjp".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrmvjpidpoanotapprove@mailinator.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe", "testcrmvjppoanotapprove@mailinator.com", "123Qwe@@"}};
        }

        return null;
    }

    /**
     * unionPay账号
     *
     * @param brand
     * @param server
     * @return regulator，username，password，cp_url,admin_url,open_api,ib_url
     */
    public static Object[][] getAlphaUnionPayUsersData(String brand, String server) {
        String cp_url = AlphaServerEnv.getCpUrl(server);
        String admin_url = AlphaServerEnv.getAdminUrl(server);
        String open_api = AlphaServerEnv.getOpenApiUrl(server);
        String ib_url = AlphaServerEnv.getIBUrl(server);
        brand = brand.trim();
        if ("vt".equalsIgnoreCase(brand)) {
            //return new Object[][] {{"SVG","testcrm_donttouchperformancefee@test.com","123Qwe",cp_url,admin_url,open_api,ib_url}};
            return new Object[][]{{"SVG", "testcrm_wdunionusertest@testcrm.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe", "testcrm_ibwdauto1@testcrm.com", "123Qwe@@"}};

        }
        if ("star".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "australiabttestcrm@mailinator.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe", "testcrm_ibwdauto1@testcrm.com", "123Qwe@@"}};
        }

        if ("um".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrm_wdunionuser@testcrm.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe", "testcrm_ibwdauto1@testcrm.com", "123Qwe@@"}};
        }
        if ("mo".equalsIgnoreCase(brand)) {
            return new Object[][]{{"VFSC", "testcrm_donttouch_cpschinaclient@test.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe", "testcrm_ibwdauto1@testcrm.com", "123Qwe@@"}};
        }

        return null;
    }
    /**
     * 返回alpha regression test的测试账号
     *
     * @param brand
     * @param server
     * @return regulator, username, password, p_url, admin_url, open_api, ib_url, admin username, admin password, ib username, ib password
     */
    public static Object[][]    getAlphaRegUsersData(String brand, String server) {
        String cp_url = AlphaServerEnv.getCpUrl(server);
        String admin_url = AlphaServerEnv.getAdminUrl(server);
        String open_api = AlphaServerEnv.getOpenApiUrl(server);
        String ib_url = AlphaServerEnv.getIBUrl(server);
        brand = brand.trim();
        if ("vt".equalsIgnoreCase(brand)) {
            //return new Object[][] {{"SVG","testcrm_donttouchperformancefee@test.com","123Qwe",cp_url,admin_url,open_api,ib_url}};
            return new Object[][]{{"SVG", "wb10@test.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe", "testcrm_ibwdauto1@testcrm.com", "123Qwe@@"}};

        }

        if ("pug".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "ndb3@test.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe", "testcrmibwhitni01@testcrm.com", "123Qwe@@"}};
        }

        if ("pugpt".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "ndb3@test.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe"}};
        }

        if ("vfx".equalsIgnoreCase(brand) || "au".equalsIgnoreCase(brand)) {
            return new Object[][]{{"VFSC2", "autotestcrmutdb@mailinator.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe", "testcrm_ibwdauto@testcrm.com", "123Qwe@@"}};
            // Replace with MT4 only user due to MT5 meti issue
            //return new Object[][] {{"VFSC2","testcrmwmlit@test.com","123Qwe",cp_url,admin_url,open_api,ib_url,"cmatest","123Qwe"}};

        }

        if ("um".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "autotesthzukzgzl@testcrmautomation.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe", "testcrm_ibwdauto1@testcrm.com", "123Qwe@@"}};
//            return new Object[][]{{"SVG", "testumtransferwithcredit@testcrm.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe", "testcrm_ibwdauto1@testcrm.com", "123Qwe@@"}};
        }
        if ("mo".equalsIgnoreCase(brand)) {
            return new Object[][]{{"VFSC", "automationmost@testcrmautomation.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe", "testcrm_ibwdauto1@testcrm.com", "123Qwe@@"}};
        }
        if ("star".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "automationstarst@mailinator.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe", "testcrm_ibwdauto2@testcrm.com", "123Qwe@@"}};
        }
        if ("vjp".equalsIgnoreCase(brand)) {
//            return new Object[][]{{"SVG", "vjptestautomation@mailinator.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "1234Qwer!", "testcrm_ibwdauto@testcrm.com", "123Qwe@@"}};
            return new Object[][]{{"SVG", "vjptestautomation@mailinator.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "1234Qwer!", "testcrmwkxJRyTRh@testcrm.com", "1234Qwer@"}};

        }
        if ("kcm".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "autotestrzrfgecy@testcrmautomation.com", "123Qwe", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe"}};
        }
        return null;
    }

    /**
     * 返回alpha registration regression test的测试账号
     *
     * @param brand
     * @param server
     * @return regulator, username, password, cp_url, admin_url, open_api, ib_url, admin username, admin password, ows username, ows password
     */
    public static Object[][] getAlphaRegisterUsersData(String brand, String server) {
        String cp_url = AlphaServerEnv.getCpUrl(server);
        String admin_url = AlphaServerEnv.getAdminUrl(server);
        String open_api = AlphaServerEnv.getOpenApiUrl(server);
        String ib_url = AlphaServerEnv.getIBUrl(server);
        brand = brand.trim();

        if ("vt".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "wb10@test.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe", "cmatest", "123Qwe"}};
        }
        if ("pug".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "ndb3@test.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe", "cmatest", "123Qwe"}};
        }
        if ("vfx".equalsIgnoreCase(brand) || "au".equalsIgnoreCase(brand)) {
            return new Object[][]{{"VFSC2", "autotestcrmutdb@mailinator.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe", "cmatest", "123Qwe"}};
        }
        if ("um".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "autotesthzukzgzl@testcrmautomation.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe", "cmatest", "123Qwe"}};
        }
        if ("mo".equalsIgnoreCase(brand)) {
            return new Object[][]{{"VFSC", "automationmost@testcrmautomation.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe", "cmatest", "123Qwe"}};
        }
        if ("star".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "automationstarst@mailinator.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe", "cmatest", "123Qwe"}};
        }
        if ("vjp".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "vjptestautomation@mailinator.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "1234Qwer!", "cmatest", "123Qwe"}};
        }

        return null;
    }


    public static Object[][] getAlphaWithdrawLimitUsersData(String brand, String server) {
        String cp_url = AlphaServerEnv.getCpUrl(server);
        String admin_url = AlphaServerEnv.getAdminUrl(server);
        String open_api = AlphaServerEnv.getOpenApiUrl(server);
        String ib_url = AlphaServerEnv.getIBUrl(server);
        brand = brand.trim();
        if ("vt".equalsIgnoreCase(brand)) {
            //return new Object[][] {{"SVG","testcrm_donttouchperformancefee@test.com","123Qwe",cp_url,admin_url,open_api,ib_url}};
//            return new Object[][]{{"SVG", "wb10@test.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe", "testcrm_ibwdauto1@testcrm.com", "123Qwe@@"}};
            return new Object[][]{{"SVG", "testvtwithdrawal@testcrm.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe", "testcrm_ibwdauto1@testcrm.com", "123Qwe@@"}};
        }

        if ("pug".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testpuwithdrawal@testcrm.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe", "testcrm_ibwdauto@testcrm.com", "123Qwe@@"}};
        }

        if ("pugpt".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "ndb3@test.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe"}};
        }

        if ("vfx".equalsIgnoreCase(brand) || "au".equalsIgnoreCase(brand)) {
//            return new Object[][]{{"VFSC2", "autotestcrmutdb@mailinator.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe", "testcrm_ibwdauto@testcrm.com", "123Qwe@@"}};
            return new Object[][]{{"VFSC2","testauwithdrawallimit@testcrm.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "whitni", "123Qwe!!", "testcrm_ibwdauto@testcrm.com", "123Qwe@@"}};
            // Replace with MT4 only user due to MT5 meti issue
            //return new Object[][] {{"VFSC2","testcrmwmlit@test.com","123Qwe",cp_url,admin_url,open_api,ib_url,"cmatest","123Qwe"}};

        }

        if ("um".equalsIgnoreCase(brand)) {
//            return new Object[][]{{"SVG", "autotesthzukzgzl@testcrmautomation.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe", "testcrm_ibwdauto1@testcrm.com", "123Qwe@@"}};
            return new Object[][]{{"SVG", "testumtransferwithcredit@testcrm.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe", "testcrm_ibwdauto1@testcrm.com", "123Qwe@@"}};
        }
        if ("mo".equalsIgnoreCase(brand)) {
            return new Object[][]{{"VFSC", "testmowithdrawal@testcrm.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe", "testcrm_ibwdauto1@testcrm.com", "123Qwe@@"}};
        }
        if ("star".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "teststtransferwithcredit@testcrm.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe", "testcrm_ibwdauto1@testcrm.com", "123Qwe@@"}};
        }
        if ("vjp".equalsIgnoreCase(brand)) {
//            return new Object[][]{{"SVG", "vjptestautomation@mailinator.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "1234Qwer!", "testcrm_ibwdauto@testcrm.com", "123Qwe@@"}};
            return new Object[][]{{"SVG", "testvjpwithdrawal@testcrm.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "1234Qwer!", "testcrm_ibwdauto@testcrm.com", "123Qwe@@"}};
        }
        if ("kcm".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "autotestrzrfgecy@testcrmautomation.com", "123Qwe", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe"}};
        }
        return null;
    }

    /**
     * 返回alpha promotion regression test的测试账号
     *
     * @param brand
     * @param server
     * @return regulator，username，password，cp_url,admin_url,open_api,ib_url
     */
    public static Object[][] getAlphaPromotionUsersData(String brand, String server) {
        String cp_url = AlphaServerEnv.getCpUrl(server);
        String admin_url = AlphaServerEnv.getAdminUrl(server);
        String open_api = AlphaServerEnv.getOpenApiUrl(server);
        String ib_url = AlphaServerEnv.getIBUrl(server);
        brand = brand.trim();
        if ("vt".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "autotestbqtozkoe@testcrmautomation.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe"}};

        }

        if ("pug".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "ndb3@test.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe"}};
        }

        if ("pugpt".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "ndb3@test.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe"}};
        }

        if ("vfx".equalsIgnoreCase(brand) || "au".equalsIgnoreCase(brand)) {
            return new Object[][]{{"VFSC2", "autotestfngwtxnb@testcrmautomation.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe"}};

        }

        if ("um".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "autotesthzukzgzl@testcrmautomation.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe"}};
        }
        if ("mo".equalsIgnoreCase(brand)) {
            return new Object[][]{{"VFSC", "testmoneta@helentest.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe"}};
        }
        if ("star".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "hwpromotion@mailinator.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe"}};
        }
        if ("vjp".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "vjptestautomation@mailinator.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "1234Qwer!"}};
        }
        return null;
    }

    /**
     * 返回alpha App regression test的测试账号
     *
     * @param brand
     * @param server
     * @return regulator, app_url, user_id, accountNumber, user_name, pwd
     */
    public static Object[][] getAlphaAPPRegUsersData(String brand, String server) {
        String app_url = AlphaServerEnv.getAPPServiceUrl(server);
        brand = brand.trim();
        if ("vt".equalsIgnoreCase(brand)) {
            //return new Object[][] {{"SVG",app_url,"10011817","804395273","MT4"}};
            return new Object[][]{{"SVG", app_url, "10011498", "14840038", "MT4", "1538@hotmail.com", "123Qwe@@"}};
        }

        if ("pug".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", app_url, "10004823", "14200026", "MT4", "autotestfnpcgbsw@testcrmautomation.com", "123Qwe@@"}};
        }

        if ("vfx".equalsIgnoreCase(brand) || "au".equalsIgnoreCase(brand)) {
            return new Object[][]{{"VFSC2", app_url, "10008363", "8350062", "MT4", "testpersentmandy@testpersentmandy.com", "123Qwe@@"}};
        }

        if ("um".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", app_url, "836182", "14760051", "MT4", "autotestunainbxg@testcrmautomation.com", "123Qwe@@"}};
        }
        if ("mo".equalsIgnoreCase(brand)) {
            return new Object[][]{{"VFSC", app_url, "709", "14750033", "MT4", "bbb@test.com", "123Qwe@@"}};
        }
        if ("star".equalsIgnoreCase(brand)) {
//            return new Object[][]{{"SVG", app_url, "10061608", "9650058", "MT4", "cdtfyqfe590@appapiautotest.com", "123Qwe@@"}};
            return new Object[][]{{"SVG", app_url, "10087055", "8181002", "MT5", "testcrmapitest1027@mailinator.com", "123Qwe@@"}};

        }
        if ("vjp".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", app_url, "2666424", "14770020", "MT4", "vjpapptestautomation@mailinator.com", "123Qwe@@"}};
        }
        if ("kcm".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", app_url, "", "", ""}};
        }
        return null;
    }

    /**
     * 返回alpha App add additional regression test的测试账号
     *
     * @param brand
     * @param server
     * @return regulator, app_url, user_id, accountNumber, user_name, pwd
     */
    public static Object[][] getAlphaAPPAddAccUsersData(String brand, String server) {
        String app_url = AlphaServerEnv.getAPPServiceUrl(server);
        brand = brand.trim();
        if ("vt".equalsIgnoreCase(brand)) {
            //return new Object[][] {{"SVG",app_url,"10011817","804395273","MT4"}};
            return new Object[][]{{"SVG", app_url, "10066542", "14840039", "MT4", "auto0718065116848@nqmo.com", "1234Qwer!"}};
        }

        if ("pug".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", app_url, "80222854", "14200027", "MT4", "auto0718070947783@nqmo.com", "1234Qwer!"}};
        }

        if ("vfx".equalsIgnoreCase(brand) || "au".equalsIgnoreCase(brand)) {
            return new Object[][]{{"VFSC2", app_url, "10152630", "8350066", "MT4", "testcrmappservicevfsc@mailinator.com", "1234Qwer!"}};
        }

        if ("um".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", app_url, "842178", "14760053", "MT4", "auto0718080405947@nqmo.com", "1234Qwer!"}};
        }
        if ("mo".equalsIgnoreCase(brand)) {
            return new Object[][]{{"VFSC", app_url, "10109", "14750020", "MT4", "auto0718080837461@nqmo.com", "1234Qwer!"}};
        }
        if ("star".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", app_url, "10083625", "9650036", "MT4", "auto0718081919762@nqmo.com", "1234Qwer!"}};
        }
        if ("vjp".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", app_url, "2673550", "14770011", "MT4", "auto0718082801757@nqmo.com", "1234Qwer!"}};
        }
        if ("kcm".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", app_url, "", "", ""}};
        }
        return null;
    }

    /**
     * @param brand
     * @param server
     * @return regulator, username, password, p_url, admin_url, open_api, ib_url, admin username, admin password
     */
    public static Object[][] getAlphaAddAccUsersData(String brand, String server) {
        String cp_url = AlphaServerEnv.getCpUrl(server);
        String admin_url = AlphaServerEnv.getAdminUrl(server);
        String open_api = AlphaServerEnv.getOpenApiUrl(server);
        String ib_url = AlphaServerEnv.getIBUrl(server);
        String app_url = AlphaServerEnv.getAPPServiceUrl(server);
        if ("vt".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrm_autoaddacc01@testcrm.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe"}};
        }

        if ("pug".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "ndb4@test.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe"}};
        }

        if ("vfx".equalsIgnoreCase(brand) || "au".equalsIgnoreCase(brand)) {
            return new Object[][]{{"VFSC2", "testcrmautovfxstaddi@testcrmautomation.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe"}};
            // Replace with MT4 only user
            //return new Object[][] {{"VFSC2","testcrmwmlit@test.com","123Qwe",cp_url,admin_url,open_api,ib_url}};
        }

        if ("um".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrm_autoaddacc01@testcrm.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe"}};
        }

        if ("kcm".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "autotestrzrfgecy@testcrmautomation.com", "123Qwe", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe"}};
        }

        if ("mo".equalsIgnoreCase(brand)) {
            return new Object[][]{{"VFSC", "testcrm_autoaddacc01@testcrm.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe"}};
        }

        if ("star".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "automation_addi@test.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe"}};
        }
        if ("vjp".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrm_vjpadditionalacc_donttouch@test.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "1234Qwer!"}};
        }
        return null;
    }

    /**
     * @param brand
     * @param server
     * @return regulator，username，password，cp_url,admin_url,open_api,ib_url
     */
    public static Object[][] getAlphaCopyTradingAddAccUsersData(String brand, String server) {
        String cp_url = AlphaServerEnv.getCpUrl(server);
        String admin_url = AlphaServerEnv.getAdminUrl(server);
        String open_api = AlphaServerEnv.getOpenApiUrl(server);
        String ib_url = AlphaServerEnv.getIBUrl(server);

        if ("pug".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrmautomationaddacc@mailinator.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url,"cmatest","123Qwe"}};
        }
        if ("vfx".equalsIgnoreCase(brand) || "au".equalsIgnoreCase(brand)) {
            return new Object[][]{{"VFSC", "testcrmcopytradingAddAcc@mailinator.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url,"cmatest", "123Qwe"}};
        }
        if ("vt".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrmcopytradingAddAcc@mailinator.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url,"helen", "123Qwe"}};
        }
        if ("vjp".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrmcopytradingAddAcc@mailinator.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url,"cmatest", "1234Qwer!"}};
        }
        if ("um".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrmcopytradingAddAcc@mailinator.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url,"cmatest", "123Qwe"}};
        }

        return null;
    }
    /**
     * @param brand
     * @param server
     * @return regulator，username，password，cp_url,admin_url,open_api,ib_url
     */
    public static Object[][] getAlphaCopyTradingTransferUsersData(String brand, String server) {
        String cp_url = AlphaServerEnv.getCpUrl(server);
        String admin_url = AlphaServerEnv.getAdminUrl(server);
        String open_api = AlphaServerEnv.getOpenApiUrl(server);
        String ib_url = AlphaServerEnv.getIBUrl(server);

        if ("pug".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testbitcopytradingtrasfer@mailinator.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url,"cmatest", "123Qwe"}};
        }

        if ("vfx".equalsIgnoreCase(brand) || "au".equalsIgnoreCase(brand)) {
            return new Object[][]{{"VFSC", "testcrmcopytradingaddaccb@mailinator.com", "1234Qwer!", cp_url, admin_url, open_api, ib_url,"cmatest", "123Qwe"}};
        }
        if ("vt".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testbitcopytradingtrasfer@mailinator.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url,"cmatest", "123Qwe"}};
        }
        if ("vjp".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testbitcopytradingtrasfer@mailinator.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url,"cmatest", "123Qwe"}};
        }

        return null;
    }

    /**
     * @param brand
     * @param server
     * @return regulator，username，password，signal provider account,cp_url,admin_url,open_api,ib_url,Signal Provider name, copier account
     */
    public static Object[][] getAlphaCopyTradingPaymentUsersData(String brand, String server) {
        String cp_url = AlphaServerEnv.getCpUrl(server);
        String admin_url = AlphaServerEnv.getAdminUrl(server);
        String open_api = AlphaServerEnv.getOpenApiUrl(server);
        String ib_url = AlphaServerEnv.getIBUrl(server);

        if ("pug".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testautomationpayment@mailinator.com", "123Qwe@@","910014079", cp_url, admin_url, open_api, ib_url,"cmatest", "123Qwe","testapp testautomationeursignalp","910014077"}};
        }

        if ("vfx".equalsIgnoreCase(brand) || "au".equalsIgnoreCase(brand)) {
            return new Object[][]{{"VFSC2", "testcrmcopytradingpaymentc@mailinator.com", "123Qwe@@","1960285", cp_url, admin_url, open_api, ib_url,"cmatest", "123Qwe","testapp testcrmautomationsignalprovider","920023804"}};
        }
        if ("vt".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrmcopytradingpaymentc@mailinator.com", "123Qwe@@","930005925", cp_url, admin_url, open_api, ib_url,"cmatest", "123Qwe","testapp testbitcopytradinghelen","16660025"}};
        }
        if ("vjp".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrmcopytradingpaymentc@mailinator.com", "123Qwe@@","84006087", cp_url, admin_url, open_api, ib_url,"cmatest", "123Qwe","testapp testcrmautomationsigp","84006086"}};
        }
        if ("mo".equalsIgnoreCase(brand)) {
            return new Object[][]{{"VFSC", "testcrmcopytradingpayment@mailinator.com", "123Qwe@@","85003500", cp_url, admin_url, open_api, ib_url,"cmatest", "123Qwe","testapp testcrmautomationsigp","7851518"}};
        }
        if ("um".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrmcopytradingpayment@mailinator.com", "123Qwe@@","22240070", cp_url, admin_url, open_api, ib_url,"cmatest", "123Qwe","testapp testcrmautomationsigp","11803620"}};
        }

        return null;
    }
    /**
     * 返回copy trading signal provider test的测试账号
     *
     * @param brand
     * @param server
     * @return regulator，copy trading user，password，copy trading account, strategy account, cp_url
     */
    public static Object[][] getAlphaCopyTradingSignalPrvCopierReviewUserData(String brand, String server) {
        String cp_url = AlphaServerEnv.getCpUrl(server);
        String admin_url = AlphaServerEnv.getAdminUrl(server);
        brand = brand.trim();
        if ("vfx".equalsIgnoreCase(brand) || "au".equalsIgnoreCase(brand)) {//ib:testcrmautomationhsubst@mailinator.com
            return new Object[][]{{"VFSC2", "testcrmautomationhsubst2@mailinator.com", "123Qwe@@", "920024703",cp_url,admin_url,"cmatest", "123Qwe"}};
        }
        if ("pug".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrmautomationhsubst@mailinator.com", "123Qwe@@", "7892730",cp_url,admin_url,"cmatest", "123Qwe"}};
        }
        if ("vt".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrmcopytradingpayment@mailinator.com", "123Qwe@@", "7892730",cp_url,admin_url,"cmatest", "123Qwe"}};
        }
        if ("vjp".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrmcopytradingpayment@mailinator.com", "123Qwe@@", "7892730",cp_url,admin_url,"cmatest", "123Qwe"}};
        }
        if ("um".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrmcopytradingpayment@mailinator.com", "123Qwe@@", "7892730",cp_url,admin_url,"cmatest", "123Qwe"}};
        }
        else {
            return new Object[][]{{"SVG", "", "", "", "", ""}};
        }
    }

    /**
     * 返回copy trading signal provider test的测试账号
     *
     * @param brand
     * @param server
     * @return regulator，copy trading user，password，copy trading account, strategy account, cp_url
     */
    public static Object[][] getAlphaCopyTradingAgentLinkUserData(String brand, String server) {
        String cp_url = AlphaServerEnv.getCpUrl(server);
        String admin_url = AlphaServerEnv.getAdminUrl(server);
        brand = brand.trim();
        if ("vfx".equalsIgnoreCase(brand) || "au".equalsIgnoreCase(brand)) {
            return new Object[][]{{"VFSC2", "testcrmautomationhsubst2@mailinator.com", "123Qwe@@", "920024703",cp_url,admin_url,"cmatest", "123Qwe","testapptestcrmautomationhsubst"}};
        }
        if ("pug".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrmautomationhsubst@mailinator.com", "123Qwe@@", "7892730",cp_url,admin_url,"cmatest", "123Qwe","testapptestcrmautomationhsubst"}};
        }
        if ("vt".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrmcopytradingpayment@mailinator.com", "123Qwe@@", "7892730",cp_url,admin_url,"cmatest", "123Qwe","testapptestcrmautomationhsubst"}};
        }
        if ("vjp".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrmcopytradingpayment@mailinator.com", "123Qwe@@", "7892730",cp_url,admin_url,"cmatest", "123Qwe","testapptestcrmautomationhsubst"}};
        }
        if ("um".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrmcopytradingpayment@mailinator.com", "123Qwe@@", "7892730",cp_url,admin_url,"cmatest", "123Qwe","testapptestcrmautomationhsubst"}};
        }

        else {
            return new Object[][]{{"SVG", "", "", "", "", ""}};
        }
    }


    /**
     * 返回alpha account login test的测试账号
     *
     * @param brand
     * @param server
     * @return regulator, username, password, cp_url, admin_url, open_api, ib_url, phone number
     */
    public static Object[][] getAlphaAccountLoginUsersData(String brand, String server) {
        String cp_url = AlphaServerEnv.getCpUrl(server);
        String admin_url = AlphaServerEnv.getAdminUrl(server);
        String open_api = AlphaServerEnv.getOpenApiUrl(server);
        String ib_url = AlphaServerEnv.getIBUrl(server);

        if ("vt".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrm_autotest_loginacc@testcrm.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe", "60 0000452807569",
                    "otpauth://totp/VT%20Markets%3Atestcrm_autotest_loginacc%40testcrm.com%3Ad0hnJR?secret=Y5V4BUVVSOTFN5MIRYWCOMCZLU32ABJW&issuer=VT%20Markets"}};
        }

        if ("pug".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrm_autotest_loginacc@testcrm.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe", "60 0000452807569",
                    "otpauth://totp/PU Prime:testcrm_autotest_loginacc@testcrm.com:W23MoL?secret=NXDDGMDGYZPNTVWUT34SFFZZ6Q2HKK7L"}};
        }

        if ("vfx".equalsIgnoreCase(brand) || "au".equalsIgnoreCase(brand)) {
            return new Object[][]{{"VFSC", "testcrm_autotest_loginacc@testcrm.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe", "60 0000452807569",
                    "otpauth://totp/Vantage%3Atestcrm_autotest_loginacc%40testcrm.com%3AJUYtSC?secret=P2KLUBKL5IQDAR6HDOKNY6Z5WYFNQN5O&issuer=Vantage"}};
        }

        if ("um".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrm_autotest_loginacc@testcrm.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe", "60 0000452807568",
                    "otpauth://totp/Ultima%20Markets%3Atestcrm_autotest_loginacc%40testcrm.com%3AIMIbhY?secret=NFLARO43U327SY4DUEDHKM3VUXMUBCDX&issuer=Ultima%20Markets"}};
        }

        if ("mo".equalsIgnoreCase(brand)) {
            return new Object[][]{{"VFSC", "testcrm_autotest_logicacc@testcrm.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe", "60 0000452807569",
                    "otpauth://totp/Moneta%20Markets%3Atestcrm_autotest_logicacc%40testcrm.com%3A4YSU22?secret=34Z7QZSRFTCA6FZSGZKNQMSWLVTBZZZV&issuer=Moneta%20Markets"}};
        }

        if ("star".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrm_autotest_loginacc@testcrm.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe", "60 0000452807569",
                    "otpauth://totp/STARTRADER%3Atestcrm_autotest_loginacc%40testcrm.com%3AQPSksv?secret=URW37W65JU4ZS74KZS6DQARHRCZIT2RI&issuer=STARTRADER"}};
        }

        if ("vjp".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrm_autotest_loginacc@testcrm.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "1234Qwer!", "60 0000452807569",
                    "otpauth://totp/Vantage Japan:testcrm_autotest_loginacc@testcrm.com:cXIAUq?secret=7X4MX2KB3C5FYJLMR3CTXGPB72UDWXID"}};
        }

        return null;
    }

    /**
     * 返回alpha account forgot password test的测试账号
     *
     * @param brand
     * @param server
     * @return regulator, username, password, cp_url, admin_url, open_api, ib_url, admin username, admin password, phone number
     */
    public static Object[][] getAlphaAccountForgotPwdUsersData(String brand, String server) {
        String cp_url = AlphaServerEnv.getCpUrl(server);
        String admin_url = AlphaServerEnv.getAdminUrl(server);
        String open_api = AlphaServerEnv.getOpenApiUrl(server);
        String ib_url = AlphaServerEnv.getIBUrl(server);

        if ("vt".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrm_autotest_forgotpwd@testcrm.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe", "60 0000452807566"}};
        }

        if ("pug".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrm_autotest_forgotpwd@testcrm.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe", "60 0000452807566"}};
        }

        if ("vfx".equalsIgnoreCase(brand) || "au".equalsIgnoreCase(brand)) {
            return new Object[][]{{"VFSC2", "testcrm_automation_forgotpwd@testcrm.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe", "55 00005464543"}};
        }

        if ("um".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrm_autotest_forgotpwd@testcrm.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe", "60 0000452807566"}};
        }

        if ("mo".equalsIgnoreCase(brand)) {
            return new Object[][]{{"VFSC", "testcrm_autotest_forgotpwd@testcrm.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe", "60 0000452807566"}};
        }

        if ("star".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrm_autotest_forgotpwd@testcrm.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe", "60 0000452807566"}};
        }

        if ("vjp".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrm_autotest_forgotpwd@testcrm.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "1234Qwer!", "60 0000452807566"}};
        }

        return null;
    }

    /**
     * 返回alpha account change password test的测试账号
     *
     * @param brand
     * @param server
     * @return regulator, username, password, p_url, admin_url, open_api, ib_url, admin username, admin password
     */
    public static Object[][] getAlphaAccountChgPwdUsersData(String brand, String server) {
        String cp_url = AlphaServerEnv.getCpUrl(server);
        String admin_url = AlphaServerEnv.getAdminUrl(server);
        String open_api = AlphaServerEnv.getOpenApiUrl(server);
        String ib_url = AlphaServerEnv.getIBUrl(server);

        if ("vt".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrm_autotest_chgpwd@testcrm.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe"}};
        }

        if ("pug".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrm_autotest_chgpwd@testcrm.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe"}};
        }

        if ("vfx".equalsIgnoreCase(brand) || "au".equalsIgnoreCase(brand)) {
            return new Object[][]{{"VFSC2", "testcrm_automation_chgpwd@testcrm.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe"}};
        }

        if ("um".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrm_autotest_chgpwd@testcrm.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe"}};
        }

        if ("mo".equalsIgnoreCase(brand)) {
            return new Object[][]{{"VFSC", "testcrm_autotest_chgpwd@testcrm.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe"}};
        }

        if ("star".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrm_autotest_chgpwd@testcrm.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe"}};
        }

        if ("vjp".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrm_autotest_chgpwd@testcrm.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "1234Qwer!"}};
        }

        return null;
    }

    /**
     * 返回alpha account 账户类型比汇总 test的测试账号
     *
     * @param brand
     * @param server
     * @param country
     * @return regulator, username, password, p_url, admin_url, open_api, ib_url, admin username, admin password
     */
    public static Object[][] getAlphaOpenAddAccConfigCheckUsersData(String brand, String server, String country) {
        String cp_url = AlphaServerEnv.getCpUrl(server);
        String admin_url = AlphaServerEnv.getAdminUrl(server);
        String open_api = AlphaServerEnv.getOpenApiUrl(server);
        String ib_url = AlphaServerEnv.getIBUrl(server);
        country = country == null ? "" : country.trim().toLowerCase();

        if ("vt".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrm_autotest_chgpwd@testcrm.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe"}};
        }

        if ("pug".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrm_autotest_chgpwd@testcrm.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe"}};
        }

        if ("vfx".equalsIgnoreCase(brand) || "au".equalsIgnoreCase(brand)) {
            String email = switch(country) {
                case "thailand" -> "testcrmaddaccinfochecktTH@testcrmautomation.com";
                case "united kingdom" -> "testcrmaddaccinfocheckUK@testcrmautomation.com";
                case "poland" -> "testcrmaddaccinfocheckPL@testcrmautomation.com";
                default -> "testcrmaddaccinfocheckFR@testcrmautomation.com"; // france
            };
            return new Object[][]{{"VFSC2", email, "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest.account", "123Qwe@@"}};
        }

        if ("um".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrm_autotest_chgpwd@testcrm.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe"}};
        }

        if ("mo".equalsIgnoreCase(brand)) {
            return new Object[][]{{"VFSC", "testcrm_autotest_chgpwd@testcrm.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe"}};
        }

        if ("star".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrm_autotest_chgpwd@testcrm.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe"}};
        }

        if ("vjp".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrm_autotest_chgpwd@testcrm.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "1234Qwer!"}};
        }

        return null;
    }

    /**
     * 返回alpha account kyc test的测试账号
     *
     * @param brand
     * @param server
     * @return regulator, username, password, p_url, admin_url, open_api, ib_url, admin username, admin password
     */
    public static Object[][] getAlphaAccountKYCUsersData(String brand, String server) {
        String cp_url = AlphaServerEnv.getCpUrl(server);
        String admin_url = AlphaServerEnv.getAdminUrl(server);
        String open_api = AlphaServerEnv.getOpenApiUrl(server);
        String ib_url = AlphaServerEnv.getIBUrl(server);

        if ("vt".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrm_automation_testcrmlvlone@testcrm.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe"}};
        }

        if ("pug".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrm_automation_testcrmlvlone@testcrm.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe"}};
        }

        if ("vfx".equalsIgnoreCase(brand) || "au".equalsIgnoreCase(brand)) {
            return new Object[][]{{"VFSC2", "testcrm_automation_testcrmlvlone@testcrm.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe"}};
        }

        if ("um".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrm_automation_testcrmlvlone@testcrm.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe"}};
        }

        if ("mo".equalsIgnoreCase(brand)) {
            return new Object[][]{{"VFSC", "testcrm_automation_testcrmlvlone@testcrm.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe"}};
        }

        if ("star".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrm_automation_testcrmlvlone@testcrm.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe"}};
        }

        if ("vjp".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrm_automation_testcrmlvlone@testcrm.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "1234Qwer!"}};
        }

        return null;
    }

    /**
     * 返回alpha App regression 出金没CC余额的测试账号
     *
     * @param brand
     * @param server
     * @return regulator, app_url, user_id, accountNumber, user_name, pwd
     */
    public static Object[][] getAlphaAPPWithdrawalUsersData(String brand, String server) {
        String app_url = AlphaServerEnv.getAPPServiceUrl(server);
        brand = brand.trim();
        if ("vt".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", app_url, "10057485", "14840041", "MT4", "testcrm_dontotuch_appwithdrawaluser@testcrm.com", "123Qwe@@"}};
        }
        if ("pug".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", app_url, "80212965", "14200029", "MT4", "testcrm_dontotuch_appwithdrawaluser@testcrm.com", "123Qwe@@"}};
        }
        if ("vfx".equalsIgnoreCase(brand) || "au".equalsIgnoreCase(brand)) {
            return new Object[][]{{"VFSC", app_url, "10095932", "8350096", "MT4", "testcrm_donttouch_appwithdrawaluser2@testcrm.com", "123Qwe@@"}};
        }
        if ("um".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", app_url, "837530", "14760078", "MT4", "testcrm_dontotuch_appwithdrawuser@testcrm.com", "123Qwe@@"}};
        }
        if ("mo".equalsIgnoreCase(brand)) {
            return new Object[][]{{"VFSC", app_url, "6310", "14750035", "MT4", "testcrm_dontotuch_appwithdrawaluser@testcrm.com", "123Qwe@@"}};
        }
        if ("star".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", app_url, "10087055", "8181002", "MT5", "testcrmapitest1027@mailinator.com", "123Qwe@@"}};

//            return new Object[][]{{"SVG", app_url, "10064085", "9650060", "MT4", "testcrm_dontotuch_appwithdrawaluser@testcrm.com", "123Qwe@@"}};
        }
        if ("vjp".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", app_url, "2668656", "14770021", "MT4", "testcrm_dontotuch_appwithdrawaluser@testcrm.com", "123Qwe@@"}};
        }
        if ("kcm".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", app_url, "", "", ""}};
        }
        return null;
    }


    /**
     * 返回alpha api deposit test的测试账号
     *
     * @param brand
     * @param server
     * @return brand，username，password，cp_url,pcshost, unionpayuser
     */
    public static Object[][] getAlphaAPIDepositUsersData(String brand, String server) {
        String cp_url = AlphaServerEnv.getCpUrl(server);
        brand = brand.trim();
        if("vt".equalsIgnoreCase(brand)) {
            return new Object[][] {{"vt","testcrm_apiautotest@testcrm.com","123Qwe@@",cp_url,"testcrm_wdunionusertest@testcrm.com"}};
        }
        if("pug".equalsIgnoreCase(brand)) {
            return new Object[][] {{"pug","testcrm_apiauto@testcrm.com","123Qwe@@",cp_url,""}};
        }
        if("vfx".equalsIgnoreCase(brand) || "au".equalsIgnoreCase(brand)) {
            return new Object[][] {{"vantage","testcrm_apiauto@testcrm.com","123Qwe@@",cp_url,""}};
        }
        if("um".equalsIgnoreCase(brand)) {
            return new Object[][] {{"ultima","testcrm_apiauto@testcrm.com","123Qwe@@",cp_url,"testcrm_wdunionuser@testcrm.com"}};
        }
        if("mo".equalsIgnoreCase(brand)) {
            return new Object[][] {{"moneta","testcrm_apiauto@testcrm.com","123Qwe@@",cp_url,"testcrm_donttouch_cpschinaclient@test.com"}};
        }
        if("star".equalsIgnoreCase(brand)) {
            return new Object[][] {{"","testcrm_apiauto@testcrm.com","123Qwe@@",cp_url,"testcrm_wdunionuser@testcrm.com"}};
        }
        if("vjp".equalsIgnoreCase(brand)) {
            return new Object[][] {{"vjp","testcrm_apiauto@testcrm.com","123Qwe@@",cp_url,""}};
        }
        return null;
    }

    /**
     * 返回alpha api withdrawal test的测试账号
     *
     * @param brand
     * @param server
     * @return regulator，normal withdraw user，password，cp_url, cc withdraw user, unionpay user
     */
    public static Object[][] getAlphaAPIWithdrawUsersData(String brand, String server) {
        String cp_url = AlphaServerEnv.getCpUrl(server);
        brand = brand.trim();
        if ("vt".equalsIgnoreCase(brand)) {
//            return new Object[][]{{"", "testcrm_wdapiautotest@testcrm.com", "123Qwe@@", cp_url, "1538@hotmail.com", "testcrm_wdunionusertest@testcrm.com"}};
            return new Object[][]{{"", "testcrm_wdapiautotest@testcrm.com", "123Qwe@@", cp_url, "adamas1103@mailinator.com", "testcrm_wdunionusertest@testcrm.com"}};

        }
        if ("pug".equalsIgnoreCase(brand)) {
            return new Object[][]{{"", "testcrm_wdapiauto@testcrm.com", "123Qwe@@", cp_url, "autotestfnpcgbsw@testcrmautomation.com", ""}};
        }
        if ("vfx".equalsIgnoreCase(brand) || "au".equalsIgnoreCase(brand)) {
            return new Object[][]{{"", "testcrm_wdapiauto@testcrm.com", "123Qwe@@", cp_url, "testpersentmandy@testpersentmandy.com", ""}};
        }
        if ("um".equalsIgnoreCase(brand)) {
            return new Object[][]{{"", "testcrm_wdapiauto@testcrm.com", "123Qwe@@", cp_url, "autotestunainbxg@testcrmautomation.com", "testcrm_wdunionuser@testcrm.com"}};
        }
        if ("mo".equalsIgnoreCase(brand)) {
            return new Object[][]{{"", "testcrm_wdapiauto@testcrm.com", "123Qwe@@", cp_url, "bbb@test.com", "testcrm_donttouch_cpschinaclient@test.com"}};
        }
        if ("star".equalsIgnoreCase(brand)) {
//            return new Object[][]{{"", "testcrm_wdapiauto@testcrm.com", "123Qwe@@", cp_url, "cdtfyqfe590@appapiautotest.com", "testcrm_wdunionuser@testcrm.com"}};
            return new Object[][]{{"", "testcrm_wdapiauto@testcrm.com", "123Qwe@@", cp_url, "cdtfyqfe590@appapiautotest.com", "testcrm1027@mailinator.com"}};

        }
        if ("vjp".equalsIgnoreCase(brand)) {
            return new Object[][]{{"", "testcrm_wdapiauto@testcrm.com", "123Qwe@@", cp_url, "vjpapptestautomation@mailinator.com", ""}};
        }
        return null;
    }

    /**
     * 返回alpha api withdrawal test的混合出金方式的用户
     *
     * @param brand
     * @param server
     * @return regulator，normal withdraw user，password，cp_url, cc withdraw user, unionpay user
     */
    public static Object[][] getAlphaAPIMixedWithdrawUserData(String brand, String server) {
        String cp_url = AlphaServerEnv.getCpUrl(server);
        brand = brand.trim();
        if ("vt".equalsIgnoreCase(brand)) { //done
            return new Object[][]{{cp_url, "123Qwe@@", "testcrmauto_cclbtotherwd@test.com","testcrm_ccotherwd@test.com",
                    "testcrm_lbtotherwd@test.com","testcrm_cclbtwd@test.com"}};
        }
        if ("pug".equalsIgnoreCase(brand)) { //done
            return new Object[][]{{cp_url, "123Qwe@@", "testcrm_cclbtotherwd@test.com","testcrm_ccotherwd@test.com",
                    "testcrm_lbtotherwd@test.com","testcrm_cclbtwd@test.com"}};
        }
        if ("vfx".equalsIgnoreCase(brand) || "au".equalsIgnoreCase(brand)) { //done
            return new Object[][]{{cp_url, "123Qwe@@", "testcrm_cclbtotherwd@test.com","testcrm_ccotherwd@test.com",
                    "testcrm_lbtotherwd@test.com","testcrm_cclbtwd@test.com"}};
        }
        if ("um".equalsIgnoreCase(brand)) { //pending run
            return new Object[][]{{cp_url, "123Qwe@@", "testcrm_cclbtotherwd@test.com","testcrm_ccotherwd@test.com",
                    "testcrm_lbtotherwd@test.com","testcrm_cclbtwd@test.com"}};
        }
        if ("mo".equalsIgnoreCase(brand)) { //done
            return new Object[][]{{cp_url, "123Qwe@@", "testcrm_cclbtotherwd@test.com","testcrm_ccotherwd@test.com",
                    "testcrm_lbtotherwd@test.com","testcrm_cclbtwd@test.com"}};
        }
        if ("star".equalsIgnoreCase(brand)) { //done, left find cc can be reverse
            return new Object[][]{{cp_url, "123Qwe@@", "testcrm_cclbtotherwd@test.com","testcrm_ccotherwd@test.com",
                    "testcrm_lbtotherwd@test.com","testcrm_cclbtwd@test.com"}};
        }
        if ("vjp".equalsIgnoreCase(brand)) { //done
            return new Object[][]{{cp_url, "123Qwe@@", "testcrm_cclbtotherwd@test.com","testcrm_ccotherwd@test.com",
                    "testcrm_lbtotherwd@test.com","testcrm_cclbtwd@test.com"}};
        }
        return null;
    }

    /**
     * 返回alpha api big amount withdrawal test的测试账号
     *
     * @param brand
     * @param server
     * @return regulator，withdraw user，password，cp_url
     */
    public static Object[][] getAlphaAPIBigAmountWDUserData(String brand, String server) {
        String cp_url = AlphaServerEnv.getCpUrl(server);
        brand = brand.trim();
        if ("vt".equalsIgnoreCase(brand)) {
//      1120      return new Object[][]{{"SVG", "auto0825071448918@nqmo.com", "1234Qwer!", cp_url}};
            return new Object[][]{{"SVG", "testcrmwTrZlQfmW@testcrm.com", "1234Qwer@", cp_url}};


        }
        if ("pug".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "zpadnsfh503@appapiautotest.com", "123Qwe", cp_url}};
        }
        if ("vfx".equalsIgnoreCase(brand) || "au".equalsIgnoreCase(brand)) {
//     1120       return new Object[][]{{"VFSC", "ysrrtsfh254@appapiautotest.com", "123Qwe", cp_url}};
            return new Object[][]{{"VFSC", "testcrmwfbLcWTRk@testcrm.com", "1234Qwer@", cp_url}};

        }
        if ("um".equalsIgnoreCase(brand)) {
//            return new Object[][]{{"SVG", "qyqbexbc871@appapiautotest.com", "123Qwe", cp_url}};
            return new Object[][]{{"SVG", "bigamount260106@mailinator.com", "123Qwe@@", cp_url}};

        }
        if ("mo".equalsIgnoreCase(brand)) {
            //旧return new Object[][]{{"VFSC", "pkekzuqg612@appapiautotest.com", "123Qwe", cp_url}};
            return new Object[][]{{"VFSC", "testcrmwWwVCQNPu@testcrm.com", "Qwer1234@", cp_url}};

        }
        if ("star".equalsIgnoreCase(brand)) {
//     旧       return new Object[][]{{"SVG", "auto0423193902714@nqmo.com", "1234Qwer!", cp_url}};
            return new Object[][]{{"SVG", "testcrmstbigmonney1027@mailinator.com", "1234Qwer!", cp_url}};

        }
        if ("vjp".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "abhetcpo329@appapiautotest.com", "123Qwe", cp_url}};
        }
        return null;
    }

    /**
     * 返回copy trading test的测试账号
     *
     * @param brand
     * @param server
     * @return regulator，copy trading user，password，copy trading account, strategy account, cp_url
     */
    public static Object[][] getAlphaCopyTradingUserData(String brand, String server) {
        String cp_url = AlphaServerEnv.getCpUrl(server);
        String admin_url = AlphaServerEnv.getAdminUrl(server);
        brand = brand.trim();
        if ("vfx".equalsIgnoreCase(brand) || "au".equalsIgnoreCase(brand)) {
        return new Object[][]{{"VFSC", "testcopytradinghelene@mailinator.com", "123Qwe@@", "920023751","19605169",cp_url,admin_url,"cmatest", "123Qwe"}};
        }
        if ("pug".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testbitcopytradinghelenb@mailinator.com", "1234Qwer!", "16660268","910014157",cp_url,admin_url,"cmatest", "123Qwe"}};
        }
        if ("vjp".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcopytradinghelene@mailinator.com", "123Qwe@@", "84006032","84006031",cp_url,admin_url,"cmatest", "1234Qwer!"}};
        }
        if ("vt".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testautomationsmoket@mailinator.com", "123Qwe@@", "930006196","930006197",cp_url,admin_url,"cmatest", "1234Qwer!"}};
        }
        if ("um".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcopytradinghelenb@mailinator.com", "123Qwe@@", "11803269","11803043",cp_url,admin_url,"cmatest", "1234Qwer!"}};
        }
        if ("mo".equalsIgnoreCase(brand)) {
            return new Object[][]{{"VFSC", "testautomationsmoket@mailinator.com", "123Qwe@@", "85003398","7851498",cp_url,admin_url,"cmatest", "1234Qwer!"}};
        }
        else {
            return new Object[][]{{"SVG", "", "", "", "", ""}};
        }
    }
    /**
     * 返回copy trading test的测试账号
     *
     * @param brand
     * @param server
     * @return regulator，copy trading user，password，copy trading account(USD), strategy account(EUR), cp_url
     */
    public static Object[][] getAlphaCopyTradingEURUserData(String brand, String server) {
        String cp_url = AlphaServerEnv.getCpUrl(server);
        String admin_url = AlphaServerEnv.getAdminUrl(server);
        brand = brand.trim();
        if ("vfx".equalsIgnoreCase(brand) || "au".equalsIgnoreCase(brand)) {
            return new Object[][]{{"VFSC", "testcrmautomationtradingeur@mailinator.com", "123Qwe@@", "920023806","19602857",cp_url,admin_url,"cmatest", "123Qwe"}};
        }
        if ("pug".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testautomationeurcp@mailinator.com", "123Qwe@@", "910014078","910014079",cp_url,admin_url,"cmatest", "123Qwe"}};
        }
        if ("vt".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrmcopytradingeurtest@mailinator.com", "123Qwe@@", "930006193","930006192",cp_url,admin_url,"cmatest", "123Qwe"}};
        }
        if ("vjp".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrmcopytradingeurtest@mailinator.com", "123Qwe@@", "84006088","84006089",cp_url,admin_url,"cmatest", "1234Qwer!"}};
        }
        if ("um".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrmcopytradingeurtest@mailinator.com", "123Qwe@@", "11803632","22240074",cp_url,admin_url,"cmatest", "1234Qwer!"}};
        }
        if ("mo".equalsIgnoreCase(brand)) {
            return new Object[][]{{"VFSC", "testcrmcopytradingeurtest@mailinator.com", "123Qwe@@", "85003487","84006089",cp_url,admin_url,"cmatest", "1234Qwer!"}};
        }
        else {
            return new Object[][]{{"SVG", "", "", "", "", ""}};
        }
    }
    /**
     * 返回copy trading test的测试账号
     *
     * @param brand
     * @param server
     * @return regulator，copy trading user，password，copy trading account, strategy account, cp_url
     */
    public static Object[][] getAlphaCopyTradingCopierUserData(String brand, String server) {
        String cp_url = AlphaServerEnv.getCpUrl(server);
        String admin_url = AlphaServerEnv.getAdminUrl(server);
        brand = brand.trim();
        if ("vfx".equalsIgnoreCase(brand) || "au".equalsIgnoreCase(brand)) {
            return new Object[][]{{"VFSC", "testcrmautomationpositioncp@mailinator.com", "123Qwe@@", "920023815","920023817",cp_url,admin_url,"cmatest", "123Qwe"}};
        }
        if ("pug".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrmautomationeposition@mailinator.com", "123Qwe@@", "910014082","910014104",cp_url,admin_url,"cmatest", "123Qwe"}};
        }
        if ("vt".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrmcopytradingeurd@mailinator.com", "123Qwe@@", "930006271","930006279",cp_url,admin_url,"cmatest", "123Qwe"}};
        }
        if ("vjp".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrmautomationeposition@mailinator.com", "123Qwe@@", "84006091","84006092",cp_url,admin_url,"cmatest", "1234Qwer!"}};
        }
        if ("um".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrmautomationeposition@mailinator.com", "123Qwe@@", "11803638","22240075",cp_url,admin_url,"cmatest", "123Qwe"}};
        }
        else {
            return new Object[][]{{"SVG", "", "", "", "", ""}};
        }
    }

    /**
     * 返回copy trading signal provider test的测试账号
     *
     * @param brand
     * @param server
     * @return regulator，copy trading user，password，copy trading account, strategy account, cp_url
     */
    public static Object[][] getAlphaCopyTradingSignalPrvUserData(String brand, String server) {
        String cp_url = AlphaServerEnv.getCpUrl(server);
        String admin_url = AlphaServerEnv.getAdminUrl(server);
        brand = brand.trim();
        if ("vfx".equalsIgnoreCase(brand) || "au".equalsIgnoreCase(brand)) {
            return new Object[][]{{"VFSC","testcrmautomationsptest@mailinator.com", "123Qwe@@", "19602888",cp_url,admin_url,"cmatest", "123Qwe"}};
        }
        if ("pug".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrmautomationsignalprovider@mailinator.com", "123Qwe@@", "910014108",cp_url,admin_url,"cmatest", "123Qwe"}};
        }
        if ("vt".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrmhwsignalprovider@mailinator.com", "123Qwe@@", "930006172",cp_url,admin_url,"cmatest", "123Qwe"}};
        }
        if ("vjp".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrmhwsignalprovider@mailinator.com", "123Qwe@@", "84006099",cp_url,admin_url,"cmatest", "1234Qwer!"}};
        }
        if ("um".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrmhwsignalprovider@mailinator.com", "123Qwe@@", "22240080",cp_url,admin_url,"cmatest", "1234Qwer!"}};
        }
        else {
            return new Object[][]{{"SVG", "", "", "", "", ""}};
        }
    }
    /**
     * 返回copy trading signal provider test的测试账号
     *
     * @param brand
     * @param server
     * @return regulator，copy trading user，password，copy trading account, strategy account, cp_url
     */
    public static Object[][] getAlphaCopyTradingSUBSignalPrvUserData(String brand, String server) {
        String cp_url = AlphaServerEnv.getCpUrl(server);
        String admin_url = AlphaServerEnv.getAdminUrl(server);
        brand = brand.trim();
        if ("vfx".equalsIgnoreCase(brand) || "au".equalsIgnoreCase(brand)) {
            return new Object[][]{{"VFSC", "testcrmautomationsubsprov@mailinator.com", "123Qwe@@", "920023835",cp_url,admin_url,"cmatest", "123Qwe"}};
        }
        if ("pug".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrmautomationsubsignalp@mailinator.com", "123Qwe@@", "910014109",cp_url,admin_url,"cmatest", "123Qwe"}};
        }
        if ("vt".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrmsubsignalprovider@mailinator.com", "123Qwe@@", "7763537",cp_url,admin_url,"cmatest", "123Qwe"}};
        }
        if ("vjp".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrmsubsignalprovider@mailinator.com", "123Qwe@@", "84006100",cp_url,admin_url,"cmatest", "123Qwe"}};
        }
        if ("um".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrmsubsignalprovider@mailinator.com", "123Qwe@@", "11803653",cp_url,admin_url,"cmatest", "123Qwe"}};
        }
        else {
            return new Object[][]{{"SVG", "", "", "", "", ""}};
        }
    }
    /**
     * 返回copy trading test的测试账号
     *
     * @param brand
     * @param server
     * @return regulator，copy trading user，password，copy trading account, strategy account, cp_url
     */
    public static Object[][] getAlphaCopyTradingCopierMenuUserData(String brand, String server) {
        String cp_url = AlphaServerEnv.getCpUrl(server);
        String admin_url = AlphaServerEnv.getAdminUrl(server);
        brand = brand.trim();//920023821
        if ("vfx".equalsIgnoreCase(brand) || "au".equalsIgnoreCase(brand)) {
            return new Object[][]{{"VFSC", "testcrmautomationcpmenu@mailinator.com", "123Qwe@@", "920023820","19602873",cp_url,admin_url,"cmatest", "123Qwe"}};
        }
        if ("pug".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrmautomationmenuhw@mailinator.com", "123Qwe@@", "910014106","910014107",cp_url,admin_url,"cmatest", "123Qwe"}};
        }
        if ("vt".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrmcopytcopierhw@mailinator.com", "123Qwe@@", "930006169","930006170",cp_url,admin_url,"cmatest", "123Qwe"}};
        }
        //换个用户
        if ("vjp".equalsIgnoreCase(brand)) {
           // return new Object[][]{{"SVG", "testcrmcopytradingeurtest@mailinator.com", "123Qwe@@", "84006088","84006087",cp_url,admin_url,"cmatest", "123Qwe"}};
            return new Object[][]{{"SVG", "testcrmcopytcopiemenurhw@mailinator.com", "123Qwe@@", "84006095","84006087",cp_url,admin_url,"cmatest", "123Qwe"}};
        }
        if ("um".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrmautomationcpmenu@mailinator.com", "123Qwe@@", "11803643","22240070",cp_url,admin_url,"cmatest", "123Qwe"}};
        }
        else {
            return new Object[][]{{"SVG", "", "", "", "", ""}};
        }
    }
    /**
     * 返回web trading test的测试账号
     *
     * @param brand
     * @param server
     * @return regulator，web trading user，password，web trading account, cp_url
     */
    public static Object[][] getAlphaWebTradingUserData(String brand, String server) {
        String cp_url = AlphaServerEnv.getCpUrl(server);
        String admin_url = AlphaServerEnv.getAdminUrl(server);
        brand = brand.trim();
        if ("vfx".equalsIgnoreCase(brand) || "au".equalsIgnoreCase(brand)) {
            return new Object[][]{{"VFSC2", "autotestwentradeqvlwhxrsau@testcrm.com", "123Qwe@@", "7217407",cp_url,admin_url,"cmatest", "123Qwe"}};
        }else if ("vjp".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "autotestwebtrade@testcrm.com", "123Qwe@@", "9142596", cp_url, admin_url, "cmatest", "1234Qwer!"}};

        }else if ("star".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "autotestwebtrade@testcrm.com", "123Qwe@@", "8181368", cp_url, admin_url, "cmatest", "123Qwe"}};

        }else if ("PUG".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "autotestwebtrade@testcrm.com", "123Qwe@@", "7893226", cp_url, admin_url, "cmatest", "123Qwe"}};

        }
        else {
            return new Object[][]{{"SVG", "", "", "", "", ""}};
        }
    }

    /**
     * 返回alpha api wallet test的测试账号
     *
     * @param brand
     * @param server
     * @return regulator，username，password，cp_url
     */
    public static Object[][] getAlphaAPIWalletUsersData(String brand, String server) {
        String cp_url = AlphaServerEnv.getCpUrl(server);
        String admin_url = AlphaServerEnv.getAdminUrl(server);
        String openapi_url = AlphaServerEnv.getOpenApiUrl(server);
        brand = brand.trim();

        if ("vfx".equalsIgnoreCase(brand) || "au".equalsIgnoreCase(brand)) {
            return new Object[][]{{"VFSC", "autotestetdfyzri@testcrmautomation.com", "123Qwe", cp_url, openapi_url, admin_url}};
        }
        if ("pug".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "autotestetdfyzri@testcrmautomation.com", "123Qwe@@", cp_url, openapi_url, admin_url}};
        }
        if ("vt".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "autotestetdfyzri@testcrmautomation.com", "123Qwe@@", cp_url, openapi_url, admin_url}};
        }
        if ("um".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "autotestetdfyzri@testcrmautomation.com", "123Qwe@@", cp_url, openapi_url, admin_url}};
        }
        if ("mo".equalsIgnoreCase(brand)) {
            return new Object[][]{{"VFSC", "autotestetdfyzri@testcrmautomation.com", "123Qwe@@", cp_url, openapi_url, admin_url}};
        }
        if ("star".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "autotestetdfyzri@testcrmautomation.com", "123Qwe@@", cp_url, openapi_url, admin_url}};
        }
        if ("vjp".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "autotestetdfyzri@testcrmautomation.com", "123Qwe@@", cp_url, openapi_url, admin_url}};
        }

        return null;
    }
    /**
     * 返回alpha API transfer测试账号 && Admin用于上传IB转账记录的账号
     *
     * @param brand
     * @param server
     * @return username，password，cp_url, IBLvl1fromAcc(Admin用途), IBLvl2ToAcc(Admin用途)
     */
    public static Object[][] getAlphaAPIAccTransferUsersData(String brand, String server) {
        String cp_url = AlphaServerEnv.getCpUrl(server);
        brand = brand.trim();
        if ("vfx".equalsIgnoreCase(brand) || "au".equalsIgnoreCase(brand)) {
            return new Object[][]{{"autotestfundtrs@testcrmautomation.com", "123Qwe@@", cp_url, "11640035", "11640034"}};
        }
        if ("vt".equalsIgnoreCase(brand)) {
            return new Object[][]{{"autotestfundtrs@mailinator.com", "123Qwe@@", cp_url, "1410006", "1240006"}};
        }
        if ("pug".equalsIgnoreCase(brand)) {
            return new Object[][]{{"autotestfundtrs@mailinator.com", "123Qwe@@", cp_url, "16340002", "16340003"}};
        }
        if ("um".equalsIgnoreCase(brand)) {
            return new Object[][]{{"autotestfundtrs@mailinator.com", "123Qwe@@", cp_url, "15530003", "15530002"}};
        }
        if ("mo".equalsIgnoreCase(brand)) {
            return new Object[][]{{"autotestfundtrs@testcrmautomation.com", "123Qwe@@", cp_url, "14850014", "14850013"}};
        }
        if ("star".equalsIgnoreCase(brand)) {
            return new Object[][]{{"autotestfundtrs@testcrmautomation.com", "123Qwe@@", cp_url,"11570003","11570002"}};
        }
        if ("vjp".equalsIgnoreCase(brand)) {
            return new Object[][]{{"autotestfundtrs@testcrmautomation.com", "123Qwe@@", cp_url, "15460003", "15460006"}};
        }
        return null;
    }

    /**
     * 返回lbt 数据的测试账号
     * @param brand
     * @return userid, email, password
     */
    public static Object[][] getAlphaLBTUsersData(String brand){
        brand = brand.trim();
        if("vt".equalsIgnoreCase(brand)) {
            return new Object[][] {{"10062733","testcrmauto_lbtuser@testcrm.com","123Qwe@@"}};
        }
        if("pug".equalsIgnoreCase(brand)) {
            return new Object[][] {{"80218218","testcrmauto_lbtuser@testcrm.com","123Qwe@@"}};
        }
        if("vfx".equalsIgnoreCase(brand) || "au".equalsIgnoreCase(brand)) {
            return new Object[][] {{"10123686","testcrmauto_lbtuser@testcrm.com","123Qwe@@"}};
        }
        if("um".equalsIgnoreCase(brand)) {
            return new Object[][] {{"839587","testcrmauto_lbtuser@testcrm.com","123Qwe@@"}};
        }
        if("mo".equalsIgnoreCase(brand)) {
            return new Object[][] {{"8675","testcrmauto_lbtuser@testcrm.com","123Qwe@@"}};
        }
        if("star".equalsIgnoreCase(brand)) {
            return new Object[][] {{"10069423","testcrmauto_lbtuser@testcrm.com","123Qwe@@"}};
        }
        if("vjp".equalsIgnoreCase(brand)) {
            return new Object[][] {{"2671737","testcrmauto_lbtuser@testcrm.com","123Qwe@@"}};
        }
        return null;
    }


    /**
     * 返回alpha api withdrawal test的测试账号
     *
     * @param brand
     * @param server
     * @return regulator，IB withdraw user，password，cp_url, cc withdraw user, unionpay user, SubIB rebate account(USD)
     */
    public static Object[][] getAlphaAPIWithdrawTransferUsersDataIB(String brand, String server) {
        String ib_url = AlphaServerEnv.getIBUrl(server);
        brand = brand.trim();
        if ("vfx".equalsIgnoreCase(brand) || "au".equalsIgnoreCase(brand)) {
            return new Object[][]{{"VFSC2","autotestodmrzlgi@testcrmautomation.com", "123Qwe@@", ib_url,"autotestodmrzlgi@testcrmautomation.com","autotestodmrzlgi@testcrmautomation.com","11640037"}};
        }
        if ("vt".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG","testcrmauto_ibtransferrecord@testcrm.com", "123Qwe@@", ib_url,"testcrmauto_ibtransferrecord@testcrm.com","testcrmauto_ibtransferrecord@testcrm.com","15600021"}};
        }
        if ("pug".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG","testcrmauto_ibtransferrecord@testcrm.com", "123Qwe@@", ib_url,"testcrmauto_ibtransferrecord@testcrm.com","testcrmauto_ibtransferrecord@testcrm.com","16340003"}};
        }
        if ("um".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG","testcrmauto_ibtransferrecord@testcrm.com", "123Qwe@@", ib_url,"testcrmauto_ibtransferrecord@testcrm.com","testcrmauto_ibtransferrecord@testcrm.com","15530002"}};
        }
        if ("mo".equalsIgnoreCase(brand)) {
            return new Object[][]{{"VFSC","testcrmauto_ibtransferrecord@testcrm.com", "123Qwe@@", ib_url,"testcrmauto_ibtransferrecord@testcrm.com","testcrmauto_ibtransferrecord@testcrm.com","14850013"}};
        }
        if ("star".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG","testcrmauto_ibtransferrecord@testcrm.com", "123Qwe@@", ib_url,"testcrmauto_ibtransferrecord@testcrm.com","testcrmauto_ibtransferrecord@testcrm.com","11570002"}};
        }
        if ("vjp".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG","testcrmauto_ibtransferrecord@testcrm.com", "123Qwe@@", ib_url,"testcrmauto_ibtransferrecord@testcrm.com","testcrmauto_ibtransferrecord@testcrm.com","15460006"}};
        }
        return null;
    }

    /**
     * 返回 alpha api third party， 初始化crm测试账户
     *
     * @param brand
     * @param server
     * @return regulator，cp user，password
     */

    public static Object[][] getAlphaAPIThirdPartyUserData(String brand, String server) {
        String cp_url = AlphaServerEnv.getCpUrl(server);
        brand = brand.trim();
        if ("vfx".equalsIgnoreCase(brand) || "au".equalsIgnoreCase(brand)) {
            return new Object[][]{{"VFSC2", "test@testcrmautomation.com", "123Qwe@@", cp_url}};
        }
        if ("vt".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "test@testcrmautomation.com", "123Qwe@@", cp_url}};
        }
        if ("pug".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "test@testcrmautomation.com", "123Qwe@@", cp_url}};
        }
        if ("um".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "test@testcrmautomation.com", "123Qwe@@", cp_url}};
        }
        if ("mo".equalsIgnoreCase(brand)) {
            return new Object[][]{{"VFSC", "test@testcrmautomation.com", "123Qwe@@", cp_url}};
        }
        if ("star".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "test@testcrmautomation.com", "123Qwe@@", cp_url}};
        }
        if ("vjp".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "test@testcrmautomation.com", "123Qwe@@", cp_url}};
        }
        return null;
    }


    /**
     * 返回alpha IB Report的测试账号
     *
     * @param brand
     * @param server
     * @return regulator，username，password，cp_url,admin_url,open_api,ib_url
     */
    public static Object[][] getAlphaIBReportUsersData(String brand, String server) {
        String cp_url = AlphaServerEnv.getCpUrl(server);
        String admin_url = AlphaServerEnv.getAdminUrl(server);
        String open_api = AlphaServerEnv.getOpenApiUrl(server);
        String ib_url = AlphaServerEnv.getIBUrl(server);
        brand = brand.trim();
        if ("vt".equalsIgnoreCase(brand)) {
            //return new Object[][] {{"SVG","testcrm_donttouchperformancefee@test.com","123Qwe",cp_url,admin_url,open_api,ib_url}};
            return new Object[][]{{"SVG", "testcrmaureg1250918@testcrm.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe", "testcrm_ibwdauto1@testcrm.com", "123Qwe@@"}};
        }
        if ("pug".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "shucang1@qq.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe", "testcrmibwhitni01@testcrm.com", "123Qwe@@"}};
        }
        if ("vfx".equalsIgnoreCase(brand) || "au".equalsIgnoreCase(brand)) {
            return new Object[][]{{"VFSC2", "testcrmaureg1250918@testcrm.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe", "testcrm_ibwdauto@testcrm.com", "123Qwe@@", "ASIC", "testcrm_ib_asic@testcrm.com", "123Qwe@@", "FCA", "testcrm_ib_fca2@testcrm.com", "123Qwe@@"}};
        }
        if ("um".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrm_ibwdauto1@testcrm.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe", "testcrm_ibwdauto1@testcrm.com", "123Qwe@@"}};
        }
        if ("mo".equalsIgnoreCase(brand)) {
            return new Object[][]{{"VFSC", "testcrmaureg1250918@testcrm.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe", "testcrm_ibwdauto1@testcrm.com", "123Qwe@@"}};
        }
        if ("star".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrmaureg1250918@testcrm.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe", "testcrm_ibwdauto1@testcrm.com", "123Qwe@@"}};
        }
        if ("vjp".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrmaureg1250918@testcrm.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "1234Qwer!", "testcrm_ibwdauto@testcrm.com", "123Qwe@@"}};
        }
        return null;
    }
    /**
     * 返回Alpha API IB Agreement的测试账号
     *
     * @param brand
     * @param server
     * @return regulator，username，password，cp_url,admin_url,open_api,ib_url
     */
    public static Object[][] getAlphaIBAgreementUsersData(String brand, String server) {
        String ib_url = AlphaServerEnv.getIBUrl(server);
        brand = brand.trim();

        if ("vt".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrm_API_ib_agreement_auto1@testcrm.com", "123Qwe@@", "8190825", ib_url}};
        }

        if ("pug".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrm_API_ib_agreement_auto1@testcrm.com", "123Qwe@@", "9270462", ib_url}};
        }


        if ("vfx".equalsIgnoreCase(brand) || "au".equalsIgnoreCase(brand)) {
            return new Object[][]{{"VFSC2", "testcrm_API_ib_agreement_auto1@testcrm.com", "123Qwe@@", "8561534", ib_url}};
        }

        if ("um".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrm_API_ib_agreement_auto1@testcrm.com", "123Qwe@@", "9160391", ib_url}};
        }

        if ("mo".equalsIgnoreCase(brand)) {
            return new Object[][]{{"VFSC", "testcrm_API_ib_agreement_auto1@testcrm.com", "123Qwe@@", "8580175", ib_url}};
        }

        if ("star".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrm_API_ib_agreement_auto1@testcrm.com", "123Qwe@@", "9560168", ib_url}};
        }

        if ("vjp".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrm_API_ib_agreement_auto1@testcrm.com", "123Qwe@@", "9490279", ib_url}};
        }

        return null;
    }

    /**
     * 返回UAT DAP Account的测试账号
     *
     * @param brand
     * @param server
     * @return regulator，username，password，dap_url,admin_url,open_api,ib_url
     */
    public static Object[][] getAlphaDAPAccountUsersData(String brand, String server) {
        String dap_url = AlphaServerEnv.getDAPUrl(server);
        String admin_url = AlphaServerEnv.getAdminUrl(server);
        String open_api = AlphaServerEnv.getOpenApiUrl(server);
        String cp_url = AlphaServerEnv.getCpUrl(server);
        String ows_url = AlphaServerEnv.getOWSUrl(server);
        brand = brand.trim();

        if ("vfx".equalsIgnoreCase(brand) || "au".equalsIgnoreCase(brand)) {
            return new Object[][]{{"VFSC2", "testcrm_dap_auto@testcrm.com", "123Qwe@@", dap_url, admin_url, open_api, cp_url, ows_url, "cmatest", "123Qwe@@", "joonyin.yap", "123Qwe@@","testcrm dap_auto"}};
        }

        if ("vt".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrm_dap_auto@testcrm.com", "123Qwe@@", dap_url, admin_url, open_api, cp_url, ows_url, "cmatest", "123Qwe@@", "joonyin.yap", "123Qwe@@","testcrm dap_auto"}};
        }
        if ("pug".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrm_dap_auto@testcrm.com", "123Qwe@@", dap_url, admin_url, open_api, cp_url, ows_url, "cmatest", "123Qwe@@", "joonyin.yap", "123Qwe@@","testcrm dap_auto"}};
        }
        if ("um".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrm_dap_auto@testcrm.com", "123Qwe@@", dap_url, admin_url, open_api, cp_url, ows_url, "cmatest", "123Qwe@@", "joonyin.yap", "123Qwe@@","testcrm dap_auto"}};
        }
        if ("mo".equalsIgnoreCase(brand)) {
            return new Object[][]{{"VFSC", "testcrm_dap_auto@testcrm.com", "123Qwe@@", dap_url, admin_url, open_api, cp_url, ows_url, "cmatest", "123Qwe@@", "joonyin.yap", "123Qwe@@","testcrm dap_auto"}};
        }
        if ("star".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrm_dap_auto@testcrm.com", "123Qwe@@", dap_url, admin_url, open_api, cp_url, ows_url, "cmatest", "123Qwe@@", "joonyin.yap", "123Qwe@@","testcrm dap_auto"}};
        }
        if ("vjp".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrm_dap_auto@testcrm.com", "123Qwe@@", dap_url, admin_url, open_api, cp_url, ows_url, "cmatest", "123Qwe@@", "joonyin.yap", "123Qwe@@","testcrm dap_auto"}};
        }

        return null;
    }

    /**
     * 获取alpha credit in测试账号
     *
     * @param brand
     * @param server
     * @return
     */
    public static Object[][] getAlphaCreditInUsersData(String brand, String server) {
        String cp_url = AlphaServerEnv.getCpUrl(server);
        String admin_url = AlphaServerEnv.getAdminUrl(server);
        String open_api = AlphaServerEnv.getOpenApiUrl(server);
        String ib_url = AlphaServerEnv.getIBUrl(server);

        if ("vt".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrmvtcredit@mailinator.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe"}};
        }

        if ("pug".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrmpucredit@mailinator.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe"}};
        }

        if ("vfx".equalsIgnoreCase(brand) || "au".equalsIgnoreCase(brand)) {
            return new Object[][]{{"VFSC2", "testcrmapiaucredit@mailinator.com", "123Qwe!!", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe"}};
        }

        if ("um".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrmumcredit@mailinator.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe"}};
        }

        if ("mo".equalsIgnoreCase(brand)) {
            return new Object[][]{{"VFSC", "testcrmocredit@mailinator.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe"}};
        }

        if ("star".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrmstcredit@mailinator.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe"}};
        }

        if ("vjp".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrmvjpcredit@mailinator.com", "123Qwe!!", cp_url, admin_url, open_api, ib_url, "cmatest", "1234Qwer!"}};
        }

        return null;
    }

    public static Object[][] getAlphaCCallBackUsersData(String brand, String server) {
        String cp_url = AlphaServerEnv.getCpUrl(server);
        String admin_url = AlphaServerEnv.getAdminUrl(server);
        String open_api = AlphaServerEnv.getOpenApiUrl(server);
        String ib_url = AlphaServerEnv.getIBUrl(server);
        brand = brand.trim();
        if ("vt".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrmbridgepaycpscallback@mailinator.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe", "testcrm_ibwdauto1@testcrm.com", "123Qwe@@"}};

        }

        if ("pug".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrmbridgepaycpscallback@mailinator.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe", "testcrmibwhitni01@testcrm.com", "123Qwe@@"}};
        }

        if ("vfx".equalsIgnoreCase(brand) || "au".equalsIgnoreCase(brand)) {
            return new Object[][]{{"VFSC", "testcrmbridgepaycpscallback@mailinator.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe", "testcrm_ibwdauto@testcrm.com", "123Qwe@@"}};

        }

        if ("um".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrmbridgepaycpscallback@mailinator.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe", "testcrm_ibwdauto1@testcrm.com", "123Qwe@@"}};
        }
        if ("mo".equalsIgnoreCase(brand)) {
            return new Object[][]{{"VFSC", "testcrmbridgepaycpscallback@mailinator.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe", "testcrm_ibwdauto1@testcrm.com", "123Qwe@@"}};
        }
        if ("star".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "testcrmbridgepaycpscallback@mailinator.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "123Qwe", "testcrm_ibwdauto2@testcrm.com", "123Qwe@@"}};
        }
        if ("vjp".equalsIgnoreCase(brand)) {
            return new Object[][]{{"SVG", "vjptestautomation@mailinator.com", "123Qwe@@", cp_url, admin_url, open_api, ib_url, "cmatest", "1234Qwer!", "testcrmwkxJRyTRh@testcrm.com", "1234Qwer@"}};

        }
        return null;
    }
}
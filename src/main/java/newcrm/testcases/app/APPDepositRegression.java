package newcrm.testcases.app;

import com.alibaba.fastjson.JSONObject;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.utils.app.Deposit;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.assertTrue;

public class APPDepositRegression {
    @Test
    @Parameters(value= {"APPURL","Regulator","Brand","UserId","AccountNo"})
    public void cryptoBTCAPPDeposit(String host, String regulator, String brand, String userId, String accountNo) {
        String channel = "120";
        String channelName = "BTC_App_Cryptocurrency";
        int type = 15;
        sendSpecifiedDeposit(host, regulator, brand, userId, accountNo, "MT4", channel,channelName,type,false);
    }

    @Test
    @Parameters(value= {"APPURL","Regulator","Brand","UserId","AccountNo"})
    public void cryptoBTCCPSDeposit(String host, String regulator, String brand, String userId, String accountNo) {
        String channel = "143";
        String channelName = "BTC-CPS-APP";
        int type = 15;
        sendSpecifiedDeposit(host, regulator, brand, userId, accountNo, "MT4", channel, channelName,type,false);
    }

    @Test
    @Parameters(value= {"APPURL","Regulator","Brand","UserId","AccountNo"})
    public void usdtERCDeposit(String host, String regulator, String brand, String userId, String accountNo) {
        String channel = "145";
        String channelName = "USDT(ERC20)-CPS-APP";
        int type = 15;
        sendSpecifiedDeposit(host, regulator, brand, userId, accountNo, "MT4", channel,channelName,type,false);
    }

    @Test
    @Parameters(value= {"APPURL","Regulator","Brand","UserId","AccountNo"})
    public void usdtTRCDeposit(String host, String regulator, String brand, String userId, String accountNo) {
        String channel = "146";
        String channelName = "USDT(TRC20)-CPS-APP";
        int type = 15;
        sendSpecifiedDeposit(host, regulator, brand, userId, accountNo, "MT4", channel, channelName,type,false);
    }

    @Test
    @Parameters(value= {"APPURL","Regulator","Brand","UserId","AccountNo"})
    public void ccDeposit(String host, String regulator, String brand, String userId, String accountNo) {
        String channel = "101";
        String channnelName = "Credit Card-APP";
        int type = 1;
        sendSpecifiedDeposit(host, regulator, brand, userId, accountNo, "MT4", channel, channnelName,type,true);
    }

    @Test
    @Parameters(value= {"APPURL","Regulator","Brand","UserId","AccountNo"})
    public void bridgeAPPCCDeposit(String host, String regulator, String brand, String userId, String accountNo) {
        String channel = "116";
        String channnelName = "CC-BRIDGER-APP";
        int type = 1;
        sendSpecifiedDeposit(host, regulator, brand, userId, accountNo, "MT4", channel, channnelName,type,true);
    }
    @Test
    @Parameters(value= {"APPURL","Regulator","Brand","UserId","AccountNo"})
    public void virtualPayAPPCCDeposit(String host, String regulator, String brand, String userId, String accountNo) {
        String channel = "132";
        String channnelName = "VirtualPay-APP";
        int type = 1;
        sendSpecifiedDeposit(host, regulator, brand, userId, accountNo, "MT4", channel, channnelName,type,true);
    }
    @Test
    @Parameters(value= {"APPURL","Regulator","Brand","UserId","AccountNo"})
    public void powercashAPPCCDeposit(String host, String regulator, String brand, String userId, String accountNo) {
        String channel = "137";
        String channnelName = "POWERCASH-APP";
        int type = 1;
        sendSpecifiedDeposit(host, regulator, brand, userId, accountNo, "MT4", channel, channnelName,type,true);
    }
    @Test
    @Parameters(value= {"APPURL","Regulator","Brand","UserId","AccountNo"})
    public void googlePayAPPCCDeposit(String host, String regulator, String brand, String userId, String accountNo) {
        String channel = "187";
        String channnelName = "GOOGLEPAY-APP";
        int type = 1;
        sendSpecifiedDeposit(host, regulator, brand, userId, accountNo, "MT4", channel, channnelName,type,true);
    }

    @Test
    @Parameters(value= {"APPURL","Regulator","Brand","UserId","AccountNo"})
    public void applepayAPPCCDeposit(String host, String regulator, String brand, String userId, String accountNo) {
        String channel = "186";
        String channnelName = "APPLEPAY-APP";
        int type = 1;
        sendSpecifiedDeposit(host, regulator, brand, userId, accountNo, "MT4", channel, channnelName,type,true);
    }

    @Test
    @Parameters(value= {"APPURL","Regulator","Brand","UserId","AccountNo"})
    public void fasaCPSAPPDeposit(String host, String regulator, String brand, String userId, String accountNo) {
        String channel = "324";
        String channnelName = "Fasapay-CPS-APP";
        int type = 7;
        sendSpecifiedDeposit(host, regulator, brand, userId, accountNo, "MT4", channel, channnelName,type,false);
    }

    @Test
    @Parameters(value= {"APPURL","Regulator","Brand","UserId","AccountNo"})
    public void skrillCPSAPPDeposit(String host, String regulator, String brand, String userId, String accountNo) {
        String channel = "317";
        String channnelName = "Skrill-CPS-APP";
        int type = 21;
        sendSpecifiedDeposit(host, regulator, brand, userId, accountNo, "MT4", channel, channnelName,type,false);
    }
    @Test
    @Parameters(value= {"APPURL","Regulator","Brand","UserId","AccountNo"})
    public void perfectMoneyAPPDeposit(String host, String regulator, String brand, String userId, String accountNo) {
        String channel = "170";
        String channnelName = "Perfect Money-APP";
        int type = 49;
        sendSpecifiedDeposit(host, regulator, brand, userId, accountNo, "MT4", channel, channnelName,type,false);
    }


    @Test
    @Parameters(value= {"APPURL","Regulator","Brand","UserId","AccountNo"})
    public void bitoloCPSDeposit(String host, String regulator, String brand, String userId, String accountNo) {
        String channel = "6";
        String channnelName = "Bitolo-CPS";
        int type = 26;
        sendSpecifiedDeposit(host, regulator, brand, userId, accountNo, "MT4", channel, channnelName,type,false);
    }

    @Test
    @Parameters(value= {"APPURL","Regulator","Brand","UserId","AccountNo"})
    public void thailandBTCPSDeposit(String host, String regulator, String brand, String userId, String accountNo) {
        String channel = "309";
        String channnelName = "Thailand-Zotapay-CPS-APP";
        int type = 4;
        sendSpecifiedDeposit(host, regulator, brand, userId, accountNo, "MT4", channel, channnelName,type,false);
    }

    @Test
    @Parameters(value= {"APPURL","Regulator","Brand","UserId","AccountNo"})
    public void malaysiaBTCPSDeposit(String host, String regulator, String brand, String userId, String accountNo) {
        String channel = "475";
        String channnelName = "Malaysia-Banking-CPS-APP";
        int type = 5;
        sendSpecifiedDeposit(host, regulator, brand, userId, accountNo, "MT4", channel, channnelName,type,false);
    }

    @Test
    @Parameters(value= {"APPURL","Regulator","Brand","UserId","AccountNo"})
    public void vietnamBTCPSAPPDeposit(String host, String regulator, String brand, String userId, String accountNo) {
        String channel = "311";
        String channnelName = "Vietnam-Zotapay-CPS-APP";
        int type = 8;
        sendSpecifiedDeposit(host, regulator, brand, userId, accountNo, "MT4", channel, channnelName,type,false);
    }

    @Test
    @Parameters(value= {"APPURL","Regulator","Brand","UserId","AccountNo"})
    public void nigeriaBTCPSDeposit(String host, String regulator, String brand, String userId, String accountNo) {
        String channel = "382";
        String channnelName = "Nigeria-Paystack-CPS-APP";
        int type = 12;
        sendSpecifiedDeposit(host, regulator, brand, userId, accountNo, "MT4", channel, channnelName,type,false);
    }

    @Test
    @Parameters(value= {"APPURL","Regulator","Brand","UserId","AccountNo"})
    public void southAfricaBTCPSAPPDeposit(String host, String regulator, String brand, String userId, String accountNo) {
        String channel = "472";
        String channnelName = "SouthAfrica-Ozow-CPS-APP";
        int type = 29;
        sendSpecifiedDeposit(host, regulator, brand, userId, accountNo, "MT4", channel, channnelName,type,false);
    }

    public void sendSpecifiedDeposit(String host, String regulator, String brand, String userId, String accountNo, String platform,String depositType,String channelName,int type,boolean isCC) {
        regulator = regulator.toUpperCase();
        brand = brand.toUpperCase();
        Deposit deposit =new Deposit(host,regulator,brand);
        String extend2 = "";
        String dpType = depositType;
        Double amount = 50.00;
        String cardBeginSixDigits = "545432";
        String cardLastFourDigits = "5555";
        String  cardHolderName= "APP API testCRM";

        String result = null;

        GlobalMethods.printDebugInfo("userId:" + userId + " accountNo: " + accountNo);
        if(isCC)
        {
            extend2 = "545433,6666,06,2028,1,testcrmdontuse test dadsaautomation";
        }
        result = deposit.sendDeposit(String.valueOf(amount), BigDecimal.valueOf(1),
                BigDecimal.valueOf(amount), accountNo, userId, platform, dpType,
                "545433", "6666", "APP API testCRM",
                "06", "2028", "1", "", extend2, "");

        System.out.println(result);

        JSONObject obj = JSONObject.parseObject(result);
        Integer rescode = obj.getInteger("code");
        assertTrue(rescode.equals(1000) , "APP API deposit failed!! \n"+result);

        JSONObject payment = null;
        //find order number in DB
        payment = deposit.getDepositRecord(GlobalProperties.ENV.ALPHA,
                GlobalProperties.BRAND.valueOf(brand), GlobalProperties.REGULATOR.valueOf(regulator),
                accountNo, type, Integer.valueOf(dpType));
        String orNum = payment.getString("order_number");

        //update deposit status
        result = deposit.updateStatus(String.valueOf(amount),BigDecimal.valueOf(1), BigDecimal.valueOf(amount),
                accountNo, userId, dpType, platform, orNum,"1",cardBeginSixDigits,cardLastFourDigits,cardHolderName);
        System.out.println("deposit order:" + result);

        obj = JSONObject.parseObject(result);
        rescode = obj.getInteger("code");
        assertTrue(rescode.equals(1134) , "APP API update deposit status failed!! \n"+result);

        result = deposit.updateStatus(String.valueOf(amount), BigDecimal.valueOf(1), BigDecimal.valueOf(amount),
                    accountNo, userId, dpType, platform, orNum, "3",cardBeginSixDigits,cardLastFourDigits,cardHolderName);

        System.out.println("deposit action" + result);

        obj = JSONObject.parseObject(result);
        rescode = obj.getInteger("code");
        assertTrue(rescode.equals(1000) , "APP API update deposit status failed!! \n"+result + "deposit channel:" + depositType
                + " type:" + type);

        GlobalMethods.printDebugInfo("deposit channel:" + depositType + " channelName:" + channelName+ " type:" + type );
    }
}

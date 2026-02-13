package newcrm.utils.callback;

import newcrm.utils.encryption.EncryptUtil;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HashMap;

public class CPSCallback extends CallbackBase{
    private final String path = "cps/manual/notify/deposit";
    @Override
    protected void generateRequest() {
        // TODO Auto-generated method stub
        this.jsonBodyHeader();
        String sign = "deposit&"+orderNum+ "&1126869001507979264&"+currency + "&null&" + amount + "&null&0000&success&null&1688708583&REMARK&" + key;
        String secStr = EncryptUtil.MD5Encode(sign, StandardCharsets.UTF_8.name());

        body = "{"
                + "    \"transaction_type\":\"deposit\","
                + "    \"order_id\":\""+orderNum+"\","
                + "    \"transaction_order_id\":\"1126869001507979264\","
                + "    \"order_currency\":\""+currency+"\","
                + "    \"actual_currency\":null,"
                + "    \"order_amount\":"+amount+","
                + "    \"actual_amount\":null,"
               // + "    \"user_id\":null,"
                //+ "    \"payment_expiry\":null,"
                + "    \"transaction_status\":\"0000\","
                + "    \"transaction_message\":\"success\","
                + "    \"variable\":null,"
                + "    \"timestamp\":\"1688708583\","
                + "    \"remarks\":\"REMARK\","
                + "    \"sign\":\""+secStr+"\""
                + "}";

    }

    @Override
    protected void setFullpath() {
        // TODO Auto-generated method stub

        this.fullPath = url+path;
    }

    @Override
    protected void setKey() {
        // TODO Auto-generated method stub

        // key from Apollo biz-payment cps_key
        if("VT".equalsIgnoreCase(brand)) {
            key = "7152ccd53be39db53345a69838119fa2167cc547";//VT SVG
        }else if("STAR".equalsIgnoreCase(brand))
        {
        	key = "beab72c20c9488230ffcaa633eeff4084df8784c";
        }
        else if("PUG".equalsIgnoreCase(brand))
        {
            if("FSA".equalsIgnoreCase((regulator)))
            {
                key="32331ea9851957e4783d5ac479b8d3d246244d61";
            }
            else {
                key = "c60fff04688f46f776a6183d69a828ec92fa21ec";
            }
        }else if("VFX".equalsIgnoreCase(brand))
        {
            if("ASIC".equalsIgnoreCase(regulator)) {
                key = "d8d71fc21d3bc82579dc57245625f6c027f5a0e6";
            }else
                if("CIMA".equalsIgnoreCase(regulator)){
                key = "61e7bb64f00dd7d323e50a958f9001e00a64b483";
            }else
                if("VFSC".equalsIgnoreCase(regulator))
            {
                key="f5eb24b2f5ac16a5c22991d1e0d5f79aeeefb7ba";
            }
           else if("VFSC2".equalsIgnoreCase(regulator))
            {
            key = "d1973e2965aaa6e30cc5e1676e316db59a9ada9a";
        }
        else if("FCA".equalsIgnoreCase(regulator))
        {
            key="595bc9849b2a7c9e9aa7df1dfcd46f0e190cf076";
        }
        }
        else if("UM".equalsIgnoreCase(brand))
        {
            key="36b33eedb98140ac5a70baee48ec03f62da42ff7";
        }
        else if("MO".equalsIgnoreCase(brand))
        {
            key="6f24886258e15d22b1a0d26820d74235c79f5e00";
        }
        else if("STAR".equalsIgnoreCase(brand))
        {
            key="beab72c20c9488230ffcaa633eeff4084df8784c";
        }
        else if("VJP".equalsIgnoreCase(brand))
        {
            key="f5eb24b2f5ac16a5c22991d1e0d5f79aeeefb7ba";
        }
    }

}

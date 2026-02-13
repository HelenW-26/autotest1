package newcrm.utils.callback;

import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.utils.encryption.EncryptUtil;

import java.nio.charset.StandardCharsets;

public class PTCallBack extends CallbackBase{

    private final String path = "cps/pt/manual/notify/deposit";
    @Override
    protected void generateRequest() {
        // TODO Auto-generated method stub
        this.jsonBodyHeader();

        body = "{"
                + "    \"order_id\":\""+orderNum+"\","
                + "    \"transaction_status\":\"0000\","
                + "    \"order_amount\":\""+amount+"\""
                + "}";

        GlobalMethods.printDebugInfo("body:" + body);
    }

    @Override
    protected void setFullpath() {
        // TODO Auto-generated method stub

        this.fullPath = url+path;
    }

    @Override
    protected void setKey() {

    }


}

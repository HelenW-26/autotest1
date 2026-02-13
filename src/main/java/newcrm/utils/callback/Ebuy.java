package newcrm.utils.callback;


import java.util.HashMap;


import newcrm.utils.encryption.EncryptUtil; 

public class Ebuy extends CallbackBase {

    private final String path ="ebuy/receiveWebDepositResult";
    @Override
    protected void generateRequest(){

      
		this.jsonBodyHeader();
        
       String hash1= amount+key+orderNum;
       String hash =EncryptUtil.MD5(hash1);
        String  orgString = "invoice_id:"+orderNum+"status:"+"success"+"amount:"+amount+"marketCode:"+"pug7he9"+"currency:"+currency+"hash:"+hash;
        body ="{"
               + "\"invoice_id\":\""+orderNum+"\","
               + "\"status\":\"success\","
               + "\"amount\":\""+amount+"\","
               + "\"marketCode\":\"pug7he9\","
               + "\"currency\":\""+currency+"\","
               + "\"hash\":\""+hash+"\""
               + "}";
               String sign = EncryptUtil.MD5(orgString);
               headerMap.put("signature", sign);    
           
    }

    @Override
  protected void setFullpath() {
    // TODO Auto-generated method stub

    this.fullPath = url+path;
  }

    @Override
  protected void setKey() {
    // TODO Auto-generated method stub
    key = "b4373CEaC7527B6BeeCaFBa9cB7c08654215074BDfA8Ede7Adae3B74838C3FF8";
  }

    

}

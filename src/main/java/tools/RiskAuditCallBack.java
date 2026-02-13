package tools;

import lombok.Getter;
import lombok.Setter;
import utils.LogUtils;

public class RiskAuditCallBack {
    // Getters and Setters
    @Setter
    @Getter
    private String url;
    private String env;
    @Setter
    @Getter
    private String brand;
    @Setter
    @Getter
    private String server;
    @Setter
    @Getter
    private String regulator;
    @Setter
    @Getter
    private String transferId;
    @Setter
    @Getter
    private String orderNum;
    @Setter
    @Getter
    private String userId;

    // 构造函数
    public RiskAuditCallBack(String env,String server,String brand, String regulator, String transferId, String orderNum) {
        this.env = env;
        if(env.equalsIgnoreCase("alpha")){
            this.url = "https://openapi-{server}.crm-alpha.com/api/risk/approval".replace("{server}", server);        }
        if(env.equalsIgnoreCase("uat")){
           this.url = "https://paymentgw.eks.bit-beta.com/payment-open-api/bsn/open-api/riskAudit/approval";
        }
        if(brand.equalsIgnoreCase("vfx")){
            brand = "AU";
        }
        if(brand.equalsIgnoreCase("mo")){
            brand = "MONETA";
        }
        this.brand = brand.toUpperCase();
        this.regulator = regulator;
        this.transferId = transferId;
        this.orderNum = orderNum;
        LogUtils.info("Risk audit callback url: " + url);
        LogUtils.info("Risk audit callback brand: " + brand);
        LogUtils.info("Risk audit callback regulator: " + regulator);
        LogUtils.info("Risk audit callback transferId: " + transferId);
        LogUtils.info("Risk audit callback orderNum: " + orderNum);
        LogUtils.info("Risk audit callback env: " + env);

    }
    public RiskAuditCallBack(String env,String brand, String regulator, String userId, String orderNum){
        String path = "/payment-open-api/bsn/risk/src-review/withdrawal/callback/ingress";
        this.env = env;
        if(env.equalsIgnoreCase("alpha")){
            this.url = "https://payment-gw.bit-eks.crm-alpha.com"+ path;        }
        if(env.equalsIgnoreCase("uat")){
            this.url = "https://paymentgw.eks.bit-beta.com"+ path;
        }
        if(brand.equalsIgnoreCase("vfx")){
            brand = "AU";
        }
        this.brand = brand.toUpperCase();
        this.regulator = regulator;
        this.userId = userId;
        this.orderNum = orderNum;
        LogUtils.info("Risk audit callback url: " + url);
        LogUtils.info("Risk audit callback brand: " + brand);
        LogUtils.info("Risk audit callback regulator: " + regulator);
        LogUtils.info("Risk audit callback userId: " + userId);
        LogUtils.info("Risk audit callback orderNum: " + orderNum);
        LogUtils.info("Risk audit callback env: " + env);
    }

}

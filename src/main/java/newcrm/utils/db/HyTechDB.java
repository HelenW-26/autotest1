package newcrm.utils.db;


import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import cn.hutool.db.ds.simple.SimpleDataSource;
import lombok.extern.slf4j.Slf4j;
import newcrm.utils.api.HytechDBUrl;
import utils.LogUtils;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.*;


/**
 * 获取数据库链接
 */
@Slf4j
public class HyTechDB {
    /**
     * global数据库
     */
    public static final String test_global_business_url = "db01.crm-alpha.com";
    public static final String test_global_business_username = "alpha_business_rw";
    public static final String test_global_business_password = "LsiDHEAzA4W1jtJG8GWL9eDA0iHu58RZ";
    public static Map<String,String> dbName = new HashMap();

    static {
        dbName.put("AU.ASIC","dev_m_regulator_asic");
        dbName.put("AU.CIMA","dev_m_regulator_cima");
        dbName.put("AU.VFSC","dev_m_regulator_vfsc");
        dbName.put("AU.VFSC2","dev_m_regulator_vfsc2");
        dbName.put("AU.FCA","dev_m_regulator_fca");
        dbName.put("AU.Global","dev_m_regulator_global");
    }

    /**
     * mt4同步
     *reportdb01.crm-alpha.com
     * alpha_business_rw
     * LsiDHEAzA4W1jtJG8GWL9eDA0iHu58RZ
     */
    public static final String test_mt4_url = "reportdb01.crm-alpha.com";
    public static final String test_mt4_username = "alpha_business_rw";
    public static final String test_mt4_password = "LsiDHEAzA4W1jtJG8GWL9eDA0iHu58RZ";

    /**
     * mt5同步库
     * mt5testdb01.crm-alpha.com
     * alpha_mt5_r
     * 0Mmw795.Dhew-%:dRzT)q%MPR(8l8Y
     */
    public static final String test_mt5_url = "mt5testdb01.crm-alpha.com";
    public static final String test_mt5_username = "alpha_mt5_r";
    public static final String test_mt5_password = "0Mmw795.Dhew-%:dRzT)q%MPR(8l8Y";

    /**
     * sws doris
     */
    public static final String test_doris_url = "doris-db01.crm-alpha.com";
    public static final String test_doris_username = "alpha_rw";
    public static final String test_doris_password = "2JBod@VB8GvZ7yL9W-NV";


    public static Db getDorisDb(String dbName){
        DataSource ds = new SimpleDataSource("jdbc:mysql://"+test_doris_url+":9030/"+dbName,test_doris_username,test_doris_password);
        return  Db.use(ds);
    }

    /**
     * 根据品牌和监管查找合适的数据库
     * @param regulator
     * @param brand
     * @return
     */
    public static String getBusiDburl(String regulator,String brand){
        String dburl = "";
        if(brand.equals("AU") || brand.equals("VFX")){
            switch (regulator){
                case "VFSC":
                    dburl = HytechDBUrl.AU_VSFC_BUSI;
                    break;
                case "VFSC2":
                    dburl = HytechDBUrl.AU_VSFC2_BUSI;
                    break;
                case "FCA":
                    dburl = HytechDBUrl.AU_FCA_BUSI;
                    break;
                default:
                    dburl = HytechDBUrl.AU_ASIC_BUSI;
            }
        }else if(brand.equals("VT")){
            dburl = HytechDBUrl.VT_SVG_BUSI;
        }else if (brand.equals("PU")){
            switch (regulator){
                case "SVG":
                    dburl = HytechDBUrl.PU_SVG_BUSI;
                    break;
                case "ASIC":
                    dburl = HytechDBUrl.PU_ASIC_BUSI;
                    break;
                default:
                    dburl = HytechDBUrl.PU_FSA_BUSI;
                    break;
            }
        }else if(brand.equals("MO")){
            dburl = HytechDBUrl.MO_VFSC_BUSI;
        } else if (brand.equals("UM")) {
            dburl = HytechDBUrl.UM_SVG_BUSI;
        }else if (brand.equals("RG")) {
            dburl = HytechDBUrl.RG_FMA_BUSI;
        }else if (brand.equals("AT")) {
            dburl = HytechDBUrl.AT_SVG_BUSI;
        }else if (brand.equals("STAR")) {
            switch (regulator){
                case "ASIC":
                    dburl = HytechDBUrl.STAR_ASIC_BUSI;
                    break;
                default:
                    dburl = HytechDBUrl.STAR_SVG_BUSI;
                    break;
            }
        }else if (brand.equals("VJP")) {
            dburl = HytechDBUrl.VJP_SVG_BUSI;
        }
        return dburl;
    }

    /**
     * 根据品牌和监管查找gloabal数据库
     * @param brand
     * @return
     */
    public static String getGlobalDburl(String brand){
        String dburl = "";
        switch (brand){
            case "AU":
                dburl = HytechDBUrl.AU_GLOBAL;
                break;
            case "PU":
                dburl = HytechDBUrl.PU_GLOBAL;
                break;
            case "VT":
                dburl = HytechDBUrl.VT_GLOBAL;
                break;
            case "VJP":
                dburl = HytechDBUrl.VJP_GLOBAL;
                break;
            case "MO":
                dburl = HytechDBUrl.MO_GLOBAL;
                break;
            case "STAR":
                dburl = HytechDBUrl.STAR_GLOBAL;
                break;
            case "UM":
                dburl = HytechDBUrl.UM_GLOBAL;
                break;
            default:
                dburl = HytechDBUrl.AU_GLOBAL;
        }

        LogUtils.info("得到的gloabal数据库地址是：" + dburl);
        return dburl;
    }


    public static List<Entity> getDorisDbQuery(String dbName,String sql){
        DataSource ds = new SimpleDataSource("jdbc:mysql://"+test_doris_url+":9030/"+dbName,test_doris_username,test_doris_password);
        System.out.println("jdbc:mysql://"+test_doris_url+":9030/"+dbName);
        Db db = Db.use(ds);
        try {
            List<Entity> query = db.query(sql);
            return query;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static Db getBusinessDb(String dbName){
        DataSource ds = new SimpleDataSource("jdbc:mysql://"+test_global_business_url+":3306/"+dbName,test_global_business_username,test_global_business_password);
        return  Db.use(ds);
    }

    public static List<Entity> getBusinessDbQuery(String dbName,String sql){
        DataSource ds = new SimpleDataSource("jdbc:mysql://"+test_global_business_url+":3306/"+dbName,test_global_business_username,test_global_business_password);
        Db db = Db.use(ds);
        try {
            List<Entity> query = db.query(sql);
            return query;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 多条件查询数据库数据并返回所有字段
     * @param dbName
     * @param tableName
     * @param condition
     * @return
     */

    public static List<Map<String,Object>> queryBusinessData(String dbName,String tableName,String... condition){
        //"select * from tb_user where id = 58027
        List<Map<String,Object>> list = new ArrayList<>();
        DataSource ds = new SimpleDataSource("jdbc:mysql://"+test_global_business_url+":3306/"+dbName,test_global_business_username,test_global_business_password);
        Db db = Db.use(ds);
        String sql = null;
        if(condition.length > 1){
            String conditionStr = condition[0];
            for(int i=1;i<condition.length;i++){
                conditionStr = conditionStr + " and " + condition[i];
            }
            sql = "select * from " + tableName +  " where " + conditionStr;
        }else{
            sql = "select * from " + tableName ;
        }
        LogUtils.info("查询语句是：" + sql);
        try {
            List<Entity> query = db.query(sql);
            if(query == null || query.size() == 0){
                LogUtils.info("Error：未查询到数据，请重新检查sql！");
                return Collections.emptyList();
            }
            for(Entity e: query){
                Map<String,Object> map = new HashMap<>();
                Set<String> fn = e.getFieldNames();
                for(String k:fn){
                    map.put(k,e.get(k));
                }
                list.add(map);
            }
            LogUtils.info("数据库查询结果：" + list.toString());
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 查询指定字段的值
     * @param dbName
     * @param tableName
     * @param field
     * @param condition
     * @return
     */
    public static Object queryField(String dbName,String tableName,String field,String... condition){
        List<Map<String,Object>> list = new ArrayList<>();
        DataSource ds = new SimpleDataSource("jdbc:mysql://"+test_global_business_url+":3306/"+dbName,test_global_business_username,test_global_business_password);
        Db db = Db.use(ds);
        String sql = null;
        if(condition.length > 1){
            String conditionStr = condition[0];
            for(int i=1;i<condition.length;i++){
                conditionStr = conditionStr + " and " + condition[i];
            }
            sql = "select " + field + " from " + tableName +  " where " + conditionStr;
        }else{
            sql = "select " + field + " from " + tableName +  " where " + condition[0];
        }
        LogUtils.info("查询语句是：" + sql);
        try {
            List<Entity> query = db.query(sql);
            if(query == null || query.size() == 0){
                LogUtils.info("Error：未查询到数据，请重新检查sql！");
                return Collections.emptyList();
            }
            for(Entity e: query){
                Map<String,Object> map = new HashMap<>();
                Set<String> fn = e.getFieldNames();
                for(String k:fn){
                    map.put(k,e.get(k));
                }
                list.add(map);
            }
            LogUtils.info("数据库查询结果：" + list.toString());
            return list.get(0).get(field);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 查询多条指定字段的值
     * @param dbName
     * @param tableName
     * @param field
     * @param condition
     * @return
     */
    public static List<String> queryFieldList(String dbName,String tableName,String field,String... condition){
        List<Map<String,Object>> list = new ArrayList<>();
        List<String> fieldList = new ArrayList<>();
        DataSource ds = new SimpleDataSource("jdbc:mysql://"+test_global_business_url+":3306/"+dbName,test_global_business_username,test_global_business_password);
        Db db = Db.use(ds);
        String sql = null;
        if(condition.length > 1){
            String conditionStr = condition[0];
            for(int i=1;i<condition.length;i++){
                conditionStr = conditionStr + " and " + condition[i];
            }
            sql = "select " + field + " from " + tableName +  " where " + conditionStr;
        }else{
            sql = "select " + field + " from " + tableName ;
        }
        LogUtils.info("查询语句是：" + sql);
        try {
            List<Entity> query = db.query(sql);
            if(query == null || query.size() == 0){
                LogUtils.info("Error：未查询到数据，请重新检查sql！");
                return Collections.emptyList();
            }
            for(Entity e: query){
                Map<String,Object> map = new HashMap<>();
                Set<String> fn = e.getFieldNames();
                for(String k:fn){
                    map.put(k,e.get(k));
                }
                list.add(map);

            }

            for(Map<String,Object> m:list){
                fieldList.add(m.get(field).toString());
            }
            LogUtils.info("数据库查询结果：" + fieldList);
            return fieldList;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 更新数据库字段
     * @param dbName
     * @param tableName
     * @param field
     * @param newValue
     * @param condition
     */
    public static void updateBusinessDbQuery(String dbName,String tableName,String field,String newValue,String... condition){
        DataSource ds = new SimpleDataSource("jdbc:mysql://"+test_global_business_url+":3306/"+dbName,test_global_business_username,test_global_business_password);
        Db db = Db.use(ds);
        String sql;
        if(condition.length > 1){
            String conditionStr = condition[0];
            for(int i=1;i<condition.length;i++){
                conditionStr = conditionStr + " and " + condition[i];
            }
            sql = "update " + tableName  + " set " + field + "='" + newValue + "' where " + conditionStr;
        }else{
            sql = "update " + tableName  + " set " + field + "='" + newValue + "' where " + condition[0];
        }
        LogUtils.info("更新语句是：" + sql);
        try {
            int n = db.execute(sql);
            LogUtils.info( "更新数据成功:" + n);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static Db getMt4Db(String dbName){
        DataSource ds = new SimpleDataSource("jdbc:mysql://"+test_mt4_url+":3306/"+dbName,test_mt4_username,test_mt4_password);
        return  Db.use(ds);
    }


    public static List<Entity> getMt4DbQuery(String dbName,String sql){
        DataSource ds = new SimpleDataSource("jdbc:mysql://"+test_mt4_url+":3306/"+dbName,test_mt4_username,test_mt4_password);
        Db db = Db.use(ds);
        try {
            List<Entity> query = db.query(sql);
            return query;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Db getMt5Db(String dbName){
        DataSource ds = new SimpleDataSource("jdbc:mysql://"+test_mt5_url+":3306/"+dbName,test_mt5_username,test_mt5_password);
        return  Db.use(ds);
    }

    public static List<Entity> getMt5DbQuery(String dbName,String sql){
        DataSource ds = new SimpleDataSource("jdbc:mysql://"+test_mt5_url+":3306/"+dbName,test_mt5_username,test_mt5_password);
        Db db = Db.use(ds);
        try {
            List<Entity> query = db.query(sql);
            return query;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void test(String... args) {
        for(String arg : args) {
            System.out.println(arg);
        }
    }


    public static void main(String[] args) {
//        Db Db = getBusinessDb("dev_um_m_regulator_global");
        try {
//            List<Entity> SS = Db.query("select * from tb_alpha_config where id = 44",new HashMap<>());
//            System.out.println(SS);
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//////        }

//            Object list = queryField("dev_m_regulator_vfsc2","tb_account_mt4_demo","mt4_account","user_id ='10065682'","is_first_account=1");
            List<String> list = queryFieldList("dev_m_regulator_global","tb_country","country_name_en");
            System.out.println(list.toString());
//
//            List<Map<String,Object>> list1 = queryBusinessData("dev_m_regulator_vfsc2","tb_account_mt4_demo","user_id ='10065682' and is_first_account=1");
//            System.out.println(list1.get(0));

//            updateBusinessDbQuery(HytechDBUrl.AU_VSFC2_BUSI,"tb_account_mt4_demo",  "create_user", "test","id =9360","user_id='10065682'");

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}

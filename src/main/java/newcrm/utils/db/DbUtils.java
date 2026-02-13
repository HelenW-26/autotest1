package newcrm.utils.db;

import java.sql.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONValidator;

import io.qameta.allure.Allure;
import newcrm.global.GlobalProperties.BRAND;
import newcrm.global.GlobalProperties.ENV;
import newcrm.global.GlobalProperties.REGULATOR;
import utils.LogUtils;
import vantagecrm.RestAPI;

import static java.util.Map.entry;

/**
 * 连接alpha和prod数据库的基本类
 * 目前prod的线上数据分两块，KCM在AWS，其余在Ali云，两个访问方式稍有不同。AwsDb实现了对AWS的访问
 * @author FengLiu
 *
 */
public class DbUtils {

	//this value you could copy from resource/newcrm/data/db_json.json
	//当线上数据都转义到AWS后，修改db_json.json，并拷贝到这里
	private final String dbs = """
{
  "ALPHA": {
    "PUG": {"SVG":"dev_pug_m_reg_svg_business","FSA":"dev_pug_m_reg_fsa_business","ASIC":"dev_pug_m_reg_asic_business","GLOBAL":"dev_pug_m_reg_fsa_global"},
    "VT": {"SVG":"dev_vt_m_svg_business","CIMA":"dev_vt_m_regulator_business","GLOBAL":"dev_vt_m_regulator_global"},
    "VFX": {"VFSC":"dev_m_regulator_vfsc","VFSC2":"dev_m_regulator_vfsc2","CIMA":"dev_m_regulator_cima","FCA":"dev_m_regulator_fca","ASIC":"dev_m_regulator_asic","GLOBAL":"dev_m_regulator_global"},
    "MO": {"VFSC":"mo_vfsc_business","GLOBAL":"mo_global"},
    "AT": {"SVG":"at_svg_business","GLOBAL":"at_global"},
    "POSP": {"ASIC":"prospero_asic_business","SVG":"prospero_svg_business","GLOBAL":"prospero_global"},
    "KCM": {"SVG":"dev_kcm_svg_business","GLOBAL":"dev_kcm_global"},
    "VDM": {"SVG":"vdm_svg_business","GLOBAL":"vdm_global"},
    "STAR": {"SVG":"dev_star_m_reg_svg_business","ASIC":"dev_star_m_reg_asic_business","SCA":"dev_star_m_reg_sca_business","GLOBAL":"dev_star_m_reg_global"},
    "VJP": {"SVG":"dev_vjp_m_svg_business","GLOBAL":"dev_vjp_m_global"},
    "UM": {"SVG":"dev_um_m_svg_business","GLOBAL":"dev_um_m_regulator_global"}
  },
  "PROD": {
    "PUG": {"INSTANCE":"PU+LIVE+%E6%A5%AD%E5%8B%99%E5%BA%AB","FSA":"pug_fsa_business","SVG":"pug_svg_business","ASIC":"pug_asic_business","GLOBAL":"pug_global"},
    "VT": {"INSTANCE":"VT+LIVE+%E6%A5%AD%E5%8B%99%E5%BA%AB","CIMA":"vt_business_db","SVG":"vt_svg_business","GLOBAL":"vt_global_db"},
    "VFX": {"INSTANCE":"AU+Aurora+%E6%A5%AD%E5%8B%99%E5%BA%AB%28NV%29","ASIC":"asic_business","CIMA":"cima_business","VFSC":"vfsc_business","VFSC2":"vfsc2_business","FCA":"fca_business","GLOBAL":"au_globale"},
    "MO": {"INSTANCE":"MO+LIVE+%E6%A5%AD%E5%8B%99%E5%BA%AB","VFSC":"mo_vfsc_business","GLOBAL":"mo_global"},
    "POSP": {"ASIC":"641","SVG":"653","GLOBAL":"642"},
    "AT": {"INSTANCE":"AT+LIVE+%E6%A5%AD%E5%8B%99%E5%BA%AB","SVG":"at_svg_business","GLOBAL":"at_global"},
    "VDM": {"INSTANCE":"VDM+LIVE+%E6%A5%AD%E5%8B%99%E5%BA%AB","SVG":"vdm_svg_business","GLOBAL":"vdm_global"},
    "KCM": {"INSTANCE":"kcm-prod-db01","SVG":"kcm_svg_business","GLOBAL":"kcm_global"},
    "VJP": {"INSTANCE":"VJP%20%E6%9D%B1%E4%BA%AC%20LIVE%20%E6%A5%AD%E5%8B%99%E5%BA%AB","SVG":"svg_business","GLOBAL":"vjp_global"},
    "STAR": {"INSTANCE":"STAR%20LIVE%20%E6%A5%AD%E5%8B%99%E5%BA%AB","SVG":"star_svg_business","ASIC":"star_asic_business","GLOBAL":"star_global"},
    "UM": {"INSTANCE":"UM+LIVE+%E6%A5%AD%E5%8B%99%E5%BA%AB","SVG":"um_svg_business","GLOBAL":"um_global"}
  },
  "UAT": {
    "PUG": {"SVG":"pug_svg_business","FSA":"pug_fsa_business","ASIC":"pug_asic_business","GLOBAL":"pug_global"},
    "VT": {"SVG":"vt_svg_business","CIMA":"vt_business_db","GLOBAL":"vt_global_db"},
    "VFX": {"VFSC":"vfsc_business","VFSC2":"vfsc2_business","CIMA":"cima_business","FCA":"fca_business","ASIC":"asic_business","GLOBAL":"au_globale"},
    "MO": {"VFSC":"mo_vfsc_business","GLOBAL":"mo_global"},
    "AT": {"SVG":"at_svg_business","GLOBAL":"at_global"},
    "POSP": {"ASIC":"prospero_asic_business","SVG":"prospero_svg_business","GLOBAL":"prospero_global"},
    "KCM": {"SVG":"dev_kcm_svg_business","GLOBAL":"dev_kcm_global"},
    "VDM": {"SVG":"vdm_svg_business","GLOBAL":"vdm_global"},
    "STAR": {"SVG":"star_svg_business","ASIC":"star_asic_business","SCA":"star_sca_business","GLOBAL":"star_global"},
    "VJP": {"SVG":"svg_business","GLOBAL":"vjp_global"},
    "UM": {"SVG":"um_svg_business","GLOBAL":"um_global"}
  }
}
""";
	
	private final ENV env;
	private final BRAND brand;
	private final REGULATOR regulator;
	private JSONObject jenv = null;
	
	private final String alphaUser="alpha_business_rw";
	private final String alphaPass="LsiDHEAzA4W1jtJG8GWL9eDA0iHu58RZ";
	private String alphaDB_url="jdbc:mysql://rm-j0b4x3vj64fm6tc4n9o.mysql.australia.rds.aliyuncs.com:3306/";

	private Map<String, String> brandUrls;
	private Map<String, String> brandPasswords;
	private Map<String, String> brandUsers;

	private Connection g_connect = null;
	private Connection r_connect = null;
	
	//AWS Brands list
	//Need to update the list as each brand completed AWS migration
	BRAND[] awsBrands = {BRAND.VT, BRAND.MO, BRAND.UM, BRAND.PUG, BRAND.VFX, BRAND.AT, BRAND.VDM, BRAND.STAR,BRAND.VJP};
	
	//for prod env use
	private String token=null;
	HashMap<String,String> headerMap;
	private final String url = "http://devops_new.tianyitechs.com/api/cmdb/database/db_execute_search/";
	private AwsDb awsdb=null;
	public DbUtils(ENV env,BRAND brand,REGULATOR regulator) {
		
		this.env = env;
		this.brand = brand;
		this.regulator = regulator;
		try {
			//MySQL upgrade to 8.0
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		jenv = ((JSONObject) JSON.parse(dbs)).getJSONObject(env.toString()).getJSONObject(this.brand.toString());
		LogUtils.info(String.format("DBUtils init:\n%s",jenv.toJSONString()));
		//For AWS brand, change alpha DB url to AWS
		if(Arrays.asList(awsBrands).contains(this.brand)) {
			alphaDB_url = "jdbc:mysql://db01.crm-alpha.com:3306/";
		};
		
	}
	
	private String getGlobalDB() {
		return jenv.getString("GLOBAL");
	}
	
	private String getRegulatorDB() {
		return jenv.getString(this.regulator.toString());
	}
	
	/**
	 * 获取AWS上的instance的名字
	 * @return
	 */
	private String getProdInstance() {
		return jenv.getString("INSTANCE");
	}
	
	
	/**
	 * 初始化数据库连接
	 */
	private void initialDBcon() {
		String prod_user="bit_qa_r";
		String prod_au_pass="Ifo!TQDsP5YwCcq-Eja0";
		String prod_au_url="jdbc:mysql://3.225.15.81:24001/";

		String prod_vt_pass="bdE#30Hy)vbOs9U&f=M&";
		String prod_vt_url="jdbc:mysql://3.225.15.81:24002/";

		String prod_pu_pass="j4ii_&v7Yv0d]0AJ8&V9";
		String prod_pu_url="jdbc:mysql://3.225.15.81:24003/";

		String prod_st_pass="E5_D6GTX0QX2jNXB0_#j";
		String prod_st_url="jdbc:mysql://3.225.15.81:24004/";

		String prod_vjp_pass="IY36ZBHf0VMc1AEO82HJDP";
		String prod_vjp_url="jdbc:mysql://3.225.15.81:24005/";

		String prod_mo_pass="fZEsP75Vi8n7DQ9WxM8t0N";
		String prod_mo_url="jdbc:mysql://3.225.15.81:24006/";

		String prod_um_pass="XAuaTSF60548TPZAH9O27T";
		String prod_um_url="jdbc:mysql://3.225.15.81:24007/";

		String uat_au_user="au_business_rw";
		String uat_au_pass="A2JEVPQAh3gv7XPJU6v0MCpjW6eZ2A";
		String uat_au_url="jdbc:mysql://3.225.15.81:30011/";

		String uat_vt_user="vt_business_rw";
		String uat_vt_pass="omlcpFSERqBVSbQ8T7uFKxxJl3NEok";
		String uat_vt_url="jdbc:mysql://3.225.15.81:30012/";

		String uat_pu_user="pug_business_rw";
		String uat_pu_pass="p0xdkt6M3XKuk9PvTtqYZrOM9lyF23";
		String uat_pu_url="jdbc:mysql://3.225.15.81:30013/";

		String uat_st_user="star_business_rw";
		String uat_st_pass="D62PHXK12fMoHUdPvn8UuvwGOQYhOZ";
		String uat_st_url="jdbc:mysql://3.225.15.81:30014/";

		String uat_vjp_user="vjp_business_rw";
		String uat_vjp_pass="rurG6LlLcDd7Q0caCJWGGoMcpRSgtn";
		String uat_vjp_url="jdbc:mysql://3.225.15.81:30015/";

		String uat_mo_user="mo_business_rw";
		String uat_mo_pass="ycqWQ4ueJzX7DywBU15U8ZXkb2SY7f";
		String uat_mo_url="jdbc:mysql://3.225.15.81:30016/";

		String uat_um_user="um_business_rw";
		String uat_um_pass="BUugbv9CN3DKeS6ZQ7lkzmZjLRZRl5";
		String uat_um_url="jdbc:mysql://3.225.15.81:30017/";

		brandUsers = Map.ofEntries(
				entry("alpha",  alphaUser),
				entry("vfx",    prod_user),
				entry("vt",     prod_user),
				entry("star",   prod_user),
				entry("pug",    prod_user),
				entry("vjp",    prod_user),
				entry("um",     prod_user),
				entry("mo",     prod_user),
				entry("vfxuat", uat_au_user),
				entry("vtuat",  uat_vt_user),
				entry("staruat",uat_st_user),
				entry("puguat", uat_pu_user),
				entry("vjpuat", uat_vjp_user),
				entry("umuat",  uat_um_user),
				entry("mouat",  uat_mo_user)
		);

		brandUrls = Map.ofEntries(
				entry("alpha",alphaDB_url),
				entry("vfx", prod_au_url),
				entry("vt", prod_vt_url),
				entry("star", prod_st_url),
				entry("pug", prod_pu_url),
				entry("vjp", prod_vjp_url),
				entry("um", prod_um_url),
				entry("mo", prod_mo_url),
				entry("vfxuat", uat_au_url),
				entry("vtuat", uat_vt_url),
				entry("staruat", uat_st_url),
				entry("puguat", uat_pu_url),
				entry("vjpuat", uat_vjp_url),
				entry("umuat", uat_um_url),
				entry("mouat", uat_mo_url)
		);

		brandPasswords = Map.ofEntries(
				entry("alpha",  alphaPass),
				entry("vfx",    prod_au_pass),
				entry("vt",     prod_vt_pass),
				entry("star",   prod_st_pass),
				entry("pug",    prod_pu_pass),
				entry("vjp",    prod_vjp_pass),
				entry("um",     prod_um_pass),
				entry("mo",     prod_mo_pass),
				entry("vfxuat", uat_au_pass),
				entry("vtuat",  uat_vt_pass),
				entry("staruat",uat_st_pass),
				entry("puguat", uat_pu_pass),
				entry("vjpuat", uat_vjp_pass),
				entry("umuat",  uat_um_pass),
				entry("mouat",  uat_mo_pass)
		);

		if(this.env.compareTo(ENV.ALPHA)==0) {
			String url = brandUrls.get(ENV.ALPHA.toString().toLowerCase());
			String password = brandPasswords.get(ENV.ALPHA.toString().toLowerCase());

			if (this.g_connect == null) {
				establishGlobalConnection(url, alphaUser, password);
			}
			if (this.r_connect == null && this.getRegulatorDB() != null) {
				establishRegulatorConnection(url, alphaUser, password);
			}
		}else if(this.env.compareTo(ENV.UAT)==0){
			String uatUser = brandUsers.get(brand.toString().toLowerCase()+ ENV.UAT.toString().toLowerCase());
			String url = brandUrls.get(brand.toString().toLowerCase()+ ENV.UAT.toString().toLowerCase());
			String password = brandPasswords.get(brand.toString().toLowerCase()+ ENV.UAT.toString().toLowerCase());

			if (this.g_connect == null) {
				establishGlobalConnection(url, uatUser, password);
			}
			if (this.r_connect == null && this.getRegulatorDB() != null) {
				establishRegulatorConnection(url, uatUser, password);
			}
		}
		else{
			String url = brandUrls.get(brand.toString().toLowerCase());
			String password = brandPasswords.get(brand.toString().toLowerCase());

			if (url != null && password != null) {
				if (this.g_connect == null) {
					establishGlobalConnection(url, prod_user,password);
				}
				if (this.r_connect == null && this.getRegulatorDB() != null) {
					establishRegulatorConnection(url, prod_user,password);
				}
			} else {
				Allure.addAttachment("DB数据库异常", "text/plain", String.valueOf(brand));
				System.err.println("Unknown brand: " + brand);
			}
		}
	}

	/**
	 * Establish global connection
	 * @param url
	 * @param password
	 */
	private void establishGlobalConnection(String url, String user,String password) {
		if (this.g_connect == null) {
			String conStrg = url + getGlobalDB() + "?useUnicode=true&characterEncoding=utf8&autoReconnect=true";
			try {
				g_connect = DriverManager.getConnection(conStrg, user, password);
			} catch (SQLException e) {
				Allure.addAttachment("DB数据库异常", "text/plain", e.getMessage());
				e.printStackTrace();
			}
		}
	}

	/**
	 * Establish regulator connection
	 * @param url
	 * @param password
	 */
	private void establishRegulatorConnection(String url, String user,String password) {
		if (this.r_connect == null && this.getRegulatorDB() != null) {
			String conStrr = url + getRegulatorDB() + "?autoReconnect=true&useSSL=false";
			try {
				r_connect = DriverManager.getConnection(conStrr, user, password);
			} catch (SQLException e) {
				Allure.addAttachment("DB数据库异常", "text/plain", e.getMessage());
				e.printStackTrace();
			}
		}
	}
	/**
	 *  将alpha数据库返回的结果集统一封装为JSONArray
	 * @param rl alpha数据库返回的结果集
	 * @return 封装好的JSONArray
	 */
	private JSONArray formatResult(ResultSet rl) {
		JSONArray jsonarray = new JSONArray();
		try {
		ResultSetMetaData metaData = rl.getMetaData();
		int column = metaData.getColumnCount();
		
		while(rl.next()) {
			String result = "{";
			for(int i = 1;i <=column; i++) {//ResultSet第一列的初始index是1，不是0
				String value = rl.getString(i);
				//处理字符串中对于JSON是特殊字符的字符
				if(value !=null && JSONValidator.from(value).validate()) {
					result = result +"\"" + metaData.getColumnLabel(i)+"\":"+value+",";
				}else {
					if(value!=null) {
						value = value.replace("\\", "\\\\");
					}
					result = result +"\"" + metaData.getColumnLabel(i)+"\":\""+value+"\",";
				}
			}
			result = result.substring(0, result.lastIndexOf(",")) + "}";
			jsonarray.add(JSON.parse(result));
		}
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return jsonarray;
		}
		return jsonarray;
	}
	
	/**
	 * 查询线上数据库
	 * @param global true查询global，false查询监管库
	 * @param sql
	 * @return
	 */
	private JSONArray queryProdDB(Boolean global,String sql) {
		JSONArray jsonarray = new JSONArray();
			String schema = "";
			if (global) {
				schema = this.getGlobalDB();
			} else {
				schema = this.getRegulatorDB();
			}
			try {
				Statement stt = r_connect.createStatement();
				ResultSet rl = stt.executeQuery(sql);
				jsonarray = formatResult(rl);
				rl.close();
				stt.close();
				return jsonarray;
			} catch (SQLException e) {
				Allure.addAttachment("DB数据库异常", "text/plain", e.getMessage());
				// TODO Auto-generated catch block
				e.printStackTrace();
				return jsonarray;
			}
		}

	
	/**
	 * 查询alpha global数据库
	 * @param sql 需要执行的SQL字符串
	 * @return
	 */
	public JSONArray queryGlobalDB(String sql) {
		initialDBcon();
		JSONArray jsonarray = new JSONArray();
		if(env.compareTo(ENV.ALPHA)==0 || env.compareTo(ENV.UAT)==0) {
			try {
				Statement stt = g_connect.createStatement();
			
				ResultSet rl = stt.executeQuery(sql);
				jsonarray = formatResult(rl);
				rl.close();
				stt.close();
				return jsonarray;
			} catch (SQLException e) {
				Allure.addAttachment("DB数据库异常", "text/plain", e.getMessage());
				// TODO Auto-generated catch block
				e.printStackTrace();
				return jsonarray;
			}
		}else {
			return queryProdDB(true,sql);
		}
	}
	
	
	/**
	 * 查询alpha监管库
	 * @param sql 需要执行的SQL
	 * @return 返回结果JSONArray
	 */
	public JSONArray queryRegulatorDB(String sql) {
		initialDBcon();
		JSONArray jsonarray = new JSONArray();
		if(env.compareTo(ENV.ALPHA)==0 || env.compareTo(ENV.UAT)==0) {
			try {
				Statement stt = r_connect.createStatement();
				ResultSet rl = stt.executeQuery(sql);
				jsonarray = formatResult(rl);
				rl.close();
				stt.close();
				return jsonarray;
			} catch (SQLException e) {
				Allure.addAttachment("DB数据库异常", "text/plain", e.getMessage());
				// TODO Auto-generated catch block
				e.printStackTrace();
				return jsonarray;
			}
		}else {
			return queryProdDB(false,sql);
		}
	}

	/**
	 * 更新alpha数据库的表
	 * @param sql 需要执行的SQL
	 * @return 返回影响行数
	 */
	public int updateRegulatorDB(String sql) {
		initialDBcon();
		if(env.equals(ENV.PROD)) {
			return -1;
		}
		try {
			Statement stt = r_connect.createStatement();
			int result =  stt.executeUpdate(sql);
			stt.close();
			return result;
		} catch (SQLException e) {
			Allure.addAttachment("DB数据库异常", "text/plain", e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
	}

	public int updateGlobalDB(String sql) {
		initialDBcon();
		if(env.equals(ENV.PROD)) {
			return -1;
		}
		try {
			Statement stt = g_connect.createStatement();
			int result =  stt.executeUpdate(sql);
			stt.close();
			return result;
		} catch (SQLException e) {
			Allure.addAttachment("DB数据库异常", "text/plain", e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
	}
	
	public static void main(String args[]) {
		/* 
		 * DbUtils obj = new DbUtils(ENV.ALPHA,BRAND.VFX,REGULATOR.VFSC2); String sql =
		 * "select U.id as userId, M.status as wcStatus, I.status as idStatus,AP.status as addrStatus,E.vars as vars from tb_user U left join tb_account_mt4 M on U.id = M.user_id left join tb_id_proof I on U.id = I.user_id left join tb_address_proof AP on U.id = AP.user_id left join tb_mail_send_log E on U.email = E.to_mail and E.template_invoke_name = \"welcome_mail\"\r\n"
		 * + " where U.email = \"YvAxqWy6wUYwMrIabQmDkA==\" and U.is_del!=1";
		 * 
		 */
		DbUtils obj = new DbUtils(ENV.PROD,BRAND.MO,REGULATOR.VFSC);
		String sql = "select+vars+from+tb_mail_send_log+where+template_invoke_name+like+\'verification_code\'+and+vars+like+\'%25Test+CRM%25\'+order+by+create_time+desc+limit+1%3B";
		
		JSONObject value = obj.queryRegulatorDB(sql).getJSONObject(0);
		System.out.println(value.toJSONString());
		System.out.println(value.getIntValue("addrStatus"));
		/*JSONArray js = obj.queryRegulatorDB(sql);
		for(int i = 0; i< js.size();i++) {
			JSONObject tt = js.getJSONObject(i);
			System.out.println(tt.get("id"));
		}*/		
	}

}

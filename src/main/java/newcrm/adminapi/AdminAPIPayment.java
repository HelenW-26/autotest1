package newcrm.adminapi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.Getter;
import lombok.Setter;
import newcrm.business.dbbusiness.PaymentDB;
import newcrm.cpapi.PCSAPIWithdraw;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.global.GlobalProperties.BRAND;
import newcrm.global.GlobalProperties.ENV;
import newcrm.global.GlobalProperties.REGULATOR;
import newcrm.utils.api.HyTechUrl;
import newcrm.utils.api.HyTechUtils;
import newcrm.utils.encryption.EncryptUtil;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import tools.RiskAuditCallBack;
import utils.LogUtils;
import vantagecrm.Utils;


import java.io.File;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static newcrm.utils.AlphaServerEnv.getDavinciURL;
import static org.testng.Assert.*;
import static tools.RiskAuditRequest.sendSRCRiskAuditRequest;

public class AdminAPIPayment extends AdminAPI{

	public PaymentDB paymentDB;
	@Getter @Setter
	public  String userId ;
	@Getter @Setter
	public  String countryCode;

	public AdminAPIPayment(String url, REGULATOR regulator, String adminUser, String password, BRAND brand, ENV testEnv) {
		super(url, regulator, adminUser, password, brand, testEnv);
		this.regulator = regulator;
		paymentDB = new PaymentDB(ENV.valueOf(String.valueOf(testEnv)), BRAND.valueOf(String.valueOf(brand)), REGULATOR.valueOf(String.valueOf(regulator)));

	}

    private String getOpetionData( Document doc,String name){
        Element selectedOption = doc.select("select#"+name+" option[selected]").first();
        return selectedOption == null? "" :  selectedOption.attr("value");
    }
    public void disable2FA(String userId){

        String toPath = this.url + "/individual/to_updateIndividual?user_id=" + userId;

        String path = this.url + "/individual/update_individual";

        JSONObject result = null;
        try {
            JSONObject body = new JSONObject();
            HttpResponse response = httpClient.getGetResponse(toPath, header,body);
            String htmlData = EntityUtils.toString(response.getEntity(),"UTF-8");

            Document doc = Jsoup.parse(htmlData);
            String oriPid = doc.select("input[id=oriPid]").attr("value");
            String oriCxd = doc.select("input[id=oriCxd]").attr("value");
            String oriCpa = doc.select("input[id=oriCpa]").attr("value");
            String oriSales = doc.select("input[id=oriSales]").attr("value");
            String oriAllocationType = doc.select("input[id=oriAllocationType]").attr("value");
            String oriAffid = doc.select("input[id=oriAffid]").attr("value");
            String accountOwner = getOpetionData(doc,"accountOwner");
            String accountOwnerPid = doc.select("input[id=accountOwnerPid]").attr("value");
            String acc_middle_name = doc.select("input[id=acc_middle_name]").attr("value");
            String acc_middle_nameEn = doc.select("input[id=acc_middle_nameEn]").attr("value");
            String acc_middle_nameNln = doc.select("input[id=acc_middle_nameNln]").attr("value");
            String acc_last_name = doc.select("input[id=acc_last_name]").attr("value");
            String acc_last_nameEn = doc.select("input[id=acc_last_nameEn]").attr("value");
            String acc_last_nameNln = doc.select("input[id=acc_last_nameNln]").attr("value");
            String cpa = doc.select("input[name=cpa]").attr("value");
            String add_cxd = doc.select("input[id=add_cxd]").attr("value");

            String promotion = getOpetionData(doc,"promotion");
            String country = getOpetionData(doc,"country");
            String ibNation = getOpetionData(doc,"ib_Nationality");


            String origJson = URLEncoder.encode(doc.select("textarea[id=origJson2]").text().replaceAll("\\s+",""));
//            this.printAPIInfo(brand, regulator, path, body, htmlData );

            header.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

            String strBody = "oriCxd="+oriCxd+"&oriCpa="+oriCpa+"&oriSales="+oriSales+"&oriAllocationType="+oriAllocationType+"&oriAffid="+oriAffid+"&oriPid="+oriPid+
                    "&id="+userId+"&gender=Male&account_type=2&clientStatus=&acc_first_name=testcrm&acc_first_nameEn=testcrm&acc_first_nameNln=testcrm" +
                    "&accountOwner="+accountOwner+"&accountOwnerPid="+accountOwnerPid+"&fromPage=to_updateIndividual&rowId=&acc_middle_name="+acc_middle_name+"&acc_middle_nameEn="+acc_middle_nameEn+
                    "&acc_middle_nameNln="+acc_middle_nameNln+"&acc_last_name="+acc_last_name+"&acc_last_nameEn="+acc_last_nameEn+"&acc_last_nameNln="+acc_last_nameNln+
                    "&cpa="+cpa+"&add_cxd="+add_cxd+"&promotion="+promotion+"&opNote=&ib_Mployment=&country="+country+"&income=&ib_Nationality="+ibNation+"&acc_id_type=National+ID+Card&place_of_birth=5015" +
                    "&client_flag=&street_name=city&acc_id_num=00005679822222&suburb=city&ib_Sinvest=&state=&state_input=&ib_funds=&indi_postcode=&firstBirthday=1991-06-13&credit_card=&whoreferredyoutous=" +
                    "&acc_tax_us=&acc_invest_deposit=&referral=&resourcePool=Select+Resource+Pool...&processed_notes=&acc_investmentexp_tradew=&acc_investmentexp_amount_tradew=&pammMasterUid=&pammMasterContent=" +
                    "&markUserType=1&passport=&bankStatement=&worldCheck=&lpoa=&origJson="+ origJson + "&disable2FA=true";

            response = httpClient.getPostResponse(path, header, strBody);

            result = JSON.parseObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
            this.printAPIInfo(brand, regulator, path, body, result);
            assertTrue(result.getBoolean("success"), "API response is not success. API Failed!! Please check\n" + result);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	public JSONArray apiClientSearch(String queryParam) {
		String path = this.url + "/individual/query_individualList";

		JSONObject body = new JSONObject();
		body.put("skipCount", false);
		body.put("pagination", new JSONObject().fluentPut("pageNo", null));

		JSONObject parameters = new JSONObject();

		// 判断传入参数是数字字符串还是邮箱格式
		if (queryParam.matches("\\d+")) {
			// 如果是数字字符串，则使用userId查询
			parameters.put("tb_user_outer.userId", new JSONObject()
					.fluentPut("fuzzy", true)
					.fluentPut("filterType", "INPUT")
					.fluentPut("input", queryParam));
		} else if (queryParam.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
			String emailEncrypt = EncryptUtil.getAdminEmailEncrypt(queryParam);
			// 如果是邮箱格式，则使用邮箱查询
			parameters.put("tb_user.emailSub", new JSONObject()
					.fluentPut("filterType", "INPUT")
					.fluentPut("input", emailEncrypt));
		} else {
			// 如果都不是，则默认按邮箱处理（可能是用户名或其它标识）
			parameters.put("tb_user.email", new JSONObject()
					.fluentPut("filterType", "INPUT")
					.fluentPut("input", queryParam));
		}

		parameters.put("directLevel", new JSONObject()
				.fluentPut("filterType", "CUSTOM")
				.fluentPut("input", "5"));
		parameters.put("returnTime", new JSONObject()
				.fluentPut("filterType", "CUSTOM")
				.fluentPut("input", "0"));

		body.put("parameters", parameters);
		header.put("Content-Type", "application/json");
		header.put("Current-Regulator", this.regulator.toString());

		JSONObject result = null;
		try {
			HttpResponse response = httpClient.getPostResponse(path, header, body, 15000);
			result = JSON.parseObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
			this.printAPIInfo(brand, regulator, path, body, result);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (result != null) {
			return result.getJSONArray("rows");
		}

		return new JSONArray(); // 返回空数组而不是null
	}


	//deposit audit search with name = "testcrm api", status = submitted to 3rd party
	//can make it to dynamic with passing param
	public JSONObject apiDPAuditSearch (String userName, String status) {
        String path = this.url + HyTechUrl.queryDepositAudit;

        JSONObject body = new JSONObject();
//        body.put("limit", 10);
//        body.put("pageNo", 1);
//        body.put("offset", 0);
//        body.put("order", "asc");
        JSONObject search = new JSONObject();
        search.put("statusQuery", status);
        search.put("typeQuery", "-1");
        search.put("channelQuery", "-1");
		String searchType = "1";
		if(userName!=null){
			if (userName.matches("\\d+")) {
				searchType = "2"; // 账号类型搜索
			} else {
				searchType = "1"; // 默认搜索类型
				LogUtils.info("Username format is not numeric: " + userName);
			}
		}
        search.put("searchType", searchType);
        search.put("userQuery", userName);
        search.put("directLevel", "5");
        search.put("user_id", "");
        search.put("org_id", "");
        search.put("startDate", "2017-01-01");
        search.put("endDate", GlobalMethods.setToDateTime());
        search.put("dateType", "1");
        search.put("countryList", "");
        search.put("mt4AccountTypeQuery", "0");
        search.put("markType", "-1");
        search.put("accountType", "MT");
        body.put("search", search);

        header.put("Content-Type", "application/json");

		JSONObject result = null;
        try {
            HttpResponse response = httpClient.getPostResponse(path, header, body);

			result = JSON.parseObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
			this.printAPIInfo(brand, regulator, path, body, result);
			if(result== null){
				throw new RuntimeException(path+" response is null\n"+ response);
			}
			assertTrue(result.getBoolean("success"), "API response is not success. API Failed!! Please check\n" + result);
			boolean depositAudit=result.getJSONArray("rows").isEmpty();
			if (!depositAudit){
				Integer apiRecordCount = result.getInteger("total");
				JSONArray dbRecords = paymentDB.getDPRecordCountByNameStatus(userName, status, GlobalMethods.setToDateTime());
				if (dbRecords != null && !dbRecords.isEmpty()) {
					Integer dbRecordCount = dbRecords.size();
					assertEquals(apiRecordCount, dbRecordCount, "AP number of records != DB number of records\n"
							+ "Total of API Records: "+apiRecordCount+ "\nTotal of DB Records: "+dbRecordCount);
				}
			}



		} catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

	public JSONObject apiDPAuditSearch (String startDate,String typeQuery,String userName, String status) {
		String path = this.url + HyTechUrl.queryDepositAudit;
		String endDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

		JSONObject body = new JSONObject();
		JSONObject search = new JSONObject();
		search.put("statusQuery", status);
		search.put("typeQuery", typeQuery);
		search.put("channelQuery", "-1");
		search.put("searchType", "1");
		search.put("userQuery", userName);
		search.put("directLevel", "5");
		search.put("user_id", "");
		search.put("org_id", "");
		search.put("startDate", startDate);
		search.put("endDate", endDate);
		search.put("dateType", "1");
		search.put("countryList", "");
		search.put("mt4AccountTypeQuery", "0");
		search.put("markType", "-1");
		search.put("accountType", "MT");
		body.put("search", search);

		header.put("Content-Type", "application/json");

		JSONObject result = null;
		try {
			HttpResponse response = httpClient.getPostResponse(path, header, body,10*1000);
			result = JSON.parseObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
			this.printAPIInfo(brand, regulator, path, body, result);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}


	public JSONObject apiDPAuditSearchByUserId (String startDate,String typeQuery,String userId, String status) {
		String path = this.url + HyTechUrl.queryDepositAudit;
		String endDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

		JSONObject body = new JSONObject();
		JSONObject search = new JSONObject();
		search.put("statusQuery", status);
		search.put("typeQuery", typeQuery);
		search.put("channelQuery", "-1");
		search.put("searchType", "3");//查询类型为：userId
		search.put("userQuery", userId);
		search.put("directLevel", "5");
		search.put("user_id", "");
		search.put("org_id", "");
		search.put("startDate", startDate);
		search.put("endDate", endDate);
		search.put("dateType", "1");
		search.put("countryList", "");
		search.put("mt4AccountTypeQuery", "0");
		search.put("markType", "-1");
		search.put("accountType", "MT");
		body.put("search", search);

		header.put("Content-Type", "application/json");

		JSONObject result = null;
		try {
			HttpResponse response = httpClient.getPostResponse(path, header, body,10*1000);
			result = JSON.parseObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
			this.printAPIInfo(brand, regulator, path, body, result);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}


	public JSONObject apiWDAuditSearch (String userName, String status){
		String path = this.url + "/payment/withdraw/query_PaymentWithdrawaList";

		JSONObject body = new JSONObject();
		body.put("skipCount", false);
		body.put("pagination", new JSONObject().fluentPut("pageNo",null));
		body.put("parameters", new JSONObject()
				.fluentPut("createTime",new JSONObject()
					.fluentPut("filterType", "DATEPICKER")
					.fluentPut("input",new JSONObject()
							.fluentPut("startDate", GlobalMethods.setFromDateTime())
							.fluentPut("endDate", GlobalMethods.setToDateTime())))
				.fluentPut("directLevel", new JSONObject()
						.fluentPut("filterType", "CUSTOM")
						.fluentPut("input", "5"))
				.fluentPut("tb_user.realName", new JSONObject()
						.fluentPut("filterType", "INPUT")
						.fluentPut("input", userName))
				.fluentPut("status", new JSONObject()
						.fluentPut("filterType", "SELECT")
						.fluentPut("input", status)));

		header.put("Content-Type", "application/json");

		JSONObject result = null;
		try {
			HttpResponse response = httpClient.getPostResponse(path, header,body);
			result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			LogUtils.info("API response:\n " + result);
			this.printAPICPInfoPro(brand, regulator, path, body, response);
			assertTrue(result.getBoolean("success"), "API response is not success. API Failed!! Please check\n" + result);
            assertFalse(result.getJSONArray("rows").isEmpty(), "API response is empty!! Please check\n" + result);

			Integer apiRecordCount = result.getInteger("total");
			Integer dbRecordCount = paymentDB.getWDRecordCountByNameStatus(userName, status, GlobalMethods.setFromDateTime(), GlobalMethods.setToDateTime()).size();
			assertEquals(apiRecordCount, dbRecordCount, "AP number of records != DB number of records\n"
					+ "Total of API Records: "+apiRecordCount+ "\nTotal of DB Records: "+dbRecordCount);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public JSONObject apiWDAuditSearchNew (String userName, String status){
		String path = this.url + "/payment/withdraw/query_PaymentWithdrawaList";

		JSONObject body = new JSONObject();
		body.put("skipCount", false);
		body.put("pagination", new JSONObject().fluentPut("pageNo",null));
		body.put("parameters", new JSONObject()
				.fluentPut("createTime",new JSONObject()
						.fluentPut("filterType", "DATEPICKER")
						.fluentPut("input",new JSONObject()
								.fluentPut("startDate", GlobalMethods.setFromDateTime())
								.fluentPut("endDate", GlobalMethods.setToDateTime())))
				.fluentPut("directLevel", new JSONObject()
						.fluentPut("filterType", "CUSTOM")
						.fluentPut("input", "5"))
				.fluentPut("tb_user.realName", new JSONObject()
						.fluentPut("filterType", "INPUT")
						.fluentPut("input", userName))
				.fluentPut("status", new JSONObject()
						.fluentPut("filterType", "SELECT")
						.fluentPut("input", status)));

		header.put("Content-Type", "application/json");

		JSONObject result = null;
		try {
			HttpResponse response = httpClient.getPostResponse(path, header,body);
			result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			LogUtils.info("API response:\n " + result);
			this.printAPICPInfoPro(brand, regulator, path, body, response);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * API分页查询WD记录
	 * @param userName
	 * @param status
	 * @param pageNo
	 * @param limit
	 * @return
	 */
	public JSONObject apiWDAuditSearchByPage (String userName, String status, Integer pageNo, Integer limit) {
		String path = this.url + "/payment/withdraw/query_PaymentWithdrawaList";

		JSONObject body = new JSONObject();
		body.put("skipCount", false);

		// 添加分页参数
		JSONObject pagination = new JSONObject();
		if (limit != null) {
			pagination.put("limit", limit);
		}
		if (pageNo != null) {
			pagination.put("pageNo", pageNo);
		}
		if (limit != null && pageNo != null) {
			pagination.put("offset", (pageNo - 1) * limit); // 计算offset
		}
		body.put("pagination", pagination);

		body.put("parameters", new JSONObject()
				.fluentPut("createTime", new JSONObject()
						.fluentPut("filterType", "DATEPICKER")
						.fluentPut("input", new JSONObject()
								.fluentPut("startDate", GlobalMethods.setFromDateTime())
								.fluentPut("endDate", GlobalMethods.setToDateTime())))
				.fluentPut("directLevel", new JSONObject()
						.fluentPut("filterType", "CUSTOM")
						.fluentPut("input", "5"))
				.fluentPut("tb_user.realName", new JSONObject()
						.fluentPut("filterType", "INPUT")
						.fluentPut("input", userName))
				.fluentPut("status", new JSONObject()
						.fluentPut("filterType", "SELECT")
						.fluentPut("input", status)));

		header.put("Content-Type", "application/json");

		JSONObject result = null;
		try {
			HttpResponse response = httpClient.getPostResponse(path, header, body);
			result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			LogUtils.info("API response:\n " + result);
			this.printAPICPInfoPro(brand, regulator, path, body, response);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public JSONObject apiWDAuditAccountSearch (String account, String status){
		String path = this.url + "/payment/withdraw/query_PaymentWithdrawaList";

		JSONObject body = new JSONObject();
		body.put("skipCount", true);
		body.put("pagination", new JSONObject()
				.fluentPut("limit", 10)
				.fluentPut("pageNo", 1)
				.fluentPut("offset", 0));
		body.put("parameters", new JSONObject()
				.fluentPut("mt4DatasourceId", new JSONObject()
						.fluentPut("filterType", "SELECT")
						.fluentPut("input", ""))
				.fluentPut("userId", new JSONObject()
						.fluentPut("fuzzy", false)
						.fluentPut("filterType", "INPUT")
						.fluentPut("input", ""))
				.fluentPut("tb_user.realName", new JSONObject()
						.fluentPut("filterType", "INPUT")
						.fluentPut("input", ""))
				.fluentPut("tb_user.countryCode", new JSONObject()
						.fluentPut("filterType", "SELECT")
						.fluentPut("input", null))
				.fluentPut("mt4Account", new JSONObject()
						.fluentPut("filterType", "INPUT")
						.fluentPut("input", account))
				.fluentPut("tb_user.email", new JSONObject()
						.fluentPut("filterType", "INPUT")
						.fluentPut("input", ""))
				.fluentPut("currency", new JSONObject()
						.fluentPut("filterType", "SELECT")
						.fluentPut("input", null))
				.fluentPut("withdrawType", new JSONObject()
						.fluentPut("filterType", "SELECT")
						.fluentPut("input", ""))
				.fluentPut("markUserType", new JSONObject()
						.fluentPut("filterType", "SELECT")
						.fluentPut("input", ""))
				.fluentPut("createTime", new JSONObject()
						.fluentPut("filterType", "DATEPICKER")
						.fluentPut("input", new JSONObject()
								.fluentPut("startDate", GlobalMethods.dateWeekAgo() )
								.fluentPut("endDate", GlobalMethods.dateDayEnd())))
				.fluentPut("status", new JSONObject()
						.fluentPut("filterType", "SELECT")
						.fluentPut("input", status))
				.fluentPut("updateTime", new JSONObject()
						.fluentPut("filterType", "DATEPICKER")
						.fluentPut("input", new JSONObject()))
				.fluentPut("orderNumber", new JSONObject()
						.fluentPut("filterType", "INPUT")
						.fluentPut("input", ""))
				.fluentPut("operateName", new JSONObject()
						.fluentPut("filterType", "INPUT")
						.fluentPut("input", ""))
				.fluentPut("checkingStatus", new JSONObject()
						.fluentPut("filterType", "SELECT")
						.fluentPut("input", ""))
				.fluentPut("directLevel", new JSONObject()
						.fluentPut("filterType", "CUSTOM")
						.fluentPut("input", "5"))
				.fluentPut("user_id", new JSONObject()
						.fluentPut("filterType", "CUSTOM")
						.fluentPut("input", ""))
				.fluentPut("org_id", new JSONObject()
						.fluentPut("filterType", "CUSTOM")
						.fluentPut("input", "")));


		header.put("Content-Type", "application/json");

		JSONObject result = null;
		try {
			HttpResponse response = httpClient.getPostResponse(path, header,body);
			result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, path, body, result );
			this.printAPICPInfoPro(brand, regulator, path, body, response);
			assertTrue(result.getBoolean("success"), "API response is not success. API Failed!! Please check\n" + result);
			assertFalse(result.getJSONArray("rows").isEmpty(), "API response is empty!! Please check\n" + result);

		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	public JSONObject apiWDAuditSearch (String userId,String account, String status){
        String path = this.url + "/payment/withdraw/query_PaymentWithdrawaList";

        JSONObject body = new JSONObject();
        body.put("skipCount", false);
        body.put("pagination", new JSONObject().fluentPut("pageNo",null));
        body.put("parameters", new JSONObject()
                .fluentPut("createTime",new JSONObject()
                        .fluentPut("filterType", "DATEPICKER")
                        .fluentPut("input",new JSONObject()
                                .fluentPut("startDate", GlobalMethods.dateDayStart())
                                .fluentPut("endDate", GlobalMethods.dateDayEnd())))
                .fluentPut("directLevel", new JSONObject()
                        .fluentPut("filterType", "CUSTOM")
                        .fluentPut("input", "5"))
                .fluentPut("userId", new JSONObject()
                        .fluentPut("filterType", "INPUT")
                        .fluentPut("input", userId)
                        .fluentPut("fuzzy", false))
                .fluentPut("mt4Account", new JSONObject()
                        .fluentPut("filterType", "INPUT")
                        .fluentPut("input", account))
                .fluentPut("status", new JSONObject()
                        .fluentPut("filterType", "SELECT")
                        .fluentPut("input", status)));

        header.put("Content-Type", "application/json");

        JSONObject result = null;
        try {
            HttpResponse response = httpClient.getPostResponse(path, header,body);
            result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
            this.printAPIInfo(brand, regulator, path, body, result );
            assertTrue(result.getBoolean("success"), "API response is not success. API Failed!! Please check\n" + result);
            assertFalse(result.getJSONArray("rows").isEmpty(), "API response is empty!! Please check\n" + result);

            Integer apiRecordCount = result.getInteger("total");
            Integer dbRecordCount = paymentDB.getWDRecordCountByUserIdStatus(userId, status, GlobalMethods.dateDayStart(), GlobalMethods.setToDateTime()).size();
            assertEquals(apiRecordCount, dbRecordCount, "AP number of records != DB number of records\n"
                    + "Total of API Records: "+apiRecordCount+ "\nTotal of DB Records: "+dbRecordCount);
        }catch(Exception e) {
            e.printStackTrace();
        }
        return result;
    }

	public void apiWDdailyCapAmt (){
		String path = this.url + "/payment/withdraw/dailyCapAmount";
		JSONObject body = new JSONObject();

		header.put("Content-Type", "application/json");

		try {
			HttpResponse response = httpClient.getGetResponse(path, header,body);
			JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, path, body, result );
			assertTrue(!result.isEmpty() && result.containsKey("amount"), "API response is empty. Please check!!\n" + result);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 数据未被处理才能进行checkClaimStatus
	 * Check claim status
	 * @param recordID
	 */
	public void apiWDAuditCheckClaimStatus(String recordID){
		String path = this.url + "/payment/withdraw/checkClaimStatus";

		HashMap <String,String> body = new HashMap<>();
		body.put("ids",recordID+",");

		header.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

		try {
			HttpResponse response = httpClient.getPostResponse(path, header, body);
			String result = EntityUtils.toString(response.getEntity(),"UTF-8");
			this.printAPIInfo(brand, regulator, path, body, result );
			assertFalse(Boolean.parseBoolean(result.trim()),"API Failed!! Path: " +path+"\n" + result);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void apiWDAuditClaimRecord(String recordID){
		String path = this.url + "/payment/withdraw/startClaimWithdraw";

		HashMap <String,String> body = new HashMap<>();
		body.put("ids",recordID+",");

		header.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

		try {
			HttpResponse response = httpClient.getPostResponse(path, header, body);
			JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, path, body, result );
			assertTrue("true".equals(result.getString("success")) && "操作成功".equals(result.getString("message")),"API Failed!! Path: " +path+"\n" + result);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void apiWDAuditUpdateNote(String recordID){
		String path = this.url + "/payment/withdraw/withdraw_notes_update";

		HashMap <String,String> body = new HashMap<>();
		body.put("id",recordID);
		body.put("note","testadminapi");
		body.put("internalNotes","testadminapi");

		header.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

		try {
			HttpResponse response = httpClient.getPostResponse(path, header, body);
			JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, path, body, result );
			assertTrue("true".equals(result.getString("success")) && "Update Successfully".equals(result.getString("message")),"API Failed!! Path: " +path+"\n" + result);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void apiWDAuditPendingRecord(String recordID, String wdAmt, String wdMethod){
		String path = this.url + "/payment/withdraw/pendingWithdraw";

		JSONObject body = new JSONObject();
		body.put("withdrawId",recordID);
		body.put("actualAmount",wdAmt);
		body.put("withdrawMethod",wdMethod+","+wdAmt);
		body.put("creditCardIds","");
		body.put("creditCardWithdrawalPlan",null);
		body.put("pendingReason","Bank Statement Required");
		body.put("processedNotes","testadminapi");

		header.put("Content-Type", "application/json");

		try {
			HttpResponse response = httpClient.getPostResponse(path, header, body);
			JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, path, body, result );
			assertTrue("true".equals(result.getString("success")) && "操作成功".equals(result.getString("message")),"API Failed!! Path: " +path+"\n" + result);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void apiWDAuditApproveRecord(String recordID, String wdActualAmt, String wdMethod, String wdType, String wdPymtAmt, String wdCategory, String wdRate){
		String path = this.url + "/payment/withdraw/confirmWithdraw";
		JSONObject body = new JSONObject();
		body.put("withdrawId",recordID);
		body.put("withdrawType",wdType);
		body.put("actualAmount",wdActualAmt);
		body.put("withdrawMethod",wdMethod+","+wdActualAmt);
		body.put("creditCardIds","");
		body.put("creditCardWithdrawalPlan",null);
		body.put("internalNotes","testadminapi withdrawal");
		body.put("processedNotes","testadminapi");
		body.put("status","sub");
		body.put("fee","0");

		if (wdCategory.equals("4")){
			body.put("finalAmount",wdActualAmt);
			body.put("toCurrencyAmount",wdPymtAmt);
			body.put("rate",wdRate);
		} else {
			body.put("paymentAmount",wdPymtAmt);
		}

		JSON.toJSONString(body, SerializerFeature.WriteMapNullValue);
		header.put("Content-Type", "application/json");
		try {
			HttpResponse response = httpClient.getPostResponse(path, header, body);
			JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, path, body, result );
			assertTrue("true".equals(result.getString("success")) && "操作成功".equals(result.getString("message")),"API Failed!! Path: " +path+"\n" + result);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void apiWDAuditFailRecord(String recordID, String reversedAmt){
		String path = this.url + "/payment/withdraw/failWithdraw";

		JSONObject body = new JSONObject();
		body.put("withdrawId",recordID);
		body.put("reversedAmount",reversedAmt);
		body.put("paymentCardTransactionList","");
		body.put("processedNotes","testadminapi");

		header.put("Content-Type", "application/json");

		try {
			HttpResponse response = httpClient.getPostResponse(path, header, body);
			JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPICPInfoPro(brand,regulator,path,body, response);
			assertTrue("true".equals(result.getString("success")) && "操作成功".equals(result.getString("message")),"API Failed!! Path: " +path+"\n" + result);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	//get withdrawal channel ID
	public String apiGetWDChannelID (String wdID){
		String path = this.url + "/payment/withdraw/to_sticpay_completeWithdraw?withdraw_id="+wdID;

		try {
			HttpResponse response = httpClient.getPostResponse(path, header, "");
			String result = EntityUtils.toString(response.getEntity(), "UTF-8");

			// Parse the HTML string
			Document doc = Jsoup.parse(result);

			//Get List of withdraw channel
			Element detailTypeList = doc.getElementById("detailTypeList");
			String detailTypeValue = detailTypeList.attr("value");

			//Get this withdraw record's channel
			Element defaultWithdrawChannel = doc.getElementById("defaultWithdrawChannel");
			String defaultWDChannel = defaultWithdrawChannel.attr("value");

			//Split the string value by comma
			String[] pairs = detailTypeValue.split(",\\s*");
			for (String pair : pairs) {
				// split into 2 parts
				String[] parts = pair.split("\\|", 2);

				if (parts[1].equalsIgnoreCase(defaultWDChannel))
				{
					return parts[0];
				}
			}

		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	//update record to audit status
	public void apiDPAuditUpdStatus (String depOrderNumber){
		String path = this.url + "/payment/deposit/update_deposit_status";
		HashMap<String,String> body = new HashMap<>();
		body.put("orderNumber", depOrderNumber);
		body.put("status", "6");
		body.put("notes", "testadminapi");
		body.put("typeId", "0");

		header.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

		try {
			HttpResponse response = httpClient.getPostResponse(path, header,body);
			JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, path, body, result);
			assertTrue(result.getBoolean("success") && "update success".equals(result.getString("message")), "API Failed!! Please check\n" + result);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	// 更新deposit record
	public void apiDPAuditUpdStatus (String depOrderNumber, String status){
		/**
		 * status: 3 = Payment Failed
		 * status: 6 = Approve(audit)
		 * status: 7 = Reject
		 * status: 9 = Pending
		 */

		LocalDateTime currentDateTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String formattedDateTime = currentDateTime.format(formatter);
		String path = this.url + "/payment/deposit/update_deposit_status";
		HashMap<String,String> body = new HashMap<>();
		body.put("orderNumber", depOrderNumber);
		body.put("status", status);
		body.put("notes", "testadminapi-"+ formattedDateTime);
		body.put("typeId", "0");

		header.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

		try {
			HttpResponse response = httpClient.getPostResponse(path, header,body);
			JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, path, body, result);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

    //approve audit record
	public void apiDPAuditApprove(String depID, String depAmt) {
		String path = this.url + "/payment/deposit/auditDeposit_pass";
		HashMap<String,String> body = new HashMap<>();
		body.put("deposit_id", depID);
		body.put("actualAmount", depAmt);
		body.put("processed_notes", "testadminapi");

		header.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

		try {
			HttpResponse response = httpClient.getPostResponse(path, header, body);
			JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			LogUtils.info("API result: " + "\n" + result.toJSONString());
//			this.printAPIInfo(brand, regulator, path, body, result);
			this.printAPICPInfoPro(brand, regulator, path, body, response);
			assertTrue(result.getBoolean("success") && "200".equals(result.getString("code")) && "操作成功".equals(result.getString("message")), "API Failed!! Please check\n" + result);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 审核存款记录，遍历最多10条记录，返回第一条审核成功的记录
	 * @param userName 用户名
	 * @param status 状态查询条件
	 * @return 第一条审核成功的记录对象，如果没有审核成功则返回null
	 */
	public JSONObject apiDPAuditApproveFirstSuccess(String userName, String status) {
		// 搜索待审核的存款记录
		JSONObject dpAuditResult = apiDPAuditSearch(userName, status);

		if (dpAuditResult == null || dpAuditResult.getJSONArray("rows").isEmpty()) {
			LogUtils.info("No deposit records found for approval");
			return null;
		}

		JSONArray records = dpAuditResult.getJSONArray("rows");
		// 最多遍历10条记录
		int maxRecords = Math.min(10, records.size());

		LogUtils.info("Starting deposit approval process, will check up to " + maxRecords + " records");

		for (int i = 0; i < maxRecords; i++) {
			try {
				JSONObject record = records.getJSONObject(i);
				String depID = record.getString("id");
				String depAmt = record.getString("actualAmount");

				LogUtils.info("Attempting to approve deposit record " + (i+1) + "/" + maxRecords + ", ID: " + depID);

				String path = this.url + "/payment/deposit/auditDeposit_pass";
				HashMap<String,String> body = new HashMap<>();
				body.put("deposit_id", depID);
				body.put("actualAmount", depAmt);
				body.put("processed_notes", "testadminapi");

				header.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

				HttpResponse response = httpClient.getPostResponse(path, header, body);
				JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
				LogUtils.info("API result: " + "\n" + result.toJSONString());
				this.printAPICPInfoPro(brand, regulator, path, body, response);

				if (result.getBoolean("success") && "200".equals(result.getString("code")) && "操作成功".equals(result.getString("message"))) {
					LogUtils.info("Successfully approved deposit record ID: " + depID + ", returning this record");
					return record; // 审核成功，立即返回该记录
				} else {
					LogUtils.info("Failed to approve deposit record ID: " + depID + ", trying next record");
				}
			} catch(Exception e) {
				LogUtils.info("Exception while processing deposit record at index " + i + ": " + e.getMessage() + ", trying next record");
				e.printStackTrace();
			}
		}

		LogUtils.info("Finished checking up to " + maxRecords + " records, no successful approval found");
		return null; // 遍历完10条记录都没有审核成功的
	}

	//信用卡入金审核审核通过
	public void apiDPCCAuditApprove(String depID, String card_number, String card_expiry,String card_name) {
		String path = this.url + "/payment/deposit/auditDeposit_pass";
		HashMap<String,String> body = new HashMap<>();
		body.put("deposit_id", depID);
		body.put("card_number", card_number);
		body.put("card_expiry", card_expiry);
		body.put("card_name", card_name);
		body.put("processed_notes", "testadminapi");
		body.put("secure3D", "1");

		header.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

		try {
			HttpResponse response = httpClient.getPostResponse(path, header,body);
			JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			LogUtils.info("API result: " +  "\n" + result.toJSONString());
			this.printAPIInfo(brand, regulator, path, body, result);
			assertTrue(result.getBoolean("success") && "200".equals(result.getString("code")) && "操作成功".equals(result.getString("message")), "API Failed!! Please check\n" + result);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	//pending audit record
	public void apiDPAuditPending(String depID) {
		String path = this.url + "/payment/deposit/auditDeposit_pending";
		HashMap<String,String> body = new HashMap<>();
		body.put("deposit_id", depID);
		body.put("pending_reason", "testadminAPI pendingReason");
		body.put("processed_notes", "testadminapi");

		header.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

		try {
			HttpResponse response = httpClient.getPostResponse(path, header,body);
			JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, path, body, result);
			assertTrue(result.getBoolean("success") && "200".equals(result.getString("code")) && "操作成功".equals(result.getString("message")), "API Failed!! Please check\n" + result);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	//reject audit record
	public void apiDPAuditReject(String depID) {
		String path = this.url + "/payment/deposit/auditDeposit_refuse";
		HashMap<String,String> body = new HashMap<>();
		body.put("deposit_id", depID);
		body.put("processed_notes", "testadminapi, testRejectReason");

		header.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

		try {
			HttpResponse response = httpClient.getPostResponse(path, header,body);
			JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, path, body, result);
			assertTrue(result.getBoolean("success") && "200".equals(result.getString("code")) && "操作成功".equals(result.getString("message")), "API Failed!! Please check\n" + result);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	//admin portal - report - deposit/withdrawal report
	public void apiDepWithdReport (){
		String path = this.url + "/report/view/depositAndWithdrawReports";

		JSONObject body = new JSONObject()
				.fluentPut("limit", 10)
				.fluentPut("pageNo", 1)
				.fluentPut("order", "asc")
				.fluentPut("search", new JSONObject()
						.fluentPut("directLevel", "5")
						.fluentPut("user_id", "")
						.fluentPut("org_id", "")
						.fluentPut("searchType", "-1")
						.fluentPut("search", "")
						.fluentPut("agentuserQuery", "")
						.fluentPut("startDate", GlobalMethods.setFromDateTime())
						.fluentPut("endDate", GlobalMethods.setToDateTime())
						.fluentPut("dataSourceId", "0")
						.fluentPut("accType", "1")
						.fluentPut("dataType", "0")
						.fluentPut("paymentMethod", "0")
						.fluentPut("countryCodes", ""));

		header.put("Content-Type", "application/json");

		try {
			HttpResponse response = httpClient.getPostResponse(path, header,body);
			JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, path, body, result );
			assertTrue(result.getBoolean("success") , "API Failed!! Path: " + path + "\n" + result);
            assertFalse(result.getJSONArray("rows").isEmpty(), "API response is empty!! Please check\n" + result);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	//admin portal - report - deposit/withdrawal report - summary
	public void apiDepWithdReportStatistic (){
		String path = this.url + "/report/view/depositAndWithdrawReportsStatistics";

		JSONObject body = new JSONObject()
				.fluentPut("pageNo", null)
				.fluentPut("order", "asc")
				.fluentPut("search", new JSONObject()
						.fluentPut("directLevel", "5")
						.fluentPut("user_id", "")
						.fluentPut("org_id", "")
						.fluentPut("searchType", "-1")
						.fluentPut("search", "")
						.fluentPut("agentuserQuery", "")
						.fluentPut("startDate", GlobalMethods.setFromDateTime())
						.fluentPut("endDate", GlobalMethods.setToDateTime())
						.fluentPut("dataSourceId", "0")
						.fluentPut("accType", "1")
						.fluentPut("dataType", "0")
						.fluentPut("paymentMethod", "0")
						.fluentPut("countryCodes", ""));

		header.put("Content-Type", "application/json");

		try {
			HttpResponse response = httpClient.getPostResponse(path, header,body);
			JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, path, body, result );
			assertTrue(result.getBoolean("success") , "API Failed!! Path: " + path + "\n" + result);
            assertFalse(result.getJSONArray("rows").isEmpty(), "API response is empty!! Please check\n" + result);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	//admin portal - davinci token
	public String apiDavinciToken (){
		String path = this.url + "/davinci/token";
		String result = "";
		try {
			HttpResponse response = httpClient.getGetResponse(path, header);
			result = EntityUtils.toString(response.getEntity(), "UTF-8");
			this.printAPIInfo(brand, regulator, path, "", result );
			assertNotNull(result, "API response is null. API Failed!! Please check\n" + result);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	//davinci portal - deposit status api
	public void apiDavinciPaymentDPStatus (String davinciToken){
		String path = getDavinciURL(brand.toString()) + "/api/payment-status?regulator=" + regulator.toString().toLowerCase() + "&dataType=1";

		header = new HashMap<>();
		header.put("Authorization","Bearer "+ davinciToken);

		try {
			HttpResponse response = httpClient.getGetResponse(path, header);
			JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, path, "", result );
			assertEquals(result.getString("status"), "success", "API Failed!! Path: " + path + "\n" + result);
			assertFalse(result.getJSONObject("data").getJSONArray("paymentStatus").isEmpty(), "API response is null!! Please check\n" + result);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	//davinci portal - deposit method api
	public void apiDavinciPaymentDPMethod (String davinciToken){
		String path = getDavinciURL(brand.toString()) + "/api/payment?regulator=" + regulator.toString().toLowerCase() + "&dataType=1";

		header = new HashMap<>();
		header.put("Authorization","Bearer "+ davinciToken);

		try {
			HttpResponse response = httpClient.getGetResponse(path, header);
			JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, path, "", result );
			assertEquals(result.getString("status"), "success", "API Failed!! Path: " + path + "\n" + result);
            assertFalse(result.getJSONObject("data").getJSONArray("payment").isEmpty(), "API response is empty!! Please check\n" + result);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	//davinci portal - payment currency api
	public void apiDavinciPaymentCurrency (String davinciToken){
		String path = getDavinciURL(brand.toString()) + "/api/currency?regulator=" + regulator.toString().toLowerCase();

		header = new HashMap<>();
		header.put("Authorization","Bearer "+ davinciToken);

		try {
			HttpResponse response = httpClient.getGetResponse(path, header);
			JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, path, "", result );
			assertEquals(result.getString("status"), "success", "API Failed!! Path: " + path + "\n" + result);
            assertFalse(result.getJSONObject("data").getJSONArray("currency").isEmpty(), "API response is empty!! Please check\n" + result);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	//davinci portal - country api
	public void apiDavinciPaymentCountries (String davinciToken){
		String path = getDavinciURL(brand.toString())+ "/api/country?regulator=" + regulator.toString().toLowerCase();

		header = new HashMap<>();
		header.put("Authorization","Bearer "+ davinciToken);

		try {
			HttpResponse response = httpClient.getGetResponse(path, header);
			JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, path, "", result );
			assertEquals(result.getString("status"), "success", "API Failed!! Path: " + path + "\n" + result);
            assertFalse(result.getJSONObject("data").getJSONArray("country").isEmpty(), "API response is empty!! Please check\n" + result);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	//davinci portal - account type api
	public void apiDavinciPaymentAcctType (String davinciToken){
		String path = getDavinciURL(brand.toString()) + "/api/account-type";

		header = new HashMap<>();
		header.put("Authorization","Bearer "+ davinciToken);

		try {
			HttpResponse response = httpClient.getGetResponse(path, header);
			JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, path, "", result );
			assertEquals(result.getString("status"), "success", "API Failed!! Path: " + path + "\n" + result);
            assertFalse(result.getJSONObject("data").getJSONObject("accountTypes").isEmpty(), "API response is empty!! Please check\n" + result);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	//davinci portal - withdrawal method api
	public void apiDavinciPaymentWDMethod (String davinciToken){
		String path = getDavinciURL(brand.toString()) + "/api/payment?regulator=" + regulator.toString().toLowerCase() + "&dataType=2";

		header = new HashMap<>();
		header.put("Authorization","Bearer "+ davinciToken);

		try {
			HttpResponse response = httpClient.getGetResponse(path, header);
			JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, path, "", result );
			assertEquals(result.getString("status"), "success", "API Failed!! Path: " + path + "\n" + result);
            assertFalse(result.getJSONObject("data").getJSONArray("payment").isEmpty(), "API response is empty!! Please check\n" + result);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	//davinci portal - withdrawal status api
	public void apiDavinciPaymentWDStatus (String davinciToken){
		String path = getDavinciURL(brand.toString()) + "/api/payment-status?regulator=" + regulator.toString().toLowerCase() + "&dataType=2" ;

		header = new HashMap<>();
		header.put("Authorization","Bearer "+ davinciToken);

		try {
			HttpResponse response = httpClient.getGetResponse(path, header);
			JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, path, "", result );
			assertEquals(result.getString("status"), "success", "API Failed!! Path: " + path + "\n" + result);
            assertFalse(result.getJSONObject("data").getJSONArray("paymentStatus").isEmpty(), "API response is empty!! Please check\n" + result);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	//davinci portal - deposit/withdrawal report
	public void apiDavinciDepWithdReport (String davinciToken){
		String path = getDavinciURL(brand.toString()) + "/api/deposit";

		JSONObject body = new JSONObject()
				.fluentPut("regulator", regulator.toString().toLowerCase())
				.fluentPut("pageNo", 1)
				.fluentPut("pageSize", 10)
				.fluentPut("sort", new JSONObject())
				.fluentPut("filter", new JSONObject().fluentPut("accounttype", new JSONArray()))
				.fluentPut("from", GlobalMethods.setFromDateOnly())
				.fluentPut("to", GlobalMethods.setToDateOnly())
				.fluentPut("accountType", 1)
				.fluentPut("levelId", "")
				.fluentPut("showAll", true)
				.fluentPut("payments", new JSONArray()
						.fluentAdd(new JSONObject()
								.fluentPut("dataType", 2)
								.fluentPut("paymentStatus", new JSONArray()
										.fluentAdd("7")
										.fluentAdd("16")
										.fluentAdd("17")))
						.fluentAdd(new JSONObject()
								.fluentPut("dataType", 3))
						.fluentAdd(new JSONObject()
								.fluentPut("dataType", 1)
								.fluentPut("paymentStatus", new JSONArray()
										.fluentAdd("5"))));

		header = new HashMap<>();
		header.put("Content-Type", "application/json");
		header.put("Authorization","Bearer "+ davinciToken);

		try {
			HttpResponse response = httpClient.getPostResponse(path, header, body);
			JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, path, body, result );
			assertEquals(result.getString("status"), "success", "API Failed!! Path: " + path + "\n" + result);
            assertFalse(result.getJSONObject("data").getJSONArray("deposit").isEmpty(), "API response is empty!! Please check\n" + result);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	//davinci portal - deposit/withdrawal report - summary
	public void apiDavinciDepWithdReportSummary (String davinciToken){
		String path = getDavinciURL(brand.toString())+ "/api/deposit/summary";

		JSONObject body = new JSONObject()
				.fluentPut("regulator", regulator.toString().toLowerCase())
				.fluentPut("filter", new JSONObject().fluentPut("accounttype", new JSONArray()))
				.fluentPut("from", GlobalMethods.setFromDateOnly())
				.fluentPut("to", GlobalMethods.setToDateOnly())
				.fluentPut("accountType", 1)
				.fluentPut("levelId", "")
				.fluentPut("showAll", true)
				.fluentPut("payments", new JSONArray()
						.fluentAdd(new JSONObject()
								.fluentPut("dataType", 2)
								.fluentPut("paymentStatus", new JSONArray()
										.fluentAdd("7")
										.fluentAdd("16")
										.fluentAdd("17")))
						.fluentAdd(new JSONObject()
								.fluentPut("dataType", 3))
						.fluentAdd(new JSONObject()
								.fluentPut("dataType", 1)
								.fluentPut("paymentStatus", new JSONArray()
										.fluentAdd("5"))));

		header = new HashMap<>();
		header.put("Content-Type", "application/json");
		header.put("Authorization","Bearer "+ davinciToken);

		try {
			HttpResponse response = httpClient.getPostResponse(path, header, body);
			JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, path, body, result );
			assertEquals(result.getString("status"), "success", "API Failed!! Path: " + path + "\n" + result);
            assertFalse(result.getJSONObject("data").getJSONArray("summary").isEmpty(), "API response is empty!! Please check\n" + result);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	//davinci portal - deposit/withdrawal daily report
	public void apiDavinciDepWithDailyReport (String davinciToken){
		String path = getDavinciURL(brand.toString()) + "/api/deposit/daily_report";

		JSONObject body = new JSONObject();
		body.put("regulator",regulator.toString().toLowerCase());
		body.put("pageNo",1);
		body.put("pageSize",10);
		body.put("sort", new JSONObject());

		header = new HashMap<>();
		header.put("Content-Type", "application/json");
		header.put("Authorization","Bearer "+ davinciToken);

		try {
			HttpResponse response = httpClient.getPostResponse(path, header, body);
			JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, path, body, result );
			assertEquals(result.getString("status"), "success", "API Failed!! Path: " + path + "\n" + result);
            assertFalse(result.getJSONObject("data").getJSONArray("fileList").isEmpty(), "API response is empty!! Please check\n" + result);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	//davinci portal - first time deposit report
	public void apiDavinciFTDReport (String davinciToken){
		String path = getDavinciURL(brand.toString()) + "/api/deposit/ftd";

		JSONObject body = new JSONObject();
		body.put("regulator", regulator.toString().toLowerCase());
		body.put("pageNo", 1);
		body.put("pageSize", 10);
		body.put("sort", new JSONObject());
		body.put("filter", new JSONObject().fluentPut("accounttype", new JSONArray()));
		body.put("from", GlobalMethods.setFromDateOnly());
		body.put("to", GlobalMethods.setToDateOnly());
		body.put("levelId", "");
		body.put("showAll", true);

		header = new HashMap<>();
		header.put("Content-Type", "application/json");
		header.put("Authorization","Bearer "+ davinciToken);

		try {
			HttpResponse response = httpClient.getPostResponse(path, header, body);
			JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, path, body, result );
			assertEquals(result.getString("status"), "success", "API Failed!! Path: " + path + "\n" + result);
            assertFalse(result.getJSONObject("data").getJSONArray("first_time_deposit").isEmpty(), "API response is empty!! Please check\n" + result);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	//search with username and type (cc 1 or unionpay 4 or both -1)
	public JSONObject apiFinancialInfoAuditSearch (String userName, String type){
		String path = this.url + "/payment/withdraw/getFinanceInfoAuditList";

		JSONObject body = new JSONObject();
		body.put("limit", 10);
		body.put("pageNo", 1);
		body.put("offset", 0);
		body.put("order", "asc");
			JSONObject search = new JSONObject();
			search.put("statusQuery","-1");
			search.put("typeQuery",type);
			search.put("searchType","0");
			search.put("userQuery",userName);
			search.put("directLevel",5);
			search.put("user_id","");
			search.put("org_id","");
		body.put("search",search);

		header.put("Content-Type", "application/json");

		JSONObject result = null;
		try {
			HttpResponse response = httpClient.getPostResponse(path, header, body);
			result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, path, body, result );
			assertTrue(result.getBoolean("success"), "API Failed!! Path: " + path + "\n" + result);
            assertFalse(result.getJSONArray("rows").isEmpty(), "API response is null!! Path: " + path + "\n" + result);

			Integer apiRecordCount = result.getInteger("total");
			Integer dbRecordCount = paymentDB.getCCUnionpayByNameType(userName,type).size();
			assertEquals(apiRecordCount, dbRecordCount, "AP number of records != DB number of records\n"
					+ "Total of API Records: "+apiRecordCount+ "\nTotal of DB Records: "+dbRecordCount);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	//status approve = 2, reject = 3
	public void apiFinancialInfoAuditUnionpay (String status, String unionpayID, String userID){
		String path = this.url + "/payment/withdraw/unionCardAudit/submit";

		HashMap<String,String> body = new HashMap<>();
		body.put("id", unionpayID);
		body.put("status",status);
		body.put("user_Id",userID);
		body.put("processed_notes", "testadminapi audit");
		header.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

		try {
			HttpResponse response = httpClient.getPostResponse(path, header, body);
			JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, path, body, result );
			assertTrue("true".equals(result.getString("success")) && "操作成功".equals(result.getString("message")), "API Failed!! Path: " + path + "\n" + result);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void apiFinancialInfoDisableCC (String ccID){
		String path = this.url + "/payment/withdraw/creditCardAudit/disable/" + ccID;
		String body = "";

		try {
			HttpResponse response = httpClient.getPostResponse(path, header, body);
			JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			assertTrue("true".equals(result.getString("success")) && "操作成功".equals(result.getString("message")), "API Failed!! Path: " + path + "\n" + result);
			this.printAPIInfo(brand, regulator, path, "", result );
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void apiFinancialInfoEnableCC (String ccID){
		String path = this.url + "/payment/withdraw/creditCardAudit/enable/" + ccID;
		String body = "";

		try {
			HttpResponse response = httpClient.getPostResponse(path, header, body);
			JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, path, "", result );
			assertTrue("true".equals(result.getString("success")) && "操作成功".equals(result.getString("message")), "API Failed!! Path: " + path + "\n" + result);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	//search with status 1 (submitted) and username
	public JSONObject apiAcctTransferAuditSearch (String userName, String status){
		String path = this.url + "/payment/transfer/query_PaymentTransferList";

		JSONObject body = new JSONObject();
		body.put("limit", 10);
		body.put("pageNo", 1);
		body.put("offset", 0);
		body.put("order", "asc");
			JSONObject search = new JSONObject();
			search.put("statusQuery",status);
			search.put("typeQuery",-1);
			search.put("searchType","1");
			search.put("userQuery",userName);
			search.put("directLevel",5);
			search.put("markType",-1);
			search.put("user_id","");
			search.put("org_id","");
			search.put("transferDate1", GlobalMethods.setFromDateOnly());
			search.put("transferDate2", GlobalMethods.setToDateOnly());
		body.put("search",search);

		header.put("Content-Type", "application/json");
		JSONObject result = null;
		try {
			HttpResponse response = httpClient.getPostResponse(path, header, body);
			result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, path, body, result );
			assertTrue(result.getBoolean("success"), "API Failed!! Path: " +path+"\n" + result);
            assertFalse(result.getJSONArray("rows").isEmpty(), "API response is null!! Path: " + path + "\n" + result);

			Integer apiRecordCount = result.getInteger("total");
			Integer dbRecordCount = paymentDB.getTransferRecordByNameStatus(userName, status, GlobalMethods.setFromDateOnly(), GlobalMethods.setToDateTime()).size();
			assertEquals(apiRecordCount, dbRecordCount, "AP number of records != DB number of records\n"
					+ "Total of API Records: "+apiRecordCount+ "\nTotal of DB Records: "+dbRecordCount);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public JSONObject apiGetTransferFromAcctInfo (String fromAcct){
		String path = this.url + "/payment/transfer/get-from-account-info";

		HashMap<String,String> body = new HashMap<>();
		body.put("fromAccount", fromAcct);

		header.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

		JSONObject result = null;
		try {
			HttpResponse response = httpClient.getPostResponse(path, header, body);
			result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, path, body, result );
			assertEquals(result.getString("code"), "0", "API Failed!! Path: " + path + "\n" + result);
			assertFalse(result.getJSONObject("data").isEmpty(), "API response is null!! Path: " + path + "\n" + result);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public void apiAddAcctTransferRecord (String fromAcct, String fromAcctName, String fromAcctBalance, String currency, String toAcct, String amount){
		String path = this.url + "/payment/transfer/apply-sub-ib-transfer";

		JSONObject body = new JSONObject();
		body.put("accountBalance", fromAcctBalance);
		body.put("accountName", fromAcctName);
		body.put("applicationNotes", "testadminapi");
		body.put("fromAccount", fromAcct);
		body.put("subIbAccountList", new JSONArray()
				.fluentAdd(new JSONObject()
						.fluentPut("subIbAccount",toAcct)
						.fluentPut("currency",currency)
						.fluentPut("amount",amount))
				.fluentAdd(new JSONObject()
						.fluentPut("subIbAccount",toAcct)
						.fluentPut("currency", currency)
						.fluentPut("amount",amount))
				.fluentAdd(new JSONObject()
						.fluentPut("subIbAccount",toAcct)
						.fluentPut("currency", currency)
						.fluentPut("amount",amount))
				.fluentAdd(new JSONObject()
						.fluentPut("subIbAccount",toAcct)
						.fluentPut("currency", currency)
						.fluentPut("amount",amount))
		);

		header.put("Content-Type", "application/json");

		try {
			HttpResponse response = httpClient.getPostResponse(path, header, body);
			JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, path, body, result );
			assertEquals(result.getString("code"), "0", "API Failed!! Path: " + path + "\n" + result);
			assertFalse(result.getJSONObject("data").isEmpty(), "API response is null. Path: " + path + "\n" + result);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public JSONObject apiAcctTransferAuditReject (String recordID, String balance){
		String path = this.url + "/payment/transfer/auditTransfer";

		HashMap<String, String> body = new HashMap<>();
		body.put("id", recordID);
		body.put("refuseReason", "testadminapi reject");
		body.put("actualAmount", balance);
		body.put("transferCreditType", "N/A");
		body.put("transferCredit", "0");
		body.put("deductCredit","0");
		body.put("status","reject");

		header.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

		JSONObject result = null;
		try {
			HttpResponse response = httpClient.getPostResponse(path, header, body);
			result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, path, body, result );
			assertTrue("true".equals(result.getString("success")) && "Rejected".equals(result.getString("message")), "API Failed!! Path: " + path + "\n" + result);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public JSONObject apiAcctTransferAuditApprove (String recordID, String balance){
		String path = this.url + "/payment/transfer/auditTransfer";

		HashMap<String, String> body = new HashMap<>();
		body.put("id", recordID);
		body.put("refuseReason", "testadminapi approve");
		body.put("actualAmount", balance);
		body.put("transferCreditType", "N/A");
		body.put("transferCredit", "0");
		body.put("deductCredit","0");
		body.put("status","sub");

		header.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

		JSONObject result = null;
		try {
			HttpResponse response = httpClient.getPostResponse(path, header, body);
			result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, path, body, result );
			assertTrue("true".equals(result.getString("success")) && "Re-transfer succeeded".equals(result.getString("message")), "API Failed!! Path: " + path + "\n" + result);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public JSONObject apiAcctTransferBulkReject (String recID1, String recID2){
		String path = this.url + "/payment/transfer/rejectPaymentTransfer";

		JSONObject body = new JSONObject();
		body.put("refuseReason", "rej");
		body.put("transferIdList", new JSONArray()
				.fluentAdd(recID1)
				.fluentAdd(recID2));

		header.put("Content-Type", "application/json");

		JSONObject result = null;
		try {
			HttpResponse response = httpClient.getPostResponse(path, header, body);
			result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, path, body, result );
			assertTrue("true".equals(result.getString("success")) && "操作成功".equals(result.getString("message")), "API Failed!! Path: " + path + "\n" + result);
			JSONArray successAddList = result.getJSONObject("data").getJSONArray("successTransferIdList");
			assertTrue(successAddList.contains(Integer.valueOf(recID1)) && successAddList.contains(Integer.valueOf(recID2)), "Not all expected transfer IDs found! Path: " + path + "\n" + result);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public void apiCCTransactionAuditSearch (){
		String path = this.url + "/payment/card/transactionList";

		JSONObject body = new JSONObject();
		body.put("skipCount", true);
		body.put("pagination", new JSONObject()
				.fluentPut("pageNo",1)
				.fluentPut("offset",0)
				.fluentPut("limit",10));
		body.put("parameters", new JSONObject()
				.fluentPut("userId", new JSONObject()
						.fluentPut("filterType","INPUT")
						.fluentPut("input",""))
				.fluentPut("mtAccount", new JSONObject()
						.fluentPut("filterType","INPUT")
						.fluentPut("input",""))
				.fluentPut("createTime", new JSONObject()
						.fluentPut("filterType","DATEPICKER")
						.fluentPut("input",new JSONObject()
								.fluentPut("startDate", GlobalMethods.setFromDateTime())
								.fluentPut("endDate", GlobalMethods.setToDateTime()))));


		header.put("Content-Type", "application/json");

		try {
			HttpResponse response = httpClient.getPostResponse(path, header, body);
			JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, path, body, result );
			assertTrue(result.getBoolean("success"), "API Failed!! Path: " +path+"\n" + result);
            assertFalse(result.getJSONArray("rows").isEmpty(), "API response is null!!  Path: " +path+"\n" + result);

			Integer apiRecordCount = result.getInteger("total");
			Integer dbRecordCount = paymentDB.getCCTransactionRecordByDate(GlobalMethods.setFromDateTime(), GlobalMethods.setToDateTime()).size();
			assertEquals(apiRecordCount, dbRecordCount, "AP number of records != DB number of records\n"
					+ "Total of API Records: "+apiRecordCount+ "\nTotal of DB Records: "+dbRecordCount);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public JSONObject apiCCTransctionQueryCC (String userId){
		String path = this.url + "/payment/card/queryCreditCardByUserId";
		HashMap<String,String> body = new HashMap<>();
		body.put("userId", userId);

		header.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

		JSONObject result = null;
		try {
			HttpResponse response = httpClient.getPostResponse(path, header, body);
			JSONArray arrayResult = JSON.parseArray(EntityUtils.toString(response.getEntity(),"UTF-8"));
			assertFalse(arrayResult.isEmpty(),"No credit card available for this user");
            this.printAPIInfo(brand, regulator, path, body, arrayResult );
			result = arrayResult.getJSONObject(0);
			assertEquals(userId, result.getString("userId"), "API Result incorrect!! Record does not belong to userID: "+userId+ "\n" + result);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	//require to consider not using JSONObject as param, not reusable in normal scenario
	public void apiCCTranscInsertAdjustment (JSONObject ccOrderInfo, String adjAmt){
		String path = this.url + "/payment/card/insertAdjustmentDetails";

		JSONObject body = new JSONObject();
		body.put("userId", ccOrderInfo.getString("userId"));
		body.put("tradingAccount", ccOrderInfo.getString("mt4Account"));
		body.put("creditCardId", ccOrderInfo.getString("creditCardId"));
		body.put("channelId", ccOrderInfo.getString("paymentChannel"));
		body.put("currency", ccOrderInfo.getString("currency"));
		body.put("orderNumber", ccOrderInfo.getString("orderNumber"));
		body.put("processNote", "testapiadmin");
		body.put("adjustmentAmount", adjAmt);

		header.put("Content-Type", "application/json");

		try {
			HttpResponse response = httpClient.getPostResponse(path, header, body);
			JSONObject result= JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, path, body, result );
			assertTrue("true".equals(result.getString("success")) && "Adjustment success!".equals(result.getString("message")), "API Failed!! Path: " +path+"\n" + result);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	//require to consider not using JSONObject as param, not reusable in normal scenario
	public void apiCCTranscBulkAdjustment (JSONObject ccInfo){
		String path = this.url + "/payment/card/bulkAdjustment";

		JSONArray bodyArray = new JSONArray();
		JSONObject body = new JSONObject();
			body.put("channelId", ccInfo.getString("paymentChannel"));
			body.put("creditCardId", ccInfo.getString("creditCardId"));
			body.put("userId", ccInfo.getString("userId"));
			body.put("creditCardNumber", ccInfo.getString("begin6Digits") + "**** **" + ccInfo.getString("last4Digits"));
			body.put("tradingAccount", ccInfo.getString("mt4Account"));
			body.put("channel", ccInfo.getString("Bridger-Googlepay"));
			body.put("pspName", ccInfo.getString(null));
			body.put("currency", ccInfo.getString("currency"));
			body.put("orderNumber", ccInfo.getString("orderNumber"));
			body.put("adjustmentAmount", "1");
			body.put("processNote", "testapiadminbulk");
			body.put("isValid", true);
			body.put("flag", true);
			body.put("uniqueId", "37378662910");
		bodyArray.add(body);

		header.put("Content-Type", "application/json");

		try {
			HttpResponse response = httpClient.getPostResponse(path, header, bodyArray);
			JSONObject result= JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, path, body, result );
			assertTrue("true".equals(result.getString("success")) && "Adjustment success!".equals(result.getString("message")), "API Failed!! Path: " +path+"\n" + result);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	//search with name
	public JSONObject apiCCArchiveAuditSearch (String userName){
		String path = this.url + "/admin/card-audit/cardAuditList";

		JSONObject body = new JSONObject();
		body.put("skipCount", false);
		body.put("pagination", new JSONObject()
				.fluentPut("pageNo",1)
				.fluentPut("offset",0)
				.fluentPut("limit",10));
		body.put("parameters", new JSONObject()
				.fluentPut("userId", new JSONObject()
						.fluentPut("filterType","INPUT")
						.fluentPut("input",""))
				.fluentPut("clientName", new JSONObject()
						.fluentPut("filterType","INPUT")
						.fluentPut("input",userName))
				.fluentPut("createTime", new JSONObject()
						.fluentPut("filterType","DATEPICKER")
						.fluentPut("input",new JSONObject()
								.fluentPut("startDate","2017-01-01 00:00:00")
								.fluentPut("endDate", GlobalMethods.setToDateTime()))));


		header.put("Content-Type", "application/json");

		JSONObject result = null;
		try {
			HttpResponse response = httpClient.getPostResponse(path, header, body);
			result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, path, body, result );
			assertTrue(result.getBoolean("success"), "API Failed!! Path: " +path+"\n" + result);
            assertFalse(result.getJSONArray("rows").isEmpty(), "API response is null!! Path: " + path + "\n" + result);

			Integer apiRecordCount = result.getInteger("total");
			Integer dbRecordCount = paymentDB.getCCArchiveRecordByName(userName, GlobalMethods.setToDateTime()).size();
			assertEquals(apiRecordCount, dbRecordCount, "AP number of records != DB number of records\n"
					+ "Total of API Records: "+apiRecordCount+ "\nTotal of DB Records: "+dbRecordCount);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	//status approve = 2, reject = 3, pending = 1
	public void apiCCArchiveAudit (String ccID, String userID, String status){
		String path = this.url + "/admin/card-audit/submit";
		HashMap<String,String> body = new HashMap<>();
		body.put("id",ccID);
		body.put("userId", userID);
		body.put("status", status);
		body.put("ccStatementReplacementUrl","");
		body.put("ccBankLetterReplacementUrl","");
		body.put("processedNotes","testadminapi");
		if ("3".equals(status)){
			body.put("rejectReason","1");
		}

		header.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

		try {
			HttpResponse response = httpClient.getPostResponse(path, header, body);
			JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, path, body, result );
			assertTrue("true".equals(result.getString("success")) && "操作成功".equals(result.getString("message")), "API Failed!! Path: " +path+"\n" + result);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	//Search with submitted status (1) and account number
	public JSONObject apiCashAdjustmentAuditSearch (String account, String status){
		String path = this.url + "/account/adjustment/query_adjustmentList";

		JSONObject body = new JSONObject();
		body.put("skipCount", false);
		body.put("pagination", new JSONObject()
				.fluentPut("pageNo",null));
		body.put("parameters", new JSONObject()
				.fluentPut("senderMt4AccountId", new JSONObject()
						.fluentPut("fuzzy",true)
						.fluentPut("filterType","INPUT")
						.fluentPut("input",account))
				.fluentPut("status", new JSONObject()
						.fluentPut("filterType","SELECT")
						.fluentPut("input", new JSONArray()
								.fluentAdd(status)))
				.fluentPut("transactionInvocationDate", new JSONObject()
						.fluentPut("filterType","DATEPICKER")
						.fluentPut("input",new JSONObject()
								.fluentPut("startDate", GlobalMethods.setFromDateTime())
								.fluentPut("endDate", GlobalMethods.setToDateTime()))));


		header.put("Content-Type", "application/json");

		JSONObject result = null;
		try {
			HttpResponse response = httpClient.getPostResponse(path, header, body);
			result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, path, body, result );
			assertTrue(result.getBoolean("success"), "API Failed!! Path: " +path+"\n" + result);
            assertFalse(result.getJSONArray("rows").isEmpty(), "API response is null!! Path: " + path + "\n" + result);
			assertEquals(account,result.getJSONArray("rows").getJSONObject(0).getString("senderMt4AccountId"), "API result incorrect, result not belong to account: "+account+"\n" + result);

			Integer apiRecordCount = result.getInteger("total");
			Integer dbRecordCount = paymentDB.getCARecordByAcctStatus(account, status, GlobalMethods.setFromDateTime(), GlobalMethods.setToDateTime()).size();
			assertEquals(apiRecordCount, dbRecordCount, "AP number of records != DB number of records\n"
					+ "Total of API Records: "+apiRecordCount+ "\nTotal of DB Records: "+dbRecordCount);

		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public JSONObject apiCashAdjustmentCheckAcct (String account){
		String path = this.url + "/account/adjustment/checkAccount";
		HashMap<String,String> body = new HashMap<>();
		body.put("account",account);

		header.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

		JSONObject result = null;
		try {
			HttpResponse response = httpClient.getPostResponse(path, header, body);
			result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, path, body, result );
			assertTrue(result.getBoolean("success"), "API Failed!! Path: " +path+"\n" + result);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	//Type 1 = deposit, 2 = withdraw
	public void apiCashAdjustmentCheckAmount (String account, String type){
		String path = this.url + "/account/adjustment/verifyAdjustAmount";
		HashMap<String,String> body = new HashMap<>();
		body.put("login",account);
		body.put("money","1");
		body.put("amount","1");
		body.put("type",type);

		header.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

		try {
			HttpResponse response = httpClient.getPostResponse(path, header, body);
			String result = EntityUtils.toString(response.getEntity(),"UTF-8");
			this.printAPIInfo(brand, regulator, path, body, result );
			assertTrue(Boolean.parseBoolean(result.trim()),"API Failed!! Path: " +path+"\n" + result);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	//Type 1 = deposit, 2 = withdraw
	public void apiCashAdjustmentCheckRecord (String userID, String type){
		String path = this.url + "/account/adjustment/check_records_add";
		HashMap<String,String> body = new HashMap<>();
		body.put("userId",userID);
		body.put("senderAmount","1");
		body.put("transactionType",type);

		header.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

		try {
			HttpResponse response = httpClient.getPostResponse(path, header, body);
			JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, path, body, result );
			assertTrue(result.getBoolean("success"),"API Failed!! Path: " +path+"\n" + result);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	//Type 1 = deposit, 2 = withdraw
	public void apiCashAdjustmentAddRecord (String account, String currency, String amount, String type){
		String path = this.url + "/account/adjustment/adjustmentAddSubmit";
		HashMap<String,String> body = new HashMap<>();
		body.put("account",account);
		body.put("money",amount);
		body.put("currency",currency);
		body.put("type",type);
		body.put("remark","testadminapi");
		body.put("ticketId","testadminapi");

		header.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

		try {
			HttpResponse response = httpClient.getPostResponse(path, header, body);
			JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, path, body, result );
			assertTrue("true".equals(result.getString("success")) && "操作成功".equals(result.getString("message")),"API Failed!! Path: " +path+"\n" + result);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public String apiCashAdjustmentUploadFile (){
		String path = this.url + "/account/adjustment/adjustmentFileupload";

		Map<String, String> fileMap = new HashMap<>();
		fileMap.put("VFX", "/src/main/resources/newcrm/data/csvDataSource/CashAdjustment/AUData.csv");
		fileMap.put("AU", "/src/main/resources/newcrm/data/csvDataSource/CashAdjustment/AUData.csv");
		fileMap.put("PUG", "/src/main/resources/newcrm/data/csvDataSource/CashAdjustment/PUGData.csv");
		fileMap.put("VT", "/src/main/resources/newcrm/data/csvDataSource/CashAdjustment/VTData.csv");
		fileMap.put("STAR", "/src/main/resources/newcrm/data/csvDataSource/CashAdjustment/STARData.csv");
		fileMap.put("VJP", "/src/main/resources/newcrm/data/csvDataSource/CashAdjustment/VJPData.csv");
		fileMap.put("UM", "/src/main/resources/newcrm/data/csvDataSource/CashAdjustment/UMData.csv");
		fileMap.put("MO", "/src/main/resources/newcrm/data/csvDataSource/CashAdjustment/MOData.csv");

		File file = new File(Utils.workingDir + fileMap.get(brand.toString().toUpperCase()));

		header.remove("Content-Type");
		JSONObject result = null;
		try {
			HttpResponse response = httpClient.uploadFilePostResponse(path, header, file);
			result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, path, "", result );
			assertTrue(result.getBoolean("success"),"API Failed!! Path: " +path+"\n" + result);
			assertTrue(!result.getJSONArray("data").isEmpty(),"API Response is empty!! Path: " +path+"\n" + result);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result.getString("message");
	}

	public void apiCashAdjustmentCheckBulkRecord (String adjustmentKey){
		String path = this.url + "/account/adjustment/check_records_bulk_add";
		HashMap<String,String> body = new HashMap<>();
		body.put("adjustmentListKey",adjustmentKey);
		body.put("deleteIds","");

		header.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

		try {
			HttpResponse response = httpClient.getPostResponse(path, header, body);
			JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, path, body, result );
			assertTrue(result.getBoolean("success"),"API Failed!! Path: " +path+"\n" + result);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void apiCashAdjustmentInsertBulkRecord (String adjustmentKey){
		String path = this.url + "/account/adjustment/insertToTable";
		HashMap<String,String> body = new HashMap<>();
		body.put("adjustmentListKey",adjustmentKey);
		body.put("deleteIds","");

		header.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

		try {
			HttpResponse response = httpClient.getPostResponse(path, header, body);
			JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, path, body, result );
			assertTrue("true".equals(result.getString("success")) && "操作成功".equals(result.getString("message")) ,"API Failed!! Path: " +path+"\n" + result);
			assertTrue(result.getJSONObject("data").getInteger("successNum") == 1, "API incorrect result!! Path: " +path+"\n" + result);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void apiCashAdjustmentCheckApproveRecord (JSONObject recordDetails){
		String path = this.url + "/account/adjustment/check_records_approve";

		JSONArray bodyArray = new JSONArray();
		JSONObject body = new JSONObject();
		body.put("transactionId", recordDetails.getInteger("transactionId"));
		body.put("operatorId", recordDetails.getInteger("operatorId"));
		body.put("senderMt4AccountId", recordDetails.getInteger("senderMt4AccountId"));
		body.put("transactionType", recordDetails.getInteger("transactionType"));
		body.put("senderAmount", recordDetails.getDoubleValue("senderAmount"));
		body.put("remark", recordDetails.getString("remark"));
		body.put("transactionInvocationDate", recordDetails.getString("transactionInvocationDate"));
		body.put("functionCode", recordDetails.getInteger("functionCode"));
		body.put("ticketId", recordDetails.getString("ticketId"));
		body.put("status", recordDetails.getInteger("status"));
		body.put("applicant", "cmatest");
		body.put("accountDealType", recordDetails.getInteger("accountDealType"));
		bodyArray.fluentAdd(body);

		header.put("Content-Type", "application/json");

		try {
			HttpResponse response = httpClient.getPostResponse(path, header, bodyArray);
			JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, path, body, result );
			assertTrue(result.getBoolean("success"), "API Failed!! Path: " +path+"\n" + result);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void apiCashAdjustmentAuditApprove(JSONObject recordDetails){
		String path = this.url + "/account/adjustment/submitAdjustBulkApprove";

		JSONArray bodyArray = new JSONArray();
		JSONObject body = new JSONObject();
		body.put("transactionId", recordDetails.getInteger("transactionId"));
		body.put("operatorId", recordDetails.getInteger("operatorId"));
		body.put("senderMt4AccountId", recordDetails.getInteger("senderMt4AccountId"));
		body.put("transactionType", recordDetails.getInteger("transactionType"));
		body.put("senderAmount", recordDetails.getDoubleValue("senderAmount"));
		body.put("remark", recordDetails.getString("remark"));
		body.put("transactionInvocationDate", recordDetails.getString("transactionInvocationDate"));
		body.put("functionCode", recordDetails.getInteger("functionCode"));
		body.put("ticketId", recordDetails.getString("ticketId"));
		body.put("status", recordDetails.getInteger("status"));
		body.put("applicant", "cmatest");
		body.put("accountDealType", recordDetails.getInteger("accountDealType"));
		bodyArray.fluentAdd(body);

		header.put("Content-Type", "application/json");

		try {
			HttpResponse response = httpClient.getPostResponse(path, header, bodyArray);
			JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, path, body, result );
			assertTrue("true".equals(result.getString("success")) && "操作成功".equals(result.getString("message")), "API Failed!! Path: " +path+"\n" + result);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void apiCashAdjustmentAuditReject(JSONObject recordDetails){
		String path = this.url + "/account/adjustment/submitAdjustBulkReject";

		JSONObject body = new JSONObject();
		body.put("reason","testadminapi");
		body.put("records",new JSONArray()
				.fluentAdd(new JSONObject()
						.fluentPut("transactionId", recordDetails.getInteger("transactionId"))
						.fluentPut("operatorId", recordDetails.getInteger("operatorId"))
						.fluentPut("senderMt4AccountId", recordDetails.getInteger("senderMt4AccountId"))
						.fluentPut("transactionType", recordDetails.getInteger("transactionType"))
						.fluentPut("senderAmount", recordDetails.getDoubleValue("senderAmount"))
						.fluentPut("remark", recordDetails.getString("remark"))
						.fluentPut("transactionInvocationDate", recordDetails.getString("transactionInvocationDate"))
						.fluentPut("functionCode", recordDetails.getInteger("functionCode"))
						.fluentPut("ticketId", recordDetails.getString("ticketId"))
						.fluentPut("status", recordDetails.getInteger("status"))
						.fluentPut("applicant", "cmatest")
						.fluentPut("accountDealType", recordDetails.getInteger("accountDealType"))));

		header.put("Content-Type", "application/json");

		try {
			HttpResponse response = httpClient.getPostResponse(path, header, body);
			JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, path, body, result );
			assertTrue("true".equals(result.getString("success")) && "操作成功".equals(result.getString("message")), "API Failed!! Path: "+path+"\n" + result);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void apiLBTTransactionAuditSearch(String userID){
		String path = this.url + "/payment/lbt/transactionList";

		JSONObject body = new JSONObject();
		body.put("skipCount",false);
		body.put("pagination", new JSONObject()
				.fluentPut("limit",10)
				.fluentPut("pageNo",1)
				.fluentPut("offset",0)
				.fluentPut("order","desc")
				.fluentPut("sort","createTime"));
		body.put("parameters", new JSONObject()
				.fluentPut("userId", new JSONObject()
						.fluentPut("filterType","INPUT")
						.fluentPut("input",userID)));

		header.put("Content-Type", "application/json");

		try {
			HttpResponse response = httpClient.getPostResponse(path, header, body);
			JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, path, body, result );
			assertTrue(result.getBoolean("success"), "API Failed!! Path: "+path+"\n" + result);
			assertFalse(result.getJSONArray("rows").isEmpty(), "API response is null!! Path: " + path + "\n" + result);
			assertEquals(userID, result.getJSONArray("rows").getJSONObject(0).getString("userId"), "Incorrect LBT search result, not belong to "+userID+"!! \nResponse: " + result);

			Integer apiRecordCount = result.getInteger("total");
			Integer dbRecordCount = paymentDB.getLBTRecordByUserID(userID).size();
			assertEquals(apiRecordCount, dbRecordCount, "AP number of records != DB number of records\n"
					+ "Total of API Records: "+apiRecordCount+ "\nTotal of DB Records: "+dbRecordCount);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void apiLBTTransactionAuditCheckBalance(String userID){
		String path = this.url + "/payment/lbt/getCurrentUserLbtBalance";

		JSONObject body = new JSONObject();
		body.put("userId",userID);

		header.put("Content-Type", "application/json");

		try {
			HttpResponse response = httpClient.getPostResponse(path, header, body);
			JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, path, body, result );
			assertTrue("true".equals(result.getString("success")) && "操作成功".equals(result.getString("message")), "API Failed!! Path: "+path+"\n" + result);
			assertTrue(result.getJSONObject("data").getInteger("USD") > 0, "Incorrect LBT data result!! Response: " + result);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void apiLBTTransactionAuditInsertAdjustment(String userID, String currency, String amount){
		header.put("Content-Type", "application/json");
		HyTechUtils.genXSourceHeader(header);
		String fullPath = this.url + HyTechUrl.LBT_ADDLBTBALANCE_BYCURRENCY;

		JSONObject body = new JSONObject();
		body.put("userId",userID);
		body.put("currency",currency);
		body.put("transactionAmount",amount);
		body.put("processedNotes","testadminapi");

		try {
			HttpResponse response = httpClient.getPostResponse(fullPath, header, body);
			JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, fullPath, body, result );
			assertTrue("true".equals(result.getString("success")) && "操作成功".equals(result.getString("message")), "Failed to adjust LBT balance!!");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	//search with status = Accepted (1) and userID
	public JSONObject apiUnionpayWithdrawalSearch(String email){
		String path = this.url + "/payment/withdraw/getPaymentWithdrawCPSList";

		JSONObject body = new JSONObject();
		body.put("limit",10);
		body.put("offset",0);
		body.put("order","asc");
		body.put("pageNo",1);
		body.put("search",new JSONObject()
				.fluentPut("dateType","0")
				.fluentPut("directLevel","5")
				.fluentPut("endDate", GlobalMethods.setToDateTime())
				.fluentPut("org_id","")
				.fluentPut("searchType","0")
				.fluentPut("startDate", GlobalMethods.setFromDateTime())
				.fluentPut("statusQuery","1")
				.fluentPut("typeQuery","-1")
				.fluentPut("userQuery",email)
				.fluentPut("user_id",""));

		header.put("Content-Type", "application/json");

		JSONObject result = null;
		try {
			HttpResponse response = httpClient.getPostResponse(path, header, body);
			result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, path, body, result );
			assertTrue(result.getBoolean("success"), "API Failed!! Path: "+path+"\n" + result);
			assertFalse(result.getJSONArray("rows").isEmpty(), "API response is null!! Path: " + path + "\n" + result);
            assertEquals(email, result.getJSONArray("rows").getJSONObject(0).getString("user_email"), "Incorrect Search Result, not belong to email: "+email+ "\nPath: " + path + "\n" + result);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	//Audit Status: Rejected = 4, Completed = 2, Accepted = 1
	//Withdrawal Status: Unpaid = -1, Paid = 1
	public JSONObject apiUnionpayWithdrawalApprove(String recordID, String currentStatus, String newStatus, String withdrawalStatus){
		String path = this.url + "/payment/withdraw/paymentWithdrawCPSAudit/submit";

		HashMap<String,String> body= new HashMap<>();
		body.put("ids",recordID);
		body.put("current_audit_status",currentStatus);
		body.put("update_audit_status",newStatus);
		body.put("withdrawal_audit_status",withdrawalStatus);

		header.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

		JSONObject result = null;
		try {
			HttpResponse response = httpClient.getPostResponse(path, header, body);
			result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, path, body, result );
			assertTrue("true".equals(result.getString("success")) && result.getString("message").contains("Successfully"), "API Failed!! Path: "+path+"\n" + result);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	//Audit Status: Rejected = 4, Completed = 2, Accepted = 1
	public JSONObject apiUnionpayWithdrawalReject(String recordID, String currentStatus, String newStatus){
		String path = this.url + "/payment/withdraw/paymentWithdrawCPSAudit/reject";

		HashMap<String,String> body= new HashMap<>();
		body.put("ids",recordID);
		body.put("current_audit_status",currentStatus);
		body.put("update_audit_status",newStatus);

		header.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

		JSONObject result = null;
		try {
			HttpResponse response = httpClient.getPostResponse(path, header, body);
			result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, path, body, result );
			assertTrue("true".equals(result.getString("success")) && result.getString("message").contains("Successfully"), "API Failed!! Path: "+path+"\n" + result);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public void apiCacheMgmtWDType(){
		String path = this.url + "/payment/cache/withdraw-type";

		JSONObject body= new JSONObject();
		body.put("limit","10");
		body.put("pageNo","1");

		header.put("Content-Type", "application/json");

		try {
			HttpResponse response = httpClient.getPostResponse(path, header, body);
			JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, path, body, result );
			assertTrue(result.getBoolean("success"), "API Failed!! Path: " + path + "\n" + result);
			assertFalse(result.getJSONArray("rows").isEmpty(),"API Response is empty!! Path: "+path+"\n" + result);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void apiCacheMgmtWDTypeRefresh(){
		String path = this.url + "/payment/cache/refresh/withdraw-type";

		try {
			HttpResponse response = httpClient.getPostResponse(path, header, "");
			JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, path, "", result );
			assertTrue("true".equals(result.getString("success")) && "操作成功".equals(result.getString("message")), "API Failed!! Path: "+path+"\n" + result);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void apiCacheMgmtPortalWDType(){
		String path = this.url + "/payment/cache/portal-withdraw-type";

		JSONObject body= new JSONObject();
		body.put("limit","10");
		body.put("pageNo","1");

		header.put("Content-Type", "application/json");

		try {
			HttpResponse response = httpClient.getPostResponse(path, header, body);
			JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, path, body, result );
			assertTrue(result.getBoolean("success"), "API Failed!! Path: "+path+"\n" + result);
			assertFalse(result.getJSONArray("rows").isEmpty(),"API Response is empty!! Path: "+path+"\n" + result);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void apiCacheMgmtPortalWDTypeRefresh(){
		String path = this.url + "/payment/cache/refresh/portal-withdraw-type";

		try {
			HttpResponse response = httpClient.getPostResponse(path, header, "");
			JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, path, "", result );
			assertTrue("true".equals(result.getString("success")) && "操作成功".equals(result.getString("message")), "API Failed!! Path: "+path+"\n" + result);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void apiCacheMgmtWDChannel(){
		String path = this.url + "/payment/cache/withdraw-channel";

		JSONObject body= new JSONObject();
		body.put("limit","10");
		body.put("pageNo","1");

		header.put("Content-Type", "application/json");

		try {
			HttpResponse response = httpClient.getPostResponse(path, header, body);
			JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, path, body, result );
			assertTrue(result.getBoolean("success"), "API Failed!! Path: "+path+"\n" + result);
			assertFalse(result.getJSONArray("rows").isEmpty(),"API Response is empty!! Path: "+path+"\n" + result);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void apiCacheMgmtWDChnlRefresh(){
		String path = this.url + "/payment/cache/refresh/withdraw-channel";

		try {
			HttpResponse response = httpClient.getPostResponse(path, header, "");
			JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, path, "", result );
			assertTrue("true".equals(result.getString("success")) && "操作成功".equals(result.getString("message")), "API Failed!! Path: " + path + "\n" + result);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void apiCacheMgmtDepType(){
		String path = this.url + "/payment/cache/deposit-type";

		JSONObject body= new JSONObject();
		body.put("limit","10");
		body.put("pageNo","1");

		header.put("Content-Type", "application/json");

		try {
			HttpResponse response = httpClient.getPostResponse(path, header, body);
			JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, path, body, result );
			assertTrue(result.getBoolean("success"), "API Failed!! Path: "+path+"\n" + result);
			assertFalse(result.getJSONArray("rows").isEmpty(),"API Response is empty!! Path: "+path+"\n" + result);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void apiCacheMgmtDepTypeRefresh(){
		String path = this.url + "/payment/cache/refresh/deposit-type";

		try {
			HttpResponse response = httpClient.getPostResponse(path, header, "");
			JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, path, "", result );
			assertTrue("true".equals(result.getString("success")) && "操作成功".equals(result.getString("message")), "API Failed!! Path: " + path + "\n" + result);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void apiCacheMgmtDepChnlRefresh(){
		String path = this.url + "/payment/cache/refresh/deposit-channel";

		try {
			HttpResponse response = httpClient.getPostResponse(path, header, "");
			JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, path, "", result );
			assertTrue("true".equals(result.getString("success")) && "操作成功".equals(result.getString("message")), "API Failed!! Path: " + path + "\n" + result);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void apiAllDepositSwitchOff(){
		String path = this.url + "/config/deposit/update_automaticIncome?auto_deposit=1";

		try {
			HttpResponse response = httpClient.getGetResponse(path, header);
			JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, path, "", result );
			assertTrue("true".equals(result.getString("success")) && "操作成功".equals(result.getString("message")), "API Failed!! Path: " + path + "\n" + result);
			JSONObject resultData = result.getJSONObject("data");
			assertTrue("入金总开关".equals(resultData.getString("p_content")) && "0".equals(resultData.getString("p_value")), "API did not switch off\n" + result);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void apiAllDepositSwitchOn(){
		String path = this.url + "/config/deposit/update_automaticIncome?auto_deposit=0";

		try {
			HttpResponse response = httpClient.getGetResponse(path, header);
			JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, path, "", result );
			assertTrue("true".equals(result.getString("success")) && "操作成功".equals(result.getString("message")), "API Failed!! Path: " + path + "\n" + result);
			JSONObject resultData = result.getJSONObject("data");
			assertTrue("入金总开关".equals(resultData.getString("p_content")) && "1".equals(resultData.getString("p_value")), "API did not switch on\n" + result);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void apiUnionpayAutoDepositOff(){
		String path = this.url + "/config/deposit/update_automaticDeposit?auto_deposit=1";

		try {
			HttpResponse response = httpClient.getGetResponse(path, header);
			JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, path, "", result );
			assertTrue("true".equals(result.getString("success")) && "操作成功".equals(result.getString("message")), "API Failed!! Path: " + path + "\n" + result);
			JSONObject resultData = result.getJSONObject("data");
			assertTrue("银联自动入金".equals(resultData.getString("p_content")) && "0".equals(resultData.getString("p_value")), "API did not switch off\n" + result);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void apiUnionpayAutoDepositOn(){
		String path = this.url + "/config/deposit/update_automaticDeposit?auto_deposit=0";

		try {
			HttpResponse response = httpClient.getGetResponse(path, header);
			JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, path, "", result );
			assertTrue("true".equals(result.getString("success")) && "操作成功".equals(result.getString("message")), "API Failed!! Path: " + path + "\n" + result);
			JSONObject resultData = result.getJSONObject("data");
			assertTrue("银联自动入金".equals(resultData.getString("p_content")) && "1".equals(resultData.getString("p_value")), "API did not switch on\n" + result);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void apiQuickPaymentAutoDepositOff(){
		String path = this.url + "/config/deposit/update_quick_automaticDeposit?quick_auto_deposit=1";

		try {
			HttpResponse response = httpClient.getGetResponse(path, header);
			JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, path, "", result );
			assertTrue("true".equals(result.getString("success")) && "操作成功".equals(result.getString("message")), "API Failed!! Path: " + path + "\n" + result);
			JSONObject resultData = result.getJSONObject("data");
			assertTrue("快捷支付自动入金".equals(resultData.getString("p_content")) && "0".equals(resultData.getString("p_value")), "API did not switch off\n" + result);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void apiQuickPaymentAutoDepositOn(){
		String path = this.url + "/config/deposit/update_quick_automaticDeposit?quick_auto_deposit=0";

		try {
			HttpResponse response = httpClient.getGetResponse(path, header);
			JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, path, "", result );
			assertTrue("true".equals(result.getString("success")) && "操作成功".equals(result.getString("message")), "API Failed!! Path: " + path + "\n" + result);
			JSONObject resultData = result.getJSONObject("data");
			assertTrue("快捷支付自动入金".equals(resultData.getString("p_content")) && "1".equals(resultData.getString("p_value")), "API did not switch on\n" + result);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void apiAutoTransferOff(){
		String path = this.url + "/admin/mt4Option/mt4account_change?description=1";

		try {
			HttpResponse response = httpClient.getGetResponse(path, header);
			JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, path, "", result );
			assertTrue("true".equals(result.getString("success")) && "操作成功".equals(result.getString("message")), "API Failed!! Path: " + path + "\n" + result);
			JSONObject resultData = result.getJSONObject("data");
			assertTrue("MT4自动转账设置".equals(resultData.getString("p_content")) && "0".equals(resultData.getString("p_value")), "API did not switch off\n" + result);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void apiAutoTransferOn(){
		String path = this.url + "/admin/mt4Option/mt4account_change?description=0";

		try {
			HttpResponse response = httpClient.getGetResponse(path, header);
			JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, path, "", result );
			assertTrue("true".equals(result.getString("success")) && "操作成功".equals(result.getString("message")), "API Failed!! Path: " + path + "\n" + result);
			JSONObject resultData = result.getJSONObject("data");
			assertTrue("MT4自动转账设置".equals(resultData.getString("p_content")) && "1".equals(resultData.getString("p_value")), "API did not switch on\n" + result);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void apiGetExchangeCurrencyPair(){
		String path = this.url + "/exchangeRate/data/getCurrencyType";

		try {
			HttpResponse response = httpClient.getGetResponse(path, header,"");
			JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, path, "", result );
			assertTrue("true".equals(result.getString("success")) && "操作成功".equals(result.getString("message")), "API Failed!! Path: " + path + "\n" + result);
			assertFalse(result.getJSONObject("data").isEmpty(), "No exchange currency type return. Kindly check.\n" + result);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public JSONObject apiGetExchangeRateLimit(String currencyPairCode){
		String path = this.url + "/exchangeRate/data/getDataByCode?code="+currencyPairCode;

		JSONObject result = null;
		try {
			HttpResponse response = httpClient.getGetResponse(path, header,"");
			result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, path, "", result );
			assertTrue("true".equals(result.getString("success")) && "操作成功".equals(result.getString("message")), "API Failed!! Path: " + path + "\n" + result);
			assertFalse(result.getJSONObject("data").isEmpty(), "No exchange rate limit return. Kindly check.\n" + result);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public void apiGetExchangeRate(){
		String path = this.url + "/exchangeRate/data/getExchangeRateData";

		try {
			HttpResponse response = httpClient.getPostResponse(path, header,"");
			JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, path, "", result );
			assertTrue("true".equals(result.getString("success")) && "操作成功".equals(result.getString("message")), "API Failed!! Path: " + path + "\n" + result);
			assertFalse(result.getJSONObject("data").isEmpty(), "No exchange rate data return. Kindly check.\n" + result);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public JSONObject getExchangeRate(){
		String path = this.url + "/exchangeRate/data/getExchangeRateData";

		try {
			HttpResponse response = httpClient.getPostResponse(path, header,"");
			JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, path, "", result );
			assertTrue("true".equals(result.getString("success")) && "操作成功".equals(result.getString("message")), "API Failed!! Path: " + path + "\n" + result);
			return result.getJSONObject("data");

		}catch(Exception e) {
			e.printStackTrace();
		}
        return null;
    }
	//update USD to MYR rate
	public void apiUpdateDepositExchangeRate(String newRateValue){
		String path = this.url + "/exchangeRate/action/updateExchangeRate";

		HashMap <String,String> body = new HashMap<>();
		body.put("USD2MYR",newRateValue);
		body.put("rateType","non_real_time");
		body.put("details","Non-real-time Exchange Rate");

		header.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

		try {
			HttpResponse response = httpClient.getPostResponse(path, header, body);
			JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, path, body, result );
			assertTrue("true".equals(result.getString("success")) && "操作成功".equals(result.getString("message")), "API Failed!! Path: " + path + "\n" + result);
			assertFalse(result.getJSONObject("data").isEmpty(), "No exchange rate data return. Kindly check.\n" + result);
			assertTrue(result.getJSONObject("data").containsValue(newRateValue), "Update fail, kindly check\n"+result);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Search Email Send Record
	 * @param uid
	 * @param startDate
	 * @param endDate
	 * @param callTemplate
	 * @return
	 */
	public JSONObject apiSearchEmailSendRecord(String uid,String startDate, String endDate,String callTemplate,String subjectName){
		String subjectDescription = "";
		if(subjectName != null) {
			subjectDescription +=subjectName;
		}
		String path = this.url + "/admin/mail/mailLogList";
		JSONObject body= new JSONObject();
		body.put("skipCount", false);
		JSONObject pagination = new JSONObject();
		pagination.put("pageNo", 1);
		pagination.put("limit", 10);
		pagination.put("offset", 0);
		body.put("pagination", pagination);
		JSONObject parameters = new JSONObject();
		JSONObject userId = new JSONObject();
		userId.put("filterType", "INPUT");
		userId.put("input", uid);
		parameters.put("userId", userId);
		JSONObject createTime = new JSONObject();
		createTime.put("mandatory", true);
		createTime.put("filterType", "DATEPICKER");
		JSONObject input = new JSONObject();
		input.put("startDate", startDate);
		input.put("endDate", endDate);
		createTime.put("input", input);
		parameters.put("createTime", createTime);
		JSONObject toMail = new JSONObject();
		toMail.put("filterType", "INPUT");
		toMail.put("input", "");
		parameters.put("toMail", toMail);
		JSONObject mt4Account = new JSONObject();
		mt4Account.put("filterType", "INPUT");
		mt4Account.put("input", "");
		parameters.put("mt4Account", mt4Account);
		JSONObject subject = new JSONObject();
		subject.put("filterType", "INPUT");
		subject.put("input", subjectDescription);
		parameters.put("subject", subject);
		JSONObject templateInvokeName = new JSONObject();
		templateInvokeName.put("filterType", "INPUT");
		templateInvokeName.put("input", callTemplate);
		parameters.put("templateInvokeName", templateInvokeName);
		JSONObject status = new JSONObject();
		status.put("filterType", "SELECT");
		status.put("input", null);
		parameters.put("status", status);
		JSONObject reSendCount = new JSONObject();
		reSendCount.put("filterType", "INPUT");
		reSendCount.put("input", "");
		parameters.put("reSendCount", reSendCount);
		body.put("parameters", parameters);

		header.put("Content-Type", "application/json");

		try {
			HttpResponse response = httpClient.getPostResponse(path, header, body);
			JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, path, body , result );
//			assertTrue("true".equals(result.getString("success")) && "操作成功".equals(result.getString("message")), "API Failed!! Path: " + path + "\n" + result);
			return result;
		}catch(Exception e) {
			e.printStackTrace();
		}
        return null;
    }
	//update USD to MYR rate
	public void apiUpdateWithdrawalExchangeRate(String newRateValue){
		String path = this.url + "/exchangeRate/action/updateExchangeRate";

		HashMap <String,String> body = new HashMap<>();
		body.put("USD2MYRWITH",newRateValue);
		body.put("rateType","non_real_time");
		body.put("details","Withdrawal Rate");

		header.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

		try {
			HttpResponse response = httpClient.getPostResponse(path, header, body);
			JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, path, body , result );
			assertTrue("true".equals(result.getString("success")) && "操作成功".equals(result.getString("message")), "API Failed!! Path: " + path + "\n" + result);
			assertFalse(result.getJSONObject("data").isEmpty(), "No exchange rate data return. Kindly check.\n" + result);
			assertTrue(result.getJSONObject("data").containsValue(newRateValue), "Update fail, kindly check\n"+result);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	//For CP API usage - cash adjustment topup money flow
	public void apiAdminCashAdjustmentForCP(String acctNum, String currency, String amount, String type) {
		apiCashAdjustmentAddRecord(acctNum,currency,amount,type);
		JSONObject cashAdjustResult1 = apiCashAdjustmentAuditSearch(acctNum,"1");
		JSONObject toApproveRecord = cashAdjustResult1.getJSONArray("rows").getJSONObject(0);
		apiCashAdjustmentAuditApprove(toApproveRecord);

		Integer status = 0;

		int maxRetries = 10;

		for (int attempt = 1; attempt <= maxRetries; attempt++) {
			status = paymentDB.getCARecordStatus(acctNum);
			System.out.println("Check " + attempt + ": status = " + status);

			if (status.equals(6)) {
				LogUtils.info(String.format("Cash adjustment status has been updated to Completed (%s)", status));
				return;
			}

			if (attempt != maxRetries) {
				try {
					Thread.sleep(3000); // Wait 3 second before next check
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				LogUtils.info(String.format("Retry fetching cash adjustment update status on attempt %s. Current Status Code: %s", attempt, status));
			}
		}

		LogUtils.info(String.format("Cash adjustment not completed. Status Code: %s", status));
	}

	// region [ Credit Adjustment ]

	// For CP usage - Add credit
	public Map.Entry<Boolean, String> apiAdminCreditAdjustmentForCP(String acctNum, String currency, String amount, String type) {

		// Add credit
		apiCreditAdjustmentAddRecord(acctNum, currency, amount, type);

		// Search credit adjustment record
		JSONObject creditAdjustResult = apiCreditAdjustmentAuditSearch(acctNum,"1");
		JSONObject toApproveRecord = creditAdjustResult.getJSONArray("rows").getJSONObject(0);

		// Approve credit adjustment
		apiCreditAdjustmentAuditApprove(toApproveRecord);

		Integer status = 0;

		int maxRetries = 10;

		for (int attempt = 1; attempt <= maxRetries; attempt++) {
			// Get credit adjustment status from DB
			status = paymentDB.getCARecordStatus(acctNum, GlobalMethods.setFromDateTime(), GlobalMethods.dateDayEnd(1));
			if(status!=null){
				String statusDesc = switch (status) {
					case 6 -> "Completed";
					case 2 -> "Rejected";
					default -> "";
				};

				if (status.equals(6)) {
					LogUtils.info(String.format("Credit adjustment status has been updated to %s (%s)", statusDesc, status));
					return new AbstractMap.SimpleEntry<>(true, "");
				}

				if (status.equals(2)) {
					LogUtils.info(String.format("Credit adjustment status has been updated to %s (%s)", statusDesc, status));
					return new AbstractMap.SimpleEntry<>(false, "Credit adjustment has been rejected.");
				}

				if (attempt != maxRetries) {
					try {
						Thread.sleep(6000); // Wait 6 second before next check
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

				LogUtils.info(String.format("Retry fetching credit adjustment update status on attempt %s. Current Status Code: %s", attempt, status));
			}
		}

		return new AbstractMap.SimpleEntry<>(false, String.format("Credit adjustment not completed. Status Code: %s", status));
	}

	public void apiCreditAdjustmentAddRecord (String account, String currency, String amount, String type) {
		String path = this.url + HyTechUrl.addCreditAdjustment;
		HashMap<String,String> body = new HashMap<>();
		body.put("account",account);
		body.put("money",amount);
		body.put("currency",currency);
		body.put("type",type);
		body.put("remark","test");
		body.put("notes","From automation case");

		header.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

		try {
			HttpResponse response = httpClient.getPostResponse(path, header, body);
			JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, path, body, result );
			assertTrue("true".equals(result.getString("success")) && "操作成功".equals(result.getString("data")),"API Failed!! Path: " +path+"\n" + result);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	//Search with submitted status (1) and account number
	public JSONObject apiCreditAdjustmentAuditSearch (String account, String status) {
		String path = this.url + HyTechUrl.searchCreditAdjustment;

		JSONObject body = new JSONObject();
		body.put("skipCount", false);
		body.put("pagination", new JSONObject()
				.fluentPut("limit", "10")
				.fluentPut("offset", "0")
				.fluentPut("pageNo", "1"));
		body.put("parameters", new JSONObject()
				.fluentPut("senderMt4AccountId", new JSONObject()
						.fluentPut("fuzzy",true)
						.fluentPut("filterType","INPUT")
						.fluentPut("input",account))
				.fluentPut("status", new JSONObject()
						.fluentPut("filterType","SELECT")
						.fluentPut("input", new JSONArray()
								.fluentAdd(status)))
				.fluentPut("transactionInvocationDate", new JSONObject()
						.fluentPut("filterType","DATEPICKER")
						.fluentPut("input",new JSONObject()
								.fluentPut("startDate", GlobalMethods.setFromDateTime())
								.fluentPut("endDate", GlobalMethods.dateDayEnd(1)))));


		header.put("Content-Type", "application/json");

		JSONObject result = null;
		try {
			HttpResponse response = httpClient.getPostResponse(path, header, body);
			result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, path, body, result );
			assertTrue(result.getBoolean("success"), "API Failed!! Path: " +path+"\n" + result);
			assertFalse(result.getJSONArray("rows").isEmpty(), "API response is null!! Path: " + path + "\n" + result);
			assertEquals(account,result.getJSONArray("rows").getJSONObject(0).getString("senderMt4AccountId"), "API result incorrect, result not belong to account: "+account+"\n" + result);

			Integer apiRecordCount = result.getInteger("total");
			Integer dbRecordCount = paymentDB.getCARecordByAcctStatus(account, status, GlobalMethods.setFromDateTime(), GlobalMethods.dateDayEnd(1)).size();
			assertEquals(apiRecordCount, dbRecordCount, "AP number of records != DB number of records\n"
					+ "Total of API Records: "+apiRecordCount+ "\nTotal of DB Records: "+dbRecordCount);

		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public void apiCreditAdjustmentAuditApprove(JSONObject recordDetails) {
		String path = this.url + HyTechUrl.submitCreditAdjustment;

		JSONArray bodyArray = new JSONArray();
		JSONObject body = new JSONObject();
		body.put("transactionId", recordDetails.getInteger("transactionId"));
		body.put("operatorId", recordDetails.getInteger("operatorId"));
		body.put("senderMt4AccountId", recordDetails.getInteger("senderMt4AccountId"));
		body.put("transactionType", recordDetails.getInteger("transactionType"));
		body.put("senderAmount", recordDetails.getDoubleValue("senderAmount"));
		body.put("remark", recordDetails.getString("remark"));
		body.put("transactionInvocationDate", recordDetails.getString("transactionInvocationDate"));
		body.put("functionCode", recordDetails.getInteger("functionCode"));
		body.put("ticketId", recordDetails.getString("ticketId"));
		body.put("status", recordDetails.getInteger("status"));
		body.put("applicant", "cmatest");
		body.put("accountDealType", recordDetails.getInteger("accountDealType"));
		bodyArray.fluentAdd(body);

		header.put("Content-Type", "application/json");

		try {
			HttpResponse response = httpClient.getPostResponse(path, header, bodyArray);
			JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
			this.printAPIInfo(brand, regulator, path, body, result );
			assertTrue("true".equals(result.getString("success")) && "操作成功".equals(result.getString("data")), "API Failed!! Path: " +path+"\n" + result);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Set userId and countryCode
	 * @param pcswdapi
	 */
	public void setUserIdAndCountryCode(PCSAPIWithdraw pcswdapi) {
		JSONObject accInfo = pcswdapi.queryMetaTraderAccountForWithdraw();
		if(accInfo!=null){
			String userId = accInfo.getString("userId");
			setUserId(userId);
			JSONArray clientInfo =apiClientSearch(userId);
			if(!clientInfo.isEmpty()){
				String countryCode = clientInfo.getJSONObject(0).getString("countryCode");
				setCountryCode(countryCode);
			}

		}

	}
	/**
	 * Batch update SRC risk records
	 * @param dbenv
	 * @param dbBrand
	 * @param dbRegulator
	 */
	public void batchUpdateSRCRiskRecord(GlobalProperties.ENV dbenv, GlobalProperties.BRAND dbBrand, GlobalProperties.REGULATOR dbRegulator) {
//		if(dbBrand!=GlobalProperties.BRAND.VFX){
//			LogUtils.info("No need to Batch update SRC risk records");
//			return;
//		}

		boolean isSRCWithdraw = paymentDB.judgeSRCWithdrawStatus(dbenv, BRAND.valueOf(String.valueOf(brand)), REGULATOR.valueOf(String.valueOf(regulator)),userId, countryCode);;
		LogUtils.info("Batch update SRC risk records");

		// 添加统计变量 - 使用Map存储详细信息
		List<Map<String, String>> processedRecords = new ArrayList<>();
		int totalProcessed = 0;
		if (isSRCWithdraw) {
			// 先获取总记录数
			JSONObject firstPageResult = apiWDAuditSearchNew("", "21");

			if (firstPageResult == null || firstPageResult.getJSONArray("rows").isEmpty()) {
				LogUtils.info("No risk audit records found");
				return;
			}

			int total = firstPageResult.getInteger("total");
			if (total == 0) {
				LogUtils.info("No risk audit records found");
				return;
			}

			int pageSize = 10;
			int pages = (total % pageSize == 0) ? total / pageSize : total / pageSize + 1;
			String brand = dbBrand.toString();

			// 分页处理所有记录
			for (int page = 1; page <= pages; page++) {
				LogUtils.info("Processing page " + page + " of " + pages);
				JSONObject wdAuditResult = apiWDAuditSearchByPage("", "21", page, pageSize);

				if (wdAuditResult == null || !wdAuditResult.getBoolean("success")) {
					LogUtils.info("No more records found on page " + page);
					break;
				}

				JSONArray riskRecords = wdAuditResult.getJSONArray("rows");

				// 批量处理当前页符合条件的记录
				for (int i = 0; i < riskRecords.size(); i++) {
					JSONObject record = riskRecords.getJSONObject(i);
					String recordId = record.getString("id"); // 获取记录ID
					String accountName = record.getString("real_name");;
					String riskAuditOrderNum = record.getString("orderNumber"); // 获取单号
					String userId = record.getString("userId"); // 获取用户ID
					String processedNotes = record.getString("processedNotes");

					// 检查是否符合批量更新条件
					if (isEligibleForBatchUpdate(accountName, processedNotes)) {
						LogUtils.info("Processing batch update for account: " + accountName + ", Order: " + riskAuditOrderNum + ", Record ID: " + recordId + ", User ID: " + userId);
							LogUtils.info("Processing batch update for order: " + riskAuditOrderNum + ", Record ID: " + recordId + ", User ID: " + userId);
							RiskAuditCallBack callBackDataAU = new RiskAuditCallBack(
									dbenv.toString(),
									brand,
									dbRegulator.toString(),
									userId,
									riskAuditOrderNum
							);

							// 调用带重试机制的方法
							sendSRCRiskAuditWithRetry(callBackDataAU, brand);

							// 记录已处理的记录信息
							Map<String, String> processedRecord = new HashMap<>();
							processedRecord.put("recordId", recordId);
							processedRecord.put("orderNumber", riskAuditOrderNum);
							processedRecord.put("userId", userId);
							processedRecord.put("accountName", accountName);
							processedRecords.add(processedRecord);
							totalProcessed++;
					}
				}
			}
		}

		// 记录统计信息 - 以字典方式输出
		LogUtils.info("=== Batch Update Summary ===");
		LogUtils.info("Total records processed: " + totalProcessed );

		if (!processedRecords.isEmpty()) {
			LogUtils.info("Processed records details:");
			for (int i = 0; i < processedRecords.size(); i++) {
				Map<String, String> record = processedRecords.get(i);
				LogUtils.info("  [" + (i+1) + "] Record ID: " + record.get("recordId") +
						", Order Number: " + record.get("orderNumber") +
						", User ID: " + record.get("userId") +
						", Account Name: " + record.get("accountName"));
			}
		}

		LogUtils.info("=== End of Batch Update Summary ===");
	}

	public void updateSingleSRCRiskRecord(String account, GlobalProperties.ENV dbenv, GlobalProperties.BRAND dbBrand, GlobalProperties.REGULATOR dbRegulator) {
		boolean isSRCWithdraw = paymentDB.getSRCWithdrawStatus(dbenv, dbBrand, dbRegulator);
		JSONObject wdAuditResult = null;
		LogUtils.info("Batch update SRC risk records");
		if (isSRCWithdraw) {
			// 搜索所有风险审核状态为21的记录
			wdAuditResult = apiWDAuditAccountSearch(account, "21");
		}

		if (wdAuditResult == null || wdAuditResult.getJSONArray("rows").isEmpty()) {
			LogUtils.info("No risk audit records found");
			return;
		}

		JSONArray riskRecords = wdAuditResult.getJSONArray("rows");
		String brand = dbBrand.toString();

		// 批量处理符合条件的记录
		for (int i = 0; i < riskRecords.size(); i++) {
			JSONObject record = riskRecords.getJSONObject(i);
			String accountName = record.getString("accountName");
			String realName = record.getString("real_name");
			// 取两个字段中不为空的一个，如果都不为空则优先取accountName
			accountName = (accountName != null && !accountName.trim().isEmpty()) ? accountName : realName;

			String processedNotes = record.getString("processedNotes");

			// 检查是否符合批量更新条件
			if (isEligibleForBatchUpdate(accountName, processedNotes)) {
				LogUtils.info("Processing batch update for account: " + accountName);
				String userId = record.getString("userId");
				String riskAuditOrderNum = record.getString("orderNumber");

				if (isSRCWithdraw && (brand.equalsIgnoreCase("VFX") || brand.equalsIgnoreCase("au"))) {
					LogUtils.info("Processing batch update for order: " + riskAuditOrderNum);
					RiskAuditCallBack callBackDataAU = new RiskAuditCallBack(
							dbenv.toString(),
							brand,
							dbRegulator.toString(),
							userId,
							riskAuditOrderNum
					);

					// 调用带重试机制的方法
					sendSRCRiskAuditWithRetry(callBackDataAU, brand);
				}
			}
		}
	}

	/**
	 * 检查记录是否符合批量更新条件
	 * @param accountName 账户名称
	 * @param processedNotes 处理备注
	 * @return 是否符合条件
	 */
	private boolean isEligibleForBatchUpdate(String accountName, String processedNotes) {
//		if ((accountName == null) || processedNotes == null) {
//			return false;
//		}

		// 检查账户名是否包含测试相关关键词
		boolean isTestAccount = accountName.contains("testcrm") ||
				accountName.contains("autotest") ||
				accountName.contains("自动化测试");

		// 检查备注是否包含SRC Review信息
		boolean hasSRCReview = processedNotes.contains("SRC Review - Risk approval sent");

		return isTestAccount && hasSRCReview;
	}



	public JSONObject updateSRCRiskRecord(String userName, GlobalProperties.ENV dbenv, GlobalProperties.BRAND dbBrand, GlobalProperties.REGULATOR dbRegulator) {
		boolean isSRCWithdraw = paymentDB.getSRCWithdrawStatus(dbenv, dbBrand, dbRegulator);
		JSONObject wdAuditResult = null;

		if (isSRCWithdraw) {
			wdAuditResult = apiWDAuditSearch(userName, "21");
		}

		if (wdAuditResult == null || wdAuditResult.getJSONArray("rows").isEmpty()) {
			LogUtils.info("No risk audit records found for user: " + userName);
			return wdAuditResult;
		}

		String userId = wdAuditResult.getJSONArray("rows").getJSONObject(0).getString("userId");
		String riskAuditOrderNum = wdAuditResult.getJSONArray("rows").getJSONObject(0).getString("orderNumber");
		String brand = dbBrand.toString();

		if (isSRCWithdraw && (brand.equalsIgnoreCase("VFX") || brand.equalsIgnoreCase("au"))) {
			LogUtils.info("flow to au new withdraw process");
			RiskAuditCallBack callBackDataAU = new RiskAuditCallBack(
					dbenv.toString(),
					brand,
					dbRegulator.toString(),
					userId,
					riskAuditOrderNum
			);

			// 调用带重试机制的方法
			sendSRCRiskAuditWithRetry(callBackDataAU, brand);
		}
		return wdAuditResult;
	}

	/**
	 * 发送SRC风险审核请求并处理重试逻辑
	 * @param callBackDataAU 风险审核回调数据
	 * @param brand 品牌
	 */
	private void sendSRCRiskAuditWithRetry(RiskAuditCallBack callBackDataAU, String brand) {
		try {
			JSONObject riskAuditRes = sendSRCRiskAuditRequest(callBackDataAU, brand);
			int retryCount = 0;
			int maxRetryTime = 60; // 1分钟 = 60秒
			int retryInterval = 15; // 15秒重试间隔
			String errMsg = riskAuditRes.getString("errmsg");
			while (!errMsg.contains("Missing PaymentRiskAudit")&&Objects.equals(riskAuditRes.getInteger("code"), 500) && retryCount * retryInterval < maxRetryTime) {
				retryCount++;
				LogUtils.info("Risk audit request failed. Waiting " + retryInterval + "s before retry. Attempt: " + retryCount);
				Thread.sleep(retryInterval * 1000);
				riskAuditRes = sendSRCRiskAuditRequest(callBackDataAU, brand);

				if (Objects.equals(riskAuditRes.getInteger("code"), 200)) {
					LogUtils.info("Risk audit request sent successfully after " + retryCount + " retries.");
					return;
				}
			}

			if (!Objects.equals(riskAuditRes.getInteger("code"), 200) && !Objects.equals(riskAuditRes.getInteger("code"), 500)) {
				LogUtils.info("Risk audit request failed with code: " + riskAuditRes.getInteger("code") + "\n" + riskAuditRes);
			} else if (Objects.equals(riskAuditRes.getInteger("code"), 500) && retryCount * retryInterval >= maxRetryTime) {
				LogUtils.info("Risk audit request failed. Maximum retry time (3 minutes) exceeded.");
			} else if (Objects.equals(riskAuditRes.getInteger("code"), 200)) {
				LogUtils.info("Risk audit request sent successfully.");
			}
		} catch (Exception e) {
			LogUtils.error("Error occurred during risk audit request: " + e.getMessage(), e);
		}
	}

}

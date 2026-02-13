package newcrm.business.businessbase.ibbase;

import static org.testng.Assert.assertTrue;

import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import newcrm.utils.http.CRMHttpClient;
import vantagecrm.Utils;


public class IBUAEBankTransfer extends IBLocalBankTransfer{
	
	public IBUAEBankTransfer(WebDriver driver) {
		super(driver);
	}

	public boolean addNewBankAccount(String branch,String accName, String accNumber, String city, String province,
									 String ifsc, String notes, String accdigit, String docid, String swift_code,
									 String docType, String accType, String bankName) {
		if(!page.chooseAddBankAccount()) { 
			return false; 
		}
		page.setBankName();
		page.setCPSBankAdress(city);
		page.setBankAccountName(accName);
		page.setBankAccountNumber(accNumber);
		page.setNotes(notes);
		return true;
	}

}

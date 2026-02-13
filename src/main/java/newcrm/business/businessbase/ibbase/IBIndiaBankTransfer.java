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

public class IBIndiaBankTransfer extends IBLocalBankTransfer{
	
	public IBIndiaBankTransfer(WebDriver driver) {
		super(driver);
	}

	@Override
	public boolean addNewBankAccount(String bank_branch, String acc_name, String acc_number, String city, String province,
									 String ifsc, String notes, String accdigit, String docid, String swift_code,
									 String docType, String accType, String bankName) {
		if(!page.chooseAddBankAccount()) { 
			return false; 
		}
		page.setBankName();
		page.setBankBranch(bank_branch);
		page.setBankAccountName(acc_name);
		page.setBankAccountNumber(acc_number);
		page.setBankCity(city);
		page.setBankProvince(province);
		page.setBankIFSC(ifsc);
		page.setNotes(notes);
		return true;
	}

}

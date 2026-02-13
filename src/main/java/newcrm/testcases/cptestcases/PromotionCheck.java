package newcrm.testcases.cptestcases;

import static org.testng.Assert.assertTrue;

import java.util.List;

import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONArray;

import newcrm.business.businessbase.promotions.PromotionRecordResult;
import newcrm.business.businessbase.promotions.PromotionResult;
import newcrm.global.GlobalMethods;

public class PromotionCheck {

	@Test
	public void checkPromotionResult() {
		PromotionRecordResult file = new PromotionRecordResult();
		List<PromotionResult> results = file.getResults();
		if(results == null || results.size()==0) {
			GlobalMethods.printDebugInfo("Have not found any records need to check");
			return;
		}
		boolean pass = true;
		for(PromotionResult r: results) {
			JSONArray objs = r.findVoucherTable();
			if(objs == null || objs.size()==0) {
				pass = false;
				GlobalMethods.printDebugInfo("!!! have not found " + r.toString() + " update!");
			}else {
				GlobalMethods.printDebugInfo(r.toString() + " - \n" +objs.toJSONString());
			}
			file.remove(r);
		}
		file.writeToFile();
		assertTrue(pass,"Some records check failed, please check the log");
		
	}
}

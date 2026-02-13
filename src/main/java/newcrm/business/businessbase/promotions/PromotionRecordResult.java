package newcrm.business.businessbase.promotions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Scanner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


/**
 * promotions testcase 将用户活动信息记录在file中，Jenkins上会触发10分钟后的job去检查结果，add result后，必须调用一次writeToFile写入文档中
 * @author FengLiu
 *
 */
public class PromotionRecordResult {

	private final String path = System.getProperty("user.dir") + "//src//main//resources//newcrm//data//promotionresult//result.json";
	
	private JSONArray results=null;
	
	public PromotionRecordResult() {
		File file = new File(path);
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		try {
			InputStreamReader reader = new InputStreamReader(new FileInputStream(file),"utf-8");
			Scanner sc = new Scanner(reader);
			String json = "";
			while(sc.hasNext()) {
				json = json + sc.nextLine();
			}
			
			if("".equals(json)) {
				json = "[]";
			}
			
			results = JSON.parseArray(json);
			
			sc.close();
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
	public void addResult(JSONObject obj) {
		results.add(obj);
	}*/
	
	public void addResult(PromotionResult result) {
		results.add(JSON.parseObject(result.toString()));
	}
	public List<PromotionResult> getResults() {
		if(results == null) {
			return null;
		}
		return results.toJavaList(PromotionResult.class);
	}
	
	public void remove(PromotionResult result) {
		results.removeIf(r->{
			PromotionResult t = ((JSONObject) r).toJavaObject(PromotionResult.class);
			if(t.equals(result)) {
				return true;
			}else {
				return false;
			}
		});
	}
	
	public synchronized void  writeToFile() {
		File file = new File(path);
		try {
			PrintWriter pw = new PrintWriter(file,"utf-8");
			pw.print(results.toJSONString());
			pw.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

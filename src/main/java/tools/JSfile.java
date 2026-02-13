package tools;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;

import org.apache.commons.io.IOUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONValidator;

/**
 * 读取语言js的内容，变成key--value的形式，方便对比以及定位
 * @author FengLiu
 *
 */
public class JSfile {
	private String content;
	private String path;
	private HashMap<String,String> results;
	public JSfile(String path) throws IOException {
		/*
		File js = new File(path);
		this.path = path;
		InputStream ins = new FileInputStream(js);
		InputStreamReader rd = new InputStreamReader(ins,"utf-8");
		content = IOUtils.toString(rd);*/
		/*
		 * Scanner sc = new Scanner(rd); while(sc.hasNextLine()) { content = content +
		 * sc.nextLine(); } sc.close(); rd.close();
		 */
		//ins.close();
		
		File js = new File(path);
		InputStream ins = new FileInputStream(js);
		InputStreamReader rd = new InputStreamReader(ins,"utf-8");
		Scanner sc = new Scanner(rd);
		
		content = "";
		Boolean finish = true;
		int fpos = 0;
		  while(sc.hasNextLine()) {
			  String line = sc.nextLine().trim();
			  int pos = line.indexOf("//");
			  if(pos ==0) {
				  continue;
			  }
			  //处理掉多余的符号
			  if(finish) {
				  fpos= line.indexOf('`');
				  if(fpos >=0) {
					  int lpos = line.lastIndexOf('`');
					  if(fpos==lpos) {
						  finish = false;
					  }
					  line = line.replace('"', ' ');
					  line = line.replace('`', '"');
				  }
				  
			  }else {
				  int lpos = line.lastIndexOf('`');
				  if(lpos>=0){
					  finish = true;
				  }
				  line = line.replace('"', ' ');
				  line = line.replace('`', '"');  
			  }
			  
			  content = content + line;
		  }
		  
		  content=content.replace("\\$", "$");//需要处理掉整个文档中的特殊字符，因为在转成JSON时，特殊字符需要处理
		  sc.close(); 
		  rd.close();
		  results = new HashMap<>();
		 
	}
	
	//将字符串处理为key-value对
	private void getmaps() throws IOException  {
		//当已经处理过直接返回
		if(results.size()>0) {
			return;
		}
		//找到字符串中第一个括号，截取括号的内容
		int pos = content.indexOf('{');
		 if(pos>=0) {
			 content = content.substring(pos);
		 }
		 
		JSONObject jsonobj = null;
		 if(JSONValidator.from(content).validate()) {
			 System.out.println("can be transfer to json object.");
			 jsonobj = JSON.parseObject(content);
		 }else {
			 try {
				 jsonobj = JSON.parseObject(content);
				 }
				 catch(JSONException e) {
					 String err = e.getMessage();
					 System.out.println(err);
					 if(err.indexOf("syntax error")>=0) {
						 err=err.substring(err.indexOf("position at")+11);
						 err=err.substring(0,err.indexOf(','));
					 }else {
						 err = err.substring(err.indexOf("pos")+3);
						 err = err.substring(0, err.indexOf(','));
					 }
					 
					 System.out.println(err);
					 Integer errpos = Integer.valueOf(err.trim());
					 System.out.println(content.substring(errpos-20, errpos+20));
				 }
		 }
		 addresult("",jsonobj); //最外层的JSON
	}
	//这是一个递归函数，依次从外向内递归，一直到把所有的字符串转成key-value形式
	private void addresult(String key,JSONObject obj) {
		if(obj==null) {
			return;
		}//当obj为null时，所有的转换结束
		for(String v_key: obj.keySet()) {
			Object value = obj.get(v_key);
			if(JSONValidator.from(value.toString()).validate()) {
				String newkey = "";
				if(key.equals("")) {
					newkey = v_key;
				}else {
					newkey = key+"."+v_key;
				}
				try {
					if(value.getClass() == JSONArray.class) {
						
						for(int s= 0; s < ((JSONArray)value).size();s++) {
							String tmp = ((JSONArray)value).get(s).toString();
							if(JSONValidator.from(tmp).validate()) {
								addresult(newkey+s,JSONObject.parseObject(tmp));//调用自身处理内层的json object
							}else {
								results.put(newkey+s,tmp);
							}
						}
					}else {
						addresult(newkey,JSONObject.parseObject(value.toString()));//调用自身处理内层 json
					}
				
				}catch(Exception err) {
					System.out.println(err.getStackTrace());
				}
				
			}else {
				results.put(key+"."+v_key,value.toString());
				//System.out.println("Key: " + key + "." + v_key + " ||| value: " + value.toString());
			}
		}
		
	}
	
	/**
	 * 查找value是否在js文件中
	 * @param value
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public Boolean checkString(String value) throws UnsupportedEncodingException {
		
		String chestr = "'"+value+"'";
		
		if(content.indexOf(chestr)<0) {
			chestr="`"+value+"`";
			if(content.indexOf(chestr)<0) {
				return false;
			}
		}
		
		return true;
		
	}
	
	public HashMap<String,String> getResult(){
		if(results.size()<=0) {//当已经存在结果时，不用在初始化，返回即可
			try {
				getmaps();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return new HashMap<>(results);
	}
	
	/**
	 * 比较两个js文件的差异，返回结果列表.翻译的原理是，当English中有对应键值时，其他需要翻译的语种中也应该有对应键值
	 * @param v_file 
	 * @return
	 */
	public List<String> diff(JSfile v_file) {
		if(v_file==null) {
			return null;
		}
		ArrayList<String> rs = new ArrayList<>();
		HashMap<String, String> hm = v_file.getResult();
		HashMap<String, String> self = this.getResult();
		Set<String> hm_s = hm.keySet();
		for(String str : hm_s) {
			if(!self.containsKey(str)) {
				if(str.indexOf("wholesale.")>=0 || str.indexOf("questionnaire.")>=0 || str.indexOf(".disclaimer")>=0) {
					continue;
				}
				
				//String miss = "key: " + str + " value: "+ hm.get(str);
				String miss = String.format("%-24s %-50s %-7s %s","key:",str,"value: ",hm.get(str) );
				rs.add(miss);
				System.out.println("Missing match:\n" +miss );
			}else {
				String enStr = hm.get(str).trim();
				String trStr = self.get(str).trim();
				if(trStr.equals(enStr)) {
					//String miss = "key: " + str + " value: "+ enStr;
					String miss = String.format("%-24s %-50s %-7s %s","key:",str,"value: ",enStr );
					rs.add(miss);
					System.out.println("Missing match:\n" +miss );
				}else {
					int pos1 = enStr.indexOf("{");
					int pos2 = trStr.indexOf("{");
					while(pos1 >=0) {
						if(pos2<0) {
							//String miss = "Error Parameter----key: " + str + " value: " + enStr;
							String miss = String.format("%-24s %-50s %-7s %s","Error Parameter----key:",str,"value: ",enStr );
							rs.add(miss);
							System.out.println("Error：\n" +miss );
							break;
						}
						String var1 = enStr.substring(pos1+1, enStr.indexOf("}",pos1+1));
						String var2 = trStr.substring(pos2+1,trStr.indexOf("}",pos2+1));
						if(!var1.equals(var2)) {
							//String miss = "Error Parameter----key: " + str + " value: " + enStr;
							String miss = String.format("%-24s %-50s %-7s %s","Error Parameter----key:",str,"value: ",enStr );
							rs.add(miss);
							System.out.println("Error：\n" +miss );
							break;
						}else {
							pos1 = enStr.indexOf("{", pos1+1);
							pos2 = trStr.indexOf("{", pos2+1);
						}
					}
					
				}
			}
		}
		
		return rs;
	}
	
	/**
	 * 检查js文件中是否有指定value
	 * @param value
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public Boolean NormalCheck(String value) throws UnsupportedEncodingException {
		
		if(content.indexOf(value.trim())<0) {
			return false;
		}
		else {
			return true;
		}
	}
	
}

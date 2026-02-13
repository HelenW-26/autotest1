package tools;

import static org.testng.Assert.assertTrue;

import java.awt.RenderingHints.Key;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.Assert;

/**
 * 用来对比检查CP的语言包，不同语言在不同的js文件里
 * 另外有翻译文件是在Excel文件中
 * @author FengLiu
 *
 */
public class CheckTranslation  {
	
	
	private final String[] languages = {"EN","FR","ES","PH","KR","CN","JP","TH","VN"};
	
	private EXFile fs;//读取的Excel值
	private JSfile js;//读取的目标语言js中的值
	private ReportFile rf;//结果文件实例
	private JSfile enjs;//读取英语js的值
	private int Column;//指定Excel文件中的列
	private int count  = 0;//没有匹配的项的计数
	private boolean initial = false;
	//将所有测试数据读取出来
	private void prepare(String dataPath,String sheetName,int column,String jsPath, String en_js) throws IOException, InterruptedException {
		Column = column;
		fs = new EXFile(dataPath,sheetName,column);
		js = new JSfile(jsPath);
		enjs = new JSfile(en_js);
		String reportfile = dataPath+  "_" + sheetName +"_report.txt";
		rf = new ReportFile(reportfile);
		rf.add("==================Missing macthed====================");//初始化结果文件
		initial = true;
	}
	
	/**
	 * 根据配置文件，读取需要测试的数据，检查Excel
	 * @param dataPath
	 * @param sheetName
	 * @param column 需要翻译语言在Excel中的列
	 * @param jsPath 需要翻译语言的js文件
	 * @param en_js
	 * @throws InterruptedException
	 * @throws IOException
	 */
	@Test(groups="justfortest")
	@Parameters(value = {"dataPath","sheetname","column","jsPath","enJsPath"})
	public void check(String dataPath,String sheetName,int column,String jsPath, String en_js) throws InterruptedException, IOException {
		if(!initial) {
			prepare(dataPath,sheetName,column,jsPath,en_js);
		}
		assertTrue(fs.checkinitial());
		
		ArrayList<String> results = fs.getContent();
		
		for(int index = 0; index < results.size();index++) {
			String str = results.get(index);
			if(!str.equals("")) {
				if(!js.checkString(str)) {
					rf.add("=========Row: "+(index+1)+" Column: " + Column + "========");
					rf.add(str);
					count++;
				}
			}
		}	
	}
	
	/***
	 * compare English and other language
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	@Test(groups="justfortest")
	@Parameters(value = {"dataPath","sheetname","column","jsPath","enJsPath"})
	public void diff(String dataPath,String sheetName,int column,String jsPath, String en_js) throws IOException, InterruptedException {
		if(!initial) {
			prepare(dataPath,sheetName,column,jsPath,en_js);
		}
		
		String currentPath = System.getProperty("user.dir");
		String fullPath = currentPath+ "\\ExtentReports\\diff.txt";
		ReportFile cmpf = new ReportFile(fullPath);
		
		for(String str: js.diff(enjs)) {
			cmpf.add(str);
		}
		cmpf.close();
	}
	
	/**
	 * check if the content in the both excel file and js file.
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	@Test(groups="justfortest")
	@Parameters(value = {"dataPath","sheetname","column","jsPath","enJsPath"})
	public void normalcheck(String dataPath,String sheetName,int column,String jsPath, String en_js) throws IOException, InterruptedException {
		if(!initial) {
			prepare(dataPath,sheetName,column,jsPath,en_js);
		}
		
		assertTrue(fs.checkinitial());
		
		ArrayList<String> results = fs.getContent();
		
		for(int index = 0; index < results.size();index++) {
			String str = results.get(index);
			if(!str.equals("")) {
				if(!js.NormalCheck(str)) {
					rf.add("=========Row: "+(index+1)+" Column: " + Column + "========");
					rf.add(str);
					count++;
				}
			}
		}
	}
	

	@AfterClass(alwaysRun = true)
	public void finishtest() {
		if(rf!=null) {
			rf.add("Missing matched in total: " + count);
			rf.close();
		}
		System.out.println("Test finished");
		//System.out.println("Finish Test.\nPlease Check the report:\n"+rf.getFullName());
	}
	
	/**
	 * 查找所有语言中是否包含指定的关键字，rebranding时使用比较多
	 * @param keyStr xml文件中配置的关键字字符串，逗号分隔开
	 * @param folderPath 存放js文件的文件夹
	 * @throws IOException
	 */
	@Test
	@Parameters(value = {"keyString","folderPath"})
	public void findKeyword(String keyStr,String folderPath) throws IOException {
		String []keywords = keyStr.split(",");
		File folder = new File(folderPath);
		File [] files = folder.listFiles();
		for(File f: files) {
			System.out.println("===============Found key words in " + f.getAbsolutePath() + "=====================");
			JSfile jsfile = new JSfile(f.getAbsolutePath()); 
			HashMap<String,String> result = jsfile.getResult();
			for(Map.Entry<String,String> e: result.entrySet()) {
				for(int i=0; i<keywords.length;i++) {
					if(e.getValue().toLowerCase().contains(keywords[i].toLowerCase().trim())) {
						System.out.printf("Key: %-50s : %s\n",e.getKey(),e.getValue());
					}
				}
			}
			System.out.println("\n\n");
		}
	}
	
	
}

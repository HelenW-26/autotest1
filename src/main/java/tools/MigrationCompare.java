package tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class MigrationCompare {

	

	private ArrayList<String> readFile(String path){
		InputStream ins = null;
		InputStreamReader rd = null;
		Scanner sc = null;
		File f1 = new File(path);
		ArrayList<String> result = new ArrayList<>();
		try {
				ins = new FileInputStream(f1);
				rd = new InputStreamReader(ins,"utf-8");
				sc = new Scanner(rd);
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		while(sc.hasNext()) {
			result.add(sc.nextLine().trim());
		}
		System.out.println(result.size()+" lines in total!");
		sc.close(); 
		try {
			rd.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	private void writeResult(String result,String resultpath) {
		PrintWriter pw = null;
				
		try {
			pw = new PrintWriter(new File(resultpath),"utf-8");
			pw.print(result);
			pw.close();
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
	}
	
	@Test
	@Parameters(value = {"path1","path2","resultpath"})
	public void compare(String path1,String path2,@Optional("")String resultpath) {
		ArrayList<String> r1 = readFile(path1);
		ArrayList<String> r2 = readFile(path2);
		String result = "";
		Integer len1 = r1.size();
		Integer len2 = r2.size();
		String ids = "";
		if(!len1.equals(len2)) {
			System.out.println("the length fo the two file are not same!!");
			return ;
		}
		for(int i=0;i<len1;i++) {
			String rr1 = r1.get(i);
			String rr2 = r2.get(i);
			if(!rr1.equals(rr2)) {
				String []values = rr1.split(",");
				if(!(result.indexOf(values[0])>0)) {
					if(ids.equals("")) {
						ids = ids+values[0];
					}else {
						ids = ids + "," + values[0];
					}
				}
				result = result +"\n"+rr1+",,"+rr2;
			}
		}
		if(ids.equals("")) {
			System.out.println("Found 0 user information mismatched");
		}else {
			System.out.println("Found " + ids.split(",").length+ " users' information mismatched");
		}
		
		System.out.println(ids.replace(",", "\n"));
		
		if(resultpath.equals("")) {
			resultpath = System.getProperty("user.dir")+ "\\ExtentReports\\Migration_result.csv";
		}else {
			int pos = resultpath.lastIndexOf("\\");
			if(pos==resultpath.length()) {
				resultpath = resultpath+"Migration_result.csv";
			}else {
				resultpath = resultpath+"\\Migration_result.csv";
			}
		}
		result ="id,table,count,,id,table,count" + result;
		writeResult(result,resultpath);
	}
	
	@Test
	@Parameters(value = {"path1","resultpath"})
	public void checkDelete(String path1,@Optional("")String resultpath) {
		ArrayList<String> r1 = readFile(path1);
		String result = "";
		for(String str: r1) {
			if(!str.equals("")) {
				String [] tmp = str.split(",");
				if(!tmp[tmp.length-1].equals("0")) {
					result = result+str+"\n";
				}
			}
		}
		if(resultpath.equals("")) {
			resultpath = System.getProperty("user.dir")+ "\\ExtentReports\\delete_result.csv";
		}else {
			int pos = resultpath.lastIndexOf("\\");
			if(pos==resultpath.length()) {
				resultpath = resultpath+"delete_result.csv";
			}else {
				resultpath = resultpath+"\\delete_result.csv";
			}
		}
		writeResult(result,resultpath);
	}
}

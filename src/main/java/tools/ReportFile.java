package tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

/**
 * 翻译测试的report文件
 * @author FengLiu
 *
 */
public class ReportFile {
	private String path;
	private FileOutputStream fo = null;
	private File file =null;
	private PrintWriter pw = null;
	
	//创建一个新的report文件
	public ReportFile(String name) {
		path = name;
		try {
			//fo = new FileOutputStream(name);
			file = new File(path);
			pw = new PrintWriter(file,"utf-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 将log写到文件缓存
	 * @param log
	 * @return
	 */
	public Boolean add(String log) {
		if(pw != null)
		{
			pw.println(log);
			return true;
		}
		return false;
		
	}
	
	public void close() {
		pw.close();
		/*
		try {
			fo.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	}
	
	public String getFullName() {
		return this.path;
	}

}

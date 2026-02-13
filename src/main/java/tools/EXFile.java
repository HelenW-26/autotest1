package tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 读取翻译的Excel文档
 * @author FengLiu
 *
 */
public class EXFile {
	private final String[] languages = {"EN","FR","ES","PH","KR","CN","JP","TH","VN"};
	private Boolean initial;
	private ArrayList<String> translations  = null;
	private int Column;
	
	/**
	 * 
	 * @param dataPath 文件路径
	 * @param sheetName Excel中sheet的名字
	 * @param column 目标语言所在的列
	 * @throws IOException
	 */
	public EXFile(String dataPath,String sheetName,int column) throws IOException {
		File file=new File(dataPath);
		
		InputStream is = null;
		XSSFWorkbook wb = null;
		
		translations = new ArrayList<String>();
		try {
			 is = new FileInputStream(file);
			 
			 wb = new XSSFWorkbook(is);
			 
			XSSFSheet values = wb.getSheet(sheetName);//获取Excel的指定sheet
			int rows = values.getLastRowNum();//获取行数
			int columns = values.getRow(0).getLastCellNum();//获取列数
			
			if (column >columns+1 || column < 1){
				this.initial = false;
				return;
			}
			Column = column;
			
			//循环读出指定列的所有值
				for(int j = 0; j <= rows;j++) {
					
					XSSFRow row = values.getRow(j);
					if(row== null) {
						translations.add("");
						continue;
					}
					XSSFCell cell = row.getCell(column-1);
					if(cell == null) {
						translations.add("");
						continue;
					}
					if(cell.getCellType() != CellType.STRING) {
						cell.setCellType(CellType.STRING);
					}
					
					String value = cell.getStringCellValue().trim();
					translations.add(new String(value.getBytes("utf-8"),"utf-8"));
				}
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
			System.out.println(e.getStackTrace());
			this.initial = false;
		}finally {
			wb.close();
			is.close();
		}
		this.initial = true;
	}
	
	public int getsize() {
		return this.translations.size();
	}
	public Boolean checkinitial() {
		return this.initial;
	}
	public void printContents(String value) {
		int pos = this.translations.indexOf(value);
		//System.out.println("========Missing Match========================");
		System.out.printf("Row: %d , Column: %d \n%s\n",pos+1,Column,value);
		
	}
	/**
	 * 
	 * @return 返回一个副本
	 */
	public ArrayList<String> getContent(){
		ArrayList<String> contents = new ArrayList<String>();
		contents.addAll(this.translations);
		return contents;
	}

}

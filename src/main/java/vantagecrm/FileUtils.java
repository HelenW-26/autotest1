package vantagecrm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Set;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.google.common.io.Files;

import utils.ExtentReports.ExtentTestManager;

/*
 * Yanni on 18/05/2020: for common operations of files like txt, excel
 */

public class FileUtils {
	
	
	//Read all cell values to 2-dimensioned 
	//Excel row number and col number start from 0.
	public static String[][] readExcel(String filePath, String sheetName, int colNumber) throws Exception 
	{
		String[][] result = null;		
		File file=new File(filePath);
		int i, j, rowNumber;
		
		System.out.println("Start reading files");
		
		try 
		{
			InputStream is = new FileInputStream(file);
			XSSFWorkbook wb = new XSSFWorkbook(is);
			DataFormatter df = new DataFormatter();
			XSSFSheet sheetContent = wb.getSheet(sheetName);
			
					
			rowNumber = sheetContent.getLastRowNum();
			
			//System.out.println("Row number is: " + rowNumber);
			result = new String[rowNumber][colNumber];
			
			
			for(i=0; i<sheetContent.getLastRowNum(); i++) 
			{
				
					for(j=0; j<colNumber; j++)
					{
						//Read from 2nd line (1st line is reserved for header)
						result[i][j]=df.formatCellValue(sheetContent.getRow(i+1).getCell(j));
						System.out.println(result[i][j]);
				
					}
			}
			
			wb.close();
				
	
		}catch(FileNotFoundException e) 
		{
			System.out.println("Can't find file: " + filePath);
		}
						
		return result;
		
		}	  

}



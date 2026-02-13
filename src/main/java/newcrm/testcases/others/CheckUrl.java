package newcrm.testcases.others;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

import newcrm.pages.Page;
import vantagecrm.Utils;

public class CheckUrl extends Page {

	public CheckUrl(WebDriver driver) {
		super(driver);
	}
	
	public List<String> getUrl() throws FileNotFoundException{
		Scanner inputFile;
		String filedir = "C:\\testcases\\ACM973 AU rebranding\\new urls.txt";
		inputFile = new Scanner(new File(filedir));
		ArrayList<String>  result = new ArrayList<>();
		while (inputFile.hasNext()) {
			String line = inputFile.next();
			if(!line.contains("http")) {
				line = "https://"+line;
			}
			result.add(line);
		}
		inputFile.close();
		return result;
	}
	
	public static void main(String [] args) throws FileNotFoundException {
		WebDriver v_driver = null;
		v_driver = Utils.funcSetupDriver(v_driver, "chrome", "false");
		v_driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1));
		CheckUrl ck = new CheckUrl(v_driver);
		
		List<String> urls = ck.getUrl();
		System.out.println("=================Start check ==================");
		for(String url:urls) {
			try {
			ck.driver.get(url);
			//ck.waitLoading();
			String keyStr = url.substring(url.indexOf("//")+2);
			Boolean r = ck.checkUrlContains(keyStr);
			if(!r) {
				System.out.println("!!!!!!!!!!!!!!!!!! WARNING  !!!!!!!!!!!!!!!!!!");
			}
			System.out.println("check url: "+url+" is " + r);
			}catch(WebDriverException e) {
				System.out.println("!!!!!!!!!!!!!!!!!! WARNING  !!!!!!!!!!!!!!!!!!");
				System.out.println("!!! please check the url: " + url + "\n because of : " + e.getMessage());
			}
		}
		
		ck.driver.quit();
		System.out.println("=================Finish check, please double check the log ==================");
	}
}

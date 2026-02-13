package vantagecrm;

import java.util.ArrayList;
import java.util.List;
import org.testng.TestNG;

import vantagecrm.Utils;

public class EntryTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		  
		  List<String> xmlPaths=new ArrayList<String>();
		  System.out.println("Parameter got is "+args[0] + ". System working dir is "+Utils.workingDir);
			  
		  xmlPaths.add(Utils.workingDir+"\\" + args[0]);
		  TestNG tng = new TestNG();
		  tng.setTestSuites(xmlPaths);
		  try 
		  {
			  tng.run();
		  }catch (Exception e)
		  {
			  e.printStackTrace();
		  }

	}

}

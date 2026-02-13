package vantagecrm;

import java.io.File;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.TestNG;
import org.testng.xml.XmlClass;

import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;


public class DynamicTestNG {
 public void runTestNGTest(Map<String,String> testngParams)
 {   //Create an instance on TestNG 
     TestNG myTestNG = new TestNG();   
     
   
     /*TestListenerAdapter tla = new TestListenerAdapter();
     myTestNG.addListener((ITestNGListener) tla);*/
     
        
     //Create an instance of XML Suite and assign a name for it. 
      XmlSuite mySuite = new XmlSuite(); 
      mySuite.setName("MySuite"); 
      //mySuite.setParallel(XmlSuite.ParallelMode.METHODS);   

     //Create an instance of XmlTest and assign a name for it.  
     XmlTest myTest = new XmlTest(mySuite); 
     myTest.setName("MyTest");   
  
     //Add any parameters that you want to set to the Test. 
     myTest.setParameters(testngParams); 
   
     //Create a list which can contain the classes that you want to run.
     List<XmlClass> myClasses = new ArrayList<XmlClass>();
     myClasses.add(new XmlClass("vantagecrm.IpLinkNavigation"));   

     //Assign that to the XmlTest Object created earlier. 
     myTest.setXmlClasses(myClasses);   

     //Create a list of XmlTests and add the Xmltest you created earlier to it.
     List<XmlTest> myTests = new ArrayList<XmlTest>(); 
     myTests.add(myTest);   

     //add the list of tests to your Suite. 
     mySuite.setTests(myTests);   

     //Add the suite to the list of suites. 
     List<XmlSuite> mySuites = new ArrayList<XmlSuite>(); 
     mySuites.add(mySuite);   
     
     //Set the list of Suites to the testNG object you created earlier. 
     myTestNG.setXmlSuites(mySuites);
     mySuite.setFileName("myTemp.xml"); 
     mySuite.setThreadCount(3);   
     myTestNG.run();

     //Create physical XML file based on the virtual XML content 
     for(XmlSuite suite : mySuites) 
     {  
         createXmlFile(suite); 
     }   
    
     System.out.println("Filerated successfully.");
 }
 

    //This method will create an Xml file based on the XmlSuite data 
    public void createXmlFile(XmlSuite mSuite) 
    { 
       FileWriter writer; 
       try { 
            writer = new FileWriter(new File("myTemp.xml")); 
            writer.write(mSuite.toXml()); 
            writer.flush(); 
            writer.close(); 
            System.out.println(new File("myTemp.xml").getAbsolutePath());
            } catch (IOException e)
            {
              e.printStackTrace(); 
            }
    }
/*   //Main Method
   public static void main (String args[]) 
   { 
       DynamicTestNG dt = new DynamicTestNG(); 
       
      

      //This Map can hold your testng Parameters. 
      Map<String,String> testngParams = new HashMap<String,String> ();
      testngParams.put("TestEnv","test"); 
      dt.runTestNGTest(testngParams); 
   }*/
 }

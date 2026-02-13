package vantagecrm;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class UpdateXmlFiles {
	
	private static String projectPath = System.getProperty("user.dir");
	private static File folder = new File(projectPath + "/src/main/resources/vantagecrm/VFSC/ALPHA");
	private static String dataPath = "/src/main/resources/vantagecrm/Data/XmlParameters.xlsx";
	
	public static void main(String[] args) throws Exception {
		
		//updateFileName();
		
		updateXmlFiles();
		
	    }
	
	private static void updateFileName()
	{

		 File[] listOfFiles = folder.listFiles();

	        for (int i = 0; i < listOfFiles.length; i++) {
				
	        	if (listOfFiles[i].isFile()) 
	                listOfFiles[i].renameTo(new File(folder, listOfFiles[i].getName().replace("IP", "VU")));
	        }

	        System.out.println("Done");
	}
	
	    
    public static void updateXmlFiles() throws Exception {
    
    File listOfFiles [] =folder.listFiles();
    String fileName = null;
	  
	  String fileXmlPath = projectPath + dataPath;
	  
	  String sheetName="sheet1";
	  int colNumber=2;
	 
	  String [][] params = FileUtils.readExcel(fileXmlPath,sheetName,colNumber);
    
    for(int i=0;i<listOfFiles.length;i++)
    {
    	fileName = listOfFiles[i].getName();
    	updateXml(folder+"\\"+fileName, params);
	}   
  };
    
    public static void updateXml(String filePath,String[][] params){
		
    try{
    	
	    DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	    Document doc = docBuilder.parse(filePath);
		
	    NodeList nodeList = doc.getElementsByTagName("parameter");
	    
	    for (int temp = 0; temp < nodeList.getLength(); temp++) {
	        Node node = nodeList.item(temp);
	        //System.out.println("\nCurrent element: " + node.getNodeName());
	        if (node.getNodeType() == Node.ELEMENT_NODE) {
	        	for (int i=0; i<params.length; i++)
	        	{
	        		String paraName=node.getAttributes().getNamedItem("name").getNodeValue();
	        		if( params[i][0].toString().equals(paraName))
	        		{
		        		//System.out.println("Name: " + node.getAttributes().getNamedItem("name").getNodeValue());
			            node.getAttributes().getNamedItem("value").setNodeValue(params[i][1]);
			            break;
	        		}
		        }
	        	}
	            
	    }
	    
	    TransformerFactory transformerFactory = TransformerFactory.newInstance();
	    Transformer transformer = transformerFactory.newTransformer();
	    DOMSource source = new DOMSource(doc);
	    StreamResult result = new StreamResult(new File(filePath));
	    transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "https://testng.org/testng-1.0.dtd");
	    transformer.transform(source, result);
	    System.out.println("Done. File: " + filePath);
    } catch (ParserConfigurationException e) {
        e.printStackTrace();
    } catch (TransformerException e) {
        e.printStackTrace();
    } catch (SAXException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    } catch (Exception e) {
    	e.printStackTrace();
    }
    
		       
	}
	    
	    
	}
	
	
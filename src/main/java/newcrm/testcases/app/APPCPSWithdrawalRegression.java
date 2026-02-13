package newcrm.testcases.app;


import newcrm.utils.app.*;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONObject;

import static org.testng.Assert.*;

public class APPCPSWithdrawalRegression {

	@Test
	@Parameters(value= {"APPURL","Regulator","Brand","UserId","AccountNo"})
	public void cpsIDBTWithdraw(String host, String regulator, String brand, String userId, String accountNo) {
		regulator = regulator.toUpperCase();
        Withdraw withdraw =new Withdraw(host,regulator,brand);

        String result = withdraw.sendSpecifiedCPSWithdraw(userId,accountNo,"USD","100.00","39");

        System.out.println(result);
		JSONObject obj = JSONObject.parseObject(result);
		Integer rescode = obj.getInteger("code");
		assertTrue(rescode.equals(1000) , "APP CPS Indonesia Local Bank Transfer withdraw failed!! \n"+result);
	}
	
	@Test
	@Parameters(value= {"APPURL","Regulator","Brand","UserId","AccountNo"})
	public void cpsMYBTWithdraw(String host, String regulator, String brand, String userId, String accountNo) {
		regulator = regulator.toUpperCase();
        Withdraw withdraw =new Withdraw(host,regulator,brand);

        String result = withdraw.sendSpecifiedCPSWithdraw(userId,accountNo,"USD","100.00","6");

        System.out.println(result);
		JSONObject obj = JSONObject.parseObject(result);
		Integer rescode = obj.getInteger("code");
		assertTrue(rescode.equals(1000) , "APP CPS Malaysia Local Bank Transfer withdraw failed!! \n"+result);
	}
	
	@Test
	@Parameters(value= {"APPURL","Regulator","Brand","UserId","AccountNo"})
	public void cpsSABTWithdraw(String host, String regulator, String brand, String userId, String accountNo) {
		regulator = regulator.toUpperCase();
        Withdraw withdraw =new Withdraw(host,regulator,brand);

        String result = withdraw.sendSpecifiedCPSWithdraw(userId,accountNo,"USD","100.00","42");

        System.out.println(result);
		JSONObject obj = JSONObject.parseObject(result);
		Integer rescode = obj.getInteger("code");
		assertTrue(rescode.equals(1000) , "APP CPS South Africa Local Bank Transfer withdraw failed!! \n"+result);
	}
	
	@Test
	@Parameters(value= {"APPURL","Regulator","Brand","UserId","AccountNo"})
	public void cpsKOBTWithdraw(String host, String regulator, String brand, String userId, String accountNo) {
		regulator = regulator.toUpperCase();
        Withdraw withdraw =new Withdraw(host,regulator,brand);

        String result = withdraw.sendSpecifiedCPSWithdraw(userId,accountNo,"USD","100.00","35");

        System.out.println(result);
		JSONObject obj = JSONObject.parseObject(result);
		Integer rescode = obj.getInteger("code");
		assertTrue(rescode.equals(1000) , "APP CPS Korean Local Bank Transfer withdraw failed!! \n"+result);
	}
	
	@Test
	@Parameters(value= {"APPURL","Regulator","Brand","UserId","AccountNo"})
	public void cpsTWBTWithdraw(String host, String regulator, String brand, String userId, String accountNo) {
		regulator = regulator.toUpperCase();
        Withdraw withdraw =new Withdraw(host,regulator,brand);

        String result = withdraw.sendSpecifiedCPSWithdraw(userId,accountNo,"USD","100.00","102");

        System.out.println(result);
		JSONObject obj = JSONObject.parseObject(result);
		Integer rescode = obj.getInteger("code");
		assertTrue(rescode.equals(1000) , "APP CPS Taiwan Local Bank Transfer withdraw failed!! \n"+result);
	}
	
	@Test
	@Parameters(value= {"APPURL","Regulator","Brand","UserId","AccountNo"})
	public void cpsGHBTWithdraw(String host, String regulator, String brand, String userId, String accountNo) {
		regulator = regulator.toUpperCase();
        Withdraw withdraw =new Withdraw(host,regulator,brand);

        String result = withdraw.sendSpecifiedCPSWithdraw(userId,accountNo,"USD","100.00","57");

        System.out.println(result);
		JSONObject obj = JSONObject.parseObject(result);
		Integer rescode = obj.getInteger("code");
		assertTrue(rescode.equals(1000) , "APP CPS Ghana Local Bank Transfer withdraw failed!! \n"+result);
	}
	
	@Test
	@Parameters(value= {"APPURL","Regulator","Brand","UserId","AccountNo"})
	public void cpsRWBTWithdraw(String host, String regulator, String brand, String userId, String accountNo) {
		regulator = regulator.toUpperCase();
        Withdraw withdraw =new Withdraw(host,regulator,brand);

        String result = withdraw.sendSpecifiedCPSWithdraw(userId,accountNo,"USD","100.00","51");

        System.out.println(result);
		JSONObject obj = JSONObject.parseObject(result);
		Integer rescode = obj.getInteger("code");
		assertTrue(rescode.equals(1000) , "APP CPS Rwanda Local Bank Transfer withdraw failed!! \n"+result);
	}
	
	@Test
	@Parameters(value= {"APPURL","Regulator","Brand","UserId","AccountNo"})
	public void cpsTABTWithdraw(String host, String regulator, String brand, String userId, String accountNo) {
		regulator = regulator.toUpperCase();
        Withdraw withdraw =new Withdraw(host,regulator,brand);

        String result = withdraw.sendSpecifiedCPSWithdraw(userId,accountNo,"USD","100.00","58");

        System.out.println(result);
		JSONObject obj = JSONObject.parseObject(result);
		Integer rescode = obj.getInteger("code");
		assertTrue(rescode.equals(1000) , "APP CPS Tanzania Local Bank Transfer withdraw failed!! \n"+result);
	}
	
	@Test
	@Parameters(value= {"APPURL","Regulator","Brand","UserId","AccountNo"})
	public void cpsUGBTWithdraw(String host, String regulator, String brand, String userId, String accountNo) {
		regulator = regulator.toUpperCase();
        Withdraw withdraw =new Withdraw(host,regulator,brand);

        String result = withdraw.sendSpecifiedCPSWithdraw(userId,accountNo,"USD","100.00","50");

        System.out.println(result);
		JSONObject obj = JSONObject.parseObject(result);
		Integer rescode = obj.getInteger("code");
		assertTrue(rescode.equals(1000) , "APP CPS Uganda Local Bank Transfer withdraw failed!! \n"+result);
	}
	
	@Test
	@Parameters(value= {"APPURL","Regulator","Brand","UserId","AccountNo"})
	public void cpsCABTWithdraw(String host, String regulator, String brand, String userId, String accountNo) {
		regulator = regulator.toUpperCase();
        Withdraw withdraw =new Withdraw(host,regulator,brand);

        String result = withdraw.sendSpecifiedCPSWithdraw(userId,accountNo,"USD","100.00","54");

        System.out.println(result);
		JSONObject obj = JSONObject.parseObject(result);
		Integer rescode = obj.getInteger("code");
		assertTrue(rescode.equals(1000) , "APP CPS Cameroon Local Bank Transfer withdraw failed!! \n"+result);
	}
	
	@Test
	@Parameters(value= {"APPURL","Regulator","Brand","UserId","AccountNo"})
	public void cpsKEBTWithdraw(String host, String regulator, String brand, String userId, String accountNo) {
		regulator = regulator.toUpperCase();
        Withdraw withdraw =new Withdraw(host,regulator,brand);

        String result = withdraw.sendSpecifiedCPSWithdraw(userId,accountNo,"USD","100.00","56");

        System.out.println(result);
		JSONObject obj = JSONObject.parseObject(result);
		Integer rescode = obj.getInteger("code");
		assertTrue(rescode.equals(1000) , "APP CPS Kenya Local Bank Transfer withdraw failed!! \n"+result);
	}
	
	@Test
	@Parameters(value= {"APPURL","Regulator","Brand","UserId","AccountNo"})
	public void cpsTHBTWithdraw(String host, String regulator, String brand, String userId, String accountNo) {
		regulator = regulator.toUpperCase();
        Withdraw withdraw =new Withdraw(host,regulator,brand);

        String result = withdraw.sendSpecifiedCPSWithdraw(userId,accountNo,"USD","100.00","5");

        System.out.println(result);
		JSONObject obj = JSONObject.parseObject(result);
		Integer rescode = obj.getInteger("code");
		assertTrue(rescode.equals(1000) , "APP CPS Thailand Local Bank Transfer withdraw failed!! \n"+result);
	}
	
	@Test
	@Parameters(value= {"APPURL","Regulator","Brand","UserId","AccountNo"})
	public void cpsVNBTWithdraw(String host, String regulator, String brand, String userId, String accountNo) {
		regulator = regulator.toUpperCase();
        Withdraw withdraw =new Withdraw(host,regulator,brand);

        String result = withdraw.sendSpecifiedCPSWithdraw(userId,accountNo,"USD","100.00","8");

        System.out.println(result);
		JSONObject obj = JSONObject.parseObject(result);
		Integer rescode = obj.getInteger("code");
		assertTrue(rescode.equals(1000) , "APP CPS Vietnam Local Bank Transfer withdraw failed!! \n"+result);
	}
	
	@Test
	@Parameters(value= {"APPURL","Regulator","Brand","UserId","AccountNo"})
	public void cpsPHBTWithdraw(String host, String regulator, String brand, String userId, String accountNo) {
		regulator = regulator.toUpperCase();
        Withdraw withdraw =new Withdraw(host,regulator,brand);

        String result = withdraw.sendSpecifiedCPSWithdraw(userId,accountNo,"USD","100.00","40");

        System.out.println(result);
		JSONObject obj = JSONObject.parseObject(result);
		Integer rescode = obj.getInteger("code");
		assertTrue(rescode.equals(1000) , "APP CPS Philippiness Local Bank Transfer withdraw failed!! \n"+result);
	}
	
	@Test
	@Parameters(value= {"APPURL","Regulator","Brand","UserId","AccountNo"})
	public void cpsMXBTWithdraw(String host, String regulator, String brand, String userId, String accountNo) {
		regulator = regulator.toUpperCase();
        Withdraw withdraw =new Withdraw(host,regulator,brand);

        String result = withdraw.sendSpecifiedCPSWithdraw(userId,accountNo,"USD","100.00","63");

        System.out.println(result);
		JSONObject obj = JSONObject.parseObject(result);
		Integer rescode = obj.getInteger("code");
		assertTrue(rescode.equals(1000) , "APP CPS Mexico Local Bank Transfer withdraw failed!! \n"+result);
	}
	
	@Test
	@Parameters(value= {"APPURL","Regulator","Brand","UserId","AccountNo"})
	public void cpsBRLBTWithdraw(String host, String regulator, String brand, String userId, String accountNo) {
		regulator = regulator.toUpperCase();
        Withdraw withdraw =new Withdraw(host,regulator,brand);

        String result = withdraw.sendSpecifiedCPSWithdraw(userId,accountNo,"USD","100.00","64");

        System.out.println(result);
		JSONObject obj = JSONObject.parseObject(result);
		Integer rescode = obj.getInteger("code");
		assertTrue(rescode.equals(1000) , "APP CPS Brazil Local Bank Transfer withdraw failed!! \n"+result);
	}

	@Test
	@Parameters(value= {"APPURL","Regulator","Brand","UserId","AccountNo"})
	public void cpsPIXBTWithdraw(String host, String regulator, String brand, String userId, String accountNo) {
		regulator = regulator.toUpperCase();
        Withdraw withdraw =new Withdraw(host,regulator,brand);

        String result = withdraw.sendSpecifiedCPSWithdraw(userId,accountNo,"USD","100.00","100");

        System.out.println(result);
		JSONObject obj = JSONObject.parseObject(result);
		Integer rescode = obj.getInteger("code");
		assertTrue(rescode.equals(1000) , "APP CPS PIX Bank Transfer withdraw failed!! \n"+result);
	}
	
	@Test
	@Parameters(value= {"APPURL","Regulator","Brand","UserId","AccountNo"})
	public void cpsJPBTWithdraw(String host, String regulator, String brand, String userId, String accountNo) {
		regulator = regulator.toUpperCase();
        Withdraw withdraw =new Withdraw(host,regulator,brand);

        String result = withdraw.sendSpecifiedCPSWithdraw(userId,accountNo,"USD","100.00","62");

        System.out.println(result);
		JSONObject obj = JSONObject.parseObject(result);
		Integer rescode = obj.getInteger("code");
		assertTrue(rescode.equals(1000) , "APP CPS Japan Local Bank Transfer withdraw failed!! \n"+result);
	}
	
	@Test
	@Parameters(value= {"APPURL","Regulator","Brand","UserId","AccountNo"})
	public void cpsINBTWithdraw(String host, String regulator, String brand, String userId, String accountNo) {
		regulator = regulator.toUpperCase();
        Withdraw withdraw =new Withdraw(host,regulator,brand);

        String result = withdraw.sendSpecifiedCPSWithdraw(userId,accountNo,"USD","100.00","24");

        System.out.println(result);
		JSONObject obj = JSONObject.parseObject(result);
		Integer rescode = obj.getInteger("code");
		assertTrue(rescode.equals(1000) , "APP CPS India Local Bank Transfer withdraw failed!! \n"+result);
	}
	
	@Test
	@Parameters(value= {"APPURL","Regulator","Brand","UserId","AccountNo"})
	public void cpsHKBTWithdraw(String host, String regulator, String brand, String userId, String accountNo) {
		regulator = regulator.toUpperCase();
        Withdraw withdraw =new Withdraw(host,regulator,brand);

        String result = withdraw.sendSpecifiedCPSWithdraw(userId,accountNo,"HKD","100.00","49");

        System.out.println(result);
		JSONObject obj = JSONObject.parseObject(result);
		Integer rescode = obj.getInteger("code");
		assertTrue(rescode.equals(1000) , "APP CPS Hong Kong (HKD) Local Bank Transfer withdraw failed!! \n"+result);
	}
	
	@Test
	@Parameters(value= {"APPURL","Regulator","Brand","UserId","AccountNo"})
	public void cpsHKUSDBTWithdraw(String host, String regulator, String brand, String userId, String accountNo) {
		regulator = regulator.toUpperCase();
        Withdraw withdraw =new Withdraw(host,regulator,brand);

        String result = withdraw.sendSpecifiedCPSWithdraw(userId,accountNo,"USD","100.00","101");

        System.out.println(result);
		JSONObject obj = JSONObject.parseObject(result);
		Integer rescode = obj.getInteger("code");
		assertTrue(rescode.equals(1000) , "APP CPS Hong Kong (USD) Local Bank Transfer withdraw failed!! \n"+result);
	}
	
	@Test
	@Parameters(value= {"APPURL","Regulator","Brand","UserId","AccountNo"})
	public void cpsNGBTWithdraw(String host, String regulator, String brand, String userId, String accountNo) {
		regulator = regulator.toUpperCase();
        Withdraw withdraw =new Withdraw(host,regulator,brand);

        String result = withdraw.sendSpecifiedCPSWithdraw(userId,accountNo,"USD","100.00","9");

        System.out.println(result);
		JSONObject obj = JSONObject.parseObject(result);
		Integer rescode = obj.getInteger("code");
		assertTrue(rescode.equals(1000) , "APP CPS Nigeria Local Bank Transfer withdraw failed!! \n"+result);
	}
	
	@Test
	@Parameters(value= {"APPURL","Regulator","Brand","UserId","AccountNo"})
	public void cpsInteracWithdraw(String host, String regulator, String brand, String userId, String accountNo) {
		regulator = regulator.toUpperCase();
        Withdraw withdraw =new Withdraw(host,regulator,brand);

        String result = withdraw.sendSpecifiedCPSWithdraw(userId,accountNo,"USD","100.00","99");

        System.out.println(result);
		JSONObject obj = JSONObject.parseObject(result);
		Integer rescode = obj.getInteger("code");
		assertTrue(rescode.equals(1000) , "APP CPS Interac withdraw failed!! \n"+result);
	}
	
	@Test
	@Parameters(value= {"APPURL","Regulator","Brand","UserId","AccountNo"})
	public void cpsFasapayWithdraw(String host, String regulator, String brand, String userId, String accountNo) { 
		regulator = regulator.toUpperCase();
		Withdraw withdraw =new Withdraw(host,regulator,brand);
	  
		String result = withdraw.sendEwalletWithdrawBatch(userId,accountNo,"USD","840","40.00","7","F00000_115");
	  
		System.out.println(result); JSONObject obj = JSONObject.parseObject(result);
		Integer rescode = obj.getInteger("code"); 
		assertTrue(rescode.equals(1000) , "APP CPS Fasapay withdraw failed!! \n"+result); 
	}
	
	@Test
	@Parameters(value= {"APPURL","Regulator","Brand","UserId","AccountNo"})
	public void cpsEbuyWithdraw(String host, String regulator, String brand, String userId, String accountNo) { 
		regulator = regulator.toUpperCase();
		Withdraw withdraw =new Withdraw(host,regulator,brand);
	  
		String result = withdraw.sendEwalletWithdrawBatch(userId,accountNo,"USD","840","40.00","75","F00000_094");
	  
		System.out.println(result); JSONObject obj = JSONObject.parseObject(result);
		Integer rescode = obj.getInteger("code"); 
		assertTrue(rescode.equals(1000) , "APP CPS Ebuy withdraw failed!! \n"+result); 
	}
	
	@Test
	@Parameters(value= {"APPURL","Regulator","Brand","UserId","AccountNo"})
	public void cpsSkrillWithdraw(String host, String regulator, String brand, String userId, String accountNo) { 
		regulator = regulator.toUpperCase();
		Withdraw withdraw =new Withdraw(host,regulator,brand);
	  
		String result = withdraw.sendEwalletWithdrawBatch(userId,accountNo,"USD","840","40.00","31","F00000_104");
	  
		System.out.println(result); JSONObject obj = JSONObject.parseObject(result);
		Integer rescode = obj.getInteger("code"); 
		assertTrue(rescode.equals(1000) , "APP CPS Skrill withdraw failed!! \n"+result); 
	}
	
	@Test
	@Parameters(value= {"APPURL","Regulator","Brand","UserId","AccountNo"})
	public void cpsNetellerithdraw(String host, String regulator, String brand, String userId, String accountNo) { 
		regulator = regulator.toUpperCase();
		Withdraw withdraw =new Withdraw(host,regulator,brand);
	  
		String result = withdraw.sendEwalletWithdrawBatch(userId,accountNo,"USD","840","40.00","32","F00000_106");
	  
		System.out.println(result); JSONObject obj = JSONObject.parseObject(result);
		Integer rescode = obj.getInteger("code"); 
		assertTrue(rescode.equals(1000) , "APP CPS Neteller withdraw failed!! \n"+result); 
	}
	
	@Test
	@Parameters(value= {"APPURL","Regulator","Brand","UserId","AccountNo"})
	public void cpsPerfectMoneyWithdraw(String host, String regulator, String brand, String userId, String accountNo) { 
		regulator = regulator.toUpperCase();
		Withdraw withdraw =new Withdraw(host,regulator,brand);
	  
		String result = withdraw.sendEwalletWithdrawBatch(userId,accountNo,"USD","840","40.00","61","F00000_102");
	  
		System.out.println(result); JSONObject obj = JSONObject.parseObject(result);
		Integer rescode = obj.getInteger("code"); 
		assertTrue(rescode.equals(1000) , "APP CPS Perfect Money withdraw failed!! \n"+result); 
	}
	
	@Test
	@Parameters(value= {"APPURL","Regulator","Brand","UserId","AccountNo"})
	public void cpsAdvcashWithdraw(String host, String regulator, String brand, String userId, String accountNo) { 
		regulator = regulator.toUpperCase();
		Withdraw withdraw =new Withdraw(host,regulator,brand);
	  
		String result = withdraw.sendEwalletWithdrawBatch(userId,accountNo,"USD","840","40.00","69","F00000_086");
	  
		System.out.println(result); JSONObject obj = JSONObject.parseObject(result);
		Integer rescode = obj.getInteger("code"); 
		assertTrue(rescode.equals(1000) , "APP CPS Advcash withdraw failed!! \n"+result); 
	}
	
	@Test
	@Parameters(value= {"APPURL","Regulator","Brand","UserId","AccountNo"})
	public void cpsAstropayWithdraw(String host, String regulator, String brand, String userId, String accountNo) { 
		regulator = regulator.toUpperCase();
		Withdraw withdraw =new Withdraw(host,regulator,brand);
	  
		String result = withdraw.sendEwalletWithdrawBatch(userId,accountNo,"USD","840","40.00","41","F00000_099");
	  
		System.out.println(result); JSONObject obj = JSONObject.parseObject(result);
		Integer rescode = obj.getInteger("code"); 
		assertTrue(rescode.equals(1000) , "APP CPS Astropay withdraw failed!! \n"+result); 
	}
	
	@Test
	@Parameters(value= {"APPURL","Regulator","Brand","UserId","AccountNo"})
	public void cpsSticPayWithdraw(String host, String regulator, String brand, String userId, String accountNo) { 
		regulator = regulator.toUpperCase();
		Withdraw withdraw =new Withdraw(host,regulator,brand);
	  
		String result = withdraw.sendEwalletWithdrawBatch(userId,accountNo,"USD","840","40.00","38","F00000_111");
	  
		System.out.println(result); JSONObject obj = JSONObject.parseObject(result);
		Integer rescode = obj.getInteger("code"); 
		assertTrue(rescode.equals(1000) , "APP CPS Sticpay withdraw failed!! \n"+result); 
	}
	
	@Test
	@Parameters(value= {"APPURL","Regulator","Brand","UserId","AccountNo"})
	public void cpsAirTMWithdraw(String host, String regulator, String brand, String userId, String accountNo) { 
		regulator = regulator.toUpperCase();
		Withdraw withdraw =new Withdraw(host,regulator,brand);
	  
		String result = withdraw.sendEwalletWithdrawBatch(userId,accountNo,"USD","840","40.00","109","F00000_179");
	  
		System.out.println(result); JSONObject obj = JSONObject.parseObject(result);
		Integer rescode = obj.getInteger("code"); 
		assertTrue(rescode.equals(1000) , "APP CPS AirTM withdraw failed!! \n"+result); 
	}
	
	@Test
	@Parameters(value= {"APPURL","Regulator","Brand","UserId","AccountNo"})
	public void cpsFXIRWithdraw(String host, String regulator, String brand, String userId, String accountNo) { 
		regulator = regulator.toUpperCase();
		Withdraw withdraw =new Withdraw(host,regulator,brand);
	  
		String result = withdraw.sendEwalletWithdrawBatch(userId,accountNo,"USD","840","40.00","97","F00000_178");
	  
		System.out.println(result); JSONObject obj = JSONObject.parseObject(result);
		Integer rescode = obj.getInteger("code"); 
		assertTrue(rescode.equals(1000) , "APP CPS FX-IR withdraw failed!! \n"+result); 
	}
	
	@Test
	@Parameters(value= {"APPURL","Regulator","Brand","UserId","AccountNo"})
	public void cpsUAEBTWithdraw(String host, String regulator, String brand, String userId, String accountNo) {
		regulator = regulator.toUpperCase();
        Withdraw withdraw =new Withdraw(host,regulator,brand);

        String result = withdraw.sendSpecifiedCPSWithdraw(userId,accountNo,"USD","50","108");

        System.out.println(result);
		JSONObject obj = JSONObject.parseObject(result);
		Integer rescode = obj.getInteger("code");
		assertTrue(rescode.equals(1000) , "APP CPS UAE Bank Transfer withdraw failed!! \n"+result);
	}
}

package vantagecrm;

public class ToDoList {
	
	/*1. Admin -> ID Audit, POA Audit, when there are 2 files uploaded, check they are different paths. (Added by Yanni on 22/05/2020)
		--done by Yanni on 25/05/2020  in NewTaskManagement.java -> funcIDPOAOperation*/
	
	/*2. System settings -> check menu names: not show xx.xx.xxxx  (Added by Yanni on 22/05/2020)   
		--- Done by Alex L in SystemSetting.java  */
	
	/*3. CPRegister -> Add function to check if the account has been auto audit .  (Added by AlexL on 22/05/2020) 
	      //Note by Yanni: CPRegister.java -> funcGetWorldCheck can return result. 
		      ---Done by Alex L on 01/06/2020 in CPRegister.java  */
	
	//4. Register and Account audit -> Add XML to cover all auto audit scenarios.  (Added by AlexL on 22/05/2020)
	//5. CPDeposit， all deposit methods can't run with CIMA beta
	//6. Thailand Mijipay - not developed. 
	//7. Indonesia pay - not developed
	
	//8. Add Fee check in Admin-> Deposit audit. (Done Added by Yanni and assigned to Fiona)
	
	//9. IBPortal -> Withdraw: Lulu change the structure. Need to rerun. (Done by Fiona. Added by Yanni on 25/05/2020)
	
	//10. Add function to change account group to TEST group after auto audit to a PROD group.  (Added by AlexL on 02/06/2020)
	
	//11. (NEW!!!) IB Portal -> Withdraw: need to develop a Canada IB withdraw via International.
	      // Canada IB will have 2 extra fields: Transit Number and Institution Number
	//12. CPWithdraw.java -> WithdrawCC: need to be updated for CIMA credit card withdraw
}

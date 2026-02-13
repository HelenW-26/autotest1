package newcrm.global;

import java.util.Map;
import java.util.List;
import java.util.Arrays;

public final class GlobalProperties {
	public static String env="alpha";
	public static String brand = "vfx";
	public static String regulator = "vfsc";
	public static String platform = "mt4";


	public static enum ENV{
		PROD,
		ALPHA,
		UAT;
		public static ENV getENV(String name) {
			for(ENV b: ENV.values()) {
				if(b.toString().equalsIgnoreCase(name)) {
					return b;
				}
			}
			return null;
		}
	}

	public static enum BRAND{
		PUG,
		VFX,
		VT,
		MO,
		POSP,
		KCM,
		UM,
		VDM,
		AT,
		STAR,
		VJP,
		PUGPT;
		public static BRAND getBRAND(String name) {
			for(BRAND b: BRAND.values()) {
				if(b.toString().equalsIgnoreCase(name)) {
					return b;
				}
			}
			return null;
		}
	}
	public static enum REGULATOR{
		VFSC,
		VFSC2,
		ASIC,
		CIMA,
		FCA,
		SVG,
		FSA,
		SCA,
		GLOBAL;

		public static REGULATOR getREGULATOR(String name) {
			for(REGULATOR r: REGULATOR.values()) {
				if(r.toString().equalsIgnoreCase(name)) {
					return r;
				}
			}
			return null;
		}
	}

	public static enum DEPOSITMETHOD {

		THAIINSTANT {
			@Override
			public String getWebName() {
				return "INTERNET BANKING (THAILAND)";
			}

			@Override
			public String getWithdrawName() {
				return "Thailand Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Internet Banking (Thailand)";
			}

			@Override
			public String getIBWithdrawName() {
				return "Internet Banking (Thailand)";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				if(brand.equalsIgnoreCase("vt")){
					return "thailandinstantbankwiretransfer-5";
				}
				return "thailandBankTransfer-5";
			}

			@Override
			public String getCPTestId() {
				if(brand.equalsIgnoreCase("star"))
				{
					return "thailandbanktransfer";
				}
				else if(brand.equalsIgnoreCase(BRAND.PUG.toString()))
				{
					return "thailandinstantbanktransfer1";
				}
				return "thailandinstantbanktransfer";
			}

			@Override
			public String getdepositDatetestid() {
				return "";
			}

			@Override
			public String getWithdrawDataTestId() { return "ThailandBankTransfer"; }
		},
		THAIINSTANT_iSmartNew {
			@Override
			public String getWebName() {
				if(brand.equalsIgnoreCase("um")||brand.equalsIgnoreCase("star"))
				{
					return "Thailand Instant Bank Transfer";
				}
				return "Internet Banking (Thailand)";
			}

			@Override
			public String getWithdrawName() {
				return "Thailand Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Internet Banking (Thailand)";
			}

			@Override
			public String getIBWithdrawName() {
				return "Internet Banking (Thailand)";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				if(brand.equalsIgnoreCase("vt")){
					return "thailandinstantbankwiretransfer-5";
				}
				return "thailandBankTransfer-5";
			}

			@Override
			public String getCPTestId() {
				if(brand.equalsIgnoreCase("star"))
				{
					return "thailandbanktransfer";
				}
				else if(brand.equalsIgnoreCase("um"))
				{
					return "iSmart-VT_88";
				}
				return "thailandinstantbanktransfer";
			}

			@Override
			public String getdepositDatetestid() {
				if (brand.equalsIgnoreCase("star"))
				{
					return "CroinPlus-THB-Startrader_742";
				}

				return "iSmart";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		THAIINSTANT_XPayNew {
			@Override
			public String getWebName() {
				return "Internet Banking (Thailand)";
			}

			@Override
			public String getWithdrawName() {
				return "Thailand Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Internet Banking (Thailand)";
			}

			@Override
			public String getIBWithdrawName() {
				return "Internet Banking (Thailand)";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				if(brand.equalsIgnoreCase("vt")){
					return "thailandinstantbankwiretransfer-5";
				}
				return "thailandBankTransfer-5";
			}

			@Override
			public String getCPTestId() {
				if(brand.equalsIgnoreCase("star"))
				{
					return "thailandbanktransfer";
				}
				else if (brand.equalsIgnoreCase("um"))
				{
					return "XPay-UM_308";
				}
				return "thailandinstantbanktransfer";
			}

			@Override
			public String getdepositDatetestid() {
				return "xpay";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		PaymentOption_New {
			@Override
			public String getWebName() {
				if (brand.equalsIgnoreCase("vjp")) {
					return "Credit Card";
				}
				return "Credit/Debit Card";
			}

			@Override
			public String getWithdrawName() {
				return "CREDIT/DEBIT CARD";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "CREDIT/DEBIT CARD";
			}

			@Override
			public String getIBWithdrawName() {
				return "need change";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "need change";
			}

			@Override
			public String getCPTestId() {
				return "PaymentOption_Main-VFX_425";
			}

			@Override
			public String getdepositDatetestid() {
				return "PaymentOption_CreditCard";
			}

			@Override
			public String getWithdrawDataTestId() { return "PaymentOption_CreditCard"; }
		},
		CREDITORDEBIT {
			@Override
			public String getWebName() {
				if (brand.equalsIgnoreCase("vjp")) {
					return "Credit Card";
				}
				return "Credit/Debit Card";
			}

			@Override
			public String getWithdrawName() {
				return "CREDIT/DEBIT CARD";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "CREDIT/DEBIT CARD";

			}

			@Override
			public String getIBWithdrawName() {
				return "need change";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "need change";
			}

			@Override
			public String getCPTestId() {
				if(brand.equalsIgnoreCase("um")){
					return "creditClick";
				} else if (brand.equalsIgnoreCase("vjp")){
					return "creditcard";
				}
				return "creditOrDebit";
			}

			@Override
			public String getdepositDatetestid() {
				return "BridgerPayCC";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		CREDITORDEBIT_New {
			@Override
			public String getWebName() {
				if (brand.equalsIgnoreCase("vjp")) {
					return "Credit Card";
				}
				return "Credit/Debit Card";
			}

			@Override
			public String getWithdrawName() {
				return "CREDIT/DEBIT CARD";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Credit/Debit Card";

			}

			@Override
			public String getIBWithdrawName() {
				return "need change";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "need change";
			}

			@Override
			public String getCPTestId() {
				if(brand.equalsIgnoreCase("um")){
					return "creditClick";
				} else if (brand.equalsIgnoreCase("vjp")){
					return "creditcard";
				}
				return "creditOrDebit";
			}

			@Override
			public String getdepositDatetestid() {
				return "BridgerPayCC";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		CCSDPAY {
			@Override
			public String getWebName() {
				return "Credit/Debit Card";
			}

			@Override
			public String getWithdrawName() {
				return "CREDIT/DEBIT CARD";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "CREDIT/DEBIT CARD";

			}

			@Override
			public String getIBWithdrawName() {
				return "need change";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "need change";
			}

			@Override
			public String getCPTestId() {
				if(brand.equalsIgnoreCase("vjp")){
					return "creditOrDebit";
				}
				return "solidPaymentECP";
			}

			@Override
			public String getdepositDatetestid() {
				return "";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		CCMYFATOORAH {
			@Override
			public String getWebName() {
				if(brand.equalsIgnoreCase("star")){
					return "Myfatoorah";
				}
				return "Credit/Debit Card";
			}

			@Override
			public String getWithdrawName() {
				return "need change";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "need change";

			}

			@Override
			public String getIBWithdrawName() {
				return "need change";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "need change";
			}

			@Override
			public String getCPTestId() {
				if (brand.equalsIgnoreCase("vt"))
				{
					return "myfatoorahcreditcards";
				}
				return "myfatoorah";
			}

			@Override
			public String getdepositDatetestid() {
				return "";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		CCMYFATOORAH_New {
			@Override
			public String getWebName() {
				if(brand.equalsIgnoreCase("star")){
					return "Myfatoorah";
				}
				return "Credit/Debit Card";
			}

			@Override
			public String getWithdrawName() {
				return "need change";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "need change";
			}

			@Override
			public String getIBWithdrawName() {
				return "need change";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "need change";
			}

			@Override
			public String getCPTestId() {
				if (brand.equalsIgnoreCase("vt"))
				{
					return "myfatoorahcreditcards";
				}
				return "myfatoorah";
			}

			@Override
			public String getdepositDatetestid() {
				return "Myfatoorah";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		CCGOOGLEPAY {
			@Override
			public String getWebName() {
				return "Google Pay";
			}

			@Override
			public String getWithdrawName() {
				return "need change";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "need change";
			}

			@Override
			public String getIBWithdrawName() {
				return "need change";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "need change";
			}

			@Override
			public String getCPTestId() {
				return "googlePay";
			}

			@Override
			public String getdepositDatetestid() {
				return "";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},

		ASICWORLDPAY {
			@Override
			public String getWebName() {
				return "Credit/Debit Card";
			}

			@Override
			public String getWithdrawName() {
				return "need change";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "need change";

			}

			@Override
			public String getIBWithdrawName() {
				return "need change";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "need change";
			}

			@Override
			public String getCPTestId() {
				return "creditdebitcardprimary";
			}

			@Override
			public String getdepositDatetestid() {
				return "worldpay";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		ASIC_CC_Checkout {
			@Override
			public String getWebName() {
				return "Credit/Debit Card";
			}

			@Override
			public String getWithdrawName() {
				return "need change";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "need change";

			}

			@Override
			public String getIBWithdrawName() {
				return "need change";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "need change";
			}

			@Override
			public String getCPTestId() {
				return "creditdebitcardprimary";
			}

			@Override
			public String getdepositDatetestid() {
				return "CheckoutCC";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		CCAPPLEGOOGLEPAY {
			@Override
			public String getWebName() {
				//Add the name of Apple pay and Goodle pay together
				if(brand.equalsIgnoreCase("star")){
					return "Credit/Debit Card";
				}
				return "Apple Pay/Google Pay";
			}

			@Override
			public String getWithdrawName() {
				return "need change";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "need change";
			}

			@Override
			public String getIBWithdrawName() {
				return "need change";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "need change";
			}

			@Override
			public String getCPTestId() {
				return "applepaygooglepay";
			}

			@Override
			public String getdepositDatetestid() {
				return "AppleGooglePay";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		CCAPPLEGOOGLEPAYNew {
			@Override
			public String getWebName() {
				//Add the name of Apple pay and Goodle pay together
				return "Credit/Debit Card";
			}

			@Override
			public String getWithdrawName() {
				return "need change";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "need change";
			}

			@Override
			public String getIBWithdrawName() {
				return "need change";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "need change";
			}

			@Override
			public String getCPTestId() {
				return "applepaygooglepay";
			}

			@Override
			public String getdepositDatetestid() {
				return "AppleGooglePay";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		APPLEPAYNew {
			@Override
			public String getWebName() {
				//Add the name of Apple pay and Goodle pay together
				return "Apple Pay";
			}

			@Override
			public String getWithdrawName() {
				return "Apple Pay";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "need change";
			}

			@Override
			public String getIBWithdrawName() {
				return "need change";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "need change";
			}

			@Override
			public String getCPTestId() {
				return "applepaygooglepay";
			}

			@Override
			public String getdepositDatetestid() {
				return "ApplePay";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		GOOGLEPAYNew {
			@Override
			public String getWebName() {
				//Add the name of Apple pay and Goodle pay together
				return "Google Pay";
			}

			@Override
			public String getWithdrawName() {
				return "need change";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "need change";
			}

			@Override
			public String getIBWithdrawName() {
				return "need change";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "need change";
			}

			@Override
			public String getCPTestId() {
				return "applepaygooglepay";
			}

			@Override
			public String getdepositDatetestid() {
				return "GooglePay";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		CCBRIDGERPAY {
			@Override
			public String getWebName() {
				return "Credit/Debit Card";
			}

			@Override
			public String getWithdrawName() {
				return "need change";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "need change";
			}

			@Override
			public String getIBWithdrawName() {
				return "need change";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "need change";
			}

			@Override
			public String getCPTestId() {
				if(brand.equalsIgnoreCase("um")){
					return "BridgerPayCC_163";
				}
				return "creditOrDebitBridgepay";
			}

			@Override
			public String getdepositDatetestid() {
				return "BridgerPayCC_163";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		I12BANKTRANSFER {
			@Override
			public String getWebName() {
				if(brand.equalsIgnoreCase("vt")){
					return "International Bank Transfer";
				}
				return "BANK WIRE TRANSFER (INTERNATIONAL)";
			}

			@Override
			public String getWithdrawName() {
				if (brand.equalsIgnoreCase("mo")){
					return "International bank transfer";
				}
				return "International Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				if(brand.equalsIgnoreCase("mo")){
					return "International Bank Transfer-International";
				}
				return "International Bank Transfer";
			}

			@Override
			public String getIBWithdrawName() {
				if(brand.equalsIgnoreCase("um") || brand.equalsIgnoreCase("vt")){
					return "Bank Transfer";
				}
				return "International Bank Transfer";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "banktransfer-2";
			}

			@Override
			public String getCPTestId() {
				if(brand.equalsIgnoreCase("pug"))
				{
					return "i12BankTransferEqual";
				}
				else if (brand.equalsIgnoreCase("vt"))
				{
					return "internationalbanktransfer";
				}
				else if(brand.equalsIgnoreCase("um"))
				{
					return "InternationalBankTransfer_164";
				}
				return "i12BankTransfer";
			}

			@Override
			public String getdepositDatetestid() {
				if(brand.equalsIgnoreCase(BRAND.VT.toString())){
					return "IBT-FAB";
				}
				return "InternationalBankTransfer";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		I12BANKTRANSFERUAT {
			@Override
			public String getWebName() {
				if(brand.equalsIgnoreCase("vt")){
					return "International Bank Transfer";
				}
				return "BANK WIRE TRANSFER (INTERNATIONAL)";
			}

			@Override
			public String getWithdrawName() {
				if (brand.equalsIgnoreCase("mo")){
					return "International bank transfer";
				}
				return "InternationalBankTransfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				if(brand.equalsIgnoreCase("mo")){
					return "International Bank Transfer-International";
				}
				return "International Bank Transfer";
			}

			@Override
			public String getIBWithdrawName() {
				if(brand.equalsIgnoreCase("um") || brand.equalsIgnoreCase("vt")){
					return "Bank Transfer";
				}
				return "International Bank Transfer";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "banktransfer-2";
			}

			@Override
			public String getCPTestId() {
				if(brand.equalsIgnoreCase("pug"))
				{
					return "i12BankTransferEqual";
				}
				else if (brand.equalsIgnoreCase("vt"))
				{
					return "internationalbanktransfer";
				}
				else if(brand.equalsIgnoreCase("um"))
				{
					return "InternationalBankTransfer_164";
				}
				return "i12BankTransfer";
			}

			@Override
			public String getdepositDatetestid() {
				if(brand.equalsIgnoreCase(BRAND.VT.toString())){
					return "IBT-FAB";
				}
				return "InternationalBankTransfer";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},

		I12BANKTRANSFER_New {
			@Override
			public String getWebName() {
				if(brand.equalsIgnoreCase(BRAND.VT.toString()) || brand.equalsIgnoreCase(BRAND.VFX.toString())){
					return "International Bank Transfer";
					//alpha is this, prod is below LOL
				}
				return "BANK WIRE TRANSFER (INTERNATIONAL)";
			}

			@Override
			public String getWithdrawName() {
				return "International Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				if(brand.equalsIgnoreCase(BRAND.PUG.toString()) || brand.equalsIgnoreCase(BRAND.UM.toString()) || brand.equalsIgnoreCase(BRAND.MO.toString())) {
					return "International Bank Transfer-International";
				}
				return "International Bank Transfer";
			}

			@Override
			public String getIBWithdrawName() {
				return "International Bank Transfer";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "International Bank Transfer";
			}

			@Override
			public String getCPTestId() {
				if(brand.equalsIgnoreCase("pug"))
				{
					return "international2";
				}
				else if (brand.equalsIgnoreCase("vt"))
				{
					return "internationalbanktransfer";
				}
				return "i12BankTransfer";
			}

			@Override
			public String getdepositDatetestid() {
				//UM alpha is IBT, but UM prod is InternationalBankTransfer LOL
                if(brand.equalsIgnoreCase(BRAND.VT.toString())|| brand.equalsIgnoreCase(BRAND.VFX.toString())){
                    return "IBT";
                }
				return "InternationalBankTransfer";
			}

			@Override
			public String getWithdrawDataTestId() {
				if(brand.equalsIgnoreCase(BRAND.VFX.toString())){
					return "Internationalbanktransfer";
				}
				return "Internationalbanktransfer_"; }
		},
		IBT_EQUALS {
			@Override
			public String getWebName() {
				return "Bank Wire Transfer (International)";
			}

			@Override
			public String getWithdrawName() {
				return "International Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "need change";
			}

			@Override
			public String getIBWithdrawName() {
				return "need change";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "need change";
			}

			@Override
			public String getCPTestId() {
				return "";
			}

			@Override
			public String getdepositDatetestid() {
				if(brand.equalsIgnoreCase("vfx")|| brand.equalsIgnoreCase("au")){
					return "IBT-Clearing-SGAUCN-VAUVFSC_405";
				}
				if(brand.equalsIgnoreCase("mo")|| brand.equalsIgnoreCase("star")|| brand.equalsIgnoreCase("pug")){
					return "InternationalBankTransfer";
				}
				return "IBT-Equals";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		IBT_EQUALS_UAT {
			@Override
			public String getWebName() {
				return "Bank Wire Transfer (International)";
			}

			@Override
			public String getWithdrawName() {
				return "International Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "need change";
			}

			@Override
			public String getIBWithdrawName() {
				return "need change";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "need change";
			}

			@Override
			public String getCPTestId() {
				return "";
			}

			@Override
			public String getdepositDatetestid() {
				if(brand.equalsIgnoreCase("vfx")|| brand.equalsIgnoreCase("au")){
					return "IBT-Equals-VAUVFSC2_409";
				}
				if(brand.equalsIgnoreCase("mo")|| brand.equalsIgnoreCase("star")|| brand.equalsIgnoreCase("pug")){
					return "InternationalBankTransfer";
				}
				return "IBT-Equals";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},

		IBT_WHITESPAY {
			@Override
			public String getWebName() {
				return "Bank Wire Transfer (International)";
			}

			@Override
			public String getWithdrawName() {
				return "International Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "need change";
			}

			@Override
			public String getIBWithdrawName() {
				return "need change";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "need change";
			}

			@Override
			public String getCPTestId() {
				return "";
			}

			@Override
			public String getdepositDatetestid() {
				return "IBT-Whitespay";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		INDIAIBT {
			@Override
			public String getWebName() {
				if(brand.equalsIgnoreCase("star"))
				{
					return "Internet Banking (India)";
				}
				else if(brand.equalsIgnoreCase(BRAND.PUG.toString())) {
					return "Bank Wire Transfer (International)";
				}
				return "India Local Bank Wire Transfer";
			}

			@Override
			public String getWithdrawName() {
				return "need change";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "need change";
			}

			@Override
			public String getIBWithdrawName() {
				return "need change";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "need change";
			}

			@Override
			public String getCPTestId() {
				if(brand.equalsIgnoreCase(BRAND.PUG.toString())){
					return "i12BankTransferEqual";
				}
				return "indialocalbankwiretransfer";
			}

			@Override
			public String getdepositDatetestid() {
				return "indialocalbankwiretransfer";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		EUROSEPAIBT {
			@Override
			public String getWebName() {
				return "EURO SEPA";
			}

			@Override
			public String getWithdrawName() {
				return "need change";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "need change";
			}

			@Override
			public String getIBWithdrawName() {
				return "need change";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "need change";
			}

			@Override
			public String getCPTestId() {
				if(brand.equalsIgnoreCase(BRAND.PUG.toString())) {
					return "eurosepa";
				}
				return "euroSepa";
			}

			@Override
			public String getdepositDatetestid() {
				return "";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		EUROSEPAIBT_New {
			@Override
			public String getWebName() {
				return "EURO SEPA";
			}

			@Override
			public String getWithdrawName() {
				return "need change";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "need change";
			}

			@Override
			public String getIBWithdrawName() {
				return "need change";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "need change";
			}

			@Override
			public String getCPTestId() {
				if(brand.equalsIgnoreCase(BRAND.PUG.toString())) {
					return "eurosepa";
				}
				return "EUROSEPA";
			}

			@Override
			public String getdepositDatetestid() {
				return "EUROSEPA";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		EUBT {
			@Override
			public String getWebName() {
				return "Internet Banking (EU)";
			}

			@Override
			public String getWithdrawName() {
				return "need change";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "need change";
			}

			@Override
			public String getIBWithdrawName() {
				return "need change";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "need change";
			}

			@Override
			public String getCPTestId() {

				if(brand.equalsIgnoreCase("pug")) {
					return "euroinstantbanktransfer";
				}

				return "payablNetbanking";
			}
			@Override
			public String getdepositDatetestid() {
				return "";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		EUBT_NodaPay {
			@Override
			public String getWebName() {
				return "Internet Banking (EU)";
			}

			@Override
			public String getWithdrawName() {
				return "Euro Instant Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Internet Banking (EU)";
			}

			@Override
			public String getIBWithdrawName() {
				return "need change";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "need change";
			}

			@Override
			public String getCPTestId() {
				return "";
			}

			@Override
			public String getdepositDatetestid() {
				return "Noda";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		VIETNAMINSTANT {
			@Override
			public String getWebName() {
				return "Internet banking (Vietnam)";
			}

			@Override
			public String getWithdrawName() {
				if(brand.equalsIgnoreCase("vfx") || brand.equalsIgnoreCase("star") || brand.equalsIgnoreCase("mo"))
				{
					return "Vietnam Local Bank Transfer";
				}
				return "Vietnam Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Internet Banking (Vietnam)";
			}

			@Override
			public String getIBWithdrawName() {
				return "Internet Banking (Vietnam)";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "Vietnam Bank Transfer";
			}

			@Override
			public String getCPTestId() {
				if(brand.equalsIgnoreCase("star")) {
					return "vietnambanktransfer";
				}
				return "vietnaminstantbanktransfer";
			}

			@Override
			public String getdepositDatetestid() {
				return "";
			}

			@Override
			public String getWithdrawDataTestId() { return "VietnamBankTransfer"; }
		},
		VIETNAMINSTANT_New {
			@Override
			public String getWebName() {
				if (brand.equalsIgnoreCase("star")) {
					return "Vietnam Instant Bank Transfer (Primary)";
				}
				return "Internet banking (Vietnam)";
			}

			@Override
			public String getWithdrawName() {
				return "Vietnam Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Internet Banking (Vietnam)";
			}

			@Override
			public String getIBWithdrawName() {
				return "Internet Banking (Vietnam)";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {

				return "vietnamBankTransfer-8";
			}

			@Override
			public String getCPTestId() {
				if(brand.equalsIgnoreCase("star")) {
					return "vietnambanktransfer";
				}
				return "vietnaminstantbanktransfer";
			}

			@Override
			public String getdepositDatetestid() {
				if (brand.equalsIgnoreCase("pug")) {
					return "168Pay-VT_990";
				}
				if (brand.equalsIgnoreCase("star")) {
					return "EeziePay-VFX_77";
				}
				return "";
			}

			@Override
			public String getWithdrawDataTestId() { return "VietnamBankTransfer"; }
		},
		VIETNAM_XPay {
			@Override
			public String getWebName() {
				return "Internet banking (Vietnam)";
			}

			@Override
			public String getWithdrawName() {
				return "Vietnam Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Internet Banking (Vietnam)";
			}

			@Override
			public String getIBWithdrawName() {
				return "Internet Banking (Vietnam)";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "vietnamBankTransfer-8";
			}

			@Override
			public String getCPTestId() {
				return "vietnaminstantbanktransfer";
			}

			@Override
			public String getdepositDatetestid() {
				return "XPay";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		WalaoPay_VND {
			@Override
			public String getWebName() {
				return "Internet banking (Vietnam)";
			}

			@Override
			public String getWithdrawName() {
				return "Vietnam Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Internet Banking (Vietnam)";
			}

			@Override
			public String getIBWithdrawName() {
				return "Internet Banking (Vietnam)";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "vietnamBankTransfer-8";
			}

			@Override
			public String getCPTestId() {
				return "vietnaminstantbanktransfer";
			}

			@Override
			public String getdepositDatetestid() {
				return "WalaoPay";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		WowsPay_QR_VND {
			@Override
			public String getWebName() {
				if(brand.equalsIgnoreCase(BRAND.STAR.toString()))
				{
					return "QR（Vietnam）";
				}
				return "Internet banking (Vietnam)";
			}

			@Override
			public String getWithdrawName() {
				if(brand.equalsIgnoreCase("vfx") || brand.equalsIgnoreCase("star") || brand.equalsIgnoreCase("mo"))
				{
					return "Vietnam Local Bank Transfer";
				}
				return "Vietnam Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Internet Banking (Vietnam)";
			}

			@Override
			public String getIBWithdrawName() {
				return "Internet Banking (Vietnam)";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {

				return "vietnamBankTransfer-8";
			}

			@Override
			public String getCPTestId() {
				if(brand.equalsIgnoreCase("star")) {
					return "vietnambanktransfer";
				}
				return "vietnaminstantbanktransfer";
			}

			@Override
			public String getdepositDatetestid() {
				return "WowsPay_QR_";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		WowsPay_Remit_VND {
			@Override
			public String getWebName() {
				return "Internet banking (Vietnam)";
			}

			@Override
			public String getWithdrawName() {
				if(brand.equalsIgnoreCase("vfx") || brand.equalsIgnoreCase("star") || brand.equalsIgnoreCase("mo"))
				{
					return "Vietnam Local Bank Transfer";
				}
				return "Vietnam Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Internet Banking (Vietnam)";
			}

			@Override
			public String getIBWithdrawName() {
				return "Internet Banking (Vietnam)";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {

				return "vietnamBankTransfer-8";
			}

			@Override
			public String getCPTestId() {
				if(brand.equalsIgnoreCase("star")) {
					return "vietnambanktransfer";
				}
				else if(brand.equalsIgnoreCase("um")) {
					return "WowsPay_Remit_VND-VFX_76";
				}
				return "vietnaminstantbanktransfer";
			}

			@Override
			public String getdepositDatetestid() {
				return "WowsPay_Remit";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		VNDEeziePay {
			@Override
			public String getWebName() {
				return "Internet banking (Vietnam)";
			}

			@Override
			public String getWithdrawName() {
				if(brand.equalsIgnoreCase("vfx") || brand.equalsIgnoreCase("star") || brand.equalsIgnoreCase("mo"))
				{
					return "Vietnam Local Bank Transfer";
				}
				return "Vietnam Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Internet Banking (Vietnam)";
			}

			@Override
			public String getIBWithdrawName() {
				return "Internet Banking (Vietnam)";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {

				return "vietnamBankTransfer-8";
			}

			@Override
			public String getCPTestId() {
				if(brand.equalsIgnoreCase("star")) {
					return "vietnambanktransfer";
				}
				else if(brand.equalsIgnoreCase("um")) {
					return "EeziePay-UM_303";
				}
				return "vietnaminstantbanktransfer";
			}

			@Override
			public String getdepositDatetestid() {
				return "EeziePay";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		ZOTAPAY_VND {
			@Override
			public String getWebName() {
				return "Internet banking (Vietnam)";
			}

			@Override
			public String getWithdrawName() {
				if(brand.equalsIgnoreCase("vfx") || brand.equalsIgnoreCase("star") || brand.equalsIgnoreCase("mo"))
				{
					return "Vietnam Local Bank Transfer";
				}
				else if(brand.equalsIgnoreCase("um")) {
					return "Vietnam Instant Bank Transfer";
				}
				return "Vietnam Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Internet Banking (Vietnam)";
			}

			@Override
			public String getIBWithdrawName() {
				return "Internet Banking (Vietnam)";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {

				return "vietnamBankTransfer-8";
			}

			@Override
			public String getCPTestId() {
				if(brand.equalsIgnoreCase("star")) {
					return "vietnambanktransfer";
				}
				else if(brand.equalsIgnoreCase("um")) {
					return "ZotaPay-VFX_43";
				}
				return "vietnaminstantbanktransfer";
			}

			@Override
			public String getdepositDatetestid() {
				return "ZotaPay";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		ZOTAPAY_PHP_NEW {
			@Override
			public String getWebName() {
				return "Philippines Instant Bank Transfer (Primary)";
			}

			@Override
			public String getWithdrawName() {
				if(brand.equalsIgnoreCase("vfx") || brand.equalsIgnoreCase("star") || brand.equalsIgnoreCase("mo"))
				{
					return "Vietnam Local Bank Transfer";
				}
				else if(brand.equalsIgnoreCase("um")) {
					return "Vietnam Instant Bank Transfer";
				}
				return "Vietnam Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Internet Banking (Vietnam)";
			}

			@Override
			public String getIBWithdrawName() {
				return "Internet Banking (Vietnam)";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {

				return "vietnamBankTransfer-8";
			}

			@Override
			public String getCPTestId() {
				if(brand.equalsIgnoreCase("star")) {
					return "vietnambanktransfer";
				}
				else if(brand.equalsIgnoreCase("um")) {
					return "ZotaPay-VFX_44";
				}
				return "ZotaPay-VFX_44";
			}

			@Override
			public String getdepositDatetestid() {
				return "ZotaPay";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},

		ZOTAPAY_PHP {
			@Override
			public String getWebName() {
				if(brand.equalsIgnoreCase(BRAND.PUG.toString())){
					return "Internet banking (Philippine)";
				}
				return "Internet banking (Philippines)";
			}

			@Override
			public String getWithdrawName() {
				if(brand.equalsIgnoreCase("vfx") || brand.equalsIgnoreCase("star") || brand.equalsIgnoreCase("mo"))
				{
					return "Vietnam Local Bank Transfer";
				}
				return "Vietnam Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Internet Banking (Vietnam)";
			}

			@Override
			public String getIBWithdrawName() {
				return "Internet Banking (Vietnam)";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {

				return "vietnamBankTransfer-8";
			}

			@Override
			public String getCPTestId() {
				if(brand.equalsIgnoreCase("star")) {
					return "vietnambanktransfer";
				}
				return "vietnaminstantbanktransfer";
			}

			@Override
			public String getdepositDatetestid() {
				return "Zotapay";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		PaymentAsia_PHP {
			@Override
			public String getWebName() {
				if(brand.equalsIgnoreCase(BRAND.PUG.toString())){
					return "Internet banking (Philippine)";
				}
				return "Internet banking (Philippines)";
			}

			@Override
			public String getWithdrawName() {
				if(brand.equalsIgnoreCase("vfx") || brand.equalsIgnoreCase("star") || brand.equalsIgnoreCase("mo"))
				{
					return "Vietnam Local Bank Transfer";
				}
				return "Vietnam Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Internet Banking (Vietnam)";
			}

			@Override
			public String getIBWithdrawName() {
				return "Internet Banking (Vietnam)";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {

				return "vietnamBankTransfer-8";
			}

			@Override
			public String getCPTestId() {
				if(brand.equalsIgnoreCase("star")) {
					return "vietnambanktransfer";
				}
				return "vietnaminstantbanktransfer";
			}

			@Override
			public String getdepositDatetestid() {
				return "PaymentAsia";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		Toppay_PHP {
			@Override
			public String getWebName() {
				if(brand.equalsIgnoreCase(BRAND.PUG.toString())){
					return "Internet Banking (Philippine)";
				}
				return "Internet banking (Philippines)";
			}

			@Override
			public String getWithdrawName() {
				if(brand.equalsIgnoreCase("vfx") || brand.equalsIgnoreCase("star") || brand.equalsIgnoreCase("mo"))
				{
					return "Vietnam Local Bank Transfer";
				}
				return "Vietnam Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Internet Banking (Vietnam)";
			}

			@Override
			public String getIBWithdrawName() {
				return "Internet Banking (Vietnam)";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {

				return "vietnamBankTransfer-8";
			}

			@Override
			public String getCPTestId() {
				if(brand.equalsIgnoreCase("star")) {
					return "vietnambanktransfer";
				}
				return "vietnaminstantbanktransfer";
			}

			@Override
			public String getdepositDatetestid() {
				return "Toppay";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		ZOTAPAY_UGX {
			@Override
			public String getWebName() {
				return "Internet banking (Uganda)";
			}

			@Override
			public String getWithdrawName() {
				if(brand.equalsIgnoreCase("vfx") || brand.equalsIgnoreCase("star") || brand.equalsIgnoreCase("mo"))
				{
					return "Vietnam Local Bank Transfer";
				}
				return "Vietnam Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Internet Banking (Vietnam)";
			}

			@Override
			public String getIBWithdrawName() {
				return "Internet Banking (Vietnam)";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {

				return "vietnamBankTransfer-8";
			}

			@Override
			public String getCPTestId() {
				if(brand.equalsIgnoreCase("star")) {
					return "vietnambanktransfer";
				}
				return "vietnaminstantbanktransfer";
			}

			@Override
			public String getdepositDatetestid() {
				return "Zotapay";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		RwandaBT_ZOTAPAY_RWF {
			@Override
			public String getWebName() {
				return "Internet Banking (Rwanda)";
			}

			@Override
			public String getWithdrawName() {
				if(brand.equalsIgnoreCase("vfx") || brand.equalsIgnoreCase("star") || brand.equalsIgnoreCase("mo"))
				{
					return "Vietnam Local Bank Transfer";
				}
				return "Vietnam Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Internet Banking (Vietnam)";
			}

			@Override
			public String getIBWithdrawName() {
				return "Internet Banking (Vietnam)";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {

				return "vietnamBankTransfer-8";
			}
			@Override
			public String getCPTestId() {
				if(brand.equalsIgnoreCase("star")) {
					return "vietnambanktransfer";
				}
				return "vietnaminstantbanktransfer";
			}

			@Override
			public String getdepositDatetestid() {
				return "Zotapay";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		JAPANBT_TheJapanOne {
			@Override
			public String getWebName() {
				return "Internet Banking (Japan)";
			}

			@Override
			public String getWithdrawName() {
				return "Japan Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Internet Banking (Japan)";
			}

			@Override
			public String getIBWithdrawName() {
				return "Internet Banking (Japan)";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "japanBankTransfer-62";
			}

			@Override
			public String getCPTestId() {
				return "TheJapanOne-VFX_82";
			}

			@Override
			public String getdepositDatetestid() {
				return "TheJapanOne";
			}

			@Override
			public String getWithdrawDataTestId() { return "JapanBankTransfer"; }
		},
		JAPANBT_Payeasy {
			@Override
			public String getWebName() {
				return "Internet Banking (Japan)";
			}

			@Override
			public String getWithdrawName() {
				return "need change";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "need change";
			}

			@Override
			public String getIBWithdrawName() {
				return "need change";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "need change";
			}

			@Override
			public String getCPTestId() {
				return "VJPDirectBank-VFX-Pay-easy_66";
			}

			@Override
			public String getdepositDatetestid() {
				return "VJPDirectBank-VFX-Pay-easy";
			}

			@Override
			public String getWithdrawDataTestId() { return "VJPDirectBank-VFX-Pay-easy"; }
		},
		ZOTAPAY_JPY {
			@Override
			public String getWebName() {
				return "Internet banking (Japan)";
			}

			@Override
			public String getWithdrawName() {
				return "Japan Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Internet Banking (Japan)";
			}

			@Override
			public String getIBWithdrawName() {
				return "Internet Banking (Japan)";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "";
			}

			@Override
			public String getCPTestId() {
				return "";
			}

			@Override
			public String getdepositDatetestid() {
				return "ZotaPay";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		JAPANIAINSTANT {
			@Override
			public String getWebName() {
				return "Internet Banking (Japan)";
			}

			@Override
			public String getWithdrawName() {
				return "Japan Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Internet Banking (Japan)";
			}

			@Override
			public String getIBWithdrawName() {
				return "Internet Banking (Japan)";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "Japan Bank Transfer";
			}

			@Override
			public String getCPTestId() {
				if(brand.equalsIgnoreCase("star") || brand.equalsIgnoreCase("vjp") || brand.equalsIgnoreCase("vfx"))
				{
					return "japanbanktransfer";
				}
				return "japaninstantbanktransfer";
			}

			@Override
			public String getdepositDatetestid() {
				return "";
			}

			@Override
			public String getWithdrawDataTestId() { return "JapanBankTransfer"; }
		},
		JAPANEWALLET {
			@Override
			public String getWebName() {
				return "Japan E-wallet";
			}

			@Override
			public String getWithdrawName() {
				return "Japan E-Wallet";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "need return";
			}

			@Override
			public String getIBWithdrawName() {
				return "need return";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "need return";
			}

			@Override
			public String getCPTestId() {
				return "japanewallet";
			}

			@Override
			public String getdepositDatetestid() {
				return "";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		JAPANCREDITCARD {
			@Override
			public String getWebName() {
				return "Internet Banking (Japan)";
			}

			@Override
			public String getWithdrawName() {
				return "Japan Local Credit Card";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "need return";
			}

			@Override
			public String getIBWithdrawName() {
				return "need return";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "need return";
			}

			@Override
			public String getCPTestId() {
				return "japanlocalcreditcard";
			}

			@Override
			public String getdepositDatetestid() {
				return "";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		JAPANBTEMAIL {
			@Override
			public String getWebName() {
				return "Japan Bank Transfer (Email)";
			}

			@Override
			public String getWithdrawName() {
				return "Japan Bank Transfer (Email)";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Internet Banking (Japan - Email)";
			}

			@Override
			public String getIBWithdrawName() {
				return "Internet Banking (Japan - Email)";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "japanBankTransferEmail)-111";
			}

			@Override
			public String getCPTestId() {
				return "need return";
			}

			@Override
			public String getdepositDatetestid() {
				return "";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		//Original VJP use, but data-testid change, so using CreditOrDebit now, can remove later
		JCB {
			@Override
			public String getWebName() {
				return "Credit Card";
			}

			@Override
			public String getWithdrawName() {
				return "need change";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "need change";
			}

			@Override
			public String getIBWithdrawName() {
				return "need change";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "need change";
			}

			@Override
			public String getCPTestId() {
				return "jcb";
			}

			@Override
			public String getdepositDatetestid() {
				return "";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		JAPANEMONEY {
			@Override
			public String getWebName() {
				return "Internet Banking (Japan)";
			}

			@Override
			public String getWithdrawName() {
				return "need return";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "need return";
			}

			@Override
			public String getIBWithdrawName() {
				return "need return";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "need return";
			}

			@Override
			public String getCPTestId() {
				return "emoneydeposit";
			}

			@Override
			public String getdepositDatetestid() {
				return "";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		JAPANEMONEY_New {
			@Override
			public String getWebName() {
				return "Internet Banking (Japan)";
			}

			@Override
			public String getWithdrawName() {
				return "need return";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "need return";
			}

			@Override
			public String getIBWithdrawName() {
				return "need return";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "need return";
			}

			@Override
			public String getCPTestId() {
				return "VJPDirectBank-VFX-E-money_520";
			}

			@Override
			public String getdepositDatetestid() {
				return "VJPDirectBank-VFX-E-money_520";
			}

			@Override
			public String getWithdrawDataTestId() { return "VJPDirectBank-VFX-E-money"; }
		},
		JAPANKONBINI {
			@Override
			public String getWebName() {
				return "Internet Banking (Japan)";
			}

			@Override
			public String getWithdrawName() {
				return "need return";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "need return";
			}

			@Override
			public String getIBWithdrawName() {
				return "need return";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "need return";
			}

			@Override
			public String getCPTestId() {
				return "konbinideposit";
			}

			@Override
			public String getdepositDatetestid() {
				return "";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		JAPANKONBINI_New {
			@Override
			public String getWebName() {
				return "Internet Banking (Japan)";
			}

			@Override
			public String getWithdrawName() {
				return "need return";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "need return";
			}

			@Override
			public String getIBWithdrawName() {
				return "need return";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "need return";
			}

			@Override
			public String getCPTestId() {
				return "VJPDirectBank-VFX-Konbini_519";
			}

			@Override
			public String getdepositDatetestid() {
				return "VJPDirectBank-VFX-Konbini_519";
			}

			@Override
			public String getWithdrawDataTestId() { return "VJPDirectBank-VFX-Konbini"; }
		},
		GHANAINSTANT {
			@Override
			public String getWebName() {
				return "Internet Banking (Ghana)";
			}

			@Override
			public String getWithdrawName() {
				return "Ghana Local Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Internet Banking (Ghana)";
			}

			@Override
			public String getIBWithdrawName() {
				return "Internet Banking (Ghana)";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "Ghana Bank Transfer";
			}

			@Override
			public String getCPTestId() {
				return "ghanainstantbanktransfer";
			}

			@Override
			public String getdepositDatetestid() {
				return "";
			}

			@Override
			public String getWithdrawDataTestId() { return "GhanaBankTransfer"; }
		},
		GHANA_Bitolo_GHS {
			@Override
			public String getWebName() {
				return "Internet Banking (Ghana)";
			}

			@Override
			public String getWithdrawName() {
				return "Ghana Local Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Internet Banking (Ghana)";
			}

			@Override
			public String getIBWithdrawName() {
				return "Internet Banking (Ghana)";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				if(brand.equalsIgnoreCase("pug")){
					return "ghanaLocalBankTransfer-57";
				}
				return "ghanaBankTransfer-57";
			}

			@Override
			public String getCPTestId() {
				return "ghanainstantbanktransfer";
			}

			@Override
			public String getdepositDatetestid() {
				if(brand.equalsIgnoreCase(BRAND.VT.toString())){
					return "Bitolo-V3-MM";
				}
				return "Bitolo_GHS";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		GHANA_Korapay_GHS {
			@Override
			public String getWebName() {
				return "Internet Banking (Ghana)";
			}

			@Override
			public String getWithdrawName() {
				return "Ghana Local Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Internet Banking (Ghana)";
			}

			@Override
			public String getIBWithdrawName() {
				return "Internet Banking (Ghana)";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				if(brand.equalsIgnoreCase("pug")){
					return "ghanaLocalBankTransfer-57";
				}
				return "ghanaBankTransfer-57";
			}

			@Override
			public String getCPTestId() {
				return "ghanainstantbanktransfer";
			}

			@Override
			public String getdepositDatetestid() {
				if(brand.equalsIgnoreCase(BRAND.VT.toString())){
					return "Korapay-VT";
				}
				return "Korapay_GHS";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		GHANA_ZotaPay_GHS{
			@Override
			public String getWebName() {
				return "Internet Banking (Ghana)";
			}

			@Override
			public String getWithdrawName() {
				return "Ghana Local Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Internet Banking (Ghana)";
			}

			@Override
			public String getIBWithdrawName() {
				return "Internet Banking (Ghana)";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				if(brand.equalsIgnoreCase("pug")){
					return "ghanaLocalBankTransfer-57";
				}
				return "ghanaBankTransfer-57";
			}

			@Override
			public String getCPTestId() {
				return "ghanainstantbanktransfer";
			}

			@Override
			public String getdepositDatetestid() {
				if(brand.equalsIgnoreCase(BRAND.VT.toString())){
					return "ZotaPay-VFX";
				}
				return "ZotaPay_GHS";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		RWANDAINSTANT {
			@Override
			public String getWebName() {
				return "Internet Banking (Rwanda)";
			}

			@Override
			public String getWithdrawName() {
				return "Rwanda Local Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Internet Banking (Rwanda)";
			}

			@Override
			public String getIBWithdrawName() {
				return "Internet Banking (Rwanda)";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "rwandaLocalBankTransfer-51";
			}

			@Override
			public String getCPTestId() {
				return "rwandainstantbanktransfer";
			}

			@Override
			public String getdepositDatetestid() {
				return "";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		TANZANIAINSTANT {
			@Override
			public String getWebName() {
				return "Internet Banking (Tanzania)";
			}

			@Override
			public String getWithdrawName() {
				return "Tanzania Local Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Internet Banking (Tanzania)";
			}

			@Override
			public String getIBWithdrawName() {
				return "Internet Banking (Tanzania)";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "tanzaniaLocalBankTransfer-58";
			}

			@Override
			public String getCPTestId() {
				return "tanzaniainstantbanktransfer";
			}

			@Override
			public String getdepositDatetestid() {
				return "";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		ZOTAPAY_TZS {
			@Override
			public String getWebName() {
				return "Internet banking (Tanzania)";
			}

			@Override
			public String getWithdrawName() {
				return "Tanzania Local Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Internet banking (Tanzania)";
			}

			@Override
			public String getIBWithdrawName() {
				return "Internet banking (Tanzania)";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "tanzaniaLocalBankTransfer-58";
			}
			@Override
			public String getCPTestId() {
				return "tanzaniainstantbanktransfer";
			}

			@Override
			public String getdepositDatetestid() {
				return "ZotaPay";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		UGANDAINSTANT {
			@Override
			public String getWebName() {
				return "Internet Banking (Uganda)";
			}

			@Override
			public String getWithdrawName() {
				return "Uganda Local Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Internet Banking (Uganda)";
			}

			@Override
			public String getIBWithdrawName() {
				return "Internet Banking (Uganda)";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() { return "Uganda Bank Transfer"; }

			@Override
			public String getCPTestId() {
				return "ugandainstantbanktransfer";
			}

			@Override
			public String getdepositDatetestid() {
				return "";
			}

			@Override
			public String getWithdrawDataTestId() { return "UgandaBankTransfer"; }
		},
		CAMEROONINSTANT {
			@Override
			public String getWebName() {
				return "Internet Banking (Cameroon)";
			}

			@Override
			public String getWithdrawName() {
				return "Cameroon Local Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Internet Banking (Cameroon)";
			}

			@Override
			public String getIBWithdrawName() {
				return "Internet Banking (Cameroon)";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "cameroonLocalBankTransfer-54";
			}

			@Override
			public String getCPTestId() {
				return "camerooninstantbanktransfer";
			}

			@Override
			public String getdepositDatetestid() {
				return "";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		KENYAINSTANT {
			@Override
			public String getWebName() {
				return "Internet Banking (Kenya)";
			}

			@Override
			public String getWithdrawName() {
				if(brand.equalsIgnoreCase("pug")){
					return "Kenya Bank Transfer";
				}
				return "Kenya Local Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Internet Banking (Kenya)";
			}

			@Override
			public String getIBWithdrawName() {
				return "Internet Banking (Kenya)";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "Kenya Bank Transfer";
			}

			@Override
			public String getCPTestId() {
				if(brand.equalsIgnoreCase("pug")){
					return "mpesa";
				}
				return "kenyainstantbanktransfer";
			}

			@Override
			public String getdepositDatetestid() {
				return "";
			}

			@Override
			public String getWithdrawDataTestId() { return "KenyaBankTransfer"; }
		},
		KENYA_Bitolo_KES {
			@Override
			public String getWebName() {
				return "Internet Banking (Kenya)";
			}

			@Override
			public String getWithdrawName() {
				if(brand.equalsIgnoreCase("pug")){
					return "M-PESA";
				}
				return "Kenya Local Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Internet Banking (Kenya)";
			}

			@Override
			public String getIBWithdrawName() {
				return "Internet Banking (Kenya)";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				if(brand.equalsIgnoreCase("pug")){
					return "mPesa-56";
				}
				return "kenyaBankTransfer-56";
			}

			@Override
			public String getCPTestId() {
				if(brand.equalsIgnoreCase("pug")){
					return "mpesa";
				}
				return "kenyainstantbanktransfer";
			}

			@Override
			public String getdepositDatetestid() {
				if(brand.equalsIgnoreCase(BRAND.VT.toString())){
					return "Bitolo-V3-MM";
				}
				return "Bitolo_KES";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		KENYA_Korapay_KES {
			@Override
			public String getWebName() {
				return "Internet Banking (Kenya)";
			}

			@Override
			public String getWithdrawName() {
				if(brand.equalsIgnoreCase("pug")){
					return "M-PESA";
				}
				return "Kenya Local Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Internet Banking (Kenya)";
			}

			@Override
			public String getIBWithdrawName() {
				return "Internet Banking (Kenya)";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				if(brand.equalsIgnoreCase("pug")){
					return "mPesa-56";
				}
				return "kenyaBankTransfer-56";
			}

			@Override
			public String getCPTestId() {
				if(brand.equalsIgnoreCase("pug")){
					return "mpesa";
				}
				return "kenyainstantbanktransfer";
			}

			@Override
			public String getdepositDatetestid() {
				return "Korapay";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		KENYA_Zotapay_KES {
			@Override
			public String getWebName() {
				return "Internet Banking (Kenya)";
			}

			@Override
			public String getWithdrawName() {
				if(brand.equalsIgnoreCase("pug")){
					return "M-PESA";
				}
				return "Kenya Local Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Internet Banking (Kenya)";
			}

			@Override
			public String getIBWithdrawName() {
				return "Internet Banking (Kenya)";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				if(brand.equalsIgnoreCase("pug")){
					return "mPesa-56";
				}
				return "kenyaBankTransfer-56";
			}

			@Override
			public String getCPTestId() {
				if(brand.equalsIgnoreCase("pug")){
					return "mpesa";
				}
				return "kenyainstantbanktransfer";
			}

			@Override
			public String getdepositDatetestid() {
				return "Zotapay";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		KENYA_UBank_KES {
			@Override
			public String getWebName() {
				return "Internet Banking (Kenya)";
			}

			@Override
			public String getWithdrawName() {
				return "Internet Banking (Kenya)";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Internet Banking (Kenya)";
			}

			@Override
			public String getIBWithdrawName() {
				return "Internet Banking (Kenya)";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "Internet Banking (Kenya)";
			}

			@Override
			public String getCPTestId() {
				return "Internet Banking (Kenya)";
			}

			@Override
			public String getdepositDatetestid() {
				return "UBank";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		PHILIPPINESINSTANT {
			@Override
			public String getWebName() {
				if(brand.equalsIgnoreCase("pug")||brand.equalsIgnoreCase("vfx")||brand.equalsIgnoreCase("star")){
					return "Internet Banking (Philippine)";
				}
				return "Internet Banking (Philippines)";
			}

			@Override
			public String getWithdrawName() {
				if(brand.equalsIgnoreCase("vfx") || brand.equalsIgnoreCase("star") ||
						brand.equalsIgnoreCase("vt") || brand.equalsIgnoreCase(BRAND.PUG.toString()) ||
						brand.equalsIgnoreCase(BRAND.MO.toString())|| brand.equalsIgnoreCase(BRAND.UM.toString())){
					return "Philippine Bank Transfer";
				}
				return "Philippines Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				// CP Withdrawal History
				return "Internet Banking (Philippines)";
			}

			@Override
			public String getIBWithdrawName() {
				//IB Withdrawal history
				return "Internet Banking (Philippine)";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				//IB Withdrawal methods
				return "Philippine Bank Transfer";
			}

			@Override
			public String getCPTestId() {
				if(brand.equalsIgnoreCase("star")){
					return "philippinebanktransfer";
				}
				return "philippinesinstantbanktransfer";
			}

			@Override
			public String getdepositDatetestid() {
				return "";
			}

			@Override
			public String getWithdrawDataTestId() {
				return "PhilippineBankTransfer";
			}
		},
		MALAYINSTANT {
			@Override
			public String getWebName() {
				return "INTERNET BANKING (MALAYSIA)";
			}

			@Override
			public String getWithdrawName() {
				return "Malaysia Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Internet Banking (Malaysia)";
			}

			@Override
			public String getIBWithdrawName() {
				//IB Withdrawal history
				return "Internet Banking (Malaysia)";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				//IB Withdrawal methods
				return "Malaysia Bank Transfer";
			}

			@Override
			public String getCPTestId() {
				// TODO Auto-generated method stub
				if(brand.equalsIgnoreCase("star"))
				{
					return "malaysiabanktransfer";
				}
				return "malaysiainstantbanktransfer";
			}

			@Override
			public String getdepositDatetestid() {
				return "";
			}

			@Override
			public String getWithdrawDataTestId() { return "MalaysiaBankTransfer"; }
		},
		INDONESIAINSTANT {
			@Override
			public String getWebName() {
				return "Internet Banking (Indonesia)";
			}

			@Override
			public String getWithdrawName() {
				return "Indonesia Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Internet Banking (Indonesia)";
			}

			@Override
			public String getIBWithdrawName() {
				return "Internet Banking (Indonesia)";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "Indonesia Bank Transfer";
			}

			@Override
			public String getCPTestId() {
				if(brand.equalsIgnoreCase("star"))
				{
					return("indonesiabanktransfer");
				}
				else if(brand.equalsIgnoreCase("um"))
				{
					return("indonesialocalbanktransfer");
				}
				return "indonesiainstantbanktransfer";
			}

			@Override
			public String getdepositDatetestid() {
				return "";
			}

			@Override
			public String getWithdrawDataTestId() { return "IndonesiaBankTransfer"; }
		},
		INDONESIA_XPay_IDR {
			@Override
			public String getWebName() {
				return "Internet Banking (Indonesia)";
			}

			@Override
			public String getWithdrawName() {
				if(brand.equalsIgnoreCase("vt")){
					return "Indonesia Local Bank Transfer";
				}
				return "Indonesia Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Internet Banking (Indonesia)";
			}

			@Override
			public String getIBWithdrawName() {
				return "Internet Banking (Indonesia)";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {

				return "indonesiaBankTransfer-39";
			}

			@Override
			public String getCPTestId() {
				if(brand.equalsIgnoreCase("star"))
				{
					return("indonesiabanktransfer");
				}
				else if(brand.equalsIgnoreCase("um"))
				{
					return("XPay-UM_307");
				}
				return "indonesiainstantbanktransfer";
			}

			@Override
			public String getdepositDatetestid() {
				return "XPay";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		INDONESIA_WowsPay_IDR {
			@Override
			public String getWebName() {
				return "Internet Banking (Indonesia)";
			}

			@Override
			public String getWithdrawName() {
				if(brand.equalsIgnoreCase("vt")){
					return "Indonesia Local Bank Transfer";
				}
				return "Indonesia Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Internet Banking (Indonesia)";
			}

			@Override
			public String getIBWithdrawName() {
				return "Internet Banking (Indonesia)";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {

				return "indonesiaBankTransfer-39";
			}

			@Override
			public String getCPTestId() {
				if(brand.equalsIgnoreCase("star"))
				{
					return("indonesiabanktransfer");
				}
				else if(brand.equalsIgnoreCase("um"))
				{
					return("indonesialocalbanktransfer");
				}
				return "indonesiainstantbanktransfer";
			}

			@Override
			public String getdepositDatetestid() {
				return "WowsPay";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		INDONESIA_ZotaPay_IDR {
			@Override
			public String getWebName() {
				return "Internet Banking (Indonesia)";
			}

			@Override
			public String getWithdrawName() {
				if(brand.equalsIgnoreCase("vt")){
					return "Indonesia Local Bank Transfer";
				}
				return "Indonesia Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Internet Banking (Indonesia)";
			}

			@Override
			public String getIBWithdrawName() {
				return "Internet Banking (Indonesia)";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {

				return "indonesiaBankTransfer-39";
			}

			@Override
			public String getCPTestId() {
				if(brand.equalsIgnoreCase("star"))
				{
					return("indonesiabanktransfer");
				}
				else if(brand.equalsIgnoreCase("um"))
				{
					return("ZotaPay-VFX_64");
				}
				return "indonesiainstantbanktransfer";
			}

			@Override
			public String getdepositDatetestid() {
				return "ZotaPay";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		INDONESIA_PaymentAsia_IDR {
			@Override
			public String getWebName() {
				return "Internet Banking (Indonesia)";
			}

			@Override
			public String getWithdrawName() {
				if(brand.equalsIgnoreCase("vt")){
					return "Indonesia Local Bank Transfer";
				}
				return "Indonesia Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Internet Banking (Indonesia)";
			}

			@Override
			public String getIBWithdrawName() {
				return "Internet Banking (Indonesia)";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {

				return "indonesiaBankTransfer-39";
			}

			@Override
			public String getCPTestId() {
				if(brand.equalsIgnoreCase("star"))
				{
					return("indonesiabanktransfer");
				}
				else if(brand.equalsIgnoreCase("um"))
				{
					return("indonesialocalbanktransfer");
				}
				return "indonesiainstantbanktransfer";
			}

			@Override
			public String getdepositDatetestid() {
				return "PaymentAsia";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		KOREAINSTANT {
			@Override
			public String getWebName() {
				if(brand.equalsIgnoreCase("vt")) {
					return "Internet Banking (Korea)";
				}
				else if(brand.equalsIgnoreCase(BRAND.PUG.toString())) {
					return "common.withdrawChannel.internetbankingsouthkorea";
				}
				return "Internet Banking (South Korea)";
			}

			@Override
			public String getWithdrawName() {
				if(brand.equalsIgnoreCase("vt")){
					return "South Korea Bank Transfer";
				}
				else if(brand.equalsIgnoreCase(BRAND.VFX.toString()) || brand.equalsIgnoreCase(BRAND.MO.toString())
						|| brand.equalsIgnoreCase(BRAND.PUG.toString()))
				{
					return "South Korea Local Bank Transfer";
				}
				return "Korea Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				if(brand.equalsIgnoreCase("vt") || brand.equalsIgnoreCase(BRAND.VFX.toString())
						|| brand.equalsIgnoreCase(BRAND.MO.toString()) || brand.equalsIgnoreCase(BRAND.PUG.toString())){
					return "Internet Banking (South Korea)";
				}
				return "Internet Banking (Korea)";
			}

			@Override
			public String getIBWithdrawName() {
				return "Internet Banking (South Korea)";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "South Korea Local Bank Transfer";
			}

			@Override
			public String getCPTestId() {
				if(brand.equalsIgnoreCase("vt")){
					return "southkoreainstantbanktransfer";
				}
				return "southkoreabanktransfer";
			}

			@Override
			public String getdepositDatetestid() {
				return "";
			}

			@Override
			public String getWithdrawDataTestId() { return "KoreaBankTransfer"; }
		},
		PAYWON_KRW {
			@Override
			public String getWebName() {
				return "Internet Banking (Korea)";
			}

			@Override
			public String getWithdrawName() {
				if(brand.equalsIgnoreCase("vt")||brand.equalsIgnoreCase("mo")){
					return "South Korea Bank Transfer";
				}
				return "Korea Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				if(brand.equalsIgnoreCase("vt")){
					return "Internet Banking (South Korea)";
				}
				return "Internet Banking (Korea)";
			}

			@Override
			public String getIBWithdrawName() {
				if(brand.equalsIgnoreCase("vt")){
					return "Internet Banking (South Korea)";
				}
				return "Internet Banking (Korea)";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				if(brand.equalsIgnoreCase("mo")){
					return "southKoreaBankTransfer-35";
				}
				return "koreaBankTransfer-35";
			}

			@Override
			public String getCPTestId() {
				if(brand.equalsIgnoreCase("vt")){
					return "southkoreainstantbanktransfer";
				}
				return "southkoreabanktransfer";
			}

			@Override
			public String getdepositDatetestid() {
				return "Paywon";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		Vpay_KRW {
			@Override
			public String getWebName() {
				return "Internet Banking (Korea)";
			}

			@Override
			public String getWithdrawName() {
				if(brand.equalsIgnoreCase("vt")||brand.equalsIgnoreCase("mo")){
					return "South Korea Bank Transfer";
				}
				return "Korea Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				if(brand.equalsIgnoreCase("vt")){
					return "Internet Banking (South Korea)";
				}
				return "Internet Banking (Korea)";
			}

			@Override
			public String getIBWithdrawName() {
				if(brand.equalsIgnoreCase("vt")){
					return "Internet Banking (South Korea)";
				}
				return "Internet Banking (Korea)";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				if(brand.equalsIgnoreCase("mo")){
					return "southKoreaBankTransfer-35";
				}
				return "koreaBankTransfer-35";
			}

			@Override
			public String getCPTestId() {
				if(brand.equalsIgnoreCase("vt")){
					return "southkoreainstantbanktransfer";
				}
				return "southkoreabanktransfer";
			}

			@Override
			public String getdepositDatetestid() {
				return "Vpay";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		TAIWANINSTANT {
			@Override
			public String getWebName() {
				if(brand.equalsIgnoreCase("vfx")||brand.equalsIgnoreCase("star")){
					return "Internet Banking (Taiwan)";
				}
				return "Taiwan Local Bank Transfer";
			}

			@Override
			public String getWithdrawName() {
				return "Taiwan Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Internet Banking (Taiwan)";
			}

			@Override
			public String getIBWithdrawName() {
				return "Internet Banking (Taiwan)";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "taiwanBankTransfer-102";
			}

			@Override
			public String getCPTestId() {
				return "taiwanbanktransfer";
			}

			@Override
			public String getdepositDatetestid() {
				return "";
			}

			@Override
			public String getWithdrawDataTestId() { return "TaiwanBankTransfer"; }
		},
		TAIWANQR {
			@Override
			public String getWebName() {
				if(brand.equalsIgnoreCase("vfx") || brand.equalsIgnoreCase("pug")){
					return "Internet Banking (Taiwan)";
				}
				return "Taiwan Supermarket QR";
			}

			@Override
			public String getWithdrawName() {
				return "Taiwan Supermarket QR";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "need return";
			}

			@Override
			public String getIBWithdrawName() {
				return "need return";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "need return";
			}

			@Override
			public String getCPTestId() {
				return "taiwanconveniencestore";
			}

			@Override
			public String getdepositDatetestid() {
				return "";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		SOUTHAFRICAINSTANT{
			@Override
			public String getWebName() {
				return "Internet Banking (South Africa)";
			}

			@Override
			public String getWithdrawName() {
				return "South Africa Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Internet Banking (South Africa)";
			}

			@Override
			public String getIBWithdrawName() {
				return "Internet Banking (South Africa)";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() { return "South Africa Bank Transfer"; }

			@Override
			public String getCPTestId() {
				return "southafricainstantbanktransfer";
			}

			@Override
			public String getdepositDatetestid() {
				return "";
			}

			@Override
			public String getWithdrawDataTestId() { return "SouthAfricaBankTransfer"; }
		},
		ZOTAPAY_ZAR {
			@Override
			public String getWebName() {
				return "Internet Banking (South Africa)";
			}

			@Override
			public String getWithdrawName() {
				return "South Africa Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Internet Banking (South Africa)";
			}

			@Override
			public String getIBWithdrawName() {
				return "Internet Banking (South Africa)";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {

				return "southAfricaBankTransfer-42";
			}

			@Override
			public String getCPTestId() {
				return "southafricainstantbanktransfer";
			}

			@Override
			public String getdepositDatetestid() {
				return "ZotaPay";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		Ozow_ZAR{
			@Override
			public String getWebName() {
				if (brand.equalsIgnoreCase(BRAND.VT.toString()))
				{
					return "Euro Instant Bank Transfer";
				}
				return "Internet Banking (South Africa)";
			}

			@Override
			public String getWithdrawName() {
				return "South Africa Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Internet Banking (South Africa)";
			}

			@Override
			public String getIBWithdrawName() {
				return "Internet Banking (South Africa)";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {

				return "southAfricaBankTransfer-42";
			}

			@Override
			public String getCPTestId() {
				if (brand.equalsIgnoreCase("pug")) {
					return "primary";
				}
				return "southafricainstantbanktransfer";
			}

			@Override
			public String getdepositDatetestid() {
				return "ozow";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		IndiaUPI {
			@Override
			public String getWebName() {
				return "UPI";
			}

			@Override
			public String getWithdrawName() {
				return "UPI";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "need return";
			}

			@Override
			public String getIBWithdrawName() {
				return "need return";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "need return";
			}

			@Override
			public String getCPTestId() {
				if (brand.equalsIgnoreCase("pug")||brand.equalsIgnoreCase("star")) {
					return "indiaupi";
				}
				return "upi";
			}

			@Override
			public String getdepositDatetestid() {
				return "";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		CheesePay {
			@Override
			public String getWebName() {
				return "UPI";
			}

			@Override
			public String getWithdrawName() {
				return "UPI";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "need return";
			}

			@Override
			public String getIBWithdrawName() {
				return "need return";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "need return";
			}

			@Override
			public String getCPTestId() {
				if (brand.equalsIgnoreCase("pug")||brand.equalsIgnoreCase("star")) {
					return "indiaupi";
				}
				else if (brand.equalsIgnoreCase("um")) {
					return "CheezeePay-VFX_54";
				}
				return "upi";
			}

			@Override
			public String getdepositDatetestid() {
				return "CheezeePay";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		PaymentOption_UPI {
			@Override
			public String getWebName() {
				return "UPI";
			}

			@Override
			public String getWithdrawName() {
				return "UPI";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "need return";
			}

			@Override
			public String getIBWithdrawName() {
				return "need return";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "need return";
			}

			@Override
			public String getCPTestId() {
				if (brand.equalsIgnoreCase("pug")||brand.equalsIgnoreCase("star")) {
					return "indiaupi";
				}
				return "upi";
			}

			@Override
			public String getdepositDatetestid() {
				return "PaymentOption";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		Cricpayz {
			@Override
			public String getWebName() {
				return "UPI";
			}

			@Override
			public String getWithdrawName() {
				return "UPI";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "need return";
			}

			@Override
			public String getIBWithdrawName() {
				return "need return";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "need return";
			}

			@Override
			public String getCPTestId() {
				if (brand.equalsIgnoreCase("pug")||brand.equalsIgnoreCase("star")) {
					return "indiaupi";
				}
				if (brand.equalsIgnoreCase("um")) {
					return "Cricpayz-QR-VFX_83";
				}
				return "upi";
			}

			@Override
			public String getdepositDatetestid() {
				return "Cricpayz";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		Xpay {
			@Override
			public String getWebName() {
				return "UPI";
			}

			@Override
			public String getWithdrawName() {
				return "UPI";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "need return";
			}

			@Override
			public String getIBWithdrawName() {
				return "need return";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "need return";
			}

			@Override
			public String getCPTestId() {
				if (brand.equalsIgnoreCase("pug")||brand.equalsIgnoreCase("star")) {
					return "indiaupi";
				}
				if (brand.equalsIgnoreCase("um")) {
					return "Xpay-VFX1_84";
				}
				return "upi";
			}

			@Override
			public String getdepositDatetestid() {
				return "Xpay";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		INDIAIAINSTANT {
			@Override
			public String getWebName() {
				if(brand.equalsIgnoreCase("pug")){
					return "India Bank Transfer";
				}
				return "Internet Banking (India)";
			}

			@Override
			public String getWithdrawName() {
				return "India Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Internet Banking (India)";
			}

			@Override
			public String getIBWithdrawName() {
				return "Internet Banking (India)";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "India Bank Transfer";
			}

			@Override
			public String getCPTestId() {
				if(brand.equalsIgnoreCase("pug")){
					return "indiabanktransfer";
				}
				return "indiainstantbanktransfer";
			}

			@Override
			public String getdepositDatetestid() {
				return "";
			}

			@Override
			public String getWithdrawDataTestId() { return "IndiaBankTransfer"; }
		},
		VOLET {
			@Override
			public String getWebName() {
				return "Volet";
			}

			@Override
			public String getWithdrawName() {
				return "Volet";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Volet";
			}

			@Override
			public String getIBWithdrawName() {
				return "need return";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "need return";
			}

			@Override
			public String getCPTestId() {
				return "upi";
			}

			@Override
			public String getdepositDatetestid() {
				return "Volet";
			}

			@Override
			public String getWithdrawDataTestId() { return "Volet"; }
		},
		IndiaP2P {
			@Override
			public String getWebName() {
				return "India Local Bank Wire Transfer"; //Star
			}

			@Override
			public String getWithdrawName() {
				return "India P2P Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "need return";
			}

			@Override
			public String getIBWithdrawName() {
				return "need return";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "need return";
			}

			@Override
			public String getCPTestId() {
				return "indiaP2P";
			}

			@Override
			public String getdepositDatetestid() {
				return "";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		IndiaP2P_New {
			@Override
			public String getWebName() {
				return "India Bank Transfer";
			}

			@Override
			public String getWithdrawName() {
				return "need return";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "need return";
			}

			@Override
			public String getIBWithdrawName() {
				return "need return";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "need return";
			}

			@Override
			public String getCPTestId() {
				return "indiaP2P";
			}

			@Override
			public String getdepositDatetestid() {
				return "BankTransfer_P2P";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		SKRILL {
			@Override
			public String getWebName() {
				return "Skrill";
			}

			@Override
			public String getWithdrawName() {
				return "Skrill";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Skrill";
			}

			@Override
			public String getIBWithdrawName() {
				return "Skrill";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "Skrill";
			}

			@Override
			public String getCPTestId() {
				return "skrill";
			}

			@Override
			public String getdepositDatetestid() {
				return "skrill";
			}

			@Override
			public String getWithdrawDataTestId() { return "Skrill"; }
		},
		STICPAY {
			@Override
			public String getWebName() {
				return "Sticpay";
			}

			@Override
			public String getWithdrawName() {
				if (brand.equalsIgnoreCase("pug")||brand.equalsIgnoreCase("vfx")||brand.equalsIgnoreCase("star")) {
					return "SticPay";
				}
				return "Sticpay";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Sticpay";
			}

			@Override
			public String getIBWithdrawName() {
				return "Sticpay";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "Sticpay";
			}

			@Override
			public String getCPTestId() {
				return "sticpay";
			}

			@Override
			public String getdepositDatetestid() {
				return "SticPay";
			}

			@Override
			public String getWithdrawDataTestId() { return "SticPay"; }
		},
		SticPayNew {
			@Override
			public String getWebName() {
				return "Sticpay";
			}

			@Override
			public String getWithdrawName() {
				if (brand.equalsIgnoreCase("pug")||brand.equalsIgnoreCase("vfx")||brand.equalsIgnoreCase("star")) {
					return "SticPay";
				}
				return "Sticpay";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Sticpay";
			}

			@Override
			public String getIBWithdrawName() {
				return "Sticpay";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "sticpay-38";
			}

			@Override
			public String getCPTestId() {
				return "sticpay";
			}
			@Override
			public String getdepositDatetestid() {
				return "SticPay";
			}

			@Override
			public String getWithdrawDataTestId() { return "SticPay"; }
		},
		NETELLER {
			@Override
			public String getWebName() {
				return "NETELLER";
			}

			@Override
			public String getWithdrawName() {
				return "Neteller";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Neteller";
			}

			@Override
			public String getIBWithdrawName() {
				return "Neteller";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "Neteller";
			}

			@Override
			public String getCPTestId() {
				return "neteller";
			}

			@Override
			public String getdepositDatetestid() {
				return "Neteller";
			}

			@Override
			public String getWithdrawDataTestId() { return "Neteller"; }
		},
		BINANCE {
			@Override
			public String getWebName() {
				return "Binance Pay";
			}

			@Override
			public String getWithdrawName() { return "Binance Pay"; }

			@Override
			public String getWithdrawHistoryName() {
				return "Binance Pay";
			}

			@Override
			public String getIBWithdrawName() {
				return "Binance Pay";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "BinancePay";
			}

			@Override
			public String getCPTestId() {
				return "BinancePay";
			}

			@Override
			public String getdepositDatetestid() {
				return "BinancePay";
			}

			@Override
			public String getWithdrawDataTestId() { return "BinancePay"; }
		},

		AUBANKTRANSFER {
			@Override
			public String getWebName() {
				return "BANK TRANSFER (INTERNATIONAL)";
			}

			@Override
			public String getWithdrawName() {
				return "International Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "BANK TRANSFER (AUSTRALIA)";
			}

			@Override
			public String getIBWithdrawName() {
				return "Bank Transfer - Australia";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "International Bank Transfer";
			}

			@Override
			public String getCPTestId() {
				return "AUBANKTRANSFER";
			}

			@Override
			public String getdepositDatetestid() {
				return "AustraliaBankTransfer";
			}

			@Override
			public String getWithdrawDataTestId() { return "Internationalbanktransfer_"; }
		},
		AUBANKTRANSFER_New {
			@Override
			public String getWebName() {
				return "Bank Wire Transfer (International)";
			}

			@Override
			public String getWithdrawName() {
				return "International Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "International Bank Transfer";
			}

			@Override
			public String getIBWithdrawName() {
				return "Bank Transfer - Australia";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				if (brand.equalsIgnoreCase("pug")) {
					return "eWalletWithdrawal-2";
				}
				return "banktransfer-2";
			}

			@Override
			public String getCPTestId() {
				return "AUBANKTRANSFER";
			}

			@Override
			public String getdepositDatetestid() {
				return "AustraliaBankTransfer";
			}

			@Override
			public String getWithdrawDataTestId() { return "Internationalbanktransfer_"; }
		},
		B2B {
			@Override
			public String getWebName() {
				return "BROKER TO BROKER TRANSFER";
			}

			@Override
			public String getWithdrawName() {
				return "broker to broker";
			}

			@Override
			public String getWithdrawHistoryName() {
				return null;
			}

			@Override
			public String getIBWithdrawName() {
				return "need change";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {

				return "need change";
			}

			@Override
			public String getCPTestId() {
				return "b2b";
			}

			@Override
			public String getdepositDatetestid() {
				return "BrokertoBrokerTransfer";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		B2B_New {
			@Override
			public String getWebName() {
				return "BROKER TO BROKER";
			}

			@Override
			public String getWithdrawName() {
				return "broker to broker";
			}

			@Override
			public String getWithdrawHistoryName() {
				return null;
			}

			@Override
			public String getIBWithdrawName() {
				return "need change";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "need change";
			}

			@Override
			public String getCPTestId() {
				return "b2b";
			}

			@Override
			public String getdepositDatetestid() {
				return "BrokertoBrokerTransfer";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		FASAPAY {
			@Override
			public String getWebName() {
				return "FASAPAY";
			}

			@Override
			public String getWithdrawName() {
				return "FasaPay";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "FasaPay";
			}

			@Override
			public String getIBWithdrawName() {
				return "FasaPay";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "fasapay-7";
			}

			@Override
			public String getCPTestId() {
				return "fasapay";
			}

			@Override
			public String getdepositDatetestid() {
				return "";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		BITWALLET {
			@Override
			public String getWebName() {
				return "Bitwallet";
			}

			@Override
			public String getWithdrawName() {
				return "Bitwallet";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Bitwallet";
			}

			@Override
			public String getIBWithdrawName() {
				return "Bitwallet";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "Bitwallet";
			}

			@Override
			public String getCPTestId() {
				if(brand.equalsIgnoreCase("vjp"))
				{
					return "bitwallet-VFX_3";
				}

				return "bitwallet";
			}

			@Override
			public String getdepositDatetestid() {
				return "Bitwallet";
			}

			@Override
			public String getWithdrawDataTestId() { return "Bitwallet"; }
		},
		CRYPTO {
			@Override
			public String getWebName() {
				return "Cryptocurrency deposit";
			}

			@Override
			public String getWithdrawName() {
				return "CRYPTOCURRENCY to";
			}

			@Override
			public String getWithdrawHistoryName() {
				return null;
			}

			@Override
			public String getIBWithdrawName() {
				return "need change";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "need change";
			}

			@Override
			public String getCPTestId() {
				return "cryptocurrencydeposit";
			}

			@Override
			public String getdepositDatetestid() {
				return "";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		CRYPTOBIT {
			@Override
			public String getWebName() {
				if(brand.equalsIgnoreCase("mo"))
				{
					return "Crypto-Bitcoin";
				}
				return "Cryptocurrency-BTC";
			}

			@Override
			public String getWithdrawName() {
				return "Cryptocurrency-Bitcoin";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Cryptocurrency-Bitcoin";
			}

			@Override
			public String getIBWithdrawName() {
				return "Cryptocurrency-Bitcoin";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "cryptocurrencybitcoin-34";
			}

			@Override
			public String getCPTestId() {
				if(brand.equalsIgnoreCase("pug"))
				{
					return "bitcoin";
				}
				else if(brand.equalsIgnoreCase("vjp"))
				{
					return "CoboPay.USDTBTC_4";
				}
				return "BITCOIN";
			}

			@Override
			public String getdepositDatetestid() {
				return "USDTBTC";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		CRYPTOBTC {
			@Override
			public String getWebName() {
				return "Cryptocurrency-BTC";
			}

			@Override
			public String getWithdrawName() {
				if(brand.equalsIgnoreCase(BRAND.MO.toString()) || brand.equalsIgnoreCase(BRAND.VT.toString())
					|| brand.equalsIgnoreCase(BRAND.STAR.toString()) || brand.equalsIgnoreCase(BRAND.UM.toString())
					|| brand.equalsIgnoreCase(BRAND.PUG.toString()))
				{
					return "Bitcoin";
				}
				return "BTC";
			}

			@Override
			public String getWithdrawHistoryName() {
				if (brand.equalsIgnoreCase("vfx")){
					return "Cryptocurrency - Bitcoin";
				} else if (brand.equalsIgnoreCase("star") | brand.equalsIgnoreCase("um")
						| brand.equalsIgnoreCase("mo") | brand.equalsIgnoreCase("pug")) {
					// payment pm all no reply de =(
					return "Cryptocurrency - Bitcon";
				}
				return "Cryptocurrency-Bitcoin";
			}

			@Override
			public String getIBWithdrawName() {
				return "Cryptocurrency - Bitcoin";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "Bitcoin";
			}

			@Override
			public String getCPTestId() {
				if(brand.equalsIgnoreCase("pug"))
				{
					return "bitcoin";
				}
				else if(brand.equalsIgnoreCase("vjp"))
				{
					return "CoboPay.USDTBTC_4";
				}
				return "BITCOIN";
			}

			@Override
			public String getdepositDatetestid() {
				return "USDTBTC";
			}

			@Override
			public String getWithdrawDataTestId() { return "BTC"; }
		},
        CRYPTOBTCNEW {
			@Override
			public String getWebName() {
				return "Cryptocurrency-BTC";
			}

			@Override
			public String getWithdrawName() {
				return "Bitcoin";
			}

			@Override
			public String getWithdrawHistoryName() {
				if (brand.equalsIgnoreCase("vfx")){
					return "Cryptocurrency - Bitcoin";
				} else if (brand.equalsIgnoreCase("star") | brand.equalsIgnoreCase("um")
						| brand.equalsIgnoreCase("mo") | brand.equalsIgnoreCase("pug")) {
					// payment pm all no reply de =(
					return "Cryptocurrency - Bitcon";
				}
				return "Cryptocurrency-Bitcoin";
			}

			@Override
			public String getIBWithdrawName() {
				return "Cryptocurrency - Bitcoin";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "Bitcoin";
			}

			@Override
			public String getCPTestId() {
				if(brand.equalsIgnoreCase("pug"))
				{
					return "bitcoin";
				}

				return "BITCOIN";
			}

			@Override
			public String getdepositDatetestid() {
				return "USDTBTC";
			}

			@Override
			public String getWithdrawDataTestId() { return "BTC"; }
		},
        CRYPTOUSDCSQL{
            @Override
            public String getWebName() {
                return "USDC-Solana";
            }

            @Override
            public String getWithdrawName() {
                return "USDC-Solana";
            }

            @Override
            public String getWithdrawHistoryName() {

                return "Cryptocurrency-USDC-SOL";
            }

            @Override
            public String getIBWithdrawName() {
                return "USDC-Solana";
            }

            @Override
            public String getIBWithdrawTypeDataTestId() {
                return "USDC-Solana";
            }

            @Override
            public String getCPTestId() {

                 return "USDC-Solana";
            }

            @Override
            public String getdepositDatetestid() {
                return "USDC-Solana";
            }

            @Override
            public String getWithdrawDataTestId() { return "USDC-Solana"; }
        },
		CRYPTOOMNI {
			@Override
			public String getWebName() {
				if(!brand.equalsIgnoreCase("vfx") || brand.equalsIgnoreCase("um")) {
					return "Crypto-Tether(OMNI)";
				}
				return "CRYPTO-USDT(OMNI)";
			}

			@Override
			public String getWithdrawName() {
				return "Cryptocurrency-Omni";
			}

			@Override
			public String getWithdrawHistoryName() {
				return null;
			}

			@Override
			public String getIBWithdrawName() {
				return "need change";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "need change";
			}

			@Override
			public String getCPTestId() {
				return "TETHER(OMNI)";
			}

			@Override
			public String getdepositDatetestid() {
				return "";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		CRYPTOERC {
			@Override
			public String getWebName() {
				if (brand.equalsIgnoreCase("mo")) {
					return "Crypto-USDT(ERC20)";
				}else if (brand.equalsIgnoreCase("vfx") || brand.equalsIgnoreCase("um")) {
					return "Cryptocurrency (USDT-ERC20)";
				}
				else
					return "Cryptocurrency-USDT(ERC-20)";
			}

			@Override
			public String getWithdrawName() {
				return "Cryptocurrency-USDT";
			}

			@Override
			public String getWithdrawHistoryName() {
				if(brand.equalsIgnoreCase("vfx")){
					return "USDT-ERC20";
				}
				else
					return "Cryptocurrency-USDT";
			}

			@Override
			public String getIBWithdrawName() {
				return "Cryptocurrency-USDT";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "cryptocurrencyusdt-36";
			}

			@Override
			public String getCPTestId() {
				if (brand.equalsIgnoreCase("pug"))
				{
					return "usdterc20";
				}
				return "USDT-ERC20";
			}

			@Override
			public String getdepositDatetestid() {
				return "USDTERC";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		CRYPTOERCNew {
			@Override
			public String getWebName() {
				if(brand.equalsIgnoreCase(BRAND.PUG.toString())) {
					return "Cryptocurrency-USDT(ERC-20)";
				}
				return "Cryptocurrency (USDT-ERC20)";
			}

			@Override
			public String getWithdrawName() {
				return "USDT-ERC";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "USDT-ERC20";
			}

			@Override
			public String getIBWithdrawName() {
				return "USDT-ERC20";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "USDT-ERC20";
			}

			@Override
			public String getCPTestId() {
				if (brand.equalsIgnoreCase("pug"))
				{
					return "usdterc20";
				}
				if (brand.equalsIgnoreCase("um"))
				{
					return "CoboPay.USDTTRC_10";
				}
				return "USDT-ERC20";
			}

			@Override
			public String getdepositDatetestid() {
				return "USDTERC";
			}

			@Override
			public String getWithdrawDataTestId() { return "USDT-ERC"; }
		},
		CRYPTOTRCNew {
			@Override
			public String getWebName() {
				return "Cryptocurrency (USDT-TRC20)";
			}

			@Override
			public String getWithdrawName() {
				return "USDT-TRC";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "USDT-TRC20";
			}

			@Override
			public String getIBWithdrawName() {
				return "USDT-TRC20";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {	return "USDT-TRC20";	}

			@Override
			public String getCPTestId() {
				if (brand.equalsIgnoreCase("pug"))
				{
					return "usdttrc20";
				}
				if (brand.equalsIgnoreCase("um") || brand.equalsIgnoreCase(BRAND.VJP.toString()))
				{
					return "CoboPay.USDTTRC_10";
				}
				return "USDT-TRC20";
			}

			@Override
			public String getdepositDatetestid() {
				return "USDTTRC";
			}

			@Override
			public String getWithdrawDataTestId() { return "USDT-TRC"; }
		},
		CRYPTOTRC {
			@Override
			public String getWebName() {
				if (brand.equalsIgnoreCase("mo")) {
					return "Crypto-USDT(TRC20)";
				}else if (brand.equalsIgnoreCase("vfx")||brand.equalsIgnoreCase("um") || brand.equalsIgnoreCase(BRAND.VJP.toString().toLowerCase())) {
					return "Cryptocurrency (USDT-TRC20)";
				}
				return "Cryptocurrency-USDT(TRC-20)";

			}

			@Override
			public String getWithdrawName() {
				return "Cryptocurrency-USDT";

			}

			@Override
			public String getWithdrawHistoryName() {
				return "Cryptocurrency-USDT";
			}

			@Override
			public String getIBWithdrawName() {
				return "Cryptocurrency-USDT";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				if (brand.equalsIgnoreCase("pug")) {
					return "eWalletWithdrawal-36";
				}
				return "withdrawalType36";
			}

			@Override
			public String getCPTestId() {
				if (brand.equalsIgnoreCase("pug"))
				{
					return "usdttrc20";
				}
				return "USDT-TRC20";
			}

			@Override
			public String getdepositDatetestid() {
				return "USDTTRC";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		CRYPTOBEP {
			@Override
			public String getWebName() {
				if(brand.equalsIgnoreCase("um")){
					return "Crypto-Tether(BEP20)";
				} else if (brand.equalsIgnoreCase("mo")) {
					return "Crypto-USDT(BEP20)";
				} else if (brand.equalsIgnoreCase(BRAND.STAR.toString()))
				{
					return "Cryptocurrency (USDT-BEP20)";
				}
				return "Cryptocurrency-USDT(BEP20)";

			}

			@Override
			public String getWithdrawName() {
				return "Cryptocurrency-BEP20";

			}

			@Override
			public String getWithdrawHistoryName() {
				return "need change";
			}

			@Override
			public String getIBWithdrawName() {
				return "need change";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				if (brand.equalsIgnoreCase("pug")) {
					return "eWalletWithdrawal-36";
				}
				return "withdrawalType36";
			}

			@Override
			public String getCPTestId() {
				if(brand.equalsIgnoreCase("um")) {
					return "TETHER(TRC20)";
				}
				return "USDT-BEP20";
			}

			@Override
			public String getdepositDatetestid() {
				return "";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		CRYPTOBEPNew {
			@Override
			public String getWebName() {
				if(brand.equalsIgnoreCase(BRAND.PUG.toString())) {
					return "Cryptocurrency-USDT(BEP20)";
				}
				return "Cryptocurrency (USDT-BEP20)";
			}

			@Override
			public String getWithdrawName() {
				if(brand.equalsIgnoreCase(BRAND.VT.toString())) {
					return "USDT-BEP20";
				}
				return "BEP20";
			}

			@Override
			public String getWithdrawHistoryName() {
				if(brand.equalsIgnoreCase(BRAND.VT.toString())) {
					return "Cryptocurrency-USDT(BEP20)";
				}
				return "USDT-BEP20";
			}

			@Override
			public String getIBWithdrawName() {
				return "USDT-BEP20";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() { return "USDT-BEP20"; }

			@Override
			public String getCPTestId() {
				if(brand.equalsIgnoreCase("um")) {
					return "CoboPay.USDTBEP20_95";
				}
				return "USDT-BEP20";
			}

			@Override
			public String getdepositDatetestid() {
				return "USDTBEP";
			}

			@Override
			public String getWithdrawDataTestId() { return "USDT-BEP20"; }
		},
		ETH {
			@Override
			public String getWebName() {
				return "Cryptocurrency-ETH";
			}

			@Override
			public String getWithdrawName() {
				return "Cryptocurrency-ETH";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Cryptocurrency-ETH";
			}

			@Override
			public String getIBWithdrawName() {
				return "Cryptocurrency-ETH";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "cryptocurrencyethcps-80";
			}

			@Override
			public String getCPTestId() {
				if(brand.equalsIgnoreCase("pug")) {
					return "eth";
				}
				else if(brand.equalsIgnoreCase(BRAND.VJP.toString())) {
					return "CoboPay.USDTETH_6";
				}
				return "ETH";
			}

			@Override
			public String getdepositDatetestid() {
				if(brand.equalsIgnoreCase(BRAND.VJP.toString())) {
					return "eth";
				}

				return "USDTETH";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		ETH_New {
			@Override
			public String getWebName() {
				return "Cryptocurrency-ETH";
			}

			@Override
			public String getWithdrawName() {
				return "ETH";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Cryptocurrency-ETH";
			}

			@Override
			public String getIBWithdrawName() {
				return "Cryptocurrency - ETH";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "ETH";
			}

			@Override
			public String getCPTestId() {
				if(brand.equalsIgnoreCase("pug")) {
					return "eth";
				}
				return "ETH";
			}

			@Override
			public String getdepositDatetestid() {
				return "USDTETH";
			}

			@Override
			public String getWithdrawDataTestId() { return "Cryptocurrency-ETH"; }
		},
		USDC {
			@Override
			public String getWebName() {
				if(brand.equalsIgnoreCase("mo")) {
					return "Cryptocurrency (USDC-ERC20)";
				}
				return "Cryptocurrency-USDC";
			}

			@Override
			public String getWithdrawName() {
				return "Cryptocurrency-USDC";
			}

			@Override
			public String getWithdrawHistoryName() {
				if(brand.equalsIgnoreCase("mo") || brand.equalsIgnoreCase("pug")) {
					return "Cryptocurrency-USDC(ERC-20)";
				}
				return "Cryptocurrency-USDC";
			}

			@Override
			public String getIBWithdrawName() {
				if(brand.equalsIgnoreCase("pug")) {
					return "Cryptocurrency-USDC(ERC-20)";
				}
				return "Cryptocurrency-USDC";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "cryptocurrencyusdccps-81";
			}

			@Override
			public String getCPTestId() {
				if (brand.equalsIgnoreCase("pug"))
				{
					return "usdcerc20";
				}
				return "USDC-ERC20";
			}

			@Override
			public String getdepositDatetestid() {
				return "USDCERC";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		USDCNew {
			@Override
			public String getWebName() {
				if(brand.equalsIgnoreCase(BRAND.PUG.toString())) {
					return "Cryptocurrency-USDC";
				}
				return "Cryptocurrency (USDC-ERC20)";
			}

			@Override
			public String getWithdrawName() {
				return "USDC-ERC";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Cryptocurrency-USDC";
			}

			@Override
			public String getIBWithdrawName() {
				if(brand.equalsIgnoreCase("pug")) {
					return "Cryptocurrency-USDC(ERC-20)";
				}
				return "Cryptocurrency-USDC";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "cryptocurrencyusdccps-81";
			}

			@Override
			public String getCPTestId() {
				if (brand.equalsIgnoreCase("pug"))
				{
					return "usdcerc20";
				}
				else if (brand.equalsIgnoreCase("um") || brand.equalsIgnoreCase(BRAND.VJP.toString()))
				{
					return "CoboPay.USDCERC_8";
				}
				return "USDC-ERC20";
			}

			@Override
			public String getdepositDatetestid() {
				return "USDCERC";
			}

			@Override
			public String getWithdrawDataTestId() {
				if (brand.equalsIgnoreCase("vfx"))
				{
					return "Cryptocurrency-USDC";
				}
				return "USDCERC";
			}
		},
		CRYPTOXRP {
			@Override
			public String getWebName() {
				return "Cryptocurrency-USDT(XRP)";
			}

			@Override
			public String getWithdrawName() {
				return "need update";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "need update";
			}

			@Override
			public String getIBWithdrawName() {
				return "need update";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "need update";
			}

			@Override
			public String getCPTestId() {
				return "XRP";
			}

			@Override
			public String getdepositDatetestid() {
				return "";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		ASTROPAY {
			@Override
			public String getWebName() {
				return "Astropay";
			}

			@Override
			public String getWithdrawName() {
				return "Astropay";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Astropay";
			}

			@Override
			public String getIBWithdrawName() {
				return "Astropay";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {

				return "astropay-41";
			}

			@Override
			public String getCPTestId() {
				return "ASTROPAY";
			}

			@Override
			public String getdepositDatetestid() {
				return "";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		VLOAD {
			@Override
			public String getWebName() {
				return "Need to change";
			}

			@Override
			public String getWithdrawName() {
				return "vload";
			}

			@Override
			public String getWithdrawHistoryName() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String getIBWithdrawName() {
				// TODO Auto-generated method stub
				return "need change";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {

				return "need change";
			}

			@Override
			public String getCPTestId() {
				// TODO Auto-generated method stub
				return "VLOAD";
			}
			@Override
			public String getdepositDatetestid() {
				return "";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		CHINAUNION{
			public String getIBWithdrawName() {
				return "need change";
			}
			public String getWebName() {
				return "UnionPay Transfer";
			}
			public String getWithdrawName() {
				return "need change";
			}
			public String getWithdrawHistoryName() {
				return "need change";
			}
			@Override
			public String getIBWithdrawTypeDataTestId() {
				if (brand.equalsIgnoreCase("pug")) {
					return "eWalletWithdrawal-4";
				}
				return "withdrawalType4";
			}
			@Override
			public String getCPTestId() {
				// TODO Auto-generated method stub
				return "CHINAUNION";
			}
			@Override
			public String getdepositDatetestid() {
				return "";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		MOBILE{
			public String getIBWithdrawName() {
				return "need change";
			}
			public String getWebName() {
				if(brand.equalsIgnoreCase("vt") || brand.equalsIgnoreCase("mo")|| brand.equalsIgnoreCase("star"))
				{
					return "Alipay";
				}
				return "MobilePay-CPS";
			}
			public String getWithdrawName() {
				return "need change";
			}
			public String getWithdrawHistoryName() {
				return "need change";
			}
			public String getIBWithdrawTypeDataTestId() {
				return "need change";
			}
			@Override
			public String getCPTestId() {
				// TODO Auto-generated method stub
				if(brand.equalsIgnoreCase("vt") || brand.equalsIgnoreCase("mo")|| brand.equalsIgnoreCase("star"))
				{
					return "alipay";
				}
				return "mobilePay";
			}
			@Override
			public String getdepositDatetestid() {
				return "ChipPay";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		ChipPay_Alipay_CNY {
			public String getWebName() {
				return "Alipay";
			}

			public String getWithdrawName() {
				return "need change";
			}

			public String getWithdrawHistoryName() {
				return "need change";
			}

			public String getIBWithdrawName() {
				return "need change";
			}

			public String getIBWithdrawTypeDataTestId() {
				return "need change";
			}

			@Override
			public String getCPTestId() {
				return "alipay";
			}

			@Override
			public String getdepositDatetestid() {
				if (brand.equalsIgnoreCase(BRAND.STAR.toString())) {
					return "ChipPay.支付寶-Startrader";
				}
				return "ChipPay.支付寶";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		SKBANKTRANSFER {

			@Override
			public String getWebName() {
				// TODO Auto-generated method stub
				return "Internet Banking (South Korea)";
			}

			@Override
			public String getWithdrawName() {
				// TODO Auto-generated method stub
				return "South Korea Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				// TODO Auto-generated method stub
				return "Internet Banking (South Korea)";
			}

			@Override
			public String getIBWithdrawName() {
				// TODO Auto-generated method stub
				return "Bank Transfer";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				if (brand.equalsIgnoreCase("pug")) {
					return "eWalletWithdrawal-2";
				}
				return "withdrawalType2";
			}

			@Override
			public String getCPTestId() {
				// TODO Auto-generated method stub
				return "southkoreabanktransfer";
			}
			@Override
			public String getdepositDatetestid() {
				return "";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},

		EBuyTRANSFER {
			@Override
			public String getWebName() {
				// TODO Auto-generated method stub
				return "Ebuy";
			}

			@Override
			public String getWithdrawName() {
				// TODO Auto-generated method stub
				return "Ebuy";
			}

			@Override
			public String getWithdrawHistoryName() {
				// TODO Auto-generated method stub
				return "Ebuy";
			}

			@Override
			public String getIBWithdrawName() {
				// TODO Auto-generated method stub
				return "EBuy";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "ebuy-75";
			}

			@Override
			public String getCPTestId() {
				// TODO Auto-generated method stub
				return "ebuy";
			}

			@Override
			public String getdepositDatetestid() {
				return "EBuy";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		Multiexc {
			@Override
			public String getWebName() {
				return "Credit/Debit Card Via Multiexc";
			}

			@Override
			public String getWithdrawName() {
				return "need change";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "need change";
			}

			@Override
			public String getIBWithdrawName() {
				return "need change";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "need change";
			}

			@Override
			public String getCPTestId() {
				return "creditdebitcardviamultiexc";
			}

			@Override
			public String getdepositDatetestid() {
				return "Multiexc";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		PERFECTMONEY {
			@Override
			public String getWebName() {
				// TODO Auto-generated method stub
				return "Perfect Money";
			}

			@Override
			public String getWithdrawName() {
				return "Perfect Money";
			}

			@Override
			public String getWithdrawHistoryName() {
				// TODO Auto-generated method stub
				return "Perfect Money";
			}

			@Override
			public String getIBWithdrawName() {
				// TODO Auto-generated method stub
				return "Perfect Money";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				if (brand.equalsIgnoreCase("pug")) {
					return "perfectMoney-61";
				}
				return "perfectmoney-61";
			}

			@Override
			public String getCPTestId() {
				// TODO Auto-generated method stub
				return "perfectmoney";
			}
			@Override
			public String getdepositDatetestid() {
				return "PerfectMoney";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		ADVCASH {
			@Override
			public String getWebName() {
				if (brand.equalsIgnoreCase("vfx")||brand.equalsIgnoreCase("mo")) {
					return "Volet";
				}
				return "Advcash";
			}

			@Override
			public String getWithdrawName() {
				if (brand.equalsIgnoreCase("vfx")||brand.equalsIgnoreCase("mo")) {
					return "Volet";
				}
				return "Advcash";
			}

			@Override
			public String getWithdrawHistoryName() {
				if (brand.equalsIgnoreCase("vfx")||brand.equalsIgnoreCase("mo")) {
					return "Volet";
				}
				return "Advcash";
			}

			@Override
			public String getIBWithdrawName() {
				if (brand.equalsIgnoreCase("vfx")||brand.equalsIgnoreCase("mo")) {
					return "Volet";
				}
				return "Advcash";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				if (brand.equalsIgnoreCase("vfx")||brand.equalsIgnoreCase("mo")) {
					return "volet-69";
				}
				return "advcash-69";
			}

			@Override
			public String getCPTestId() {
				if (brand.equalsIgnoreCase("vfx")||brand.equalsIgnoreCase("mo")) {
					return "volet";
				}
				return "advcash";
			}
			@Override
			public String getdepositDatetestid() {
				return "AdvCash";
			}

			@Override
			public String getWithdrawDataTestId() { return "Volet"; }
		},
		MalaysiaEWallet{
			public String getIBWithdrawName() {
				return "need change";
			}
			public String getWebName() {
				if (brand.equalsIgnoreCase("vt")) {
					return "eWallet";
				}
				return "e-Wallet-2C2P";
			}
			public String getWithdrawName() {
				return "Malaysia E-Wallet";
			}
			public String getWithdrawHistoryName() {
				if (brand.equalsIgnoreCase("vt")) {
					return "eWallet";
				}
				return "e-Wallet-2C2P";
			}
			public String getIBWithdrawTypeDataTestId() {
				return "need change";
			}
			@Override
			public String getCPTestId() {
				if (brand.equalsIgnoreCase("vt")) {
					return "malaysiaewallet";
				}
				return "ewallet";
			}
			@Override
			public String getdepositDatetestid() {
				return "2C2P.EWallet";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		MalaysiaFPX{
			public String getIBWithdrawName() {
				return "need change";
			}
			public String getWebName() {
				if (brand.equalsIgnoreCase("um")) {
					return "Internet Banking (Malaysia)";
				}
				return "FPX";
			}
			public String getWithdrawName() {
				return "FPX";
			}
			public String getWithdrawHistoryName() {
				if (brand.equalsIgnoreCase("um")) {
					return "Internet Banking (Malaysia)";
				}
				return "FPX";
			}
			public String getIBWithdrawTypeDataTestId() {
				return "need return";
			}
			@Override
			public String getCPTestId() {
				if (brand.equalsIgnoreCase("vt")) {
					return "malaysiafpx";
				}
				return "fpx";
			}

			@Override
			public String getdepositDatetestid() {
				return "E-Wallet";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		MalaysiaFPXNew{
			public String getIBWithdrawName() {
				return "need change";
			}
			public String getWebName() {
				if (brand.equalsIgnoreCase(BRAND.VT.toString())){
					return "FPX";
				}
				return "E-Wallet";
			}
			public String getWithdrawName() {
				return "FPX";
			}
			public String getWithdrawHistoryName() {
				if (brand.equalsIgnoreCase("um")) {
					return "Internet Banking (Malaysia)";
				}
				return "FPX";
			}
			public String getIBWithdrawTypeDataTestId() {
				return "need return";
			}
			@Override
			public String getCPTestId() {
				if (brand.equalsIgnoreCase("vt")) {
					return "malaysiafpx";
				}
				return "fpx";
			}

			@Override
			public String getdepositDatetestid() {
				return "2C2P.FPX";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		MalaysiaXPAY{
			public String getIBWithdrawName() {
				return "need change";
			}

			public String getWebName() {
				return "Internet Banking (Malaysia)";

			}

			public String getWithdrawName() {
				return "FPX";
			}

			public String getWithdrawHistoryName() {
				if (brand.equalsIgnoreCase("um")) {
					return "Internet Banking (Malaysia)";
				}
				return "FPX";
			}

			public String getIBWithdrawTypeDataTestId() {
				return "need return";
			}

			@Override
			public String getCPTestId() {
				if (brand.equalsIgnoreCase("vt")) {
					return "malaysiafpx";
				}
				return "fpx";
			}

			@Override
			public String getdepositDatetestid() {
				return "XPAY";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		MalaysiaXPAYNew{
			public String getIBWithdrawName() {
				return "need change";
			}

			public String getWebName() {
				if (brand.equalsIgnoreCase("um")) {
					return "Internet Banking (Malaysia)";
				}
				if (brand.equalsIgnoreCase("vt")){
					return "Malaysia Instant Bank Transfer (Primary)";
				}

				return "Malaysia Instant Bank Transfer";
			}

			public String getWithdrawName() {
				return "FPX";
			}

			public String getWithdrawHistoryName() {
				if (brand.equalsIgnoreCase("um")) {
					return "Internet Banking (Malaysia)";
				}
				return "FPX";
			}

			public String getIBWithdrawTypeDataTestId() {
				return "need return";
			}

			@Override
			public String getCPTestId() {
				if (brand.equalsIgnoreCase("vt")) {
					return "malaysiafpx";
				}
				return "fpx";
			}

			@Override
			public String getdepositDatetestid() {
				if (brand.equalsIgnoreCase("um")) {
					return "XPay-UM_309";
				}
				if(brand.equalsIgnoreCase("mo")){
					return "XPay-VFX_79";
				}
				if(brand.equalsIgnoreCase("vt")){
					return "2C2P.FPX-VT_618";
				}
				return "XPAY";
			}

			@Override
			public String getWithdrawDataTestId() { return "XPay_THB"; } //temp
		},
		Malaysia_EeziePay {
			@Override
			public String getWebName() {
				return "Internet banking (Malaysia)";
			}

			@Override
			public String getWithdrawName() {
				return "Malaysia Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Internet Banking (Malaysia)";
			}

			@Override
			public String getIBWithdrawName() {
				return "Internet Banking (Malaysia)";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "";
			}
			@Override
			public String getCPTestId() {
				return "";
			}

			@Override
			public String getdepositDatetestid() {
				return "EeziePay";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		UNIONPAY{
			public String getIBWithdrawName() {
				return "Union Pay";
			}
			public String getWebName() {
				if(brand.equalsIgnoreCase("star") || brand.equalsIgnoreCase("um")|| brand.equalsIgnoreCase("mo"))
				{
					return "UnionPay Transfer";
				}
				return "UnionPay";
			}
			public String getWithdrawName() {
				if(brand.equalsIgnoreCase("star"))
				{
					return "Union Pay";
				}
				return "UnionPay";
			}
			public String getWithdrawHistoryName() {
				if(brand.equalsIgnoreCase("star"))
				{
					return "Union Pay";
				}
				return "UnionPay";
			}
			@Override
			public String getIBWithdrawTypeDataTestId() {
				if (brand.equalsIgnoreCase("pug")) {
					return "eWalletWithdrawal-4";
				}
				return "withdrawalType4";
			}
			@Override
			public String getCPTestId() {
				if(brand.equalsIgnoreCase("um"))
				{
					return "unionpay";

				}
				return "UNIONPAY";
			}

			@Override
			public String getdepositDatetestid() {
				return "";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		UNIONPAY_Bitpay {
			public String getWebName() {
				return "Unionpay";
			}

			public String getWithdrawName() {
				return "UnionPay";
			}

			public String getWithdrawHistoryName() {
				return "UnionPay";
			}

			public String getIBWithdrawName() {
				return "Union Pay";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				if (brand.equalsIgnoreCase("pug")) {
					return "eWalletWithdrawal-4";
				}
				return "withdrawalType4";
			}

			@Override
			public String getCPTestId() {
				if(brand.equalsIgnoreCase("um"))
				{
					return "unionpay";

				}
				return "UNIONPAY";
			}

			@Override
			public String getdepositDatetestid() {
				return "Bitpay-VT";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		UNIONPAY_Teleport {
			public String getWebName() {
				return "Unionpay";
			}

			public String getWithdrawName() {
				return "UnionPay";
			}

			public String getWithdrawHistoryName() {
				return "UnionPay";
			}

			public String getIBWithdrawName() {
				return "Union Pay";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				if (brand.equalsIgnoreCase("pug")) {
					return "eWalletWithdrawal-4";
				}
				return "withdrawalType4";
			}

			@Override
			public String getCPTestId() {
				if(brand.equalsIgnoreCase("um"))
				{
					return "unionpay";

				}
				return "UNIONPAY";
			}

			@Override
			public String getdepositDatetestid() {
				return "TELEPORT-VT";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		UNIONPAY_DynamicPay {
			public String getWebName() {
				return "Unionpay";
			}

			public String getWithdrawName() {
				return "UnionPay";
			}

			public String getWithdrawHistoryName() {
				return "UnionPay";
			}

			public String getIBWithdrawName() {
				return "Union Pay";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "withdrawalType4";
			}

			@Override
			public String getCPTestId() {
				return "UNIONPAY";
			}

			@Override
			public String getdepositDatetestid() {
				return "DynamicPay-VT";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		UNIONPAY_Mpay {
			public String getWebName() {
				return "Unionpay";
			}

			public String getWithdrawName() {
				return "UnionPay";
			}

			public String getWithdrawHistoryName() {
				return "UnionPay";
			}

			public String getIBWithdrawName() {
				return "Union Pay";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "withdrawalType4";
			}

			@Override
			public String getCPTestId() {
				return "UNIONPAY";
			}

			@Override
			public String getdepositDatetestid() {
				return "Mpay-ST";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		UNIONPAY_MyPay {
			public String getWebName() {
				return "Unionpay";
			}

			public String getWithdrawName() {
				return "UnionPay";
			}

			public String getWithdrawHistoryName() {
				return "UnionPay";
			}

			public String getIBWithdrawName() {
				return "Union Pay";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "withdrawalType4";
			}

			@Override
			public String getCPTestId() {
				return "UNIONPAY";
			}

			@Override
			public String getdepositDatetestid() {
				return "MyPay_VFX";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		UNIONPAY_Passtopay {
			public String getWebName() {
				return "Unionpay";
			}

			public String getWithdrawName() {
				return "UnionPay";
			}

			public String getWithdrawHistoryName() {
				return "UnionPay";
			}

			public String getIBWithdrawName() {
				return "Union Pay";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "withdrawalType4";
			}

			@Override
			public String getCPTestId() {
				return "UNIONPAY";
			}

			@Override
			public String getdepositDatetestid() {
				return "Passtopay-ST";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		UNIONPAY_Toppay2 {
			public String getWebName() {
				return "Unionpay";
			}

			public String getWithdrawName() {
				return "UnionPay";
			}

			public String getWithdrawHistoryName() {
				return "UnionPay";
			}

			public String getIBWithdrawName() {
				return "Union Pay";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "withdrawalType4";
			}

			@Override
			public String getCPTestId() {
				return "UNIONPAY";
			}

			@Override
			public String getdepositDatetestid() {
				return "Toppay2-VFX";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		UNIONPAY_uEnjoy {
			public String getWebName() {
				return "Unionpay";
			}

			public String getWithdrawName() {
				return "UnionPay";
			}

			public String getWithdrawHistoryName() {
				return "UnionPay";
			}

			public String getIBWithdrawName() {
				return "Union Pay";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "withdrawalType4";
			}

			@Override
			public String getCPTestId() {
				return "UNIONPAY";
			}

			@Override
			public String getdepositDatetestid() {
				return "uEnjoy-STAR";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		UNIONPAY_ChipPay {
			public String getWebName() {
				return "Unionpay";
			}

			public String getWithdrawName() {
				return "UnionPay";
			}

			public String getWithdrawHistoryName() {
				return "UnionPay";
			}

			public String getIBWithdrawName() {
				return "Union Pay";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				if (brand.equalsIgnoreCase("pug")) {
					return "eWalletWithdrawal-4";
				}
				return "withdrawalType4";
			}

			@Override
			public String getCPTestId() {
				if(brand.equalsIgnoreCase("um"))
				{
					return "unionpay";

				}
				return "UNIONPAY";
			}

			@Override
			public String getdepositDatetestid() {
				if(brand.equalsIgnoreCase(BRAND.VT.toString())){
					return "ChipPay";
				}
				else if(brand.equalsIgnoreCase(BRAND.STAR.toString())){
					return "ChipPay-Startrader";
				}
				return "ChipPay-MM";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		UNIONPAY_Chinapay {
			public String getWebName() {
				return "Unionpay";
			}

			public String getWithdrawName() {
				return "UnionPay";
			}

			public String getWithdrawHistoryName() {
				return "UnionPay";
			}

			public String getIBWithdrawName() {
				return "Union Pay";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "withdrawalType4";
			}

			@Override
			public String getCPTestId() {
				return "UNIONPAY";
			}

			@Override
			public String getdepositDatetestid() {
				return "Chinapay-MM";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		UNIONPAY_Global_Small {
			public String getWebName() {
				return "Unionpay";
			}

			public String getWithdrawName() {
				return "UnionPay";
			}

			public String getWithdrawHistoryName() {
				return "UnionPay";
			}

			public String getIBWithdrawName() {
				return "Union Pay";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "withdrawalType4";
			}

			@Override
			public String getCPTestId() {
				return "UNIONPAY";
			}

			@Override
			public String getdepositDatetestid() {
				return "Small_CNY_Router";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		UNIONPAY_Global_Medium {
			public String getWebName() {
				return "Unionpay";
			}

			public String getWithdrawName() {
				return "UnionPay";
			}

			public String getWithdrawHistoryName() {
				return "UnionPay";
			}

			public String getIBWithdrawName() {
				return "Union Pay";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "withdrawalType4";
			}

			@Override
			public String getCPTestId() {
				return "UNIONPAY";
			}

			@Override
			public String getdepositDatetestid() {
				return "Medium_CNY_Router";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		UNIONPAY_Global_Large {
			public String getWebName() {
				return "Unionpay";
			}

			public String getWithdrawName() {
				return "UnionPay";
			}

			public String getWithdrawHistoryName() {
				return "UnionPay";
			}

			public String getIBWithdrawName() {
				return "Union Pay";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "withdrawalType4";
			}

			@Override
			public String getCPTestId() {
				return "UNIONPAY";
			}

			@Override
			public String getdepositDatetestid() {
				return "Large_CNY_Router";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		UNIONPAY_Global_VIP {
			public String getWebName() {
				return "Unionpay";
			}

			public String getWithdrawName() {
				return "UnionPay";
			}

			public String getWithdrawHistoryName() {
				return "UnionPay";
			}

			public String getIBWithdrawName() {
				return "Union Pay";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "withdrawalType4";
			}

			@Override
			public String getCPTestId() {
				return "UNIONPAY";
			}

			@Override
			public String getdepositDatetestid() {
				return "VIP_CNY_Router";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		UNIONPAY_Global_Minority {
			public String getWebName() {
				return "Unionpay";
			}

			public String getWithdrawName() {
				return "UnionPay";
			}

			public String getWithdrawHistoryName() {
				return "UnionPay";
			}

			public String getIBWithdrawName() {
				return "Union Pay";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "withdrawalType4";
			}

			@Override
			public String getCPTestId() {
				return "UNIONPAY";
			}

			@Override
			public String getdepositDatetestid() {
				return "EthnicMinority_CNY_Router";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		MEXICOINSTANT {
			@Override
			public String getWebName() {
				if(brand.equalsIgnoreCase("vfx") || brand.equalsIgnoreCase("star"))
				{
					return "Internet Banking (Mexico)";
				}
				return "spei";
			}

			@Override
			public String getWithdrawName() {
				return "Mexico Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Internet Banking (Mexico)";
			}

			@Override
			public String getIBWithdrawName() {
				return "Internet Banking (Mexico)";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "mexicoBankTransfer-63";
			}

			@Override
			public String getCPTestId() {
				if(brand.equalsIgnoreCase("vfx")){
					return "mexicolocalpayment";
				}else if (brand.equalsIgnoreCase("pug")) {
					return "primary";
				}else if (brand.equalsIgnoreCase("star")) {
					return "mexicobanktransfer";
				}
				return "spei";
			}

			@Override
			public String getdepositDatetestid() {
				return "";
			}

			@Override
			public String getWithdrawDataTestId() { return "MexicoBankTransfer"; }
		},
		Bitolo_MXN {
			@Override
			public String getWebName() {
				return "Internet Banking (Mexico)";
			}

			@Override
			public String getWithdrawName() {
				return "Mexico Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Internet Banking (Mexico)";
			}

			@Override
			public String getIBWithdrawName() {
				return "Internet Banking (Mexico)";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "mexicoBankTransfer-63";
			}

			@Override
			public String getCPTestId() {
				if(brand.equalsIgnoreCase("vfx")){
					return "mexicolocalpayment";
				}else if (brand.equalsIgnoreCase("pug")) {
					return "primary";
				}else if (brand.equalsIgnoreCase("star")) {
					return "mexicobanktransfer";
				}
				return "spei";
			}

			@Override
			public String getdepositDatetestid() {
				return "Bitolo";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		Pagsmile_MXN {
			@Override
			public String getWebName() {
				return "Internet Banking (Mexico)";
			}

			@Override
			public String getWithdrawName() {
				return "Mexico Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Internet Banking (Mexico)";
			}

			@Override
			public String getIBWithdrawName() {
				return "Internet Banking (Mexico)";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "";
			}

			@Override
			public String getCPTestId() {
				return "";
			}

			@Override
			public String getdepositDatetestid() {
				return "Pagsmile";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		ON4_Oxxo_MXN {
			@Override
			public String getWebName() {
				return "Internet Banking (Mexico)";
			}

			@Override
			public String getWithdrawName() {
				return "Mexico Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Internet Banking (Mexico)";
			}

			@Override
			public String getIBWithdrawName() {
				return "Internet Banking (Mexico)";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "mexicoBankTransfer-63";
			}

			@Override
			public String getCPTestId() {
				if(brand.equalsIgnoreCase("vfx")){
					return "mexicolocalpayment";
				}else if (brand.equalsIgnoreCase("pug")) {
					return "primary";
				}else if (brand.equalsIgnoreCase("star")) {
					return "mexicobanktransfer";
				}
				return "spei";
			}

			@Override
			public String getdepositDatetestid() {
				return "4ON_Oxxo";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		BPay {
			@Override
			public String getWebName() {
				return "BPAY";
			}

			@Override
			public String getWithdrawName() {
				return "Mexico Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Internet Banking (Mexico)";
			}

			@Override
			public String getIBWithdrawName() {
				return "Internet Banking (Mexico)";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "mexicoBankTransfer-63";
			}

			@Override
			public String getCPTestId() {
				return "";
			}

			@Override
			public String getdepositDatetestid() {
				return "Bpay";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		Tygapay {
			@Override
			public String getWebName() {
				return "Tygapay";
			}

			@Override
			public String getWithdrawName() {
				return "Tygapay";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Tygapay";
			}

			@Override
			public String getIBWithdrawName() {
				return "Tygapay";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "mexicoBankTransfer-63";
			}

			@Override
			public String getCPTestId() { return "Tygapay"; }

			@Override
			public String getdepositDatetestid() {
				return "Tygapay";
			}

			@Override
			public String getWithdrawDataTestId() { return "Tygapay"; }
		},
		BRAZILINSTANT {
			@Override
			public String getWebName() {
				return "Internet Banking (Brazil)";
			}

			@Override
			public String getWithdrawName() {
				return "Brazil Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Internet Banking (Brazil)";
			}

			@Override
			public String getIBWithdrawName() {
				return "Internet Banking (Brazil)";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "Brazil Bank Transfer";
			}

			@Override
			public String getCPTestId() {
				if(brand.equalsIgnoreCase("star"))
				{
					return "brazilbanktransfer";
				}
				return "brazilinstantbanktransfer";
			}

			@Override
			public String getdepositDatetestid() {
				return "";
			}

			@Override
			public String getWithdrawDataTestId() { return "BrazilBankTransfer"; }
		},
		Bitolo_PIX_BRL {
			@Override
			public String getWebName() {
				return "Internet Banking (Brazil)";
			}

			@Override
			public String getWithdrawName() {
				if(brand.equalsIgnoreCase("vt")) {
					return "Brazil Instant Bank Transfer";
				}
				return "Brazil Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Internet Banking (Brazil)";
			}

			@Override
			public String getIBWithdrawName() {
				return "Internet Banking (Brazil)";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "brazilBankTransfer-64";
			}

			@Override
			public String getCPTestId() {
				if(brand.equalsIgnoreCase("star"))
				{
					return "brazilbanktransfer";
				}
				else if(brand.equalsIgnoreCase("um"))
				{
					return "Bitolo-V3-MM_299";
				}
				return "brazilinstantbanktransfer";
			}

			@Override
			public String getdepositDatetestid() {
				return "Bitolo";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		Pagsmile_BRL {
			@Override
			public String getWebName() {
				return "Internet Banking (Brazil)";
			}

			@Override
			public String getWithdrawName() {
				if(brand.equalsIgnoreCase("vt")) {
					return "Brazil Instant Bank Transfer";
				}
				return "Brazil Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Internet Banking (Brazil)";
			}

			@Override
			public String getIBWithdrawName() {
				return "Internet Banking (Brazil)";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "brazilBankTransfer-64";
			}

			@Override
			public String getCPTestId() {
				if(brand.equalsIgnoreCase("star"))
				{
					return "brazilbanktransfer";
				}
				return "brazilinstantbanktransfer";
			}

			@Override
			public String getdepositDatetestid() {
				return "Pagsmile";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		ON4_PIX_BRL {
			@Override
			public String getWebName() {
				return "Internet Banking (Brazil)";
			}

			@Override
			public String getWithdrawName() {
				if(brand.equalsIgnoreCase("vt")) {
					return "Brazil Instant Bank Transfer";
				}
				return "Brazil Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Internet Banking (Brazil)";
			}

			@Override
			public String getIBWithdrawName() {
				return "Internet Banking (Brazil)";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "brazilBankTransfer-64";
			}

			@Override
			public String getCPTestId() {
				if(brand.equalsIgnoreCase("star"))
				{
					return "brazilbanktransfer";
				}
				return "brazilinstantbanktransfer";
			}

			@Override
			public String getdepositDatetestid() {
				return "4ON_PIX";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		Toppay_PIX_BRL {
			@Override
			public String getWebName() {
				return "Internet Banking (Brazil)";
			}

			@Override
			public String getWithdrawName() {
				if(brand.equalsIgnoreCase("vt")) {
					return "Brazil Instant Bank Transfer";
				}
				return "Brazil Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Internet Banking (Brazil)";
			}

			@Override
			public String getIBWithdrawName() {
				return "Internet Banking (Brazil)";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "brazilBankTransfer-64";
			}

			@Override
			public String getCPTestId() {
				if(brand.equalsIgnoreCase("star"))
				{
					return "brazilbanktransfer";
				}
				else if(brand.equalsIgnoreCase("um"))
				{
					return "Toppay-VFX_86";
				}
				return "brazilinstantbanktransfer";
			}

			@Override
			public String getdepositDatetestid() {
				return "Toppay";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		BRAZILPIX {
			@Override
			public String getWebName() {
				return "Internet Banking (Brazil)";
			}

			@Override
			public String getWithdrawName() {
				return "PIX";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "PIX";
			}

			@Override
			public String getIBWithdrawName() {
				return "PIX";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "PIX";
			}

			@Override
			public String getCPTestId() {
				if(brand.equalsIgnoreCase("mo")) {
					return "pixpagamentopreferencial";
				}
				return "pix";
			}

			@Override
			public String getdepositDatetestid() {
				return "";
			}

			@Override
			public String getWithdrawDataTestId() { return "PIX"; }
		},
		BRAZILPIXQR {
			@Override
			public String getWebName() {
				return "Internet Banking (Brazil)";
			}

			@Override
			public String getWithdrawName() {
				return "Brazil PIX QR";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "need return";
			}

			@Override
			public String getIBWithdrawName() {
				return "need return";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "need return";
			}

			@Override
			public String getCPTestId() {
				return "brazilpixqr";
			}

			@Override
			public String getdepositDatetestid() {
				return "";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		HONGKONGINSTANT {
			@Override
			public String getWebName() {
				return "Internet Banking (Hong Kong)";
			}

			@Override
			public String getWithdrawName() {
				if(brand.equalsIgnoreCase("vt") || brand.equalsIgnoreCase("vfx") ||
						brand.equalsIgnoreCase("um")) {
					return "Hong Kong Bank Transfer";
				}
				return "Hong Kong Local Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Internet Banking (Hong Kong)";
			}

			@Override
			public String getIBWithdrawName() {
				return "Internet Banking (Hong Kong)";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "hongKongBankTransfer-49";
			}

			@Override
			public String getCPTestId() {
				if(brand.equalsIgnoreCase("um") || brand.equalsIgnoreCase(BRAND.VT.toString())) {
					return "CWPay-VFX_467";
				}
				return "hongkonglocalbanktransfer";
			}

			@Override
			public String getdepositDatetestid() {
				return "CWPay-VFX";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},

		CHILEINSTANT{
			@Override
			public String getWebName() {
				return "Internet Banking (Chile)";
			}

			@Override
			public String getWithdrawName() {
				return "Chile Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Internet Banking (Chile)";
			}

			@Override
			public String getIBWithdrawName() {
				return "Internet Banking (Chile)";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "Chile Bank Transfer";
			}

			@Override
			public String getCPTestId() {
				return "chileinstantbanktransfer";
			}

			@Override
			public String getdepositDatetestid() {
				return "Pagsmile";
			}

			@Override
			public String getWithdrawDataTestId() { return "ChileBankTransfer"; }
		},
		COLOMBIAINSTANT{
			@Override
			public String getWebName() {
				return "Internet Banking (Colombia)";
			}

			@Override
			public String getWithdrawName() {
				return "Colombia Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Internet Banking (Colombia)";
			}

			@Override
			public String getIBWithdrawName() {
				return "Internet Banking (Colombia)";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "Colombian Bank Transfer";
			}

			@Override
			public String getCPTestId() {
				if(brand.equalsIgnoreCase("vfx")){
					return "colombialocalpayment";
				}
				return "colombiainstantbanktransfer";
			}

			@Override
			public String getdepositDatetestid() {
				return "Pagsmile";
			}

			@Override
			public String getWithdrawDataTestId() { return "ColombianBankTransfer"; }
		},
		COLOMBIAINSTANT_New{
			@Override
			public String getWebName() {
				return "Internet Banking (Colombia)";
			}

			@Override
			public String getWithdrawName() {
				return "Colombian Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Internet Banking (Colombia)";
			}

			@Override
			public String getIBWithdrawName() {
				return "Internet Banking (Colombia)";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "colombiaBankTransfer-89";
			}

			@Override
			public String getCPTestId() {
				if(brand.equalsIgnoreCase("vfx")){
					return "colombialocalpayment";
				}
				return "colombiainstantbanktransfer";
			}

			@Override
			public String getdepositDatetestid() {
				return "Pagsmile";
			}

			@Override
			public String getWithdrawDataTestId() { return "ColombianBankTransfer"; }
		},
		COLOMBIA_4ON_PSE{
			@Override
			public String getWebName() {
				return "Internet Banking (Colombia)";
			}

			@Override
			public String getWithdrawName() {
				return "Colombia Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Internet Banking (Colombia)";
			}

			@Override
			public String getIBWithdrawName() {
				return "Internet Banking (Colombia)";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "colombiaBankTransfer-89";
			}

			@Override
			public String getCPTestId() {
				if(brand.equalsIgnoreCase("vfx")){
					return "colombialocalpayment";
				}
				return "colombiainstantbanktransfer";
			}

			@Override
			public String getdepositDatetestid() {
				return "4ON_PSE";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		COLOMBIA_4ON_Efecty{
			@Override
			public String getWebName() {
				return "Internet Banking (Colombia)";
			}

			@Override
			public String getWithdrawName() {
				return "Colombia Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Internet Banking (Colombia)";
			}

			@Override
			public String getIBWithdrawName() {
				return "Internet Banking (Colombia)";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "colombiaBankTransfer-89";
			}

			@Override
			public String getCPTestId() {
				if(brand.equalsIgnoreCase("vfx")){
					return "colombialocalpayment";
				}
				return "colombiainstantbanktransfer";
			}

			@Override
			public String getdepositDatetestid() {
				return "4ON_Efecty";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		PERUINSTANT{
			@Override
			public String getWebName() {
				return "Internet Banking (Peru)";
			}

			@Override
			public String getWithdrawName() {
				return "Peru Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Internet Banking (Peru)";
			}

			@Override
			public String getIBWithdrawName() {
				return "Internet Banking (Peru)";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "peruBankTransfer-90";
			}

			@Override
			public String getCPTestId() {
				return "peruinstantbanktransfer";
			}

			@Override
			public String getdepositDatetestid() {
				return "Pagsmile";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		PENBT_4ON_PagoEfectivo{
			@Override
			public String getWebName() {
				return "Internet Banking (Peru)";
			}

			@Override
			public String getWithdrawName() {
				return "Peru Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Internet Banking (Peru)";
			}

			@Override
			public String getIBWithdrawName() {
				return "Internet Banking (Peru)";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "peruBankTransfer-90";
			}

			@Override
			public String getCPTestId() {
				return "peruinstantbanktransfer";
			}

			@Override
			public String getdepositDatetestid() {
				return "4ON_PagoEfectivo";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		NIGERIAINSTANT{
			@Override
			public String getWebName() {
				return "Internet Banking (Nigeria)";
			}

			@Override
			public String getWithdrawName() {
				return "Nigeria Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Internet Banking (Nigeria)";
			}

			@Override
			public String getIBWithdrawName() {
				return "Internet Banking (Nigeria)";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "Nigeria Bank Transfer";
			}

			@Override
			public String getCPTestId() {
				return "nigeriainstantbanktransfer";
			}

			@Override
			public String getdepositDatetestid() {
				return "";
			}

			@Override
			public String getWithdrawDataTestId() { return "NigeriaBankTransfer"; }
		},
		Korapay_NGN{
			@Override
			public String getWebName() {
				return "Internet Banking (Nigeria)";
			}

			@Override
			public String getWithdrawName() {
				return "Nigeria Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Internet Banking (Nigeria)";
			}

			@Override
			public String getIBWithdrawName() {
				return "Internet Banking (Nigeria)";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "nigeriaBankTransfer-67";
			}

			@Override
			public String getCPTestId() {
				return "nigeriainstantbanktransfer";
			}

			@Override
			public String getdepositDatetestid() {
				return "Korapay";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		Bitolo_NGN_MM{
			@Override
			public String getWebName() {
				return "Internet Banking (Nigeria)";
			}

			@Override
			public String getWithdrawName() {
				return "Nigeria Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Internet Banking (Nigeria)";
			}

			@Override
			public String getIBWithdrawName() {
				return "Internet Banking (Nigeria)";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "nigeriaBankTransfer-67";
			}

			@Override
			public String getCPTestId() {
				return "nigeriainstantbanktransfer";
			}

			@Override
			public String getdepositDatetestid() {
				return "Bitolo";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},

		LAOSINSTANT{
			@Override
			public String getWebName() {
				return "Internet Banking (Laos)";
			}

			@Override
			public String getWithdrawName() {
				return "Laos Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Internet Banking (Laos)";
			}

			@Override
			public String getIBWithdrawName() {
				return "Internet Banking (Laos)";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "Laos Bank Transfer";
			}

			@Override
			public String getCPTestId() {
				return "laosbanktransfer";
			}

			@Override
			public String getdepositDatetestid() {
				return "ThunderXPay_LAK";
			}

			@Override
			public String getWithdrawDataTestId() { return "LaosBankTransfer"; }
		},
		MONGOLIAINSTANT{
			@Override
			public String getWebName() {
				return "Internet Banking (Mongolia)";
			}

			@Override
			public String getWithdrawName() {
				return "Mongolia Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Internet Banking (Mongolia)";
			}

			@Override
			public String getIBWithdrawName() {
				return "Internet Banking (Mongolia)";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "Mongolia Bank Transfer";
			}

			@Override
			public String getCPTestId() {
				return "need return";
			}

			@Override
			public String getdepositDatetestid() {
				return "";
			}

			@Override
			public String getWithdrawDataTestId() { return "MongoliaBankTransfer"; }
		},
		ZAMBIAINSTANT{
			@Override
			public String getWebName() {
				return "Internet Banking (Zambia)";
			}

			@Override
			public String getWithdrawName() {
				return "Zambia Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Internet Banking (Zambia)";
			}

			@Override
			public String getIBWithdrawName() {
				return "Internet Banking (Zambia)";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "Zambia Bank Transfer";
			}

			@Override
			public String getCPTestId() {
				return "zambiainstantbanktransfer";
			}

			@Override
			public String getdepositDatetestid() {
				return "";
			}

			@Override
			public String getWithdrawDataTestId() { return "ZambiaBankTransfer"; }
		},
		ZAMBIABT_Zotapay_ZMW{
			@Override
			public String getWebName() {
				return "Internet Banking (Zambia)";
			}

			@Override
			public String getWithdrawName() {
				return "Zambia Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Internet Banking (Zambia)";
			}

			@Override
			public String getIBWithdrawName() {
				return "Internet Banking (Zambia)";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "zambiaBankTransfer-52";
			}

			@Override
			public String getCPTestId() {
				return "zambiainstantbanktransfer";
			}

			@Override
			public String getdepositDatetestid() {
				return "Zotapay";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		INTERAC {
			@Override
			public String getWebName() {
				if(brand.equalsIgnoreCase(BRAND.PUG.toString()))
				{
					return "69";
				}
				return "Interac";
			}

			@Override
			public String getWithdrawName() {
				return "Interac";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Interac";
			}

			@Override
			public String getIBWithdrawName() {
				return "Interac";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "interac-99";
			}

			@Override
			public String getCPTestId() {
				return "interac";
			}

			@Override
			public String getdepositDatetestid() {
				return "Gigadat";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		INTERAC_New {
			@Override
			public String getWebName() {
				return "Interac";
			}

			@Override
			public String getWithdrawName() {
				return "Interac";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Interac";
			}

			@Override
			public String getIBWithdrawName() {
				return "Interac";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "interac-99";
			}

			@Override
			public String getCPTestId() {
				return "interac";
			}

			@Override
			public String getdepositDatetestid() {
				return "Gigadat";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		LOCALDEPOSITOR {
			@Override
			public String getWebName() {
				return "Local Depositor";
			}

			@Override
			public String getWithdrawName() {
				return "Local Depositor";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Local Depositor";
			}

			@Override
			public String getIBWithdrawName() {
				return "Local Depositor";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "localDepositor-65";
			}

			@Override
			public String getCPTestId() {
				if (brand.equalsIgnoreCase("um"))
				{
					return "CroinPlus-UM_301";
				}
				return "localdepositor";
			}

			@Override
			public String getdepositDatetestid() {
				return "CroinPlus";
			}

			@Override
			public String getWithdrawDataTestId() { return "LocalDepositor"; }
		},
		LOCALDEPOSITOR_New {
			@Override
			public String getWebName() {
				if (brand.equalsIgnoreCase(BRAND.STAR.toString())) {
					return "Thailand Local Depositor";
				}
				return "Local Depositor";
			}

			@Override
			public String getWithdrawName() {
				return "Local Depositor";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Local Depositor";
			}

			@Override
			public String getIBWithdrawName() {
				return "Local Depositor";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "Local Depositor";
			}

			@Override
			public String getCPTestId() {
				if (brand.equalsIgnoreCase("um"))
				{
					return "CroinPlus-UM_301";
				}
				return "localdepositor";
			}

			@Override
			public String getdepositDatetestid() {
				return "CroinPlus";
			}

			@Override
			public String getWithdrawDataTestId() { return "LocalDepositor"; }
		},
		VIETNAMQR {
			@Override
			public String getWebName() {
				if(brand.equalsIgnoreCase("star"))
				{
					return "QR（Vietnam）";
				}
				return "Internet Banking (Vietnam)";
			}

			@Override
			public String getWithdrawName() {
				return "Vietnam QR Payment";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "need return";
			}

			@Override
			public String getIBWithdrawName() {
				return "need return";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "need return";
			}

			@Override
			public String getCPTestId() {
				if (brand.equalsIgnoreCase("vfx"))
				{
					return "vietnamqrpayment";
				}
				else if(brand.equalsIgnoreCase("star"))
				{
					return "qrvietnambanktransfer";
				}
				return "vietnamqr";
			}

			@Override
			public String getdepositDatetestid() {
				return "";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		VIETNAMP2P {
			@Override
			public String getWebName() {
				return "Internet banking (Vietnam)";
			}

			@Override
			public String getWithdrawName() {
				return "Vietnam P2P Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "need return";
			}

			@Override
			public String getIBWithdrawName() {
				return "need return";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "need return";
			}

			@Override
			public String getCPTestId() {
				return "vietnamp2pbanktransfer";
			}

			@Override
			public String getdepositDatetestid() {
				return "";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		VIETNAMMOMO {
			@Override
			public String getWebName() {
				return "Momopay";
			}

			@Override
			public String getWithdrawName() {
				return "Momopay";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "need return";
			}

			@Override
			public String getIBWithdrawName() {
				return "need return";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "need return";
			}

			@Override
			public String getCPTestId() {
				if (brand.equalsIgnoreCase("pug")) {
					return "vietnammomopay";
				}
				return "momopay";
			}

			@Override
			public String getdepositDatetestid() {
				return "momopay";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		THAILANDQR {
			@Override
			public String getWebName() {
				return "Internet Banking (Thailand)";
			}

			@Override
			public String getWithdrawName() {
				return "Thailand QR Payment";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "need return";
			}

			@Override
			public String getIBWithdrawName() {
				return "need return";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "need return";
			}

			@Override
			public String getCPTestId() {
				return "thailandqrpayment";
			}

			@Override
			public String getdepositDatetestid() {
				return "";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		THAILANDQR_Vtpay {
			@Override
			public String getWebName() {
				return "Internet banking (Thailand)";
			}

			@Override
			public String getWithdrawName() {
				return "Internet banking (Thailand)";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "need return";
			}

			@Override
			public String getIBWithdrawName() {
				return "need return";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "need return";
			}

			@Override
			public String getCPTestId() {
				return "thailandqrpayment";
			}

			@Override
			public String getdepositDatetestid() {
				return "Vtpay-QR";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		THAILANDQR_Xpay {
			@Override
			public String getWebName() {
				return "QR (Thailand)";
			}

			@Override
			public String getWithdrawName() {
				return "Thailand QR Payment";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "need return";
			}

			@Override
			public String getIBWithdrawName() {
				return "need return";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "need return";
			}

			@Override
			public String getCPTestId() {
				return "thailandqrpayment";
			}

			@Override
			public String getdepositDatetestid() {
				return "XPay-VFX1_233";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		THB_EeziePay {
			@Override
			public String getWebName() {
				return "Internet banking (Thailand)";
			}

			@Override
			public String getWithdrawName() {
				return "Thailand Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Internet Banking (Thailand)";
			}

			@Override
			public String getIBWithdrawName() {
				return "Internet Banking (Thailand)";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "";
			}
			@Override
			public String getCPTestId() {
				return "";
			}

			@Override
			public String getdepositDatetestid() {
				return "EeziePay";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		ZOTAPAY_THB {
			@Override
			public String getWebName() {
				return "Internet banking (Thailand)";
			}

			@Override
			public String getWithdrawName() {
				return "Thailand Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Internet Banking (Thailand)";
			}

			@Override
			public String getIBWithdrawName() {
				return "Internet Banking (Thailand)";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "";
			}

			@Override
			public String getCPTestId() {
				return "";
			}

			@Override
			public String getdepositDatetestid() {
				return "ZotaPay";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		INDONESIAVIRTUAL {
			@Override
			public String getWebName() {
				return "Internet Banking (Indonesia)";
			}

			@Override
			public String getWithdrawName() {
				return "Indonesia Virtual Account Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "need return";
			}

			@Override
			public String getIBWithdrawName() {
				return "need return";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "need return";
			}

			@Override
			public String getCPTestId() {
				return "indonesiavirtualaccountbanktransfer";
			}

			@Override
			public String getdepositDatetestid() {
				return "";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		INDONESIAP2P {
			@Override
			public String getWebName() {
				return "Internet Banking (Indonesia)";
			}

			@Override
			public String getWithdrawName() {
				return "Indonesia P2P Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "need return";
			}

			@Override
			public String getIBWithdrawName() {
				return "need return";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "need return";
			}

			@Override
			public String getCPTestId() {
				return "indonesiap2pbanktransfer";
			}

			@Override
			public String getdepositDatetestid() {
				return "";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		INDONESIAGOPAY {
			@Override
			public String getWebName() {
				if (brand.equalsIgnoreCase("star"))
				{
					return "GoPay";
				}
				return "Internet Banking (Indonesia)";
			}

			@Override
			public String getWithdrawName() {
				return "Indonesia GoPay";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "need return";
			}

			@Override
			public String getIBWithdrawName() {
				return "need return";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "need return";
			}

			@Override
			public String getCPTestId() {
				if (brand.equalsIgnoreCase("star"))
				{
					return "gopay";
				}
				return "indonesiagopay";
			}

			@Override
			public String getdepositDatetestid() {
				return "";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		WowsPay_QR_Gopay {
			@Override
			public String getWebName() {
				if (brand.equalsIgnoreCase(BRAND.STAR.toString()))
				{
					return "GoPay";
				}
				return "Internet Banking (Indonesia)";
			}

			@Override
			public String getWithdrawName() {
				return "Indonesia GoPay";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "need return";
			}

			@Override
			public String getIBWithdrawName() {
				return "need return";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "need return";
			}

			@Override
			public String getCPTestId() {
				if (brand.equalsIgnoreCase(BRAND.STAR.toString()))
				{
					return "gopay";
				}
				return "indonesiagopay";
			}

			@Override
			public String getdepositDatetestid() {
				return "Wowspay_QR_Gopay";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		NETHERLANDINSTANT {
			@Override
			public String getWebName() {
				return "Netherlands Instant Bank Transfer";
			}

			@Override
			public String getWithdrawName() {
				return "Netherlands Instant Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "need return";
			}

			@Override
			public String getIBWithdrawName() {
				return "need return";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "need return";
			}

			@Override
			public String getCPTestId() {
				if (brand.equalsIgnoreCase(BRAND.PUG.toString()))
				{
					return "dutchinstantbanktransfer";
				}
				return "netherlandsinstantbanktransfer";
			}

			@Override
			public String getdepositDatetestid() {
				return "";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		UAEINSTANT {
			@Override
			public String getWebName() {
				return "UAE Bank Transfer";
			}

			@Override
			public String getWithdrawName() {
				return "UAE Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				if(brand.equalsIgnoreCase("star")){
					return "UAE Bank Transfer";
				}
				return "Internet Banking (UAE)";
			}

			@Override
			public String getIBWithdrawName() {
				return "Internet Banking (UAE)";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "uaeBankTransfer-108";
			}

			@Override
			public String getCPTestId() {
				return "uaebanktransfer";
			}

			@Override
			public String getdepositDatetestid() {
				return "";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		UAEP2P {
			@Override
			public String getWebName() {
				return "UAE Bank Transfer";
			}

			@Override
			public String getWithdrawName() {
				return "UAE Bank Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Internet Banking (UAE)";
			}

			@Override
			public String getIBWithdrawName() {
				return "Internet Banking (UAE)";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "uaeBankTransfer-108";
			}

			@Override
			public String getCPTestId() {
				return "uaebanktransfer";
			}

			@Override
			public String getdepositDatetestid() {
				if(brand.equalsIgnoreCase(BRAND.VT.toString())) {
					return "Offline_FAB_P2P";
				}
				else if(brand.equalsIgnoreCase(BRAND.STAR.toString())) {
					return "UAEBankTransfer";
				}
				return "BankTransfer_P2P";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		AirTM {
			@Override
			public String getWebName() {
				return "E-wallet";
			}

			@Override
			public String getWithdrawName() {
				return "AirTM";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "E-wallet";
			}

			@Override
			public String getIBWithdrawName() {
				return "E-wallet";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "airtm-109";
			}

			@Override
			public String getCPTestId() {
				return "airtm";
			}

			@Override
			public String getdepositDatetestid() {
				return "";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		AirTM_New {
			@Override
			public String getWebName() {
				return "E-wallet";
			}

			@Override
			public String getWithdrawName() {
				return "AirTM";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "AirTM";
			}

			@Override
			public String getIBWithdrawName() {
				return "AirTM";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "airtm-109";
			}

			@Override
			public String getCPTestId() {
				return "airtm";
			}

			@Override
			public String getdepositDatetestid() {
				return "AirTM";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		FXIR {
			@Override
			public String getWebName() {
				return "FX-IR";
			}

			@Override
			public String getWithdrawName() {
				return "FX-IR";
			}

			@Override
			public String getWithdrawHistoryName() {
				if(brand.equalsIgnoreCase("mo")) {
					return "Ewallet";
				}
				return "FX-IR";
			}

			@Override
			public String getIBWithdrawName() {
				if(brand.equalsIgnoreCase("mo")) {
					return "Ewallet";
				}
				return "FX-IR";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "fxIr-97";
			}

			@Override
			public String getCPTestId() {
				return "fxir";
			}

			@Override
			public String getdepositDatetestid() {
				return "FXIR-VT";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		BANXA {
			@Override
			public String getWebName() {
				return "Banxa-Visa/Master-CPS";
			}

			@Override
			public String getWithdrawName() {
				return "need change";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "need change";
			}

			@Override
			public String getIBWithdrawName() {
				return "need change";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "need change";
			}

			@Override
			public String getCPTestId() {
				return "banxa";
			}

			@Override
			public String getdepositDatetestid() {
				return "Banxa_VISAMASTER-Star";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		BANXA_NL_New {
			@Override
			public String getWebName() {
				return "Internet Banking (Netherlands)";
			}

			@Override
			public String getWithdrawName() {
				return "Netherlands Local Transfer";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Internet Banking (Netherlands)";
			}

			@Override
			public String getIBWithdrawName() {
				return "Internet Banking (Netherlands)";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "need change";
			}

			@Override
			public String getCPTestId() {
				return "banxa";
			}

			@Override
			public String getdepositDatetestid() {
				if(brand.equalsIgnoreCase(BRAND.STAR.toString())) {
					return "Banxa-iDEAL-Startrader";
				}
				return "banxa";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		GIROPAY {
			@Override
			public String getWebName() {
				return "Giropay";
			}

			@Override
			public String getWithdrawName() {
				return "need change";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "need change";
			}

			@Override
			public String getIBWithdrawName() {
				return "need change";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "need change";
			}

			@Override
			public String getCPTestId() {
				return "giropay";
			}

			@Override
			public String getdepositDatetestid() {
				return "";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		AZUPAY {
			@Override
			public String getWebName() {
				return "PayID";
			}

			@Override
			public String getWithdrawName() {
				return "PayID";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "PayID";
			}

			@Override
			public String getIBWithdrawName() {
				return "PayID";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "payid-113";
			}

			@Override
			public String getCPTestId() {
				return "payid";
			}

			@Override
			public String getdepositDatetestid() {
				return "Azupay";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		BDTBkash {
			@Override
			public String getWebName() {
				return "Bkash";
			}

			@Override
			public String getWithdrawName() {
				return "Bkash";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Bkash";
			}

			@Override
			public String getIBWithdrawName() {
				return "need change";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "need change";
			}

			@Override
			public String getCPTestId() {
				return "bkash";
			}

			@Override
			public String getdepositDatetestid() {
				return "Bkash";
			}

			@Override
			public String getWithdrawDataTestId() { return "Bkash"; }
		},
		BDTRocket {
			@Override
			public String getWebName() {
				return "Rocket";
			}

			@Override
			public String getWithdrawName() {
				return "Rocket";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Rocket";
			}

			@Override
			public String getIBWithdrawName() {
				return "Rocket";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "Rocket";
			}

			@Override
			public String getCPTestId() {
				return "rocket";
			}

			@Override
			public String getdepositDatetestid() {
				return "Rocket";
			}

			@Override
			public String getWithdrawDataTestId() { return "Rocket"; }
		},
		ARSBT {
			@Override
			public String getWebName() {
				return "Internet Banking (Argentina)";
			}

			@Override
			public String getWithdrawName() {
				return "";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "";
			}

			@Override
			public String getIBWithdrawName() {
				return "";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "";
			}

			@Override
			public String getCPTestId() {
				return "argentinalocalpayment";
			}

			@Override
			public String getdepositDatetestid() {
				return "";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		ARSBT_4ON_PagoFacil {
			@Override
			public String getWebName() {
				return "Internet Banking (Argentina)";
			}

			@Override
			public String getWithdrawName() {
				return "";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "";
			}

			@Override
			public String getIBWithdrawName() {
				return "";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "";
			}

			@Override
			public String getCPTestId() {
				return "argentinalocalpayment";
			}

			@Override
			public String getdepositDatetestid() {
				return "4ON_PagoFacil";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		ARSBT_4ON_MercadoPagoWallet {
			@Override
			public String getWebName() {
				return "Internet Banking (Argentina)";
			}

			@Override
			public String getWithdrawName() {
				return "";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "";
			}

			@Override
			public String getIBWithdrawName() {
				return "";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "";
			}

			@Override
			public String getCPTestId() {
				return "argentinalocalpayment";
			}

			@Override
			public String getdepositDatetestid() {
				return "4ON_MercadoPagoWallet";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		ARSBT_4ON_RapiPago {
			@Override
			public String getWebName() {
				return "Internet Banking (Argentina)";
			}

			@Override
			public String getWithdrawName() {
				return "";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "";
			}

			@Override
			public String getIBWithdrawName() {
				return "";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "";
			}

			@Override
			public String getCPTestId() {
				return "argentinalocalpayment";
			}

			@Override
			public String getdepositDatetestid() {
				return "4ON_RapiPago";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		},
		Paypal {
			@Override
			public String getWebName() {
				return "PayPal";
			}

			@Override
			public String getWithdrawName() {
				return "PayPal";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "PayPal";
			}

			@Override
			public String getIBWithdrawName() {
				return "PayPal";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "PayPal";
			}

			@Override
			public String getCPTestId() {
				return "paypal";
			}

			@Override
			public String getdepositDatetestid() {
				return "Paypal";
			}

			@Override
			public String getWithdrawDataTestId() { return "Paypal"; }
		},
		BDTNagad {
			@Override
			public String getWebName() {
				return "Nagad";
			}

			@Override
			public String getWithdrawName() {
				return "Nagad";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "Nagad";
			}

			@Override
			public String getIBWithdrawName() {
				return "need change";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "need change";
			}

			@Override
			public String getCPTestId() {
				return "nagad";
			}

			@Override
			public String getdepositDatetestid() {
				return "Nagad";
			}

			@Override
			public String getWithdrawDataTestId() { return "Nagad"; }
		},
		SLASH {
			@Override
			public String getWebName() {
				return "Internet Banking (Japan)";
			}

			@Override
			public String getWithdrawName() {
				return "need change";
			}

			@Override
			public String getWithdrawHistoryName() {
				return "need change";
			}

			@Override
			public String getIBWithdrawName() {
				return "need change";
			}

			@Override
			public String getIBWithdrawTypeDataTestId() {
				return "need change";
			}

			@Override
			public String getCPTestId() {
				return "slash";
			}

			@Override
			public String getdepositDatetestid() {
				return "";
			}

			@Override
			public String getWithdrawDataTestId() { return ""; }
		};

		public abstract String getCPTestId();

		public abstract String getIBWithdrawName();
		public abstract String getWebName();

		public abstract String getWithdrawName();

		public abstract String getWithdrawHistoryName();

		public abstract String getIBWithdrawTypeDataTestId();
		public abstract String getdepositDatetestid();
		public abstract String getWithdrawDataTestId();
	};

	public static enum CPMenuName {
		HOME, LIVEACCOUNTS, DEMOACCOUNTS, ADDACCOUNT, ADDDEMOACCOUNT, DEPOSITFUNDS, WITHDRAWFUNDS, TRANSFERACCOUNTS, TRANSACTIONHISTORY,
		PAYMENTDETAILS, DOWNLOADS, SUPPORT, PROFILES, MYPROFILES, CHANGEPWD, TWOFA, CHARTSBYTRADINGVIEW, PREMIUMTRADERTOOLS, MARKETWIDGETS,
		PREMIUMTRADERTUTORIALS, CONTACTSUS, CPPORTAL, IBPORTAL,DAPPORTAL,IBDASHBOARD,IBTRANSACTIONHISTORY,DEPOSITBONUS,TRADINGBONUS, IBACCOUNTREPORT, IBCLIENTREPORT, IBREBATEREPORT, UPGRADETOIB, AGREEIBAGREEMENT, IBREFERRALLINKSMENU, IBCAMPAIGNLINKSMENU, IBPROFILEMENU,
		ADDACCOUNTMOBILE,DEPOSITFUNDSMOBILE,WITHDRAWFUNDSMOBILE,TRANSACTIONHISTORYMOBILE,
		WALLET_HOME, CloseVCard,WALLET_DEPOSIT, WALLET_WITHDRAW, WALLET_TRANSFER, WALLET_CONVERT, WALLET_TRANSACTION_HISTORY,COPYTRADING, DISCOVER, COPIER, SIGNALPROVIDER, TRADE, SECURITYMANAGEMENT, WALLET_CARD,CLOSE_VCARD_TIPS,WALLET_EARN
	};

	public static enum AdminMenuName {
		CLIENT, TWO_FACTOR_LIMIT_SETTING, EMAIL_MGMT, WITHDRAWAL_AUDIT, DEPOSIT_AUDIT, ACCOUNT_AUDIT, ADDITIONAL_ACCOUNT_AUDIT,
		POI_POA_AUDIT, MULTI_FACTOR_AUTHENTICATION, WITHDRAWAL_LIMITS, EXTERNAL_USER, TRADING_ACCOUNT, REBATE_ACCOUNT, CAMPAIGN_MGMT,
		DEPOSIT_WITHDRAWAL_REPORT, CASH_ADJUSTMENT_AUDIT, CREDIT_ADJUSTMENT_AUDIT, PT_MGMT, REBATE_ACCOUNT_AGREEMENT
	}

	public static enum APPMenuName {
		TRADES,ORDERS,PROMO,PROFILE,TRANSFER,DEPOSIT,WITHDRAW,FUNDS,SETTING
	};

	public static enum CURRENCY {
		USD("USD"),
		CAD("CAD"),
		GBP("GBP"),
		AUD("AUD"),
		EUR("EUR"),
		SGD("SGD"),
		NZD("NZD"),
		HKD("HKD"),
		JPY("JPY"),
		USDT("UST"),
		USC("USC"),
		AED("AED"),
		BTC("BTC"),
		ETH("ETH"),
		PLN("PLN");

		private final String currencyCode;

		CURRENCY(String currencyCode) {
			this.currencyCode = currencyCode;
		}

		public String getCurrencyCode() {
			return currencyCode;
		}

		public String getCurrencyDesc() {
			return this.name();
		}

		public static CURRENCY getRecByCurrencyDesc(String currencyDesc) {
			for (CURRENCY currency : values()) {
				if (currencyDesc.equalsIgnoreCase(currency.getCurrencyDesc())) {
					return currency;
				}
			}
			return null;
		}

		public static CURRENCY getRecByCurrencyCode(String currencyCode) {
			for (CURRENCY currency : values()) {
				if (currencyCode.equalsIgnoreCase(currency.currencyCode)) {
					return currency;
				}
			}
			return null;
		}

	};

	public static enum PLATFORM {
		MT4("4"),
		MT5("5"),
		MTS("6");

		private final String serverCategory;

		PLATFORM(String serverCategory) {
			this.serverCategory = serverCategory;
		}

		public String getServerCategory() {
			return serverCategory;
		}

		public String getPlatformDesc() {
			return this.name();
		}

		public static PLATFORM getRecByServerCategory(String serverCategory) {
			for (PLATFORM platform : values()) {
				if (serverCategory.equalsIgnoreCase(platform.serverCategory)) {
					return platform;
				}
			}
			return null;
		}

		public static PLATFORM getRecByPlatformDesc(String platformDesc) {
			for (PLATFORM platform : values()) {
				if (platformDesc.toLowerCase().contains(platform.getPlatformDesc().toLowerCase())) {
					return platform;
				}
			}
			return null;
		}

	};

	public static enum PORTAL_LANGUAGE {
		ENGLISH {
			@Override
			public String getTestId() {
				return "en_US";
			}

			@Override
			public String getDesc() {
				return "English";
			}
		},
		JAPANESE {
			@Override
			public String getTestId() {
				return "ja";
			}

			@Override
			public String getDesc() {
				return "日本語";
			}
		};

		public abstract String getTestId();
		public abstract String getDesc();
	}

	public static enum TRANSACTIONTYPE {
		WITHDRAW, DEPOSIT, TRANSFER
	};

	public static enum ACCOUNTTYPE {
		ALL {
			@Override
			public String getAccountTypeCode() {
				return "0";
			}

			@Override
			public String getTestId() {
				return "";
			}

			@Override
			public String getLiveAccountName() {
				return "ALL";
			}
		},
		// MT4
		STANDARD_STP {
			@Override
			public String getAccountTypeCode() {
				return "1";
			}

			@Override
			public String getTestId() {
				return "standardSTP";
			}

			@Override
			public String getLiveAccountName() {

				if(GlobalProperties.brand.equalsIgnoreCase(BRAND.PUG.toString()) ||
						GlobalProperties.brand.equalsIgnoreCase(BRAND.UM.toString())) {
					return "Standard";
				} else if(GlobalProperties.brand.equalsIgnoreCase(BRAND.MO.toString())) {
					return "Direct STP";
				}

				return "Standard STP";
			}
		},
		RAW_ECN {
			@Override
			public String getAccountTypeCode() {
				return "2";
			}

			@Override
			public String getTestId() {
				return "rawECN";
			}

			@Override
			public String getLiveAccountName() {

				if(GlobalProperties.brand.equalsIgnoreCase(BRAND.PUG.toString())) {
					return "Prime";
				} else if(GlobalProperties.brand.equalsIgnoreCase(BRAND.MO.toString())) {
					return "PRIME ECN";
				} else if(GlobalProperties.brand.equalsIgnoreCase(BRAND.VT.toString()) ||
						GlobalProperties.brand.equalsIgnoreCase(BRAND.STAR.toString())) {
					return "Raw ECN";
				}

				return "RAW ECN";
			}
		},
		ISLAMIC_STP {
			@Override
			public String getAccountTypeCode() {
				return "6";
			}

			@Override
			public String getTestId() {
				return "swapFreeStandardSTP";
			}

			@Override
			public String getLiveAccountName() {
				if(GlobalProperties.brand.equalsIgnoreCase(BRAND.VT.toString())) {
					return "Swap-free Standard STP";
				} else if(GlobalProperties.brand.equalsIgnoreCase(BRAND.PUG.toString())) {
					return "Islamic Standard";
				} else if(GlobalProperties.brand.equalsIgnoreCase(BRAND.UM.toString())) {
					return "SWAP Free STANDARD";
				}

				return "SWAP FREE STP";
			}
		},
		ISLAMIC_ECN {
			@Override
			public String getAccountTypeCode() {
				return "7";
			}

			@Override
			public String getTestId() {
				return "swapFreeRawECN";
			}

			@Override
			public String getLiveAccountName() {
				if(brand.equalsIgnoreCase("vt")) {
					return "Swap-free Raw ECN";
				}
				if(brand.equalsIgnoreCase("pug")){
					return "Islamic Prime";
				}
				return "SWAP FREE ECN";
			}
		},
		PRO_ECN {
			@Override
			public String getAccountTypeCode() {
				return "8";
			}

			@Override
			public String getTestId() {
				return "";
			}

			@Override
			public String getLiveAccountName() {
				return "PRO ECN";
			}
		},
		VIP_STP{
			@Override
			public String getAccountTypeCode() {
				return "9";
			}

			@Override
			public String getTestId() {
				return "9";
			}

			@Override
			public String getLiveAccountName() {
				return "VIP STP";
			}
		},
		PAMM_INVESTOR {
			@Override
			public String getAccountTypeCode() {
				return "10";
			}

			@Override
			public String getTestId() {
				return "PAMM";
			}

			@Override
			public String getLiveAccountName() {
				return "PAMM investor";
			}
		},
		PREMIUM_STP {
			@Override
			public String getAccountTypeCode() {
				return "38";
			}

			@Override
			public String getTestId() {
				return "PREMIUM_STP";
			}

			@Override
			public String getLiveAccountName() {
				return "Premium STP";
			}
		},
		PERPETUAL {
			@Override
			public String getAccountTypeCode() {
				return "116";
			}

			@Override
			public String getTestId() {
				return "Perpetual";
			}

			@Override
			public String getLiveAccountName() {
				return "Perpetual";
			}
		},
		// MT5
		HEDGE_STP {
			@Override
			public String getAccountTypeCode() {
				return "13";
			}

			@Override
			public String getTestId() {
				return "standardSTP";
			}

			@Override
			public String getLiveAccountName() {

				if(GlobalProperties.brand.equalsIgnoreCase(BRAND.PUG.toString())) {
					return "MT5 Standard";
				} else if(GlobalProperties.brand.equalsIgnoreCase(BRAND.MO.toString())) {
					return "Direct STP";
				}

				return "Standard STP";
			}
		},
		HEDGE_ECN {
			@Override
			public String getAccountTypeCode() {
				return "14";
			}

			@Override
			public String getTestId() {
				return "rawECN";
			}

			@Override
			public String getLiveAccountName() {
				return "Hedge Ecn";
			}
		},
		MT5_ISLAMIC_STP {
			@Override
			public String getAccountTypeCode() {
				return "15";
			}

			@Override
			public String getTestId() {
				return "swapFreeStandardSTP";
			}

			@Override
			public String getLiveAccountName() {
				if(GlobalProperties.brand.equalsIgnoreCase(BRAND.VT.toString())) {
					return "Swap-free Standard STP";
				} else if(GlobalProperties.brand.equalsIgnoreCase(BRAND.PUG.toString())) {
					return "Islamic Standard";
				} else if(GlobalProperties.brand.equalsIgnoreCase(BRAND.UM.toString())) {
					return "SWAP Free STANDARD";
				}

				return "SWAP FREE STP";
			}
		},
		MT5_ISLAMIC_ECN {
			@Override
			public String getAccountTypeCode() {
				return "16";
			}

			@Override
			public String getTestId() {
				return "swapFreeRawECN";
			}

			@Override
			public String getLiveAccountName() {
				return "Swap-Free ECN";
			}
		},
		MT5_PERPETUAL {
			@Override
			public String getAccountTypeCode() {
				return "117";
			}

			@Override
			public String getTestId() {
				return "Perpetual";
			}

			@Override
			public String getLiveAccountName() {
				return "Perpetual";
			}
		},
		MT5_STANDARD_STP_CENT {
			@Override
			public String getAccountTypeCode() {
				return "24";
			}

			@Override
			public String getTestId() {
				return "STANDARD_STP_CENT";
			}

			@Override
			public String getLiveAccountName() {
				return "Standard STP Cent";
			}
		},
		MT5_ISLAMIC_STP_CENT {
			@Override
			public String getAccountTypeCode() {
				return "25";
			}

			@Override
			public String getTestId() {
				return "ISLAMIC_STP_CENT";
			}

			@Override
			public String getLiveAccountName() {
				return "Swap-free STP Cent";
			}
		},
		MT5_RAW_ECN_CENT {
			@Override
			public String getAccountTypeCode() {
				return "26";
			}

			@Override
			public String getTestId() {
				return "RAW_ECN_CENT";
			}

			@Override
			public String getLiveAccountName() {
				return "Raw ECN Cent";
			}
		},
		MT5_ISLAMIC_ECN_CENT {
			@Override
			public String getAccountTypeCode() {
				return "27";
			}

			@Override
			public String getTestId() {
				return "ISLAMIC_ECN_CENT";
			}

			@Override
			public String getLiveAccountName() {
				return "Swap-free ECN Cent";
			}
		},
		MT5_TRADING_VIEW_STP {
			@Override
			public String getAccountTypeCode() {
				return "128";
			}

			@Override
			public String getTestId() {
				return "TRADING_VIEW_STP";
			}

			@Override
			public String getLiveAccountName() {
				return "TradingView STP";
			}
		},
		MT5_TRADING_VIEW {
			@Override
			public String getAccountTypeCode() {
				return "37";
			}

			@Override
			public String getTestId() {
				return "TRADING_VIEW";
			}

			@Override
			public String getLiveAccountName() {
				return "TradingView";
			}
		},
		MT5_PAMM {
			@Override
			public String getAccountTypeCode() {
				return "28";
			}

			@Override
			public String getTestId() {
				return "PAMM";
			}

			@Override
			public String getLiveAccountName() {
				return "PAMM investor";
			}
		},
		// MTS
		MTS_ST {
			@Override
			public String getAccountTypeCode() {
				return "99";
			}

			@Override
			public String getTestId() {
				return "st";
			}

			@Override
			public String getLiveAccountName() {
				return "Copy Trading";
			}
		},
		MTS_HEDGE_STP {
			@Override
			public String getAccountTypeCode() {
				return "100";
			}

			@Override
			public String getTestId() {
				return "standardSTP";
			}

			@Override
			public String getLiveAccountName() {
				return "MTS Standard STP";
			}
		},
		MTS_HEDGE_ECN {
			@Override
			public String getAccountTypeCode() {
				return "110";
			}

			@Override
			public String getTestId() {
				return "rawECN";
			}

			@Override
			public String getLiveAccountName() {
				return "Hedge ECN";
			}
		},
		MTS_ISLAMIC_STP {
			@Override
			public String getAccountTypeCode() {
				return "102";
			}

			@Override
			public String getTestId() {
				return "swapFreeStandardSTP";
			}

			@Override
			public String getLiveAccountName() {
				return "Swap-free STP";
			}
		},
		MTS_ISLAMIC_ECN {
			@Override
			public String getAccountTypeCode() {
				return "103";
			}

			@Override
			public String getTestId() {
				return "swapFreeRawECN";
			}

			@Override
			public String getLiveAccountName() {
				return "Swap-free ECN";
			}
		},
		MTS_STANDARD_STP_CENT {
			@Override
			public String getAccountTypeCode() {
				return "104";
			}

			@Override
			public String getTestId() {
				return "STANDARD_STP_CENT";
			}

			@Override
			public String getLiveAccountName() {
				return "Standard STP Cent";
			}
		},
		MTS_ISLAMIC_STP_CENT {
			@Override
			public String getAccountTypeCode() {
				return "105";
			}

			@Override
			public String getTestId() {
				return "ISLAMIC_STP_CENT";
			}

			@Override
			public String getLiveAccountName() {
				return "Swap-free STP Cent";
			}
		},
		MTS_RAW_ECN_CENT {
			@Override
			public String getAccountTypeCode() {
				return "107";
			}

			@Override
			public String getTestId() {
				return "RAW_ECN_CENT";
			}

			@Override
			public String getLiveAccountName() {
				return "Raw ECN Cent";
			}
		},
		MTS_ISLAMIC_ECN_CENT {
			@Override
			public String getAccountTypeCode() {
				return "108";
			}

			@Override
			public String getTestId() {
				return "ISLAMIC_ECN_CENT";
			}

			@Override
			public String getLiveAccountName() {
				return "Swap-free ECN Cent";
			}
		};

		public abstract String getAccountTypeCode();
		public abstract String getTestId();
		public abstract String getLiveAccountName();

		private static final Map<PLATFORM, List<ACCOUNTTYPE>> accountTypeMap =
				Map.of(
						PLATFORM.MT4, List.of(STANDARD_STP, RAW_ECN, ISLAMIC_STP, ISLAMIC_ECN, PAMM_INVESTOR, PREMIUM_STP, PERPETUAL),
						PLATFORM.MT5, List.of(HEDGE_STP, HEDGE_ECN, MT5_ISLAMIC_STP, MT5_ISLAMIC_ECN, MT5_PERPETUAL, MT5_TRADING_VIEW_STP, MT5_TRADING_VIEW, MT5_STANDARD_STP_CENT, MT5_ISLAMIC_STP_CENT, MT5_RAW_ECN_CENT, MT5_ISLAMIC_ECN_CENT, MT5_PAMM),
						PLATFORM.MTS, List.of(MTS_HEDGE_STP, MTS_HEDGE_ECN, MTS_ISLAMIC_STP, MTS_ISLAMIC_ECN, MTS_STANDARD_STP_CENT, MTS_ISLAMIC_STP_CENT, MTS_RAW_ECN_CENT, MTS_ISLAMIC_ECN_CENT)
				);

		public static ACCOUNTTYPE getRecByTestId(String testId, PLATFORM platform) {
			List<ACCOUNTTYPE> accountTypeAllowed = accountTypeMap.getOrDefault(platform, List.of());

			for (ACCOUNTTYPE type : accountTypeAllowed) {
				if (testId.equalsIgnoreCase(type.getTestId())) {
					return type;
				}
			}
			return null;
		}

		public static ACCOUNTTYPE getRecByAccTypeCode(String accTypeCode, PLATFORM platform) {
			List<ACCOUNTTYPE> accountTypeAllowed = accountTypeMap.getOrDefault(platform, List.of());

			for (ACCOUNTTYPE type : accountTypeAllowed) {
				if (accTypeCode.equalsIgnoreCase(type.getAccountTypeCode())) {
					return type;
				}
			}
			return null;
		}

		public static ACCOUNTTYPE getRecByAccTypeDesc(String accTypeDesc, PLATFORM platform) {
			List<ACCOUNTTYPE> accountTypeAllowed = accountTypeMap.getOrDefault(platform, List.of());

			// Compare length and description
			for (ACCOUNTTYPE type :
					accountTypeAllowed.stream()
							.sorted((a, b) -> Integer.compare(b.getLiveAccountName().length(), a.getLiveAccountName().length()))
							.toList()) {

				if (accTypeDesc.toLowerCase().contains(type.getLiveAccountName().toLowerCase())) {
					return type;
				}
			}

			return null;
		}
	}

	public static enum STATUS {
		INCOMPLETE, FAILED, SUCCESSFUL, PROCESSING, SUBMITTED, ACTIVE,REJECTED,PAID, PENDING, PROCESSED
	}

	public static enum ACC_STATUS {
		ALL, ACTIVE, INACTIVE, PROCESSING, REJECTED, PENDING, ARCHIVED;

		public String getStatusDesc() {
			return this.name();
		}

		public static ACC_STATUS getRecByStatusDesc(String statusDesc) {
			for (ACC_STATUS status : values()) {
				if (statusDesc.equalsIgnoreCase(status.getStatusDesc())) {
					return status;
				}
			}
			return null;
		}
	}

	public static enum USERTYPE{
		Individual,Company
	}


	public static enum PROMOTION{
		DEPOSITBONUS {
			@Override
			public Integer getCampaignType(ENV env) {
				// TODO Auto-generated method stub
				if(env.equals(ENV.ALPHA)) {
					if(brand.equalsIgnoreCase("vfx") || brand.equalsIgnoreCase("pug") || brand.equalsIgnoreCase("star")) {
						return 6;
					}else if(brand.equalsIgnoreCase("vt")) {
						return 5;
					}else {
						return -1;
					}
				}
				return -1;
			}
		},
		RAF{

			@Override
			public Integer getCampaignType(ENV env) {
				// TODO Auto-generated method stub
				if(env.equals(ENV.ALPHA)) {
					if(brand.equalsIgnoreCase("vfx")) {
						return 9;
					}else if(brand.equalsIgnoreCase("vt")) {
						return 5;
					}else if(brand.equalsIgnoreCase("pug")) {
						return 12;
					}else {
						return -1;
					}
				}
				return -1;
			}

		},
		TRADINGBONUS {
			@Override
			public Integer getCampaignType(ENV env) {
				// TODO Auto-generated method stub
				return 11;//PUG
			}
		}
		;

		public abstract Integer getCampaignType(ENV env);//for DPB： type，for others： campaign id
	}


	public enum PTPaymentMethod {
		CreditCard
				{
					public Integer getPaymentType() {
						// TODO Auto-generated method stub
						return 1;
					}

					public Integer getPaymentChannel() {
						// TODO Auto-generated method stub
						return 8;
					}
				},
		CryptoBitCOIN{
			public Integer getPaymentType() {
				// TODO Auto-generated method stub
				return 15;
			}

			public Integer getPaymentChannel() {
				// TODO Auto-generated method stub
				return 6;
			}
		},
		CryptoUSDTERC{
			public Integer getPaymentType() {
				// TODO Auto-generated method stub
				return 15;
			}

			public Integer getPaymentChannel() {
				// TODO Auto-generated method stub
				return 3;
			}
		},
		CryptoUSDTTRC{
			public Integer getPaymentType() {
				// TODO Auto-generated method stub
				return 15;
			}

			public Integer getPaymentChannel() {
				// TODO Auto-generated method stub
				return 7;
			}
		},
		CryptoETH{
			public Integer getPaymentType() {
				// TODO Auto-generated method stub
				return 15;
			}

			public Integer getPaymentChannel() {
				// TODO Auto-generated method stub
				return 8;
			}
		},
		CryptoUSDCERC{
			public Integer getPaymentType() {
				// TODO Auto-generated method stub
				return 15;
			}

			public Integer getPaymentChannel() {
				// TODO Auto-generated method stub
				return 9;
			}
		},
		Ewallet{
			public Integer getPaymentType() {
				// TODO Auto-generated method stub
				return null;
			}

			public Integer getPaymentChannel() {
				// TODO Auto-generated method stub
				return null;
			}
		},
		EwalletPerfectMoney{
			public Integer getPaymentType() {
				// TODO Auto-generated method stub
				return 49;
			}

			public Integer getPaymentChannel() {
				// TODO Auto-generated method stub
				return 10;
			}
		},
		EwalletSTICPAY{
			public Integer getPaymentType() {
				// TODO Auto-generated method stub
				return 17;
			}

			public Integer getPaymentChannel() {
				// TODO Auto-generated method stub
				return 2;
			}

		},
		EwalletNeteller{
			public Integer getPaymentType() {
				// TODO Auto-generated method stub
				return 11;
			}

			public Integer getPaymentChannel() {
				// TODO Auto-generated method stub
				return 2;
			}
		},
		EwalletSkrill{
			public Integer getPaymentType() {
				// TODO Auto-generated method stub
				return 21;
			}

			public Integer getPaymentChannel() {
				// TODO Auto-generated method stub
				return 3;
			}
		};

		public abstract Integer getPaymentType();
		public abstract Integer getPaymentChannel();
	}

	public enum WalletDepositMethod {

		CryptoBTC {
			public String getCryptoCurrencyDisplayDesc() {
				return "BTC";
			}

			public String getCryptoNetworkDisplayDesc() {
				return "Bitcoin";
			}
		},
		CryptoETH {
			public String getCryptoCurrencyDisplayDesc() {
				return "ETH";
			}

			public String getCryptoNetworkDisplayDesc() {
				return "ERC20";
			}
		},
		CryptoUSDC {
			public String getCryptoCurrencyDisplayDesc() {
				return "USDC";
			}

			public String getCryptoNetworkDisplayDesc() {
				return "ERC20";
			}
		},
		CryptoUSDT_ERC {
			public String getCryptoCurrencyDisplayDesc() {
				return "USDT";
			}

			public String getCryptoNetworkDisplayDesc() {
				return "ERC20";
			}
		},
		CryptoUSDT_TRC {
			public String getCryptoCurrencyDisplayDesc() {
				return "USDT";
			}

			public String getCryptoNetworkDisplayDesc() {
				return "TRC20";
			}
		};

		public abstract String getCryptoCurrencyDisplayDesc();
		public abstract String getCryptoNetworkDisplayDesc();

	}

	public enum WalletWithdrawMethod {

		CryptoBTC {
			public String getCryptoCurrencyDisplayDesc() {
				return "BTC";
			}

			public String getCryptoNetworkDisplayDesc() {
				return "Bitcoin";
			}
		},
		CryptoETH {
			public String getCryptoCurrencyDisplayDesc() {
				return "ETH";
			}

			public String getCryptoNetworkDisplayDesc() {
				return "ERC20";
			}
		},
		CryptoUSDC {
			public String getCryptoCurrencyDisplayDesc() {
				return "USDC";
			}

			public String getCryptoNetworkDisplayDesc() {
				return "ERC20";
			}
		},
		CryptoUSDT_ERC {
			public String getCryptoCurrencyDisplayDesc() {
				return "USDT";
			}

			public String getCryptoNetworkDisplayDesc() {
				return "ERC20";
			}
		},
		CryptoUSDT_TRC {
			public String getCryptoCurrencyDisplayDesc() {
				return "USDT";
			}

			public String getCryptoNetworkDisplayDesc() {
				return "TRC20";
			}
		};

		public abstract String getCryptoCurrencyDisplayDesc();
		public abstract String getCryptoNetworkDisplayDesc();

	}

	// deposit url
	public final static String SKRILLURL = "skrill.com";
	public final static String STICPAYURL = "sticpay.com";
	public final static String NETELLERURL = "neteller.com";
	public final static String FASAPAYURL = "sci.fasapay.com";
	public final static String ZotapayURL = "secure.clients.fund";
	public final static String EeziePayURL = "jojostreet616";
	public final static String MALAYPAYTRUSTURL = "paytrust88";//Malaysia
	public final static String ASTROPAYURL = "astropay.com";
	public final static String BRIDGEPAYURL = "bridgePaymentDeposit";
	public final static String CRYPTOURL = "trustworthypay";
	public final static String THAIZotaURL = "secure.clients.fund";
	public final static String THAIEeziePayURL = "kainetmart128"; //Thailand,Vietnam
	public final static String THAIPayToDayURL = "thailandPayToDay";
	public final static String PaymentAsiaURL = "payment.pa-sys.com";
	public final static String XpayURL = "transfer1515";
	public final static String UNIONPAYURL = "vfnbqt.customspms.com";
	public final static String MOBILEPAYURL = "gateway.wangming.digital";
	public final static String SDPayURL = "payment.asiapaygateway";
	public final static String PagsmileURL = "pagsmile";
	public final static String PaystackURL = "checkout.paystack.com";
	public final static String Transact365URL = "api.plusdebit.com";
	public final static String MalaysiaEwalletURL = "pgw-ui.2c2p.com";
	public final static String BitwalletURL = "secure.bitwallet.com";
	public final static String SwiffyURL = "payments.swiffy.me";
	public final static String WowspayURL = "cashier.y9pay.xyz";
	public final static String MyFatoorahURL = "portal.myfatoorah.com";
	public final static String AppleGooglePayURL = "worldpay.com";
	public final static String EbuyURL = "pay.ebuycompany.com";
	public final static String PaywonURL = "paywon";
	public final static String WowsPay_QRURL = "tianciv380918.com";
	public final static String ON4_PIX_URL = "api.4on.me";
	public final static String KorapayURL = "korapay";
	public final static String Bitolo_PIX_URL = "bzpay.bitolo";
	public final static String Toppay_PIX_URL = "h5pay.v2.toppay.cc";
	public final static String Tygapay = "payme.tygapay.com";
	public final static String AdvcashURL = "wallet.advcash.com";
	public final static String WalaoPayURL = "realfx188.com";

	public final static String ApplePayGooglePay = "payments.worldpay.com";

	//Register url
	//public final static String ALPHAMOURL= "https://monamarketsdev:147258@monamarketsdev.wpengine.com/open-live-account";
	public final static String PRODMOURL = "https://www.monetamarkets.com/open-live-account";

	//public final static String ALPHAURL = "https://newvantagefx:987456@new.vantagefx.com";
//	public final static String ALPHAURL = "https://testregmarkets:147258@testreg.marketsdata1.com";
	public final static String ALPHAURL = "https://testregmarkets:147258@testreg.marketsdata1.com/unity";

	public final static String ALPHA_DEMO_VFX_VFSC_URL = "https://www.vantagemarkets.com/open-demo-account-crm";
	public final static String ALPHA_DEMO_VFX_ASIC_URL = "https://www.vantagemarkets.com/en-au/open-demo-account-crm";
	public final static String ALPHA_DEMO_VFX_FCA_URL = "https://www.vantagemarkets.co.uk/open-demo-account-crm";
	public final static String ALPHA_DEMO_VT_URL = "https://www.vtmarkets.com/bit-demo-test";
	public final static String ALPHA_DEMO_PU_URL = "https://www.puprime.com/demo-account-test";
	public final static String ALPHA_DEMO_STAR_SVG_URL = "https://www.startrader.com/demo-account-crm";
	public final static String ALPHA_DEMO_STAR_ASIC_URL = "https://www.startraderprime.com.au/demo-account-crm";
	public final static String ALPHA_DEMO_UM_URL = "https://www.ultimamarkets.com/free-demo-account-crm";
	public final static String ALPHA_DEMO_VJP_URL = "https://www.vantagetradings.com/en/open-demo-account-crm";
	public final static String ALPHA_DEMO_MO_URL = "https://www.monetamarkets.com/open-demo-account-crm";

	public final static String VFXALPHAURL = "https://www.vantagemarkets.com/open-live-account-test-20230510/?ppwp=1";
	public final static String ALPHAPUGSVGURL = "https://caprimecom1dev.wpengine.com/live-account-test/";

	public final static String ALPHAPUGPTSVGURL = "https://www.puxtrader.com/prop-trading-registration-test";
	public final static String PRODFCAURL = "https://www.vantagemarkets.co.uk";
	public final static String PRODVFXURL = "https://www.vantagemarkets.com/open-live-account";
	public final static String PRODASICURL = "https://www.vantagemarkets.com/en-AU";
	public final static String PRODCIMACURL = "";
	public final static String PRODVTURL = "https://www.vtmarkets.com";

	//public final static String PRODVJPURL = "https://www.vantagetradings.com";
	public final static String PRODVJPURL ="https://www.vantagetradings.com/open-live-account/?affid=NBMjY2NjE3NA==";

	public final static String PRODFSAURL = "https://www.puprime.com";
	//public final static String PRODSVGURL = "https://www.puprime.online";
	public final static String PRODSVGURL = "https://www.puprime.online/forex-trading-account";

	public final static String PRODPPASIC = "https://www.prosperomarkets.com";
	//public final static String PRODUMURL = "https://www.ultimamarkets.com";
	//public final static String PRODUMURL = "https://www.ultimamarkets.asia/";
	public final static String PRODUMURL = "https://www.ultimamarkets.asia/accounts/open-live-account";
	public final static String PRODKCMURL = "https://www.klimexcm.com";
	public final static String PRODATURL = "https://www.alphatick.com";

	public final static String PRODSTARURL = "https://www.startrader.com/live-account";

    public final static String TESTENV_LIVEACC_REG_URL = "https://www.vantagemarkets.com/open-live-account-crm/";

    //CPA Registration URL
    public final static String TESTENV_CPA_REG_URL = "https://partners.vantagemarkets.com/cpa-register-crm/?affid=NBMTAwNjExODI=";

    //IB Program Registration URL
    public final static String TESTENV_IBPROGRAM_REG_URL = "https://partners.vantagemarkets.com/ib-cpa-register-acr-2806/?affid=MTE2NDAwMzI=";

    //Teams notification channels
	public final static String AUTOMATION_UpdateGroups_webhookUrl = "https://hytechconsult.webhook.office.com/webhookb2/737242f0-84b4-45d9-a298-4ccd1dbfd7eb@a6845a69-8e54-4e81-bb36-90f7c49ebbdc/IncomingWebhook/e6271e823f884309aa961394e35d6dff/48ea6e0b-fc99-4266-a2a5-5599fae5f372";
	public final static String AUTOMATION_SendCloudBalanceAlarm_webhookUrl = "https://open.larksuite.com/open-apis/bot/v2/hook/ec01d7ba-786b-47f3-87e4-b153203af917";
	public final static String AUTOMATION_SendCloud_Daily_webhookUrl = "https://open.larksuite.com/open-apis/bot/v2/hook/8f09d558-ebdf-40c7-a754-a8262b232ee9";
	public final static String AUTOMATION_CryptoW_Monitor_webhookUrl = "https://hytechconsult.webhook.office.com/webhookb2/737242f0-84b4-45d9-a298-4ccd1dbfd7eb@a6845a69-8e54-4e81-bb36-90f7c49ebbdc/IncomingWebhook/a45eb286de5344c2a7a65cd544814d2f/48ea6e0b-fc99-4266-a2a5-5599fae5f372";
	public final static String AUTOMATION_QA_Team_webhookUrl = "https://hytechconsult.webhook.office.com/webhookb2/0d15a7f6-8447-43f6-b8fb-49528db1d47f@a6845a69-8e54-4e81-bb36-90f7c49ebbdc/IncomingWebhook/282a45c6e0574d4bb656e364849cf7dc/48ea6e0b-fc99-4266-a2a5-5599fae5f372";
	public final static String AUTOMATION_CPS_Monitor_webhookUrl = "https://hytechconsult.webhook.office.com/webhookb2/737242f0-84b4-45d9-a298-4ccd1dbfd7eb@a6845a69-8e54-4e81-bb36-90f7c49ebbdc/IncomingWebhook/38fff9efa63d43988cbb1be89275cb8c/48ea6e0b-fc99-4266-a2a5-5599fae5f372";
	public final static String AUTOMATION_Lark_CPS_Monitor_webhookUrl = "https://open.larksuite.com/open-apis/bot/v2/hook/ded4df9f-b188-46e5-b6bf-7bb9dd74a965";
	public final static String AUTOMATION_Lark_RTS_CPS_Monitor_webhookUrl = "https://open.larksuite.com/open-apis/bot/v2/hook/e5845894-ed8e-4724-82e9-0ab6b455fc22";
	public final static String AUTOMATION_Lark_Automation_Test_webhookUrl = "https://open.larksuite.com/open-apis/bot/v2/hook/053257c6-a1e3-4bc7-8400-db9c9c8eb9b5";
	public final static long WAITTIME = 90;// driver wait time, seconds


	public static boolean debug = true;

	public static boolean isWeb = true;


}

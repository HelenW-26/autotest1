package newcrm.utils.encryption;

/**
 * Xpay provided class to encrypt and decrypt string
 * @author copy from excalibur
 *
 */
public class XpayDataEncrypt
{
	private static String[] strSplit = new String[] { "", "g", "h", "G", "k", "g", "J", "K", "I", "h", "i", "j", "H" };
	private static String sP = ""+"g|h|G|k|g|J|K|I|h|i|j|H";
	private static int n = 0;
//	private  static String strTemp = "";
	public XpayDataEncrypt()
	{
	}

	public static final String EncryptData(String ToEncrypt) //加密
	{
		String strTemp = "";
		if (!ToEncrypt.equals("") && ToEncrypt != null)
		{
			byte[] temp = ToEncrypt.replace("\r\n", "").getBytes();
			for (int i = 0; i < temp.length; i++)
			{
				int number = Integer.parseInt((new Byte(temp[i])).toString()); //转为ASCII码
				strTemp = strTemp + EncryptToInt16(number);
			}
		}
		return strTemp;
	}

	private static String EncryptToInt16(int number) //转为十六进制
	{
		//if (n == 12)
		//    n = 0;
		//n++;
		//return Convert.ToString(number, 16) + strSplit[n];
		if (n == 12)
		{
			n = 1;
		}
		else
		{
			n++;
		}

		String mychar = "H";
		try
		{
			mychar = strSplit[n];
		}
		catch (java.lang.Exception e)
		{
			n = 1;
		}
		
		return Integer.toHexString(number) + mychar;

	}

	public static final String DecryptData(String ToDecrypt) //解密
	{
		String strTemp = "";
		if (!ToDecrypt.equals("") && ToDecrypt != null)
		{
			String[] str = ToDecrypt.replace("\r\n", "").split(sP);

			for (int i = 0; i < str.length; i++)
			{
				strTemp = strTemp + EncryptToString(str[i].toString());
			}
		}

		return strTemp;
	}

	private static String EncryptToString(String number) //先转为ASCII码在转为字符
	{
		return String.valueOf((char)Integer.parseInt(number,16));
	}

	public static final String GetQueryString(String strArgName, String strUrl) //分割URL字符串
	{
		if (!strUrl.equals("") && strUrl != null)
		{
			//strUrl = strUrl.Replace("?", "&");
			String strArgValue = "";
			String[] strList = strUrl.split("[&]", -1);
			int intCount = strList.length;
			for (int i = 0; i < intCount; i++)
			{
				int intPos = strList[i].toString().indexOf("=");
				if (intPos == -1)
				{
					continue;
				}
				String strListArgName = strList[i].toString().substring(0, intPos);
				if (strArgName.equals(strListArgName))
				{
					strArgValue = strList[i].toString().substring(intPos + 1);
				}
			}
			return strArgValue;
		}
		else
		{
			return "";
		}
	}
	
	public static void main(String args[]){
//		XpayDataEncrypt de = new XpayDataEncrypt();
		System.out.println(EncryptData("RefID=VU1675720220617001728&Curr=USD&Amount=100.00&Status=002&TransID=2020082573012178&ValidationKey=29251126&EncryptText=03E7690E61D9B4862D17D09431C13D77"));
		
		 //String url = DecryptData("52h65k66J49I44i3dH43h41k32J30I30i31H30h30k38J32I30i32H30h30k38J32I35i30H36h34k32J31I35i26H43h75k72J72I3di56H4eh44k26J41I6di6fH75h6ek74J3dI31i34H31h35k34J30I30i2eH30h30k26J53I74i61H74h75k73J3dI30i30H32h26k54J72I61i6eH73h49k44J3dI32i30H32h30k30J38I32i35H37h33k30J31I32i31H37h38k26J56I61i6cH69h64k61J74I69i6fH6eh4bk65J79I3di32H39h32k35J31I31i32H36h26k45J6eI63i72H79h70k74J54I65i78H74h3dk30J33I45i37H36h39k30J45I36i31H44h39k42J34I38i36H32h44k31J37I44i30H39h34k33J31I43i31H33h44k37J37I");
//		 String  RefID = GetQueryString("RefID", url);
		//System.out.println(url);
	}

}
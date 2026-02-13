package newcrm.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import newcrm.utils.callback.*;

public class DepositCallBack {

	//定义callback，以及指定实现的类
	public enum CHANNEL{
		BRIDGE_CARDPAY
		{
			@Override
			public String getClassName() {
				// TODO Auto-generated method stub
				return BridgeCardPay.class.getName();
			}
		},
		WORLDCARD{

			@Override
			public String getClassName() {
				// TODO Auto-generated method stub
				return WorldCard.class.getName();
			}
			
		},
		CARDPAY{

			@Override
			public String getClassName() {
				// TODO Auto-generated method stub
				return CardPay.class.getName();
			}
			
		},
		XPAY{
			@Override
			public String getClassName() {
				// TODO Auto-generated method stub
				return Xpay.class.getName();
			}
			
		},
		POWERCASH {
			@Override
			public String getClassName() {
				// TODO Auto-generated method stub
				return PowerCash.class.getName();
			}
		},
		SOLIDPAY{
			@Override
			public String getClassName() {
				// TODO Auto-generated method stub
				return SolidPay.class.getName();
			}
		},
		CREDITCARD_VIRTUALPAY {
			@Override
			public String getClassName() {
				// TODO Auto-generated method stub
				return CreditCardVirtualPay.class.getName();
			}
		},
		ZOTAPAY{
			@Override
			public String getClassName() {
				// TODO Auto-generated method stub
				return ZotaPay.class.getName();
			}
			
		},
		SKRILLPAY {
			@Override
			public String getClassName() {
				// TODO Auto-generated method stub
				return SkrillPay.class.getName();
			}
		},
		FASAPAY {
			@Override
			public String getClassName() {
				// TODO Auto-generated method stub
				return FasaPay.class.getName();
			}
		},
		STICPAY {
			@Override
			public String getClassName() {
				// TODO Auto-generated method stub
				return SticPay.class.getName();
			}
		},
		NETELLERPAY{
			@Override
			public String getClassName() {
				// TODO Auto-generated method stub
				return null;
			}
			
		},
		ASTROPAY {
			@Override
			public String getClassName() {
				// TODO Auto-generated method stub
				return AstroPay.class.getName();
			}
		},
		BRAZIL_PAGSMILE {
			@Override
			public String getClassName() {
				// TODO Auto-generated method stub
				return BrazilePagsmile.class.getName();
			}
		},
		//add following after jenkins
		PERFECTMONEY {
			@Override
			public String getClassName() {
				// TODO Auto-generated method stub
				return PerfecMoney.class.getName();
			}
		},
		BITWALLET {
			@Override
			public String getClassName() {
				// TODO Auto-generated method stub
				return Bitwallet.class.getName();
			}
		},
		EEZIEPAY {
			@Override
			public String getClassName() {
				// TODO Auto-generated method stub
				return EeziePay.class.getName();
			}
		},
		USDTERC{

			@Override
			public String getClassName() {
				// TODO Auto-generated method stub
				return UsdtErcPay.class.getName();
			}
			
		},
		USDTTRC{

			@Override
			public String getClassName() {
				// TODO Auto-generated method stub
				return UsdtTrcCpsPay.class.getName();
			}
			
		},
		UNIONCPS {
			@Override
			public String getClassName() {
				// TODO Auto-generated method stub
				return UnionCPSPay.class.getName();
			}
		},
		MOBILECPS {
			@Override
			public String getClassName() {
				// TODO Auto-generated method stub
				return MobileCPSPay.class.getName();
			}
		},
		EBUY {
			@Override
			public String getClassName(){
				return Ebuy.class.getName();
			}
		},
		Paystack {
			@Override
			public String getClassName() {
				// TODO Auto-generated method stub
				return Paystack.class.getName();
			}
			},
		PTCallBack {
			@Override
			public String getClassName() {
				// TODO Auto-generated method stub
				return PTCallBack.class.getName();
			}
		},
		CPSCallback
				{
					@Override
					public String getClassName() {
						// TODO Auto-generated method stub
						return CPSCallback.class.getName();
					}

		};
		public abstract String getClassName();
	}
	
	//brand
	private String brand;
	private String regulator;
	private String url;
	
	private CallbackBase callback;
	/***
	 * 
	 * @param url open-api
	 * @param brand
	 * @param regulator
	 */
	public DepositCallBack(String url,String brand,String regulator) {
		if(url.contains("https")) {
			this.url=url+"/";
		}else {
			this.url ="https://"+ url+"/";
		}
		this.brand = brand;
		this.regulator = regulator;
	}
	
	/***
	 *  根据channel，调用不同类的构造函数，创建对应channel的instance，生成对应的callback request
	 * @param channel
	 * @param orderNum
	 * @param currency
	 * @param amount
	 * @param first6num
	 * @param last4num
	 * @param year
	 * @param month
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void generateCallback(CHANNEL channel,String orderNum, String currency, Double amount,String first6num,String last4num,String year,String month) {
		try {
			Class cls = Class.forName(channel.getClassName());
			Constructor constructor = cls.getConstructor();
			callback = (CallbackBase) constructor.newInstance();
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		callback.setEnv(url, brand, regulator);
		callback.setPaymentInfo(orderNum, currency, amount, first6num, last4num, year, month);
		//Method method = cls.getMethod("test", String.class,String.class);
		
	}
	public void generateCallback(CHANNEL channel,String orderNum, String currency, String amount,String first6num,String last4num,String year,String month) {
		try {
			Class cls = Class.forName(channel.getClassName());
			Constructor constructor = cls.getConstructor();
			callback = (CallbackBase) constructor.newInstance();
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		callback.setEnv(url, brand, regulator);
		callback.setPaymentInfo(orderNum, currency, amount, first6num, last4num, year, month);
		//Method method = cls.getMethod("test", String.class,String.class);

	}

	public void generatePTCallback(CHANNEL channel,String orderNum, String currency, Double amount,String first6num,String last4num,String year,String month) {
		try {
			Class cls = Class.forName(channel.getClassName());
			Constructor constructor = cls.getConstructor();
			callback = (CallbackBase) constructor.newInstance();
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		callback.setEnv(url, brand, regulator);
		callback.setPaymentInfo(orderNum, currency, amount, first6num, last4num, year, month);
		//Method method = cls.getMethod("test", String.class,String.class);

	}

	
	/**
	 * 如果已经构造了callback，则发送
	 * @return
	 */
	public String sendCallback() {
		if(callback !=null) {
			return callback.sendCallback();
		}
		return null;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void test(CHANNEL channel) throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Class cls = Class.forName(channel.getClassName());
		
		Constructor constructor = cls.getConstructor();
		Method method = cls.getMethod("test", String.class,String.class);
		CallbackBase object = (CallbackBase) constructor.newInstance();
		method.invoke(object, channel.toString(),"second info");
	}
}

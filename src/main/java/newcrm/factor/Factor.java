package newcrm.factor;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import newcrm.global.GlobalMethods;
import org.openqa.selenium.WebDriver;

import newcrm.global.GlobalProperties;
import utils.LogUtils;

public class Factor {
	private String brand;
	private WebDriver driver;
	private String TestEnv;
	private String Regulator;
	private String host;
	
	private FactorEnum specific;
	private FactorEnum common;
	
	/***
	 * 
	 * @param TestEnv   prod or alpha
	 * @param Regulator
	 * @param brand     PUG,VT,VFX,MO,UM,AT
	 * @param driver
	 */
	public Factor(String TestEnv, String brand, String Regulator, WebDriver driver) {
		this.brand = brand.trim().toLowerCase();
		this.TestEnv = TestEnv.trim().toLowerCase();
		this.Regulator = Regulator.trim().toLowerCase();
		this.driver = driver;
		this.specific = FactorEnum.getFactorEnum(brand, Regulator, TestEnv);
		this.common = FactorEnum.getBrandFactorEnum(brand);
	}

	//For API tests
	public Factor(String TestEnv, String brand, String Regulator, String host) {
		this.brand = brand.trim().toLowerCase();
		this.TestEnv = TestEnv.trim().toLowerCase();
		this.Regulator = Regulator.trim().toLowerCase();
		this.host = host.trim().toLowerCase();
		this.specific = FactorEnum.getFactorEnum(brand, Regulator, TestEnv);
		this.common = FactorEnum.getBrandFactorEnum(brand);
	}
	

	@SuppressWarnings("unchecked")
	public <T> T newInstance(Class<T> a,String...args){
		//查找指定监管和环境下是否有特殊要求
		LogUtils.info("工厂类实例化类为: " + this.specific.name());
		Class [] cls = this.specific.getCls();
		Class<T> result = null;
		for(Class c:cls) {
			if(a.isAssignableFrom(c)) {
				result = c;
				LogUtils.info("工厂类实例化类为: " + result.getName());
				break;
			}
		}
		//查找global
		if(result == null) {
			cls = this.common.getCls();
			for(Class c:cls) {
				if(a.isAssignableFrom(c)) {
					result = c;
					LogUtils.info("工厂类实例化类为: " + result.getName());
					break;
				}
			}
		}	
		//如果都没有，使用共用business类
		if(result == null) {
			result = a;
			LogUtils.info("工厂类实例化类为: " + result.getName());
		}

		Constructor constructor;
		//处理3个特殊的构造函数，注册和login需要带有url
		if(result.getName().toLowerCase().contains("apiregister")) {
			try {
				constructor = result.getConstructor(String.class,String.class,String.class,String.class);
				Object obj = constructor.newInstance(this.host,this.getRegistrationUrl(this.specific), this.Regulator, this.brand);
				return result.cast(obj);
			} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if(result.getSimpleName().toLowerCase().contains("registerdemo")) {
			try {
				constructor = result.getConstructor(WebDriver.class,String.class);
				Object obj = constructor.newInstance(driver,this.getDemoRegistrationUrl(this.specific));
				return result.cast(obj);
			} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if(result.getName().toLowerCase().contains("register")) {
			try {
				constructor = result.getConstructor(WebDriver.class,String.class);
				Object obj = constructor.newInstance(driver,this.getRegistrationUrl(this.specific));
				return result.cast(obj);
			} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// .getSimpleName() instead of .getName() to avoid checking package names.
		if(result.getSimpleName().toLowerCase().contains("login")) {
			if(args==null || args.length==0) {
				return null;
			}

			try {
				constructor = result.getConstructor(WebDriver.class,String.class);
				Object obj = constructor.newInstance(driver,args[0]);
				LogUtils.info("Login Page Url: " + driver.getCurrentUrl());
				return result.cast(obj);
			} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		try {
			constructor = result.getConstructor(WebDriver.class);
			Object obj = constructor.newInstance(driver);
			return result.cast(obj);
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	private String getRegistrationUrl(FactorEnum f) {
		switch(f) {
		//at
		case AT_SVG_PROD: return GlobalProperties.PRODATURL;
		//um
		case UM_SVG_PROD: return GlobalProperties.PRODUMURL;
		//prospero
		case PROSPERO_ASIC_PROD:
		case PROSPERO_SVG_PROD: return GlobalProperties.PRODPPASIC;
		//moneta
		case MO_VFSC_PROD: return GlobalProperties.PRODMOURL;
		//kcm
		case KCM_SVG_PROD: return GlobalProperties.PRODKCMURL;
		//vfx
		case VFX_VFSC_PROD:
		case VFX_VFSC2_PROD: return GlobalProperties.PRODVFXURL;
		case VFX_ASIC_PROD: return GlobalProperties.PRODASICURL;
		case VFX_FCA_PROD: return GlobalProperties.PRODFCAURL;
		case VFX_CIMA_PROD: return GlobalProperties.PRODCIMACURL;
		//case VFX_VFSC2_ALPHA: return GlobalProperties.VFXALPHAURL;
		//pug
		case PUG_SVG_PROD: return GlobalProperties.PRODSVGURL;
		case PUG_FSA_PROD: return GlobalProperties.PRODFSAURL;
		//case PUG_SVG_ALPHA: return GlobalProperties.ALPHAPUGSVGURL;
		
		//vt
		case VT_SVG_PROD:
		case VT_CIMA_PROD: return GlobalProperties.PRODVTURL;

		//star
		case STAR_SVG_PROD: return GlobalProperties.PRODSTARURL;

		//vjp
		case VJP_SVG_PROD: return GlobalProperties.PRODVJPURL;


		//pug-pt
		case PUGPT_SVG_ALPHA: return GlobalProperties.ALPHAPUGPTSVGURL;

//		// AU alpha temp registration url. Wait 17.11 all brand 双端统一 go live then remove
//		case VFX_VFSC_ALPHA, VFX_VFSC2_ALPHA, VFX_VFSC_UAT, VFX_VFSC2_UAT: return "https://www.vantagemarkets.com/open-live-account-crm";
//		case VFX_ASIC_ALPHA, VFX_ASIC_UAT: return "https://www.vantagemarkets.com/en-au/open-live-account-crm";

		default:
			return GlobalProperties.ALPHAURL;
		}
		
	}

	private String getDemoRegistrationUrl(FactorEnum f) {

		return switch (f) {
			// VFX
			case VFX_VFSC_ALPHA, VFX_VFSC2_ALPHA, VFX_VFSC_UAT, VFX_VFSC2_UAT -> GlobalProperties.ALPHA_DEMO_VFX_VFSC_URL;
			case VFX_ASIC_ALPHA, VFX_ASIC_UAT -> GlobalProperties.ALPHA_DEMO_VFX_ASIC_URL;
			case VFX_FCA_ALPHA, VFX_FCA_UAT -> GlobalProperties.ALPHA_DEMO_VFX_FCA_URL;
			// VT
			case VT_SVG_ALPHA, VT_SVG_UAT -> GlobalProperties.ALPHA_DEMO_VT_URL;
			// PU
			case PUG_SVG_ALPHA, PUG_SVG_UAT -> GlobalProperties.ALPHA_DEMO_PU_URL;
			// STAR
			case STAR_SVG_ALPHA, STAR_SVG_UAT -> GlobalProperties.ALPHA_DEMO_STAR_SVG_URL;
			case STAR_ASIC_ALPHA, STAR_ASIC_UAT -> GlobalProperties.ALPHA_DEMO_STAR_ASIC_URL;
			// UM
			case UM_SVG_ALPHA, UM_SVG_UAT -> GlobalProperties.ALPHA_DEMO_UM_URL;
			// VJP
			case VJP_SVG_ALPHA, VJP_SVG_UAT -> GlobalProperties.ALPHA_DEMO_VJP_URL;
			// MO
			case MO_VFSC_ALPHA, MO_VFSC_UAT -> GlobalProperties.ALPHA_DEMO_MO_URL;
			// Default
			default -> "";
		};

	}

}

package newcrm.utils;

import org.openqa.selenium.support.events.WebDriverListener;


/**
 * 构造一个带有监听器的webdriver，可以监听事件的发生。最新版本的Webdriver已不支持现有实现
 * @author FengLiu
 *
 */
public class MyWebDriverListener implements WebDriverListener {

	//private static final Logger logger = LoggerFactory.getLogger(MyWebDriverListener.class);

	public MyWebDriverListener() {
		//migrate log4j to log4j2
		/*String path = System.getProperty("user.dir");
		path = path + "//src//main//resources//log4j.properties";
		PropertyConfigurator.configure(path);*/
	}

}

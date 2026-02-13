package newcrm.global;

import io.qameta.allure.Allure;
import newcrm.utils.AlphaServerEnv;
import newcrm.utils.UATServerEnv;
import newcrm.global.GlobalProperties.PLATFORM;
import newcrm.global.GlobalProperties.ENV;
import newcrm.global.GlobalProperties.BRAND;
import org.apache.commons.lang3.RandomStringUtils;
import utils.LogUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class GlobalMethods {

	static String Brand = "";
	static String Server = "";
	static String Platform = "";

	public static String setServer(String server) {
		Server = server;
		return server;
	}
	public static String getServer() {
		System.out.println("Server is set to: " + Server);
		return Server;
	}
	public static String setPlatform(String platform) {
		Platform = platform;
		return Platform;
	}
	public static String getPlatform() {
		GlobalMethods.printDebugInfo("Platform is set to: " +  Platform);
		return Platform;
	}
	public static String setEnvValues(String brand) {
		//Convert different brand keywords on Jenkins
		if("ultima".equalsIgnoreCase(brand)) {
			brand = "UM";
		}
		if("moneta".equalsIgnoreCase(brand)) {
			brand = "MO";
		}
		if("au".equalsIgnoreCase(brand)) {
			brand = "VFX";
		}
		Brand = brand;		
		return brand;
	}
	public static String getBrand() { return Brand; }
	public static void printDebugInfo(String info) {
		if(GlobalProperties.debug) {
			LogUtils.info(info);
			String str = "*DEBUG INFO* " + info;
			Allure.addAttachment("DEBUG Log信息", "text/plain", str);
			/*System.out.println(str);*/
		}
	}

	/**
	 * Get test server id list
	 *
	 * @param platform	Platform
	 * @param testEnv	Testing Environment
	 * @param brand		Brand
	 * @return List of available test server id
	 */
	public static List<String> getTestServers(PLATFORM platform, ENV testEnv, BRAND brand) {
		List<String> servers = switch (platform) {
			case MT5 ->
					switch(testEnv) {
						case UAT -> UATServerEnv.getMT5TestServerIdByBrand(brand);
						default -> AlphaServerEnv.getMT5TestServerIdByBrand(brand);
					};
			case MT4 ->
					switch(testEnv) {
						case UAT -> UATServerEnv.getMT4TestServerIdByBrand(brand);
						default -> AlphaServerEnv.getMT4TestServerIdByBrand(brand);
					};
			case MTS ->
					switch(testEnv) {
						case UAT -> UATServerEnv.getMTSTestServerIdByBrand(brand);
						default -> AlphaServerEnv.getMTSTestServerIdByBrand(brand);
					};
		};

		LogUtils.info(String.format("%s available Test Servers IDs: %s", platform, servers.toString()));
		return servers;
	}

	/**
	 * Get registration aff id
	 *
	 * @param brand		Brand
	 * @param testEnv	Testing Environment
	 * @return Aff ID string value
	 */
	public static String getRegAffID(String brand, ENV testEnv)
	{
		return  switch (testEnv) {
			case ALPHA ->
					switch(brand.toLowerCase()) {
						case "vfx" -> "NBMTAxNTE3Nw==&am=";
						case "pug" -> "NBMTAwMDE5MDY=";
						case "vt" -> "NBMTAwMDIzNDc=&am=";
						case "um" -> "NBODMxMTc0&am=";
						case "mo" -> "NBMTI4Mg==&am=";
						case "star" -> "NBMTAxNTE3Nw==&am=";
						case "vjp" -> "NBMjY2NjQyMw==";
						default -> "";
					};
			case UAT ->
					switch(brand.toLowerCase()) {
						case "vfx" -> "NBMTAwNTk2MTU=";
						case "pug" -> "NBMTAwMDE5MDY=";
						case "vt" -> "NBMTAwMDIzNDc=&am=";
						case "um" -> "NBODMxMTc0&am=";
						case "mo" -> "NBMTI4Mg==&am=";
						case "star" -> "NBMTAxNTE3Nw==&am=";
						case "vjp" -> "NBMjY2NjQyMw==";
						default -> "";
					};
			case PROD ->
					switch(brand.toLowerCase()) {
				// Update affid owner
						case "vfx" -> "NBMjExOTQz&am="; //TestCRM Automation
						case "pug" -> "NBMTEwMTQw" + "&time=" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));//TestCRM Automation
						case "vt" -> "NBODA1NjI=";//TestCRM Automation
						case "um" -> "NBODMwNDI5";//TestCRM Automation
						case "mo" -> "NBNTY3MTc=";//TestCRM Automation
						case "star" -> "NBMjcxMjY0Mw==&am=";//TestCRM Automation
						default -> "";
					};
		};
	}

	/**
	 * 
	 * @param length the length of the final string
	 * @return a string of the specific length
	 */
	public static String getRandomString(int length) {
		String strings= "ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz"; 
	      String rstring ="";
	      for (int i=0; i<length ; i++) { 
	         Random randomGenerator = new Random(); 
	         int randomInt = randomGenerator.nextInt(strings.length()); 
	         rstring += strings.substring(randomInt,randomInt+1);
	      } 
	      return rstring ; 
	}
	
	public static String getRandomNumberString(int length) {
		String strings= "0123456789"; 
	      String rstring ="";
	      for (int i=0; i<length ; i++) { 
	         Random randomGenerator = new Random(); 
	         int randomInt = randomGenerator.nextInt(strings.length()); 
	         rstring += strings.substring(randomInt,randomInt+1);
	      } 
	      return rstring ; 
	}
	
	public static double getBalanceFromString(String origin) {
		origin = origin.replace(",", "");
		String regEx = "(([1-9][0-9]*)+(.[0-9]{1,2})?$)";
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(origin);
		String amount = "Nothing";
		if (matcher.find()) {
			amount = matcher.group(1);
		}
		
		return Double.valueOf(amount);
	}
	
	/**
	 * 
	 * @param regulator
	 * @param brand
	 * @return
	 */
	public static String getPreVBrand(String regulator, String brand) {
		String v_Brand = "";
		switch(regulator.toLowerCase().trim()) {
		case "cima": 
			if(brand.equalsIgnoreCase("VT")){
				v_Brand = "vt";
			}else {
				v_Brand = "ky";
			}
			break;
		case "svg": 
			if(brand.equalsIgnoreCase("PUG")){
				v_Brand = "svg";
			} 
			else if(brand.equalsIgnoreCase("UM")) {
				v_Brand = "um";
			}
			else {
				v_Brand = "vtsvg";
			}
			break;
		case "asic":
			v_Brand = "au"; break;
		case "vfsc":
			if(brand.equalsIgnoreCase("MO")) {
				v_Brand = "movfsc";
			}else {
				v_Brand = regulator.toLowerCase().trim();
			}
			break;
		default:
			v_Brand = regulator.toLowerCase().trim();
		}
		return v_Brand;
	}
	
	/***
	 * 
	 * @param value
	 * @return {"currency","amount"}
	 */
	public static List<String> getCurrencyAndAmount(String value){
		List<String> result = new ArrayList<String>();
		String currency = value.substring(value.lastIndexOf(" ") +1);
		result.add(currency);
		String t_balance = value.substring(0, value.lastIndexOf(" "));
		t_balance = t_balance.replace(",", "");
		String regEx = "(([1-9][0-9]*)+(.[0-9]{1,2})?$)";
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(t_balance);
		String balance = "0";
		if(matcher.find()) {
			balance = matcher.group(1);
		}
		result.add(balance);
		
		return result;
	}

	public static String generatePassword(){
		String upLetters = RandomStringUtils.random(3, 'A', 'Z', true, false);
		String loLetters = RandomStringUtils.random(3, 'a', 'z', true, false);
		String numLetters = RandomStringUtils.random(3, '0', '9', false, true);
		String specialLetters = RandomStringUtils.random(2, 36, 38, false, false);

		String pwd = upLetters
				.concat(loLetters)
				.concat(numLetters)
				.concat(specialLetters);

		GlobalMethods.printDebugInfo("Generate Password: " + pwd);
		Allure.addAttachment("DEBUG Log信息", "text/plain", "Generate Password: " + pwd);

		return pwd;
	}

	public static String fmtAmount(String amount) {
		// Handle amount > 1000 format comparison
		BigDecimal dAmount = new BigDecimal(amount);
		DecimalFormat formatter = new DecimalFormat("#,###");
		return formatter.format(dAmount);
	}

	public static String getCurrentDate(){
		LocalDate currentDate = LocalDate.now();
		String date = currentDate.toString();
		return date;
	}

	public static String dateDayStart() {
		LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return startOfDay.format(formatter);
	}

	public static String dateDayStart(long daysToSubtract) {
		LocalDateTime startOfDay = LocalDateTime.now().minusDays(daysToSubtract).withHour(0).withMinute(0).withSecond(0).withNano(0);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return startOfDay.format(formatter);
	}

	public static String dateDayEnd() {
		LocalDateTime endOfDay = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(0);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return endOfDay.format(formatter);
	}

	public static String dateDayEnd(long daysToAdd) {
		LocalDateTime endOfDay = LocalDateTime.now().plusDays(daysToAdd).withHour(23).withMinute(59).withSecond(59).withNano(0);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return endOfDay.format(formatter);
	}

	public static String setFromDateTime() {
		LocalDateTime dayOneMonthBefore = LocalDateTime.now().minusDays(30);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return dayOneMonthBefore.format(formatter);
	}

	public static String setToDateTime() {
		LocalDateTime todayDate = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return todayDate.format(formatter);
	}

	public static String setFromDateOnly() {
		LocalDate dayOneMonthBefore = LocalDate.now().minusDays(30);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		return dayOneMonthBefore.format(formatter);
	}

	public static String setFromDateOnly(long daysToSubtract) {
		LocalDate dayOneMonthBefore = LocalDate.now().minusDays(daysToSubtract);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		return dayOneMonthBefore.format(formatter);
	}

	public static String setToDateOnly() {
		LocalDate todayDate = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		return todayDate.format(formatter);
	}

	public static String setToDateOnly(long daysToAdd) {
		LocalDate todayDate = LocalDate.now().plusDays(daysToAdd);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		return todayDate.format(formatter);
	}

	public static String dateWeekAgo() {
		LocalDateTime threeMonthsAgo = LocalDateTime.now()
				.minusDays(7)
				.withHour(0)
				.withMinute(0)
				.withSecond(0)
				.withNano(0);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return threeMonthsAgo.format(formatter);
	}

	public static String getRegisterBrand(String regBrand) {
        return switch (regBrand) {
            case "VFX" -> "AU";
            case "MO" -> "MONETA";
            case "PUG", "VT", "VJP", "STAR", "UM" -> regBrand;
            default -> "";
        };
	}

	/**
	 * Find value by regular expression
	 *
	 * @param input     the original string
	 * @param regex     the regular expression used to search for
	 * @return Return full match when no capturing groups, otherwise return only capturing groups
	 */
	public static Optional<List<String>> findValueByRegex(String input, String regex) {
		Pattern pattern = Pattern.compile(regex, Pattern.UNICODE_CHARACTER_CLASS);
		Matcher matcher = pattern.matcher(input);

		if (matcher.find()) {
			List<String> groups = new ArrayList<>();

			if (matcher.groupCount() == 0) {
				// No capturing groups → return full match
				groups.add(matcher.group(0).trim());
			} else {
				// Return only capturing groups
				for (int i = 1; i <= matcher.groupCount(); i++) {
					groups.add(matcher.group(i).trim());
				}
			}

			return Optional.of(groups);
		}

		return Optional.empty();
	}

	/**
	 * Compare two numeric values
	 *
	 * @param expected      expected value
	 * @param actual        the original string
	 * @return boolean result when both result match
	 */
	public static boolean compareNumericValues(String expected, String actual) {
		// Null-safe, remove commas, trim
		expected = Objects.toString(expected, "").replace(",", "").trim();
		actual   = Objects.toString(actual, "").replace(",", "").trim();

		try {
			// Try numeric comparison
			BigDecimal b1 = new BigDecimal(expected);
			BigDecimal b2 = new BigDecimal(actual);
			return b1.compareTo(b2) == 0;
		} catch (NumberFormatException e) {
			// Not numeric → fallback to string comparison
			return expected.equalsIgnoreCase(actual);
		}
	}

	/**
	 * Check for duplicate text
	 *
	 * @param text      the original string
	 * @param word      the string to search for
	 * @return boolean result when there is more than one occurrence
	 */
	public static boolean checkDuplicateByRegex(String text, String word) {
		if (text == null || word == null || word.isEmpty()) {
			return false;
		}

		String regex = "\\b" + Pattern.quote(word) + "\\b";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(text);

		int count = 0;
		while (matcher.find()) {
			count++;
			if (count > 1) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Replaces all occurrences of search string in input, ignoring case.
	 *
	 * @param input       the original string
	 * @param search      the string to search for
	 * @param replacement the string to replace with
	 * @return modified string with case-insensitive replacements
	 */
	public static String replaceAllIgnoreCase(String input, String search, String replacement) {
		return input.replaceAll("(?i)" + Pattern.quote(search), replacement);
	}

}

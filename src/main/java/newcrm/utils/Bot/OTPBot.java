package newcrm.utils.Bot;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import vantagecrm.DBUtils;

/**
 * For fetching OTP code from DB
 * @author Alex Liu
 *
 */

public class OTPBot extends TelegramLongPollingBot {
	Update update;
	private static String Brand = "";
	private static String Regulator = "";
	
    @Override
    public void onUpdateReceived(Update update) {
    	String code = "";
    	
        // Check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText().toLowerCase();
            String chatId = update.getMessage().getChatId().toString();
            System.out.println(update.getMessage());

            try {
            	// Handle /start command
            	switch(messageText.toLowerCase()) {
                case "/start":
                    sendMenu(chatId);
                    break;
                // Handle main menu of brands
                case "vfx":
                    Brand = "VFX";
                    sendSubMenu1(chatId, "VFX");
                    break;
                case "vt":
                    Brand = "VT";
                    sendSubMenu1(chatId, "VT");
                    break;
                case "pug":
                    Brand = "PUG";
                    sendSubMenu1(chatId, "PUG");
                    break;
                case "mo":
                    Brand = "MO";
                    sendSubMenu1(chatId, "MO");
                    break;
                case "um":
                    Brand = "UM";
                    sendSubMenu1(chatId, "UM");
                    break;
                case "vjp":
                    Brand = "VJP";
                    sendSubMenu1(chatId, "VJP");
                    break;
                case "star":
                    Brand = "STAR";
                    sendSubMenu1(chatId, "STAR");
                    break;
                case "at":
                    Brand = "AT";
                    sendSubMenu1(chatId, "AT");
                    break;
                // Handle sub-menu options of regulators
                case "asic":
                	Regulator = "ASIC";
                    sendSubMenu2(chatId);
                    break;
                case "cima":
                	Regulator = "CIMA";
                    sendSubMenu2(chatId);
                    break;
                case "vfsc":
                	Regulator = "VFSC";
                    sendSubMenu2(chatId);
                    break;
                case "vfsc2":
                	Regulator = "VFSC2";
                    sendSubMenu2(chatId);
                    break;
                case "fca":
                	Regulator = "FCA";
                    sendSubMenu2(chatId);
                    break;
                case "fsa":
                	Regulator = "FSA";
                    sendSubMenu2(chatId);
                    break;
                case "svg":
                	Regulator = "SVG";
                    sendSubMenu2(chatId);
                    break;
                    
                // Handle third-level menu options
                case "alpha":
                    code = GetOTP("ALPHA", Brand, Regulator); 
                    sendTextMessage(chatId, code);	
                    break;
                case "prod":
                    code = GetOTP("PROD", Brand, Regulator); 
                    sendTextMessage(chatId, code);
                    break;

                // Handle /done command
                case "/done":
                    removeKeyboard(chatId);
                    break;
                    
                default:
                	sendTextMessage(chatId, "Don't mess up with me dude!🤔");
                    break;
                }

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
        }
    }

    private void sendMenu(String chatId) {
        SendMessage message = new SendMessage(chatId, "Please choose a brand:");
        message.setReplyMarkup(getKeyboard("VFX", "VT", "PUG", "MO", "UM", "AT", "VJP", "STAR"));
        try {
            execute(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Send sub-menu options based on the selected main menu option
    private void sendSubMenu1(String chatId, String option) throws Exception {
        SendMessage message = new SendMessage(chatId, "Please choose regulator:");
        switch (option) {
            case "VFX":
                message.setReplyMarkup(getKeyboard("ASIC","CIMA","VFSC","VFSC2","FCA","/done"));
                break;
            case "PUG":
                message.setReplyMarkup(getKeyboard("FSA","SVG","/done"));
                break;
            case "MO":
                message.setReplyMarkup(getKeyboard("VFSC","/done"));
                break;
            case "VT":
            case "VJP":
            case "STAR":
            case "AT":
            case "UM":
                message.setReplyMarkup(getKeyboard("SVG","/done"));
                break;
        }
        try {
            execute(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendSubMenu2(String chatId) {
        SendMessage message = new SendMessage(chatId, "Please choose PROD or ALPHA:");
        message.setReplyMarkup(getKeyboard("PROD", "ALPHA", "/done"));
        try {
            execute(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Helper method to create a reply keyboard with the given options
    private ReplyKeyboardMarkup getKeyboard(String... options) {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        for (String option : options) {
            KeyboardRow row = new KeyboardRow();
            row.add(option);
            keyboard.add(row);
        }
        markup.setKeyboard(keyboard);
        markup.setOneTimeKeyboard(true);
        return markup;
    }

    private void sendTextMessage(String chatId, String messageText) {
        SendMessage message = new SendMessage(chatId, messageText);
        try {
            execute(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void removeKeyboard(String chatId) {
        ReplyKeyboardRemove keyboard = new ReplyKeyboardRemove();
        SendMessage message = new SendMessage(chatId, "Thanks for using OTPHunter.👋Have a great day!");
        keyboard.setRemoveKeyboard(true);
        message.setReplyMarkup(keyboard);
        try {
            execute(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    //For getting OTP from DB. Supporting both alpha and prod
    public static String GetOTP(String TestEnv, String Brand, String regulator) throws Exception {
    	String code = "";
    	String vars = DBUtils.funcReadOTPInBusinessDB(TestEnv, Brand, regulator, "");
	 	
	 	//To extract code with regular expressions
	 	Pattern p = Pattern.compile("[0-9]{6}");		 	
	    Matcher m = p.matcher(vars);
	    
        // if an occurrence if a pattern was found in a given string...
        if (m.find()) {
            code =m.group(0); // second matched digits
            System.out.println("code: "+code);
        }else
        {
        	System.out.println("Couldn't find verification code. The body is:" + vars);
        }
		return code;
    }
    
    
    @Override
    public String getBotUsername() {
        return "OTPHunter_bot";
    }

    @Override
    public String getBotToken() {
        return "6068051610:AAGZYigjFbLSc8sqU6FMaqNr00qw_5nhOPw";
    }
}

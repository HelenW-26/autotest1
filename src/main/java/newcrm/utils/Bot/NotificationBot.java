package newcrm.utils.Bot;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
/**
 * For sending notification to any telegram user or group
 * @author Alex Liu
 *
 */
public class NotificationBot extends TelegramLongPollingBot {
    private final String token = "5699043564:AAG1ULM5nmg2kpUNkOQu1YZs9jKR1d_ocGc"; // Replace YOUR_API_KEY with your Telegram bot API key
    //private final static String chatId = "856771533"; // Replace SPECIFIED_USER_CHAT_ID with the chat ID of the user you want to send the message to


	@Override
    public void onUpdateReceived(Update update) {
        // This method is not used in this example
    }

    public void sendMessageToUser(String chatId, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);

        try {
            execute(sendMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public String getBotToken() {
        return token;
    }

	@Override
	public String getBotUsername() {
		// TODO Auto-generated method stub
		return null;
	}
}


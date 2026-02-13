package newcrm.testcases.others;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import newcrm.utils.Bot.OTPBot;

public class TestOTPBot {
    public static void main(String[] args) throws TelegramApiException {

        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
        	botsApi.registerBot(new OTPBot());
            System.out.println("OTPHunter started!");
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}

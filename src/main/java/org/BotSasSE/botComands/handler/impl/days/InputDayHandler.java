package org.BotSasSE.botComands.handler.impl.days;

import lombok.extern.slf4j.Slf4j;
import org.BotSasSE.botComands.enums.ConversationState;
import org.BotSasSE.botComands.handler.DbHandler;
import org.BotSasSE.botComands.handler.UserRequestHandler;
import org.BotSasSE.botComands.helper.KeyboardHelper;
import org.BotSasSE.botComands.model.UserRequest;
import org.BotSasSE.botComands.model.UserSession;
import org.BotSasSE.botComands.service.TelegramService;
import org.BotSasSE.botComands.service.UserSessionService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;


@Slf4j
@Component
public class InputDayHandler extends UserRequestHandler {

    private final DbHandler userSearchService;
    private final TelegramService telegramService;
    private final KeyboardHelper keyboardHelper;
    private final UserSessionService userSessionService;

    public InputDayHandler(DbHandler userSearchService, TelegramService telegramService, KeyboardHelper keyboardHelper, UserSessionService userSessionService) {
        this.userSearchService = userSearchService;
        this.telegramService = telegramService;
        this.keyboardHelper = keyboardHelper;
        this.userSessionService = userSessionService;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return isTextMessage(userRequest.getUpdate())
                && ConversationState.WAITING_FOR_DAY.equals(userRequest.getUserSession().getState());
    }

    @Override
    public void handle(UserRequest userRequest) {
        UserSession session = userRequest.getUserSession();
        ReplyKeyboardMarkup replyKeyboardMarkup = keyboardHelper.buildMenuWithCancel();

        String dayNumberStr = userRequest.getUpdate().getMessage().getText();
        if (dayNumberStr.equals("Nowy miesiac")) {
            session.setMonthWork(null);
            telegramService.sendMessage(userRequest.getChatId(),
                    "✍️Wprowadź nowy numer miesiąca od 1 do 12⤵️", replyKeyboardMarkup);
            session.setState(ConversationState.WAITING_FOR_MONTH);
            userSessionService.saveSession(userRequest.getChatId(), session);
        } else if (dayNumberStr.equals("Nowy rok")) {
            session.setYearWork(null);
            session.setMonthWork(null);
            telegramService.sendMessage(userRequest.getChatId(),
                    "✍️Nowy rok⤵️", replyKeyboardMarkup);
            session.setState(ConversationState.WAITING_FOR_YEAR);
            userSessionService.saveSession(userRequest.getChatId(), session);
        } else if (dayNumberStr.equals("Suma godzin w " + session.getNameOfMonthWork(session.getMonthWork()))) {
            ReplyKeyboardMarkup replyKeyboardMarkupSum = keyboardHelper.buildWorkHourMenu(session.getYearWork(), session.getMonthWork());
            telegramService.sendMessage(userRequest.getChatId(),
                    "Suma godzin w " + session.getNameOfMonthWork(session.getMonthWork()) + " wynosi " +
                            userSearchService.getSumWorkHour(session.getPeselNumber(), session.getMonthWork(),
                                    session.getYearWork()), replyKeyboardMarkupSum);
            session.setState(ConversationState.WAITING_FOR_DAY);
        } else {
            try {
                int dayNumber = Integer.parseInt(dayNumberStr);
                if (dayNumber < 1 || dayNumber > 31) {
                    telegramService.sendMessage(userRequest.getChatId(),
                            "✍️Wprowadź dzień miesiąca od 1 do 31⤵️", replyKeyboardMarkup);
                    session.setState(ConversationState.WAITING_FOR_DAY);
                    userSessionService.saveSession(userRequest.getChatId(), session);
                } else if (session.getMonthWork().equals("2") && Integer.parseInt(session.getYearWork()) % 4 != 0 && dayNumber > 28 ||
                        session.getMonthWork().equals("2") && Integer.parseInt(session.getYearWork()) % 4 == 0 && dayNumber > 29 ||
                        (session.getMonthWork().equals("04") || session.getMonthWork().equals("06") || session.getMonthWork().equals("09") || session.getMonthWork().equals("11")) && dayNumber > 30) {
                    telegramService.sendMessage(userRequest.getChatId(),
                            "✍️Wprowadź odpowiedni dzień dla " + session.getNameOfMonthWork(session.getMonthWork()) + " od 1 do 31⤵️",
                            replyKeyboardMarkup);
                    session.setState(ConversationState.WAITING_FOR_DAY);
                    userSessionService.saveSession(userRequest.getChatId(), session);
                } else {
                    session.setDayNumber(String.valueOf(dayNumber));
                    telegramService.sendMessage(userRequest.getChatId(),
                            "✍️Wprowadż ilość przepracowanych godzin⤵️", replyKeyboardMarkup);
                    session.setState(ConversationState.WAITING_FOR_HOURS);
                    userSessionService.saveSession(userRequest.getChatId(), session);
                }
            } catch (NumberFormatException e) {
                telegramService.sendMessage(userRequest.getChatId(),
                        "✍️Wprowadź dzień miesiąca od 1 do 31⤵️", replyKeyboardMarkup);
                session.setState(ConversationState.WAITING_FOR_DAY);
                userSessionService.saveSession(userRequest.getChatId(), session);
            }
        }
    }

    @Override
    public boolean isGlobal() {
        return false;
    }

}


package org.BotSasSE.botComands.handler.impl.month;

import lombok.extern.slf4j.Slf4j;
import org.BotSasSE.botComands.enums.ConversationState;
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
public class InputMonthHandler extends UserRequestHandler {

    private final TelegramService telegramService;
    private final KeyboardHelper keyboardHelper;
    private final UserSessionService userSessionService;

    public InputMonthHandler(TelegramService telegramService, KeyboardHelper keyboardHelper, UserSessionService userSessionService) {
        this.telegramService = telegramService;
        this.keyboardHelper = keyboardHelper;
        this.userSessionService = userSessionService;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return isTextMessage(userRequest.getUpdate())
                && ConversationState.WAITING_FOR_MONTH.equals(userRequest.getUserSession().getState());
    }

    @Override
    public void handle(UserRequest userRequest) {
        ReplyKeyboardMarkup replyKeyboardMarkup = keyboardHelper.buildMenuWithCancel();
        UserSession session = userRequest.getUserSession();
        String monthNumberStr = userRequest.getUpdate().getMessage().getText().replaceAll("^0+","");
        try {
            int monthNumber = Integer.parseInt(monthNumberStr);
            if (monthNumber < 1 || monthNumber > 12) {
                telegramService.sendMessage(userRequest.getChatId(),
                        "✍️Wprowadź numer miesiąca od 1 do 12⤵️", replyKeyboardMarkup);
                session.setState(ConversationState.WAITING_FOR_MONTH);
                userSessionService.saveSession(userRequest.getChatId(), session);
            } else {
                session.setMonthWork(String.valueOf(monthNumber));
                telegramService.sendMessage(userRequest.getChatId(),
                        "✍️Wprowadź dzień dla wybranego miesiąca " + session.getNameOfMonthWork(session.getMonthWork())+
                                " od 1 do 31⤵️",
                        replyKeyboardMarkup);
                session.setState(ConversationState.WAITING_FOR_DAY);
                userSessionService.saveSession(userRequest.getChatId(), session);
            }
        } catch (NumberFormatException e) {
            telegramService.sendMessage(userRequest.getChatId(),
                    "✍️Wprowadź numer miesiąca od 1 do 12⤵️", replyKeyboardMarkup);
            session.setState(ConversationState.WAITING_FOR_MONTH);
            userSessionService.saveSession(userRequest.getChatId(), session);
        }
    }

    @Override
    public boolean isGlobal() {
        return false;
    }

}


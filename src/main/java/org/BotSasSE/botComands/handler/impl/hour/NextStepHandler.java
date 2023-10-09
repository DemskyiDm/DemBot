package org.BotSasSE.botComands.handler.impl.hour;

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
public class NextStepHandler extends UserRequestHandler {

    private final TelegramService telegramService;
    private final KeyboardHelper keyboardHelper;
    private final UserSessionService userSessionService;

    public NextStepHandler(TelegramService telegramService, KeyboardHelper keyboardHelper, UserSessionService userSessionService) {
        this.telegramService = telegramService;
        this.keyboardHelper = keyboardHelper;
        this.userSessionService = userSessionService;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return isTextMessage(userRequest.getUpdate())
                && ConversationState.WAITING_FOR_NEXT_STEP.equals(userRequest.getUserSession().getState());
    }

    @Override
    public void handle(UserRequest userRequest) {
        UserSession session = userRequest.getUserSession();
        ReplyKeyboardMarkup replyKeyboardMarkup = keyboardHelper.buildWorkHourMenu(session.getYearWork(), session.getMonthWork());
        String input = userRequest.getUpdate().getMessage().getText();
        if (input.equals("Wprowadź kolejny dzień miesiąca")) {
            UserSession sessionNew = userRequest.getUserSession();
            telegramService.sendMessage(userRequest.getChatId(),
                    "✍️Wprowadź dzień miesiąca od 1 do 31⤵️", replyKeyboardMarkup);
            session.setState(ConversationState.WAITING_FOR_DAY);
            userSessionService.saveSession(userRequest.getChatId(), session);

        } else if (input.equals("Powrót na start")) {
            UserSession session1 = userRequest.getUserSession();
            ReplyKeyboardMarkup replyKeyboardMarkupNew = keyboardHelper.buildMainMenu();

            telegramService.sendMessage(userRequest.getChatId(),
                    "✍️Jesteśmy na początku. Wybierz, co chcesz zrobić⤵️",
                    replyKeyboardMarkupNew);
            session.setState(ConversationState.CONVERSATION_STARTED);
            userSessionService.saveSession(userRequest.getChatId(), session);
        }
        }


    @Override
    public boolean isGlobal() {
        return false;
    }

}

/*

    @Override
    public void handle(UserRequest userRequest) {
        ReplyKeyboardMarkup replyKeyboardMarkup = keyboardHelper.buildMenuWithCancel();
        UserSession session = userRequest.getUserSession();
        telegramService.sendMessage(userRequest.getChatId(),
                "✍️Напишіть свій номер пропуску з 4х цифр⤵️",
                replyKeyboardMarkup);
        session.setState(ConversationState.WAITING_FOR_PeselNumber);
        userSessionService.saveSession(userRequest.getChatId(), session);
    }


 */
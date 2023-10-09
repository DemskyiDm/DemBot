package org.BotSasSE.botComands.handler.impl;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.BotSasSE.botComands.enums.ConversationState;
import org.BotSasSE.botComands.handler.UserRequestHandler;
import org.BotSasSE.botComands.helper.KeyboardHelper;
import org.BotSasSE.botComands.model.UserRequest;
import org.BotSasSE.botComands.model.UserSession;
import org.BotSasSE.botComands.service.TelegramService;
import org.BotSasSE.botComands.service.UserSessionService;

import java.util.List;

@Component
public class FirstHandler extends UserRequestHandler {

    public static String START_COMMAND = "❗️Start";
    public static List<String> methodsFirst = List.of("Advance", "Inputting working hours", "salary calculation", "Send a photo report card");

    private final TelegramService telegramService;
    private final KeyboardHelper keyboardHelper;
    private final UserSessionService userSessionService;

    public FirstHandler(TelegramService telegramService, KeyboardHelper keyboardHelper, UserSessionService userSessionService) {
        this.telegramService = telegramService;
        this.keyboardHelper = keyboardHelper;
        this.userSessionService = userSessionService;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return isTextMessage(userRequest.getUpdate(), START_COMMAND);
    }

    @Override
    public void handle(UserRequest userRequest) {
        UserSession session = userRequest.getUserSession();
        ReplyKeyboardMarkup replyKeyboardMarkup = keyboardHelper.buildSecondMenuWithDataReset(methodsFirst);
        ReplyKeyboardMarkup replyKeyboardMarkupCancel = keyboardHelper.buildMenuWithCancel();
        //session.setPeselNumber("98110923203");
        //session.setPassportNumber("MR2399");

        if (session.getPeselNumber() != null && session.getPassportNumber() != null) { // check if pesel and pass number already exists

            telegramService.sendMessage(userRequest.getChatId(),
                    "Jesteś zalogowany do systemu. Wybierz, co chcesz zrobić",
                    replyKeyboardMarkup);
            session.setState(ConversationState.WAITING_FOR_COMMAND);
        } else if (session.getPeselNumber() != null && session.getPassportNumber() == null) { // check if pass number already exists

            telegramService.sendMessage(userRequest.getChatId(),
                    "Już wprowadzony numer PESEL: " + session.getPeselNumber() + ". Wprowadż serię i numer paszportu",
                    replyKeyboardMarkupCancel);
            session.setState(ConversationState.WAITING_FOR_Passport);
        } else {
            telegramService.sendMessage(userRequest.getChatId(),
                    "✍️Podaj swój numer PESEL⤵️",
                    replyKeyboardMarkupCancel);
            session.setState(ConversationState.WAITING_FOR_PeselNumber);
            userSessionService.saveSession(userRequest.getChatId(), session);
        }

    }

    @Override
    public boolean isGlobal() {
        return true;
    }

}

package org.BotSasSE.botComands.handler.impl.peselNumber;

import org.BotSasSE.botComands.handler.DbHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.BotSasSE.botComands.enums.ConversationState;
import org.BotSasSE.botComands.handler.UserRequestHandler;
import org.BotSasSE.botComands.helper.KeyboardHelper;
import org.BotSasSE.botComands.model.UserRequest;
import org.BotSasSE.botComands.model.UserSession;
import org.BotSasSE.botComands.service.TelegramService;
import org.BotSasSE.botComands.service.UserSessionService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Service

public class PeselNumberHandler extends UserRequestHandler {
    private final DbHandler userSearchService;
    private final TelegramService telegramService;
    private final KeyboardHelper keyboardHelper;
    private final UserSessionService userSessionService;

    @Autowired
    public PeselNumberHandler(DbHandler userSearchService, TelegramService telegramService, KeyboardHelper keyboardHelper, UserSessionService userSessionService) {
        this.userSearchService = userSearchService;
        this.telegramService = telegramService;
        this.keyboardHelper = keyboardHelper;
        this.userSessionService = userSessionService;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return isTextMessage(userRequest.getUpdate())
                && ConversationState.WAITING_FOR_PeselNumber.equals(userRequest.getUserSession().getState());
    }

    @Override
    public void handle(UserRequest userRequest) {
        ReplyKeyboardMarkup replyKeyboardMarkup = keyboardHelper.buildMainMenu();
        ReplyKeyboardMarkup replyKeyboardMarkup_1 = keyboardHelper.buildMenuWithCancel();
        UserSession session = userRequest.getUserSession();
        String peselNumber = userRequest.getUpdate().getMessage().getText();
        if (peselNumber.matches("^\\d{11}$")) {

            if (userSearchService.searchUserByPesel(peselNumber).equals("Error")) {
                telegramService.sendMessage(userRequest.getChatId(),
                        "✍️Wprowadż swój numer PESEL⤵️",
                        replyKeyboardMarkup);
                session.setState(ConversationState.WAITING_FOR_PeselNumber);
            } else {
                telegramService.sendMessage(userRequest.getChatId(), userSearchService.searchUserByPesel(peselNumber), replyKeyboardMarkup);
                session.setPeselNumber(peselNumber);
                telegramService.sendMessage(userRequest.getChatId(),
                        "✍️Dla poprawnej weryfikacji i logowania wprowadz serię i numer paszportu⤵️",
                        replyKeyboardMarkup_1);
                session.setState(ConversationState.WAITING_FOR_Passport);
                userSessionService.saveSession(userRequest.getChatId(), session);
            }
        } else {
            telegramService.sendMessage(userRequest.getChatId(),
                    "✍️Wprowadż swój numer PESEL⤵️",
                    replyKeyboardMarkup);
            session.setState(ConversationState.WAITING_FOR_PeselNumber);
            userSessionService.saveSession(userRequest.getChatId(), session);
        }
    }

    @Override
    public boolean isGlobal() {
        return false;
    }

}


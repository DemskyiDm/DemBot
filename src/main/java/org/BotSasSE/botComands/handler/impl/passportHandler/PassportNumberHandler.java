package org.BotSasSE.botComands.handler.impl.passportHandler;

import lombok.extern.slf4j.Slf4j;
import org.BotSasSE.botComands.enums.ConversationState;
import org.BotSasSE.botComands.handler.DbHandler;
import org.BotSasSE.botComands.handler.UserRequestHandler;
import org.BotSasSE.botComands.helper.KeyboardHelper;
import org.BotSasSE.botComands.model.UserRequest;
import org.BotSasSE.botComands.model.UserSession;
import org.BotSasSE.botComands.service.TelegramService;
import org.BotSasSE.botComands.service.UserSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.List;

@Slf4j
@Component
@Service

public class PassportNumberHandler extends UserRequestHandler {
    private final DbHandler userSearchService;
    private final TelegramService telegramService;
    private final KeyboardHelper keyboardHelper;
    private final UserSessionService userSessionService;
    public static List<String> methodsFirst = List.of("Advance", "Inputting working hours", "salary calculation","Send a photo report card");

    @Autowired
    public PassportNumberHandler(DbHandler userSearchService, TelegramService telegramService, KeyboardHelper keyboardHelper, UserSessionService userSessionService) {
        this.userSearchService = userSearchService;
        this.telegramService = telegramService;
        this.keyboardHelper = keyboardHelper;
        this.userSessionService = userSessionService;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return isTextMessage(userRequest.getUpdate())
                && ConversationState.WAITING_FOR_Passport.equals(userRequest.getUserSession().getState());
    }

    @Override
    public void handle(UserRequest userRequest) {
        ReplyKeyboardMarkup replyKeyboardMarkup = keyboardHelper.buildMenuWithCancel();
        UserSession session = userRequest.getUserSession();
        String passportNumber = userRequest.getUpdate().getMessage().getText().toUpperCase().replaceAll("\\s", "");

        if (userSearchService.searchUserByPassport(session.getPeselNumber()).equals("Error")) {
            telegramService.sendMessage(userRequest.getChatId(),
                    "✍️Wprowadż swój paszport. Twój paszport nie znaleziono w bazie dannych⤵️",
                    replyKeyboardMarkup);
            session.setState(ConversationState.WAITING_FOR_Passport);
        } else {
            if (passportNumber.equals(userSearchService.searchUserByPassport(session.getPeselNumber()))) {
                session.setPassportNumber(passportNumber);
                ReplyKeyboardMarkup replyKeyboardMarkupLogin = keyboardHelper.buildSecondMenuWithDataReset(methodsFirst);
                telegramService.sendMessage(userRequest.getChatId(), "Jesteś zalogowany. Wybierz, co dalej chcesz zrobić", replyKeyboardMarkupLogin);
                session.setState(ConversationState.WAITING_FOR_COMMAND);
                userSessionService.saveSession(userRequest.getChatId(), session);
            } else {
                telegramService.sendMessage(userRequest.getChatId(),
                        "✍️Wprowadż swój paszport. Twój paszport nie znaleziono w bazie dannych⤵️",
                        replyKeyboardMarkup);
                session.setState(ConversationState.WAITING_FOR_Passport);
            }
        }


    }

    @Override
    public boolean isGlobal() {
        return false;
    }

}


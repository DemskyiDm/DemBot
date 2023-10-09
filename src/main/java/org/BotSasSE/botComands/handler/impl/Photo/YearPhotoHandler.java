package org.BotSasSE.botComands.handler.impl.Photo;

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
public class YearPhotoHandler extends UserRequestHandler {

    private final TelegramService telegramService;
    private final KeyboardHelper keyboardHelper;
    private final UserSessionService userSessionService;

    public YearPhotoHandler(TelegramService telegramService, KeyboardHelper keyboardHelper, UserSessionService userSessionService) {
        this.telegramService = telegramService;
        this.keyboardHelper = keyboardHelper;
        this.userSessionService = userSessionService;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return isTextMessage(userRequest.getUpdate())
                && ConversationState.WAITING_FOR_SURNAME_PHOTO.equals(userRequest.getUserSession().getState());
    }

    @Override
    public void handle(UserRequest userRequest) {
        ReplyKeyboardMarkup replyKeyboardMarkup = keyboardHelper.buildMenuWithCancel();

        String surnameYearPhoto = userRequest.getUpdate().getMessage().getText();
        UserSession session = userRequest.getUserSession();

        try {
            int year = Integer.parseInt(surnameYearPhoto);
            if (year < 2022 || year > 2100) {
                telegramService.sendMessage(userRequest.getChatId(),
                        "✍️Napisz rok⤵️", replyKeyboardMarkup);
                session.setState(ConversationState.WAITING_FOR_SURNAME_PHOTO);
                userSessionService.saveSession(userRequest.getChatId(), session);
            } else {
                telegramService.sendMessage(userRequest.getChatId(),"Dzięki, rok zapisany, wprowaz numer miesiącu!",
                        replyKeyboardMarkup);
                session.setYearWork(String.valueOf(year));
                telegramService.sendMessage(userRequest.getChatId(),
                        "✍️Wprowadź numer miesiąca od 1 do 12⤵️", replyKeyboardMarkup);
                session.setState(ConversationState.WAITING_FOR_SURNAME_PHOTO);
                userSessionService.saveSession(userRequest.getChatId(), session);
            }
        } catch (NumberFormatException e) {
            telegramService.sendMessage(userRequest.getChatId(),
                    "✍️Napisz rok⤵️", replyKeyboardMarkup);
            session.setState(ConversationState.WAITING_FOR_SURNAME_PHOTO);
            userSessionService.saveSession(userRequest.getChatId(), session);
        }



        session.setPhotoYear(surnameYearPhoto);
        System.out.println(session.getPhotoYear());
        session.setState(ConversationState.WAITING_FOR_MONTH_PHOTO);
    /*    userSessionService.saveSession(userRequest.getChatId(), session);
        telegramService.sendMessage(userRequest.getChatId(),
                "✍️Wprowadż numer miesiąca od 1 do 12⤵️", replyKeyboardMarkup);*/
    }

    @Override
    public boolean isGlobal() {
        return false;
    }


}

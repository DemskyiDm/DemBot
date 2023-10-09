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
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
public class MethodEnteredHandler extends UserRequestHandler {

    private final TelegramService telegramService;
    private final KeyboardHelper keyboardHelper;
    private final UserSessionService userSessionService;

    public MethodEnteredHandler(TelegramService telegramService, KeyboardHelper keyboardHelper, UserSessionService userSessionService) {
        this.telegramService = telegramService;
        this.keyboardHelper = keyboardHelper;
        this.userSessionService = userSessionService;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return isTextMessage(userRequest.getUpdate())
                && ConversationState.WAITING_FOR_COMMAND.equals(userRequest.getUserSession().getState());
    }

    @Override
    public void handle(UserRequest userRequest) {
        ReplyKeyboardMarkup replyKeyboardMarkup = keyboardHelper.buildMenuWithCancel();
        ReplyKeyboardMarkup replyKeyboardMarkupAdvance = keyboardHelper.buildAdvanceMenu();
        String input = userRequest.getUpdate().getMessage().getText();
        if (input.equals("Advance")) {
            telegramService.sendMessage(userRequest.getChatId(),
                    "✍️Dla wysłania wniosku na zaliczkę naciśnij przycisk⤵️",
                    replyKeyboardMarkupAdvance);
            UserSession session = userRequest.getUserSession();
            session.setState(ConversationState.WAITING_FOR_ADVANCE);
            userSessionService.saveSession(userRequest.getChatId(), session);
        } else if (input.equals("Inputting working hours")) {

            UserSession session = userRequest.getUserSession();
            ReplyKeyboardMarkup replyKeyboardMarkupHours = keyboardHelper.buildWorkHourMenu(session.getYearWork(), session.getMonthWork());
            if (session.getYearWork() == null) {
                telegramService.sendMessage(userRequest.getChatId(),
                        "Witam. Wybierz rok, za który chcesz wprowadzić godziny",
                        replyKeyboardMarkupHours);
                session.setState(ConversationState.WAITING_FOR_YEAR);
                userSessionService.saveSession(userRequest.getChatId(), session);
            } else if (session.getMonthWork() == null) {
                telegramService.sendMessage(userRequest.getChatId(),
                        "Wybierz miesiąc, za jaki chcesz wprowadzić godziny",
                        replyKeyboardMarkupHours);
                session.setState(ConversationState.WAITING_FOR_MONTH);
                userSessionService.saveSession(userRequest.getChatId(), session);
            } else {
                telegramService.sendMessage(userRequest.getChatId(),
                        "Wybierz dzień, za jaki chcesz wprowadzić godziny",
                        replyKeyboardMarkupHours);
                session.setState(ConversationState.WAITING_FOR_DAY);
                userSessionService.saveSession(userRequest.getChatId(), session);
            }
        } else if (input.equals("salary calculation")) {
            UserSession session = userRequest.getUserSession();
            ReplyKeyboardMarkup replyKeyboardMarkupHours = keyboardHelper. buildSalaryCalculationMenu(session.getSalaryYear(),
                    session.getSalaryMonth());
            if (session.getSalaryYear() == null) {
                telegramService.sendMessage(userRequest.getChatId(),
                        "Witam. Wybierz rok, za który chcesz otrzymać rozliczenie swojego wynagrodzenia",
                        replyKeyboardMarkupHours);
                session.setState(ConversationState.WAITING_FOR_SALARY_YEAR);
                userSessionService.saveSession(userRequest.getChatId(), session);
            } else if (session.getMonthWork() == null) {
                telegramService.sendMessage(userRequest.getChatId(),
                        "Wybierz miesiąc, za który chcesz otrzymać rozliczenie swojego wynagrodzenia",
                        replyKeyboardMarkupHours);
                session.setState(ConversationState.WAITING_FOR_SALARY_MONTH);
                userSessionService.saveSession(userRequest.getChatId(), session);
            } else {
                telegramService.sendMessage(userRequest.getChatId(),
                        "Wybierz miesiąc, za który chcesz otrzymać rozliczenie swojego wynagrodzenia",
                        replyKeyboardMarkupHours);
                session.setState(ConversationState.WAITING_FOR_GET_CALCULATION);
                userSessionService.saveSession(userRequest.getChatId(), session);

            }
        } else if (input.equals("Send a photo report card")) {
            telegramService.sendMessage(userRequest.getChatId(),
                    "✍️Wprowadz rok, potem wprowadzisz miesiąc, za który chcesz wysłąć raport godzinowy ⤵️",
                    replyKeyboardMarkup);
            UserSession session = userRequest.getUserSession();
            session.setState(ConversationState.WAITING_FOR_SURNAME_PHOTO);
            userSessionService.saveSession(userRequest.getChatId(), session);
        } else if (input.equals("Wyjść z systemu")) {
            UserSession session = userRequest.getUserSession();
            telegramService.sendMessage(userRequest.getChatId(),
                    "❗️Dane użytkownika usunięte z systemu.️",
                    replyKeyboardMarkup);
            session.setPeselNumber(null);
            session.setPassportNumber(null);
            session.setMonthWork(null);
            session.setYearWork(null);

            session.setState(ConversationState.CONVERSATION_STARTED);
            userSessionService.saveSession(userRequest.getChatId(), session);
        }
    }

    @Override
    public boolean isGlobal() {
        return false;
    }

}

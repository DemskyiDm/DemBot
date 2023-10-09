package org.BotSasSE.botComands.handler.impl.month;

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
public class SalaryCalculationMonthHandler extends UserRequestHandler {

    private final DbHandler userSearchService;
    private final TelegramService telegramService;
    private final KeyboardHelper keyboardHelper;
    private final UserSessionService userSessionService;

    public SalaryCalculationMonthHandler(DbHandler userSearchService, TelegramService telegramService,
                                         KeyboardHelper keyboardHelper, UserSessionService userSessionService) {
       this.userSearchService = userSearchService;
        this.telegramService = telegramService;
        this.keyboardHelper = keyboardHelper;
        this.userSessionService = userSessionService;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return isTextMessage(userRequest.getUpdate())
                && ConversationState.WAITING_FOR_SALARY_MONTH.equals(userRequest.getUserSession().getState());
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
                session.setState(ConversationState.WAITING_FOR_SALARY_MONTH);
                userSessionService.saveSession(userRequest.getChatId(), session);
            } else {
                session.setSalaryMonth(String.valueOf(monthNumber));
                telegramService.sendMessage(userRequest.getChatId(),
                        "✍️W wybranym misiącu " + session.getNameOfMonthWork(session.getSalaryMonth())+" " + session.getSalaryYear() +
                                " roku miałeś ⤵️" + userSearchService.getSalaryCalculation(session.getPeselNumber(),session.getSalaryYear(),
                                session.getSalaryMonth()),
                        replyKeyboardMarkup);


                userSessionService.saveSession(userRequest.getChatId(), session);
                session.setState(ConversationState.CONVERSATION_STARTED);
                session.setSalaryYear(null);
                session.setSalaryMonth(null);
            }
        } catch (NumberFormatException e) {
            telegramService.sendMessage(userRequest.getChatId(),
                    "✍️Wprowadź numer miesiąca od 1 do 12⤵️", replyKeyboardMarkup);
            session.setState(ConversationState.WAITING_FOR_SALARY_MONTH);
            userSessionService.saveSession(userRequest.getChatId(), session);
        }
    }

    @Override
    public boolean isGlobal() {
        return false;
    }

}


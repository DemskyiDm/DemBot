package org.BotSasSE.botComands.handler.impl.hour;

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
public class InputHoursHandler extends UserRequestHandler {

    private final DbHandler userSearchService;
    private final TelegramService telegramService;
    private final KeyboardHelper keyboardHelper;
    private final UserSessionService userSessionService;

    public InputHoursHandler(DbHandler userSearchService, TelegramService telegramService, KeyboardHelper keyboardHelper, UserSessionService userSessionService) {
        this.userSearchService = userSearchService;
        this.telegramService = telegramService;
        this.keyboardHelper = keyboardHelper;
        this.userSessionService = userSessionService;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return isTextMessage(userRequest.getUpdate())
                && ConversationState.WAITING_FOR_HOURS.equals(userRequest.getUserSession().getState());
    }

    @Override
    public void handle(UserRequest userRequest) {
        ReplyKeyboardMarkup replyKeyboardMarkup = keyboardHelper.buildMainMenu();
        UserSession session = userRequest.getUserSession();
        String numberHoursWorkStr = userRequest.getUpdate().getMessage().getText();
        try {
            int numberHoursWork = Integer.parseInt(numberHoursWorkStr);
            if (numberHoursWork < 0 || numberHoursWork > 13) {
                telegramService.sendMessage(userRequest.getChatId(),
                        "✍️Wprowadż ilość przepracowanych godzin od 0 do 12⤵️", replyKeyboardMarkup);
                session.setState(ConversationState.WAITING_FOR_HOURS);
                userSessionService.saveSession(userRequest.getChatId(), session);
            } else {
                if (userSearchService.checkHourOfWork(session.getPeselNumber(), session.getYearWork(),
                        session.getMonthWork(), session.getDayNumber()).equals("ok")) {


                    session.setNumberHoursWork(String.valueOf(numberHoursWork));
                    ReplyKeyboardMarkup replyKeyboardMarkupFinal = keyboardHelper.buildNextStepMenu();
                    telegramService.sendMessage(userRequest.getChatId(),
                            "✍️Dzięki, dane zapisane ⤵️", replyKeyboardMarkupFinal);
                    userSearchService.setHourOfWork(session.getPeselNumber(), session.getYearWork(), session.getMonthWork(),
                            session.getDayNumber(), session.getNumberHoursWork());
                    session.setDayNumber(null);
                    session.setNumberHoursWork(null);
                    telegramService.sendMessage(userRequest.getChatId(),
                            "✍️Wybierz co dalej⤵️", replyKeyboardMarkupFinal);
                    session.setState(ConversationState.WAITING_FOR_NEXT_STEP);
                    userSessionService.saveSession(userRequest.getChatId(), session);
                } else {
                    ReplyKeyboardMarkup replyKeyboardMarkupSec = keyboardHelper.buildWorkHourMenu(session.getYearWork(), session.getMonthWork());

                    telegramService.sendMessage(userRequest.getChatId(),
                            "Godziny dla wskazanego dnia już wprowadzone. Wybierz inny dzień", replyKeyboardMarkupSec);
                    session.setState(ConversationState.WAITING_FOR_DAY);
                    userSessionService.saveSession(userRequest.getChatId(), session);
                }

            }
        } catch (NumberFormatException e) {
            telegramService.sendMessage(userRequest.getChatId(),
                    "✍️Wprowadż ilość przepracowanych godzin od 0 do 12⤵️", replyKeyboardMarkup);
            session.setState(ConversationState.WAITING_FOR_HOURS);
            userSessionService.saveSession(userRequest.getChatId(), session);
        }
    }


    @Override
    public boolean isGlobal() {
        return false;
    }

}


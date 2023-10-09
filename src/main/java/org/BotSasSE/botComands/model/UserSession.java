package org.BotSasSE.botComands.model;

import lombok.Builder;
import lombok.Data;
import org.BotSasSE.botComands.enums.ConversationState;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
public class UserSession {
    private Long chatId;
    private ConversationState state;
    private String text;
    private String peselNumber;
    private String passportNumber;
    private String yearWork;
    private String monthWork;
    private String dayNumber;
    private String numberHoursWork;
    private String surnamePhoto;
    private String salaryYear;
    private String photoYear;
    private String salaryMonth;
    private String photoMonth;

    public String getNameOfMonthWork(String month) {
        Map<String, String> monthNames = new HashMap<>();
        monthNames.put("1", "Styczeń");
        monthNames.put("2", "Luty");
        monthNames.put("3", "Marzec");
        monthNames.put("4", "Kwiecień");
        monthNames.put("5", "Maj");
        monthNames.put("6", "Czerwiec");
        monthNames.put("7", "Lipiec");
        monthNames.put("8", "Sierpień");
        monthNames.put("9", "Wrzesień");
        monthNames.put("10", "Październik");
        monthNames.put("11", "Listopad");
        monthNames.put("12", "Grudzień");

        String monthName = monthNames.getOrDefault(month, "Nieznany miesiąc");

        return monthName;
    }

}

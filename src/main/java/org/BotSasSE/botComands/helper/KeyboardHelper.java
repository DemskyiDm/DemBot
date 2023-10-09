package org.BotSasSE.botComands.helper;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

import static org.BotSasSE.botComands.constant.Constants.BTN_CANCEL;

/**
 * Helper class, allows to build keyboards for users
 */
@Component
public class KeyboardHelper {

    public ReplyKeyboardMarkup buildSecondMenu(List<String> methodsFirst) {
        List<KeyboardRow> rows = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        for (String method : methodsFirst) {
            KeyboardButton button = new KeyboardButton(method);
            row.add(button);
        }
        rows.add(row);

        KeyboardRow row2 = new KeyboardRow(List.of(new KeyboardButton(BTN_CANCEL)));

        rows.add(row2);

        return ReplyKeyboardMarkup.builder()
                .keyboard(rows)
                .selective(true)
                .resizeKeyboard(true)
                .oneTimeKeyboard(false)
                .build();
    }

    public ReplyKeyboardMarkup buildSecondMenuWithDataReset(List<String> methodsFirst) {
        List<KeyboardRow> rows = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        for (String method : methodsFirst) {
            KeyboardButton button = new KeyboardButton(method);
            row.add(button);
        }
        rows.add(row);

        KeyboardRow row2 = new KeyboardRow(List.of(new KeyboardButton("Wyjść z systemu")));
        KeyboardRow row3 = new KeyboardRow(List.of(new KeyboardButton(BTN_CANCEL)));

        rows.add(row2);
        rows.add(row3);

        return ReplyKeyboardMarkup.builder()
                .keyboard(rows)
                .selective(true)
                .resizeKeyboard(true)
                .oneTimeKeyboard(false)
                .build();
    }


    public ReplyKeyboardMarkup buildMainMenu() {
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add("❗️Start");

        return ReplyKeyboardMarkup.builder()
                .keyboard(List.of(keyboardRow))
                .selective(true)
                .resizeKeyboard(true)
                .oneTimeKeyboard(false)
                .build();
    }

    public ReplyKeyboardMarkup buildMenuWithCancel() {
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(BTN_CANCEL);

        return ReplyKeyboardMarkup.builder()
                .keyboard(List.of(keyboardRow))
                .selective(true)
                .resizeKeyboard(true)
                .oneTimeKeyboard(false)
                .build();
    }


    public ReplyKeyboardMarkup buildLoginMenu() {
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add("❗Login");

        KeyboardRow row2 = new KeyboardRow(List.of(new KeyboardButton(BTN_CANCEL)));

        return ReplyKeyboardMarkup.builder()
                .keyboard(List.of(keyboardRow, row2))
                .selective(true)
                .resizeKeyboard(true)
                .oneTimeKeyboard(false)
                .build();
    }

    public ReplyKeyboardMarkup buildAdvanceMenu() {
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add("✍️Wysłać wniosek na otrzymanie zaliczki⤵️");

        KeyboardRow row2 = new KeyboardRow(List.of(new KeyboardButton(BTN_CANCEL)));

        return ReplyKeyboardMarkup.builder()
                .keyboard(List.of(keyboardRow, row2))
                .selective(true)
                .resizeKeyboard(true)
                .oneTimeKeyboard(false)
                .build();
    }

    public ReplyKeyboardMarkup buildWorkHourMenu(String year, String month) {
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

        List<KeyboardButton> buttons = new ArrayList<>();
        List<KeyboardButton> buttonsRow2 = new ArrayList<>();
        if (year != null && !year.isEmpty()) {
            buttons.add(new KeyboardButton("Rok wprowadzony - " + year));

        } else {
            buttons.add(new KeyboardButton("Wprowadż rok"));
        }
        if (year != null && !year.isEmpty()) {
            if (month != null && !month.isEmpty()) {
                buttons.add(new KeyboardButton("Miesiąc wprowadzony - " + monthName));
                buttons.add(new KeyboardButton("Wprowadż dzień"));
                buttonsRow2.add(new KeyboardButton("Nowy rok"));
                buttonsRow2.add(new KeyboardButton("Nowy miesiac"));
                buttonsRow2.add(new KeyboardButton("Suma godzin w " + monthName));
            } else {
                buttons.add(new KeyboardButton("Wprowadż miesiąc"));
            }
        }


        KeyboardRow row1 = new KeyboardRow(buttons);
        KeyboardRow row2 = new KeyboardRow(buttonsRow2);
        KeyboardRow row3 = new KeyboardRow(List.of(new KeyboardButton(BTN_CANCEL)));

        return ReplyKeyboardMarkup.builder()
                .keyboard(List.of(row1, row2, row3))
                .selective(true)
                .resizeKeyboard(true)
                .oneTimeKeyboard(false)
                .build();
    }


    public ReplyKeyboardMarkup buildNextStepMenu() {
        List<KeyboardButton> buttons = List.of(
                new KeyboardButton("Wprowadź kolejny dzień miesiąca"),
                new KeyboardButton("Powrót na start"));
        KeyboardRow row1 = new KeyboardRow(buttons);

        KeyboardRow row2 = new KeyboardRow(List.of(new KeyboardButton(BTN_CANCEL)));

        return ReplyKeyboardMarkup.builder()
                .keyboard(List.of(row1, row2))
                .selective(true)
                .resizeKeyboard(true)
                .oneTimeKeyboard(false)
                .build();
    }

    public ReplyKeyboardMarkup buildSalaryCalculationMenu(String year, String month) {
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

        List<KeyboardButton> buttons = new ArrayList<>();
        List<KeyboardButton> buttonsRow2 = new ArrayList<>();
        if (year != null && !year.isEmpty()) {
            buttons.add(new KeyboardButton("Rok wprowadzony - " + year));

        } else {
            buttons.add(new KeyboardButton("Wprowadż rok"));
        }
        if (year != null && !year.isEmpty()) {
            if (month != null && !month.isEmpty()) {
                buttons.add(new KeyboardButton("Miesiąc wprowadzony - " + monthName));
                buttonsRow2.add(new KeyboardButton("Nowy rok"));
                buttonsRow2.add(new KeyboardButton("Nowy miesiac"));
            } else {
                buttons.add(new KeyboardButton("Wprowadż miesiąc"));
            }
        }

        KeyboardRow row1 = new KeyboardRow(buttons);
        KeyboardRow row2 = new KeyboardRow(buttonsRow2);
        KeyboardRow row3 = new KeyboardRow(List.of(new KeyboardButton(BTN_CANCEL)));

        return ReplyKeyboardMarkup.builder()
                .keyboard(List.of(row1, row2, row3))
                .selective(true)
                .resizeKeyboard(true)
                .oneTimeKeyboard(false)
                .build();
    }

}

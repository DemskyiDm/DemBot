package org.BotSasSE.reactController.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class EditAdresCombo {
    private Long employee_id;
    private String employee_pesel;
    private String employee_last_name;
    private String employee_first_name;
    private String employee_status;
    private String residence_city;
    private String residence_street;
    private String residence_home;
    private String residence_flat;
    private String residence_room;
    private Double value_accommodation;
    private LocalDate start_date;
    private LocalDate end_date;
}

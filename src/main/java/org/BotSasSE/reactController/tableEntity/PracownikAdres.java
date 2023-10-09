package org.BotSasSE.reactController.tableEntity;


import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
@Data
@AllArgsConstructor

@Table(name = "pracownikadres")
public class PracownikAdres {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_address_id", nullable = false)
    private Integer employee_address_id;

    private Long employee_id;

    @Column(name = "start_date")
    private LocalDate start_date;
    @Column(name = "end_date")
    private LocalDate end_date;
    @Transient
    private String employee_first_name;
    @Transient
    private String employee_last_name;
    @Transient
    private String residence_city;
    @Transient
    private String residence_street;
    @Transient
    private String residence_home;
    @Transient
    private String residence_flat;
    @Transient
    private String residence_room;
    @ManyToOne
    @JoinColumn(name = "employee_id", insertable = false, updatable = false)
    private Worker pracownik;
    @ManyToOne
    @JoinColumn(name = "residence_address_id")
    private Adres adres;

    public PracownikAdres() {
    }



}

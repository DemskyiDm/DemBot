package org.BotSasSE.reactController.tableEntity;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor

@Table(name = "avanse")
public class Avanse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "advance_id", nullable = false)
    private Integer advance_id;
    @Column(name = "advance_date")
    private LocalDate advance_date;
    private Long employee_id;
    @Column(name = "advance_amount")
    private Integer advance_amount;
    @Column(name = "week_number")
    private Integer week_number;
    @Transient
    private String employee_first_name;
    @Transient
    private String employee_last_name;
    @ManyToOne
    @JoinColumn(name = "employee_id", insertable = false, updatable = false)
    private Worker pracownikAvanse;

    public Avanse() {
    }

}

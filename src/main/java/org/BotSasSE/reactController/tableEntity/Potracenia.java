package org.BotSasSE.reactController.tableEntity;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor

@Table(name = "potracenia")
public class Potracenia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dedustions_id", nullable = false)
    private Integer dedustions_id;
    @Column(name = "dedutcions_date")
    private LocalDate dedutcions_date;
    private Long employee_id;
    @Column(name = "year")
    private Integer year;
    @Column(name = "month")
    private Integer month;
    @Column(name = "amount")
    private Integer amount;
    @Transient
    private String employee_first_name;
    @Transient
    private String employee_last_name;
    @ManyToOne
    @JoinColumn(name = "employee_id", insertable = false, updatable = false)
    private Worker pracownikPotracenie;

    public Potracenia() {
    }

}

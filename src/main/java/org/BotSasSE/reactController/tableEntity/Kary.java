package org.BotSasSE.reactController.tableEntity;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor

@Table(name = "kary")
public class Kary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "penalty_id", nullable = false)
    private Integer penalty_id;
    private Long employee_id;
    @Column(name = "year")
    private Integer year;
    @Column(name = "month")
    private Integer month;
    @Column(name = "penalty_amount")
    private Integer penalty_amount;
    @Transient
    private String employee_first_name;
    @Transient
    private String employee_last_name;
    @ManyToOne
    @JoinColumn(name = "employee_id", insertable = false, updatable = false)
    private Worker pracownikKary;

    public Kary() {
    }

}

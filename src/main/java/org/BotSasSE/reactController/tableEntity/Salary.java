package org.BotSasSE.reactController.tableEntity;


import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import java.sql.Date;


@Entity
@Data
@AllArgsConstructor

@Table(name = "wyplaty")
public class Salary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payroll_id", nullable = false)
    private Long payroll_id;
    private Long employee_id;
    @Column(name = "year")
    private String year;
    @Column(name = "month")
    private String month;
    @Column(name = "salary_amount")
    private Double salary_amount;
    @Column(name = "housing_cost")
    private Double housing_cost;
    @Column(name = "deductions")
    private Double deductions;
    @Column(name = "advances")
    private Double advances;
    @Column(name = "penalties")
    private Double penalties;
    @Column(name = "net_payout")
    private Double net_payout;

    @Transient
    private String employee_first_name;
    @Transient
    private String employee_last_name;
    @ManyToOne
    @JoinColumn(name = "employee_id", insertable = false, updatable = false)
    private Worker pracownikSalary;

    public Salary() {
    }

}

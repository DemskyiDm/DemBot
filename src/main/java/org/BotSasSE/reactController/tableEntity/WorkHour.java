package org.BotSasSE.reactController.tableEntity;


import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;


@Entity
@Data
@AllArgsConstructor

@Table(name = "godzinypracy")
public class WorkHour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "workhour_id", nullable = false)
    private Long workhour_id;
    private Long employee_id;
    @Column(name = "year")
    private String year;
    @Column(name = "month")
    private String month;
    @Column(name = "day")
    private String day;
    @Column(name = "work_hours")
    private Integer work_hours;

    @Transient
    private String employee_first_name;
    @Transient
    private String employee_last_name;
    @ManyToOne
    @JoinColumn(name = "employee_id", insertable = false, updatable = false)
    private Worker pracownikWorkHours;

    public WorkHour() {
    }



}

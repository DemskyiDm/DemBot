package org.BotSasSE.reactController.tableEntity;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@Component
@Table(name = "kosztyzakwaterowania")
public class KosztZakwater {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "housing_cost_id", nullable = false)
    private Long housing_cost_id;
    private Long employee_id;
    @Column(name = "residence_address_id")
    private Long residence_address_id;
    @Column(name = "start_date")
    private java.sql.Date start_date;
    @Column(name = "end_date")
    private java.sql.Date end_date;
    @Column(name = "year")
    private String year;
    @Column(name = "month")
    private String month;
    @Column(name = "cost")
    private String cost;
    @Transient
    private String employee_first_name;
    @Transient
    private String employee_last_name;

    // Додайте поле для вартості проживання
    @Transient
    private Double value_accommodation;

    @ManyToOne
    @JoinColumn(name = "employee_id", insertable = false, updatable = false)
    private Worker pracownikKosztZakw;
    @ManyToOne
    @JoinColumn(name = "residence_address_id", insertable = false, updatable = false)
    private Adres adres;

    public KosztZakwater() {
    }

}



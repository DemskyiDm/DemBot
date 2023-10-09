package org.BotSasSE.reactController.tableEntity;


import lombok.*;

import java.sql.Date;
import javax.persistence.*;


@Entity
@Data
@AllArgsConstructor

@Table(name = "pracownicy")
public class Worker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id", nullable = false)
    private Long employee_id;
    @Column(name = "employee_passport_number", nullable = false)
    private String employee_passport_number;
    @Column(name = "employee_pesel", nullable = false)
    private String employee_pesel;
    @Column(name = "employee_last_name", nullable = false)
    private String employee_last_name;
    @Column(name = "employee_first_name", nullable = false)
    private String employee_first_name;
    @Column(name = "employee_gender", nullable = false)
    private String employee_gender;
    @Column(name = "employee_position_id", nullable = false)
    private int employee_position_id;
    @Column(name = "employee_position", nullable = false)
    private String employee_position;
    @Column(name = "employee_status", nullable = false)
    private String employee_status;
    @Column(name = "employee_accommodation_date")
    private Date employee_accommodation_date;
    @Column(name = "employee_training_date")
    private Date employee_training_date;
    @Column(name = "employee_medical_certificate_date")
    private Date employee_medical_certificate_date;
    @Column(name = "employee_document_type")
    private String employee_document_type;
    @Column(name = "employee_entry_to_rp_date")
    private Date employee_entry_to_rp_date;
    @Column(name = "employee_legalization_document")
    private String employee_legalization_document;
    @Column(name = "employee_legalization_document_expiry")
    private Date employee_legalization_document_expiry;
    @Column(name = "employee_contract_start")
    private Date employee_contract_start;
    @Column(name = "employee_contract_end")
    private Date employee_contract_end;
    @Column(name = "employee_phone")
    private String employee_phone;
    @Column(name = "employee_last_working_day")
    private Date employee_last_working_day;
    @Column(name = "employee_departure_date")
    private Date employee_departure_date;
    @Column(name = "employee_comments")
    private String employee_comments;
    @Column(name = "employee_student_card_end")
    private Date employee_student_card_end;
    @Column(name = "employee_date_of_birth")
    private String employee_date_of_birth;


    public Worker() {
    }

    //@OneToMany(mappedBy = "pracownik", cascade = CascadeType.ALL)
   // private List<PracownikAdres> adresy = new ArrayList<>();
}

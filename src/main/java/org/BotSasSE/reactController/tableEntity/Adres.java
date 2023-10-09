package org.BotSasSE.reactController.tableEntity;


import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Data
@AllArgsConstructor

@Table(name = "adresymieszkan")
public class Adres {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "residence_address_id", nullable = false)
    private Integer residence_address_id;
    @Column(name = "residence_city")
    private String residence_city;
    @Column(name = "residence_street")
    private String residence_street;
    @Column(name = "residence_home")
    private String residence_home;
    @Column(name = "residence_flat")
    private String residence_flat;
    @Column(name = "residence_room")
    private String residence_room;
    @Column(name = "value_accommodation ")
    private Double value_accommodation;
/*
    @OneToMany(mappedBy = "adres", cascade = CascadeType.ALL)
    private List<KosztZakwater> kosztZakwaterList;
*/
   public Adres() {
    }



}

package org.BotSasSE.reactController.repository;


import org.BotSasSE.reactController.interfaces.AdresProjection;
import org.BotSasSE.reactController.interfaces.AdresWorkerProjection;
import org.BotSasSE.reactController.tableEntity.Adres;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdresRepository extends JpaRepository<Adres, Integer> {
    List<Adres> findAll();
    List<Adres> findById(int residence_address_id);
   // List<AdresProjection> findAllProjectedAdres();
}

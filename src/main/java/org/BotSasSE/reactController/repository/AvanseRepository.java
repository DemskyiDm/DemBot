package org.BotSasSE.reactController.repository;


import org.BotSasSE.reactController.tableEntity.Adres;
import org.BotSasSE.reactController.tableEntity.Avanse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvanseRepository extends JpaRepository<Avanse, Integer> {
    List<Avanse> findAll();
}

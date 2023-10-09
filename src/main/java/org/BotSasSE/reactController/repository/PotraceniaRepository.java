package org.BotSasSE.reactController.repository;


import org.BotSasSE.reactController.tableEntity.Avanse;
import org.BotSasSE.reactController.tableEntity.Potracenia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PotraceniaRepository extends JpaRepository<Potracenia, Integer> {
    List<Potracenia> findAll();
}

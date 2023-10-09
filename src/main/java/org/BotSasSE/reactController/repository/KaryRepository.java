package org.BotSasSE.reactController.repository;


import org.BotSasSE.reactController.tableEntity.Kary;
import org.BotSasSE.reactController.tableEntity.Potracenia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KaryRepository extends JpaRepository<Kary, Integer> {
    List<Kary> findAll();
}

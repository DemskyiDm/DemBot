package org.BotSasSE.reactController.repository;


import org.BotSasSE.reactController.tableEntity.PracownikAdres;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PracownikAdresRepository extends JpaRepository<PracownikAdres, Integer> {
    List<PracownikAdres> findAll();
}

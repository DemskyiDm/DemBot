package org.BotSasSE.reactController.repository;


import org.BotSasSE.reactController.tableEntity.KosztZakwater;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Component
public interface KosztZakwaterRepository extends JpaRepository<KosztZakwater, Long> {
    List<KosztZakwater> findAll();
    List<KosztZakwater> findByYearAndMonth(String year, String month);


}

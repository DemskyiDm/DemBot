package org.BotSasSE.reactController.repository;


import org.BotSasSE.reactController.tableEntity.Adres;
import org.BotSasSE.reactController.tableEntity.Salary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalaryRepository extends JpaRepository<Salary, Long> {
    List<Salary> findAll();
    List<Salary> findByYearAndMonth(String year, String month);

}

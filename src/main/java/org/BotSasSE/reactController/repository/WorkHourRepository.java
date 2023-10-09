package org.BotSasSE.reactController.repository;


import org.BotSasSE.reactController.tableEntity.KosztZakwater;
import org.BotSasSE.reactController.tableEntity.WorkHour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Component
public interface WorkHourRepository extends JpaRepository<WorkHour, Long> {
    List<WorkHour> findAll();
    List<WorkHour> findByYearAndMonth(String year, String month);
    @Query("SELECT wh FROM WorkHour wh WHERE wh.employee_id = :employee_id AND wh.year = :year AND wh.month = :month " +
            "AND wh.day = :day")
    WorkHour findByEmployee_idAndYearAndMonthAndDay(@Param("employee_id") Long employeeId, @Param("year") String year,
                                             @Param("month") String month, @Param("day") String day);
}

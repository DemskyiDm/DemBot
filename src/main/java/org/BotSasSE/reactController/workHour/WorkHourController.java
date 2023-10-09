package org.BotSasSE.reactController.workHour;

import org.BotSasSE.reactController.ResponseMessage;
import org.BotSasSE.reactController.repository.WorkHourRepository;
import org.BotSasSE.reactController.tableEntity.*;
import org.hibernate.jdbc.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
//@RequestMapping("/koszt")
public class WorkHourController {

    private final WorkHourRepository workHourRepository;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public WorkHourController(WorkHourRepository workHourRepository, JdbcTemplate jdbcTemplate) {
        this.workHourRepository = workHourRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @PersistenceContext
    private EntityManager entityManager;

    @GetMapping("/listHours")
    public ResponseEntity<List<WorkHour>> WorkHourYearAndMonth(
            @RequestParam("year") String year,
            @RequestParam("month") String month) {

        List<WorkHour> allRecordsForYearMonth = workHourRepository.findByYearAndMonth(year, month);

        for (WorkHour workHourMonthYear : allRecordsForYearMonth) {
            Worker pracownikWorkHours = workHourMonthYear.getPracownikWorkHours();
            String firstName = pracownikWorkHours.getEmployee_first_name();
            String lastName = pracownikWorkHours.getEmployee_last_name();
            workHourMonthYear.setEmployee_first_name(firstName);
            workHourMonthYear.setEmployee_last_name(lastName);
        }
        return new ResponseEntity<>(allRecordsForYearMonth, HttpStatus.OK);

    }


    @PutMapping("/editworkhours")
    public ResponseEntity<ResponseMessage> EditWorkHours(@RequestBody List<Map<String, String>> requestDataList) {
        try {
            for (Map<String, String> request : requestDataList) {
                String year = request.get("year");
                String month = request.get("month");
                Long employee_id = Long.parseLong(request.get("employee_id"));
                String day = request.get("day");
                Integer workHours = Integer.parseInt(request.get("work_hours"));

                String updateRecord = employee_id + " " + year + " " + month + " " + day + " " + workHours;
                String updateRecordwithoutHours = employee_id + " " + year + " " + month + " " + day;

                String existingRecordKey = getExistingRecordKey(getEmployeeWorkHousForDay(employee_id, year, month, day));
                String existingRecordKeyWithoutWorkHours =
                        getExistingRecordKeyWithoutHours(getEmployeeWorkHousForDayWithoutHours(employee_id, year, month, day));

                if (existingRecordKey == null || !updateRecord.equals(existingRecordKey)) {
                    if (!updateRecordwithoutHours.equals((existingRecordKeyWithoutWorkHours))) {
                        WorkHour workHour = createKosztZakwaterFromEmployeeData(employee_id, year,
                                month, day, workHours);
                        workHourRepository.save(workHour);
                    } else {
                        WorkHour workHour = createKosztZakwaterFromEmployeeDataWithoutWorkHours(employee_id, year,
                                month, day, workHours);
                        workHourRepository.save(workHour);
                    }
                }
            }
            return ResponseEntity.ok(new ResponseMessage("SendToDB"));
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseMessage("ErrorPage"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private WorkHour createKosztZakwaterFromEmployeeData(Long employee_id, String year,
                                                         String month, String day, Integer work_hours) {
        WorkHour work_Hour = new WorkHour();
        work_Hour.setEmployee_id(employee_id);
        work_Hour.setYear(year);
        work_Hour.setMonth(month);
        work_Hour.setDay(day);
        work_Hour.setWork_hours(work_hours);
        return work_Hour;
    }

    private WorkHour createKosztZakwaterFromEmployeeDataWithoutWorkHours(Long employee_id, String year,
                                                                         String month, String day, Integer work_hours) {
        WorkHour work_Hour = workHourRepository.findByEmployee_idAndYearAndMonthAndDay(employee_id, year, month, day);
        work_Hour.setWork_hours(work_hours);
        return work_Hour;
    }

    private String getExistingRecordKey(List<Map<String, Object>> existingData) {
        String existingRecordKey = null;

        for (Map<String, Object> existingRecord : existingData) {
            String year_existing = null;
            if (existingRecord.get("year") != null) {
                year_existing = existingRecord.get("year").toString();
            }

            String month_existing = null;
            if (existingRecord.get("month") != null) {
                month_existing = existingRecord.get("month").toString();
            }

            Long employee_id_existing = null;
            if (existingRecord.get("employee_id") != null) {
                employee_id_existing = Long.parseLong(existingRecord.get("employee_id").toString());
            }

            String day_existing = null;
            if (existingRecord.get("day") != null) {
                day_existing = existingRecord.get("day").toString();
            }

            String workHours_existing = null;
            if (existingRecord.get("work_hours") != null) {
                workHours_existing = existingRecord.get("work_hours").toString();
            }

            existingRecordKey =
                    employee_id_existing + " " + year_existing + " " + month_existing + " " + day_existing + " " + workHours_existing;
        }

        return existingRecordKey;
    }

    public List<Map<String, Object>> getEmployeeWorkHousForDay(Long employee_id, String year, String month,
                                                               String day) {
        try {
            String query = "SELECT DISTINCT gp.employee_id, gp.year, gp.month, gp.day, gp.work_hours " +
                    "FROM godzinypracy gp " +
                    "WHERE gp.employee_id=? AND gp.year=? AND gp.month=? AND gp.day=?";
            Object[] params = new Object[]{employee_id, year, month, day};
            return jdbcTemplate.queryForList(query, params);
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return Collections.emptyList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public List<Map<String, Object>> getEmployeeWorkHousForDayWithoutHours(Long employee_id, String year, String month,
                                                                           String day) {
        try {
            String query = "SELECT DISTINCT gp.employee_id, gp.year, gp.month, gp.day " +
                    "FROM godzinypracy gp " +
                    "WHERE gp.employee_id=? AND gp.year=? AND gp.month=? AND gp.day=?";
            Object[] params = new Object[]{employee_id, year, month, day};
            return jdbcTemplate.queryForList(query, params);
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return Collections.emptyList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    private String getExistingRecordKeyWithoutHours(List<Map<String, Object>> existingData) {
        String existingRecordKey = null;

        for (Map<String, Object> existingRecord : existingData) {
            String year_existing = null;
            if (existingRecord.get("year") != null) {
                year_existing = existingRecord.get("year").toString();
            }

            String month_existing = null;
            if (existingRecord.get("month") != null) {
                month_existing = existingRecord.get("month").toString();
            }

            Long employee_id_existing = null;
            if (existingRecord.get("employee_id") != null) {
                employee_id_existing = Long.parseLong(existingRecord.get("employee_id").toString());
            }

            String day_existing = null;
            if (existingRecord.get("day") != null) {
                day_existing = existingRecord.get("day").toString();
            }

            existingRecordKey =
                    employee_id_existing + " " + year_existing + " " + month_existing + " " + day_existing;
        }

        return existingRecordKey;
    }
}

package org.BotSasSE.reactController.handleSalary;

import org.BotSasSE.reactController.ResponseMessage;
import org.BotSasSE.reactController.repository.SalaryRepository;
import org.BotSasSE.reactController.tableEntity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/salary")
public class handleSalary {
    private SalaryRepository salaryRepository;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public handleSalary(SalaryRepository salaryRepository, JdbcTemplate jdbcTemplate) {
        this.salaryRepository = salaryRepository;
        this.jdbcTemplate = jdbcTemplate;
    }


    @GetMapping("/salarylist")
    public ResponseEntity<List<Salary>> getAllKoszt() {
        List<Salary> salaryAll = salaryRepository.findAll();

        for (Salary salary : salaryAll) {
            Worker pracownik = salary.getPracownikSalary();
            String firstName = pracownik.getEmployee_first_name();
            String lastName = pracownik.getEmployee_last_name();
            salary.setEmployee_first_name(firstName);
            salary.setEmployee_last_name(lastName);
        }

        return new ResponseEntity<>(salaryAll, HttpStatus.OK);
    }

    @DeleteMapping("/salary/{id}")
    public ResponseEntity<ResponseMessage> deleteAvanse(@PathVariable Long id) {
        try {
            salaryRepository.deleteById(id);
            return ResponseEntity.ok(new ResponseMessage("Dane wynagrodzenia pracownika usunięte"));
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseMessage("Błąd w trakcie usunięcia: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/checkAndUpdateSalary")
    public ResponseEntity<ResponseMessage> checkAndUpdateSalary(
            @RequestBody Map<String, String> request) {
        String year = request.get("year");
        String month = request.get("month");

        List<Salary> existingRecords = salaryRepository.findByYearAndMonth(year, month);
        Map<String, Boolean> existingRecordsMap = createExistingRecordsMap(existingRecords);

        LocalDate startDateLocal = LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), 1);
        LocalDate endDateLocal = startDateLocal.withDayOfMonth(startDateLocal.lengthOfMonth());
        java.sql.Date startDate = localDateToSqlDate(startDateLocal);
        java.sql.Date endDate = localDateToSqlDate(endDateLocal);
        List<Map<String, Object>> allEmployeeForMonthYear = getEmployeeIdsForMonth(startDate, endDate);


        for (Map<String, Object> employeeData : allEmployeeForMonthYear) {

            String key = generateKeyFromEmployeeData(employeeData);
            if (!existingRecordsMap.containsKey(key)) {
                Salary salaryRecord = createSalaryFromEmployeeData(employeeData);
                 salaryRepository.save(salaryRecord);
                existingRecordsMap.put(key, true);
            } else {
                // Оновлюємо існуючий запис, якщо він вже існує
                Salary existingSalary = getExistingSalary(existingRecords, key);
                updateExistingSalary(existingSalary, employeeData);
                salaryRepository.save(existingSalary);
            }
        }

        return ResponseEntity.ok(new ResponseMessage("Dane wynagrodzeń dodane do bazy danych"));
    }

    private Salary getExistingSalary(List<Salary> existingRecords, String key) {
        for (Salary existingSalary : existingRecords) {
            if (generateKeyFromSalary(existingSalary).equals(key)) {
                return existingSalary;
            }
        }
        return null;
    }

    private void updateExistingSalary(Salary existingSalary, Map<String, Object> employeeData) {
        // Оновлення існуючого запису на основі нових даних з employeeData
        existingSalary.setSalary_amount(getSumOfWorkHours(
                (Long) employeeData.get("employee_id"),
                (employeeData.get("month")).toString(),
                employeeData.get("year").toString()
        ));
        existingSalary.setHousing_cost(getSumOfHousingCost((Long) employeeData.get("employee_id"),
                (employeeData.get("month")).toString(),
                employeeData.get("year").toString()));
        existingSalary.setDeductions(getSumOfDeductions((Long) employeeData.get("employee_id"),
                (employeeData.get("month")).toString(),
                employeeData.get("year").toString()));
        existingSalary.setAdvances(getSumOfAdvances((Long) employeeData.get("employee_id"),
                (employeeData.get("month")).toString(),
                employeeData.get("year").toString()));
        existingSalary.setPenalties(getSumOfPenalties((Long) employeeData.get("employee_id"),
                (employeeData.get("month")).toString(),
                employeeData.get("year").toString()));
        double net_pay = (existingSalary.getSalary_amount() - existingSalary.getHousing_cost() -
                existingSalary.getDeductions() - existingSalary.getAdvances() - existingSalary.getPenalties());
        if (net_pay < 0.0) {
            existingSalary.setNet_payout(0.0);
        } else {
            existingSalary.setNet_payout(existingSalary.getSalary_amount() - existingSalary.getHousing_cost() -
                    existingSalary.getDeductions() - existingSalary.getAdvances() - existingSalary.getPenalties());
        }
        // Додаткове оновлення інших полів, якщо потрібно
    }

    private java.sql.Date localDateToSqlDate(LocalDate localDate) {
        return java.sql.Date.valueOf(localDate);
    }


    public List<Map<String, Object>> getEmployeeIdsForMonth(java.sql.Date startDate, java.sql.Date endDate) {
        try {
            String query = "SELECT DISTINCT pa.employee_id, pa.employee_contract_start, pa" +
                    ".employee_contract_end, " +
                    " EXTRACT(MONTH FROM CAST(? AS DATE)) AS month, " +
                    " EXTRACT(YEAR FROM CAST(? AS DATE)) AS year " +
                    "                     FROM pracownicy pa " +
                    "                     WHERE pa.employee_accommodation_date <= ? " +
                    "                        AND (pa.employee_departure_date IS NULL OR pa.employee_departure_date >= ?)";
            Object[] params = new Object[]{endDate, endDate, endDate, startDate};
            return jdbcTemplate.queryForList(query, params);
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    private Map<String, Boolean> createExistingRecordsMap(List<Salary> existingRecords) {
        Map<String, Boolean> existingRecordsMap = new HashMap<>();
        for (Salary existingRecord : existingRecords) {
            String key = generateKeyFromSalary(existingRecord);
            existingRecordsMap.put(key, true);
        }
        return existingRecordsMap;
    }

    private String generateKeyFromSalary(Salary salary) {
        return salary.getEmployee_id() + "-" + salary.getYear() + "-" +
                salary.getMonth();
    }


    private String generateKeyFromEmployeeData(Map<String, Object> employeeData) {
        Long employeeId = (Long) employeeData.get("employee_id");
        String yearNew = employeeData.get("year").toString();
        String monthNew = employeeData.get("month").toString();
        return employeeId + "-" + yearNew + "-" + monthNew;
    }

    private Salary createSalaryFromEmployeeData(Map<String, Object> employeeData) {
        Salary salaryRecord = new Salary();
        salaryRecord.setEmployee_id((Long) employeeData.get("employee_id"));
        salaryRecord.setMonth((employeeData.get("month")).toString());
        salaryRecord.setYear((employeeData.get("year")).toString());
        salaryRecord.setSalary_amount(getSumOfWorkHours(
                (Long) employeeData.get("employee_id"),
                (employeeData.get("month")).toString(),
                employeeData.get("year").toString()
        ));
        salaryRecord.setHousing_cost(getSumOfHousingCost((Long) employeeData.get("employee_id"),
                (employeeData.get("month")).toString(),
                employeeData.get("year").toString()));
        salaryRecord.setDeductions(getSumOfDeductions((Long) employeeData.get("employee_id"),
                (employeeData.get("month")).toString(),
                employeeData.get("year").toString()));
        salaryRecord.setAdvances(getSumOfAdvances((Long) employeeData.get("employee_id"),
                (employeeData.get("month")).toString(),
                employeeData.get("year").toString()));
        salaryRecord.setPenalties(getSumOfPenalties((Long) employeeData.get("employee_id"),
                (employeeData.get("month")).toString(),
                employeeData.get("year").toString()));
        double net_pay = (salaryRecord.getSalary_amount() - salaryRecord.getHousing_cost() -
                salaryRecord.getDeductions() - salaryRecord.getAdvances() - salaryRecord.getPenalties());
        if (net_pay < 0.0) {
            salaryRecord.setNet_payout(0.0);
        } else {
            salaryRecord.setNet_payout(salaryRecord.getSalary_amount() - salaryRecord.getHousing_cost() -
                    salaryRecord.getDeductions() - salaryRecord.getAdvances() - salaryRecord.getPenalties());
        }
        return salaryRecord;
    }

    public Double getSumOfWorkHours(Long employee_id, String month, String year) {
        try {
            String query = "SELECT " +
                    "SUM(CAST(gp.work_hours AS numeric)) * CAST(st.hourly_rate AS numeric) AS total_salary " +
                    "FROM godzinypracy gp " +
                    "INNER JOIN pracownicy p ON gp.employee_id = p.employee_id " +
                    "INNER JOIN stawki st ON st.rate_id = p.employee_position_id " +
                    "WHERE gp.employee_id = ? AND gp.month = ? " +
                    "AND gp.year = ? " +
                    "GROUP BY p.employee_id, st.hourly_rate;";

            Object[] params = new Object[]{employee_id, month, year};

            Double totalSalary = jdbcTemplate.queryForObject(query, params, Double.class);
            return totalSalary != null ? totalSalary : 0.0;
        } catch (EmptyResultDataAccessException e) {
            return 0.0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        }
    }

    public Double getSumOfHousingCost(Long employee_id, String month, String year) {
        try {
            String query =
                    "SELECT SUM(CAST(cost as  numeric)) as total_cost " +
                            "FROM  kosztyzakwaterowania " +
                            "WHERE employee_id = ? AND month = ? " +
                            "AND year = ? " +
                            "GROUP BY employee_id;";

            Object[] params = new Object[]{employee_id, month, year};

            Double totalCost = jdbcTemplate.queryForObject(query, params, Double.class);
            return totalCost != null ? totalCost : 0.0;
        } catch (EmptyResultDataAccessException e) {
            return 0.0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        }
    }

    public Double getSumOfDeductions(Long employee_id, String month, String year) {
        try {
            String query =
                    "SELECT SUM(cast(amount as numeric)) as total_amount " +
                            "FROM potracenia " +
                            "WHERE employee_id = ? AND month = ? " +
                            "AND year = ? " +
                            "GROUP BY employee_id;";

            Object[] params = new Object[]{employee_id, month, year};

            Double totalDeductions = jdbcTemplate.queryForObject(query, params, Double.class);
            return totalDeductions != null ? totalDeductions : 0.0;
        } catch (EmptyResultDataAccessException e) {
            return 0.0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        }
    }

    public Double getSumOfAdvances(Long employee_id, String month, String year) {
        try {
            String query =
                    "SELECT SUM(CAST(advance_amount as numeric)) as total_advance_amount " +
                            "FROM avanse " +
                            "WHERE employee_id = ? AND extract(month from advance_date ) =  cast( ? as numeric) " +
                            "AND extract(year from advance_date) = cast( ? as numeric) " +
                            "GROUP BY employee_id;";

            Object[] params = new Object[]{employee_id, month, year};

            Double totalDeductions = jdbcTemplate.queryForObject(query, params, Double.class);
            return totalDeductions != null ? totalDeductions : 0.0;
        } catch (EmptyResultDataAccessException e) {
            return 0.0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        }
    }

    public Double getSumOfPenalties(Long employee_id, String month, String year) {
        try {

            String query =
                    "SELECT SUM(CAST(penalty_amount as numeric)) as total_penalty_amount " +
                            "FROM kary " +
                            "WHERE employee_id = ? AND month = ? " +
                            "AND year = ? " +
                            "GROUP BY employee_id;";

            Object[] params = new Object[]{employee_id, month, year};

            Double totalPenalties = jdbcTemplate.queryForObject(query, params, Double.class);
            return totalPenalties != null ? totalPenalties : 0.0;
        } catch (EmptyResultDataAccessException e) {
            return 0.0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        }
    }
}

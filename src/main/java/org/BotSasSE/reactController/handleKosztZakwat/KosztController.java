package org.BotSasSE.reactController.handleKosztZakwat;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.BotSasSE.reactController.ResponseMessage;
import org.BotSasSE.reactController.repository.*;
import org.BotSasSE.reactController.repository.KosztZakwaterRepository;
import org.BotSasSE.reactController.tableEntity.Adres;
import org.BotSasSE.reactController.tableEntity.KosztZakwater;
import org.BotSasSE.reactController.tableEntity.Worker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/koszt")
public class KosztController {

    private final KosztZakwaterRepository kosztZakwaterRepository;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public KosztController(KosztZakwaterRepository kosztZakwaterRepository, JdbcTemplate jdbcTemplate) {
        this.kosztZakwaterRepository = kosztZakwaterRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @PersistenceContext
    private EntityManager entityManager;

    @PostMapping("/checkAndUpdate")
    public ResponseEntity<ResponseMessage> checkAndUpdateKosztForYearAndMonth(
            @RequestBody Map<String, String> request) {
        String year = request.get("year");
        String month = request.get("month");

        List<KosztZakwater> existingRecords = kosztZakwaterRepository.findByYearAndMonth(year, month);
        Map<String, Boolean> existingRecordsMap = createExistingRecordsMap(existingRecords);

        LocalDate endDateLocal = LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), 1);
        LocalDate startDateLocal = endDateLocal.withDayOfMonth(endDateLocal.lengthOfMonth());
        java.sql.Date endDate = localDateToSqlDate(endDateLocal);
        java.sql.Date startDate = localDateToSqlDate(startDateLocal);

        List<Map<String, Object>> allEmployeeForMonthYear = getEmployeeIdsForMonth(startDate, endDate);

        for (Map<String, Object> employeeData : allEmployeeForMonthYear) {
            String key = generateKeyFromEmployeeData(employeeData);
            if (!existingRecordsMap.containsKey(key)) {
                KosztZakwater kosztRecord = createKosztZakwaterFromEmployeeData(employeeData);
                kosztZakwaterRepository.save(kosztRecord);
                existingRecordsMap.put(key, true);
            }
        }

        return ResponseEntity.ok(new ResponseMessage("Dane kosztów zakwaterowania dodane do bazy danych"));
    }



    private Map<String, Boolean> createExistingRecordsMap(List<KosztZakwater> existingRecords) {
        Map<String, Boolean> existingRecordsMap = new HashMap<>();
        for (KosztZakwater existingRecord : existingRecords) {
            String key = generateKeyFromKosztZakwater(existingRecord);
            existingRecordsMap.put(key, true);
        }
        return existingRecordsMap;
    }

    private String generateKeyFromKosztZakwater(KosztZakwater kosztZakwater) {
        return kosztZakwater.getEmployee_id() + "-" + kosztZakwater.getYear() + "-" +
                kosztZakwater.getMonth() + "-" + kosztZakwater.getResidence_address_id();
    }

    private String generateKeyFromEmployeeData(Map<String, Object> employeeData) {
        Long employeeId = (Long) employeeData.get("employee_id");
        Long residenceAddressId = (Long) employeeData.get("residence_address_id");
        String yearNew = ((BigDecimal) employeeData.get("year")).toString();
        String monthNew = ((BigDecimal) employeeData.get("month")).toString();
        return employeeId + "-" + yearNew + "-" + monthNew + "-" + residenceAddressId;
    }

    private KosztZakwater createKosztZakwaterFromEmployeeData(Map<String, Object> employeeData) {
        KosztZakwater kosztRecord = new KosztZakwater();
        kosztRecord.setEmployee_id((Long) employeeData.get("employee_id"));
        kosztRecord.setResidence_address_id((Long) employeeData.get("residence_address_id"));
        kosztRecord.setStart_date((java.sql.Date) employeeData.get("start_date"));
        kosztRecord.setEnd_date((java.sql.Date) employeeData.get("end_date"));
        kosztRecord.setMonth(((BigDecimal) employeeData.get("month")).toString());
        kosztRecord.setYear(((BigDecimal) employeeData.get("year")).toString());
        BigDecimal costBigDecimal = new BigDecimal(employeeData.get("cost").toString());
        BigDecimal roundedCost = costBigDecimal.setScale(2, RoundingMode.HALF_UP);
        kosztRecord.setCost(roundedCost.toString());
        return kosztRecord;
    }

    private java.sql.Date localDateToSqlDate(LocalDate localDate) {
        return java.sql.Date.valueOf(localDate);
    }

    public List<Map<String, Object>> getEmployeeIdsForMonth(java.sql.Date startDate, java.sql.Date endDate) {
        try {
            String query = "SELECT DISTINCT pa.employee_id, pa.residence_address_id, am.value_accommodation,pa.start_date,pa.end_date," +
                    "    EXTRACT(MONTH FROM CAST(? AS DATE)) AS month," +
                    "    EXTRACT(YEAR FROM CAST(? AS DATE)) AS year," +
                    "(am.value_accommodation * " +
                    "    (CASE" +
                    "        WHEN EXTRACT(MONTH FROM pa.start_date) = EXTRACT(MONTH FROM CAST(? AS DATE))" +
                    "            AND EXTRACT(YEAR FROM pa.start_date) = EXTRACT(YEAR FROM CAST(? AS DATE))" +
                    "        THEN" +
                    "            CASE" +
                    "                WHEN EXTRACT(MONTH FROM pa.end_date) = EXTRACT(MONTH FROM CAST(? AS DATE))" +
                    "                    AND EXTRACT(YEAR FROM pa.end_date) = EXTRACT(YEAR FROM CAST(? AS DATE))" +
                    "                THEN" +
                    "                    CASE" +
                    "                        WHEN pa.start_date = pa.end_date" +
                    "                        THEN 1" +
                    "                        ELSE DATE_PART('day', pa.end_date) - DATE_PART('day', pa.start_date) + 1" +
                    "                    END" +
                    "                ELSE DATE_PART('day', CAST(? AS DATE)) - DATE_PART('day', pa.start_date) + 1" +
                    "            END" +
                    "        ELSE" +
                    "            CASE" +
                    "                WHEN EXTRACT(MONTH FROM pa.end_date) = EXTRACT(MONTH FROM CAST(? AS DATE))" +
                    "                THEN DATE_PART('day', pa.end_date)" +
                    "                ELSE DATE_PART('day', CAST(? AS DATE)) - DATE_PART('day', CAST(? AS DATE)) + 1" +
                    "            END" +
                    "    END)) AS COST" +
                    " FROM pracownikadres pa " +
                    " INNER JOIN AdresyMieszkan am ON pa.residence_address_id = am.residence_address_id" +
                    " WHERE pa.start_date <= ? " +
                    "    AND (pa.end_date IS NULL OR pa.end_date >= ?);";
            Object[] params = new Object[]{startDate, startDate, startDate, startDate, startDate, startDate,
                    startDate, startDate, startDate, endDate, startDate, endDate};
            return jdbcTemplate.queryForList(query, params);
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return Collections.emptyList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}

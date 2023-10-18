package org.BotSasSE.botComands.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.time.temporal.WeekFields;
import java.util.Locale;

import java.util.List;
import java.util.Map;

@Service
public class DbHandler {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DbHandler(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public String searchUserByPesel(String userPesel) {

        try {
            String query = "SELECT employee_first_name, employee_last_name FROM pracownicy WHERE employee_pesel = ?";
            Object[] params = new Object[]{userPesel};
            List<Map<String, Object>> results = jdbcTemplate.queryForList(query, params);
            if (!results.isEmpty()) {
                Map<String, Object> result = results.get(0);
                String firstName = (String) result.get("employee_first_name");
                String lastName = (String) result.get("employee_last_name");
                return "Witam " + firstName + "  " + lastName + "! Wasz numer PESEL został wprowadzony.";
            } else {
                return "Error";
            }
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return "Błąd";
        } catch (Exception e) {
            e.printStackTrace();
            return "Błąd";
        }
    }


    private Long getEmployeeId(String userPesel) {
        try {
            String employeeIdQuery = "SELECT employee_id FROM pracownicy WHERE employee_pesel = ?";
            Object[] employeeIdParams = new Object[]{userPesel};
            List<Map<String, Object>> employeeIdResults = jdbcTemplate.queryForList(employeeIdQuery, employeeIdParams);
            if (!employeeIdResults.isEmpty()) {
                Map<String, Object> employeeIdResult = employeeIdResults.get(0);
                return (Long) employeeIdResult.get("employee_id");
            } else {
                return null;
            }
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String searchUserByPassport(String userPesel) {

        try {
            String query = "SELECT employee_passport_number FROM pracownicy WHERE employee_pesel = ?";
            Object[] params = new Object[]{userPesel};
            List<Map<String, Object>> results = jdbcTemplate.queryForList(query, params);
            if (!results.isEmpty()) {
                Map<String, Object> result = results.get(0);
                return (String) result.get("employee_passport_number");
            } else {
                System.out.println("Blad");
                return "Error";
            }
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return "Error";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error";
        }
    }

    public String sentAdvanceRequest(String userPesel) {

        try {
            String query = "SELECT employee_id, employee_status FROM pracownicy WHERE employee_pesel = ?";
            Object[] params = new Object[]{userPesel};
            List<Map<String, Object>> results = jdbcTemplate.queryForList(query, params);
            if (!results.isEmpty()) {
                Map<String, Object> result = results.get(0);
                String status = (String) result.get("employee_status");
                Long employeeId = (Long) result.get("employee_id");
                LocalDate currentDate = LocalDate.now();
                int week = currentDate.get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear());
                int weekNumber = currentDate.get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear()) + 1;
                DayOfWeek dayOfWeek = currentDate.getDayOfWeek();
                if (status.trim().equalsIgnoreCase("pracuje")) {
                    String sentQuery = "INSERT INTO avanse  (advance_date, employee_id, advance_amount, week_number) values (?, ?, ?, ?)";
                    Object[] sentParams = new Object[]{currentDate, employeeId, 300, week};
                    jdbcTemplate.update(sentQuery, sentParams);
                    if (dayOfWeek == DayOfWeek.MONDAY) {
                        return "Dziękuję, wniosek na otrzymanie zaliczki przyjęty! W razie akceptacji wniosku, zaliczka będzie wypłacona w dniu " + currentDate.plusDays(4);
                    } else {
                        DayOfWeek desiredDayOfWeek = DayOfWeek.FRIDAY;
                        LocalDate desiredDate = currentDate.with(ChronoField.DAY_OF_WEEK, desiredDayOfWeek.getValue()).with(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear(), weekNumber);
                        return "Dziękuję, wniosek na otrzymanie zaliczki przyjęty! W razie akceptacji wniosku, zaliczka będzie wypłacona w dniu " + desiredDate;
                    }

                } else {
                    return "Zaliczki tylko dla pracujących";
                }
            } else {
                return "Error";
            }
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return "Błąd";
        } catch (Exception e) {
            e.printStackTrace();
            return "Błąd";
        }
    }


    public String getSumWorkHour(String userPesel, String month, String year) {

        try {
            Long employeeId = getEmployeeId(userPesel);
            if (employeeId != null) {

                String query = "SELECT sum(CAST(godzinypracy.work_hours AS INTEGER)) AS totalWorkHours FROM godzinypracy " + "WHERE (godzinypracy.employee_id = ? and month = ? and year = ?)";
                Object[] params = new Object[]{employeeId, month, year};
                List<Map<String, Object>> results = jdbcTemplate.queryForList(query, params);

                if (!results.isEmpty()) {
                    Map<String, Object> result = results.get(0);
                    Long totalWorkHours = (Long) result.get("totalWorkHours");
                    if (totalWorkHours != null) {
                        return String.valueOf(totalWorkHours);
                    } else {
                        return "0";
                    }
                } else {
                    return "Error";
                }
            } else {
                System.out.println("Blad");
                return "Error";
            }
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return "Error";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error";
        }
    }

    public String setHourOfWork(String userPesel, String year, String month, String day, String hour) {

        try {
            Long employeeId = getEmployeeId(userPesel);
            if (employeeId != null) {

                String checkQuery = "SELECT work_hours FROM public.godzinypracy WHERE employee_id = ? AND year = " + "? AND month=? AND day=?";
                Object[] checkParams = new Object[]{employeeId, year, month, day};
                List<Map<String, Object>> checkResults = jdbcTemplate.queryForList(checkQuery, checkParams);

                if (checkResults.isEmpty()) {
                    String sentQuery = "INSERT INTO godzinypracy  (employee_id, year, month, day, work_hours) values (?, ?, " + "?, ?, ?)";
                    Object[] sentParams = new Object[]{employeeId, year, month, day, hour};
                    jdbcTemplate.update(sentQuery, sentParams);
                    return "Dane zapisano";
                } else {
                    return "GOdziny dla wskazanego dnia już wprowadzone. Wybierz inny dzień";
                }

            } else {
                return "Error";
            }
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return "Error";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error";
        }
    }


    public String checkHourOfWork(String userPesel, String year, String month, String day) {

        try {
            Long employeeId = getEmployeeId(userPesel);
            if (employeeId != null) {

                String checkQuery = "SELECT work_hours FROM public.godzinypracy WHERE employee_id = ? AND year = " + "? AND month=? AND day=?";
                Object[] checkParams = new Object[]{employeeId, year, month, day};
                List<Map<String, Object>> checkResults = jdbcTemplate.queryForList(checkQuery, checkParams);

                if (checkResults.isEmpty()) {
                    return "ok";
                } else {
                    return "Godziny dla wskazanego dnia już wprowadzone. Wybierz inny dzień";
                }
            } else {
                return "Error";
            }
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return "Error";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error";
        }
    }

    public String getSalaryCalculation(String userPesel, String year, String month) {
        String salary_amount;
        String housing_cost;
        String deductions;
        String advances;
        String penalties;
        String net_payout;

        try {
            Long employeeId = getEmployeeId(userPesel);
            if (employeeId != null) {

                String query = "SELECT salary_amount, housing_cost, deductions, advances, penalties, net_payout FROM wyplaty WHERE employee_id = ? AND year=?" +
                        " AND month = ?";

                Object[] params = new Object[]{employeeId, year, month};
                List<Map<String, Object>> results = jdbcTemplate.queryForList(query, params);

                if (!results.isEmpty()) {
                    Map<String, Object> result = results.get(0);

                    salary_amount = (String) result.get("salary_amount");
                    housing_cost = (String) result.get("housing_cost");
                    deductions = (String) result.get("deductions");
                    advances = (String) result.get("advances");
                    penalties = (String) result.get("penalties");
                    net_payout = (String) result.get("net_payout");

                    String printResult =
                            "\n Suma godzin pracy za wybrany miesiąc " + month + " " + year + ": " + getSumWorkHour(userPesel, month, year) + ". Suma " +
                                    "wynagrodzenia do potrąceń: " + salary_amount + "\n" + "Potrącenia z pencji: \n";

                    if (!housing_cost.equals("0")) {
                        printResult += "za mieszkanie: " + housing_cost + "\n";
                    }
                    if (!deductions.equals("0")) {
                        printResult += "potrącenia zgodnie z umową: " + deductions + "\n";
                    }
                    if (!advances.equals("0")) {
                        printResult += "otrzymane zaliczki: " + advances + "\n";
                    }
                    if (!penalties.equals("0")) {
                        printResult += "kary umowne: " + penalties + "\n";
                    }
                    if (!net_payout.equals("0")) {
                        printResult += "Do wypłaty: " + net_payout + "\n";
                    }
                    return printResult;
                } else {
                    return "0";
                }
            } else {
                return "Error";
            }

        } catch (
                EmptyResultDataAccessException e) {
            e.printStackTrace();
            return "Error";
        } catch (
                Exception e) {
            e.printStackTrace();
            return "Error";
        }
    }


    public String surnameUserforPhoto(String userPesel) {

        try {
            String query = "SELECT employee_first_name, employee_last_name FROM pracownicy WHERE employee_pesel = ?";
            Object[] params = new Object[]{userPesel};
            List<Map<String, Object>> results = jdbcTemplate.queryForList(query, params);
            if (!results.isEmpty()) {
                Map<String, Object> result = results.get(0);
                String firstName = (String) result.get("employee_first_name");
                String lastName = (String) result.get("employee_last_name");
                return firstName + "_" + lastName;
            } else {
                return "Error";
            }
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return "Błąd";
        } catch (Exception e) {
            e.printStackTrace();
            return "Błąd";
        }
    }

}
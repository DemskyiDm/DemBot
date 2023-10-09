package org.BotSasSE.reactController.handleSalary;

import org.BotSasSE.reactController.repository.SalaryRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
public class HandleComponents {
    private SalaryRepository salaryRepository;
    private final JdbcTemplate jdbcTemplate;


    public HandleComponents(SalaryRepository salaryRepository, JdbcTemplate jdbcTemplate) {
        this.salaryRepository = salaryRepository;
        this.jdbcTemplate = jdbcTemplate;
    }




}

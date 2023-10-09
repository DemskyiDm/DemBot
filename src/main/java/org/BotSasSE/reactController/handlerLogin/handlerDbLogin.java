package org.BotSasSE.reactController.handlerLogin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class handlerDbLogin {

    private static JdbcTemplate jdbcTemplate;

    @Autowired
    public handlerDbLogin(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public static boolean searchLogin(String user, String pass) {

        try {
            String query = "SELECT userpassword, userrole FROM login WHERE userlogin = ?";
            Object[] params = new Object[]{user};
            List<Map<String, Object>> results = jdbcTemplate.queryForList(query, params);
            if (!results.isEmpty()) {
                Map<String, Object> result = results.get(0);
                String password = (String) result.get("userpassword");
                return password.equals(pass);
            } else {
                return false;
            }
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public static String searchRole(String user, String pass) {

        try {
            String query = "SELECT userrole FROM login WHERE userlogin = ? AND userpassword = ?";
            Object[] params = new Object[]{user, pass};

            String role = jdbcTemplate.queryForObject(query, params, String.class);

            return role;

        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
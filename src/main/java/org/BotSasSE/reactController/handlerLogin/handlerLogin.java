package org.BotSasSE.reactController.handlerLogin;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
@RequestMapping("/api/auth")
public class handlerLogin {

    @Autowired
    private handlerDbLogin dbLogin;

    @PostMapping("/login")
    public ResponseEntity<?> handleLogin(@RequestBody LoginRequest loginRequest) {
        // get user and password
        String username = loginRequest.getUserLogin();
        String password = loginRequest.getUserPassword();

        // checking
        if (checkCredentials(username, password)) {
            // Generate JWT token
            String role = dbLogin.searchRole(username, password);
            System.out.println(role);
            String jwtToken = generateJwtToken(role);

            // Create a JSON response with token and role
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("token", jwtToken);
            jsonResponse.put("role", role);

            return ResponseEntity.ok(jsonResponse.toString());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    private boolean checkCredentials(String username, String password) {
        return handlerDbLogin.searchLogin(username, password);
    }

    private String generateJwtToken(String role) {
        String secretKey = "yourSecretKey"; // Replace with your actual secret key
        return Jwts.builder()
                .claim("role", role)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

}


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

public class JwtGenerator {
    public static void main(String[] args) {
        String secret = "4f1feeca525de4cdb064656007da3edac7895a87ff0ea865693300fb8b6e8f9c";
        Key signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

        String token = Jwts.builder()
                .setSubject("usuario_prueba")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hora
                .signWith(signingKey)
                .compact();

        System.out.println("Token JWT generado: " + token);
    }
}
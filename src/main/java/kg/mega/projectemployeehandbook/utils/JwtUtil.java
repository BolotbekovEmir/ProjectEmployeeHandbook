package kg.mega.projectemployeehandbook.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import kg.mega.projectemployeehandbook.models.dto.admin.TokenAdminDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;
import java.util.List;

/**
 * Утилита для работы с JWT-токенами.
 */
@Component
public class JwtUtil {

    @Value("${jwt.lifetime}")
    private Duration lifetime;

    SecretKey secretKey = Jwts.SIG.HS256.key().build();

    /**
     * Генерация JWT-токена для пользователя.
     * @param user Информация о пользователе.
     * @return Сгенерированный JWT-токен.
     */
    public String generateToken(UserDetails user) {
        Date issuedDate = new Date();
        Date expirationDate = new Date(issuedDate.getTime() + lifetime.toNanos());

        return Jwts.builder()
            .claim("roles", user.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList())
            .subject(user.getUsername())
            .issuedAt(issuedDate)
            .expiration(expirationDate)
            .signWith(secretKey)
            .compact();
    }

    /**
     * Парсинг JWT-токена для получения информации о пользователе.
     * @param token JWT-токен.
     * @return Информация о пользователе из токена.
     */
    public TokenAdminDTO parseToken(String token) {
        TokenAdminDTO userTokenDTO = new TokenAdminDTO();
        Claims claims = getClaims(token);
        userTokenDTO.setToken(token);
        userTokenDTO.setAdminName(claims.getSubject());
        userTokenDTO.setActiveTill(claims.getExpiration());
        return userTokenDTO;
    }

    /**
     * Получение данных из токена.
     * @param token JWT-токен.
     * @return Данные из токена.
     */
    private Claims getClaims(String token) {
        return Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }

    /**
     * Получение имени администратора из токена.
     * @param token JWT-токен.
     * @return Имя администратора.
     */
    public String getAdminName(String token) {
        return getClaims(token).getSubject();
    }

    /**
     * Получение списка ролей из токена.
     * @param token JWT-токен.
     * @return Список ролей.
     */
    public List<String> getRoles(String token) {
        return getClaims(token).get("roles", List.class);
    }
}

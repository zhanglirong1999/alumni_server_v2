package cn.edu.seu.alumni_server.common.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

public class TokenUtil {
    public static final String TOKEN_HEADER = "X-Wx-Token";
    static final long EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7;
    static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // TODO 换成redis
    public static volatile ConcurrentHashMap<String, String> tokens = new ConcurrentHashMap<String, String>();

    public static boolean checkToken(String token) {
        if (StringUtils.isEmpty(token)) {
            return false;
        }
        try {
            String accountId = parseJWT(token);
            if (tokens.get(accountId).equals(token)) {
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String createToken(String accountId) {
        if (tokens.get(accountId) != null) {
            return tokens.get(accountId);
        }

        String token = createJWT(accountId);
        tokens.put(accountId, token);
        return token;
    }

    public static Long getAccountId(String token) throws Exception {
        return Long.parseLong(parseJWT(token));
    }

    static String createJWT(String accountId) {

        String jws = Jwts.builder()
                .setSubject(accountId)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_TIME))
                .signWith(key)
                .compact();

        return jws;
    }

    static String parseJWT(String jws) throws Exception {
        if (jws != null) {
            Jws<Claims> jwsObj = Jwts.parser().setSigningKey(key).parseClaimsJws(jws);

            String accountId = jwsObj.getBody().getSubject();

            return accountId;
        }
        return null;
    }

}

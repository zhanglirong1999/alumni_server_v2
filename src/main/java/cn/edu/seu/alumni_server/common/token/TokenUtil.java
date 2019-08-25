package cn.edu.seu.alumni_server.common.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

public class TokenUtil {
    static final long EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7;     // 7天
    static final String SECRET = "password@1";            // 密码
    static final String TOKEN_PREFIX = "Bearer";        // Token前缀
    static final String HEADER_STRING = "Authorization";// 存放Token的Header Key
    static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256); //

    public static String createJWT(String accountId) {

        String jws = Jwts.builder()
                .setSubject(accountId)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_TIME))
                .signWith(key)
                .compact();

        return jws;
    }

    public static String parseJWT(String jws) throws Exception {
        if (jws != null) {
            Jws<Claims> jwsObj = Jwts.parser().setSigningKey(key).parseClaimsJws(jws);

            String accountId = jwsObj.getBody().getSubject();

            return accountId;
        }
        return null;
    }
}

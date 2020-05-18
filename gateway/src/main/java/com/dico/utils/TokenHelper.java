package com.dico.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

/**
 * token帮助类
 *
 * @author Gaodl
 * @version v1.0
 * 文件名称: TokenHelper
 * 创建时间: 2018/12/11
 */
@Slf4j
public class TokenHelper {

    @Value("${jwt.secret}")
    public String SECRET;

    @Value("${jwt.header}")
    private String AUTH_HEADER;

    static final String AUDIENCE_UNKNOWN = "unknown";

    private SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;


    public TokenHelper() {
        super();
    }

    public TokenHelper(String SECRET, String AUTH_HEADER) {
        this.SECRET = SECRET;
        this.AUTH_HEADER = AUTH_HEADER;
    }

    /**
     * 获取token中所有的Claims
     *
     * @author Gaodl
     * 方法名称: getAllClaimsFromToken
     * 参数： [token]
     * 返回值： io.jsonwebtoken.Claims
     * 创建时间: 2018/12/11
     */
    private Claims getAllClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    /**
     * 获取token失效时间
     *
     * @author Gaodl
     * 方法名称: getExpirationDateFromToken
     * 参数： [token]
     * 返回值： java.util.Date
     * 创建时间: 2018/12/11
     */
    public Date getExpirationDateFromToken(String token) {
        Date expiration;
        try {
            final Claims claims = this.getAllClaimsFromToken(token);
            expiration = claims.getExpiration();
        } catch (Exception e) {
            expiration = null;
        }
        return expiration;
    }

    /**
     * 判断token是否失效
     *
     * @author Gaodl
     * 方法名称: isTokenExpired
     * 参数： [token]
     * 返回值： java.lang.Boolean
     * 创建时间: 2018/12/11
     */
    private Boolean isTokenExpired(String token) {
        boolean flag = true;
        try {
            final Date expiration = getExpirationDateFromToken(token);
            flag = expiration.before(new Date());
        } catch (Exception e) {
            log.error("鉴权失败");
            flag = true;
        }
        return flag;
    }

    /**
     * 校验token是否合法
     *
     * @author Gaodl
     * 方法名称: validateToken
     * 参数： [token]
     * 返回值： java.lang.Boolean
     * 创建时间: 2018/12/11
     */
    public Boolean validateToken(String token) {
        return isTokenExpired(token);
    }
}

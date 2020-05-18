package com.dico.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mobile.device.Device;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * token帮助类
 *
 * @author Gaodl
 * @version v1.0
 * 文件名称: TokenHelper
 * 创建时间: 2018/12/11
 */
public class TokenHelper {

    @Value("${spring.application.name}")
    private String APP_NAME;

    @Value("${jwt.secret}")
    public String SECRET;

    @Value("${jwt.expires_in}")
    private int EXPIRES_IN;

    @Value("${jwt.mobile_expires_in}")
    private int MOBILE_EXPIRES_IN;

    @Value("${jwt.header}")
    private String AUTH_HEADER;

    static final String AUDIENCE_UNKNOWN = "unknown";
    static final String AUDIENCE_WEB = "web";
    static final String AUDIENCE_MOBILE = "mobile";
    static final String AUDIENCE_TABLET = "tablet";

    private SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;

    /**
     * 从HttpServletRequest中获取用户ID
     *
     * @author Gaodl
     * 方法名称: getUserIdByRequest
     * 参数： [request]
     * 返回值： java.lang.String
     * 创建时间: 2018/12/20
     */
    public String getUserIdByRequest(HttpServletRequest request) {
        return this.getUserIdFromToken(this.getToken(request));
    }

    /**
     * 从token中获取用户ID
     *
     * @author Gaodl
     * 方法名称: getUserIdFromToken
     * 参数： [token]
     * 返回值： java.lang.String
     * 创建时间: 2018/12/20
     */
    private String getUserIdFromToken(String token) {
        String userId;
        try {
            final Claims claims = this.getAllClaimsFromToken(token);
            userId = claims.getSubject();
        } catch (Exception e) {
            userId = null;
        }
        return userId;
    }

    /**
     * 获取签发时间
     *
     * @author Gaodl
     * 方法名称: getIssuedAtDateFromToken
     * 参数： [token]
     * 返回值： java.util.Date
     * 创建时间: 2018/12/11
     */
    public Date getIssuedAtDateFromToken(String token) {
        Date issueAt;
        try {
            final Claims claims = this.getAllClaimsFromToken(token);
            issueAt = claims.getIssuedAt();
        } catch (Exception e) {
            issueAt = null;
        }
        return issueAt;
    }

    /**
     * 从token中获取Audience
     *
     * @author Gaodl
     * 方法名称: getAudienceFromToken
     * 参数： [token]
     * 返回值： java.lang.String
     * 创建时间: 2018/12/11
     */
    public String getAudienceFromToken(String token) {
        String audience;
        try {
            final Claims claims = this.getAllClaimsFromToken(token);
            audience = claims.getAudience();
        } catch (Exception e) {
            audience = null;
        }
        return audience;
    }

    /**
     * 刷新token
     *
     * @author Gaodl
     * 方法名称: refreshToken
     * 参数： [token]
     * 返回值： java.lang.String
     * 创建时间: 2018/12/11
     */
    public String refreshToken(String token) {
        String refreshedToken;
        Date a = new Date();
        try {
            final Claims claims = this.getAllClaimsFromToken(token);
            claims.setIssuedAt(a);
            refreshedToken = Jwts.builder()
                    .setClaims(claims)
                    .setExpiration(generateExpirationDate(EXPIRES_IN))
                    .signWith(SIGNATURE_ALGORITHM, SECRET)
                    .compact();
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }

    /**
     * 生成token
     *
     * @author Gaodl
     * 方法名称: generateToken
     * 参数： [userId]
     * 返回值： java.lang.String
     * 创建时间: 2018/12/11
     */
    public String generateToken(String userId, Device device) {
        String audience = generateAudience(device);
        return Jwts.builder()
                .setIssuer(APP_NAME)
                .setSubject(userId)
                .setAudience(audience)
                .setIssuedAt(new Date())
                .setExpiration(generateExpirationDate(EXPIRES_IN))
                .signWith(SIGNATURE_ALGORITHM, SECRET)
                .compact();
    }

    /**
     * 根据请求客户端生成audience
     *
     * @author Gaodl
     * 方法名称: generateAudience
     * 参数： [device]
     * 返回值： java.lang.String
     * 创建时间: 2018/12/25
     */
    private String generateAudience(Device device) {
        String audience = AUDIENCE_UNKNOWN;
        if (device.isNormal()) {
            audience = AUDIENCE_WEB;
        } else if (device.isTablet()) {
            audience = AUDIENCE_TABLET;
        } else if (device.isMobile()) {
            audience = AUDIENCE_MOBILE;
        }
        return audience;
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
     * 设置token失效时间
     *
     * @author Gaodl
     * 方法名称: generateExpirationDate
     * 参数： [expiresIn]
     * 返回值： java.util.Date
     * 创建时间: 2018/12/11
     */
    private Date generateExpirationDate(int expiresIn) {
        expiresIn = expiresIn * 1000 * 60;
        return new Date(System.currentTimeMillis() + expiresIn);
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
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
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

    /**
     * 获取HttpServletRequest中的token
     *
     * @author Gaodl
     * 方法名称: getToken
     * 参数： [request]
     * 返回值： java.lang.String
     * 创建时间: 2018/12/11
     */
    public String getToken(HttpServletRequest request) {
        String authHeader = getAuthHeaderFromHeader(request);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    /**
     * 获取HttpServletRequest中定义存储token的key对应的token值
     *
     * @author Gaodl
     * 方法名称: getAuthHeaderFromHeader
     * 参数： [request]
     * 返回值： java.lang.String
     * 创建时间: 2018/12/11
     */
    public String getAuthHeaderFromHeader(HttpServletRequest request) {
        return request.getHeader(AUTH_HEADER);
    }

    /**
     * 获取token有效时长
     *
     * @author Gaodl
     * 方法名称: getExpiredIn
     * 参数： []
     * 返回值： int
     * 创建时间: 2018/12/11
     */
    public int getExpiredIn() {
        return EXPIRES_IN;
    }

    /**
     * 获取token有效时长
     *
     * @author Gaodl
     * 方法名称: getMobileExpiresIn
     * 参数： []
     * 返回值： int
     * 创建时间: 2019/4/23
     */
    public int getMobileExpiresIn() {
        return MOBILE_EXPIRES_IN;
    }

}

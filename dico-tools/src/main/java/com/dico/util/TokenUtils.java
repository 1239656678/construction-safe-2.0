package com.dico.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import javax.servlet.http.HttpServletRequest;

@SuppressWarnings("static-access")
public class TokenUtils {

    private static String secret;
    private static String authHeader;

    static {
        secret = "qwertyuiop";
        authHeader = "Authorization";
    }

    public TokenUtils(String secret, String authHeader) {
        this.secret = secret;
        this.authHeader = authHeader;
    }

    /**
     * 从HttpServletRequest中获取用户ID
     *
     * @author Gaodl
     * 方法名称: getUserIdByRequest
     * 参数： [request]
     * 返回值： java.lang.String
     * 创建时间: 2018/12/20
     */
    public static String getUserIdByRequest(HttpServletRequest request) {
        return getUserIdFromToken(getToken(request));
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
    private static String getUserIdFromToken(String token) {
        String userId;
        try {
            final Claims claims = new TokenUtils(secret, authHeader).getAllClaimsFromToken(token);
            userId = claims.getSubject();
        } catch (Exception e) {
            userId = null;
        }
        return userId;
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
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
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
    public static String getToken(HttpServletRequest request) {
        String ah = new TokenUtils(secret, authHeader).getAuthHeaderFromHeader(request);
        if (ah != null && ah.startsWith("Bearer ")) {
            return ah.substring(7);
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
    private String getAuthHeaderFromHeader(HttpServletRequest request) {
        return request.getHeader(authHeader);
    }

}

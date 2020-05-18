package com.dico.util;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

/**
 * MD5加密类
 *
 * @author Gaodl
 * @version v1.0
 * 文件名称: HashUtils
 * 创建时间: 2018/12/13
 */
public class HashUtils {

    public static int HASH_ITERATIONS = 1024;

    /**
     * 简单的不加盐加密方法
     *
     * @author Gaodl
     * 方法名称: toHash
     * 参数： [ALGORITHM_NAME, value]
     * 返回值： java.lang.String
     * 创建时间: 2018/12/13
     */
    public static String toHash(String ALGORITHM_NAME, String value) {
        return new SimpleHash(ALGORITHM_NAME, value).toString();
    }

    /**
     * 不加盐循环加密方法
     *
     * @author Gaodl
     * 方法名称: toHashIterations
     * 参数： [ALGORITHM_NAME, value]
     * 返回值： java.lang.String
     * 创建时间: 2018/12/13
     */
    public static String toHashIterations(String ALGORITHM_NAME, String value) {
        return new SimpleHash(ALGORITHM_NAME, value, HASH_ITERATIONS).toString();
    }

    /**
     * 简单的加盐加密方法
     *
     * @author Gaodl
     * 方法名称: toComplexHash
     * 参数： [ALGORITHM_NAME, value, salt]
     * 返回值： java.lang.String
     * 创建时间: 2018/12/13
     */
    public static String toComplexHash(String ALGORITHM_NAME, String value, String salt) {
        return new SimpleHash(ALGORITHM_NAME, value, ByteSource.Util.bytes(salt)).toString();
    }

    /**
     * 加盐循环加密方法
     *
     * @author Gaodl
     * 方法名称: toComplexHashIterations
     * 参数： [ALGORITHM_NAME, value, salt]
     * 返回值： java.lang.String
     * 创建时间: 2018/12/13
     */
    public static String toComplexHashIterations(String ALGORITHM_NAME, String value, String salt) {
        return new SimpleHash(ALGORITHM_NAME, value, ByteSource.Util.bytes(salt), HASH_ITERATIONS).toString();
    }

    /**
     * 设置加密次数的加密方法
     *
     * @author Gaodl
     * 方法名称: toHash
     * 参数： [ALGORITHM_NAME, value, HASH_ITERATIONS]
     * 返回值： java.lang.String
     * 创建时间: 2018/12/13
     */
    public static String toHash(String ALGORITHM_NAME, String value, int HASH_ITERATIONS) {
        return new SimpleHash(ALGORITHM_NAME, value, HASH_ITERATIONS).toString();
    }

    /**
     * 设置加密次数和盐的加密方法
     *
     * @author Gaodl
     * 方法名称: toComplexHash
     * 参数： [ALGORITHM_NAME, value, salt, HASH_ITERATIONS]
     * 返回值： java.lang.String
     * 创建时间: 2018/12/13
     */
    public static String toComplexHash(String ALGORITHM_NAME, String value, String salt, int HASH_ITERATIONS) {
        return new SimpleHash(ALGORITHM_NAME, value, ByteSource.Util.bytes(salt), HASH_ITERATIONS).toString();
    }


}

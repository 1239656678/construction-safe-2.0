package com.dico.common.shiro;

import com.dico.modules.domain.SysUser;
import com.dico.modules.service.SysUserService;
import com.dico.util.HashUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.Sha1Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import javax.annotation.PostConstruct;

/**
 * 用户鉴权类
 *
 * @author Gaodl
 * @version v1.0
 * 文件名称: CustomRealm
 * 创建时间: 2018/12/12
 */
@Slf4j
public class CustomRealm extends AuthorizingRealm {

    @Lazy
    @Autowired
    private SysUserService sysUserService;

    /**
     * 设定密码校验的Hash算法与迭代次数
     *
     * @author Gaodl
     * 方法名称: initCredentialsMatcher
     * 参数： []
     * 返回值： void
     * 创建时间: 2018/12/11
     */
    @PostConstruct
    public void initCredentialsMatcher() {
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(Sha1Hash.ALGORITHM_NAME);
        matcher.setHashIterations(HashUtils.HASH_ITERATIONS);
        setCredentialsMatcher(matcher);
    }

    /**
     * 授权方法
     *
     * @author Gaodl
     * 方法名称: doGetAuthorizationInfo
     * 参数： [principals]
     * 返回值： org.apache.shiro.authz.AuthorizationInfo
     * 创建时间: 2018/12/11
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }

    /**
     * 鉴权方法
     *
     * @author Gaodl
     * 方法名称: doGetAuthenticationInfo
     * 参数： [authenticationToken]
     * 返回值： org.apache.shiro.authc.AuthenticationInfo
     * 创建时间: 2018/12/11
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) authenticationToken;
        String username = usernamePasswordToken.getUsername();
        // 根据用户名查询数据库
        SysUser sysUser = sysUserService.getUserByUsername(username);
        // 用户不存在
        if (sysUser == null) {
            throw new UnknownAccountException();
        }
        // 用户被禁用
        if (!sysUser.isEnable()) {
            throw new LockedAccountException();
        }
        return new SimpleAuthenticationInfo(new AuthenticationRequest(usernamePasswordToken.getUsername(), String.valueOf(usernamePasswordToken
                .getPassword())), sysUser.getPassword(), ByteSource.Util.bytes(HashUtils.toHash(Md5Hash.ALGORITHM_NAME, username)), getName());
    }
}

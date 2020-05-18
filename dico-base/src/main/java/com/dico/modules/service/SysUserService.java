package com.dico.modules.service;

import com.dico.jpa.dao.JpaDao;
import com.dico.jpa.service.JpaService;
import com.dico.modules.domain.SysUser;
import com.dico.modules.repo.SysUserRepository;
import com.dico.result.ResultData;
import com.dico.util.HashUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.crypto.hash.Sha1Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户service层
 *
 * @author Gaodl
 * @version v1.0
 * 文件名称: UserService
 * 创建时间: 2018/12/12
 */
@Slf4j
@Service
public class SysUserService extends JpaService<SysUser, String> {

    @Autowired
    private SysUserRepository sysUserRepository;

    @Override
    protected JpaDao<SysUser, String> getDao() {
        return sysUserRepository;
    }

    /**
     * @author Gaodl
     * 方法名称: getUserByIdAndDelFlagIsFalse
     * 参数： [id]
     * 返回值： com.dico.modules.domain.SysUser
     * 创建时间: 2018/12/24
     */
    public SysUser getUserByIdAndDelFlagIsFalse(String id) {
        return sysUserRepository.getUserByIdAndDelFlagIsFalse(id);
    }

    public void deleteByIdIn(String[] ids) {
        sysUserRepository.deleteByIdIn(ids);
    }

    /**
     * 没有条件获取所有数据
     *
     * @author Gaodl
     * 方法名称: findAll
     * 参数： []
     * 返回值： java.util.List<com.dico.modules.domain.User>
     * 创建时间: 2018/12/20
     */
    public List<SysUser> findAll() {
        return sysUserRepository.getUserByDelFlagIsFalse();
    }

    /**
     * 根据用户名查询用户
     *
     * @author Gaodl
     * 方法名称: getUserByUsername
     * 参数： [username]
     * 返回值： com.dico.modules.domain.User
     * 创建时间: 2018/12/12
     */
    public SysUser getUserByUsername(String username) {
        return sysUserRepository.getUserByUsername(username);
    }


    /**
     * 保存用户的方法
     *
     * @author Gaodl
     * 方法名称: save
     * 参数： [user]
     * 返回值： void
     * 创建时间: 2018/12/12
     */
    @Override
    public void save(SysUser sysUser) {
        sysUserRepository.save(sysUser);
    }

    /**
     * 更新数据的方法
     *
     * @author Gaodl
     * 方法名称: update
     * 参数： [user]
     * 返回值： void
     * 创建时间: 2018/12/13
     */
    @Override
    public void update(SysUser sysUser) {
        super.update(sysUser);
    }

    /**
     * 修改用户密码
     *
     * @author Gaodl
     * 方法名称: updatePass
     * 参数： [userId, oldPassWord, newPassWord]
     * 返回值： com.dico.result.ResultData
     * 创建时间: 2019/5/17
     */
    public ResultData updatePass(String userId, String oldPassWord, String newPassWord) {
        try {
            SysUser sysUser = this.getUserByIdAndDelFlagIsFalse(userId);
            if (null == sysUser) {
                return ResultData.getFailResult("获取当前用户出错");
            }
            if (!sysUser.getPassword().equals(HashUtils.toComplexHashIterations(Sha1Hash.ALGORITHM_NAME, oldPassWord, sysUser.getSalt()))) {
                return ResultData.getFailResult("旧密码错误");
            }
            newPassWord = HashUtils.toComplexHashIterations(Sha1Hash.ALGORITHM_NAME, newPassWord, sysUser.getSalt());
            if (sysUser.getPassword().equals(newPassWord)) {
                return ResultData.getFailResult("新密码不能与旧密码相同");
            }
            sysUser.setPassword(newPassWord);
            this.update(sysUser);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }

    /**
     * 根据部门ID获取所有用户
     *
     * @author Gaodl
     * 方法名称: findByOrganizationIdAndDelFlagIsFalseOrderByCreateDateDesc
     * 参数： [organizationId]
     * 返回值： java.util.List<com.dico.modules.domain.SysUser>
     * 创建时间: 2019/5/21
     */
    public List<SysUser> findByOrganizationIdAndDelFlagIsFalseOrderByCreateDateDesc(String organizationId) {
        return sysUserRepository.findByOrganizationIdAndDelFlagIsFalseOrderByCreateDateDesc(organizationId);
    }
}

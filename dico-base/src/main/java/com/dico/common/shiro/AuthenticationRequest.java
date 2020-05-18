package com.dico.common.shiro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 用户登录模版类
 *
 * @author Gaodl
 * @version v1.0
 * 文件名称: AuthenticationRequest
 * 创建时间: 2018/12/12
 */
@Data
@ApiModel(value = "用户鉴权的组合对象")
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest implements Serializable {
    @ApiModelProperty(value = "用户名", required = true, example = "system")
    private String username;
    @ApiModelProperty(value = "密码", required = true, example = "111111")
    private String password;
}

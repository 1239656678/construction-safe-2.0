package com.dico.feign.feignServer;

import com.dico.feign.domain.Cost;
import com.dico.modules.po.CostApply;
import com.dico.modules.service.CostService;
import com.dico.result.ResultData;
import com.dico.util.TokenUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Gaodl
 * @version v1.0
 * 文件名称: $file_name$
 * 创建时间: $date$
 */
@Slf4j
@RestController
@RequestMapping(value = "activitiServer")
@Api(tags = "微服务调模块", produces = "微服务调模块Api")
public class ActivitiServer {

    @Autowired
    CostService costService;

    /**
     * 生成区域菜单
     *
     * @param cost
     * @return
     */
    @PostMapping("/startActiviti")
    @ApiOperation(value = "生成区域菜单", notes = "生成区域菜单")
    public ResultData startActiviti(HttpServletRequest request, @RequestBody Cost cost) {
        String userid = TokenUtils.getUserIdByRequest(request);
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("applyuserid", userid);
        CostApply costApply = new CostApply();
        costApply.setUser_id(userid);
        costApply.setTarget_id(cost.getRepairId());
        costApply.setCost_money(cost.getCostMoney()+"");
        costApply.setType(cost.getType());
        ProcessInstance ins = costService.startWorkflow(costApply, userid, variables);
        System.out.println("流程id" + ins.getId() + "已启动");
        return ResultData.getSuccessResult();
    }
}

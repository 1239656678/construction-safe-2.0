package com.dico.feign.feignClient;

import com.dico.feign.domain.*;
import com.dico.feign.hystrix.ActivitiHystrix;
import com.dico.feign.hystrix.DicoBaseHystrix;
import com.dico.result.ResultData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Gaodl
 * @version v1.0
 * 文件名称: $file_name$
 * 创建时间: $date$
 */
@FeignClient(value = "activiti", path = "activitiServer", fallbackFactory = ActivitiHystrix.class)
public interface ActivitiClient {
    @PostMapping(value = "/startActiviti")
    ResultData startActiviti(@RequestBody Cost cost);
}

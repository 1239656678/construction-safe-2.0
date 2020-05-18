package com.dico.feign.feignClient;

import com.dico.feign.domain.SysRole;
import com.dico.feign.hystrix.ConstructionSafeHystrix;
import com.dico.feign.hystrix.DicoBaseHystrix;
import com.dico.modules.domain.SmsEquipment;
import com.dico.result.ResultData;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author Administrator
 * @date 2020-04-29 11:13
 */
@FeignClient(value = "construction-safe", path = "ConstructionSafeServer", fallbackFactory = ConstructionSafeHystrix.class)
public interface ConstructionSafeClient {

     @GetMapping(value = "/smsEquipmentFindByClassId")
     List<SmsEquipment> findByClassId(@RequestParam(name = "classId") String classId);

     @GetMapping("/smsEquipmentDataInfo")
     SmsEquipment smsEquipmentDataInfo(@RequestParam(name = "smsEquipmentId") String smsEquipmentId);

}

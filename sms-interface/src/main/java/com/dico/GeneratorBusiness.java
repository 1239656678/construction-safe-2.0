package com.dico;

import com.dico.generator.GeneratorBusinessUtil;
import com.dico.modules.domain.SmsEquipment;
import com.dico.modules.domain.SmsRegions;
import com.dico.modules.domain.SmsUserInspectionPlan;

/**
 * 代码生成工具类
 *
 * @author Gaodl
 * @version v1.0
 * 文件名称: GeneratorBusiness
 * 创建时间: 2019/3/1
 */
public class GeneratorBusiness {
    /**
     * 代码生成方法，需要先生成实体类。
     *
     * @author Gaodl
     * 方法名称: main
     * 参数： [args]
     * 返回值： void
     * 创建时间: 2019/3/1
     */
    public static void main(String[] args) {
        GeneratorBusinessUtil.generator(new SmsUserInspectionPlan().getClass(), "construction-safe", "/src/main/java/com/dico/modules/");
    }
}

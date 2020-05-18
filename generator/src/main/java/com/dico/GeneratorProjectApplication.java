package com.dico;

import com.dico.util.GeneratorFactory;

public class GeneratorProjectApplication {
    /**
     * 修改项目名，执行生成项目工具
     *
     * @author Gaodl
     * 方法名称: main
     * 参数： [args]
     * 返回值： void
     * 创建时间: 2019/1/10
     */
    public static void main(String[] args) {
        //项目名
        String projectName = "dico-test";//项目名称
        GeneratorFactory.doMake(projectName);
    }
}


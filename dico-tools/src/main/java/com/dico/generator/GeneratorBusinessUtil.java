package com.dico.generator;

import java.io.File;

/**
 * @author Gaodl
 * @version v1.0
 * 文件名称: $file_name$
 * 创建时间: $date$
 */
public class GeneratorBusinessUtil {

    private static final String DOMAIN_NAME = "${domain}";
    private static final String UPPER_DOMAIN_NAME = "${upperDomain}";

    public static void generator(Class cls, String projectName, String URL) {
        final String REPO_PATH = System.getProperty("user.dir") + File.separator + projectName + File.separator + URL + "/repo/" + cls.getSimpleName() + "Repository.java";
        final String SERVICE_PATH = System.getProperty("user.dir") + File.separator + projectName + File.separator + URL + "/service/" + cls.getSimpleName() + "Service.java";
        final String CONTROLLER_PATH = System.getProperty("user.dir") + File.separator + projectName + File.separator + URL + "/controller/" + cls.getSimpleName() + "Controller.java";

        // 开始生成repository
        String repoContent = new ClassPathResourceReader("${upperDomain}Repository.java").getContent();
        repoContent = repoContent.replace(DOMAIN_NAME, FileUtil.lowerName(cls.getSimpleName()));
        repoContent = repoContent.replace(UPPER_DOMAIN_NAME, FileUtil.upperName(cls.getSimpleName()));
        FileUtil.createNewFile(REPO_PATH, repoContent);
        System.out.println("生成DAO层成功。");
        // 开始生成service
        String serviceContent = new ClassPathResourceReader("${upperDomain}Service.java").getContent();
        serviceContent = serviceContent.replace(DOMAIN_NAME, FileUtil.lowerName(cls.getSimpleName()));
        serviceContent = serviceContent.replace(UPPER_DOMAIN_NAME, FileUtil.upperName(cls.getSimpleName()));
        FileUtil.createNewFile(SERVICE_PATH, serviceContent);
        System.out.println("生成SERVICE层成功。");
        // 开始生成controller
        String controllerContent = new ClassPathResourceReader("${upperDomain}Controller.java").getContent();
        controllerContent = controllerContent.replace(DOMAIN_NAME, FileUtil.lowerName(cls.getSimpleName()));
        controllerContent = controllerContent.replace(UPPER_DOMAIN_NAME, FileUtil.upperName(cls.getSimpleName()));
        FileUtil.createNewFile(CONTROLLER_PATH, controllerContent);
        System.out.println("生成CONTROLLER层成功。");
    }
}

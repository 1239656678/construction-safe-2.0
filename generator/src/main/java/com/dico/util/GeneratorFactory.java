package com.dico.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * 代码生成工厂类
 *
 * @author Gaodl
 * @version v1.0
 * 文件名称: GeneratorFactory
 * 创建时间: 2019/1/10
 */
public class GeneratorFactory {
    private static final String TEMPLATE_PACKAGE = "template";
    private static final String PROJECT_DB_NAME = "${projectDBName}";
    private static final String PROJECT_NAME_KEY = "${projectName}";
    private static final String PROJECT_NAME_KEY_UPPERNAME = "${projectNameUpperName}";
    // 模板路径
    private static String templdate_url;
    // 项目生成路径
    private static String generator_url;

    static {
        PropertiesUtil p = new PropertiesUtil("generator.properties");
        templdate_url = p.readProperty("templdate-url");
        generator_url = p.readProperty("generator-url");
    }


    public GeneratorFactory() {
    }

    /**
     * 入口方法
     *
     * @author Gaodl
     * 方法名称: doMake
     * 参数： [projectName]
     * 返回值： void
     * 创建时间: 2019/1/10
     */
    public static void doMake(String projectName) {
        try {
            String projectTempldateUrl = System.getProperty("user.dir") + File.separator + templdate_url;
            readfile(projectTempldateUrl, generator_url, projectName);
            System.out.println("********** 缔科微服务业务框架生成工具  ：  微服务模块 {" + projectName + "} 生成成功 。 下载目录：" + generator_url + "\\" + projectName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 读取某个文件夹下的所有文件
     *
     * @author Gaodl
     * 方法名称: readfile
     * 参数： [filepath, generator_url, projectName]
     * 返回值： boolean
     * 创建时间: 2019/1/10
     */
    public static boolean readfile(String filepath, String generator_url, String projectName) throws FileNotFoundException, IOException {
        try {
            String mainProjectUrl = generator_url + File.separator + projectName;
            FileUtil.createDir(mainProjectUrl);

            File file = new File(filepath);
            if (!file.isDirectory()) {
                String path = file.getPath();
                String content = FileUtil.read(path);
                if (content != null) {
                    content = content.replace(PROJECT_NAME_KEY, projectName).trim();
                }
                path = path.substring(path.indexOf(TEMPLATE_PACKAGE), path.length()).replace(TEMPLATE_PACKAGE + File.separator, "").replace("." + PROJECT_NAME_KEY, "." + lowerName(projectName)).replace(PROJECT_NAME_KEY, lowerName(projectName));
                if (!TEMPLATE_PACKAGE.equals(path)) {
                    String mkdirFileUrl = generator_url + File.separator + projectName + File.separator + path;
                    //过滤掉svn文件
                    if (mkdirFileUrl.indexOf("svn") == -1) {
                        System.out.println("mkdirFileUrl=" + mkdirFileUrl);
                        FileUtil.createNewFile(mkdirFileUrl, content);
                    }
                }
            } else if (file.isDirectory()) {
                String[] filelist = file.list();
                String path = file.getPath();
                path = path.substring(path.indexOf(TEMPLATE_PACKAGE), path.length()).replace(TEMPLATE_PACKAGE + File.separator, "").replace("." + PROJECT_NAME_KEY, "." + lowerName(projectName)).replace(PROJECT_NAME_KEY, lowerName(projectName));
                if (!TEMPLATE_PACKAGE.equals(path)) {
                    String mkdirFileUrl = generator_url + File.separator + projectName + File.separator + path;
                    FileUtil.createDir(mkdirFileUrl);
                }
                for (int i = 0; i < filelist.length; i++) {
                    File readfile = new File(filepath + File.separator + filelist[i]);
                    if (!readfile.isDirectory()) {
                        path = readfile.getPath();
                        String content = FileUtil.read(path);
                        if (content != null) {
                            content = content.replace(PROJECT_NAME_KEY, projectName).trim();
                            content = content.replace(PROJECT_DB_NAME, GeneratorFactory.reversal(projectName).trim());
                            content = content.replace(PROJECT_NAME_KEY_UPPERNAME, GeneratorFactory.upperName(projectName).trim());
                        }
                        path = path.substring(path.indexOf(TEMPLATE_PACKAGE), path.length()).replace(TEMPLATE_PACKAGE + File.separator, "").replace("." + PROJECT_NAME_KEY, "." + lowerName(projectName)).replace(PROJECT_NAME_KEY, lowerName(projectName));
                        if (!TEMPLATE_PACKAGE.equals(path)) {
                            String mkdirFileUrl = generator_url + File.separator + projectName + File.separator + path;
                            FileUtil.createNewFile(mkdirFileUrl, content);
                        }
                    } else if (readfile.isDirectory()) {
                        readfile(filepath + File.separator + filelist[i], generator_url, projectName);
                    }
                }

            }

        } catch (FileNotFoundException e) {
            System.out.println("readfile()   Exception:" + e.getMessage());
        }
        return true;
    }

    /**
     * "-"转"_"
     *
     * @author Gaodl
     * 方法名称: reversal
     * 参数： [projectName]
     * 返回值： java.lang.String
     * 创建时间: 2019/1/10
     */
    private static String reversal(String projectName) {
        return projectName.replaceAll("-", "_");
    }

    /**
     * 首字母小写
     *
     * @author Gaodl
     * 方法名称: lowerName
     * 参数： [name]
     * 返回值： java.lang.String
     * 创建时间: 2019/1/10
     */
    public static String lowerName(String name) {
        if (name.indexOf("-") > 0) {
            String[] names = name.split("-");
            String firstName = names[0].substring(0, 1).toLowerCase() + names[0].substring(1);
            String lastName = "";
            for (int i = 1; i < names.length; i++) {
                lastName += upperName(names[i]);
            }
            name = firstName + lastName;
        } else {
            name = name.substring(0, 1).toLowerCase() + name.substring(1);
        }
        return name;
    }

    /**
     * 首字母大写
     *
     * @author Gaodl
     * 方法名称: upperName
     * 参数： [name]
     * 返回值： java.lang.String
     * 创建时间: 2019/1/10
     */
    public static String upperName(String name) {
        String newName = "";
        if (name.indexOf("-") > 0) {
            String[] names = name.split("-");
            for (String name1 : names) {
                newName += name1.substring(0, 1).toUpperCase() + name1.substring(1);
            }
            name = newName;
        } else {
            name = name.substring(0, 1).toUpperCase() + name.substring(1);
        }
        return name;
    }
}

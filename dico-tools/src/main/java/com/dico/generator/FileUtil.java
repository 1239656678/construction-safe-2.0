package com.dico.generator;

import java.io.*;

/**
 * 文件工具类
 *
 * @author Gaodl
 * @version v1.0
 * 文件名称: FileUtil
 * 创建时间: 2019/1/10
 */
public class FileUtil {

    /**
     * 创建文件
     *
     * @author Gaodl
     * 方法名称: createNewFile
     * 参数： [fileDirectoryAndName, fileContent]
     * 返回值： void
     * 创建时间: 2019/1/10
     */
    protected static void createNewFile(String fileDirectoryAndName, String fileContent) {
        try {
            String fileName = fileDirectoryAndName;
            //文件名首字母大写
            if (fileName.endsWith(".java")) {
                String s1 = fileName.substring(0, fileName.lastIndexOf("\\") + 1);
                String s2 = fileName.substring(fileName.lastIndexOf("\\") + 1);
                fileName = s1 + s2.substring(0, 1).toUpperCase() + s2.substring(1);
            }
            // 创建File对象，参数为String类型，表示目录名
            File myFile = new File(fileName);
            // 判断文件是否存在，如果不存在则调用createNewFile()方法创建新目录，否则跳至异常处理代码
            if (!myFile.exists())
                myFile.createNewFile();
            // 下面把数据写入创建的文件，首先新建文件名为参数创建FileWriter对象
            FileWriter resultFile = new FileWriter(myFile);
            // 把该对象包装进PrinterWriter对象
            PrintWriter myNewFile = new PrintWriter(resultFile);
            // 再通过PrinterWriter对象的println()方法把字符串数据写入新建文件
            myNewFile.println(fileContent);
            resultFile.close(); // 关闭文件写入流
        } catch (Exception ex) {
            System.out.println("无法创建新文件！");
            ex.printStackTrace();
        }
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

    /**
     * 创建目录
     *
     * @author Gaodl
     * 方法名称: createDir
     * 参数： [destDirName]
     * 返回值： boolean
     * 创建时间: 2019/1/10
     */
    protected static boolean createDir(String destDirName) {
        if (destDirName.indexOf("svn") > 0) {
            return false;
        }
        File dir = new File(destDirName);
        if (dir.exists()) {
            // System.out.println("创建目录" + destDirName + "失败，目标目录已经存在");
            return false;
        }
        if (!destDirName.endsWith(File.separator)) {
            destDirName = destDirName + File.separator;
        }
        // 创建目录
        if (dir.mkdirs()) {
            // System.out.println("创建目录" + destDirName + "成功！");
            return true;
        } else {
            // System.out.println("创建目录" + destDirName + "失败！");
            return false;
        }
    }

}

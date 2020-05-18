package com.dico.util;

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
    public static void createNewFile(String fileDirectoryAndName, String fileContent) {
        if (fileDirectoryAndName.indexOf("svn") > 0) {
            return;
        }
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
     * 首字母大写
     *
     * @author Gaodl
     * 方法名称: captureName
     * 参数： [name]
     * 返回值： java.lang.String
     * 创建时间: 2019/1/10
     */
    public static String captureName(String name) {
        char[] cs = name.toCharArray();
        cs[0] -= 32;
        return String.valueOf(cs);

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
    public static boolean createDir(String destDirName) {
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

    /**
     * 主要是输入流的使用，最常用的写法
     *
     * @author Gaodl
     * 方法名称: read
     * 参数： [filePath]
     * 返回值： java.lang.String
     * 创建时间: 2019/1/10
     */
    public static String read(String filePath) {
        // 读取txt内容为字符串
        StringBuffer txtContent = new StringBuffer();
        // 每次读取的byte数
        byte[] b = new byte[8 * 1024];
        InputStream in = null;
        try {
            // 文件输入流
            in = new FileInputStream(filePath);
            while (in.read(b) != -1) {
                // 字符串拼接
                txtContent.append(new String(b));
            }
            // 关闭流
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return txtContent.toString();
    }
}

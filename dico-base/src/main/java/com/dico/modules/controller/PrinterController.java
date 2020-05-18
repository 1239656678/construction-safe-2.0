package com.dico.modules.controller;

import com.dico.modules.domain.Attachments;
import com.dico.modules.service.AttachmentsServer;
import com.dico.result.ResultData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.Sides;
import javax.servlet.http.HttpServletRequest;
import java.awt.print.PrinterJob;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件上传业务类
 *
 * @author xipeifeng
 * @version v1.0
 * 文件名称: PrinterController
 * 创建时间: 2019/5/16
 */
@RestController
@RequestMapping("printer")
@Api(tags = "文件打印", description = "文件打印API")
public class PrinterController {
    /**
     * 图片上传的方法
     *
     * @description https://blog.csdn.net/vatxiongxiaohui/article/details/83985896
     * @author xipeifeng
     * 方法名称: image
     * 参数： [request, file]
     * 返回值： com.dico.result.ResultData
     * 创建时间: 2019/1/15
     */
    @Autowired
    private AttachmentsServer attachmentsServer;

    @PostMapping("/image")
    @ApiOperation(value = "图片上传", notes = "图片上传的方法")
    public ResultData image(HttpServletRequest request) {
        String fileName = request.getParameter("fileName");
        ResultData resultData = new ResultData(1, "打印成功", null);
        if (StringUtils.isNotBlank(fileName)) {
            File file = new File("E:\\cloud-services\\nginx\\static\\qr-code\\" + fileName);
            if (file.exists()) {
                String printerName = "HP MFP M436 PCL6";//打印机名包含字串
                try {
                    JPGPrint(file, printerName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                resultData.setCode(1).setMsg("文件不存在或者损坏").setData(null);
            }
        } else {
            resultData.setCode(1).setMsg("请输入文件名").setData(null);
        }
        return resultData;
    }

    // 传入文件和打印机名称
    public static void JPGPrint(File file, String printerName) throws PrintException {
        if (file == null) {
            System.err.println("缺少打印文件");
        }
        InputStream fis = null;
        try {
            // 设置打印格式，如果未确定类型，可选择autosense
            DocFlavor flavor = DocFlavor.INPUT_STREAM.JPEG;
            // 设置打印参数
            PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
            aset.add(new Copies(1)); //份数
            //aset.add(MediaSize.ISO.A4); //纸张
            // aset.add(Finishings.STAPLE);//装订
            aset.add(Sides.DUPLEX);//单双面
            // 定位打印服务
            PrintService printService = null;
            if (printerName != null) {
                //获得本台电脑连接的所有打印机
                PrintService[] printServices = PrinterJob.lookupPrintServices();
                if (printServices == null || printServices.length == 0) {
                    System.out.print("打印失败，未找到可用打印机，请检查。");
                    return;
                }
                //匹配指定打印机
                for (int i = 0; i < printServices.length; i++) {
                    System.out.println(printServices[i].getName());
                    if (printServices[i].getName().contains(printerName)) {
                        printService = printServices[i];
                        break;
                    }
                }
                if (printService == null) {
                    System.out.print("打印失败，未找到名称为" + printerName + "的打印机，请检查。");
                    return;
                }
            }
            fis = new FileInputStream(file); // 构造待打印的文件流
            Doc doc = new SimpleDoc(fis, flavor, null);
            DocPrintJob job = printService.createPrintJob(); // 创建打印作业
            job.print(doc, aset);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } finally {
            // 关闭打印的文件流
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 根据targetId查询所有附件
     *
     * @author Stephen
     * 方法名称: findAttachmentList
     * 参数： [targetId]
     * 返回值： com.dico.result.ResultData
     * 创建时间: 2019/10/28
     */
    @GetMapping("/findAttachments")
    @ApiOperation(value = "根据targetId查询所有附件", notes = "根据targetId查询所有附件")
    public ResultData findAttachments(@RequestParam(value = "targetId") String targetId) {
        List<Map<String, String>> dataList = null;
        List<Attachments> attachmentsList = attachmentsServer.findByTargetIdEqualsAndDelFlagIsFalse(targetId);
        if (null == attachmentsList) {
            return ResultData.getSuccessResult();
        } else {
            dataList = new ArrayList<>();
            for (Attachments attachment : attachmentsList) {
                Map<String, String> dataMap = new HashMap<>();
                dataMap.put("fileName", attachment.getFileName());
                dataMap.put("fileUrl", attachment.getFileUrl());
                dataList.add(dataMap);
            }
        }
        return ResultData.getSuccessResult(dataList);
    }

}

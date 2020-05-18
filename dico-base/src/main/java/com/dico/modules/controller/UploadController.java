package com.dico.modules.controller;

import com.dico.modules.domain.SysUser;
import com.dico.modules.domain.Attachments;
import com.dico.modules.service.AttachmentsServer;
import com.dico.modules.service.SysUserService;
import com.dico.result.ResultData;
import com.dico.util.TokenUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.*;

/**
 * 文件上传业务类
 *
 * @author Gaodl
 * @version v1.0
 * 文件名称: UploadController
 * 创建时间: 2019/1/15
 */
@Slf4j
@RestController
@RequestMapping("upload")
@Api(tags = "文件上传", description = "文件上传API")
public class UploadController {

    @Value("${file-upload.url.img}")
    private String UPLOAD_IMG_URL;

    @Value("${file-upload.url.headicon}")
    private String UPLOAD_HEADICON_URL;

    @Value("${file-upload.url.file}")
    private String UPLOAD_FILE_URL;

    // 指定各中文件上传方法中允许上传的文件后缀
    @Value("${file-upload.allowSuffix.img}")
    private String IMG_ALLOW_SUFFIX_STRING;
    @Value("${file-upload.allowSuffix.file}")
    private String FILE_ALLOW_SUFFIX_STRING;
    @Value("${file-upload.allowSuffix.headicon}")
    private String HEADICON_ALLOW_SUFFIX_STRING;

    private static List<String> IMG_ALLOW_SUFFIX = new ArrayList<>();
    private static List<String> FILE_ALLOW_SUFFIX = new ArrayList<>();
    private static List<String> HEADICON_ALLOW_SUFFIX = new ArrayList<>();

    @Autowired
    private AttachmentsServer attachmentsServer;
    @Resource
    private SysUserService sysUserService;


    /**
     * 上传多个图片
     *
     * @author Gaodl
     * 方法名称: uploadImages
     * 参数： [file, userId, targetId]
     * 返回值： com.dico.result.ResultData
     * 创建时间: 2019/11/6
     */
    @PostMapping("/uploadImages")
    @ApiOperation(value = "上传多个图片", notes = "上传多个图片")
    public ResultData uploadImages(HttpServletRequest request, @RequestParam(value = "files") MultipartFile[] files) {
        String currentUserId = TokenUtils.getUserIdByRequest(request);
        String[] IMG_ALLOW_SUFFIX_STRINGS = IMG_ALLOW_SUFFIX_STRING.split(",");
        IMG_ALLOW_SUFFIX = Arrays.asList(IMG_ALLOW_SUFFIX_STRINGS);
        ResultData resultData = new ResultData().setCode(1).setMsg("上传失败");// 默认上传失败
        List<String> attachmentIdList = new ArrayList<>();
        try {
            for (MultipartFile file : files) {
                // 原始文件名称
                String fileName = file.getOriginalFilename();
                String fileSuffix = "";
                if (fileName.indexOf(".") > -1) {
                    fileSuffix = fileName.substring(fileName.lastIndexOf("."));
                }
                // uuid生成新的文件名称
                String fileCode = UUID.randomUUID().toString().replaceAll("-", "") + fileSuffix;
                String fileHref = UPLOAD_IMG_URL.split("static")[1] + fileCode;
                if (IMG_ALLOW_SUFFIX.contains(fileSuffix.toLowerCase().substring(1))) {
                    File filePath = new File(UPLOAD_IMG_URL);
                    //如果文件夹不存在则创建
                    if (!filePath.exists() && !filePath.isDirectory()) {
                        filePath.mkdirs();
                    }
                    try {
                        String attachmentId = this.save(currentUserId, fileCode, fileName, fileSuffix, fileHref, null);
                        File newFile = new File(UPLOAD_IMG_URL + fileCode);
                        //通过CommonsMultipartFile的方法直接写文件（注意这个时候）
                        file.transferTo(newFile);
                        attachmentIdList.add(attachmentId);
                    } catch (Exception e) {
                        log.error(e.getMessage());
                        throw new RuntimeException(e.getMessage());
                    }
                } else {
                    resultData.setMsg("暂不支持此格式的文件上传");
                }
            }
            resultData.setCode(0).setMsg("上传成功").setData(attachmentIdList);
        } catch (RuntimeException e) {
            return resultData.setMsg(e.getMessage());
        }
        return resultData;
    }

    private String save(String currentUserId, String fileCode, String fileName, String fileSuffix, String fileHref, String targetId) throws RuntimeException {
        try {
            SysUser sysUser = sysUserService.findOne(currentUserId);
            Attachments attachments = new Attachments();
            attachments.setFileCode(fileCode);
            attachments.setFileName(fileName);
            attachments.setFileType(fileSuffix);
            attachments.setTargetId(targetId);
            attachments.setFileUrl(fileHref);
            attachments.setCreateDate(new Date());
            attachments.setCreateUser(sysUser.getId());
            attachments.setCreateUserName(sysUser.getName());
            attachments.setDelFlag(false);
            attachmentsServer.save(attachments);
            return attachments.getId();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 图片上传的方法
     *
     * @author Gaodl
     * 方法名称: image
     * 参数： [request, file]
     * 返回值： com.dico.result.ResultData
     * 创建时间: 2019/1/15
     */
    @PostMapping("/image")
    @ApiOperation(value = "图片上传", notes = "图片上传的方法")
    public ResultData image(HttpServletRequest request, @RequestParam("file") MultipartFile file) {
        String[] IMG_ALLOW_SUFFIX_STRINGS = IMG_ALLOW_SUFFIX_STRING.split(",");
        IMG_ALLOW_SUFFIX = Arrays.asList(IMG_ALLOW_SUFFIX_STRINGS);
        String currentUserId = TokenUtils.getUserIdByRequest(request);
        ResultData resultData = new ResultData().setCode(1).setMsg("上传失败");// 默认上传失败
        // 原始文件名称
        String fileName = file.getOriginalFilename();
        String fileSuffix = "";
        if (fileName.indexOf(".") > -1) {
            fileSuffix = fileName.substring(fileName.lastIndexOf("."));
        }
        // uuid生成新的文件名称
        String fileCode = UUID.randomUUID().toString().replaceAll("-", "") + fileSuffix;
        String fileHref = UPLOAD_IMG_URL.split("static")[1] + fileCode;
        if (IMG_ALLOW_SUFFIX.contains(fileSuffix.toLowerCase().substring(1))) {
            File filePath = new File(UPLOAD_IMG_URL);
            //如果文件夹不存在则创建
            if (!filePath.exists() && !filePath.isDirectory()) {
                filePath.mkdirs();
            }
            try {
                String attachmentId = this.save(currentUserId, fileCode, fileName, fileSuffix, fileHref);
                File newFile = new File(UPLOAD_IMG_URL + fileCode);
                //通过CommonsMultipartFile的方法直接写文件（注意这个时候）
                file.transferTo(newFile);
                Map<String, String> dataMap = new HashMap<>(2);
                dataMap.put("attachmentId", attachmentId);
                dataMap.put("attachmentHref", fileHref);
                resultData.setCode(0).setMsg("上传成功").setData(dataMap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            resultData.setMsg("暂不支持此格式的文件上传");
        }
        return resultData;
    }

    /**
     * 文件上传
     *
     * @author Gaodl
     * 方法名称: file
     * 参数： [request, file]
     * 返回值： com.dico.result.ResultData
     * 创建时间: 2019/1/15
     */
    @PostMapping("/file")
    @ApiOperation(value = "文件上传", notes = "文件上传的方法")
    public ResultData file(HttpServletRequest request, @RequestParam("file") MultipartFile file) {
        String[] FILE_ALLOW_SUFFIX_STRINGS = FILE_ALLOW_SUFFIX_STRING.split(",");
        FILE_ALLOW_SUFFIX = Arrays.asList(FILE_ALLOW_SUFFIX_STRINGS);
        String currentUserId = TokenUtils.getUserIdByRequest(request);
        ResultData resultData = new ResultData().setCode(1).setMsg("上传失败");// 默认上传失败
        // 原始文件名称
        String fileName = file.getOriginalFilename();
        String fileSuffix = "";
        if (fileName.indexOf(".") > -1) {
            fileSuffix = fileName.substring(fileName.lastIndexOf("."));
        }
        // uuid生成新的文件名称
        String fileCode = UUID.randomUUID().toString().replaceAll("-", "") + fileSuffix;
        String fileHref = UPLOAD_FILE_URL.split("static")[1] + fileCode;
        if (FILE_ALLOW_SUFFIX.contains(fileSuffix.toLowerCase().substring(1))) {
            File filePath = new File(UPLOAD_FILE_URL);
            //如果文件夹不存在则创建
            if (!filePath.exists() && !filePath.isDirectory()) {
                filePath.mkdirs();
            }
            try {
                String attachmentId = this.save(currentUserId, fileCode, fileName, fileSuffix, fileHref);
                File newFile = new File(UPLOAD_FILE_URL + fileCode);
                //通过CommonsMultipartFile的方法直接写文件（注意这个时候）
                file.transferTo(newFile);
                Map<String, String> dataMap = new HashMap<>(2);
                dataMap.put("attachmentId", attachmentId);
                dataMap.put("attachmentHref", fileHref);
                resultData.setCode(0).setMsg("上传成功").setData(dataMap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            resultData.setMsg("暂不支持此格式的文件上传");
        }
        return resultData;
    }

    /**
     * 图像上传
     *
     * @author Gaodl
     * 方法名称: headicon
     * 参数： [request, file]
     * 返回值： com.dico.result.ResultData
     * 创建时间: 2019/1/15
     */
    @PostMapping("/headicon")
    @ApiOperation(value = "图像上传", notes = "图像上传的方法")
    public ResultData headicon(HttpServletRequest request, @RequestParam("file") MultipartFile file) {
        String[] HEADICON_ALLOW_SUFFIX_STRINGS = HEADICON_ALLOW_SUFFIX_STRING.split(",");
        HEADICON_ALLOW_SUFFIX = Arrays.asList(HEADICON_ALLOW_SUFFIX_STRINGS);
        String currentUserId = TokenUtils.getUserIdByRequest(request);
        ResultData resultData = new ResultData().setCode(1).setMsg("上传失败");// 默认上传失败
        // 原始文件名称
        String fileName = file.getOriginalFilename();
        String fileSuffix = "";
        if (fileName.indexOf(".") > -1) {
            fileSuffix = fileName.substring(fileName.lastIndexOf("."));
        }
        // uuid生成新的文件名称
        String fileCode = UUID.randomUUID().toString().replaceAll("-", "") + fileSuffix;
        String fileHref = UPLOAD_HEADICON_URL.split("static")[1] + fileCode;
        if (HEADICON_ALLOW_SUFFIX.contains(fileSuffix.toLowerCase().substring(1))) {
            File filePath = new File(UPLOAD_HEADICON_URL);
            //如果文件夹不存在则创建
            if (!filePath.exists() && !filePath.isDirectory()) {
                filePath.mkdirs();
            }
            try {
                String attachmentId = this.save(currentUserId, fileCode, fileName, fileSuffix, fileHref);
                File newFile = new File(UPLOAD_HEADICON_URL + fileCode);
                //通过CommonsMultipartFile的方法直接写文件（注意这个时候）
                file.transferTo(newFile);
                Map<String, String> dataMap = new HashMap<>(2);
                dataMap.put("attachmentId", attachmentId);
                dataMap.put("attachmentHref", fileHref);
                resultData.setCode(0).setMsg("上传成功").setData(dataMap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            resultData.setMsg("暂不支持此格式的文件上传");
        }
        return resultData;
    }

    /**
     * 保存附件的方法
     *
     * @author Gaodl
     * 方法名称: save
     * 参数： [currentUserId, fileCode, fileName, fileSuffix, fileHref]
     * 返回值： java.lang.String
     * 创建时间: 2019/1/15
     */
    public String save(String currentUserId, String fileCode, String fileName, String fileSuffix, String fileHref) throws Exception {
        SysUser sysUser = sysUserService.findOne(currentUserId);
        Attachments attachments = new Attachments();
        attachments.setFileCode(fileCode);
        attachments.setFileName(fileName);
        attachments.setFileType(fileSuffix);
        attachments.setFileUrl(fileHref);
        attachments.setCreateDate(new Date());
        attachments.setCreateUser(sysUser.getId());
        attachments.setCreateUserName(sysUser.getName());
        attachments.setDelFlag(false);
        attachmentsServer.save(attachments);
        return attachments.getId();
    }

}

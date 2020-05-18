package com.dico.feign.feignServer;

import com.dico.feign.domain.NoticeFeignDomain;
import com.dico.feign.domain.NoticeFeignPerson;
import com.dico.feign.domain.OrganizationFeignDomain;
import com.dico.modules.domain.*;
import com.dico.modules.service.*;
import com.dico.qrcode.QRCodeAttribute;
import com.dico.result.ImageResult;
import com.dico.result.ResultData;
import com.dico.result.ValidatedResult;
import com.dico.util.QRCodeUtils;
import com.dico.util.TokenUtils;
import com.dico.util.TransmitUtils;
import com.dico.util.ValiDatedUtils;
import com.google.zxing.WriterException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Gaodl
 * @version v1.0
 * 文件名称: $file_name$
 * 创建时间: $date$
 */
@Slf4j
@RestController
@RequestMapping(value = "dicoBaseServer")
@Api(tags = "微服务调模块", produces = "微服务调模块Api")
public class DicoBaseServer {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysMenuService sysMenuService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private NoticeMessageService noticeMessageService;

    @Autowired
    private NoticePersonService noticePersonService;

    @Autowired
    private AttachmentsServer attachmentsServer;

    @Autowired
    private DataDictionaryTypeService dataDictionaryTypeService;

    @Autowired
    private DataDictionaryService dataDictionaryService;

    @Autowired
    private SysOrganizationUserService sysOrganizationUserService;

    @Value("${file-upload.url.img}")
    private String UPLOAD_IMG_URL;

    @Value("${file-upload.url.headicon}")
    private String UPLOAD_HEADICON_URL;

    @Value("${file-upload.url.file}")
    private String UPLOAD_FILE_URL;

    // 维保单位查询key
    @Value("${organization.wbdw-query-key}")
    private String WBDW_QUERY_KEY;

    // 维修小组查询key
    @Value("${organization.wxxz-query-key}")
    private String WXXZ_QUERY_KEY;

    // 指定各中文件上传方法中允许上传的文件后缀
    @Value("${file-upload.allowSuffix.img}")
    private String IMG_ALLOW_SUFFIX_STRING;
    @Value("${file-upload.allowSuffix.file}")
    private String FILE_ALLOW_SUFFIX_STRING;
    @Value("${file-upload.allowSuffix.headicon}")
    private String HEADICON_ALLOW_SUFFIX_STRING;

    @Value("${file-upload.url.qr-code}")
    private String QR_CODE_URL;

    private static List<String> IMG_ALLOW_SUFFIX = new ArrayList<>();
    private static List<String> FILE_ALLOW_SUFFIX = new ArrayList<>();
    private static List<String> HEADICON_ALLOW_SUFFIX = new ArrayList<>();

    /**
     * 根据用户ID查询用户
     *
     * @param userId
     * @return
     */
    @GetMapping("/findUserById")
    @ApiOperation(value = "根据用户ID查询用户", notes = "根据用户ID查询用户")
    public SysUser findUserById(String userId) {
        return sysUserService.getUserByIdAndDelFlagIsFalse(userId);
    }


    /**
     * 获取用户绑定的角色信息
     *
     * @param userId
     * @return
     */
    @GetMapping("/getBindRoles")
    @ApiOperation(value = "获取用户绑定的角色信息", notes = "获取用户绑定的角色信息")
    public List<SysRole> getBindRoles(String userId) {
        return sysRoleService.findBindRoles(userId);
    }

    /**
     * 根据ID判断用户是否为系统管理员
     *
     * @param userId
     * @return
     */
    @GetMapping("/isSuper")
    @ApiOperation(value = "根据ID判断用户是否为系统管理员", notes = "根据ID判断用户是否为系统管理员")
    public boolean isSuper(String userId) {
        return sysRoleService.isSuper(userId);
    }

    /**
     * 修改用户密码
     *
     * @param userId
     * @param oldPassWord
     * @param newPassWord
     * @return
     */
    @PostMapping("/updatePass")
    @ApiOperation(value = "修改用户密码", notes = "修改用户密码")
    public ResultData updatePass(String userId, String oldPassWord, String newPassWord) {
        return sysUserService.updatePass(userId, oldPassWord, newPassWord);
    }

    /**
     * 根据组织ID获取所有用户
     *
     * @param organizationId
     * @return
     */
    @GetMapping("/findUserByOrganizationId")
    @ApiOperation(value = "根据组织ID获取所有用户", notes = "根据组织ID获取所有用户")
    public List<Map<String, Object>> findUserByOrganizationId(String organizationId) {
        List<Map<String, Object>> dataList = null;
        try {
            if (StringUtils.isBlank(organizationId)) {
                return null;
            }
            dataList = new ArrayList<>();
            List<SysUser> sysUserList = sysUserService.findByOrganizationIdAndDelFlagIsFalseOrderByCreateDateDesc(organizationId);
            for (SysUser sysUser : sysUserList) {
                Map<String, Object> dataMap = new HashMap<>();
                dataMap.put("id", sysUser.getId());
                dataMap.put("name", sysUser.getName());
                dataList.add(dataMap);
            }
        } catch (Exception e) {
            return null;
        }
        return dataList;
    }

    /**
     * 根据ID查询组织信息
     *
     * @param organizationId
     * @return
     */
    @ResponseBody
    @GetMapping("findOrganizationById")
    @ApiOperation(value = "根据ID查询组织信息", notes = "根据ID查询组织信息")
    public ResultData findOrganizationById(String organizationId) {
        Organization organization = organizationService.getOrganizationByIdAndDelFlagIsFalse(organizationId);
        if (null == organization) {
            return ResultData.getFailResult("该组织不存在");
        }
        OrganizationFeignDomain organizationFeignDomain = new OrganizationFeignDomain();
        organizationFeignDomain.setId(organization.getId());
        organizationFeignDomain.setName(organization.getName());
        organizationFeignDomain.setDetails(organization.getDetails());
        return new ResultData().setCode(0).setMsg("成功").setData(organizationFeignDomain);
    }

    /**
     * 保存消息通知
     *
     * @param request
     * @param noticeFeignDomain
     * @return
     */
    @Transactional
    @PostMapping("/saveMessageNotice")
    @ApiOperation(value = "保存消息通知", notes = "保存消息通知")
    public ResultData saveMessageNotice(HttpServletRequest request, @RequestBody NoticeFeignDomain noticeFeignDomain) {
        // 获取当前登陆人
        String currentUserId = TokenUtils.getUserIdByRequest(request);
        SysUser currentSysUser = sysUserService.findOne(currentUserId);
        // 校验数据
        if (null == currentSysUser) {
            try {
                throw new RuntimeException("获取系统登录人出错");
            } catch (RuntimeException e) {
                e.printStackTrace();
                return new ResultData().setCode(1).setMsg("获取系统登录人出错");
            }
        }
        if (null == noticeFeignDomain) {
            try {
                throw new RuntimeException("消息对象为空");
            } catch (RuntimeException e) {
                e.printStackTrace();
                return new ResultData().setCode(1).setMsg("消息对象为空");
            }
        }
        try {
            ValidatedResult validatedResult = ValiDatedUtils.valiDatedBean(noticeFeignDomain);
            if (!validatedResult.isStatus()) {
                return ResultData.getFailResult(validatedResult.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultData.getFailResult(e.getMessage());
        }
        if (noticeFeignDomain.getNoticeFeignPersonList().size() == 0) {
            try {
                throw new RuntimeException("没有消息通知人");
            } catch (RuntimeException e) {
                e.printStackTrace();
                return new ResultData().setCode(1).setMsg("没有消息通知人");
            }
        }
        // 创建并保存消息对象
        NoticeMessage noticeMessage = new NoticeMessage();
        noticeMessage.setTitle(noticeFeignDomain.getNoticeTitle());
        noticeMessage.setContent(noticeFeignDomain.getNoticeContent());
        noticeMessage.setCreateUser(currentUserId);
        noticeMessage.setCreateUserName(currentSysUser.getName());
        noticeMessage.setCreateDate(new Date());
        noticeMessage.setDelFlag(false);
        noticeMessageService.save(noticeMessage);
        // 创建并保存消息通知人员对象
        List<NoticePerson> noticePersonList = new ArrayList<>();
        for (NoticeFeignPerson noticeFeignPerson : noticeFeignDomain.getNoticeFeignPersonList()) {
            NoticePerson noticePerson = new NoticePerson();
            noticePerson.setMessageId(noticeMessage.getId());
            noticePerson.setUserId(noticeFeignPerson.getId());
            noticePerson.setUserName(noticeFeignPerson.getName());
            noticePerson.setCreateUser(currentUserId);
            noticePerson.setCreateUserName(currentSysUser.getName());
            noticePerson.setCreateDate(new Date());
            noticePerson.setDelFlag(false);
            noticePersonList.add(noticePerson);
        }
        noticePersonService.save(noticePersonList);
        return new ResultData().setCode(0).setMsg("保存成功");
    }

    /**
     * 根据用户ID获取最新消息
     *
     * @param userId
     * @return
     */
    @GetMapping("/findNewMessageByUserId")
    @ApiOperation(value = "根据用户ID获取最新消息", notes = "根据用户ID获取最新消息")
    public NoticeMessage findNewMessageByUserId(String userId) {
        List<NoticePerson> noticePersonList = noticePersonService.findByUserIdAndDelFlagIsFalseOrderByCreateDateDesc(userId);
        if (null == noticePersonList) {
            return null;
        }
        for (NoticePerson noticePerson : noticePersonList) {
            NoticeMessage noticeMessage = noticeMessageService.getByIdEqualsAndDelFlagIsFalse(noticePerson.getMessageId());
            if (null != noticeMessage) {
                return noticeMessage;
            }
        }
        return null;
    }

    /**
     * 根据用户ID获取所有消息
     *
     * @param userId
     * @return
     */
    @GetMapping("/findMessageListByUserId")
    @ApiOperation(value = "根据用户ID获取所有消息", notes = "根据用户ID获取所有消息")
    public ResultData findMessageListByUserId(String userId) {
        List<Map<String, Object>> dataList = null;
        List<Map<String, Object>> resultList = new ArrayList<>();
        try {
            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            dataList = noticePersonService.findAllByUserIdAndDelFlagIsFalseOrderByCreateDateDesc(userId);
            dataList.forEach(map -> {
                Map<String, Object> dataMap = new HashMap<>();
                Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();
                String key = "";
                String value = "";
                while (iterator.hasNext()) {
                    Map.Entry entry = iterator.next();
                    if ("messageDate".equals(entry.getKey())) {
                        Timestamp messageDate = (Timestamp) entry.getValue();
                        entry.setValue(sdf.format(messageDate));
                    }
                    dataMap.put(entry.getKey().toString(), entry.getValue());
                }
                resultList.add(dataMap);
            });
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult(resultList);
    }

    /**
     * 删除用户接收的消息
     *
     * @param userId
     * @param messageId
     * @return
     */
    @DeleteMapping("/deleteSelfMessage")
    @ApiOperation(value = "删除用户接收的消息", notes = "删除用户接收的消息")
    public ResultData deleteSelfMessage(String userId, String messageId) {
        try {
            if (StringUtils.isBlank(userId) || StringUtils.isBlank(messageId)) {
                return ResultData.getFailResult("获取不到用户或消息");
            }
            noticePersonService.deleteByUserIdAndMessageId(userId, messageId);
            return ResultData.getSuccessResult();
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
    }

    /**
     * 生成带文字说明的二维码
     *
     * @param content
     * @param viewMsg
     * @param width
     * @param height
     * @return
     */
    @PostMapping("/generatorTextQrCode")
    @ApiOperation(value = "生成带文字说明的二维码", notes = "生成带文字说明的二维码")
    public ImageResult generatorTextQrCode(@RequestParam(defaultValue = "", value = "content") String content, @RequestParam(defaultValue = "", value = "viewMsg") String viewMsg, @RequestParam(defaultValue = "800", value = "width", required = false) int width, @RequestParam(defaultValue = "800", value = "height", required = false) int height) {
        try {
            QRCodeAttribute qrCodeAttribute = new QRCodeAttribute();
            qrCodeAttribute.setImagePath(QR_CODE_URL);
            qrCodeAttribute.setContent(content);
            qrCodeAttribute.setImageViewText(viewMsg);
            qrCodeAttribute.setImageHeight(height);
            qrCodeAttribute.setImageWidth(width);
            QRCodeUtils qrCodeUtils = new QRCodeUtils(qrCodeAttribute);
            return qrCodeUtils.ganertorTextImage();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 生成普通二维码
     *
     * @param content
     * @param width
     * @param height
     * @return
     * @throws WriterException
     * @throws IOException
     */
    @PostMapping("/generatorQrCode")
    @ApiOperation(value = "生成普通二维码", notes = "生成普通二维码")
    public ImageResult generatorQrCode(@RequestParam(defaultValue = "", value = "content") String content, @RequestParam(defaultValue = "800", value = "width", required = false) int width, @RequestParam(defaultValue = "800", value = "height", required = false) int height) throws WriterException, IOException {
        try {
            QRCodeAttribute qrCodeAttribute = new QRCodeAttribute();
            qrCodeAttribute.setImagePath(QR_CODE_URL);
            qrCodeAttribute.setContent(content);
            qrCodeAttribute.setImageHeight(height);
            qrCodeAttribute.setImageWidth(width);
            QRCodeUtils qrCodeUtils = new QRCodeUtils(qrCodeAttribute);
            return qrCodeUtils.generatorImage();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 根据ID查询附件
     *
     * @param id
     * @return
     */
    @GetMapping("/getAttachment")
    @ApiOperation(value = "根据ID查询附件", notes = "根据ID查询附件")
    public Attachments getAttachment(@RequestParam(value = "id") String id) {
        return attachmentsServer.getByIdEqualsAndDelFlagIsFalse(id);
    }

    /**
     * 根据ID集合查询附件
     *
     * @param ids
     * @return
     */
    @GetMapping("/findAttachmentListByIds")
    @ApiOperation(value = "根据ID集合查询附件", notes = "根据ID集合查询附件")
    public List<Attachments> findAttachmentListByIds(@RequestParam(value = "ids") String[] ids) {
        return attachmentsServer.findByIdIn(ids);
    }

    /**
     * 根据外键查询所有附件
     *
     * @param targetId
     * @return
     */
    @GetMapping("/findAttachmentListByTargetId")
    @ApiOperation(value = "根据外键查询所有附件", notes = "根据外键查询所有附件")
    public ResultData findAttachmentListByTargetId(@RequestParam(value = "targetId") String targetId) {
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

    /**
     * 修改附件信息
     *
     * @param attachments
     * @return
     */
    @PutMapping("/updateAttchment")
    @ApiOperation(value = "修改附件信息", notes = "修改附件信息")
    public ResultData updateAttchment(@RequestBody Attachments attachments) {
        try {
            ValidatedResult validatedResult = ValiDatedUtils.valiDatedBean(attachments);
            if (!validatedResult.isStatus()) {
                return ResultData.getFailResult(validatedResult.getMessage());
            }
            Attachments _attachments = attachmentsServer.getByIdEqualsAndDelFlagIsFalse(attachments.getId());
            if (null == _attachments) {
                return ResultData.getFailResult("附件不存在");
            } else {
                TransmitUtils.sources2destination(attachments, _attachments);
                attachmentsServer.update(_attachments);
            }
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }

    /**
     * 上传图片
     *
     * @param file
     * @param userId
     * @param targetId
     * @return
     */
    @PostMapping("/uploadImage")
    @ApiOperation(value = "上传图片", notes = "上传图片")
    public ResultData uploadImage(@RequestParam(value = "file") MultipartFile file, @RequestParam(value = "userId") String userId, @RequestParam(value = "targetId") String targetId) {
        String[] IMG_ALLOW_SUFFIX_STRINGS = IMG_ALLOW_SUFFIX_STRING.split(",");
        IMG_ALLOW_SUFFIX = Arrays.asList(IMG_ALLOW_SUFFIX_STRINGS);
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
                String attachmentId = this.save(userId, fileCode, fileName, fileSuffix, fileHref, targetId);
                File newFile = new File(UPLOAD_IMG_URL + fileCode);
                //通过CommonsMultipartFile的方法直接写文件（注意这个时候）
                file.transferTo(newFile);
                Map<String, String> dataMap = new HashMap<>(2);
                dataMap.put("attachmentId", attachmentId);
                dataMap.put("attachmentHref", fileHref);
                resultData.setCode(0).setMsg("上传成功").setData(dataMap);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        } else {
            resultData.setMsg("暂不支持此格式的文件上传");
        }
        return resultData;
    }

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
    public ResultData uploadImages(@RequestParam(value = "files") MultipartFile[] files, @RequestParam(value = "userId") String userId) {
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
                        String attachmentId = this.save(userId, fileCode, fileName, fileSuffix, fileHref, null);
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
     * 字典类型模糊查询
     *
     * @param name
     * @return
     */
    @GetMapping("/findDataDictionaryTypeListByName")
    @ApiOperation(value = "字典类型模糊查询", notes = "字典类型模糊查询")
    public List<DataDictionaryType> findDataDictionaryTypeListByName(String name) {
        Map<String, Object> queryMap = new HashMap<>(2);
        queryMap.put("delFlag_EQ", false);
        if (StringUtils.isNotBlank(name)) {
            queryMap.put("name_LIKE", name);
        }
        Sort sort = new Sort(Sort.Direction.DESC, "createDate");
        return dataDictionaryTypeService.findAll(queryMap, sort);
    }
        /**
         * 根据id获取菜单
         *
         * @param id
         * @return
         */
        @GetMapping("/getSysMenuById")
        @ApiOperation(value = "根据id获取菜单", notes = "根据id获取菜单")
        public SysMenu getSysMenuById(@RequestParam(value = "id") String id) {
            return sysMenuService.findOne(id);
        }

    /**
     * 根据类型ID查询数据字典
     *
     * @param typeId
     * @return
     */
    @GetMapping("/findDataByType")
    @ApiOperation(value = "根据类型ID查询数据字典", notes = "根据类型ID查询数据字典")
    public ResultData findDataByType(String typeId) {
        List<Map<String, Object>> dataList = null;
        try {
            Map<String, Object> queryMap = new HashMap<>(2);
            queryMap.put("delFlag_EQ", false);
            if (StringUtils.isNotBlank(typeId)) {
                queryMap.put("typeId_EQ", typeId);
            }
            Sort sort = new Sort(Sort.Direction.DESC, "createDate");
            dataList = new ArrayList<>();
            List<DataDictionary> dataDictionaryList = dataDictionaryService.findAll(queryMap, sort);
            for (DataDictionary dataDictionary : dataDictionaryList) {
                Map<String, Object> dataMap = new HashMap<>();
                dataMap.put("id", dataDictionary.getId());
                dataMap.put("name", dataDictionary.getName());
                dataList.add(dataMap);
            }
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult(dataList);
    }

    /**
     * 生成区域菜单
     *
     * @param sysMenu
     * @return
     */
    @PostMapping("/generatorAreaMenus")
    @ApiOperation(value = "生成区域菜单", notes = "生成区域菜单")
    public ResultData generatorAreaMenus(@RequestBody SysMenu sysMenu) {
        sysMenuService.save(sysMenu);
        return ResultData.getSuccessResult();
    }

    /**
     * 根据区域ID获取菜单
     *
     * @param areaId
     * @return
     */
    @GetMapping("/getSysMenuByAreaId")
    @ApiOperation(value = "根据区域ID获取菜单", notes = "根据区域ID获取菜单")
    public SysMenu getSysMenuByAreaId(@RequestParam(value = "areaId") String areaId) {
        System.out.println("dico-base-areaId"+areaId);
        return sysMenuService.getByAreaId(areaId);
    }


    /**
     * 保存菜单
     */
    @PostMapping("/saveMenu")
    @ApiOperation(value = "保存菜单对象",notes ="保存菜单对象" )
    public void saveMenuEntity(@RequestBody SysMenu sysMenu){
        System.out.println("dico-base"+sysMenu);
        sysMenuService.save(sysMenu);
    };


    /**
     * 获取区域管理根节点菜单
     *
     * @return
     */
    @GetMapping("/getSysMenuIsAreaRoot")
    @ApiOperation(value = "获取区域管理根节点菜单", notes = "获取区域管理根节点菜单")
    public SysMenu getSysMenuIsAreaRoot() {

        System.out.println("=================="+sysMenuService.getByAreaId("true"));
        return sysMenuService.getByAreaId("true");
    }

    /**
     * 获取当前用户所有的菜单
     *
     * @return
     */
    @GetMapping("/findSysMenuByCurrentUser")
    @ApiOperation(value = "获取当前用户所有的菜单", notes = "获取当前用户所有的菜单")
    public ResultData findSysMenuByCurrentUser(HttpServletRequest request) {
        String userId = TokenUtils.getUserIdByRequest(request);
        if (StringUtils.isBlank(userId)) {
            return ResultData.getFailResult("未登录");
        }
        SysUser sysUser = sysUserService.getUserByIdAndDelFlagIsFalse(userId);
        if (null == sysUser) {
            return ResultData.getFailResult("用户不存在");
        }
        List<CurrentSysMenu> currentSysMenuList = sysMenuService.findAll(sysUser);
        return ResultData.getSuccessResult(currentSysMenuList);
    }

    /**
     * 当前用户是否为部门管理员
     *
     * @return
     */
    @GetMapping("/isOrganizationUser")
    @ApiOperation(value = "当前用户是否为部门管理员", notes = "当前用户是否为部门管理员")
    public Boolean isOrganizationUser(HttpServletRequest request) {
        String userId = TokenUtils.getUserIdByRequest(request);
        if (StringUtils.isBlank(userId)) {
            return false;
        }
        SysOrganizationUser sysOrganizationUser = sysOrganizationUserService.findByUserId(userId);
        return null == sysOrganizationUser ? false : true;
    }

    /**
     * 判断当前用户是否是维修小组用户
     *
     * @return
     */
    @GetMapping("/isWxxz")
    @ApiOperation(value = "判断当前用户是否是维修小组用户", notes = "判断当前用户是否是维修小组用户")
    public Boolean isWxxz(HttpServletRequest request) {
        String userId = TokenUtils.getUserIdByRequest(request);
        if (StringUtils.isBlank(userId)) {
            return false;
        }
        SysUser sysUser = sysUserService.getUserByIdAndDelFlagIsFalse(userId);
        if(null == sysUser || !sysUser.getOrganizationId().equals(WXXZ_QUERY_KEY)){
            return false;
        }
        return true;
    }

    /**
     * 判断当前用户是否是维保单位用户
     *
     * @return
     */
    @GetMapping("/isWbdw")
    @ApiOperation(value = "判断当前用户是否是维保单位用户", notes = "判断当前用户是否是维保单位用户")
    public Boolean isWbdw(HttpServletRequest request) {
        String userId = TokenUtils.getUserIdByRequest(request);
        if (StringUtils.isBlank(userId)) {
            return false;
        }
        SysUser sysUser = sysUserService.getUserByIdAndDelFlagIsFalse(userId);
        if(null == sysUser){
            return false;
        }else{
            Organization organization = organizationService.getOrganizationByIdAndDelFlagIsFalse(sysUser.getOrganizationId());
            if(null == organization || !organization.getParentOrganizationId().equals(WBDW_QUERY_KEY)){
                return false;
            }
        }
        return true;
    }



}

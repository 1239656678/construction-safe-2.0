package com.dico.modules.controller;

import com.dico.modules.domain.NoticeMessage;
import com.dico.modules.domain.NoticePerson;
import com.dico.modules.domain.SysUser;
import com.dico.modules.service.NoticeMessageService;
import com.dico.modules.service.NoticePersonService;
import com.dico.modules.service.SysUserService;
import com.dico.result.ResultData;
import com.dico.result.ValidatedResult;
import com.dico.util.TokenUtils;
import com.dico.util.TransmitUtils;
import com.dico.util.ValiDatedUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 消息通知模块
 *
 * @author Gaodl
 * @version v1.0
 * 文件名称: NoticeMessageController
 * 创建时间: 2019/2/25
 */
@RestController
@Api(tags = "消息通知模块", produces = "消息通知模块Api")
public class NoticeMessageController {

    @Autowired
    private NoticeMessageService noticeMessageService;

    @Autowired
    private NoticePersonService noticePersonService;
    @Autowired
    private SysUserService sysUserService;

    /**
     * 分页查询方法
     *
     * @author Gaodl
     * 方法名称: dataPage
     * 参数： [title, createBeginDate, createEndDate, pageNum, pageSize]
     * 返回值： com.dico.result.ResultData
     * 创建时间: 2019/1/8
     */
    @ResponseBody
    @GetMapping("/notice/dataPage")
    @ApiOperation(value = "获取通知消息分页数据", notes = "获取分页数据方法")
    public ResultData dataPage(String title, String createBeginDate, String createEndDate, @RequestParam(required = false, defaultValue = "0", value = "pageNum") int pageNum, @RequestParam(required = false, defaultValue = "10", value = "pageSize") int pageSize) {
        Page<NoticeMessage> noticeMessagePage;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Map<String, Object> queryMap = new HashMap<>(3);
            queryMap.put("delFlag_EQ", false);
            if (StringUtils.isNotBlank(title)) {
                queryMap.put("title_LIKE", title);
            }
            if (null != createBeginDate && null != createEndDate) {
                Map<String, Date> queryDateMap = new HashMap<>(2);
                queryDateMap.put("beginDate", sdf.parse(createBeginDate));
                queryDateMap.put("endDate", sdf.parse(createEndDate));
                queryMap.put("createDate_BETWEEN", queryDateMap);
            }
            Sort sort = new Sort(Sort.Direction.DESC, "createDate");
            noticeMessagePage = noticeMessageService.findAll(queryMap, pageNum, pageSize, sort);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult(noticeMessagePage);
    }

    /**
     * @param userId
     * @param pageNum
     * @param pageSize
     * @return
     * @author xipeifeng
     * @description 根据接收人读取通知消息
     * @since 2019-5-8
     */
    @ResponseBody
    @GetMapping("/notice/noticeMessageByUserId")
    @ApiOperation(value = "根据接收人获取通知消息", notes = "获取分页数据方法")
    public ResultData noticeMessageByUserId(String userId, @RequestParam(required = false, defaultValue = "0", value = "pageNum") int pageNum, @RequestParam(required = false, defaultValue = "10", value = "pageSize") int pageSize) {
        Page<NoticePerson> noticeMessagePersonPage;
        try {
            Map<String, Object> queryMap = new HashMap<>(3);
            queryMap.put("delFlag_EQ", false);
            if (StringUtils.isNotBlank(userId)) {
                queryMap.put("userId_EQ", userId);
            }
            Sort sort = new Sort(Sort.Direction.DESC, "createDate");
            noticeMessagePersonPage = noticePersonService.findAll(queryMap, pageNum, pageSize, sort);
            List<NoticePerson> list = noticeMessagePersonPage.getContent();
            for (NoticePerson person : list) {
                NoticeMessage message = noticeMessageService.getByIdEqualsAndDelFlagIsFalse(person.getMessageId());
                person.setNoticeMessage(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult(noticeMessagePersonPage);
    }

    /**
     * @param request
     * @param noticeMessage
     * @return
     * @author xipeifeng
     * @since 2019-5-13
     */
    @ResponseBody
    @PostMapping("/notice/save")
    @ApiOperation(value = "通知信息保存", notes = "保存方法")
    public ResultData save(HttpServletRequest request, @RequestBody NoticeMessage noticeMessage) {
        // 获取当前登陆人
        String currentUserId = TokenUtils.getUserIdByRequest(request);
        SysUser currentSysUser = sysUserService.findOne(currentUserId);
        noticeMessage.setCreateUser(currentUserId);
        noticeMessage.setCreateUserName(currentSysUser.getName());
        noticeMessage.setCreateDate(new Date());
        try {
            noticeMessageService.save(noticeMessage);
        } catch (Exception e) {
            return new ResultData().setCode(1).setMsg(e.getMessage());
        }
        List<NoticePerson> peopleList = new ArrayList<NoticePerson>();
        //接收人信息
        if (StringUtils.isNotBlank(noticeMessage.getReceive())) {
            String[] people = noticeMessage.getReceive().split(",");
            for (int i = 0; i < people.length; i++) {
                if (StringUtils.isNotBlank(people[i])) {
                    NoticePerson noticePerson = new NoticePerson();
                    noticePerson.setMessageId(noticeMessage.getId());
                    noticePerson.setReadFlag(false);
                    noticePerson.setUserId(people[i]);
                    noticePerson.setUserName(sysUserService.findOne(people[i]).getUsername());
                    noticePerson.setCreateUser(currentUserId);
                    noticePerson.setCreateUserName(currentSysUser.getName());
                    noticePerson.setCreateDate(new Date());
                    peopleList.add(noticePerson);
                }
            }
        }
        //增加接收人
        noticePersonService.save(peopleList);
        return new ResultData().setCode(0).setMsg("保存成功");
    }

    /**
     * 物理删除
     *
     * @author xipeifeng
     * 方法名称: delete
     * 参数： [ids]
     * 返回值： com.dico.result.ResultData
     * 创建时间: 2019/5/13
     */
    @ResponseBody
    @DeleteMapping("/notice/delete")
    @ApiOperation(value = "物理删除", notes = "物理删除方法,可执行批量删除,参数用\",\"隔开")
    public ResultData delete(String ids) {
        try {
            NoticeMessage message = noticeMessageService.getByIdEqualsAndDelFlagIsFalse(ids);
            List<NoticePerson> noticePersonList = noticePersonService.findByMessageId(message.getId());
            for (NoticePerson p : noticePersonList) {
                p.setDelFlag(true);
                noticePersonService.deleteById(p.getId());
            }
            noticeMessageService.deleteByIdIn(ids.split(","));
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }

    /**
     * 逻辑删除
     *
     * @author xipeifeng
     * 方法名称: remove
     * 参数： [ids]
     * 返回值： com.dico.result.ResultData
     * 创建时间: 2019/5/13
     */
    @ResponseBody
    @DeleteMapping("/notice/remove")
    @ApiOperation(value = "逻辑删除", notes = "逻辑删除方法")
    public ResultData remove(String id) {
        if (StringUtils.isNotBlank(id)) {
            try {
                NoticeMessage message = noticeMessageService.getByIdEqualsAndDelFlagIsFalse(id);
                message.setDelFlag(true);
                noticeMessageService.update(message);
                List<NoticePerson> noticePersonList = noticePersonService.findByMessageId(message.getId());
                for (NoticePerson p : noticePersonList) {
                    p.setDelFlag(true);
                    noticePersonService.update(p);
                }
            } catch (Exception e) {
                return ResultData.getFailResult(e.getMessage());
            }
        }

        return ResultData.getSuccessResult();
    }

    /**
     * 更新方法
     *
     * @author xipeifeng
     * 方法名称: update
     * 参数： [request, noticeMessage]
     * 返回值： com.dico.result.ResultData
     * 创建时间: 2019/5/13
     */
    @ResponseBody
    @PutMapping("/notice/update")
    @ApiOperation(value = "修改", notes = "修改方法")
    public ResultData update(HttpServletRequest request, @RequestBody NoticeMessage noticeMessage) {
        try {
            // 获取当前登录用户
            String userId = TokenUtils.getUserIdByRequest(request);
            // 根据ID查询数据
            NoticeMessage _message = noticeMessageService.getByIdEqualsAndDelFlagIsFalse(noticeMessage.getId());
            // 校验实体类，如果校验不通过，则返回错误信息
            ValidatedResult validatedResult = ValiDatedUtils.valiDatedBean(noticeMessage);
            if (!validatedResult.isStatus()) {
                return ResultData.getFailResult(validatedResult.getMessage());
            }
            TransmitUtils.sources2destination(noticeMessage, _message);
            _message.setUpdateDate(new Date());
            _message.setUpdateUser(userId);
            noticeMessageService.update(_message);
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }

    /**
     * 通知查询
     *
     * @author xipeifeng
     * 方法名称: get
     * 参数： [id]
     * 返回值： com.dico.result.ResultData
     * 创建时间: 2019/10/21
     */
    @ResponseBody
    @GetMapping("/notice/dataInfo")
    @ApiOperation(value = "查询通知详情", notes = "查询通知详情")
    public ResultData dataInfo(String id) {
        NoticeMessage message = noticeMessageService.getByIdEqualsAndDelFlagIsFalse(id);
        if (StringUtils.isBlank(id)) {
            return ResultData.getFailResult("数据不存在");
        }
        return ResultData.getSuccessResult(message);
    }
}

package com.jdpu.examsystem.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


import cn.hutool.core.collection.ListUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jdpu.examsystem.entity.TExamNoticeEntity;
import com.jdpu.examsystem.vo.ExamNoticeVo;
import com.jdpu.common.entity.vo.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.jdpu.examsystem.service.TExamNoticeService;
import com.jdpu.common.utils.PageUtils;
import com.jdpu.common.utils.R;



/**
 * 考试通知表
 *
 * @author zuck
 * @date 2022-03-27 22:53:42
 */
@RestController
@RequestMapping("/api/examsystem/texamnotice")
public class TExamNoticeController {
    @Autowired
    private TExamNoticeService tExamNoticeService;

    /**
     * 已读全部通知
     */
    @GetMapping("/allReadNotice/{userId}")
    public RestResponse allReadNotice(@PathVariable("userId") Integer userId) {
        List<TExamNoticeEntity> noticeEntities = tExamNoticeService.list(new QueryWrapper<TExamNoticeEntity>().eq("user_id", userId));
        noticeEntities.forEach(notice -> {
            notice.setIsRead(1);
            tExamNoticeService.updateById(notice);
        });
        return RestResponse.ok();
    }


    /**
     * 清空通知(已读的）
     */
    @GetMapping("/deleteUserNotice/{userId}")
    public RestResponse deleteUserNotice(@PathVariable("userId") Integer userId) {
        boolean remove = tExamNoticeService.remove(new QueryWrapper<TExamNoticeEntity>().eq("user_id", userId).eq("is_read",1));
        return RestResponse.ok(remove);
    }

    /**
     * 根据user_id获取用户未读通知数量
     */
    @GetMapping("/getUnReadCount/{userId}")
    public RestResponse getUnReadCount(@PathVariable("userId") Integer userId) {
        Integer count = tExamNoticeService.count(new QueryWrapper<TExamNoticeEntity>()
                .eq("user_id",userId).eq("is_read",0));
        return RestResponse.ok(count);
    }

    /**
     * 根据User_id获取与用户相关的通知
     */
    @GetMapping("/getNotice/{userId}")
    public RestResponse getNotice(@PathVariable("userId") Integer userId) {
        List<ExamNoticeVo> noticeEntityList = tExamNoticeService.getNoticeListByUserId(userId);
        List<ExamNoticeVo> reverse = ListUtil.reverse(noticeEntityList);
        return RestResponse.ok(reverse);
    }

    /**
     * 设置用户已读通知
     */
    @PostMapping("/isRead")
    public RestResponse isRead(@RequestBody String jsonStr){
        JSONObject jsonObject = JSONUtil.parseObj(jsonStr);
        String userId = jsonObject.getStr("userId");
        String argumentsId = jsonObject.getStr("argumentsId");

        boolean res = tExamNoticeService.update(new UpdateWrapper<TExamNoticeEntity>().eq("user_id",userId).eq("exam_arguments_id",argumentsId).set("is_read",1));
        return res ? RestResponse.ok() :RestResponse.fail(500,"设置用户已读失败");
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
   // @RequiresPermissions("examsystem:texamnotice:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = tExamNoticeService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
   // @RequiresPermissions("examsystem:texamnotice:info")
    public R info(@PathVariable("id") Integer id){
		TExamNoticeEntity tExamNotice = tExamNoticeService.getById(id);

        return R.ok().put("tExamNotice", tExamNotice);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("examsystem:texamnotice:save")
    public R save(@RequestBody TExamNoticeEntity tExamNotice){
		tExamNoticeService.save(tExamNotice);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
   // @RequiresPermissions("examsystem:texamnotice:update")
    public R update(@RequestBody TExamNoticeEntity tExamNotice){
		tExamNoticeService.updateById(tExamNotice);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
  //  @RequiresPermissions("examsystem:texamnotice:delete")
    public R delete(@RequestBody Integer[] ids){
		tExamNoticeService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}

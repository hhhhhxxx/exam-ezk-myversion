package com.jdpu.examsystem.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jdpu.examsystem.service.TExamRoomService;
import com.jdpu.examsystem.entity.TExamRoomUserRelationEntity;
import com.jdpu.examsystem.service.TExamRoomUserRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.jdpu.examsystem.entity.TExamRoomEntity;
import com.jdpu.common.utils.PageUtils;
import com.jdpu.common.utils.R;



/**
 * 考场编排表
 *
 * @author zuck
 * @email ${email}
 * @date 2022-04-11 15:37:04
 */
@RestController
@RequestMapping("/api/examsystem/texamroom")
public class TExamRoomController {
    @Autowired
    private TExamRoomService tExamRoomService;
    @Autowired
    private TExamRoomUserRelationService tExamRoomUserRelationService;

    /**
     * 根据考场id获取考生列表
     */
    @GetMapping("/getRoomUserList/{roomId}")
    public R getRoomUserList(@PathVariable Integer roomId) {
        List<TExamRoomUserRelationEntity> list = tExamRoomUserRelationService.list(new QueryWrapper<TExamRoomUserRelationEntity>()
                .eq("exam_room_id", roomId));
        return R.ok().put("data",list);
    }

    /**
     * 将考生加入到考场
      */
    @PostMapping("/addUserToRoom")
    public R addUserToRoom(@RequestBody String jsonStr){
        JSONObject jsonObject = JSONUtil.parseObj(jsonStr);
        String roomId = jsonObject.getStr("roomId");
        JSONArray userIdsJson = jsonObject.getJSONArray("userIds");

        // 去重
        List<Integer> userIds = userIdsJson.toList(Integer.class)
                .stream().distinct().collect(Collectors.toList());

        // 添加关联关系
        TExamRoomUserRelationEntity roomUserRelationEntity = new TExamRoomUserRelationEntity();
        roomUserRelationEntity.setExamRoomId(Integer.valueOf(roomId));

        userIds.forEach(userId ->{
            // 先查看数据库是否存在重复数据
            TExamRoomUserRelationEntity one = tExamRoomUserRelationService.getOne(
                    new QueryWrapper<TExamRoomUserRelationEntity>().eq("exam_room_id", roomId).eq("user_id", userId));
            if (one == null) {
                roomUserRelationEntity.setUserId(userId);
                tExamRoomUserRelationService.save(roomUserRelationEntity);
            }
        });

        return R.ok();
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
   // @RequiresPermissions("examsystem:texamroom:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = tExamRoomService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
   // @RequiresPermissions("examsystem:texamroom:info")
    public R info(@PathVariable("id") Integer id){
		TExamRoomEntity tExamRoom = tExamRoomService.getById(id);

        return R.ok().put("tExamRoom", tExamRoom);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("examsystem:texamroom:save")
    public R save(@RequestBody TExamRoomEntity tExamRoom){
		tExamRoomService.save(tExamRoom);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
   // @RequiresPermissions("examsystem:texamroom:update")
    public R update(@RequestBody TExamRoomEntity tExamRoom){
		tExamRoomService.updateById(tExamRoom);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
  //  @RequiresPermissions("examsystem:texamroom:delete")
    public R delete(@RequestBody Integer[] ids){
		tExamRoomService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}

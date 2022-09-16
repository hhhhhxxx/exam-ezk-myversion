package com.jdpu.api.controller.student;


import com.jdpu.api.entities.count.AverageScoreAndTime;
import com.jdpu.api.service.StudentCountService;
import com.jdpu.common.entity.vo.RestResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: xJh
 * @Date: 2022/3/24
 */
@Api(value = "学生统计模块")
@RestController
@RequestMapping("/stu/count")
public class StudentCountController {
    @Autowired
    private StudentCountService studentCountService;

    @ApiOperation("根据学科获取当前学生的学科均分以及考试平均时间")
    @GetMapping("/AvergeScoreAndTime")
    public RestResponse getAverageScoreAnd(@RequestParam("userId")Integer userId, @RequestParam("subjectId")Integer subjectId){
        AverageScoreAndTime averageScoreAndTime = studentCountService.GetAverageScoreAndTime(subjectId, userId);
        return RestResponse.ok(averageScoreAndTime);
    }

    @ApiOperation("根据用户id获取当前学生所有学科的平均分及平均考试时间")
    @GetMapping("/AllAvergeScoreAndTime")
    public RestResponse getAllAverage(@RequestParam("userId")Integer userId){
        List<AverageScoreAndTime> list = studentCountService.GetAllAverageScoreAndTime(userId);
        return RestResponse.ok(list);
    }

    @ApiOperation("根据用户id获取当前已经完成的试卷数量")
    @GetMapping("AllFinishedPaper")
    public RestResponse getAllFinished(@RequestParam("userId")Integer userId){
        Integer integer = studentCountService.FinishedPaper(userId);
        return RestResponse.ok(integer);
    }
}

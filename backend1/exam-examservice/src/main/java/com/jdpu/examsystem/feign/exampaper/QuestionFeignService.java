package com.jdpu.examsystem.feign.exampaper;


import com.jdpu.examsystem.vo.dto.admin.question.QuestionEditRequestVM;
import com.jdpu.common.entity.vo.RestResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 题目服务远程调用
 */
@FeignClient(name = "exam-api",path = "/api/admin/question")
public interface QuestionFeignService {
    @RequestMapping(value = "/select/{id}", method = RequestMethod.POST)
    public RestResponse<QuestionEditRequestVM> select(@PathVariable Integer id);
}

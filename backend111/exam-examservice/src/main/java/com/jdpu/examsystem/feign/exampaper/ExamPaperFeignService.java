package com.jdpu.examsystem.feign.exampaper;

import com.jdpu.examsystem.vo.dto.admin.exam.ExamPaperEditRequestVM;
import com.jdpu.examsystem.vo.dto.admin.exam.ExamPaperPageRequestVM;
import com.jdpu.examsystem.vo.dto.admin.exam.ExamResponseVM;
import com.github.pagehelper.PageInfo;
import com.jdpu.common.entity.vo.RestResponse;
import com.netflix.ribbon.proxy.annotation.Http;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 试卷服务远程调用
 */
@FeignClient(name = "exam-api",path = "/api/admin/exam/paper")
public interface ExamPaperFeignService {
    /**
     * 获取试卷
     * @param id 试卷id
     */
    @RequestMapping(value = "/select/{id}", method = RequestMethod.POST)
    RestResponse<ExamPaperEditRequestVM> select(@PathVariable Integer id);

    /**
     * 试卷编辑
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    RestResponse<ExamPaperEditRequestVM> edit(@RequestBody @Valid ExamPaperEditRequestVM model);

    /**
     * 试卷删除
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    RestResponse delete(@PathVariable Integer id);

    /**
     * 试卷列表分页
     * @param model
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    RestResponse<PageInfo<ExamResponseVM>> pageList(@RequestBody ExamPaperPageRequestVM model);
}

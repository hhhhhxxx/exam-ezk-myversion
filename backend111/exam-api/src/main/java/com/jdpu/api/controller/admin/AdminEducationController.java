package com.jdpu.api.controller.admin;


import com.jdpu.api.context.UserBaseApiController;
import com.jdpu.api.entities.Subject;
import com.jdpu.api.service.SubjectService;
import com.jdpu.common.param.admin.education.SubjectEditRequestVM;
import com.jdpu.common.param.admin.education.SubjectPageRequestVM;
import com.jdpu.common.param.admin.education.SubjectResponseVM;
import com.github.pagehelper.PageInfo;

import com.jdpu.common.entity.vo.RestResponse;
import com.jdpu.common.xzsOld.utils.PageInfoHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/api/admin/education")
public class AdminEducationController extends UserBaseApiController {

    private final SubjectService subjectService;

    @Autowired
    public AdminEducationController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    /**
     * 学科列表
     */
    @RequestMapping(value = "/subject/list", method = RequestMethod.POST)
    public RestResponse<List<Subject>> list() {
        List<Subject> subjects = subjectService.allSubject();
        return RestResponse.ok(subjects);
    }

    /**
     * 学科分页
     * @param model
     * {
     *     "level": null,  //年级
     *     "pageIndex": 1,
     *     "pageSize": 10
     * }
     */
    @RequestMapping(value = "/subject/page", method = RequestMethod.POST)
    public RestResponse<PageInfo<SubjectResponseVM>> pageList(@RequestBody SubjectPageRequestVM model) {
        PageInfo<Subject> pageInfo = subjectService.page(model);
        PageInfo<SubjectResponseVM> page = PageInfoHelper.copyMap(pageInfo, e -> modelMapper.map(e, SubjectResponseVM.class));
        return RestResponse.ok(page);
    }

    /**
     * 学科编辑
     * @param model
     * {
     *     "id": 41,
     *     "name": "数学",  //学科名称
     *     "level": 2,   //年级
     *     "levelName": "二年级"  //年级名称
     * }
     */
    @RequestMapping(value = "/subject/edit", method = RequestMethod.POST)
    public RestResponse edit(@RequestBody @Valid SubjectEditRequestVM model) {
        Subject subject = modelMapper.map(model, Subject.class);
        if (model.getId() == null) {
            subject.setDeleted(false);
            subjectService.insertByFilter(subject);
        } else {
            subjectService.updateByIdFilter(subject);
        }
        return RestResponse.ok();
    }

    /**
     * 学科查询
     * @param id 学科id
     */
    @RequestMapping(value = "/subject/select/{id}", method = RequestMethod.POST)
    public RestResponse<SubjectEditRequestVM> select(@PathVariable Integer id) {
        Subject subject = subjectService.selectById(id);
        SubjectEditRequestVM vm = modelMapper.map(subject, SubjectEditRequestVM.class);
        return RestResponse.ok(vm);
    }

    /**
     * 学科删除
     * @param id 学科id
     */
    @RequestMapping(value = "/subject/delete/{id}", method = RequestMethod.POST)
    public RestResponse delete(@PathVariable Integer id) {
        Subject subject = subjectService.selectById(id);
        subject.setDeleted(true);
        subjectService.updateByIdFilter(subject);
        return RestResponse.ok();
    }
}

package com.jdpu.api.service;


import com.jdpu.api.entities.Subject;
import com.jdpu.common.param.admin.education.SubjectPageRequestVM;
import com.github.pagehelper.PageInfo;
import com.jdpu.common.xzsOld.service.BaseService;

import java.util.List;

public interface SubjectService extends BaseService<Subject> {

    List<Subject> getSubjectByLevel(Integer level);

    List<Subject> allSubject();

    Integer levelBySubjectId(Integer id);

    PageInfo<Subject> page(SubjectPageRequestVM requestVM);
}

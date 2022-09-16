package com.jdpu.api.service;

import com.jdpu.api.entities.ExamPaper;
import com.jdpu.common.param.admin.exam.ExamPaperEditRequestVM;
import com.jdpu.common.param.admin.exam.ExamPaperPageRequestVM;
import com.jdpu.common.param.admin.exam.autopaper.ExamAutoPaperRequestVM;
import com.jdpu.common.param.student.dashboard.PaperFilter;
import com.jdpu.common.param.student.dashboard.PaperInfo;
import com.jdpu.common.param.student.exam.ExamPaperPageVM;
import com.github.pagehelper.PageInfo;
import com.jdpu.common.entity.UserEntity;
import com.jdpu.common.xzsOld.service.BaseService;

import java.util.List;

public interface ExamPaperService extends BaseService<ExamPaper> {
    PageInfo<ExamPaper> page(ExamPaperPageRequestVM requestVM);

    PageInfo<ExamPaper> taskExamPage(ExamPaperPageRequestVM requestVM);

    PageInfo<ExamPaper> studentPage(ExamPaperPageVM requestVM);

    ExamPaper savePaperFromVM(ExamPaperEditRequestVM examPaperEditRequestVM, UserEntity user);

    ExamPaperEditRequestVM examPaperToVM(Integer id);

    List<PaperInfo> indexPaper(PaperFilter paperFilter);

    Integer selectAllCount();

    List<Integer> selectMothCount();

    ExamPaper autoCreatePaper(ExamAutoPaperRequestVM autoPaperRequestVM);
}

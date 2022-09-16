package com.jdpu.api.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.jdpu.api.entities.ExamPaper;
import com.jdpu.api.entities.Question;
import com.jdpu.api.entities.TextContent;
import com.jdpu.api.entities.enums.ActionEnum;
import com.jdpu.api.entities.enums.ExamPaperTypeEnum;
import com.jdpu.api.entities.exam.ExamPaperQuestionItemObject;
import com.jdpu.api.entities.exam.ExamPaperTitleItemObject;
import com.jdpu.api.mapper.ExamPaperMapper;
import com.jdpu.api.mapper.QuestionMapper;
import com.jdpu.api.service.ExamPaperService;
import com.jdpu.api.service.QuestionService;
import com.jdpu.api.service.SubjectService;
import com.jdpu.api.service.TextContentService;
import com.jdpu.common.param.admin.exam.ExamPaperEditRequestVM;
import com.jdpu.common.param.admin.exam.ExamPaperPageRequestVM;
import com.jdpu.common.param.admin.exam.ExamPaperTitleItemVM;
import com.jdpu.common.param.admin.exam.autopaper.ExamAutoPaperRequestVM;
import com.jdpu.common.param.admin.question.QuestionEditRequestVM;
import com.jdpu.common.param.student.dashboard.PaperFilter;
import com.jdpu.common.param.student.dashboard.PaperInfo;
import com.jdpu.common.param.student.exam.ExamPaperPageVM;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jdpu.common.entity.UserEntity;
import com.jdpu.common.xzsOld.entities.KeyValue;
import com.jdpu.common.xzsOld.service.impl.BaseServiceImpl;
import com.jdpu.common.xzsOld.utils.DateTimeUtil;
import com.jdpu.common.xzsOld.utils.ExamUtil;
import com.jdpu.common.xzsOld.utils.JsonUtil;
import com.jdpu.common.xzsOld.utils.ModelMapperSingle;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class ExamPaperServiceImpl extends BaseServiceImpl<ExamPaper> implements ExamPaperService {

    protected final static ModelMapper modelMapper = ModelMapperSingle.Instance();
    private final ExamPaperMapper examPaperMapper;
    private final QuestionMapper questionMapper;
    private final TextContentService textContentService;
    private final QuestionService questionService;
    private final SubjectService subjectService;

    @Autowired
    public ExamPaperServiceImpl(ExamPaperMapper examPaperMapper, QuestionMapper questionMapper, TextContentService textContentService, QuestionService questionService, SubjectService subjectService) {
        super(examPaperMapper);
        this.examPaperMapper = examPaperMapper;
        this.questionMapper = questionMapper;
        this.textContentService = textContentService;
        this.questionService = questionService;
        this.subjectService = subjectService;
    }


    @Override
    public PageInfo<ExamPaper> page(ExamPaperPageRequestVM requestVM) {
        return PageHelper.startPage(requestVM.getPageIndex(), requestVM.getPageSize(), "id desc").doSelectPageInfo(() ->
                examPaperMapper.page(requestVM));
    }

    @Override
    public PageInfo<ExamPaper> taskExamPage(ExamPaperPageRequestVM requestVM) {
        return PageHelper.startPage(requestVM.getPageIndex(), requestVM.getPageSize(), "id desc").doSelectPageInfo(() ->
                examPaperMapper.taskExamPage(requestVM));
    }

    @Override
    public PageInfo<ExamPaper> studentPage(ExamPaperPageVM requestVM) {
        return PageHelper.startPage(requestVM.getPageIndex(), requestVM.getPageSize(), "id desc").doSelectPageInfo(() ->
                examPaperMapper.studentPage(requestVM));
    }


    @Override
    @Transactional
    public ExamPaper savePaperFromVM(ExamPaperEditRequestVM examPaperEditRequestVM, UserEntity user) {
        ActionEnum actionEnum = (examPaperEditRequestVM.getId() == null) ? ActionEnum.ADD : ActionEnum.UPDATE;
        Date now = new Date();
        List<ExamPaperTitleItemVM> titleItemsVM = examPaperEditRequestVM.getTitleItems();
        List<ExamPaperTitleItemObject> frameTextContentList = frameTextContentFromVM(titleItemsVM);
        String frameTextContentStr = JsonUtil.toJsonStr(frameTextContentList);

        ExamPaper examPaper;
        if (actionEnum == ActionEnum.ADD) {
            examPaper = modelMapper.map(examPaperEditRequestVM, ExamPaper.class);
            TextContent frameTextContent = new TextContent(frameTextContentStr, now);
            textContentService.insertByFilter(frameTextContent);
            examPaper.setFrameTextContentId(frameTextContent.getId());
            examPaper.setCreateTime(now);
            examPaper.setCreateUser(user.getId());
            examPaper.setDeleted(false);
            examPaperFromVM(examPaperEditRequestVM, examPaper, titleItemsVM);
            examPaperMapper.insertSelective(examPaper);
        } else {
            examPaper = examPaperMapper.selectByPrimaryKey(examPaperEditRequestVM.getId());
            TextContent frameTextContent = textContentService.selectById(examPaper.getFrameTextContentId());
            frameTextContent.setContent(frameTextContentStr);
            textContentService.updateByIdFilter(frameTextContent);
            modelMapper.map(examPaperEditRequestVM, examPaper);
            examPaperFromVM(examPaperEditRequestVM, examPaper, titleItemsVM);
            examPaperMapper.updateByPrimaryKeySelective(examPaper);
        }
        return examPaper;
    }

    @Override
    public ExamPaperEditRequestVM examPaperToVM(Integer id) {
        // t_exam_paper
        ExamPaper examPaper = examPaperMapper.selectByPrimaryKey(id);

        //id=1, level=null, subjectId=1, paperType=1, name=其中考试, suggestTime=60, limitDateTime=null, titleItems=null, score=null
        ExamPaperEditRequestVM vm = modelMapper.map(examPaper, ExamPaperEditRequestVM.class);
        vm.setLevel(examPaper.getGradeLevel());

        // t_text_context
        TextContent frameTextContent = textContentService.selectById(examPaper.getFrameTextContentId());


        List<ExamPaperTitleItemObject> examPaperTitleItemObjects = JsonUtil.toJsonListObject(frameTextContent.getContent(), ExamPaperTitleItemObject.class);

        // 问题的id数组
        List<Integer> questionIds = examPaperTitleItemObjects.stream()
                .flatMap(t -> t.getQuestionItems().stream()
                        .map(q -> q.getId()))
                .collect(Collectors.toList());


        List<Question> questions = questionMapper.selectByIds(questionIds);

        List<ExamPaperTitleItemVM> examPaperTitleItemVMS = examPaperTitleItemObjects.stream().map(t -> {
            // ExamPaperTitleItemObject -> ExamPaperTitleItemVM
            ExamPaperTitleItemVM tTitleVM = modelMapper.map(t, ExamPaperTitleItemVM.class);
            List<QuestionEditRequestVM> questionItemsVM = t.getQuestionItems().stream().map(i -> {
                Question question = questions.stream().filter(q -> q.getId().equals(i.getId())).findFirst().get();
                QuestionEditRequestVM questionEditRequestVM = questionService.getQuestionEditRequestVM(question);
                questionEditRequestVM.setItemOrder(i.getItemOrder());
                return questionEditRequestVM;
            }).collect(Collectors.toList());
            tTitleVM.setQuestionItems(questionItemsVM);
            return tTitleVM;
        }).collect(Collectors.toList());



        vm.setTitleItems(examPaperTitleItemVMS);
        vm.setScore(ExamUtil.scoreToVM(examPaper.getScore()));
        // 设置监考老师
        // vm.setTeacherName(examPaper.getTeacherName());

        if (ExamPaperTypeEnum.TimeLimit == ExamPaperTypeEnum.fromCode(examPaper.getPaperType())) {
            List<String> limitDateTime = Arrays.asList(DateTimeUtil.dateFormat(examPaper.getLimitStartTime()), DateTimeUtil.dateFormat(examPaper.getLimitEndTime()));
            vm.setLimitDateTime(limitDateTime);
        }

        return vm;
    }

    @Override
    public List<PaperInfo> indexPaper(PaperFilter paperFilter) {
        return examPaperMapper.indexPaper(paperFilter);
    }


    @Override
    public Integer selectAllCount() {
        return examPaperMapper.selectAllCount();
    }

    @Override
    public List<Integer> selectMothCount() {
        Date startTime = DateTimeUtil.getMonthStartDay();
        Date endTime = DateTimeUtil.getMonthEndDay();
        List<KeyValue> mouthCount = examPaperMapper.selectCountByDate(startTime, endTime);
        List<String> mothStartToNowFormat = DateTimeUtil.MothStartToNowFormat();
        return mothStartToNowFormat.stream().map(md -> {
            KeyValue keyValue = mouthCount.stream().filter(kv -> kv.getName().equals(md)).findAny().orElse(null);
            return null == keyValue ? 0 : keyValue.getValue();
        }).collect(Collectors.toList());
    }

    /**
     * 自动组卷
     * @param autoPaperRequestVM
     * @return
     */
    @Override
    public ExamPaper autoCreatePaper(ExamAutoPaperRequestVM autoPaperRequestVM) {
        // 1.从题库中随机获取difficult,subjectId为指定值的question数据
        Integer difficult = autoPaperRequestVM.getDifficult();
        Integer subjectId = autoPaperRequestVM.getSubjectId();
        Integer titleCount = autoPaperRequestVM.getTitleCount();
        // 获取问题id列表,从中获取titleCount数量的id
        List<Integer> questionIdList = questionService.selectIdsByDifficultAndSubjectId(difficult,subjectId);
        List<Integer> indexList = new ArrayList<>();
        List<Integer> newQuestionIdList = new ArrayList<>();
        for (int i = 0; i < titleCount; i++) {
            int randomIndex = RandomUtil.randomInt(0, questionIdList.size());
            while (indexList.contains(randomIndex)) {
                // 避免重复
                randomIndex = RandomUtil.randomInt(0, questionIdList.size());
            }
            indexList.add(randomIndex);
            newQuestionIdList.add(questionIdList.get(randomIndex));
        }
        // 根据questionId获取QuestionEditRequestVM
        List<QuestionEditRequestVM> questionList = newQuestionIdList.stream().map(questionId -> {
            QuestionEditRequestVM question = questionService.getQuestionEditRequestVM(questionId);
            return question;
        }).collect(Collectors.toList());

        List<ExamPaperTitleItemVM> titleItemList = new ArrayList<>();
        ExamPaperTitleItemVM titleItem = new ExamPaperTitleItemVM();
        titleItem.setName("自动组卷默认题目");
        titleItem.setQuestionItems(questionList);
        titleItemList.add(titleItem);

        List<ExamPaperTitleItemObject> frameTextContentList = frameTextContentFromVM(titleItemList);
        String frameTextContentStr = JsonUtil.toJsonStr(frameTextContentList);

        // 构建ExamPaper对象
        Date now = new Date();
        ExamPaper examPaper = new ExamPaper();

        TextContent frameTextContent = new TextContent(frameTextContentStr, now);
        textContentService.insertByFilter(frameTextContent);
        examPaper.setFrameTextContentId(frameTextContent.getId());

        // 试卷
        examPaper.setGradeLevel(autoPaperRequestVM.getLevel());
        examPaper.setSubjectId(autoPaperRequestVM.getSubjectId());
        examPaper.setPaperType(autoPaperRequestVM.getPaperType());
        examPaper.setName(autoPaperRequestVM.getName());
        examPaper.setSuggestTime(autoPaperRequestVM.getSuggestTime());
        examPaper.setCreateTime(now);
        examPaper.setScore(100);
        examPaper.setQuestionCount(titleCount);
        examPaper.setDeleted(false);
        examPaperMapper.insertSelective(examPaper);

        return examPaper;
    }

    private void examPaperFromVM(ExamPaperEditRequestVM examPaperEditRequestVM, ExamPaper examPaper, List<ExamPaperTitleItemVM> titleItemsVM) {
        Integer gradeLevel = subjectService.levelBySubjectId(examPaperEditRequestVM.getSubjectId());
        Integer questionCount = titleItemsVM.stream()
                .mapToInt(t -> t.getQuestionItems().size()).sum();
        Integer score = titleItemsVM.stream().
                flatMapToInt(t -> t.getQuestionItems().stream()
                        .mapToInt(q -> ExamUtil.scoreFromVM(q.getScore()))
                ).sum();
        examPaper.setQuestionCount(questionCount);
        examPaper.setScore(score);
        examPaper.setGradeLevel(gradeLevel);
        List<String> dateTimes = examPaperEditRequestVM.getLimitDateTime();
        if (ExamPaperTypeEnum.TimeLimit == ExamPaperTypeEnum.fromCode(examPaper.getPaperType())) {
            examPaper.setLimitStartTime(DateTimeUtil.parse(dateTimes.get(0), DateTimeUtil.STANDER_FORMAT));
            examPaper.setLimitEndTime(DateTimeUtil.parse(dateTimes.get(1), DateTimeUtil.STANDER_FORMAT));
        }
    }

    private List<ExamPaperTitleItemObject> frameTextContentFromVM(List<ExamPaperTitleItemVM> titleItems) {
        AtomicInteger index = new AtomicInteger(1);
        return titleItems.stream().map(t -> {
            ExamPaperTitleItemObject titleItem = modelMapper.map(t, ExamPaperTitleItemObject.class);
            List<ExamPaperQuestionItemObject> questionItems = t.getQuestionItems().stream()
                    .map(q -> {
                        ExamPaperQuestionItemObject examPaperQuestionItemObject = modelMapper.map(q, ExamPaperQuestionItemObject.class);
                        examPaperQuestionItemObject.setItemOrder(index.getAndIncrement());
                        return examPaperQuestionItemObject;
                    })
                    .collect(Collectors.toList());
            titleItem.setQuestionItems(questionItems);
            return titleItem;
        }).collect(Collectors.toList());
    }
}

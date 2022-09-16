package com.jdpu.examsystem.service.impl;

import cn.hutool.json.JSONObject;
import com.jdpu.examsystem.dao.TExamInfoDao;
import com.jdpu.examsystem.entity.TExamInfoEntity;
import com.jdpu.examsystem.service.TExamInfoService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jdpu.common.utils.PageUtils;
import com.jdpu.common.utils.Query;


@Service("tExamInfoService")
public class TExamInfoServiceImpl extends ServiceImpl<TExamInfoDao, TExamInfoEntity> implements TExamInfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<TExamInfoEntity> page = this.page(
                new Query<TExamInfoEntity>().getPage(params),
                new QueryWrapper<TExamInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public Integer insertByStudent(JSONObject jsonObject) {
        String examArgumentsId = jsonObject.getStr("examArgumentsId");
        String studentName = jsonObject.getStr("studentName");

        QueryWrapper<TExamInfoEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("student_name",studentName);
        queryWrapper.eq("exam_arguments_id",examArgumentsId);
        TExamInfoEntity infoEntity = this.baseMapper.selectOne(queryWrapper);

        // 如果数据库存在 考试号与学生姓名对应的记录
        if (infoEntity == null) {
            TExamInfoEntity info = new TExamInfoEntity();
            info.setExamArgumentsId(Integer.valueOf(examArgumentsId));
            info.setStudentName(studentName);
            int insert = this.baseMapper.insert(info);
            if (insert == 0) {
                return null;
            }
//            TExamInfoEntity infoEntity2 = this.baseMapper.selectOne(queryWrapper);
            return info.getId();

        }else{
            return infoEntity.getId();
        }
    }

    @Override
    public boolean updateExitFull(JSONObject jsonObject) {
        String examArgumentsId = jsonObject.getStr("examArgumentsId");
        String studentName = jsonObject.getStr("studentName");
        String exitCount = jsonObject.getStr("exitCount");
        String examInfoId = jsonObject.getStr("examInfoId");

        try {
            // 前端能传来exam_info_id，那就直接查主键
            if (examInfoId != null) {
                TExamInfoEntity infoEntity = this.baseMapper.selectById(examInfoId);
                infoEntity.setExitFullCount(Integer.valueOf(exitCount));
                int count = this.baseMapper.updateById(infoEntity);
                return count > 0;
            }else{
                QueryWrapper<TExamInfoEntity> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("student_name",studentName);
                queryWrapper.eq("exam_arguments_id",examArgumentsId);
                TExamInfoEntity infoEntity = this.baseMapper.selectOne(queryWrapper);

                // 也可以让数据库直接加1，但如果多次考试就不能这样写
                // Integer pre = infoEntity.getExitFullCount();
                // infoEntity.setExitFullCount(pre + 1);
                infoEntity.setExitFullCount(Integer.valueOf(exitCount));
                int count = this.baseMapper.updateById(infoEntity);
                return count > 0;
            }
        } catch (NullPointerException e) {
            log.error("考生未确认姓名,退出了全屏",e);
            return false;
        }
    }

    @Override
    public boolean updateOverNum(JSONObject jsonObject) {
        String examInfoId = jsonObject.getStr("examInfoId");
        String peopleNumCount = jsonObject.getStr("peopleNumCount");
        TExamInfoEntity infoEntity = this.baseMapper.selectById(examInfoId);
        if (infoEntity != null) {
            infoEntity.setNumOverCount(Integer.valueOf(peopleNumCount));
            int count = this.baseMapper.updateById(infoEntity);
            return count > 0;
        }
        return false;
    }

    @Override
    public boolean updateNonLiveCount(JSONObject jsonObject) {
        String examInfoId = jsonObject.getStr("examInfoId");
        String nonLiveCount = jsonObject.getStr("nonLiveCount");
        TExamInfoEntity infoEntity = this.baseMapper.selectById(examInfoId);
        if (infoEntity != null) {
            infoEntity.setNonLiveCount(Integer.valueOf(nonLiveCount));
            int count = this.baseMapper.updateById(infoEntity);
            return count > 0;
        }
        return false;
    }

    @Override
    public boolean updateBeforeImg(JSONObject jsonObject) {
        String examInfoId = jsonObject.getStr("examInfoId");
        String webUrl = jsonObject.getStr("webUrl");
        TExamInfoEntity infoEntity = this.baseMapper.selectById(examInfoId);
        if (infoEntity != null) {
            infoEntity.setBeforeExamImg(webUrl);
            int count = this.baseMapper.updateById(infoEntity);
            return count > 0;
        }
        return false;
    }

    @Override
    public List<TExamInfoEntity> getExamInfoList(JSONObject jsonObject) {
        String examArgumentsId = jsonObject.getStr("examArgumentsId");
        QueryWrapper<TExamInfoEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("exam_arguments_id",examArgumentsId);
        return this.baseMapper.selectList(wrapper);
    }

    @Override
    public boolean updateCheckSex(JSONObject jsonObject) {
        String examInfoId = jsonObject.getStr("examInfoId");
        String gender = jsonObject.getStr("gender");
        TExamInfoEntity infoEntity = this.baseMapper.selectById(examInfoId);
        infoEntity.setCheckSex(Integer.valueOf(gender));
        int count = this.baseMapper.updateById(infoEntity);
        return count>0;
    }

    @Override
    public boolean getIsEnterExam(Integer argumentId, String stuName) {
        // 根据exam_paper_id,studentName获取列表TExamInfoEntity数据count
        QueryWrapper<TExamInfoEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("exam_arguments_id",argumentId).eq("student_name",stuName);
        Integer count = this.baseMapper.selectCount(wrapper);
        return count > 0;
    }
}

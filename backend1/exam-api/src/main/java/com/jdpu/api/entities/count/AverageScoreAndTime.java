package com.jdpu.api.entities.count;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: xJh
 * @Date: 2022/3/24
 * 学生学科平均分和平均作答时间返回实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AverageScoreAndTime {

    private String AverageScore;

    private String AverageTime;
}

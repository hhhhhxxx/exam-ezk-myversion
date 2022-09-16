package com.jdpu.examsystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;

@Data
@TableName("t_user_class")
public class UserClass implements Serializable {
    private static final long serialVersionUID = -2143539181805283910L;

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer userId;
    private Integer classId;
}

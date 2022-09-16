package com.whd;

import com.jdpu.examsystem.ExamExamSystemApplication;
import com.jdpu.examsystem.service.UserClassService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ExamExamSystemApplication.class})
public class DBTest {


    @Test
    public void testDbArray(){

        // stringRedisTemplate.opsForValue().get("123");

    }
}

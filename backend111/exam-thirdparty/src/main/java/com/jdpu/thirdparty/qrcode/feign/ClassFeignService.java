package com.jdpu.thirdparty.qrcode.feign;


import com.jdpu.common.entity.vo.RestResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 班级服务远程调用
 */
@FeignClient(name = "exam-examsystem",path = "/api/examsystem/class")
public interface ClassFeignService {
    @ApiOperation("学生绑定班级")
    @PostMapping("/bind")
    public RestResponse bindStuClass(@RequestBody String jsonStr);
}

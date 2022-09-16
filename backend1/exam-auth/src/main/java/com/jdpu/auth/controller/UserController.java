package com.jdpu.auth.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.jdpu.auth.service.IUserService;
import com.jdpu.auth.service.UsertokenService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jdpu.common.entity.vo.RestResponse;
import com.jdpu.common.param.StuQueryParam;
import com.jdpu.common.param.TeaQueryParam;
import com.google.code.kaptcha.Producer;
import com.jdpu.common.param.user.UserCreateVM;
import com.jdpu.common.param.user.UserIdsVM;
import com.jdpu.common.param.user.UserPageRequestVM;
import com.jdpu.common.xzsOld.entities.enums.UserStatusEnum;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.swagger.annotations.ApiOperation;
import com.jdpu.common.oauth2.exception.AuthErrorType;
import com.jdpu.common.dto.UserDTO;
import com.jdpu.common.entity.UserEntity;
import com.jdpu.common.param.UserQueryParam;
import com.jdpu.common.regex.RegexUtil;
import com.jdpu.common.result.ResponseDTO;
import com.jdpu.auth.client.OAuthRemoteService;
import com.jdpu.common.service.JwtService;
import com.jdpu.auth.entiy.LoginEntiy;
import com.jdpu.auth.entiy.UserManager;
import com.jdpu.auth.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Map;


/**
 * @author hhf
 * @email hhf@qq.com
 * @date 2022-03-04 15:53:04
 */
@RestController
@RequestMapping("/user")
@Slf4j
@CrossOrigin
public class UserController {
    @Autowired
    private IUserService userService;
    @Autowired
    private UsertokenService usertokenService;

    @Qualifier("com.jdpu.auth.client.OAuthRemoteService")
    @Autowired
    private OAuthRemoteService oAuthRemoteService;

    // @Value 配置文件读取值 或者常量注入
    @Value("${spring.oauth.client_id}")
    private String clientId;
    @Value("${spring.oauth.client_secret}")
    private String clientSecret;
    @Value("${spring.oauth.scope}")
    private String scope;
    @Value("${spring.oauth.grant_type}")
    private String grantType;
    @Value("${spring.oauth.refresh_grant_type}")
    private String refreshGrantType;

    @Autowired
    private Producer producer;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    private JwtService jwtService;

    @PostAuthorize("hasAuthority('jojo')")
    @GetMapping("/res1")
    public RestResponse test1() {

        return RestResponse.ok("资源1");
    }

    @GetMapping("/res2")
    public RestResponse test2() {
        return RestResponse.ok("资源2");
    }

    @PostMapping("/login")
    public RestResponse login(@RequestBody LoginEntiy loginEntiy) {
        String phone = loginEntiy.getUserName();
        String password = loginEntiy.getPassword();

        log.info("phone:{}", phone);
        log.info("password{}:", password);
        if (StringUtils.isEmpty(phone) || !RegexUtil.isPhone(phone)) {
            return RestResponse.fail("非法的手机号");
        }
        if (StringUtils.isEmpty(password)) {
            return RestResponse.fail("密码或者验证码为空");
        }
        //默认账号密码登陆
        Integer type = 0;

        //TODO 判断验证码 先不判断 开发方便
        // String code = loginEntiy.getCode();
        // String capthchaKey = loginEntiy.getCapthchaKey();
        // log.info("code:{}",code);
        // log.info("capthchaKey:{}",capthchaKey);
        // if (org.apache.commons.lang3.StringUtils.isBlank(code) || org.apache.commons.lang3.StringUtils.isBlank(capthchaKey))
        // {
        //     return Result.fail("验证码错误");
        // }
        //
        // // 验证码在redis
        // if (!code.equals(redisUtil.hget("captcha",capthchaKey)))
        // {
        //     System.out.println("---------");
        //     System.out.println(code+"code");
        //     System.out.println("key"+redisUtil.hget("captcha",capthchaKey));
        //     return Result.fail("验证码错误");
        //
        // }
        //
        // //一次性使用
        // redisUtil.hdel("captcha",capthchaKey);

        //这里判断如果密码是空验证码不为空，则设置type为1
//        if (StringUtils.isEmpty(password) && StringUtils.isEmpty(code)){
//            type=1;
//
//        }

        //判断手机号是否有注册
        boolean isRegister = this.userService.isRegister(phone);

        if (!Boolean.TRUE.equals(isRegister)) {
            // 不允许直接手机号+密码注册并登录
            if (type == 0) {
                log.info("该手机号[{}]未注册,不允许手机号+密码注册", phone);
                return RestResponse.fail("手机号或者密码错误");
            }
            UserEntity newUser = new UserEntity();

            newUser.setRegIp(UserManager.getUserIP());
            newUser.setName(phone);
            newUser.setPassword(type == 0 ? password : phone);
            newUser.setPhone(phone);
            newUser.setPortrait(null);

            this.userService.saveUser(newUser);

            log.info("用户注册成功");
        }

        // UserDTO userById = this.userService.getUserById(100030011);
        // return this.usertokenService.createAuthToken(phone, password, code, type);

        return this.usertokenService.createAuthToken(phone, password, "123", type);
    }


    @ApiOperation(value = "刷新token", produces = MimeTypeUtils.APPLICATION_JSON_VALUE, notes = "根据refresh_token重新获取access_token")
    @PostMapping("refresh_token")
    public RestResponse refreshToken(String refreshtoken) {

        if (StringUtils.isEmpty(refreshtoken)) {
            // return ResponseDTO.ofError(201, "refresh_token为空");
            return RestResponse.fail(AuthErrorType.INVALID_TOKEN, "refresh_token为空");
        }

        String refreshResult = this.oAuthRemoteService.refreshToken(refreshtoken, refreshGrantType, clientId, clientSecret);


        JSONObject resultObject = JSON.parseObject(refreshResult);

        String code = resultObject.getString("code");


        if (code != null && AuthErrorType.INVALID_TOKEN.getCode().equals(code)) {

            String data = resultObject.getString("data");
            JSONObject dataObject = JSON.parseObject(data);
            return RestResponse.fail(AuthErrorType.INVALID_TOKEN, dataObject);

        } else {

            return RestResponse.ok(resultObject);
        }
    }

    /**
     * 获取验证码
     *
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @GetMapping("/captcha")
    public ResponseDTO<Map> captcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("进入验证码");
        String code = producer.createText();
        String key = UUID.randomUUID().toString();
        BufferedImage image = producer.createImage(code);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", outputStream);
        BASE64Encoder encoder = new BASE64Encoder();
        String str = "data:image/jpeg;base64,";
        String base64Img = str + encoder.encode(outputStream.toByteArray());


        boolean hset = redisUtil.hset("captcha", key, code, 120);
        System.out.println(hset);
        System.out.println(key);
        System.out.println("验证码是：" + code);
        System.out.println("hset" + redisUtil.hget("captcha", key));

        return ResponseDTO.success(MapUtil.builder().put("CapthchaKey", key)
                .put("captchaImg", base64Img).build());
    }


    /**
     * 后台查询用列表的接口(可根据角色查询)
     */
    @PostMapping("/getUserPages")
    public Page<UserDTO> getQueryCourses(@RequestBody UserQueryParam userQueryParam) {
        return userService.getUserPages(userQueryParam);
    }

    //根据手机号获取信息
    @GetMapping("/getUserByPhone")
    public RestResponse getUserByPhone(@RequestParam("phone") String phone) {

        UserDTO user = this.userService.getUserByPhone(phone);

        return RestResponse.ok(user);
    }

    /**
     * 当前用户信息
     */
    @RequestMapping(value = "/current", method = RequestMethod.POST)
    public RestResponse current(HttpServletRequest request) {

        String authorization = request.getHeader("Authorization");

        UserDTO user = null;

        if (StrUtil.isNotBlank(authorization)) {
            //1.获取jwt
            Jws<Claims> jwt = jwtService.getJwt(authorization);

            if (jwt != null && jwt.getBody() != null) {
                String idStr = (String) jwt.getBody().get("user_id");
                Integer id = Integer.valueOf(idStr);

                user = userService.getUserById(id);

                return RestResponse.ok(user);
            }
        }

        return RestResponse.fail();
    }

    /**
     * 用户信息更新
     */
    @PostMapping("/edit")
    public RestResponse edit(@RequestBody UserCreateVM model) {
        if (model.getId() == null) {  // 创建

            if (this.userService.isRegister(model.getPhone())) {
                return RestResponse.fail().setMessage("手机号已存在");
            }
            if (StrUtil.isBlank(model.getPassword())) {
                log.info("喵喵：{}", model.getPassword());
                return RestResponse.fail().setMessage("密码不能为空");
            }
        }

        UserEntity user = BeanUtil.toBean(model, UserEntity.class);

        if (model.getId() == null) {
            userService.saveUser(user);
        } else {
            userService.updateUser(user);
        }
        return RestResponse.ok("更新成功");
    }

    @PostMapping("/delete/{id}")
    public RestResponse delete(@PathVariable Integer id) {
        boolean sussess = userService.deleteUser(id);

        if (sussess) {
            return RestResponse.ok().setMessage("删除成功");
        } else {
            return RestResponse.fail().setMessage("删除失败");
        }
    }

    /**
     * 用户状态修改 禁用用户
     */
    @PostMapping("/changeStatus/{id}")
    public RestResponse changeStatus(@PathVariable Integer id) {
        // 获取完整user
        UserEntity user = userService.getById(id);

        UserStatusEnum userStatusEnum = UserStatusEnum.fromCode(user.getStatus());

        Integer newStatus = userStatusEnum == UserStatusEnum.Enable ? UserStatusEnum.Disable.getCode() : UserStatusEnum.Enable.getCode();

        user.setStatus(newStatus);

        boolean sussess = userService.updateUser(user);

        if (sussess) {
            return RestResponse.ok(newStatus);
        } else {
            return RestResponse.fail();
        }
    }

        /**
     * 用户分页
     */
    @PostMapping("/page/list")
    public RestResponse<Page<UserEntity>> pageList(@RequestBody UserPageRequestVM model) {


        Page<UserEntity> userPage = userService.userPage(model);

        return RestResponse.ok(userPage);
    }

    @PostMapping("/select/{id}")
    public RestResponse select(@PathVariable Integer id) {
        // User user = userService.getUserById(id);
        // UserResponseVM userVm = UserResponseVM.from(user);
        // return RestResponse.ok(userVm);

        UserDTO user = userService.getUserById(id);
        return RestResponse.ok(user);
    }


    // 获取教师
    @PostMapping("/page/teacher")
    public RestResponse teacherPage(@RequestBody TeaQueryParam userQueryParam) {
        Page<UserEntity> page = userService.teacherPage(userQueryParam);
        return RestResponse.ok(page);
    }

    // 获取学生
    @PostMapping("/page/student")
    public RestResponse studentPage(@RequestBody StuQueryParam stuQueryParam) {
        Page<UserEntity> page = userService.studentPage(stuQueryParam);

        return RestResponse.ok(page);
    }

    @GetMapping("/get/{userId}")
    public RestResponse getUserById(@PathVariable("userId") Integer userId) {

        UserEntity user = userService.getById(userId);

        if(user == null) {
            return RestResponse.fail();
        }
        return RestResponse.ok(user);
    }

    @GetMapping("/get")
    public RestResponse getUserByIds(@RequestBody UserIdsVM model) {
        List<UserEntity> userList = userService.getUserByIds(model.getIds());

        if(userList.isEmpty()) {
            return RestResponse.fail();
        }

        return RestResponse.ok(userList);
    }

    @GetMapping("/getTeacherList")
    public RestResponse getTeacherList() {
        // 所有老师
        List<UserEntity> userList = userService.getTeacherList();

        if(userList.isEmpty()) {
            return RestResponse.fail();
        }

        return RestResponse.ok(userList);
    }
}

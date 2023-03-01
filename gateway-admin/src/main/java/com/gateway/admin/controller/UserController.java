package com.gateway.admin.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.voidtime.mall.common.response.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("user")
public class UserController {

    @PostMapping("login")
    public Result<Object> login(@RequestBody Map<String, String> map) {
        HashMap<String, String> map1 = new HashMap<>();
        map1.put("token", "1111");
        return Result.success(map1);
    }

    @PostMapping("info")
    public Result<Object> info() {
        JSONObject jsonObject = JSON.parseObject(userInfo);
        return Result.success(jsonObject);
    }

    static String userInfo = "{\n" +
            "  \"name\": \"王立群\",\n" +
            "  \"avatar\": \"//lf1-xgcdn-tos.pstatp.com/obj/vcloud/vadmin/start.8e0e4855ee346a46ccff8ff3e24db27b.png\",\n" +
            "  \"email\": \"wangliqun@email.com\",\n" +
            "  \"job\": \"frontend\",\n" +
            "  \"jobName\": \"前端艺术家\",\n" +
            "  \"organization\": \"Frontend\",\n" +
            "  \"organizationName\": \"前端\",\n" +
            "  \"location\": \"beijing\",\n" +
            "  \"locationName\": \"北京\",\n" +
            "  \"introduction\": \"人潇洒，性温存\",\n" +
            "  \"personalWebsite\": \"https://www.arco.design\",\n" +
            "  \"phone\": \"150****0000\",\n" +
            "  \"registrationDate\": \"2013-05-10 12:10:00\",\n" +
            "  \"accountId\": \"15012312300\",\n" +
            "  \"certification\": 1,\n" +
            "  \"role\": \"admin\"\n" +
            "}";

}

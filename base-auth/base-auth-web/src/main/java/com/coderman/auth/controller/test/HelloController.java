package com.coderman.auth.controller.test;

import com.coderman.api.util.ResultUtil;
import com.coderman.api.vo.ResultVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/${domain}")
public class HelloController {

    @Value("${msg:hello}")
    private String msg;

    @GetMapping(value = "/get/config")
    public ResultVO<String> getConfig() {
        return ResultUtil.getSuccess(String.class, msg);
    }
}

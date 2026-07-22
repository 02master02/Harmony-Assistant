package com.example.censor.controller;

import com.example.censor.service.CensorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 单一控制器：首页展示与和谐提交。
 */
@Controller
public class CensorController {

    private final CensorService censorService;

    public CensorController(CensorService censorService) {
        this.censorService = censorService;
    }

    /** 首页 */
    @GetMapping("/")
    public String index() {
        return "index";
    }

    /** 提交文本并返回和谐结果 */
    @PostMapping("/censor")
    public String censor(@RequestParam(value = "text", required = false, defaultValue = "") String text,
                         Model model) {
        String result = censorService.censor(text);
        model.addAttribute("original", text);
        model.addAttribute("result", result);
        return "index";
    }
}

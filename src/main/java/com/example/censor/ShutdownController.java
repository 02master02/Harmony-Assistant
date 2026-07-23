package com.example.censor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sys")
public class ShutdownController {

    private static ApplicationContext context;

    @Autowired
    public void setContext(ApplicationContext ctx) {
        context = ctx;
    }

    @PostMapping("/shutdown")
    public String shutdown() {
        new Thread(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException ignored) {}
            SpringApplication.exit(context, () -> 0);
            System.exit(0);
        }).start();
        return "ok";
    }
}
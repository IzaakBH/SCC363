package com.scc363.hospitalproject;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RequestErrorHandler implements ErrorController {


    @RequestMapping("/error")
    public String redirect()
    {
        System.out.println("error occurred so redirecting");
        return "signin";
    }

    @Override
    public String getErrorPath() {
        return null;
    }
}

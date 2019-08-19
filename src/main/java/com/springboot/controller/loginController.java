package com.springboot.controller;

import com.springboot.component.RealEstateMortgageComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class loginController {

    @Autowired
    private RealEstateMortgageComponent realEstateMortgageComponent;

    @RequestMapping("/login")
    public String login() {
        return "login";
    }


}

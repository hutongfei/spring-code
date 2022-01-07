package com.fresh.controller;

import com.fresh.temp.IndexOfMethod;
import com.fresh.test.TUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;


import java.math.BigDecimal;
import java.util.Date;

@RequestMapping("/page")
@Controller
public class PageController {

    @GetMapping("/index")
    public String index(@ModelAttribute(name = "id") String id , ModelMap modelMap){
        TUser tUser = new TUser();
        tUser.setCreateDate(new Date());
        tUser.setId(10000l);
        tUser.setFlag(true);
        tUser.setUserName("Jonne");
        tUser.setPrice(BigDecimal.valueOf(1000.00));
        tUser.setState(1);

        modelMap.put("indexMethod", new IndexOfMethod());

        modelMap.put("user", tUser);

        if (StringUtils.equals(id,"1")) {
            modelMap.put("flag", 1);
            modelMap.put("flag1", false);
        }

        modelMap.put("b1", false);
        modelMap.put("b2", false);
        modelMap.put("b3", true);


        return "index";
    }

}

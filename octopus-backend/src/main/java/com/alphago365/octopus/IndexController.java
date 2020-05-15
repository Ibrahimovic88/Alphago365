package com.alphago365.octopus;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@RestController
public class IndexController {

    @GetMapping(value = {"/", "/api/v2"})
    public ModelAndView index(HttpServletRequest req, ModelMap model) {
        return new ModelAndView("redirect:/swagger-ui.html", model);
    }
}
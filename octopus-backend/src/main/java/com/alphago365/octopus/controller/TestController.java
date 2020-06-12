package com.alphago365.octopus.controller;


import com.alphago365.octopus.service.RestService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@Api(tags = "test")
public class TestController {

    @Autowired
    private RestService restService;

    @ApiOperation(value = "test url", tags = {"test"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get response", response = String.class)
    })
    @GetMapping
    public String getResponse(@RequestParam(name = "url") String strUrl) {
        return restService.getJson(strUrl);
    }
}

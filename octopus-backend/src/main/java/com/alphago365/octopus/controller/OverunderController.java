package com.alphago365.octopus.controller;


import com.alphago365.octopus.model.Overunder;
import com.alphago365.octopus.service.OverunderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(tags = "overunder")
public class OverunderController {

    @Autowired
    private OverunderService overunderService;

    @ApiOperation(value = "get overunder list by match id", tags = {"overunder"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get overunder list", response = List.class)
    })
    @GetMapping(value = "/matches/{match-id}/overunder")
    public List<Overunder> getListByMatchId(@PathVariable(name = "match-id") long matchId) {
        return overunderService.findByMatchId(matchId);
    }

    @ApiOperation(value = "get overunder by match id and provider id", tags = {"overunder"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get overunder", response = Overunder.class)
    })
    @GetMapping(value = "/matches/{match-id}/overunder/{provider-id}")
    public Overunder getByMatchIdAndProviderId(@PathVariable(name = "match-id") long matchId,
                                               @PathVariable(name = "provider-id") int providerId) {
        return overunderService.findByMatchIdAndProviderId(matchId, providerId);
    }
}

package com.alphago365.octopus.controller;


import com.alphago365.octopus.model.Handicap;
import com.alphago365.octopus.service.HandicapService;
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
@Api(tags = "handicap")
public class HandicapController {

    @Autowired
    private HandicapService handicapService;

    @ApiOperation(value = "get handicap list by match id", tags = {"handicap"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get handicap list", response = List.class)
    })
    @GetMapping(value = "/matches/{match-id}/handicap")
    public List<Handicap> getListByMatchId(@PathVariable(name = "match-id") long matchId) {
        return handicapService.findByMatchId(matchId);
    }

    @ApiOperation(value = "get handicap by match id and provider id", tags = {"handicap"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get handicap", response = Handicap.class)
    })
    @GetMapping(value = "/matches/{match-id}/handicap/{provider-id}")
    public Handicap getByMatchIdAndProviderId(@PathVariable(name = "match-id") long matchId,
                                              @PathVariable(name = "provider-id") int providerId) {
        return handicapService.findByMatchIdAndProviderId(matchId, providerId);
    }
}

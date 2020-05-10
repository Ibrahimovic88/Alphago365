package com.alphago365.octopus.controller;


import com.alphago365.octopus.model.Handicap;
import com.alphago365.octopus.model.Match;
import com.alphago365.octopus.model.Provider;
import com.alphago365.octopus.service.HandicapService;
import com.alphago365.octopus.service.MatchService;
import com.alphago365.octopus.service.ProviderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Api(tags = "handicap")
public class HandicapController {

    @Autowired
    private MatchService matchService;

    @Autowired
    private ProviderService providerService;

    @Autowired
    private HandicapService handicapService;

    @ApiOperation(value = "get handicap list by match id", tags = {"handicap"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get handicap list", response = List.class)
    })
    @GetMapping(value = "/matches/{match-id}/handicap")
    public List<Handicap> getListByMatchId(@PathVariable(name = "match-id") long matchId) {
        Match match = matchService.findById(matchId);
        return handicapService.findByMatch(match)
                .parallelStream()
                .sorted(Comparator.comparing(Handicap::getDisplayOrder))
                .collect(Collectors.toList());
    }

    @ApiOperation(value = "get handicap by match id and provider id", tags = {"handicap"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get odds", response = Handicap.class)
    })
    @GetMapping(value = "/matches/{match-id}/handicap/{provider-id}")
    public Handicap getByMatchIdAndProviderId(@PathVariable(name = "match-id") long matchId,
                                                  @PathVariable(name = "provider-id") int providerId) {
        Match match = matchService.findById(matchId);
        Provider provider = providerService.findById(providerId);
        return handicapService.findByMatchAndProvider(match, provider);
    }
}

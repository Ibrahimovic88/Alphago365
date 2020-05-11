package com.alphago365.octopus.controller;


import com.alphago365.octopus.model.Match;
import com.alphago365.octopus.model.Odds;
import com.alphago365.octopus.model.Provider;
import com.alphago365.octopus.service.MatchService;
import com.alphago365.octopus.service.OddsService;
import com.alphago365.octopus.service.ProviderService;
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
@Api(tags = "odds")
public class OddsController {

    @Autowired
    private MatchService matchService;

    @Autowired
    private ProviderService providerService;

    @Autowired
    private OddsService oddsService;

    @ApiOperation(value = "get odds list by match id", tags = {"odds"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get odds list", response = List.class)
    })
    @GetMapping(value = "/matches/{match-id}/odds")
    public List<Odds> getOddsListByMatchId(@PathVariable(name = "match-id") long matchId) {
        Match match = matchService.findById(matchId);
        return oddsService.findByMatch(match);
    }

    @ApiOperation(value = "get odds by match id and provider id", tags = {"odds"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get odds", response = Odds.class)
    })
    @GetMapping(value = "/matches/{match-id}/odds/{provider-id}")
    public Odds getByMatchIdAndProviderId(@PathVariable(name = "match-id") long matchId,
                                          @PathVariable(name = "provider-id") int providerId) {
        Match match = matchService.findById(matchId);
        Provider provider = providerService.findById(providerId);
        return oddsService.findByMatchAndProvider(match, provider);
    }
}

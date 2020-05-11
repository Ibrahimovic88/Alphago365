package com.alphago365.octopus.controller;


import com.alphago365.octopus.model.Match;
import com.alphago365.octopus.model.Overunder;
import com.alphago365.octopus.model.Provider;
import com.alphago365.octopus.service.MatchService;
import com.alphago365.octopus.service.OverunderService;
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
@Api(tags = "overunder")
public class OverunderController {

    @Autowired
    private MatchService matchService;

    @Autowired
    private ProviderService providerService;

    @Autowired
    private OverunderService overunderService;

    @ApiOperation(value = "get overunder list by match id", tags = {"overunder"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get overunder list", response = List.class)
    })
    @GetMapping(value = "/matches/{match-id}/overunder")
    public List<Overunder> getListByMatchId(@PathVariable(name = "match-id") long matchId) {
        Match match = matchService.findById(matchId);
        return overunderService.findByMatch(match)
                .parallelStream()
                .sorted(Comparator.comparing(Overunder::getDisplayOrder))
                .collect(Collectors.toList());
    }

    @ApiOperation(value = "get overunder by match id and provider id", tags = {"overunder"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get odds", response = Overunder.class)
    })
    @GetMapping(value = "/matches/{match-id}/overunder/{provider-id}")
    public Overunder getByMatchIdAndProviderId(@PathVariable(name = "match-id") long matchId,
                                               @PathVariable(name = "provider-id") int providerId) {
        Match match = matchService.findById(matchId);
        Provider provider = providerService.findById(providerId);
        return overunderService.findByMatchAndProvider(match, provider);
    }
}

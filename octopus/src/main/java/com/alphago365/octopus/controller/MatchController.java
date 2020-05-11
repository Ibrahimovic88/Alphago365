package com.alphago365.octopus.controller;


import com.alphago365.octopus.model.Match;
import com.alphago365.octopus.payload.AppResponse;
import com.alphago365.octopus.service.MatchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/matches")
@Api(tags = "match")
public class MatchController {

    @Autowired
    private MatchService matchService;

    @ApiOperation(value = "get match list of specified latest days", tags = {"match"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get match list", response = List.class)
    })
    @GetMapping
    public List<Match> getMatchListOfLatestDays(@RequestParam(value = "latest-days", defaultValue = "1") int latestDays) {
        return matchService.getMatchListOfLatestDays(latestDays)
                .parallelStream()
                .sorted(Comparator.comparing(Match::getKickoffTime).reversed())
                .collect(Collectors.toList());
    }

    @ApiOperation(value = "get match by id", tags = {"match"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get match", response = Match.class)
    })
    @GetMapping(value = "/{id}")
    public Match getMatch(@PathVariable(value = "id") long id) {
        return matchService.findById(id);
    }

    @ApiOperation(value = "match created", tags = {"match"})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully create match", response = AppResponse.class),
            @ApiResponse(code = 403, message = "Only admin can create match")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> add(@Valid @RequestBody Match match) {
        matchService.save(match);
        return ResponseEntity.ok(new AppResponse(true, "Successfully create match"));
    }
}

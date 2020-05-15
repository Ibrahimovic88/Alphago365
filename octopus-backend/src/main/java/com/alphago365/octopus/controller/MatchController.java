package com.alphago365.octopus.controller;


import com.alphago365.octopus.config.DownloadConfig;
import com.alphago365.octopus.model.Match;
import com.alphago365.octopus.payload.AppResponse;
import com.alphago365.octopus.service.MatchService;
import com.alphago365.octopus.util.DateUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/matches")
@Api(tags = "match")
public class MatchController {

    @Autowired
    private MatchService matchService;

    @Autowired
    private DownloadConfig downloadConfig;

    @ApiOperation(value = "get match list of latest days before and after specified date", tags = {"match"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get match list", response = List.class)
    })
    @GetMapping(value = "/specified-date")
    public List<Match> getMatchListOfLatestDaysWithSpecifiedDate(@RequestParam(name = "date", required = false) String strDate
            , @RequestParam(name = "latest-days", defaultValue = "1") int latestDays) {
        if(StringUtils.isEmpty(strDate)) {
            strDate = downloadConfig.getMatchDate();
        }
        Instant date = DateUtils.parseToInstant(strDate, "yyyy-MM-dd");
        return matchService.findMatchesOfLatestDaysWithSpecifiedDate(date, latestDays);
    }

    @ApiOperation(value = "get match list of latest days before and after current date", tags = {"match"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get match list", response = List.class)
    })
    @GetMapping(value = "/current-date")
    public List<Match> getMatchListOfLatestDaysWithCurrentDate(@RequestParam(name = "latest-days", defaultValue = "1") int latestDays) {
        Instant date = DateUtils.asInstant(LocalDate.now());
        return matchService.findMatchesOfLatestDaysWithSpecifiedDate(date, latestDays);
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

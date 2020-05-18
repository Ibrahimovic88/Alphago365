package com.alphago365.octopus.parser;

import com.alphago365.octopus.converter.WdlConverter;
import com.alphago365.octopus.exception.JsonParseException;
import com.alphago365.octopus.exception.ParseException;
import com.alphago365.octopus.model.Match;
import com.alphago365.octopus.util.DateUtils;
import com.github.openjson.JSONArray;
import com.github.openjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class MatchParser extends ListParser<Match> {

    @Autowired
    private WdlConverter wdlConverter;

    @Override
    public List<Match> parseList(@NotNull String json) throws ParseException {
        if (json.isEmpty()) {
            return Collections.emptyList();
        }
        JSONObject root = new JSONObject(json);
        int code = root.getInt("code");
        if (code != 0) {
            String errMsg = root.getString("code_str");
            throw new JsonParseException("Parse match list with error: " + errMsg);
        }
        List<Match> matchList = new ArrayList<>();
        JSONArray jsonArray = root.getJSONObject("info").getJSONArray("Matches");
        jsonArray.forEach(item -> {
            Match match = parseMatchMatchItem((JSONObject) item);
            matchList.add(match);
        });

        return matchList;
    }

    private Match parseMatchMatchItem(JSONObject item) {

        Match match = new Match();
        match.setId(item.getLong("MatchID"));
        Instant date = DateUtils.parseToInstant(item.getString("BetDate"), "yyyy-MM-dd");
        match.setDate(date);
        match.setSerialNumber(item.getInt("MatchOrder"));
        match.setLeague(item.getString("LeagueName"));
        match.setHome(item.getString("HomeName"));
        match.setAway(item.getString("AwayName"));
        LocalDateTime localDateTime = DateUtils.parseToDateTime(item.getString("MatchTime"), "yyyy-MM-dd HH:mm:ss");
        match.setKickoffTime(DateUtils.asInstant(localDateTime));

        match.setHalfHomeGoals(item.getInt("HalfHomeGoals"));
        match.setHalfAwayGoals(item.getInt("HalfAwayGoals"));

        match.setFinalHomeGoals(item.getInt("HomeGoals"));
        match.setFinalAwayGoals(item.getInt("AwayGoals"));

        int wdlResult;
        if (match.getFinalHomeGoals() == -1
                || match.getFinalAwayGoals() == -1) {
            wdlResult = -1;
        } else {
            wdlResult = (match.getFinalHomeGoals() > match.getFinalAwayGoals() ? 3
                    : (match.getFinalHomeGoals().equals(match.getFinalAwayGoals()) ? 1 : 0)
            );
        }
        match.setActualWdl(wdlConverter.convertToEntityAttribute(wdlResult));

        return match;
    }
}

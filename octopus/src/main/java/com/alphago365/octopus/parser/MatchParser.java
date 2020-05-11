package com.alphago365.octopus.parser;

import com.alphago365.octopus.exception.JsonParseException;
import com.alphago365.octopus.exception.ParseException;
import com.alphago365.octopus.model.Match;
import com.alphago365.octopus.util.DateUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

@Component
public class MatchParser extends ListParser<Match> {

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
        match.setDate(DateUtils.parse(item.getString("BetDate"), "yyyy-MM-dd"));
        match.setSerialNumber(item.getInt("MatchOrder"));
        match.setLeague(item.getString("LeagueName"));
        match.setHome(item.getString("HomeName"));
        match.setAway(item.getString("AwayName"));
        LocalDateTime localDateTime = DateUtils.parse(item.getString("MatchTime"), "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        match.setKickoffTime(DateUtils.asInstant(localDateTime));

        return match;
    }
}

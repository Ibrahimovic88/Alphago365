package com.alphago365.octopus.parser;

import com.alphago365.octopus.exception.ParseException;
import com.alphago365.octopus.model.CompositeChangeId;
import com.alphago365.octopus.model.Handicap;
import com.alphago365.octopus.model.HandicapChange;
import com.alphago365.octopus.util.DateUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

@Component
@Scope("prototype")
public class HandicapChangeParser extends MatchRelatedParser<HandicapChange> {

    private final Handicap handicap;

    @Autowired
    public HandicapChangeParser(@NotNull Handicap handicap) {
        super(handicap.getMatch());
        this.handicap = handicap;
    }

    @Override
    public List<HandicapChange> parseList(@NotNull String json) throws ParseException {
        JSONObject root = new JSONObject(json);
        int code = root.getInt("code");
        if (code != 0) {
            String reason = root.getString("code_str");
            throw new ParseException(reason);
        }

        // tree map ensure ordered
        Map<CompositeChangeId, HandicapChange> map = new TreeMap<>(Comparator.comparing(CompositeChangeId::getUpdateTime));

        JSONObject info = root.getJSONObject("info");
        LocalDateTime kickoffTime = DateUtils.parse(info.getString("MatchTime"), "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        info.getJSONArray("changeOdds").forEach(item -> {
            HandicapChange change = parseChangeOddsItem((JSONObject) item);
            change.setKickoffTime(DateUtils.asInstant(kickoffTime));
            map.put(change.getCompositeChangeId(), change);
        });

        return new ArrayList<>(map.values());
    }

    private HandicapChange parseChangeOddsItem(JSONObject jsonObject) {
        HandicapChange change = new HandicapChange(handicap);

        JSONObject odds = jsonObject.getJSONObject("odds");
        change.setHome(odds.getDouble("home"));
        change.setAway(odds.getDouble("away"));
        change.setBoundary(jsonObject.getDouble("boundary"));
        Long matchId = handicap.getMatch().getId();
        int providerId = handicap.getProvider().getId();
        LocalDateTime localDateTime = DateUtils.parse(jsonObject.getString("update_time"), "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Instant updateTime = DateUtils.asInstant(localDateTime);
        change.setCompositeChangeId(new CompositeChangeId(matchId, providerId, updateTime));

        return change;
    }
}
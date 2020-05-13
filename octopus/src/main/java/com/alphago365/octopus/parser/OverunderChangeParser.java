package com.alphago365.octopus.parser;

import com.alphago365.octopus.exception.ParseException;
import com.alphago365.octopus.model.CompositeChangeId;
import com.alphago365.octopus.model.Overunder;
import com.alphago365.octopus.model.OverunderChange;
import com.alphago365.octopus.util.DateUtils;
import com.github.openjson.JSONObject;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

@Component
@Scope("prototype")
public class OverunderChangeParser extends MatchRelatedParser<OverunderChange> {

    private final Overunder overunder;

    public OverunderChangeParser(@NotNull Overunder overunder) {
        super(overunder.getMatch());
        this.overunder = overunder;
    }

    @Override
    public List<OverunderChange> parseList(@NotNull String json) throws ParseException {
        JSONObject root = new JSONObject(json);
        int code = root.getInt("code");
        if (code != 0) {
            String reason = root.getString("code_str");
            throw new ParseException(reason);
        }
        Map<CompositeChangeId, OverunderChange> map = new TreeMap<>(Comparator.comparing(CompositeChangeId::getUpdateTime));

        JSONObject info = root.getJSONObject("info");
        LocalDateTime localDateTime = DateUtils.parseToDateTime(info.getString("MatchTime"), "yyyy-MM-dd HH:mm:ss");
        Instant kickoffTime = DateUtils.asInstant(localDateTime);
        info.getJSONArray("changeOdds").forEach(item -> {
            OverunderChange change = parseChangeOddsItem((JSONObject) item);
            change.setKickoffTime(kickoffTime);
            map.put(change.getCompositeChangeId(), change);
        });

        return new ArrayList<>(map.values());
    }

    private OverunderChange parseChangeOddsItem(JSONObject jsonObject) {
        OverunderChange change = new OverunderChange(overunder);

        JSONObject odds = jsonObject.getJSONObject("odds");
        change.setOver(odds.getDouble("over"));
        change.setUnder(odds.getDouble("under"));
        change.setBoundary(jsonObject.getDouble("boundary"));
        LocalDateTime localDateTime = DateUtils.parseToDateTime(jsonObject.getString("update_time"), "yyyy-MM-dd HH:mm:ss");
        Instant updateTime = DateUtils.asInstant(localDateTime);
        change.setCompositeChangeId(new CompositeChangeId(overunder.getMatch().getId(), overunder.getProvider().getId(), updateTime));

        return change;
    }
}
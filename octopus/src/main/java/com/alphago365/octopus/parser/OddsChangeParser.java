package com.alphago365.octopus.parser;

import com.alphago365.octopus.exception.ParseException;
import com.alphago365.octopus.model.CompositeChangeId;
import com.alphago365.octopus.model.Odds;
import com.alphago365.octopus.model.OddsChange;
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
public class OddsChangeParser extends MatchRelatedParser<OddsChange> {

    private final Odds odds;

    public OddsChangeParser(@NotNull Odds odds) {
        super(odds.getMatch());
        this.odds = odds;
    }

    @Override
    public List<OddsChange> parseList(@NotNull String json) throws ParseException {
        JSONObject root = new JSONObject(json);
        int code = root.getInt("code");
        if (code != 0) {
            String reason = root.getString("code_str");
            throw new ParseException(reason);
        }
        Map<CompositeChangeId, OddsChange> map = new TreeMap<>(Comparator.comparing(CompositeChangeId::getUpdateTime));

        JSONObject info = root.getJSONObject("info");
        LocalDateTime kickoffTime = DateUtils.parse(info.getString("MatchTime"), "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        info.getJSONArray("changeOdds").forEach(item -> {
            OddsChange change = parseChangeOddsItem((JSONObject) item);
            change.setKickoffTime(DateUtils.asInstant(kickoffTime));
            map.put(change.getCompositeChangeId(), change);
        });

        return new ArrayList<>(map.values());
    }

    private OddsChange parseChangeOddsItem(JSONObject jsonObject) {
        OddsChange change = new OddsChange(odds);

        JSONObject odds = jsonObject.getJSONObject("odds");
        change.setHome(odds.getDouble("home"));
        change.setDraw(odds.getDouble("draw"));
        change.setAway(odds.getDouble("away"));
        LocalDateTime localDateTime = DateUtils.parse(jsonObject.getString("update_time"), "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Instant updateTime = DateUtils.asInstant(localDateTime);
        change.setCompositeChangeId(new CompositeChangeId(this.odds.getMatch().getId(), this.odds.getProvider().getId(), updateTime));

        return change;
    }
}
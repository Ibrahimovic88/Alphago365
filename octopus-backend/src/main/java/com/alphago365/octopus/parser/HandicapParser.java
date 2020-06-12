package com.alphago365.octopus.parser;

import com.alphago365.octopus.exception.ParseException;
import com.alphago365.octopus.model.Handicap;
import com.alphago365.octopus.model.Match;
import com.alphago365.octopus.model.Provider;
import com.github.openjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Component
@Scope("prototype")
public class HandicapParser extends MatchRelatedParser<Handicap> {

    @Autowired
    public HandicapParser(@NotNull Match match) {
        super(match);
    }

    @Override
    public List<Handicap> parseList(@NotNull String json) throws ParseException {
        JSONObject root = new JSONObject(json);
        int code = root.getInt("code");
        if (code != 0) {
            String reason = root.getString("code_str");
            throw new ParseException(reason);
        }
        List<Handicap> handicapList = new ArrayList<>();

        root.getJSONArray("info").forEach(item -> {
            Handicap handicap = parseInfoItem((JSONObject) item);
            Provider provider = handicap.getProvider();
            if (!isIncluded(provider)) {
                return;
            }
            handicapList.add(handicap);
        });

        return handicapList;
    }

    private Handicap parseInfoItem(JSONObject jsonObject) {
        Handicap handicap = new Handicap(match);

        // display order
        handicap.setDisplayOrder(jsonObject.getInt("DisplayOrder"));

        // provider
        int providerId = jsonObject.getInt("provider_id");
        String providerName = jsonObject.getString("provider_name");
        Provider provider = new Provider(providerId, providerName);
        handicap.setProvider(provider);

        // id
        handicap.setId(Long.parseLong(String.format("%d%d", match.getId(), providerId))); // concat match id with provider id

        // Ratio
        JSONObject ratio = jsonObject.getJSONObject("Radio");
        handicap.setRatioHome(ratio.getDouble("home"));
        handicap.setRatioAway(ratio.getDouble("away"));

        // Kelly
        JSONObject kelly = jsonObject.getJSONObject("Kelly");
        handicap.setKellyHome(kelly.getDouble("home"));
        handicap.setKellyAway(kelly.getDouble("away"));

        // Payoff
        handicap.setPayoff(jsonObject.getDouble("Payoff"));

        return handicap;
    }

    private boolean isIncluded(@NotNull Provider provider) {
        return downloadConfig.getHandicapProviderIds().contains(provider.getId());
    }
}
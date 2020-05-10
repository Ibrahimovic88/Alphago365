package com.alphago365.octopus.parser;

import com.alphago365.octopus.exception.ParseException;
import com.alphago365.octopus.model.Match;
import com.alphago365.octopus.model.Odds;
import com.alphago365.octopus.model.Provider;
import org.json.JSONObject;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Component
@Scope("prototype")
public class OddsParser extends MatchRelatedParser<Odds> {

    public OddsParser(@NotNull Match match) {
        super(match);
    }

    @Override
    public List<Odds> parseList(@NotNull String json) throws ParseException {
        JSONObject root = new JSONObject(json);
        int code = root.getInt("code");
        if (code != 0) {
            String reason = root.getString("code_str");
            throw new ParseException(reason);
        }
        List<Odds> oddsList = new ArrayList<>();

        root.getJSONArray("info").forEach(item -> {
            Odds odds = parseInfoItem((JSONObject) item);
            Provider provider = odds.getProvider();
            if (!isIncluded(provider)) {
                return;
            }
            oddsList.add(odds);
        });

        return oddsList;
    }

    private Odds parseInfoItem(JSONObject jsonObject) {
        Odds odds = new Odds(match);

        // display order
        odds.setDisplayOrder(jsonObject.getInt("DisplayOrder"));

        // provider
        int providerId = jsonObject.getInt("provider_id");
        String providerName = jsonObject.getString("provider_name");
        Provider provider = new Provider(providerId, providerName);
        odds.setProvider(provider);

        // Ratio
        JSONObject ratio = jsonObject.getJSONObject("Radio");
        odds.setRatioHome(ratio.getDouble("home"));
        odds.setRatioDraw(ratio.getDouble("draw"));
        odds.setRatioAway(ratio.getDouble("away"));

        // Kelly
        JSONObject kelly = jsonObject.getJSONObject("Kelly");
        odds.setKellyHome(kelly.getDouble("home"));
        odds.setKellyDraw(kelly.getDouble("draw"));
        odds.setKellyAway(kelly.getDouble("away"));

        // Payoff
        odds.setPayoff(jsonObject.getDouble("Payoff"));

        return odds;
    }

    private boolean isIncluded(@NotNull Provider provider) {
        return downloadConfig.getOddsProviderIds().contains(provider.getId());
    }
}
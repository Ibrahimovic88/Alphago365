package com.alphago365.octopus.parser;

import com.alphago365.octopus.exception.ParseException;
import com.alphago365.octopus.model.Match;
import com.alphago365.octopus.model.Overunder;
import com.alphago365.octopus.model.Provider;
import com.github.openjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@Scope("prototype")
public class OverunderParser extends MatchRelatedParser<Overunder> {

    @Autowired
    public OverunderParser(@NotNull Match match) {
        super(match);
    }

    @Override
    public List<Overunder> parseList(@NotNull String json) throws ParseException {
        JSONObject root = new JSONObject(json);
        int code = root.getInt("code");
        if (code != 0) {
            String reason = root.getString("code_str");
            throw new ParseException(reason);
        }
        List<Overunder> overUnderList = new ArrayList<>();

        root.getJSONArray("info").forEach(item -> {
            Overunder Overunder = parseInfoItem((JSONObject) item);
            Provider provider = Overunder.getProvider();
            if (!isInclude(provider)) {
                return;
            }
            overUnderList.add(Overunder);
        });

        return overUnderList;
    }

    private Overunder parseInfoItem(JSONObject jsonObject) {
        Overunder overunder = new Overunder(match);

        // display order
        overunder.setDisplayOrder(jsonObject.getInt("DisplayOrder"));

        // provider
        int providerId = jsonObject.getInt("provider_id");
        String providerName = jsonObject.getString("provider_name");
        Provider provider = new Provider(providerId, providerName);
        overunder.setProvider(provider);

        // id
        // id
        overunder.setId(Long.parseLong(String.format("%d%d", match.getId(), providerId))); // concat match id with provider id

        // Ratio
        JSONObject ratio = jsonObject.getJSONObject("Radio");
        overunder.setRatioOver(ratio.getDouble("over"));
        overunder.setRatioUnder(ratio.getDouble("under"));

        // Kelly
        JSONObject kelly = jsonObject.getJSONObject("Kelly");
        overunder.setKellyOver(kelly.getDouble("over"));
        overunder.setKellyUnder(kelly.getDouble("under"));

        // Payoff
        overunder.setPayoff(jsonObject.getDouble("Payoff"));

        return overunder;
    }

    private boolean isInclude(@NotNull Provider provider) {
        return downloadConfig.getHandicapProviderIds().contains(provider.getId());
    }
}
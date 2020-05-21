package com.alphago365.octopus.mvp.repository.match;

import com.alphago365.octopus.deserializer.InstantDeserializer;
import com.alphago365.octopus.mvp.model.Match;
import com.alphago365.octopus.mvp.remote.BaseRemote;
import com.alphago365.octopus.mvp.remote.RemoteConfig;
import com.google.gson.GsonBuilder;

import java.time.Instant;
import java.util.List;

import io.reactivex.Observable;

public class MatchRemoteRepositoryImpl extends BaseRemote implements MatchRemoteRepository {
    @Override
    public Observable<List<Match>> getMatchList(final String date, final int latestDays) {
        return create(MatchRemoteService.class, RemoteConfig.BASE_URL).getMatchList(date, latestDays);
    }

    @Override
    protected GsonBuilder getGsonBuilder() {
        GsonBuilder gsonBuilder = super.getGsonBuilder();
        gsonBuilder.registerTypeAdapter(Instant.class, new InstantDeserializer());
        return gsonBuilder;
    }
}

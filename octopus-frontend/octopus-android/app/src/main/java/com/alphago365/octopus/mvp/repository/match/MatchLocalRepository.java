package com.alphago365.octopus.mvp.repository.match;

import com.alphago365.octopus.mvp.model.Match;

import java.util.List;

public interface MatchLocalRepository extends MatchRepository {

    void saveMatchList(List<Match> matchList);

}

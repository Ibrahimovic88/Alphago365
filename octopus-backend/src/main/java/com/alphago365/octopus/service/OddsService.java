package com.alphago365.octopus.service;

import com.alphago365.octopus.exception.ResourceNotFoundException;
import com.alphago365.octopus.model.Match;
import com.alphago365.octopus.model.Odds;
import com.alphago365.octopus.model.OddsChange;
import com.alphago365.octopus.model.Provider;
import com.alphago365.octopus.repository.OddsChangeRepository;
import com.alphago365.octopus.repository.OddsRepository;
import com.alphago365.octopus.repository.ProviderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OddsService {

    @Autowired
    private MatchService matchService;

    @Autowired
    private ProviderService providerService;

    @Autowired
    private OddsRepository oddsRepository;
    @Autowired
    private OddsChangeRepository oddsChangeRepository;

    public List<Odds> findByMatchId(Long matchId) {
        Match match = matchService.findById(matchId);
        List<Odds> oddsList = oddsRepository.findByMatch(match);
        oddsList.forEach(OddsService::sortChangeHistories);
        return oddsList
                .parallelStream()
                .sorted(Comparator.comparing(Odds::getDisplayOrder))
                .collect(Collectors.toList());
    }

    private static void sortChangeHistories(Odds odds) {
        odds.getChangeHistories().sort(Comparator.comparing(OddsChange::getUpdateTime).reversed());
    }

    public Odds findByMatchIdAndProviderId(Long matchId, int providerId) {
        Match match = matchService.findById(matchId);
        Provider provider = providerService.findById(providerId);
        Odds odds = oddsRepository.findByMatchAndProvider(match, provider)
                .<ResourceNotFoundException>orElseThrow(() -> {
                    throw new ResourceNotFoundException("Odds not found by match and provider");
                });
        sortChangeHistories(odds);
        return odds;
    }

    @Transactional
    public List<Odds> saveAll(@NotNull List<Odds> oddsList) {
        List<Odds> savedList = new ArrayList<>();
        oddsList.forEach(odds -> {
            Provider tempProvider = odds.getProvider();
            Provider savedProvider = saveProviderIfNotExists(tempProvider);
            odds.setProvider(savedProvider);
            savedList.add(oddsRepository.save(odds));
        });
        return savedList;
    }

    private Provider saveProviderIfNotExists(Provider tempProvider) {
        Integer id = tempProvider.getId();
        if (providerService.existsById(id)) {
            return providerService.findById(id);
        }
        return providerService.save(tempProvider);
    }

    @Transactional
    public List<OddsChange> saveAllChanges(@NotNull List<OddsChange> handicapChangeList) {
        return oddsChangeRepository.saveAll(handicapChangeList);
    }
}

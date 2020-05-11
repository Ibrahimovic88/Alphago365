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
    private OddsRepository oddsRepository;

    @Autowired
    private OddsChangeRepository oddsChangeRepository;

    @Autowired
    private ProviderRepository providerRepository;

    public List<Odds> findByMatch(Match match) {
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

    public Odds findByMatchAndProvider(Match match, Provider provider) {
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
            odds.setProvider(saveProviderIfNotExists(odds));
            savedList.add(oddsRepository.save(odds));
        });
        return savedList;
    }

    private Provider saveProviderIfNotExists(Odds odds) {
        Provider tempProvider = odds.getProvider();
        return providerRepository
                .findById(tempProvider.getId())
                .orElse(providerRepository.save(tempProvider));
    }

    @Transactional
    public List<OddsChange> saveAllChanges(@NotNull List<OddsChange> handicapChangeList) {
        return oddsChangeRepository.saveAll(handicapChangeList);
    }
}

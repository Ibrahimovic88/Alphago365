package com.alphago365.octopus.service;

import com.alphago365.octopus.exception.ResourceNotFoundException;
import com.alphago365.octopus.model.Handicap;
import com.alphago365.octopus.model.HandicapChange;
import com.alphago365.octopus.model.Match;
import com.alphago365.octopus.model.Provider;
import com.alphago365.octopus.repository.HandicapChangeRepository;
import com.alphago365.octopus.repository.HandicapRepository;
import com.alphago365.octopus.repository.ProviderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
public class HandicapService {

    @Autowired
    private HandicapRepository handicapRepository;

    @Autowired
    private HandicapChangeRepository handicapChangeRepository;

    @Autowired
    private ProviderRepository providerRepository;

    public List<Handicap> findByMatch(Match match) {
        List<Handicap> handicapList = handicapRepository.findByMatch(match);
        handicapList.forEach(HandicapService::sortChangeHistories);
        return handicapList;
    }

    private static void sortChangeHistories(Handicap odds) {
        odds.getChangeHistories().sort(Comparator.comparing(HandicapChange::getUpdateTime).reversed());
    }

    public Handicap findByMatchAndProvider(Match match, Provider provider) {
        Handicap handicap = handicapRepository.findByMatchAndProvider(match, provider).<ResourceNotFoundException>orElseThrow(() -> {
            throw new ResourceNotFoundException("Handicap not found by match and provider");
        });
        sortChangeHistories(handicap);
        return handicap;
    }

    @Transactional
    public List<Handicap> saveAll(@NotNull List<Handicap> handicapList) {
        List<Handicap> savedHandicapList = new ArrayList<>();
        handicapList.forEach(handicap -> {
            handicap.setProvider(saveProviderIfNotExists(handicap));
            savedHandicapList.add(handicapRepository.save(handicap));
        });
        return savedHandicapList;
    }

    private Provider saveProviderIfNotExists(Handicap handicap) {
        Provider tempProvider = handicap.getProvider();
        return providerRepository
                .findById(tempProvider.getId())
                .orElse(providerRepository.save(tempProvider));
    }

    @Transactional
    public List<HandicapChange> saveAllChanges(@NotNull List<HandicapChange> handicapChangeList) {
        return handicapChangeRepository.saveAll(handicapChangeList);
    }
}

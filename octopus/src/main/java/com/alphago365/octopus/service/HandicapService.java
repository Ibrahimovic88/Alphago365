package com.alphago365.octopus.service;

import com.alphago365.octopus.exception.ResourceNotFoundException;
import com.alphago365.octopus.model.Handicap;
import com.alphago365.octopus.model.HandicapChange;
import com.alphago365.octopus.model.Match;
import com.alphago365.octopus.model.Provider;
import com.alphago365.octopus.repository.HandicapChangeRepository;
import com.alphago365.octopus.repository.HandicapRepository;
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
public class HandicapService {

    @Autowired
    private MatchService matchService;

    @Autowired
    private ProviderService providerService;

    @Autowired
    private HandicapRepository handicapRepository;
    @Autowired
    private HandicapChangeRepository handicapChangeRepository;

    @Transactional
    public List<Handicap> findByMatchId(Long matchId) {
        Match match = matchService.findById(matchId);
        List<Handicap> handicapList = handicapRepository.findByMatch(match);
        handicapList.forEach(HandicapService::sortChangeHistories);
        return handicapList.parallelStream()
                .sorted(Comparator.comparing(Handicap::getDisplayOrder))
                .collect(Collectors.toList());
    }

    private static void sortChangeHistories(Handicap odds) {
        odds.getChangeHistories().sort(Comparator.comparing(HandicapChange::getUpdateTime).reversed());
    }

    @Transactional
    public Handicap findByMatchIdAndProviderId(Long matchId, int providerId) {
        Match match = matchService.findById(matchId);
        Provider provider = providerService.findById(providerId);
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
            Provider tempProvider = handicap.getProvider();
            Provider savedProvider = saveProviderIfNotExists(tempProvider);
            handicap.setProvider(savedProvider);
            savedHandicapList.add(handicapRepository.save(handicap));
        });
        return savedHandicapList;
    }

    private Provider saveProviderIfNotExists(Provider tempProvider) {
        Integer id = tempProvider.getId();
        if (providerService.existsById(id)) {
            return providerService.findById(id);
        }
        return providerService.save(tempProvider);
    }

    @Transactional
    public List<HandicapChange> saveAllChanges(@NotNull List<HandicapChange> handicapChangeList) {
        return handicapChangeRepository.saveAll(handicapChangeList);
    }
}

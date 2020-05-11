package com.alphago365.octopus.service;

import com.alphago365.octopus.exception.ResourceNotFoundException;
import com.alphago365.octopus.model.Match;
import com.alphago365.octopus.model.Overunder;
import com.alphago365.octopus.model.OverunderChange;
import com.alphago365.octopus.model.Provider;
import com.alphago365.octopus.repository.OverunderChangeRepository;
import com.alphago365.octopus.repository.OverunderRepository;
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
public class OverunderService {

    @Autowired
    private MatchService matchService;

    @Autowired
    private ProviderService providerService;

    @Autowired
    private OverunderRepository overunderRepository;
    @Autowired
    private OverunderChangeRepository overunderChangeRepository;

    @Transactional
    public List<Overunder> findByMatchId(Long matchId) {
        Match match = matchService.findById(matchId);
        List<Overunder> handicapList = overunderRepository.findByMatch(match);
        handicapList.forEach(OverunderService::sortChangeHistories);
        return handicapList.parallelStream()
                .sorted(Comparator.comparing(Overunder::getDisplayOrder))
                .collect(Collectors.toList());
    }

    private static void sortChangeHistories(Overunder overunder) {
        overunder.getChangeHistories().sort(Comparator.comparing(OverunderChange::getUpdateTime).reversed());
    }

    @Transactional
    public Overunder findByMatchIdAndProviderId(Long matchId, int providerId) {
        Match match = matchService.findById(matchId);
        Provider provider = providerService.findById(providerId);
        Overunder overunder = overunderRepository.findByMatchAndProvider(match, provider).<ResourceNotFoundException>orElseThrow(() -> {
            throw new ResourceNotFoundException("Handicap not found by match and provider");
        });
        sortChangeHistories(overunder);
        return overunder;
    }

    @Transactional
    public List<Overunder> saveAll(@NotNull List<Overunder> overunderList) {
        List<Overunder> savedList = new ArrayList<>();
        overunderList.forEach(overunder -> {
            Provider tempProvider = overunder.getProvider();
            Provider savedProvider = saveProviderIfNotExists(tempProvider);
            overunder.setProvider(savedProvider);
            savedList.add(overunderRepository.save(overunder));
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
    public List<OverunderChange> saveAllChanges(@NotNull List<OverunderChange> overunderChangeList) {
        return overunderChangeRepository.saveAll(overunderChangeList);
    }
}

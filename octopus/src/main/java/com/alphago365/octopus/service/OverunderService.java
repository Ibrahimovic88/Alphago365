package com.alphago365.octopus.service;

import com.alphago365.octopus.exception.ResourceNotFoundException;
import com.alphago365.octopus.model.Match;
import com.alphago365.octopus.model.Overunder;
import com.alphago365.octopus.model.OverunderChange;
import com.alphago365.octopus.model.Provider;
import com.alphago365.octopus.repository.OverunderChangeRepository;
import com.alphago365.octopus.repository.OverunderRepository;
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
public class OverunderService {

    @Autowired
    private OverunderRepository overunderRepository;

    @Autowired
    private OverunderChangeRepository overunderChangeRepository;

    @Autowired
    private ProviderRepository providerRepository;

    public List<Overunder> findByMatch(Match match) {
        List<Overunder> handicapList = overunderRepository.findByMatch(match);
        handicapList.forEach(OverunderService::sortChangeHistories);
        return handicapList;
    }

    private static void sortChangeHistories(Overunder overunder) {
        overunder.getChangeHistories().sort(Comparator.comparing(OverunderChange::getUpdateTime).reversed());
    }

    public Overunder findByMatchAndProvider(Match match, Provider provider) {
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
            overunder.setProvider(saveProviderIfNotExists(overunder));
            savedList.add(overunderRepository.save(overunder));
        });
        return savedList;
    }

    private Provider saveProviderIfNotExists(Overunder overunder) {
        Provider tempProvider = overunder.getProvider();
        return providerRepository
                .findById(tempProvider.getId())
                .orElse(providerRepository.save(tempProvider));
    }

    @Transactional
    public List<OverunderChange> saveAllChanges(@NotNull List<OverunderChange> overunderChangeList) {
        return overunderChangeRepository.saveAll(overunderChangeList);
    }
}

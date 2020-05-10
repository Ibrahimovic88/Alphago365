package com.alphago365.octopus.service;

import com.alphago365.octopus.exception.ResourceNotFoundException;
import com.alphago365.octopus.model.Match;
import com.alphago365.octopus.model.Handicap;
import com.alphago365.octopus.model.Provider;
import com.alphago365.octopus.repository.HandicapRepository;
import com.alphago365.octopus.repository.ProviderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.util.List;

@Service
@Slf4j
public class HandicapService {

    @Autowired
    private HandicapRepository handicapRepository;

    @Autowired
    private ProviderRepository providerRepository;

    public List<Handicap> findByMatch(Match match) {
        return handicapRepository.findByMatch(match);
    }

    public Handicap findByMatchAndProvider(Match match, Provider provider) {
        return handicapRepository.findByMatchAndProvider(match, provider).<ResourceNotFoundException>orElseThrow(() -> {
            throw new ResourceNotFoundException("Handicap not found by match and provider");
        });
    }

    @Transactional
    public void saveAll(@NotNull List<Handicap> handicapList) {
        handicapList.forEach(handicap -> {
            Provider tempProvider = handicap.getProvider();
            Provider savedProvider = providerRepository
                    .findById(tempProvider.getId())
                    .orElse(providerRepository.save(tempProvider));
            handicap.setProvider(savedProvider);
            handicapRepository.save(handicap);
        });
    }
}

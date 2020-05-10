package com.alphago365.octopus.service;

import com.alphago365.octopus.exception.ResourceNotFoundException;
import com.alphago365.octopus.model.Match;
import com.alphago365.octopus.model.Odds;
import com.alphago365.octopus.model.Provider;
import com.alphago365.octopus.repository.OddsRepository;
import com.alphago365.octopus.repository.ProviderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.util.List;

@Service
@Slf4j
public class OddsService {

    @Autowired
    private OddsRepository oddsRepository;

    @Autowired
    private ProviderRepository providerRepository;

    public List<Odds> findByMatch(Match match) {
        return oddsRepository.findByMatch(match);
    }

    public Odds findByMatchAndProvider(Match match, Provider provider) {
        return oddsRepository.findByMatchAndProvider(match, provider).<ResourceNotFoundException>orElseThrow(() -> {
            throw new ResourceNotFoundException("Odds not found by match and provider");
        });
    }

    @Transactional
    public void saveAll(@NotNull List<Odds> oddsList) {
        oddsList.forEach(odds -> {
            Provider tempProvider = odds.getProvider();
            Provider savedProvider = providerRepository.findById(tempProvider.getId())
                    .orElse(providerRepository.save(tempProvider));
            odds.setProvider(savedProvider);
            oddsRepository.save(odds);
        });
    }
}

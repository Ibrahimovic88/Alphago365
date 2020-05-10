package com.alphago365.octopus.service;

import com.alphago365.octopus.exception.ResourceNotFoundException;
import com.alphago365.octopus.model.Provider;
import com.alphago365.octopus.repository.ProviderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Slf4j
public class ProviderService {

    @Autowired
    private ProviderRepository providerRepository;

    @Transactional
    public void save(Provider provider) {
        if (providerRepository.existsById(provider.getId())) {
            log.warn("Skip to save existing provider " + provider);
            return;
        }
        providerRepository.save(provider);
    }

    public Provider findById(int id) {
        return providerRepository.findById(id).<ResourceNotFoundException>orElseThrow(() -> {
            throw new ResourceNotFoundException("Provider not found by id: " + id);
        });
    }
}

package com.poultry.farm.service;

import com.poultry.farm.entity.Batch;
import com.poultry.farm.repository.BatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BatchService {

    @Autowired
    private BatchRepository batchRepository;

    public List<Batch> getAvailableBatches() {
        return batchRepository.findByAvailableHensGreaterThan(0);
    }

    public Batch createBatch(Batch batch) {
        batch.setAvailableHens(batch.getTotalHens());
        return batchRepository.save(batch);
    }
}
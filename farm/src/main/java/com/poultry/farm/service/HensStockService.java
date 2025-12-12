package com.poultry.farm.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poultry.farm.dto.HensStockRequestdto;
import com.poultry.farm.entity.Batch;
import com.poultry.farm.repository.BatchRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class HensStockService {

	@Autowired
	private BatchRepository batchRepository;

	public Batch addStock(HensStockRequestdto hensStockRequestdto) {

		Batch hensBatch = new Batch();

		hensBatch.setTotalHens(hensStockRequestdto.getHens());
		hensBatch.setAvailableHens(hensStockRequestdto.getHens());
		hensBatch.setBreed(hensStockRequestdto.getBreed());
		hensBatch.setDateCreated(LocalDate.now());
		hensBatch.setBatchCode(hensStockRequestdto.getBatchCode());
		hensBatch.setWeight(hensStockRequestdto.getWeight());

		log.info("Stock updated Succesfully");
		return batchRepository.save(hensBatch);
	}

	public List<Batch> getStock() {
		return batchRepository.findAll();
	}

//	public void reduceHensLeft(Long stockId, int hensGiven) {
//
//	    Batch stock = batchRepository.findById(stockId)
//	        .orElseThrow(() -> new RuntimeException("Stock not found"));
//
//	    if(stock.getAvailableHens() < hensGiven) {
//	        throw new RuntimeException("Not enough hens in stock");
//	    }
//
//	    stock.setAvailableHens(stock.getAvailableHens() - hensGiven);
//
//	    batchRepository.save(stock);
//	}

	public Batch updateStock(Long id, Integer hens, String breed, String batchCode) {

		Batch henStock = batchRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Stock Not Found with id: " + id));

		if (hens != null) {
			henStock.setTotalHens(hens);
			henStock.setAvailableHens(hens);
		}

		if (breed != null && !breed.isEmpty()) {
			henStock.setBreed(breed);
		}

		if (batchCode != null && !batchCode.isEmpty()) {
			henStock.setBatchCode(batchCode);
		}

		return batchRepository.save(henStock);
	}



}
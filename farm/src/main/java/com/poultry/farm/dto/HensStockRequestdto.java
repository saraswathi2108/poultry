package com.poultry.farm.dto;

import lombok.Data;

@Data
public class HensStockRequestdto {

	private int hens;
	private String breed;
	private String batchCode;
	private Double weight;
}
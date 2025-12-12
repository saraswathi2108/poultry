package com.poultry.farm.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "batches")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Batch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int totalHens;
    private String batchCode;
    private String breed;
    private LocalDate dateCreated;
    private int availableHens;
    private Double weight;
}
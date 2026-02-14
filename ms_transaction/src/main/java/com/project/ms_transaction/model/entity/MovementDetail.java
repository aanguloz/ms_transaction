package com.project.ms_transaction.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MovementDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "movement_id")
    Movement movement;

    @ManyToOne
    @JoinColumn(name = "warehouse_id")
    Warehouse warehouse;

    @ManyToOne
    @JoinColumn(name = "product_id")
    ProductWarehouse productWarehouse;

    String productLocation;
    Integer quantity;
    BigDecimal unitCost;
    BigDecimal unitPrice;

    String expirationDate;

}

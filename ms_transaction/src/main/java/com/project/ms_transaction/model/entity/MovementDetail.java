package com.project.ms_transaction.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table
@Data
@Getter
@Setter
@Builder
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
    @JoinColumn(name = "product_warehouse_id")
    ProductWarehouse productWarehouse;

    String productLocation;
    Integer quantity;
    Double unitCost;
    Double unitPrice;

    String expirationDate;

}

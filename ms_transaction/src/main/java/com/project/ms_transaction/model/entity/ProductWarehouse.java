package com.project.ms_transaction.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductWarehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;
    String description;
    String expirationDate;
    Double price;

    @OneToMany(mappedBy = "productWarehouse", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Inventory> inventories = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "product_warehouse_warehouse",
            joinColumns = @JoinColumn(name = "product_warehouse_id"),
            inverseJoinColumns = @JoinColumn(name = "warehouse_id")
    )
    Set<Warehouse> warehouses = new HashSet<>();

}

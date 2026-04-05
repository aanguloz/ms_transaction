package com.project.ms_transaction.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Checkin extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(unique = true, nullable = false)
    String code;

    String numberDocument;
    String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id")
    Document document;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "checkin_products",
            joinColumns = @JoinColumn(name = "checkin_id"),
            inverseJoinColumns = @JoinColumn(name = "product_warehouse_id")
    )
    private Set<ProductWarehouse> productWarehouses = new HashSet<>();

    @OneToMany(mappedBy = "checkin", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CheckinDetail> details = new ArrayList<>();

}

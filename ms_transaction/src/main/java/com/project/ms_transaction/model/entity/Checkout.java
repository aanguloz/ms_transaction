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
    public class Checkout extends BaseEntity{

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

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "client_id")
        private Client client;

        @ManyToMany(fetch = FetchType.LAZY)
        @JoinTable(
                name = "checkout_products",
                joinColumns = @JoinColumn(name = "checkout_id"),
                inverseJoinColumns = @JoinColumn(name = "product_warehouse_id")
        )
        private Set<ProductWarehouse> productWarehouses = new HashSet<>();

        @OneToMany(mappedBy = "checkout", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<CheckoutDetail> details = new ArrayList<>();

    }

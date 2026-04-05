package com.project.ms_transaction.model.entity;

import com.project.ms_transaction.model.enums.MovementType;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table
@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Movement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Enumerated(EnumType.STRING)
    MovementType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "checkin_id")
    Checkin checkIn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "checkout_id")
    Checkout checkOut;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_origin_id")
    Warehouse warehouseOrigin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_destiny_id")
    Warehouse warehouseDestiny;

//    Integer quantity;
//    BigDecimal unitCost;
//    BigDecimal unitPrice;

    String documentReference;
    String observation;

    Instant creationDate;

}

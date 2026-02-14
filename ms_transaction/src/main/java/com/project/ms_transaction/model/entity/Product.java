package com.project.ms_transaction.model.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;
    String description;
    Boolean isDeleted = false;

    public Product(String name, String description) {
        this.name = name;
        this.description = description;
    }
}

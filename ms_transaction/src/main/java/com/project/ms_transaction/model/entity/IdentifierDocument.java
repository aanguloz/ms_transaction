package com.project.ms_transaction.model.entity;

import com.project.ms_transaction.model.enums.IdentifierType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IdentifierDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String code;
    String name;
    String description;

    @Enumerated(EnumType.STRING)
    IdentifierType type;

    public IdentifierDocument(String code, String name, String description, IdentifierType type) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.type = type;
    }

}

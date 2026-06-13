package com.project.ms_transaction.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table
@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String completeName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "identifier_document_id", nullable = false)
    private IdentifierDocument identifierDocument;

    private String numberDocument;
    private String phoneNumber;
    private String address;
    private String email;

    public Person (String completeName, IdentifierDocument identifierDocument, String numberDocument, String phoneNumber, String address, String email) {
        this.completeName = completeName;
        this.identifierDocument = identifierDocument;
        this.numberDocument = numberDocument;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.email = email;
    }

}

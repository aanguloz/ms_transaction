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
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "person_id", unique = true)
    private Person person;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "company_id", unique = true)
    private Company company;

    private Boolean isActive;

    @PrePersist @PreUpdate
    private void validateExclusiveAssociation() {
        boolean hasPerson = person != null;
        boolean hasCompany = company != null;

        if (hasPerson && hasCompany) {
            throw new IllegalStateException("Un cliente no puede ser Persona y Empresa simultáneamente");
        }
        if (!hasPerson && !hasCompany) {
            throw new IllegalStateException("Un cliente debe tener asociada una Persona o una Empresa");
        }
    }

    public String getDisplayName() {
        return person != null ? person.getCompleteName() : (company != null ? company.getCompanyName() : null);
    }

    public String getIdentifier() {
        return person != null ? person.getNumberDocument() : (company != null ? company.getRucNumber() : null);
    }

    public Client(Person person, Boolean isActive) {
        this.person = person;
        this.isActive = isActive;
    }

    public Client(Company company, Boolean isActive) {
        this.company = company;
        this.isActive = isActive;
    }
}

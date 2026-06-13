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
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String companyName;
    private String rucNumber;
    private String address;
    private String email;
    private String phoneNumber;

    public Company(String companyName, String rucNumber, String address, String email, String phoneNumber) {
        this.companyName = companyName;
        this.rucNumber = rucNumber;
        this.address = address;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

}

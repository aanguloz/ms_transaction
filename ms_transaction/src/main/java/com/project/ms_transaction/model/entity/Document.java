package com.project.ms_transaction.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Document {

    Long id;
    String code;
    String codeSunat;
    String name;
    String description;

}

package com.project.ms_transaction.model.entity;

import com.project.ms_transaction.model.enums.DocumentType;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String code;
    String codeSunat;
    String name;
    String description;

    @Enumerated(EnumType.STRING)
    DocumentType type;

    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Checkin> checkins = new ArrayList<>();

    public Document(String code, String codeSunat, String name, String description, DocumentType type) {
        this.code = code;
        this.codeSunat = codeSunat;
        this.name = name;
        this.description = description;
        this.type = type;
    }

}

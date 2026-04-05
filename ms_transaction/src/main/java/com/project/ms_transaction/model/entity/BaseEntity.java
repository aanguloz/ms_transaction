package com.project.ms_transaction.model.entity;

import jakarta.persistence.MappedSuperclass;
import lombok.*;

import java.time.Instant;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class BaseEntity {

    Instant createdAt;
    Instant updatedAt;
    Instant deletedAt;

}

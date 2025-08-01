package com.wow.libre.infrastructure.entities;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "partner")
public class PartnerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private String name;
    @Column(name = "realm_id")
    private Long realmId;
    private boolean status;
}

package com.joao.dev.clinica_odontologica.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "turn_status")
@Data
public class TurnStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name;
}

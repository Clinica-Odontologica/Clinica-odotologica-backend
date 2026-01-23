package com.joao.dev.clinica_odontologica.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Entity
@Table(name = "turn_services")
@Data
public class TurnService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "turn_id", nullable = false)
    private Turn turn;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;

    @Column(name = "price_at_time", nullable = false)
    private BigDecimal priceAtTime;
}

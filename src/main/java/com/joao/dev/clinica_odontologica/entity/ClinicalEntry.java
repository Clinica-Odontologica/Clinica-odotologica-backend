package com.joao.dev.clinica_odontologica.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "clinical_entries")
@Data
public class ClinicalEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "turn_id", nullable = false, unique = true)
    private Turn turn;

    @Column(nullable = false)
    private String diagnosis;

    @Column(name = "treatment_notes", nullable = false, columnDefinition = "TEXT")
    private String treatmentNotes;

    @Column(name = "attachment_url")
    private String attachmentUrl;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() { createdAt = LocalDateTime.now(); updatedAt = LocalDateTime.now(); }
    @PreUpdate
    protected void onUpdate() { updatedAt = LocalDateTime.now(); }
}

package com.br.equaly.auth.app.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "CorporationKey")
@Table(name = "KEY", schema = "CORPORATION")
public class CorporationKey {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "CORPORATION_ID")
    private Corporation corporation;

    @Column(name = "VALUE")
    private String value;

    @Column(name = "IS_ACTIVE")
    private Boolean isActive;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name = "DISABLED_AT")
    private LocalDateTime disabledAt;
}

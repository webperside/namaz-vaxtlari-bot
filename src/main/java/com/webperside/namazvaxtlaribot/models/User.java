package com.webperside.namazvaxtlaribot.models;

import com.webperside.namazvaxtlaribot.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.time.Instant;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "user_id")
    private Integer id;

    @Column(name = "user_tg_id", length = 50)
    private String userTgId;

    @ManyToOne
    @JoinColumn(name="fk_settlement_id")
    private Settlement settlement;

    @Column(name = "user_status", nullable = false, columnDefinition = "tinyint(4) default 1")
    private UserStatus userStatus;

    @Column(name = "created_at", updatable = false, columnDefinition = "timestamp")
    private Instant createdAt;
}

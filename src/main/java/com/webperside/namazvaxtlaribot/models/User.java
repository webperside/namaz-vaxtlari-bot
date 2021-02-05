package com.webperside.namazvaxtlaribot.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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
    @JoinColumn(name="settlement_id")
    private Settlement settlement;

}

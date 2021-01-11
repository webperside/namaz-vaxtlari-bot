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
@Table(name = "city_settlement")
public class CitySettlement {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "city_settlement_id")
    private Integer id;

    @JoinColumn(name = "city_id", referencedColumnName = "city_id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private City city;

    @JoinColumn(name = "settlement_id", referencedColumnName = "settlement_id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Settlement settlement;
}

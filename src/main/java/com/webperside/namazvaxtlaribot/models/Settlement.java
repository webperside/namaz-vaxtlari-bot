package com.webperside.namazvaxtlaribot.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "settlement")
public class Settlement {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "settlement_id")
    private Integer id;

    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "value", length = 50)
    private String value;

    @ManyToOne
    @JoinColumn(name="city_id", nullable=false)
    private City city;

}

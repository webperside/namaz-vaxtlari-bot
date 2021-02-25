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
@Table(name = "city")
public class City {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "city_id")
    private Integer id;

    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "value", length = 50)
    private String value;

    @JoinColumn(name = "fk_source_id", referencedColumnName = "source_id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Source source;

    @OneToMany(mappedBy="city")
    private List<Settlement> settlements;
}

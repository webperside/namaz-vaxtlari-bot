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
@Table(name = "source")
public class Source {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "source_id")
    private Integer id;

    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "description", length = 250)
    private String description;

    @Column(name = "url", length = 100)
    private String url;

    @OneToMany(mappedBy = "source")
    private List<City> cities;
}

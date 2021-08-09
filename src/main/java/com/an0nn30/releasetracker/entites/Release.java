package com.an0nn30.releasetracker.entites;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDate;

/***
 * Release.java
 *
 * Standard entity object to handle our Release object.
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
public class Release {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private String status;
    @Column(nullable = false)
    private LocalDate releaseDate;
    @Column(nullable = true)
    private LocalDate createdAtDate;
    @Column(nullable = true)
    private LocalDate lastUpdateAtDate;

}

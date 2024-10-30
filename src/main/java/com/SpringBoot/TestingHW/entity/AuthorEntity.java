package com.SpringBoot.TestingHW.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "Author")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "authoredBy",cascade = CascadeType.ALL)
    private List<bookEntity> bookEntityList;

}

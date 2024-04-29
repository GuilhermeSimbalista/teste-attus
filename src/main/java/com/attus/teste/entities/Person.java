package com.attus.teste.entities;

import com.attus.teste.dtos.PersonRequestDTO;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_person")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fullName;
    private LocalDate dateOfBirth;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Address> addresses = new ArrayList<>();

    public Person(PersonRequestDTO data) {
        this.fullName = data.fullName();
        this.dateOfBirth = data.dateOfBirth();
    }
}

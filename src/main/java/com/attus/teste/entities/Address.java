package com.attus.teste.entities;

import com.attus.teste.dtos.AddressRequestDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "tb_address")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String street;
    private String zipCode;
    private String number;
    private String city;
    private String state;
    private boolean isPrimary = false;

    @ManyToOne
    @JoinColumn(name = "person_id")
    @JsonIgnore
    private Person person;

    public Address(AddressRequestDTO data) {
        this.street = data.street();
        this.zipCode = data.zipCode();
        this.number = data.number();
        this.city = data.city();
        this.state = data.state();
    }

}

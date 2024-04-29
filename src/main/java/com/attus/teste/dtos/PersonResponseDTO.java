package com.attus.teste.dtos;

import com.attus.teste.entities.Address;
import com.attus.teste.entities.Person;

import java.time.LocalDate;
import java.util.List;

public record PersonResponseDTO(Long id, String fullName, LocalDate dateOfBirth, List<Address> addresses) {
    public PersonResponseDTO(Person person) { this(person.getId(), person.getFullName(), person.getDateOfBirth(), person.getAddresses()); }

}

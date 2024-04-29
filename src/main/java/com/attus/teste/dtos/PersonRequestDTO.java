package com.attus.teste.dtos;


import java.time.LocalDate;
import java.util.List;

public record PersonRequestDTO(String fullName, LocalDate dateOfBirth, List<Long> addressesId) {
}

package com.attus.teste.dtos;


public record AddressRequestDTO(String street,
                                String zipCode,
                                String number,
                                String city,
                                String state) {
}

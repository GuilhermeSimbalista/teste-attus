package com.attus.teste.dtos;

import com.attus.teste.entities.Address;

public record AddressResponseDTO(Long id,
                                 String street,
                                 String zipCode,
                                 String number,
                                 String city,
                                 String state,
                                 boolean isPrimary,
                                 Long personId) {

    public AddressResponseDTO(Address address) {
        this(address.getId(),
            address.getStreet(),
            address.getZipCode(),
            address.getNumber(),
            address.getCity(),
            address.getState(),
            address.isPrimary(),
            address.getPerson() != null ? address.getPerson().getId() : null);}
}

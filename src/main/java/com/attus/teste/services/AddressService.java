package com.attus.teste.services;

import com.attus.teste.dtos.AddressRequestDTO;
import com.attus.teste.dtos.AddressResponseDTO;
import com.attus.teste.entities.Address;
import com.attus.teste.repositories.AddressRepository;
import com.attus.teste.repositories.PersonRepository;
import com.attus.teste.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AddressService {

    public final AddressRepository addressRepository;
    public final PersonRepository personRepository;

    public AddressService(AddressRepository addressRepository, PersonRepository personRepository) {
        this.addressRepository = addressRepository;
        this.personRepository = personRepository;
    }

    @Transactional(readOnly = true)
    public AddressResponseDTO findById(Long id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
        return new AddressResponseDTO(address);
    }

    @Transactional(readOnly = true)
    public List<AddressResponseDTO> findAll() {
        return addressRepository.findAll().stream().map(AddressResponseDTO::new).toList();
    }

    @Transactional
    public AddressResponseDTO insert(AddressRequestDTO addressRequest) {
        Address address = new Address(addressRequest);

        Address response = addressRepository.save(address);
        return new AddressResponseDTO(response);
    }

    @Transactional
    public AddressResponseDTO update(Long id, AddressRequestDTO addressRequest) {
        try{
            Address address = addressRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
            address.setStreet(addressRequest.street());
            address.setZipCode(addressRequest.zipCode());
            address.setNumber(addressRequest.number());
            address.setCity(addressRequest.city());
            address.setState(addressRequest.state());

            Address response = addressRepository.save(address);
            return new AddressResponseDTO(response);

        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(id);
        }
    }
}

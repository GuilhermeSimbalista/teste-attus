package com.attus.teste.services;

import com.attus.teste.dtos.PersonRequestDTO;
import com.attus.teste.dtos.PersonResponseDTO;
import com.attus.teste.entities.Address;
import com.attus.teste.entities.Person;
import com.attus.teste.repositories.AddressRepository;
import com.attus.teste.repositories.PersonRepository;
import com.attus.teste.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PersonService {

    public final PersonRepository personRepository;

    public final AddressRepository addressRepository;

    public PersonService(PersonRepository personRepository, AddressRepository addressRepository) {
        this.personRepository = personRepository;
        this.addressRepository = addressRepository;
    }

    @Transactional(readOnly = true)
    public PersonResponseDTO findById(Long id) {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
        return new PersonResponseDTO(person);
    }

    @Transactional(readOnly = true)
    public List<PersonResponseDTO> findAll() {
        return personRepository.findAll().stream().map(PersonResponseDTO::new).toList();
    }

    @Transactional
    public PersonResponseDTO insert(PersonRequestDTO personRequest) {
        Person person = new Person(personRequest);
        List<Address> addresses = addressRepository.findAllById(personRequest.addressesId());
        for (Address address : addresses) {
            address.setPerson(person);
            person.getAddresses().add(address);
        }

        Person response = personRepository.save(person);
        return new PersonResponseDTO(response);
    }

    @Transactional
    public PersonResponseDTO setMainAddress(Long personId, Long addressId) {
        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new ResourceNotFoundException(personId));
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException(addressId));

        person.getAddresses().forEach(e -> e.setPrimary(false));
        address.setPrimary(true);
        addressRepository.save(address);

        Person response = personRepository.save(person);
        return new PersonResponseDTO(response);
    }

    @Transactional
    public PersonResponseDTO update(Long id, PersonRequestDTO personRequest) {
        try {
            Person person = personRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
            person.setFullName(personRequest.fullName());
            person.setDateOfBirth(personRequest.dateOfBirth());
            List<Address> addresses = addressRepository.findAllById(personRequest.addressesId());
            for (Address address : addresses) {
                address.setPerson(person);
                person.getAddresses().add(address);
            }

            Person response = personRepository.save(person);
            return new PersonResponseDTO(response);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(id);
        }

    }
}

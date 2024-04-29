package com.attus.teste.services;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.attus.teste.dtos.PersonRequestDTO;
import com.attus.teste.dtos.PersonResponseDTO;
import com.attus.teste.entities.Address;
import com.attus.teste.entities.Person;
import com.attus.teste.repositories.AddressRepository;
import com.attus.teste.repositories.PersonRepository;
import com.attus.teste.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private PersonService personService;

    @Test
    public void findById_Found_ReturnsPersonResponseDTO() {
        // Setup
        Long validId = 1L;
        Person mockPerson = new Person();
        when(personRepository.findById(validId)).thenReturn(Optional.of(mockPerson));

        // Execute
        PersonResponseDTO result = personService.findById(validId);

        // Verify
        assertNotNull(result);
        assertEquals(mockPerson.getId(), result.id());
    }

    @Test
    public void findById_NotFound_ThrowsResourceNotFoundException() {
        // Setup
        Long invalidId = 2L;
        when(personRepository.findById(invalidId)).thenReturn(Optional.empty());

        // Execute Verify
        assertThrows(ResourceNotFoundException.class, () -> personService.findById(invalidId));
    }

    @Test
    public void findAll_ReturnsListOfPersonResponseDTO() {
        // Setup
        List<Person> people = List.of(new Person(), new Person());
        when(personRepository.findAll()).thenReturn(people);

        // Execute
        List<PersonResponseDTO> results = personService.findAll();

        // Verify
        assertNotNull(results);
        assertEquals(2, results.size());
    }

    @Test
    public void insert_ValidRequest_ReturnsPersonResponseDTO() {
        // Setup
        LocalDate dateOfBirth = LocalDate.parse("2000-02-03");
        PersonRequestDTO request = new PersonRequestDTO("Nome", dateOfBirth, List.of(1L, 2L));
        when(addressRepository.findAllById(request.addressesId())).thenReturn(List.of(new Address(), new Address()));
        when(personRepository.save(any(Person.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Execute
        PersonResponseDTO result = personService.insert(request);

        // Verify
        assertNotNull(result);
        assertEquals(request.fullName(), result.fullName());
    }

    @Test
    public void setMainAddress_ValidIds_UpdatesPrimaryAddress() {
        // Setup
        Long personId = 1L;
        Long addressId = 1L;
        Person mockPerson = new Person();
        Address mockAddress = new Address();
        mockPerson.setAddresses(new ArrayList<>(List.of(mockAddress)));

        when(personRepository.findById(personId)).thenReturn(Optional.of(mockPerson));
        when(addressRepository.findById(addressId)).thenReturn(Optional.of(mockAddress));
        when(personRepository.save(any(Person.class))).thenReturn(mockPerson);
        when(addressRepository.save(any(Address.class))).thenReturn(mockAddress);

        // Execute
        PersonResponseDTO result = personService.setMainAddress(personId, addressId);

        // Verify
        assertTrue(mockAddress.isPrimary());
        assertNotNull(result);
    }

    @Test
    public void setMainAddress_InvalidPersonId_ThrowsResourceNotFoundException() {
        // Setup
        Long invalidPersonId = 99L;
        Long addressId = 1L;
        when(personRepository.findById(invalidPersonId)).thenReturn(Optional.empty());

        // Execute Verify
        assertThrows(ResourceNotFoundException.class, () -> personService.setMainAddress(invalidPersonId, addressId));
    }

    @Test
    public void setMainAddress_InvalidAddressId_ThrowsResourceNotFoundException() {
        // Setup
        Long personId = 1L;
        Long invalidAddressId = 99L;
        Person mockPerson = new Person();
        when(personRepository.findById(personId)).thenReturn(Optional.of(mockPerson));
        when(addressRepository.findById(invalidAddressId)).thenReturn(Optional.empty());

        // Execute Verify
        assertThrows(ResourceNotFoundException.class, () -> personService.setMainAddress(personId, invalidAddressId));
    }

    @Test
    public void update_ValidId_UpdatesPersonAndReturnsDTO() {
        // Setup
        Long validId = 1L;
        LocalDate dateOfBirth = LocalDate.parse("1990-01-01");
        PersonRequestDTO request = new PersonRequestDTO("Updated Name", dateOfBirth, List.of(1L));
        Person existingPerson = new Person();
        existingPerson.setAddresses(new ArrayList<>());

        when(personRepository.findById(validId)).thenReturn(Optional.of(existingPerson));
        when(addressRepository.findAllById(request.addressesId())).thenReturn(List.of(new Address()));
        when(personRepository.save(any(Person.class))).thenReturn(existingPerson);

        // Execute
        PersonResponseDTO result = personService.update(validId, request);

        // Verify
        assertNotNull(result);
        assertEquals("Updated Name", existingPerson.getFullName());
    }

    @Test
    public void update_InvalidId_ThrowsResourceNotFoundException() {
        // Setup
        Long invalidId = 2L;
        PersonRequestDTO request = new PersonRequestDTO("Name", LocalDate.now(), List.of(1L));

        when(personRepository.findById(invalidId)).thenReturn(Optional.empty());

        // Execute Verify
        assertThrows(ResourceNotFoundException.class, () -> personService.update(invalidId, request));
    }



}


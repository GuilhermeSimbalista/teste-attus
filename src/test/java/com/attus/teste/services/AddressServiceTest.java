package com.attus.teste.services;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.attus.teste.dtos.AddressRequestDTO;
import com.attus.teste.dtos.AddressResponseDTO;
import com.attus.teste.entities.Address;
import com.attus.teste.repositories.AddressRepository;
import com.attus.teste.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class AddressServiceTest {

    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private AddressService addressService;

    @Test
    public void findById_Found_ReturnsAddressResponseDTO() {
        // Setup
        Long validId = 1L;
        Address mockAddress = new Address();
        when(addressRepository.findById(validId)).thenReturn(Optional.of(mockAddress));

        // Execute
        AddressResponseDTO result = addressService.findById(validId);

        // Verify
        assertNotNull(result);
        assertEquals(mockAddress.getId(), result.id());
    }

    @Test
    public void findById_NotFound_ThrowsResourceNotFoundException() {
        // Setup
        Long invalidId = 2L;
        when(addressRepository.findById(invalidId)).thenReturn(Optional.empty());

        // Execute Verify
        assertThrows(ResourceNotFoundException.class, () -> addressService.findById(invalidId));
    }

    @Test
    public void findAll_ReturnsListOfAddressResponseDTO() {
        // Setup
        List<Address> addresses = List.of(new Address(), new Address());
        when(addressRepository.findAll()).thenReturn(addresses);

        // Execute
        List<AddressResponseDTO> results = addressService.findAll();

        // Verify
        assertNotNull(results);
        assertEquals(2, results.size());
    }

    @Test
    public void insert_ValidRequest_ReturnsAddressResponseDTO() {
        // Setup
        AddressRequestDTO request = new AddressRequestDTO("Street", "ZipCode", "Number", "City", "State"); // Adapte conforme necessário
        Address mockAddress = new Address();
        when(addressRepository.save(any(Address.class))).thenReturn(mockAddress);

        // Execute
        AddressResponseDTO result = addressService.insert(request);

        // Verify
        assertNotNull(result);
        assertEquals(mockAddress.getStreet(), result.street());
    }

    @Test
    public void update_ValidId_UpdatesAddressAndReturnsDTO() {
        // Setup
        Long validId = 1L;
        AddressRequestDTO request = new AddressRequestDTO("New Street", "New ZipCode", "New Number", "New City", "New State");
        Address existingAddress = new Address();

        when(addressRepository.findById(validId)).thenReturn(Optional.of(existingAddress));
        when(addressRepository.save(any(Address.class))).thenReturn(existingAddress);

        // Execute
        AddressResponseDTO result = addressService.update(validId, request);

        // Verify
        assertNotNull(result);
        assertEquals("New Street", existingAddress.getStreet());
    }

    @Test
    public void update_InvalidId_ThrowsResourceNotFoundException() {
        // Setup
        Long invalidId = 2L;
        AddressRequestDTO request = new AddressRequestDTO("Street", "ZipCode", "Number", "City", "State");

        when(addressRepository.findById(invalidId)).thenReturn(Optional.empty());

        // Execute Verify
        assertThrows(ResourceNotFoundException.class, () -> addressService.update(invalidId, request));
    }





}


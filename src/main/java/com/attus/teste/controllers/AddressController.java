package com.attus.teste.controllers;

import com.attus.teste.dtos.AddressRequestDTO;
import com.attus.teste.dtos.AddressResponseDTO;
import com.attus.teste.services.AddressService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/address")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<AddressResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok().body(addressService.findById(id));
    }

    @GetMapping()
    public ResponseEntity<List<AddressResponseDTO>> findAll() {
        return ResponseEntity.ok().body(addressService.findAll());
    }

    @PostMapping
    public ResponseEntity<AddressResponseDTO> insert(@RequestBody AddressRequestDTO addressRequest) {
        AddressResponseDTO address = addressService.insert(addressRequest);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(address.id()).toUri();
        return ResponseEntity.created(uri).body(address);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AddressResponseDTO> update(@PathVariable Long id, @RequestBody AddressRequestDTO addressRequest) {
        return ResponseEntity.ok().body(addressService.update(id, addressRequest));
    }
}

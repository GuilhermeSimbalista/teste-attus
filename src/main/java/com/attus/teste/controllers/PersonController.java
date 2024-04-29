package com.attus.teste.controllers;

import com.attus.teste.dtos.PersonRequestDTO;
import com.attus.teste.dtos.PersonResponseDTO;
import com.attus.teste.services.PersonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/person")
public class PersonController {

    private final PersonService personService;


    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok().body(personService.findById(id));
    }

    @GetMapping()
    public ResponseEntity<List<PersonResponseDTO>> findAll() {
        return ResponseEntity.ok().body(personService.findAll());
    }

    @PostMapping
    public ResponseEntity<PersonResponseDTO> insert(@RequestBody PersonRequestDTO personRequest) {
        PersonResponseDTO person = personService.insert(personRequest);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(person.id()).toUri();
        return ResponseEntity.created(uri).body(person);
    }

    @PostMapping("/{personId}/address/{addressId}")
    public ResponseEntity<PersonResponseDTO> setMainAddress(@PathVariable Long personId, @PathVariable Long addressId) {
        return ResponseEntity.ok().body(personService.setMainAddress(personId, addressId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonResponseDTO> update(@PathVariable Long id, @RequestBody PersonRequestDTO personRequest) {
        return ResponseEntity.ok().body(personService.update(id, personRequest));
    }

}

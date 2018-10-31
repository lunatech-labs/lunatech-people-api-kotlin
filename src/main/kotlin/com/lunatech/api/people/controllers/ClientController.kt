package com.lunatech.api.people.controllers

import com.lunatech.api.people.model.Client
import com.lunatech.api.people.repository.ClientRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
class ClientController{

    @Autowired
    lateinit var clientRepository: ClientRepository

    @GetMapping("/api/clients")
    fun getClients() = clientRepository.findAll()

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/clients")
    fun saveClient(@RequestBody client: Client): Client {
        return clientRepository.save(client)
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/api/clients")
    fun updateClient(@RequestBody client: Client): Client {
        return clientRepository.save(client)
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/api/clients")
    fun deleteClient(@RequestBody client: Client): Unit {
        clientRepository.deleteById(client.id)
    }
}
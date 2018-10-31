package com.lunatech.api.people.repository

import com.lunatech.api.people.model.Client
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Component


interface ClientRepository : CrudRepository<Client, Int>
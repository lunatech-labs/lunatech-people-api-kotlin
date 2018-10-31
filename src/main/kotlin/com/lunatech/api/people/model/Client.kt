package com.lunatech.api.people.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class Client(@Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Int,
                  val name: String)
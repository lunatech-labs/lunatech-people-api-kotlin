package com.lunatech.api.people.model

import javax.persistence.*

@Entity
data class Project(@Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Int,
                   val name: String,
                   @ManyToOne @JoinColumn(name = "client") val client: Client)
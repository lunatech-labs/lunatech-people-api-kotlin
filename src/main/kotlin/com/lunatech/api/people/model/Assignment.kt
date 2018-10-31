package com.lunatech.api.people.model

import javax.persistence.*

@Entity
data class Assignment(@Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Int,
                      val person: String,
                      @ManyToOne @JoinColumn(name = "project") val project: Project)
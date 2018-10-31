package com.lunatech.api.people.model

data class Name(val fullName: String?, val familyName: String?, val givenName: String?)

data class Person(val email: String?, val name: Name?, val country: String?, val thumbnail: String?,
                  val managers: List<String?>?,
                  val roles: List<String>?,
                  val level: String?,
                  val github: String?)
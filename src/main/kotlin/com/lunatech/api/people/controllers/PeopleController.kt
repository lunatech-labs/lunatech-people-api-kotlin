package com.lunatech.api.people.controllers

import com.google.gson.JsonObject
import com.lunatech.api.people.service.PersonService
import com.lunatech.api.people.utils.PeopleUtils.Companion.processFieldsParam
import com.lunatech.api.people.utils.PeopleUtils.Companion.applyFilters
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
class PeopleController {

    @Autowired
    lateinit var personService: PersonService

    /**
     * Primary endpoint for the People API.
     * A set of filters and field constraints can
     * be provided in the request parameters which
     * are then applied to the list of people.
     */
    @GetMapping("/api/people")
    fun getPeople(@RequestParam allRequestParams: Map<String, String>): List<JsonObject> {
        var allPeople =
                personService.getPeople()

        var filteredPeople =
                applyFilters(allPeople, allRequestParams)

        var filterFields =
                processFieldsParam(allRequestParams)

        return filteredPeople
                .map { p -> p.toJSON(filterFields) }
    }

}
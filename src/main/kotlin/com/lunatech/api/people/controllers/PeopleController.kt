package com.lunatech.api.people.controllers

import com.lunatech.api.people.model.Person
import com.lunatech.api.people.service.PersonService
import com.lunatech.api.people.utils.Utils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
class PeopleController {

    @Autowired
    lateinit var personService: PersonService

    /** CONSTANTS */
    val PARAM_COUNTRY = "country"
    val PARAM_FULL_NAME = "fullName"
    val PARAM_LEVEL = "level"
    val PARAM_MANAGERS = "managers"
    val PARAM_ROLES = "roles"

    val DELIMETER_OR = ","
    val DELIMETER_AND = ";"
    val DELIMETER_AT = "@"

    val SEQUENCE_AND = 0
    val SEQUENCE_OR = 1

    @GetMapping("/api/people")
    fun getPeopleByFilters(@RequestParam allRequestParams: Map<String, String>): List<Person> {
        for (param in allRequestParams) {
            println(param.key + ": " + param.value)
        }

        var allPeople: List<Person> = personService.getPeople()

        //println(allPeople.size)

        var filteredPeople: List<Person> = applyFilters(allPeople, allRequestParams)

        //println(filteredPeople.size)
        //println()

        return filteredPeople
    }

    fun applyFilters(people: List<Person>, filters: Map<String, String>): List<Person> {
        // Copy list
        var peopleToFilter: List<Person> = people.filter {_ -> true}

        if(filters.containsKey(PARAM_COUNTRY)) {
            var countryString: String = filters.get(PARAM_COUNTRY)!!
            var countries: List<String> = countryString.split(DELIMETER_OR)

            peopleToFilter = filterByCountries(peopleToFilter, countries)
        }

        if(filters.containsKey(PARAM_FULL_NAME)) {
            var fullNameString: String = filters.get(PARAM_FULL_NAME)!!
            var fullNames: List<String> = fullNameString.split(DELIMETER_OR)

            peopleToFilter = filterByFullNames(peopleToFilter, fullNames)
        }

        if(filters.containsKey(PARAM_LEVEL)) {
            var levelString: String = filters.get(PARAM_LEVEL)!!
            var levels: List<String> = levelString.split(DELIMETER_OR)

            peopleToFilter = filterByLevels(peopleToFilter, levels)
        }

        if(filters.containsKey(PARAM_MANAGERS)) {
            var managerString: String = filters.get(PARAM_MANAGERS)!!
            var managers: List<String> = managerString.split(DELIMETER_OR)

            peopleToFilter = filterByManagers(peopleToFilter, managers)
        }

        if(filters.containsKey(PARAM_ROLES)) {
            var rolesString: String = filters.get(PARAM_ROLES)!!
            var roles: List<String>

            val operations = Utils.parseAndOrString(rolesString)
            var sequence: Int

            if(operations[0] == 0 && operations[1] == 0) {
                roles = rolesString.split(DELIMETER_OR)
                peopleToFilter = filterByRoles(peopleToFilter, roles, SEQUENCE_OR)
            } else if(operations[0] == 0 && operations[1] == 1) {
                roles = rolesString.split(DELIMETER_OR)
                peopleToFilter = filterByRoles(peopleToFilter, roles, SEQUENCE_OR)
            } else if(operations[0] == 1 && operations[1] == 0) {
                roles = rolesString.split(DELIMETER_AND)
                peopleToFilter = filterByRoles(peopleToFilter, roles, SEQUENCE_AND)
            } else {
                peopleToFilter = emptyList()
            }
        }

        return peopleToFilter
    }

    fun filterByCountries(people: List<Person>, filterCountries: List<String>): List<Person> {
        return people.filter { person -> equalsAtLeastOneCountry(person, filterCountries) }
    }

    fun filterByFullNames(people: List<Person>, filterFullNames: List<String>): List<Person> {
        return people.filter { person -> equalsAtLeastOneFullName(person, filterFullNames) }
    }

    fun filterByLevels(people: List<Person>, filterLevels: List<String>): List<Person> {
        return people.filter { person -> equalsAtLeastOneLevel(person, filterLevels) }
    }

    fun filterByManagers(people: List<Person>, filterManagers: List<String>): List<Person> {
        return people.filter { person -> equalsAtLeastOneManager(person, filterManagers) }
    }

    fun filterByRoles(people: List<Person>, filterRoles: List<String>, sequence: Int): List<Person> {
        when (sequence) {
            SEQUENCE_OR -> return people.filter { person -> equalsAtLeastOneRole(person, filterRoles) }
            SEQUENCE_AND -> return people.filter { person -> equalsAllRoles(person, filterRoles) }
            else -> return people
        }
    }

    fun equalsAtLeastOneCountry(person: Person, countries: List<String>): Boolean {
        var exists = false

        for (country in countries) {
            var countryCleaned = Utils.tidyText(country)
            var personCountryCleaned = Utils.tidyText(person.country!!)

            if(countryCleaned.equals(personCountryCleaned)) {
                exists = true
            }
        }

        return exists
    }

    fun equalsAtLeastOneFullName(person: Person, fullNames: List<String>): Boolean {
        var exists = false

        for (fullName in fullNames) {
            if(equalsFullName(person, fullName)) {
                exists = true
            }
        }

        return exists
    }

    fun equalsFullName(person: Person, filterFullName: String): Boolean {
        var cleanedPersonFullName = Utils.tidyText(person.name!!.fullName!!)
        var cleanedFilterFullName = Utils.tidyText(filterFullName)

        return cleanedPersonFullName.equals(cleanedFilterFullName)
    }

    fun equalsAtLeastOneLevel(person: Person, levels: List<String>): Boolean {
        var exists = false

        for (level in levels) {
            if(person.level != null) {
                var levelCleaned = Utils.tidyText(level)
                var personLevelCleaned = Utils.tidyText(person.level!!)

                if (personLevelCleaned.equals(levelCleaned)) {
                    exists = true
                }
            }
        }

        return exists
    }

    fun equalsAtLeastOneManager(person: Person, managers: List<String>): Boolean {
        var exists: Boolean = false

        for (manager in managers) {
            if(person.managers != null) {
                var managerCleaned = Utils.tidyText(manager)

                var personManagersEmails = person.managers!!
                var personManagersNames = personManagersEmails
                        .map { person -> person!!.split(DELIMETER_AT).get(0) }
                var personManagersNamesCleaned = personManagersNames.map { person -> Utils.tidyText(person) }

                if (personManagersNamesCleaned.contains(managerCleaned)) {
                    exists = true
                }
            }
        }

        return exists
    }

    fun equalsAtLeastOneRole(person: Person, roles: List<String>): Boolean {
        var exists: Boolean = false

        for (role in roles) {
            if (person.roles != null) {
                // Only alphabets, no spaces beginning or end, all uppercase
                var personsRolesCleaned = person.roles!!.map {
                    role -> Utils.tidyText(role) }

                var roleCleaned = Utils.tidyText(role)

                if (personsRolesCleaned.contains(roleCleaned)) {
                    exists = true
                }
            }
        }

        return exists
    }

    fun equalsAllRoles(person: Person, roles: List<String>): Boolean {
        if(person.roles == null) {
            return false
        }

        var personRolesCleaned = person.roles!!.map { role -> Utils.tidyText(role) }
        var rolesCleaned = roles.map { role -> Utils.tidyText(role) }

        // Used to track which roles have been satisfied
        val rolesSatisfied = IntArray(rolesCleaned.size, {0})

        // Compute which of the roles are satisfied by person
        for (i in 0..(rolesCleaned.size - 1)) {
            val roleCleaned = rolesCleaned[i]

            for (personRoleCleaned in personRolesCleaned) {
                if(roleCleaned.equals(personRoleCleaned)) {
                    rolesSatisfied[i] = 1
                }
            }
        }

        // An array representing every role as satisfied
        val allSatisfied = IntArray(rolesCleaned.size, {1})

        return rolesSatisfied.asList()
                .equals(allSatisfied.asList())

    }

}
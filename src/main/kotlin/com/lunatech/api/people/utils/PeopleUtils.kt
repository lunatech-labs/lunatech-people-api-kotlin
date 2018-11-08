package com.lunatech.api.people.utils

import com.lunatech.api.people.model.Person

class PeopleUtils {

    companion object {

        /**
         * Returns a list of field constraints extracted
         * from the request parameters. If no field
         * constraints are provided, then all fields from
         * the Person object are included in the list of constraints
         * i.e. all fields will be included.
         */
        @JvmStatic fun processFieldsParam(allRequestParams: Map<String, String>): List<String> {
            if(!allRequestParams.containsKey(Constants.PARAM_FIELDS)) {
                return Person.getFieldNames()
            }

            var fields = allRequestParams[Constants.PARAM_FIELDS]!!

            return fields
                    .split(Constants.DELIMETER_OR)
                    .map { f -> StringUtils.removeSpaces(f) }
        }

        /**
         * Filters the provided list of people with the
         * appropriate filters specified in the request
         * parameters.
         */
        @JvmStatic fun applyFilters(people: List<Person>, filters: Map<String, String>): List<Person> {
            // Copy list
            var peopleToFilter: List<Person> = people.filter {_ -> true}

            if(filters.containsKey(Constants.PARAM_COUNTRY)) {
                var countryString: String = filters[Constants.PARAM_COUNTRY]!!
                var countries: List<String> = countryString.split(Constants.DELIMETER_OR)

                peopleToFilter = filterByCountries(peopleToFilter, countries)
            }

            if(filters.containsKey(Constants.PARAM_FULL_NAME)) {
                var fullNameString: String = filters[Constants.PARAM_FULL_NAME]!!
                var fullNames: List<String> = fullNameString.split(Constants.DELIMETER_OR)

                peopleToFilter = filterByFullNames(peopleToFilter, fullNames)
            }

            if(filters.containsKey(Constants.PARAM_LEVEL)) {
                var levelString: String = filters[Constants.PARAM_LEVEL]!!
                var levels: List<String> = levelString.split(Constants.DELIMETER_OR)

                peopleToFilter = filterByLevels(peopleToFilter, levels)
            }

            if(filters.containsKey(Constants.PARAM_MANAGERS)) {
                var managerString: String = filters[Constants.PARAM_MANAGERS]!!
                var managers: List<String> = managerString.split(Constants.DELIMETER_OR)

                peopleToFilter = filterByManagers(peopleToFilter, managers)
            }

            if(filters.containsKey(Constants.PARAM_ROLES)) {
                var rolesString: String = filters[Constants.PARAM_ROLES]!!
                var roles: List<String>

                val operations = StringUtils.parseAndOrString(rolesString)

                if(operations[Constants.SEQUENCE_AND] == Constants.NOT_SATISFIED
                        && operations[Constants.SEQUENCE_OR] == Constants.NOT_SATISFIED) {

                    roles = rolesString.split(Constants.DELIMETER_OR)
                    peopleToFilter = filterByRoles(peopleToFilter, roles, Constants.SEQUENCE_OR)

                } else if(operations[Constants.SEQUENCE_AND] == Constants.NOT_SATISFIED
                        && operations[Constants.SEQUENCE_OR] == Constants.SATISFIED) {

                    roles = rolesString.split(Constants.DELIMETER_OR)
                    peopleToFilter = filterByRoles(peopleToFilter, roles, Constants.SEQUENCE_OR)

                } else if(operations[Constants.SEQUENCE_AND] == Constants.SATISFIED
                        && operations[Constants.SEQUENCE_OR] == Constants.NOT_SATISFIED) {

                    roles = rolesString.split(Constants.DELIMETER_AND)
                    peopleToFilter = filterByRoles(peopleToFilter, roles, Constants.SEQUENCE_AND)

                } else {
                    peopleToFilter = emptyList()
                }
            }

            return peopleToFilter
        }

        private fun filterByCountries(people: List<Person>, filterCountries: List<String>): List<Person> {
            return people.filter { person -> equalsAtLeastOneCountry(person, filterCountries) }
        }

        private fun filterByFullNames(people: List<Person>, filterFullNames: List<String>): List<Person> {
            return people.filter { person -> equalsAtLeastOneFullName(person, filterFullNames) }
        }

        private fun filterByLevels(people: List<Person>, filterLevels: List<String>): List<Person> {
            return people.filter { person -> equalsAtLeastOneLevel(person, filterLevels) }
        }

        private fun filterByManagers(people: List<Person>, filterManagers: List<String>): List<Person> {
            return people.filter { person -> equalsAtLeastOneManager(person, filterManagers) }
        }

        /**
         * Filters a list of people by roles.
         * This function can handle both the AND
         * and OR operations. Not both simultaneously,
         * only one at a time.
         */
        private fun filterByRoles(people: List<Person>, filterRoles: List<String>, sequence: Int): List<Person> {
            when (sequence) {
                Constants.SEQUENCE_OR -> return people.filter { person -> equalsAtLeastOneRole(person, filterRoles) }
                Constants.SEQUENCE_AND -> return people.filter { person -> equalsAllRoles(person, filterRoles) }
                else -> return people
            }
        }

        private fun equalsAtLeastOneCountry(person: Person, countries: List<String>): Boolean {
            var exists = false

            for (country in countries) {
                var countryCleaned = StringUtils.tidyText(country)
                var personCountryCleaned = StringUtils.tidyText(person.country!!)

                if(countryCleaned == personCountryCleaned) {
                    exists = true
                }
            }

            return exists
        }

        private fun equalsAtLeastOneFullName(person: Person, fullNames: List<String>): Boolean {
            var exists = false

            for (fullName in fullNames) {
                if(equalsFullName(person, fullName)) {
                    exists = true
                }
            }

            return exists
        }

        private fun equalsFullName(person: Person, filterFullName: String): Boolean {
            var cleanedPersonFullName = StringUtils.tidyText(person.name!!.fullName!!)
            var cleanedFilterFullName = StringUtils.tidyText(filterFullName)

            return cleanedPersonFullName == cleanedFilterFullName
        }

        private fun equalsAtLeastOneLevel(person: Person, levels: List<String>): Boolean {
            var exists = false

            for (level in levels) {
                if(person.level != null) {
                    var levelCleaned = StringUtils.tidyText(level)
                    var personLevelCleaned = StringUtils.tidyText(person.level!!)

                    if (personLevelCleaned == levelCleaned) {
                        exists = true
                    }
                }
            }

            return exists
        }

        private fun equalsAtLeastOneManager(person: Person, managers: List<String>): Boolean {
            var exists = false

            for (manager in managers) {
                if(person.managers != null) {
                    var managerCleaned = StringUtils.tidyText(manager)

                    var personManagersEmails = person.managers!!
                    var personManagersNames = personManagersEmails
                            .map { person -> person!!.split(Constants.DELIMETER_AT)[0] }
                    var personManagersNamesCleaned = personManagersNames
                            .map { person -> StringUtils.tidyText(person) }

                    if (personManagersNamesCleaned.contains(managerCleaned)) {
                        exists = true
                    }
                }
            }

            return exists
        }

        private fun equalsAtLeastOneRole(person: Person, roles: List<String>): Boolean {
            var exists = false

            for (role in roles) {
                if (person.roles != null) {
                    // Only alphabets, no spaces beginning or end, all uppercase
                    var personsRolesCleaned = person.roles!!.map {
                        role -> StringUtils.tidyText(role) }

                    var roleCleaned = StringUtils.tidyText(role)

                    if (personsRolesCleaned.contains(roleCleaned)) {
                        exists = true
                    }
                }
            }

            return exists
        }

        private fun equalsAllRoles(person: Person, roles: List<String>): Boolean {
            if(person.roles == null) {
                return false
            }

            var personRolesCleaned = person.roles!!.map { role -> StringUtils.tidyText(role) }
            var rolesCleaned = roles.map { role -> StringUtils.tidyText(role) }

            // Used to track which roles have been satisfied
            val rolesSatisfied = IntArray(rolesCleaned.size, {Constants.NOT_SATISFIED})

            // Compute which of the roles are satisfied by person
            for (i in 0..(rolesCleaned.size - 1)) {
                val roleCleaned = rolesCleaned[i]

                for (personRoleCleaned in personRolesCleaned) {
                    if(roleCleaned == personRoleCleaned) {
                        rolesSatisfied[i] = 1
                    }
                }
            }

            // An array representing every role as satisfied
            val allSatisfied = IntArray(rolesCleaned.size, {Constants.SATISFIED})

            return rolesSatisfied.asList() == allSatisfied.asList()

        }

    }

}
package com.lunatech.api.people.service

import com.google.api.services.admin.directory.Directory
import com.google.api.services.admin.directory.model.User
import com.lunatech.api.people.model.Name
import com.lunatech.api.people.model.Person
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

interface PersonService{
    fun getPeople(): List<Person>
    fun getPerson(id: String) : Person?
}

@Service
class PersonServiceImpl : PersonService {
    @Autowired
    lateinit var googleAdminServiceBuilder: GoogleAdminServiceBuilder

    lateinit var directory: Directory

    @Cacheable("person")
    override fun getPeople(): List<Person> {
        directory = googleAdminServiceBuilder.buildDirectory()
        val people = directory.users().list().setCustomer("C011fv0nq")
                .set("projection", "full")
                .setMaxResults(500)
                .execute().users.filterNot { x -> x.suspended }.map { x -> convertGSuitePerson(x) }

        return people

    }

    override fun getPerson(id: String): Person? {
        return getPeople().find { x -> x.email == id }
    }

    private fun convertGSuitePerson(user: User): Person{
        val jobDetails : Map<String, Any?> = if(user.customSchemas != null) user.customSchemas.getOrDefault("Job_Details", emptyMap()) else emptyMap()
        val roles = jobDetails.get("roles") as List<Map<String, String>>?
        val level = jobDetails.get("level") as String?
        val github = jobDetails.get("github") as String?

        val rolesList: List<String>? = roles?.map{ x -> x.get("value")} as List<String>?

        val relations = user.relations as List<Map<String, String>>?
        val managers = relations?.filter { x -> x.get("type") == "manager" }?.map { x -> x.get("value") }?.toList()


        val person = Person(email = user.primaryEmail,
                name = Name(user.name.fullName, user.name.familyName, user.name.givenName),
                country = getCountryFromOrganization(user.orgUnitPath),
                thumbnail = user.thumbnailPhotoUrl,
                managers = managers,
                roles = rolesList,
                level = level,
                github = github)

        return person

    }

    val countryPattern = "/Lunatech ([A-Z]{2})".toRegex();

    private fun getCountryFromOrganization(org: String): String {
        var country: String = "NULL"

        if(org.matches(countryPattern))
            country = org.substring(org.length - 2, org.length);

        return country;
    }
}
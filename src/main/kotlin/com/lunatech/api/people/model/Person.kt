package com.lunatech.api.people.model

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.lunatech.api.people.utils.Constants

data class Name(val fullName: String?,
                val familyName: String?,
                val givenName: String?) {

    fun toJSON(): JsonObject {
        var json = JsonObject()

        // Reflect over fields
        val fields = this.javaClass.declaredFields

        // Add to json
        for (field in fields) {
            json.addProperty(field.name, field.get(this) as String)
        }

        return json
    }

}

data class Person(val email: String?,
                  val name: Name?,
                  val country: String?,
                  val thumbnail: String?,
                  val managers: List<String?>?,
                  val roles: List<String>?,
                  val level: String?,
                  val github: String?) {

    fun toJSON(fieldFilters: List<String>): JsonObject {
        var json = JsonObject()

        // Reflect over fields
        val fields = this.javaClass.declaredFields
        val valueMap: HashMap<String, Any?> = HashMap()

        for (f in fields) {
            try {
                valueMap[f.name] = f.get(this)
            } catch(e: IllegalStateException) {
                valueMap[f.name] = null
            }
        }

        // Add field to json if filter matches
        for (field in fieldFilters) {
            if(valueMap.contains(field)) {
                var value = valueMap[field]
                var valueType = if (value != null) value::class else null

                if(valueType == String::class) {
                    json.addProperty(field, value as String)
                } else if(valueType == Name::class) {
                    value = value as Name
                    json.add(field, value.toJSON())
                } else if(valueType.toString() == Constants.CLASS_ARRAYS_ARRAYLIST
                        || valueType.toString() == Constants.CLASS_COLLECTIONS_SINGLETONLIST
                        || valueType == ArrayList::class) {
                    value = value as Iterable<String>
                    var jsonArray = JsonArray()

                    for (element in value) {
                        jsonArray.add(element)
                    }

                    json.add(field, jsonArray)
                } else if(valueType == null) {
                    json.addProperty(field, "null")
                }
            }
        }

        return json
    }

    companion object {

        fun getFieldNames(): List<String> {
            return Person::
            class.java
                    .declaredFields
                    .map { f -> f.name }
        }

    }

}
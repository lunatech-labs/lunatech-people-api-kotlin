package com.lunatech.api.people.service

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson.JacksonFactory
import com.google.api.services.admin.directory.Directory
import com.google.api.services.admin.directory.DirectoryScopes
import org.springframework.stereotype.Component

@Component
class GoogleAdminServiceBuilder {

    private val SERVICE_ACCOUNT_JSON = "{\n" +
            " \"type\": \"service_account\",\n" +
            " \"project_id\": \"lunatech-api-platform\",\n" +
            " \"private_key_id\": \"7cacf4da4576f106f71646d3758a36b856483e26\",\n" +
            " \"private_key\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCv/bst/oMJYFKm\\n2NmT3QVhtZmoRisFI52PmwfAfrEZF4UkyD+fxbCHmXmtAu/4B5sWEG4d6Ah7kXKl\\n3T9LLnDkvfUKhQ6WrJ7Tj8RIdY3pVrEp1O9b2GpwsKyn+E4QmD+b2UR/cwfki2cT\\npwG/8pdlA1/vx6JxXrLpQhugrMeB0sU6gySVKBxCJCcu0TkC1y+hXhpZRTapckgX\\n5AFmQfPEAfnYp5hTjInis49KoZtRyTcCeYfEt1sTpdaV1PgUnV18p9VN5v7QH85M\\nwJLI3+hnjRsjg7H7DOTY1+JNJYULuM4i9B053OR+ANMC96If5ouo3X4qFlrnLU9+\\nYo+53MqXAgMBAAECggEALbMbefQPHs4uaDkDNsw764V2oyWlYi4/adz+BFRhtHxR\\nDJ4bN2fkUTHfULfkjGmdMfuOPKHWIhBAzyVGnEUw95UEHnkyspmcnJ9GfvJXqXHj\\ni1NJ3HW0HhMionNuAg0m51dh3B6IvCYe3BGP+MIL4iUgMmmKmxnWJ5ANEIoNPH6K\\nYNEdw1FLRu8ihb7A9IlaYukljg61ZoTozft+Dh1xoTkiTJs1GLJJSEXR6yt4Q6Yz\\newUX36ANVvbk67kuocEfkpZDEgLb9tiHYhUHBkFNWbmTa2GJpf8KpuxTBiMJzq1O\\nA+tP3PnSPH/bDpInL8PrtCIiXBnPw2xDCuWsQHK7MQKBgQDg551S844WbSEam1VN\\neI+l5FsM7dTOeRosQkYcszpPve9t0QBQsiNVZ5V3VPlPpLQOZ2kG4aweDMz3nqTX\\nyc81OtBq67LmEm/M0j5O0zeTrMF50/ElKeiEWAJdxYwjIa33NeARMPVF/qo23CGh\\n6hzDSI6ByR+IcjTcIXErOPWqIwKBgQDIUthcnv+VXjJpjtya83RpgzGtqygmKYKR\\n6GLGMsBmwk7JMr5j4eyXCJgtyXh3vCYMwgwJjksYk9sRpp3frxW90WnRdBNVZfri\\nyCdzLMzFU0lPtCo6JfGNZExMFPD2itWMFZV6t1EcnLsStqC499rhItDMctUgyCCT\\n/Rvi88ki/QKBgB1Ut4TQ9K/iDZYtueM/nORSsrkt7zn6OzqYbhJiIXGy4J3eyv67\\nLLZ/qtOERntnjH/wZpcaKVtF6hlFHFR0Ikzb3cCD+rHcp637oVqgWzgsKJNd264w\\nF/3iiHR4Rf1y8AA0i4NScjptqv86mDxIjvW86bDtbOpbkp8+0UaEyC0nAoGAGoaN\\ny8XqBfdRKZUXNRVjps0OePae/F6mwXdKDQCPZYv5Lr8fYs55q44hyH5TC1fImgvf\\n2CwVWY3Khk3PvdzLPjVKk8eQ/8/fJsRmJOQbwU1D+d/bb5OypoxPZgun1J2RpVj+\\nUB+SB/3g75trMNLxErqvd0MFYa/eUHpFhQnO6mkCgYEAvNeGrcIEdTaFCeVeTA0i\\n2d7C++RTb+c9L9M2dv9MJ+aRq3+gJ2mxUxd26aTNc52+uF8DCESaMpVnHqvawNEC\\nTCBc9n3UtQjcrUF4GwqgRUiqfMoRrVIGXTp8kHlvfFHZ3GfRa8qvRxa68TsLPiqk\\nJ36BtZnf5Syl974KlSCT2Ys=\\n-----END PRIVATE KEY-----\\n\",\n" +
            " \"client_email\": \"people-api@lunatech-api-platform.iam.gserviceaccount.com\",\n" +
            " \"client_id\": \"117066990439995669517\",\n" +
            " \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\",\n" +
            " \"token_uri\": \"https://accounts.google.com/o/oauth2/token\",\n" +
            " \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\",\n" +
            " \"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/people-api%40lunatech-api-platform.iam.gserviceaccount.com\"\n" +
            "}"
    private val APPLICATION_NAME: String = "Lunatech People API"
    private val JSON_FACTORY: JsonFactory = JacksonFactory()
    private val TOKENS_DIRECTORY_PATH: String = "tokens"
    private val SCOPES = listOf<String>(DirectoryScopes.ADMIN_DIRECTORY_USER_READONLY, DirectoryScopes.ADMIN_DIRECTORY_USERSCHEMA_READONLY)
    private val HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport()
    private val CREDENTIAL = GoogleCredential.fromStream(SERVICE_ACCOUNT_JSON.byteInputStream()).createScoped(SCOPES)
    private val SERVICE_ACCOUNT_USER: String = "erik.bakker@lunatech.com"
    private val impersonatedCredential = GoogleCredential.Builder()
            .setTransport(CREDENTIAL.getTransport())
            .setJsonFactory(CREDENTIAL.getJsonFactory())
            .setServiceAccountId(CREDENTIAL.getServiceAccountId())
            .setServiceAccountUser(SERVICE_ACCOUNT_USER)
            .setServiceAccountPrivateKey(CREDENTIAL.getServiceAccountPrivateKey())
            .setServiceAccountScopes(CREDENTIAL.getServiceAccountScopes())
            .build()

    fun buildDirectory(): Directory {
        return Directory.Builder(HTTP_TRANSPORT, JSON_FACTORY, impersonatedCredential).setApplicationName(APPLICATION_NAME).build()
    }
}
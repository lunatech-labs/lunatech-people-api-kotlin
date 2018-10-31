package com.lunatech.api.people.service

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.googleapis.auth.oauth2.GooglePublicKeysManager
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson.JacksonFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

interface TokenVerificationService{
    fun verify(token: String) : Boolean
}

@Component
class TokenVerificationServiceImpl: TokenVerificationService {

    @Autowired
    lateinit var personService: PersonService

    private val GOOGLE_OAUTH_ID = "181311629383-j7bc8l9paeouthco6g6cfao3sibqj5pv.apps.googleusercontent.com"
    private val JSON_FACTORY: JsonFactory = JacksonFactory()
    private val HTTP_TRANSPORT = NetHttpTransport()
    private val KEY_MANAGER = GooglePublicKeysManager.Builder(HTTP_TRANSPORT, JSON_FACTORY).build()
    private val VERIFIER = GoogleIdTokenVerifier.Builder(KEY_MANAGER)
            .setAudience(listOf(GOOGLE_OAUTH_ID))
            .build()


    override fun verify(token: String): Boolean {
        val result = VERIFIER.verify(token)
        if(result == null) return false
        val person = personService.getPerson(result.payload.email)
        return person != null
    }
}


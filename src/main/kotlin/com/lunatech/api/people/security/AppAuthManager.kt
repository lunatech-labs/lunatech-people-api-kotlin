package com.lunatech.api.people.security

import com.lunatech.api.people.service.TokenVerificationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class AppAuthManager : AuthenticationManager{

    @Autowired
    lateinit var tokenVerificationService: TokenVerificationService

    val KEY : String = "SdEssInlUTHaK1d1ZltHMbOR5Ht7Ak0A"

    override fun authenticate(authentication: Authentication?): Authentication {
        val pair: Pair<String, String> = authentication?.principal as Pair<String, String>
        val header = pair.first.orEmpty()
        val urlParam = pair.second.orEmpty()

        when {
            header.isEmpty() && urlParam.isEmpty() -> throw AuthenticationFailedException("API Key or ID-Token required")
            !header.isEmpty() && !urlParam.isEmpty() -> throw AuthenticationFailedException("API Key and ID-Token received, only one allowed")
            header.isEmpty() && !urlParam.isEmpty() -> {
                if(urlParam != KEY) throw AuthenticationFailedException("Bad API Key")
            }
            !header.isEmpty() && urlParam.isEmpty() -> {
                if(!tokenVerificationService.verify(header)) throw AuthenticationFailedException("Invalid ID-Token")
            }
        }

        authentication!!.isAuthenticated = true
        return authentication!!
    }
}

class AuthenticationFailedException(msg: String?) : AuthenticationException(msg) {}

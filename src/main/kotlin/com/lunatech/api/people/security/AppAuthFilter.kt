package com.lunatech.api.people.security

import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest


class AppAuthFilter : AbstractPreAuthenticatedProcessingFilter() {

    override fun getPreAuthenticatedCredentials(request: HttpServletRequest?): Any {
        return "N/A"
    }

    override fun getPreAuthenticatedPrincipal(request: HttpServletRequest?): Any {
        return Pair(request?.getHeader("X-ID-Token"), request?.getParameter("apiKey"))
    }
}


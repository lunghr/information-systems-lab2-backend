package com.lunghr.informationsystemslab1.auth.config

import com.lunghr.informationsystemslab1.auth.services.JwtService
import com.lunghr.informationsystemslab1.auth.services.UserService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthFilter(
    private val jwtService: JwtService,
    private val userService: UserService
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        if ("OPTIONS" == request.method) {
            response.status = HttpServletResponse.SC_OK
            response.setHeader("Access-Control-Allow-Origin", "*")
            response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
            response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, X-Requested-With")
            return
        }

// Get JWT token from header
        val jwt = jwtService.getTokenFromHeader(request)
        // Get username from JWT token
        val username = jwt?.let { jwtService.getUsername(it) }
        // If username is not null and there is no authentication in the context
        if (username != null && SecurityContextHolder.getContext().authentication == null) {

            userService.userDetailsService().loadUserByUsername(username).takeIf { jwtService.validateToken(jwt, it) }
                ?.let {
                    val authToken = UsernamePasswordAuthenticationToken(it, null, it.authorities).apply {
                        details = WebAuthenticationDetailsSource().buildDetails(request)
                    }
                    SecurityContextHolder.getContext().authentication = authToken
                }
        }
        filterChain.doFilter(request, response)
    }
}

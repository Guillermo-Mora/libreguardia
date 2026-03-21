package com.libreguardia.api.service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtService {
    private companion object {
        //THE SECRET KEY SHOULD BE STORED APART, AND BE RANDOMLY GENERATED IN A FILE OUTSIDE FROM THE APP,
        //THEN THE APP WILL RETRIEVE IT FROM THAT FILE
        private const val SECRET = "5367566859703373367639792F423F452848284D6251655468576D5A71347437"
    }

    fun generateToken(
        email: String,
        role: String
    ): String {
        val claims = hashMapOf<String, Any>(
            "role" to role
        )
        return createToken(claims, email)
    }

    private fun createToken(
        claims: Map<String, Any>,
        email: String
    ): String {
        return Jwts.builder()
            .claims(claims)
            .subject(email)
            .issuedAt(Date())
            //THE EXPIRATION TIME SHOULD BE STORED APART, IN A FILE OUTSIDE FROM THE APP,
            //THEN THE APP WILL RETRIEVE IT FROM THAT FILE
            .expiration(Date(System.currentTimeMillis() + 1000 * 60 * 30))
            .signWith(getSignKey())
            .compact()
    }

    private fun getSignKey() = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET))

    fun extractEmail(token: String): String = extractClaim(
        token = token,
        claimsResolver = Claims::getSubject
    )

    fun extractExpirationDate(token: String): Date = extractClaim(
        token = token,
        claimsResolver = Claims::getExpiration
    )

    fun <T> extractClaim(
        token: String,
        claimsResolver: (Claims) -> T
    ) = claimsResolver(extractAllClaims(token))

    private fun extractAllClaims(token: String): Claims = Jwts.parser()
        .verifyWith(getSignKey())
        .build()
        .parseSignedClaims(token)
        .payload

    private fun isTokenExpired(token: String) = extractExpirationDate(token).before(Date())

    fun validateToken(
        token: String,
        email: String
    ) = (extractEmail(token) == email && !isTokenExpired(token))
}
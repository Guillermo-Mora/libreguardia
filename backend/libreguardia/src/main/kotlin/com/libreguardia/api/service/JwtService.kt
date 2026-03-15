package com.libreguardia.api.service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtService {
    private companion object {
        const val SECRET = "5367566859703373367639792F423F452848284D6251655468576D5A71347437"
    }

    fun generateToken(email: String): String {
        val claims = hashMapOf<String, Any>()
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
            .expiration(Date(System.currentTimeMillis() + 1000 * 60 * 30))
            .signWith(getSignKey())
            .compact()
    }

    private fun getSignKey() = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET))

    fun extractUsername(token: String) = extractClaim(token, Claims::getSubject)

    fun extractExpiration(token: String) = extractClaim(token, Claims::getExpiration)

    fun <T> extractClaim(
        token: String,
        claimsResolver: (Claims) -> T
    ) = claimsResolver(extractAllClaims(token))

    private fun extractAllClaims(token: String) = Jwts.parser()
        .verifyWith(getSignKey())
        .build()
        .parseSignedClaims(token)
        .payload

    private fun isTokenExpired(token: String) = extractExpiration(token).before(Date())

    fun validateToken(
        token: String,
        userDetails: UserDetails
    ) = (extractUsername(token).equals(userDetails.username) && !isTokenExpired(token))
}
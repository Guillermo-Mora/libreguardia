package com.libreguardia.api.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserAppDetails(
    val userEmail: String,
    val userIsActive: Boolean,
    val userPassword: String,
    val userRoleName: String
) : UserDetails {
    override fun getAuthorities(): Collection<GrantedAuthority> = listOf(SimpleGrantedAuthority(userRoleName))

    override fun getUsername() = userEmail

    override fun getPassword() = userPassword

    override fun isAccountNonExpired() = true

    override fun isAccountNonLocked() = true

    override fun isCredentialsNonExpired() = true

    override fun isEnabled() = userIsActive
}
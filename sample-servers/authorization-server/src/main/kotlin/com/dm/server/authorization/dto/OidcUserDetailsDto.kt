package com.dm.server.authorization.dto

import org.springframework.security.core.GrantedAuthority
import org.xyyh.oidc.userdetails.OidcUserDetails

data class OidcUserDetailsDto(
    private val subject: String,
    private val username: String,
    private val password: String,
    private val accountExpired: Boolean = false,
    private val accountLocked: Boolean = false,
    private val credentialsExpired: Boolean = false,
    private val enabled: Boolean = true,
    private val authorities: Collection<GrantedAuthority> = listOf(),
    private val claims: Map<String, Any> = mapOf()
) : OidcUserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority> = authorities.toList()

    override fun getUsername(): String = username

    override fun getPassword(): String = password

    override fun isAccountNonExpired(): Boolean = !accountExpired

    override fun isAccountNonLocked(): Boolean = !accountLocked

    override fun isCredentialsNonExpired(): Boolean = !credentialsExpired

    override fun isEnabled(): Boolean = enabled

    override fun getName(): String = username

    override fun getAttributes(): Map<String, Any> = claims

    override fun getSubject(): String = subject

    override fun getClaims(): Map<String, Any> = claims

    companion object Factory {
        fun from(user: OidcUserDetails): OidcUserDetailsDto {
            return OidcUserDetailsDto(
                user.subject,
                user.username,
                user.password,
                !user.isAccountNonExpired,
                !user.isAccountNonLocked,
                !user.isCredentialsNonExpired,
                user.isEnabled,
                listOf(),
                user.claims
            );
        }
    }
}

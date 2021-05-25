package com.dm.server.authorization.service.impl

import com.dm.server.authorization.dto.OidcUserDetailsDto
import com.dm.uap.entity.User
import com.dm.uap.repository.UserRepository
import com.dm.uap.repository.findOneByUsernameIgnoreCaseOrNull
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.oauth2.core.oidc.StandardClaimNames

import org.springframework.stereotype.Service
import org.xyyh.oidc.userdetails.OidcUserDetails
import org.xyyh.oidc.userdetails.OidcUserDetailsService

@Service
class OidcUserDetailsServiceImpl(
    private val userRepository: UserRepository
) : OidcUserDetailsService {
    override fun loadUserByUsername(username: String): OidcUserDetails {
        fun toOidcUserdetailsDto(user: User): OidcUserDetailsDto {
            val claims: Map<String, Any> = mapOf(
                StandardClaimNames.SUB to user.id.toString(),
                StandardClaimNames.NAME to user.username,
                StandardClaimNames.GIVEN_NAME to "", // TODO
                StandardClaimNames.FAMILY_NAME to "", // TODO
                StandardClaimNames.MIDDLE_NAME to "", // TODO
                StandardClaimNames.NICKNAME to user.fullname,
                StandardClaimNames.PREFERRED_USERNAME to "",// TODO
                StandardClaimNames.PROFILE to "",// TODO
                StandardClaimNames.PICTURE to user.profilePhoto,
                StandardClaimNames.WEBSITE to "",// TODO
                StandardClaimNames.EMAIL to user.email,
                StandardClaimNames.EMAIL_VERIFIED to true,// TODO
                StandardClaimNames.GENDER to "female",// TODO
                StandardClaimNames.BIRTHDATE to (user.birthDate?.toString() ?: ""),
                StandardClaimNames.ZONEINFO to "", // TODO
                StandardClaimNames.LOCALE to "",// TODO
                StandardClaimNames.PHONE_NUMBER to user.mobile,
                StandardClaimNames.PHONE_NUMBER_VERIFIED to true,
                StandardClaimNames.ADDRESS to "",// TODO
                StandardClaimNames.UPDATED_AT to "" // TODO
            )
            return OidcUserDetailsDto(
                user.id.toString(),
                user.username,
                user.password,
                user.isAccountExpired,
                user.isLocked,
                user.isCredentialsExpired,
                user.isEnabled,
                user.roles.map { SimpleGrantedAuthority(it.authority) },
                claims
            )
        }
        return userRepository.findOneByUsernameIgnoreCaseOrNull(username)?.let {
            toOidcUserdetailsDto(it)
        } ?: throw UsernameNotFoundException("user can not find");
    }
}

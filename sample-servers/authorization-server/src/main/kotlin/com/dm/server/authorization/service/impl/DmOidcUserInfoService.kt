package com.dm.server.authorization.service.impl

import com.dm.security.core.userdetails.UserDetailsDto
import com.dm.uap.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.oauth2.core.oidc.OidcUserInfo
import org.springframework.security.oauth2.core.oidc.StandardClaimNames
import org.springframework.stereotype.Service
import org.xyyh.oidc.core.OidcUserInfoService

@Service
class DmOidcUserInfoService(private val userRepository: UserRepository) : OidcUserInfoService {
    override fun loadOidcUserInfo(userDetails: UserDetails): OidcUserInfo {
        if (userDetails is UserDetailsDto) {
            return userRepository.findByIdOrNull(userDetails.id)?.let {
                mapOf(
                    StandardClaimNames.SUB to it.id.toString(),
                    StandardClaimNames.NAME to it.fullname,
                    StandardClaimNames.GIVEN_NAME to it.givenName,
                    StandardClaimNames.FAMILY_NAME to it.familyName,
                    StandardClaimNames.MIDDLE_NAME to it.middleName,
                    StandardClaimNames.NICKNAME to it.fullname,
                    StandardClaimNames.PREFERRED_USERNAME to it.username,
                    StandardClaimNames.PROFILE to it.profile,
                    StandardClaimNames.PICTURE to it.profilePhoto,
                    StandardClaimNames.WEBSITE to it.website,
                    StandardClaimNames.EMAIL to it.email,
                    StandardClaimNames.EMAIL_VERIFIED to true,
                    StandardClaimNames.GENDER to it.gender,
                    StandardClaimNames.BIRTHDATE to (it.birthDate?.toString() ?: ""),
                    StandardClaimNames.ZONEINFO to it.zoneinfo,
                    StandardClaimNames.LOCALE to it.local,
                    StandardClaimNames.PHONE_NUMBER to it.mobile,
                    StandardClaimNames.PHONE_NUMBER_VERIFIED to it.isPhoneNumberVerified,
                    StandardClaimNames.ADDRESS to it.address,
                    StandardClaimNames.UPDATED_AT to (it.lastModifiedTime?.toInstant()?.epochSecond ?: "")
                )
            }.let { OidcUserInfo(it) }
        } else {
            return OidcUserInfo(mapOf())
        }
    }
}

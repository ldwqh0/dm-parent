package com.dm.uap.repository

import com.dm.uap.entity.User

fun UserRepository.findOneByUsernameIgnoreCaseOrNull(username: String): User? =
    findOneByUsernameIgnoreCase(username).orElse(null)


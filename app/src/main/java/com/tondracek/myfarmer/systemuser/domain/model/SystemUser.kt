package com.tondracek.myfarmer.systemuser.domain.model

import java.util.UUID

typealias UserId = UUID

data class SystemUser(
    val id: UserId
)

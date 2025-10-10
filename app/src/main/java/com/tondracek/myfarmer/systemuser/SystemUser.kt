package com.tondracek.myfarmer.systemuser

import java.util.UUID

typealias UserId = UUID

data class SystemUser(
    val id: UserId
)

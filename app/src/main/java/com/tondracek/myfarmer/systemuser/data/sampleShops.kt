package com.tondracek.myfarmer.systemuser.data

import com.tondracek.myfarmer.systemuser.domain.model.SystemUser
import java.util.UUID

val user0 = SystemUser(id = UUID.randomUUID())
val user1 = SystemUser(id = UUID.randomUUID())
val user2 = SystemUser(id = UUID.randomUUID())

val sampleUsers by lazyOf(listOf(user0, user1, user2))

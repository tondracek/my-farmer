package com.tondracek.myfarmer.auth.domain.usecase

import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class LogoutUC @Inject constructor(
    private val auth: FirebaseAuth,
) {
    operator fun invoke() = auth.signOut()

}

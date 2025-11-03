package com.tondracek.myfarmer.contactinfo.domain.model

data class ContactInfo(
    val phoneNumber: String?,
    val email: String?,
    val website: MediaLink?,
    val facebook: MediaLink?,
    val instagram: MediaLink?,
) {
    companion object {
        val EMPTY = ContactInfo(
            phoneNumber = null,
            email = null,
            website = null,
            facebook = null,
            instagram = null,
        )
    }
}

data class MediaLink(
    val title: String,
    val url: String,
)

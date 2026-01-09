package com.tondracek.myfarmer.contactinfo.domain.model

data class ContactInfo(
    val phoneNumber: String?,
    val email: String?,
    val website: String?,
    val facebookLink: String?,
    val instagramLink: String?,
) {
    companion object {
        val EMPTY = ContactInfo(
            phoneNumber = null,
            email = null,
            website = null,
            facebookLink = null,
            instagramLink = null,
        )
    }
}

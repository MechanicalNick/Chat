package com.tinkoff.homework.data.dto

import javax.inject.Inject

interface Credentials{
    val id: Long
    val fullName: String
    val avatar: String
}

class CredentialsImpl @Inject constructor(): Credentials {
    override val id = 605342L
    override val fullName = "Niktia"
    override val avatar = "https://secure.gravatar.com/avatar/ee4b9444f72347133a30fd3b3c7c6539?d=identicon&version=1"
}
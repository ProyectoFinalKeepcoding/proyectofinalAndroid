package com.mockknights.petshelter.testUtils.fakeData

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

object FakeLoginData {

    const val validUser = "validUser"
    const val validPassword = "validPassword"
    const val token = "token"
    const val id = "id"


    fun getToken(user: String, password: String): Flow<List<String>> {
        return if(user == validUser && password == validPassword) {
            flow { emit(listOf(token, id)) }
        } else {
            flow { emit(emptyList()) }
        }
    }
}
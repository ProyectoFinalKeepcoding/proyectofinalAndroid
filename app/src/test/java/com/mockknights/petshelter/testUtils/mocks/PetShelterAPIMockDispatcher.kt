package com.mockknights.petshelter.testUtils.mocks

import com.mockknights.petshelter.testUtils.getJson
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest
import java.net.HttpURLConnection
import java.util.concurrent.TimeUnit

class PetShelterAPIMockDispatcher: Dispatcher() {
    override fun dispatch(request: RecordedRequest): MockResponse {
        return when (request.path) {
            "/shelters" -> {
                return MockResponse()
                    .setResponseCode(HttpURLConnection.HTTP_OK)
                    .setBody(getJson("json/petshelters.json"))
            }
            "/shelters/2D2118F9-2C9D-4351-B4A4-B4972F9C9730" -> {
                return MockResponse()
                    .setResponseCode(HttpURLConnection.HTTP_OK)
                    .setBody(getJson("json/petshelter.json"))
            }
            "/auth/signin" -> {
                return MockResponse()
                    .setResponseCode(HttpURLConnection.HTTP_OK)
                    .setBody(getJson("json/login.json"))
            }

            else -> MockResponse().throttleBody(1024, 5, TimeUnit.SECONDS)
        }
    }
}
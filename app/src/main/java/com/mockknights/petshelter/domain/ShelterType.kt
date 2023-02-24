package com.mockknights.petshelter.domain

import com.squareup.moshi.Json

enum class ShelterType(val stringValue: String) {
    @Json(name = "particular") PARTICULAR("particular"),
    @Json(name = "localGovernment") LOCAL_GOVERNMENT("localGovernment"),
    @Json(name = "veterinary") VETERINARY("veterinary"),
    @Json(name = "shelterPoint") SHELTER_POINT("shelterPoint"),
    @Json(name = "kiwokoStore") KIWOKO_STORE("kiwokoStore");

    override fun toString(): String {
        return when(this) {
            PARTICULAR -> "Particular"
            LOCAL_GOVERNMENT -> "Ayuntamiento"
            VETERINARY -> "Veterinario"
            SHELTER_POINT -> "Punto de acogida"
            KIWOKO_STORE -> "Tienda Kiwoko"
        }
    }}
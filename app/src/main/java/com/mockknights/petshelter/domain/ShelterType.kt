package com.mockknights.petshelter.domain

enum class ShelterType(val stringValue: String) {
    PARTICULAR("particular"),
    LOCAL_GOVERNMENT("localGovernment"),
    VETERINARY("veterinary"),
    SHELTER_POINT("shelterPoint"),
    KIWOKO_STORE("kiwokoStore");

    override fun toString(): String {
        return when(this) {
            PARTICULAR -> "Particular"
            LOCAL_GOVERNMENT -> "Ayuntamiento"
            VETERINARY -> "Veterinario"
            SHELTER_POINT -> "Punto de acogida"
            KIWOKO_STORE -> "Tienda Kiwoko"
        }
    }}
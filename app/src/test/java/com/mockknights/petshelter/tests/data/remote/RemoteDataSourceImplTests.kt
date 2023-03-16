package com.mockknights.petshelter.tests.data.remote

import com.google.common.truth.Truth
import com.mockknights.petshelter.data.remote.RemoteDataSourceImpl
import com.mockknights.petshelter.testUtils.fakeData.FakePetShelterData
import com.mockknights.petshelter.testUtils.fakeData.FakeRegisterData
import com.mockknights.petshelter.testUtils.fakes.FakeAPI
import com.mockknights.petshelter.tests.base.BaseNetworkTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import okhttp3.MultipartBody
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)

class RemoteDataSourceImplTests: BaseNetworkTest() {

    lateinit var sut: RemoteDataSourceImpl

    @Before
    fun setupRemote() {
        sut = RemoteDataSourceImpl(api)
    }

    @Test
    fun `WHEN getToken THEN returns token`() = runTest {
        // GIVEN

        // WHEN
        val tokenAndId = sut.getToken().first()

        // THEN
        Truth.assertThat(tokenAndId[0]).isEqualTo("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJjb20uaXNtYWVsc2FicmkuUGV0U2hlbHRlciIsInN1YiI6IjcwOERENUZFLTk3N0YtNEQ2My1CMEUzLTZFODFBMTFDQjlGRSJ9.aqAdTFsFsFDrWSpxoiQYZxJHehe-R_EaUm5UOcV8WB4")
        Truth.assertThat(tokenAndId[1]).isEqualTo("708DD5FE-977F-4D63-B0E3-6E81A11CB9FE")
    }

    @Test
    fun `WHEN getShelters THEN returns shelters`() = runTest {
        // GIVEN

        // WHEN
        val shelters = sut.getAllPetShelter().first()

        // THEN
        Truth.assertThat(shelters[0].name).isEqualTo("Fran")
    }

    @Test
    fun `WHEN getShelter THEN returns shelter`() = runTest {
        // GIVEN

        // WHEN
        val shelter = sut.getShelter("2D2118F9-2C9D-4351-B4A4-B4972F9C9730").first()

        // THEN
        Truth.assertThat(shelter.name).isEqualTo("Fran1")
    }

    @Test
    fun `WHEN updateShelter THEN updates shelter`() = runTest {
        // GIVEN
        val fakeApi = FakeAPI()
        sut = RemoteDataSourceImpl(fakeApi)
        // WHEN
        sut.updateShelter("", FakePetShelterData.getRemotePetShelter())

        // THEN
        Truth.assertThat(fakeApi.updateCompleted).isTrue()
    }

    @Test
    fun `WHEN uploadPhoto THEN uploads photo`() = runTest {
        // GIVEN
        val fakeApi = FakeAPI()
        sut = RemoteDataSourceImpl(fakeApi)
        // WHEN
        sut.uploadPhoto("", MultipartBody.Part.createFormData("",""))

        // THEN
        Truth.assertThat(fakeApi.uploadCompleted).isTrue()
    }

    @Test
    fun `WHEN register THEN register completes`() = runTest {
        // GIVEN
        val fakeApi = FakeAPI()
        sut = RemoteDataSourceImpl(fakeApi)
        // WHEN
        sut.register(FakeRegisterData.getRegisterRequest(true))

        // THEN
        Truth.assertThat(fakeApi.registerCompleted).isTrue()
    }
}
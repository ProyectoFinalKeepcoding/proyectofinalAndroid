package com.mockknights.petshelter.tests.ui.detail

import android.content.Context
import com.google.common.truth.Truth
import com.mockknights.petshelter.data.RepositoryImpl
import com.mockknights.petshelter.data.remote.response.Address
import com.mockknights.petshelter.data.remote.response.PetShelterRemote
import com.mockknights.petshelter.di.RemoteModule
import com.mockknights.petshelter.domain.PetShelter
import com.mockknights.petshelter.domain.Repository
import com.mockknights.petshelter.domain.ShelterType
import com.mockknights.petshelter.testUtils.fakeData.FakeDetailData
import com.mockknights.petshelter.testUtils.fakeData.FakePetShelterData
import com.mockknights.petshelter.testUtils.fakes.FakeRemoteDataSource
import com.mockknights.petshelter.ui.detail.DetailState
import com.mockknights.petshelter.ui.detail.DetailViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
class DetailViewModelTests {

    private val testScheduler = TestCoroutineScheduler()
    private val testDispatcher = StandardTestDispatcher(testScheduler)
    private val testScope = TestScope(testDispatcher)
    private lateinit var context: Context
    private lateinit var fakeRemoteDataSource: FakeRemoteDataSource
    private lateinit var repository: Repository
    private lateinit var sut: DetailViewModel

    @Before
    fun setUp() {
        // Create repository and fake data source
        fakeRemoteDataSource = FakeRemoteDataSource()
        context = RuntimeEnvironment.getApplication().baseContext
        repository = RepositoryImpl(
            remoteDataSource = fakeRemoteDataSource,
            sharedPreferences = context.getSharedPreferences("NAME", Context.MODE_PRIVATE),
            mapper = RemoteModule.provideMapper(),
        )
        // Create the SUT
        sut = DetailViewModel(repository, testDispatcher)
    }

    @Test
    fun `WHEN getShelterDetail with correct id THEN success value is set`() = testScope.runTest {

        // GIVEN the setup conditions

        // WHEN get the actual detailState with a valid id
        sut.getShelterDetail(FakeDetailData.successId)
        advanceUntilIdle()

        // THEN, the state will be set as success
        val detailStatePetShelter = (sut.detailState.value as DetailState.Success).petShelter
        Truth.assertThat(sut.detailState.value).isInstanceOf(DetailState.Success::class.java)
        Truth.assertThat(detailStatePetShelter).isEqualTo(FakePetShelterData.getPetShelter())
    }

    @Test
    fun `WHEN getShelterDetail with incorrect id THEN failure value is set`() = testScope.runTest {
        // GIVEN the setup conditions

        // WHEN get the actual detailState with an invalid id
        sut.getShelterDetail(FakeDetailData.errorId)
        advanceUntilIdle()

        // THEN, the state will be set as error in the catch block
        val detailStateErrorMessage = (sut.detailState.value as DetailState.Error).message
        Truth.assertThat(sut.detailState.value).isInstanceOf(DetailState.Error::class.java)
        Truth.assertThat(detailStateErrorMessage)
            .isEqualTo("No shelter found with id: ${FakeDetailData.errorId}")
    }

    @Test
    fun `WHEN updated shelter name THEN shelter name is updated`() = testScope.runTest {

        // GIVEN the setup conditions
        // Get the detail state with a valid id
        sut.getShelterDetail(FakeDetailData.successId)
        advanceUntilIdle()
        // Get the detail state value
        val priorValue = (sut.detailState.value as DetailState.Success).petShelter

        // WHEN updated the shelter name
        sut.onEditName("${priorValue.name}modified")
        advanceUntilIdle()

        // THEN, the modified value is prior name plus modified
        val modifiedValue = (sut.detailState.value as DetailState.Success).petShelter
        Truth.assertThat(modifiedValue).isInstanceOf(PetShelter::class.java)
        Truth.assertThat(modifiedValue.name).isEqualTo("${priorValue.name}modified")
    }

    @Test
    fun `WHEN updated shelter phone THEN shelter phone is updated`() = testScope.runTest {

        // GIVEN the setup conditions
        // Get the detail state with a valid id
        sut.getShelterDetail(FakeDetailData.successId)
        advanceUntilIdle()
        // Get the detail state value
        val priorValue = (sut.detailState.value as DetailState.Success).petShelter

        // WHEN updated the shelter phone number
        sut.onUpdatedPhone("${priorValue.phoneNumber}0")
        advanceUntilIdle()

        // THEN, the modified value is prior phone number plus 0
        val modifiedValue = (sut.detailState.value as DetailState.Success).petShelter
        Truth.assertThat(modifiedValue).isInstanceOf(PetShelter::class.java)
        Truth.assertThat(modifiedValue.phoneNumber).isEqualTo("${priorValue.phoneNumber}0")
    }

    @Test
    fun `WHEN updated shelter address THEN shelter address is updated`() = testScope.runTest {

        // GIVEN the setup conditions
        // Get the detail state with a valid id
        sut.getShelterDetail(FakeDetailData.successId)
        advanceUntilIdle()

        // WHEN updated the shelter address
        sut.onUpdatedAddress("0.0", "0.0")
        advanceUntilIdle()

        // THEN, the modified value is Address(0.0, 0.0)
        val modifiedValue = (sut.detailState.value as DetailState.Success).petShelter
        Truth.assertThat(modifiedValue).isInstanceOf(PetShelter::class.java)
        Truth.assertThat(modifiedValue.address).isEqualTo(Address(0.0, 0.0))
    }

    @Test
    fun `WHEN updated shelterType THEN shelterType is updated`() = testScope.runTest {

        // GIVEN the setup conditions
        // Get the detail state with a valid id
        sut.getShelterDetail(FakeDetailData.successId)
        advanceUntilIdle()

        // WHEN updated the shelter type
        sut.onUpdatedShelterType(ShelterType.PARTICULAR)
        advanceUntilIdle()

        // THEN, the modified value is ShelterType.PARTICULAR
        val modifiedValue = (sut.detailState.value as DetailState.Success).petShelter
        Truth.assertThat(modifiedValue).isInstanceOf(PetShelter::class.java)
        Truth.assertThat(modifiedValue.shelterType).isEqualTo(ShelterType.PARTICULAR)
    }

    @Test
    fun `WHEN clicked on image THEN photoURL is updated`() = testScope.runTest {

        // GIVEN the setup conditions
        // Get the detail state with a valid id
        sut.getShelterDetail(FakeDetailData.successId)
        advanceUntilIdle()
        // Get the detail state value
        val priorValue = (sut.detailState.value as DetailState.Success).petShelter

        // WHEN clicked onImage
        sut.onImageClicked()
        advanceUntilIdle()

        // THEN,the photoURL is updated  so the modified value id.png
        val modifiedValue = (sut.detailState.value as DetailState.Success).petShelter
        Truth.assertThat(modifiedValue).isInstanceOf(PetShelter::class.java)
        Truth.assertThat(modifiedValue.photoURL).isEqualTo("${priorValue.id}.png")
    }

    @Test
    fun `WHEN onSave THEN shelter is updated`() = testScope.runTest {

        // GIVEN the setup conditions
        // Get the detail state with a valid id
        sut.getShelterDetail(FakeDetailData.successId)
        advanceUntilIdle()

        // WHEN
        sut.onSaveClicked(context)
        advanceUntilIdle()

        // THEN, the value is updated in the fake remote data source
        val detailStateShelter = (sut.detailState.value as DetailState.Success).petShelter
        val updatedShelter = fakeRemoteDataSource.updatedShelter
        Truth.assertThat(updatedShelter).isInstanceOf(PetShelterRemote::class.java)
        Truth.assertThat(updatedShelter.id).isEqualTo(detailStateShelter.id)
    }
}
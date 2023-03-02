package com.mockknights.petshelter.tests.ui.detail

import com.google.common.truth.Truth
import com.mockknights.petshelter.data.RepositoryImpl
import com.mockknights.petshelter.data.remote.response.Address
import com.mockknights.petshelter.di.RemoteModule
import com.mockknights.petshelter.domain.PetShelter
import com.mockknights.petshelter.domain.Repository
import com.mockknights.petshelter.domain.ShelterType
import com.mockknights.petshelter.testUtils.fakeData.FakeDetailData
import com.mockknights.petshelter.testUtils.fakes.FakeRemoteDataSource
import com.mockknights.petshelter.ui.detail.DetailState
import com.mockknights.petshelter.ui.detail.DetailViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
class DetailViewModelTests {

    lateinit var ioDispatcher: CoroutineDispatcher
    lateinit var fakeRemoteDataSource: FakeRemoteDataSource
    lateinit var repository: Repository
    lateinit var sut: DetailViewModel
    lateinit var actualList: MutableList<PetShelter>
    lateinit var collectFlowJob: Job

    @Before
    fun setUp() = runTest {
        // Create repository and fake data source
        fakeRemoteDataSource = FakeRemoteDataSource()
        repository = RepositoryImpl(
            remoteDataSource = fakeRemoteDataSource,
            sharedPreferences = RemoteModule.provideSharedPreferences(RuntimeEnvironment.getApplication().baseContext),
            mapper = RemoteModule.provideMapper(),
        )
    }

    @After
    fun tearDown() {
        collectFlowJob.cancel()
    }

    @Test
    fun `WHEN getShelterDetail with correct id THEN success value is set`() = runTest {

        // GIVEN the setup conditions
        ioDispatcher = StandardTestDispatcher(testScheduler)
        sut = DetailViewModel(repository, ioDispatcher)
        println("TEST1: sut setup")
        // Create a list to collect the flow and collect it
        actualList = mutableListOf()
        collectFlowJob = launch(UnconfinedTestDispatcher(testScheduler)) {
            repository.getShelter(FakeDetailData.successId).toList(actualList)
        }

        // WHEN get the actual detailState with a valid id after the flow is collected
        collectFlowJob.join()
        println("TEST1: collection done")
        sut.getShelterDetail(FakeDetailData.successId)

        // THEN
        // The fake data source will emit the same value as the actualList
        advanceUntilIdle()
        println("TEST1: getShelterDetail with success done")
        println("TEST1: assertions reached")
        Truth.assertThat(actualList).isNotEmpty()
        // The state will be set as success
        Truth.assertThat(sut.detailState.value).isInstanceOf(DetailState.Success::class.java)
        // The state will have the same value as the actualList fake data
        Truth.assertThat((sut.detailState.value as DetailState.Success).petShelter).isEqualTo(actualList[0])
    }

    @Test
    fun `WHEN getShelterDetail with incorrect id THEN failure value is set`() = runTest {

        // GIVEN getShelterDetail with invalid id, overriding the setup conditions
        ioDispatcher = StandardTestDispatcher(testScheduler)
        sut = DetailViewModel(repository, ioDispatcher)
        println("TEST2: sut setup")
        actualList = mutableListOf()
        collectFlowJob = launch(UnconfinedTestDispatcher(testScheduler)) {
            repository.getShelter(FakeDetailData.errorId).toList(actualList)
            println("TEST2: collection done")
        }

        // WHEN
        collectFlowJob.join()
        sut.getShelterDetail(FakeDetailData.errorId)

        // THEN
        advanceUntilIdle()
        println("TEST2: getshelter with error done")
        // The fake data source will emit an empty value
        println("TEST2: assertions reached")
        Truth.assertThat(actualList).isEmpty()
        // The state will be set as error in the catch block, so we wait for the value to be set and compare
        Truth.assertThat(sut.detailState.value).isInstanceOf(DetailState.Error::class.java)
    }

    @Test
    fun `WHEN updated shelter name THEN shelter name is updated`() = runTest {

        // GIVEN the setup conditions
        ioDispatcher = StandardTestDispatcher(testScheduler)
        sut = DetailViewModel(repository, ioDispatcher)
        println("TEST3: sut setup")
        // Create a list to collect the flow and collect it
        actualList = mutableListOf()
        collectFlowJob = launch(UnconfinedTestDispatcher(testScheduler)) {
            repository.getShelter(FakeDetailData.successId).toList(actualList)
        }
        collectFlowJob.join()
        println("TEST3: collection done")
        // Get the detail state with a valid id
        sut.getShelterDetail(FakeDetailData.successId)
        advanceUntilIdle()
        println("TEST3: getshelter with valid id done")
        // Get the detail state value
        val priorValue = (sut.detailState.value as DetailState.Success).petShelter
        println("TEST3: priorValue name is ${priorValue.name}")

        // WHEN updated the shelter properties
        sut.onEditName("${priorValue.name}modified")
//            sut.onUpdatedPhone("${priorValue.name}0")
//            sut.onUpdatedAddress("0.0", "0.0")
//            sut.onUpdatedShelterType(ShelterType.PARTICULAR)
//            sut.onImageClicked()

        advanceUntilIdle()
        println("TEST3: updated shelter name done, new name = namemodified")

        // THEN, the modified value is namemodified
        val modifiedValue = (sut.detailState.value as DetailState.Success).petShelter
        println("TEST3: actual modifiedValue name is ${modifiedValue.name}")

        println("TEST3: assertions reached")
        Truth.assertThat(modifiedValue).isInstanceOf(PetShelter::class.java)
        Truth.assertThat(modifiedValue.name).isEqualTo("${priorValue.name}modified")
//        Truth.assertThat(modifiedValue.phoneNumber).isEqualTo("${priorValue.phoneNumber}0")
//        Truth.assertThat(modifiedValue.address).isEqualTo(Address(0.0, 0.0))
//        Truth.assertThat(modifiedValue.shelterType).isEqualTo(ShelterType.PARTICULAR)
//        Truth.assertThat(modifiedValue.photoURL).isEqualTo("${priorValue.id}.png")
    }
}
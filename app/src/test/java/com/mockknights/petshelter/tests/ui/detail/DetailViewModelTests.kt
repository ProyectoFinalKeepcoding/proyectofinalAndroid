package com.mockknights.petshelter.tests.ui.detail

import androidx.compose.runtime.collectAsState
import com.google.common.truth.Truth
import com.mockknights.petshelter.data.RepositoryImpl
import com.mockknights.petshelter.data.remote.response.PetShelterRemote
import com.mockknights.petshelter.di.RemoteModule
import com.mockknights.petshelter.domain.PetShelter
import com.mockknights.petshelter.domain.Repository
import com.mockknights.petshelter.testUtils.fakeData.FakeDetailData
import com.mockknights.petshelter.testUtils.fakes.FakeRemoteDataSource
import com.mockknights.petshelter.ui.detail.DetailState
import com.mockknights.petshelter.ui.detail.DetailViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
class DetailViewModelTests {

    lateinit var fakeRemoteDataSource: FakeRemoteDataSource
    lateinit var repository: Repository
    lateinit var sut: DetailViewModel

    @Before
    fun setUp() {
        fakeRemoteDataSource = FakeRemoteDataSource()
        repository = RepositoryImpl(
            remoteDataSource = fakeRemoteDataSource,
            sharedPreferences = RemoteModule.provideSharedPreferences(RuntimeEnvironment.getApplication().baseContext),
            mapper = RemoteModule.provideMapper(),
        )
        sut = DetailViewModel(repository)
    }

    @Test
    fun `WHEN getShelterDetail with correct id THEN success value is set`() = runTest {
        // GIVEN
        // WHEN
        val actualList = mutableListOf<PetShelter>()
        val collectJob = launch(UnconfinedTestDispatcher(testScheduler)) {
            repository.getShelter(FakeDetailData.successId).toList(actualList)
        }
        sut.getShelterDetail(FakeDetailData.successId)
        // THEN
        // The fake data source will emit the same value as the actualList
        Truth.assertThat(actualList).isNotEmpty()
        // The state will be set as success
        Truth.assertThat(sut.detailState.value).isInstanceOf(DetailState.Success::class.java)
        // The state will have the same value as the actualList fake data
        Truth.assertThat((sut.detailState.value as DetailState.Success).petShelter).isEqualTo(actualList[0])
        // FINALLY
        collectJob.cancel()
    }

    @Test
    fun `WHEN getShelterDetail with incorrect id THEN failure value is set`() = runTest {
        // GIVEN
        // WHEN
        val actualList = mutableListOf<PetShelter>()
        val collectJob = launch(UnconfinedTestDispatcher(testScheduler)) {
            repository.getShelter(FakeDetailData.errorId).toList(actualList)
        }
        sut.getShelterDetail(FakeDetailData.errorId)
        // THEN
        // The fake data source will emit an empty value
        Truth.assertThat(actualList).isEmpty()
        // The state will be set as error in the catch block
        Truth.assertThat(sut.detailState.value).isInstanceOf(DetailState.Error::class.java)
        // FINALLY
        collectJob.cancel()
    }
}
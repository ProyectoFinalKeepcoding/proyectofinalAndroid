package com.mockknights.petshelter.tests.ui.map

import com.google.common.truth.Truth
import com.mockknights.petshelter.data.RepositoryImpl
import com.mockknights.petshelter.di.CoroutinesModule
import com.mockknights.petshelter.di.RemoteModule
import com.mockknights.petshelter.domain.PetShelter
import com.mockknights.petshelter.domain.Repository
import com.mockknights.petshelter.testUtils.fakes.FakeRemoteDataSource
import com.mockknights.petshelter.ui.map.MapViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
class MapViewModelTests {
    private val testScheduler = TestCoroutineScheduler()
    private val testDispatcher = StandardTestDispatcher(testScheduler)
    private val testScope = TestScope(testDispatcher)
    private lateinit var fakeRemoteDataSource: FakeRemoteDataSource
    private lateinit var repository: Repository
    private lateinit var sut: MapViewModel

    @Before
    fun setUp() {
        // Create repository and fake data source
        fakeRemoteDataSource = FakeRemoteDataSource()
        repository = RepositoryImpl(
            remoteDataSource = fakeRemoteDataSource,
            sharedPreferences = RemoteModule.provideSharedPreferences(RuntimeEnvironment.getApplication().baseContext),
            mapper = RemoteModule.provideMapper(),
        )
        // Create the SUT
//        sut = MapViewModel(repository, CoroutinesModule.provideIOCoroutineDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `WHEN init sut THEN a list of petShelter is set in the state`() = testScope.runTest {
        // GIVEN the setup conditions
        // Emulate the main dispatcher with the test dispatcher
        val dispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(dispatcher)

        // WHEN init sut
        sut = MapViewModel(repository, CoroutinesModule.provideIOCoroutineDispatcher())
        advanceUntilIdle()

        // THEN a list of petShelter is set in the state
        Truth.assertThat(sut.petShelters.value).isNotEmpty()
        Truth.assertThat((sut.petShelters.value).first()).isInstanceOf(PetShelter::class.java)
    }
}
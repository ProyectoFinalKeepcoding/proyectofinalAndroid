package com.mockknights.petshelter.tests.ui.map

import android.content.Context
import com.mockknights.petshelter.R
import com.google.common.truth.Truth
import com.mockknights.petshelter.data.RepositoryImpl
import com.mockknights.petshelter.data.remote.mappers.PetShelterMapper
import com.mockknights.petshelter.di.RemoteModule
import com.mockknights.petshelter.domain.PetShelter
import com.mockknights.petshelter.domain.Repository
import com.mockknights.petshelter.domain.ShelterType
import com.mockknights.petshelter.testUtils.fakeData.FakeMapData
import com.mockknights.petshelter.testUtils.fakes.FakeRemoteDataSource
import com.mockknights.petshelter.ui.map.MapShelterListState
import com.mockknights.petshelter.ui.map.MapViewModel
import kotlinx.coroutines.*
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
    private lateinit var context: Context
    private lateinit var fakeRemoteDataSource: FakeRemoteDataSource
    private lateinit var repository: Repository
    private lateinit var sut: MapViewModel

    @Before
    fun setUp() {
        // Init context
        context = RuntimeEnvironment.getApplication().baseContext
        // Create repository and fake data source
        fakeRemoteDataSource = FakeRemoteDataSource()
        repository = RepositoryImpl(
            remoteDataSource = fakeRemoteDataSource,
            sharedPreferences = context.getSharedPreferences("NAME", Context.MODE_PRIVATE),
            mapper = RemoteModule.provideMapper(),
        )
        // Init sut
        sut = MapViewModel(repository, testDispatcher)
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

        // WHEN init sut with valid data
        advanceUntilIdle() // Wait until the sut is initialized and the data is fetched

        // THEN a list of petShelter is set in the state
        Truth.assertThat((sut.mapShelterListState.value as MapShelterListState.Success).petShelters).isNotEmpty()
        Truth.assertThat((sut.mapShelterListState.value as MapShelterListState.Success).petShelters.first()).isInstanceOf(PetShelter::class.java)
    }

    @Test
    fun `WHEN init sut with error THEN the state is set to error`() = testScope.runTest {
        // GIVEN the setup conditions
        // Emulate the main dispatcher with the test dispatcher
        val dispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(dispatcher)

        // WHEN init sut with invalid data (empty array)
        fakeRemoteDataSource.returnValidPetShelterList = false
        sut = MapViewModel(repository, testDispatcher)
        advanceUntilIdle()

        // THEN an exception is thrown and error state is set
        Truth.assertThat((sut.mapShelterListState.value)).isInstanceOf(MapShelterListState.Error::class.java)
        Truth.assertThat((sut.mapShelterListState.value as MapShelterListState.Error).message).isEqualTo("Empty pet shelter list")
    }

    @Test
    fun `WHEN setModalShelter with existing shelter name THEN the shelter is set`() = testScope.runTest {
        // GIVEN the setup conditions
        // Emulate the main dispatcher with the test dispatcher
        val dispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(dispatcher)
        advanceUntilIdle() // Wait until the sut is initialized and the data is fetched
        Truth.assertThat(sut.modalShelterList.value).isEmpty()

        // WHEN init modal shelter is set
        sut.setModalShelter(FakeMapData.getPetShelterList().first().name)

        // THEN, the shelter is set
        Truth.assertThat(sut.modalShelterList.value).isNotEmpty()
        Truth.assertThat(sut.modalShelterList.value.first().name).isEqualTo(FakeMapData.getPetShelterList().first().name)
    }

    @Test
    fun `WHEN setModalShelter with non existing shelter name THEN the shelter remains empty`() = testScope.runTest {
        // GIVEN the setup conditions
        // Emulate the main dispatcher with the test dispatcher
        val dispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(dispatcher)
        advanceUntilIdle() // Wait until the sut is initialized and the data is fetched
        Truth.assertThat(sut.modalShelterList.value).isEmpty()

        // WHEN init modal shelter is set with invalid name
        sut.setModalShelter("${FakeMapData.getPetShelterList().first().name}modifiedToNonExistingName")

        // THEN, the modal shelter is not set
        Truth.assertThat(sut.modalShelterList.value).isEmpty()
    }

    @Test
    fun `WHEN getShelterIconByShelterType THEN the proper icon is returned `() = testScope.runTest {
        // GIVEN the setup conditions
        // Emulate the main dispatcher with the test dispatcher
        val dispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(dispatcher)
        advanceUntilIdle() // Wait until the sut is initialized and the data is fetched

        // WHEN getShelterIconByShelterType is called with different shelter types
        val particular = sut.getShelterIconByShelterType(ShelterType.PARTICULAR.stringValue)
        val localGovernment = sut.getShelterIconByShelterType(ShelterType.LOCAL_GOVERNMENT.stringValue)
        val veterinary = sut.getShelterIconByShelterType(ShelterType.VETERINARY.stringValue)
        val shelterPoint = sut.getShelterIconByShelterType(ShelterType.SHELTER_POINT.stringValue)
        val kiwokoStore = sut.getShelterIconByShelterType(ShelterType.KIWOKO_STORE.stringValue)
        val questionMark = sut.getShelterIconByShelterType("")


        // THEN, the proper icon is returned
        Truth.assertThat(particular).isEqualTo(R.drawable.particular)
        Truth.assertThat(localGovernment).isEqualTo(R.drawable.towncouncil)
        Truth.assertThat(veterinary).isEqualTo(R.drawable.veterinary)
        Truth.assertThat(shelterPoint).isEqualTo(R.drawable.animalshelter)
        Truth.assertThat(kiwokoStore).isEqualTo(R.drawable.kiwoko)
        Truth.assertThat(questionMark).isEqualTo(R.drawable.questionmark)
    }

    @Test
    fun `WHEN onPermissionRequestCompleted with granted permission THEN locationPermission is granted`() = testScope.runTest {
        // GIVEN the setup conditions
        // Emulate the main dispatcher with the test dispatcher
        val dispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(dispatcher)
        advanceUntilIdle() // Wait until the sut is initialized and the data is fetched
        Truth.assertThat(sut.locationPermissionGranted.value).isFalse()

        // WHEN toggle modal
        sut.onPermissionRequestCompleted(true, context)

        // THEN, the modal is expanded
        Truth.assertThat(sut.locationPermissionGranted.value).isTrue()
    }

    @Test
    fun `WHEN getClosestShelter THEN closest shelter is returned`() = testScope.runTest {
        // GIVEN the setup conditions
        // Emulate the main dispatcher with the test dispatcher
        val dispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(dispatcher)
        advanceUntilIdle() // Wait until the sut is initialized and the data is fetched

        // WHEN getClosestShelter is called with granted permission
        sut.locationPermissionGranted.value = true
        val closestShelter = sut.getClosestShelter()

        // THEN, the closest shelter is returned (Joakin)
        Truth.assertThat(closestShelter)
            .isEqualTo(PetShelterMapper().mapOnePetShelterRemoteToPresentation(FakeMapData.getPetShelterList()[3]))
    }
}
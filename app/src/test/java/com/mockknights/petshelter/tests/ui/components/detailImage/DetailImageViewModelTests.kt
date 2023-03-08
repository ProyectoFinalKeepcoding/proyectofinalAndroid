package com.mockknights.petshelter.tests.ui.components.detailImage

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import com.google.common.truth.Truth
import com.mockknights.petshelter.data.RepositoryImpl
import com.mockknights.petshelter.R
import com.mockknights.petshelter.di.RemoteModule
import com.mockknights.petshelter.domain.Repository
import com.mockknights.petshelter.testUtils.fakeData.FakeDetailData
import com.mockknights.petshelter.testUtils.fakeData.FakePetShelterData
import com.mockknights.petshelter.testUtils.fakes.FakeRemoteDataSource
import com.mockknights.petshelter.ui.components.detailImage.DetailImageViewModel
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
class DetailImageViewModelTests {
    private val testScheduler = TestCoroutineScheduler()
    private val testDispatcher = StandardTestDispatcher(testScheduler)
    private val testScope = TestScope(testDispatcher)
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var context: Context
    private lateinit var fakeRemoteDataSource: FakeRemoteDataSource
    private lateinit var repository: Repository
    private lateinit var sut: DetailImageViewModel

    @Before
    fun setUp() {
        // Init context
        context = RuntimeEnvironment.getApplication().baseContext
        sharedPreferences = context.getSharedPreferences("NAME", Context.MODE_PRIVATE)
        // Create repository and fake data source
        fakeRemoteDataSource = FakeRemoteDataSource()
        repository = RepositoryImpl(
            remoteDataSource = fakeRemoteDataSource,
            sharedPreferences = sharedPreferences,
            mapper = RemoteModule.provideMapper(),
        )
        // Init sut
        sut = DetailImageViewModel(repository, sharedPreferences, testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `WHEN onSelectedImage with token THEN photo is uploaded`() = testScope.runTest {
        // GIVEN the setup conditions
        // Emulate the main dispatcher with the test dispatcher
        val dispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(dispatcher)
        // Set token into shared preferences

        // WHEN
        sharedPreferences.edit().putString("TOKEN", "token").apply()
        sut.onSelectedImage(
            uri = Uri.parse("android.resource://com.mockknights.petshelter/${R.drawable.person_image}"),
            shelterId = FakeDetailData.successId,
            localContext = context
        )
        advanceUntilIdle()

        // THEN
        Truth.assertThat(fakeRemoteDataSource.id).isEqualTo(FakeDetailData.successId)
        Truth.assertThat(fakeRemoteDataSource.photo).isNotNull()
    }

}

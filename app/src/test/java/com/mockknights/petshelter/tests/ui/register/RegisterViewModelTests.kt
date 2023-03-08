package com.mockknights.petshelter.tests.ui.register

import android.content.Context
import com.google.common.truth.Truth
import com.mockknights.petshelter.data.RepositoryImpl
import com.mockknights.petshelter.di.RemoteModule
import com.mockknights.petshelter.domain.Repository
import com.mockknights.petshelter.testUtils.fakeData.FakeRegisterData
import com.mockknights.petshelter.testUtils.fakes.FakeRemoteDataSource
import com.mockknights.petshelter.ui.register.RegisterState
import com.mockknights.petshelter.ui.register.RegisterViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class RegisterViewModelTests {

    private val testScheduler = TestCoroutineScheduler()
    private val testDispatcher = StandardTestDispatcher(testScheduler)
    private val testScope = TestScope(testDispatcher)
    private lateinit var fakeRemoteDataSource: FakeRemoteDataSource
    private lateinit var repository: Repository
    private lateinit var sut: RegisterViewModel

    @Before
    fun setUp() {
        // Create repository and fake data source
        fakeRemoteDataSource = FakeRemoteDataSource()
        val context = RuntimeEnvironment.getApplication().baseContext
        repository = RepositoryImpl(
            remoteDataSource = fakeRemoteDataSource,
            sharedPreferences = context.getSharedPreferences("NAME", Context.MODE_PRIVATE),
            mapper = RemoteModule.provideMapper(),
        )
        // Create the SUT
        sut = RegisterViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `WHEN register with valid register request THEN stateRegister updates to Success`() = testScope.runTest {
        // GIVEN the setup conditions
        // Emulate the main dispatcher with the test dispatcher
        val dispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(dispatcher)

        // WHEN register with valid user and password
        sut.register(FakeRegisterData.getRegisterRequest(true))

        // THEN stateRegister updates to Success
        Truth.assertThat(sut.registerState.value).isInstanceOf(RegisterState.Success::class.java)
    }

    @Test
    fun `WHEN register with invalid register request THEN stateRegister updates to Failure`() = testScope.runTest {
        // GIVEN the setup conditions
        // Emulate the main dispatcher with the test dispatcher
        val dispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(dispatcher)

        // WHEN register with valid user and password
        sut.register(FakeRegisterData.getRegisterRequest(false))

        // THEN stateRegister updates to Success
        Truth.assertThat(sut.registerState.value).isInstanceOf(RegisterState.Failure::class.java)
        Truth.assertThat((sut.registerState.value as RegisterState.Failure).error)
            .isEqualTo("ERROR EN EL REGISTRO") // Thrown error from FakeRemoteDataSource
    }
}
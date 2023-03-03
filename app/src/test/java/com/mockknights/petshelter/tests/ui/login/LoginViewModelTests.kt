package com.mockknights.petshelter.tests.ui.login

import com.google.common.truth.Truth
import com.mockknights.petshelter.data.RepositoryImpl
import com.mockknights.petshelter.di.CoroutinesModule
import com.mockknights.petshelter.di.RemoteModule
import com.mockknights.petshelter.domain.Repository
import com.mockknights.petshelter.testUtils.fakeData.FakeLoginData
import com.mockknights.petshelter.testUtils.fakes.FakeRemoteDataSource
import com.mockknights.petshelter.ui.login.LoginState
import com.mockknights.petshelter.ui.login.LoginViewModel
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
class LoginViewModelTests {

    private val testScheduler = TestCoroutineScheduler()
    private val testDispatcher = StandardTestDispatcher(testScheduler)
    private val testScope = TestScope(testDispatcher)
    private lateinit var fakeRemoteDataSource: FakeRemoteDataSource
    private lateinit var repository: Repository
    private lateinit var sut: LoginViewModel


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
        sut = LoginViewModel(repository, RemoteModule.provideSharedPreferences(RuntimeEnvironment.getApplication().baseContext), CoroutinesModule.provideIOCoroutineDispatcher())
    }

    @After
    fun tearDown() {

    }

    @Test
    fun `WHEN getToken with correct user and password THEN stateLogin updates to Success`() = testScope.runTest {
        // GIVEN the setup conditions
        // Emulate the main dispatcher with the test dispatcher
        val dispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(dispatcher)

        // WHEN getToken with correct user and password
        fakeRemoteDataSource.returnValidToken = true
        sut.getToken(FakeLoginData.validUser, FakeLoginData.validPassword)
        advanceUntilIdle()

        // THEN, the state will be set as success
        val loginState = sut.stateLogin.value
        Truth.assertThat(sut.stateLogin.value).isInstanceOf(LoginState.Success::class.java)
        Truth.assertThat((loginState as LoginState.Success).token).isEqualTo(FakeLoginData.token)
        Truth.assertThat((loginState).id).isEqualTo(FakeLoginData.id)
    }

    @Test
    fun `WHEN getToken with invalid user and password THEN stateLogin updates to Failure`() = testScope.runTest {
        // GIVEN the setup conditions
        // Emulate the main dispatcher with the test dispatcher
        val dispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(dispatcher)

        // WHEN getToken any invalid user and password
        fakeRemoteDataSource.returnValidToken = false
        sut.getToken("anyinvalidstring", "anyinvalidstring")
        advanceUntilIdle()

        // THEN, the state will be set as failure, and the message is the one of the exception
        val loginState = sut.stateLogin.value
        Truth.assertThat(sut.stateLogin.value).isInstanceOf(LoginState.Failure::class.java)
        Truth.assertThat((loginState as LoginState.Failure).error).isEqualTo("No token found")
    }

    @Test
    fun `WHEN resetState THEN stateLogin updates to Loading`() = testScope.runTest {
        // GIVEN the setup conditions
        // Emulate the main dispatcher with the test dispatcher
        val dispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(dispatcher)
        // GetToken any invalid user and password
        fakeRemoteDataSource.returnValidToken = false
        sut.getToken("anyinvalidstring", "anyinvalidstring")
        advanceUntilIdle()
        // the state will be set as failure, and the message is the one of the exception
        val loginState = sut.stateLogin.value
        Truth.assertThat(sut.stateLogin.value).isInstanceOf(LoginState.Failure::class.java)
        Truth.assertThat((loginState as LoginState.Failure).error).isEqualTo("No token found")

        // WHEN called reset state
        sut.resetState()
        advanceUntilIdle()

        // THEN. the state will be Loading
        Truth.assertThat(sut.stateLogin.value).isInstanceOf(LoginState.Loading::class.java)
    }
}
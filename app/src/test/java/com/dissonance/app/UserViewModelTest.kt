//package com.dissonance.app
//
//import com.dissonance.app.data.UserRepository
//import com.dissonance.app.data.model.User
//import org.junit.Before
//import org.junit.Rule
//import org.junit.Test
//import org.junit.runner.RunWith
//import org.mockito.Mock
//import org.mockito.Mockito.*
//import org.mockito.junit.MockitoJUnitRunner
//import org.junit.Assert.*
//
//@RunWith(MockitoJUnitRunner::class)
//class UserViewModelTest {
//
//    // This rule ensures LiveData updates happen immediately in tests
//    @get:Rule
//    val instantTaskExecutorRule = InstantTaskExecutorRule()
//
//    @Mock
//    private lateinit var repository: UserRepository
//
//    private lateinit var viewModel: UserViewModel
//
//    private val testUser = User(
//        id = "user123",
//        username = "testUser",
//        email = "test@example.com",
//        aboutMe = "This is a test user",
//        // Add other required fields based on your User model
//    )
//
//    @Before
//    fun setup() {
//        // Create a custom version of the ViewModel that uses our mocked repository
//        viewModel = object : UserViewModel() {
//            // Override the repository property to use our mocked version
//            override val repository: UserRepository = this@UserViewModelTest.repository
//        }
//    }
//
//    @Test
//    fun `fetchUser sets user object correctly`() {
//        // Arrange
//        val userId = "user123"
//        // Mock the repository response
//        doAnswer { invocation ->
//            val callback = invocation.arguments[1] as (User?) -> Unit
//            callback(testUser)
//            null
//        }.`when`(repository).getUser(eq(userId), any())
//
//        // Act
//        viewModel.fetchUser(userId)
//
//        // Assert
//        assertEquals(testUser, viewModel.userObjObserve.value)
//    }
//
//    @Test
//    fun `updateUserName updates username when successful`() {
//        // Arrange
//        val userId = "user123"
//        val newName = "newUserName"
//
//        // Set initial user value
//        viewModel.fetchUser(userId)  // This will trigger the mocked response above
//
//        // Mock the repository response for updateUserName
//        doAnswer { invocation ->
//            val callback = invocation.arguments[2] as (Boolean) -> Unit
//            callback(true)  // Simulate successful update
//            null
//        }.`when`(repository).updateUserName(eq(userId), eq(newName), any())
//
//        // Act
//        viewModel.updateUserName(userId, newName)
//
//        // Assert
//        assertEquals(newName, viewModel.userObjObserve.value?.username)
//    }
//
//    @Test
//    fun `updateUserName does not update username when unsuccessful`() {
//        // Arrange
//        val userId = "user123"
//        val originalName = "testUser"
//        val newName = "newUserName"
//
//        // Set initial user value
//        viewModel.fetchUser(userId)  // This will trigger the mocked response above
//
//        // Mock the repository response for updateUserName
//        doAnswer { invocation ->
//            val callback = invocation.arguments[2] as (Boolean) -> Unit
//            callback(false)  // Simulate failed update
//            null
//        }.`when`(repository).updateUserName(eq(userId), eq(newName), any())
//
//        // Act
//        viewModel.updateUserName(userId, newName)
//
//        // Assert
//        assertEquals(originalName, viewModel.userObjObserve.value?.username)
//    }
//
//    @Test
//    fun `updateUserEmail updates email when successful`() {
//        // Arrange
//        val userId = "user123"
//        val newEmail = "new@example.com"
//
//        // Set initial user value
//        viewModel.fetchUser(userId)
//
//        // Mock the repository response
//        doAnswer { invocation ->
//            val callback = invocation.arguments[2] as (Boolean) -> Unit
//            callback(true)  // Simulate successful update
//            null
//        }.`when`(repository).updateUserEmail(eq(userId), eq(newEmail), any())
//
//        // Act
//        viewModel.updateUserEmail(userId, newEmail)
//
//        // Assert
//        assertEquals(newEmail, viewModel.userObjObserve.value?.email)
//    }
//
//    @Test
//    fun `updateUserAboutMe updates aboutMe when successful`() {
//        // Arrange
//        val userId = "user123"
//        val newAboutMe = "This is my updated about me"
//
//        // Set initial user value
//        viewModel.fetchUser(userId)
//
//        // Mock the repository response
//        doAnswer { invocation ->
//            val callback = invocation.arguments[2] as (Boolean) -> Unit
//            callback(true)  // Simulate successful update
//            null
//        }.`when`(repository).updateUserAboutMe(eq(userId), eq(newAboutMe), any())
//
//        // Act
//        viewModel.updateUserAboutMe(userId, newAboutMe)
//
//        // Assert
//        assertEquals(newAboutMe, viewModel.userObjObserve.value?.aboutMe)
//    }
//}
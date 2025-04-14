//package com.dissonance.app
//
//import androidx.arch.core.executor.testing.InstantTaskExecutorRule
//import com.dissonance.app.data.model.Review
//import com.dissonance.app.viewmodel.ReviewViewModel
//import com.google.firebase.firestore.FirebaseFirestore
//import com.google.firebase.firestore.QuerySnapshot
//import org.junit.Before
//import org.junit.Rule
//import org.junit.Test
//import org.junit.Assert.*
//import org.junit.runner.RunWith
//import org.mockito.Mock
//import org.mockito.Mockito.*
//import org.mockito.junit.MockitoJUnitRunner
//import com.google.firebase.firestore.Query
//import com.google.firebase.firestore.DocumentSnapshot
//import com.google.android.gms.tasks.Task
//import com.google.android.gms.tasks.Tasks
//import com.google.firebase.firestore.CollectionReference
//
//@RunWith(MockitoJUnitRunner::class)
//class ReviewViewModelTest {
//
//    @get:Rule
//    val instantTaskExecutorRule = InstantTaskExecutorRule()
//
//    @Mock
//    private lateinit var mockFirestore: FirebaseFirestore
//
//    @Mock
//    private lateinit var mockCollectionReference: CollectionReference
//
//    @Mock
//    private lateinit var mockQuery: Query
//
//    @Mock
//    private lateinit var mockTask: Task<QuerySnapshot>
//
//    @Mock
//    private lateinit var mockQuerySnapshot: QuerySnapshot
//
//    private lateinit var viewModel: ReviewViewModel
//
//    @Before
//    fun setup() {
//        // Set up the chain of mocks for Firestore
//        `when`(mockFirestore.collection("reviews")).thenReturn(mockCollectionReference)
//        `when`(mockCollectionReference.orderBy("timestamp", Query.Direction.DESCENDING)).thenReturn(mockQuery)
//        `when`(mockQuery.limit(anyLong())).thenReturn(mockQuery)
//        `when`(mockQuery.get()).thenReturn(mockTask)
//
//        viewModel = ReviewViewModel(mockFirestore)
//    }
//    // TODO error
//    @Test
//    fun `getRecentReviews fetches reviews successfully`() {
//        // Create mock data
//        val mockReviews = listOf(
//            Review(userId = "1", albumTitle = "Review 1", rating = 4),
//            Review(userId = "2", albumTitle = "Review 2", rating = 5)
//        )
//
//        // Mock documents
//        val mockDocuments = mockReviews.map { review ->
//            mock(DocumentSnapshot::class.java).apply {
//                `when`(toObject(Review::class.java)).thenReturn(review)
//            }
//        }
//
//        // Set up the successful task result
//        `when`(mockTask.isSuccessful).thenReturn(true)
//        `when`(mockQuerySnapshot.documents).thenReturn(mockDocuments)
//        `when`(mockTask.result).thenReturn(mockQuerySnapshot)
//
//        // Use Tasks.forResult to create a completed task
//        `when`(mockQuery.get()).thenReturn(Tasks.forResult(mockQuerySnapshot))
//
//        // Call the method
//        viewModel.getRecentReviews(2)
//
//        // Assert the result
//        assertEquals(mockReviews, viewModel.reviewListObserve.value)
//    }
//}

package com.dissonance.app

import com.dissonance.app.data.model.Review
import com.dissonance.app.viewmodel.ReviewViewModel
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import com.google.firebase.firestore.FirebaseFirestore
import org.junit.Assert.*

@RunWith(MockitoJUnitRunner::class)
class ReviewViewModelTest {

    @Mock
    private lateinit var mockFirestore: FirebaseFirestore

    private lateinit var viewModel: ReviewViewModel

    @Before
    fun setup() {
        viewModel = ReviewViewModel(mockFirestore)
    }

    @Test
    fun `validateReview returns true for valid review`() {
        val validReview = Review(
            userId = "user123",
            username = "testUser",
            albumId = "album123",
            albumCoverUrl = "https://example.com/cover.jpg",
            albumTitle = "Test Album",
            artistName = "Test Artist",
            rating = 5,
            reviewTitle = "Great Album",
            reviewText = "This was a great experience!",
            timestamp = System.currentTimeMillis()
        )

        val result = viewModel.validateReview(validReview)
        assertTrue("Valid review should return true", result)
    }

    @Test
    fun `validateReview returns false when userId is empty`() {
        val invalidReview = Review(
            userId = "",
            username = "testUser",
            albumId = "album123",
            albumCoverUrl = "https://example.com/cover.jpg",
            albumTitle = "Test Album",
            artistName = "Test Artist",
            rating = 5,
            reviewTitle = "Great Album",
            reviewText = "This was a great experience!",
            timestamp = System.currentTimeMillis()
        )

        val result = viewModel.validateReview(invalidReview)
        assertFalse("Review with empty userId should return false", result)
    }


    @Test
    fun `validateReview returns false when reviewText is too long`() {
        val longText = "a".repeat(9001)
        val invalidReview = Review(
            userId = "user123",
            username = "testUser",
            albumId = "album123",
            albumCoverUrl = "https://example.com/cover.jpg",
            albumTitle = "Test Album",
            artistName = "Test Artist",
            rating = 5,
            reviewTitle = "Great Album",
            reviewText = longText,
            timestamp = System.currentTimeMillis()
        )

        val result = viewModel.validateReview(invalidReview)
        assertFalse("Review with reviewText more than 500 chars should return false", result)
    }

    @Test
    fun `validateReview returns false when rating is below 1`() {
        val invalidReview = Review(
            userId = "user123",
            username = "testUser",
            albumId = "album123",
            albumCoverUrl = "https://example.com/cover.jpg",
            albumTitle = "Test Album",
            artistName = "Test Artist",
            rating = 0,
            reviewTitle = "Terrible Album",
            reviewText = "This was a terrible experience!",
            timestamp = System.currentTimeMillis()
        )

        val result = viewModel.validateReview(invalidReview)
        assertFalse("Review with rating below 1 should return false", result)
    }

    @Test
    fun `validateReview returns false when rating is above 12`() {
        val invalidReview = Review(
            userId = "user123",
            username = "testUser",
            albumId = "album123",
            albumCoverUrl = "https://example.com/cover.jpg",
            albumTitle = "Test Album",
            artistName = "Test Artist",
            rating = 12,
            reviewTitle = "Amazing Album",
            reviewText = "This was an amazing experience!",
            timestamp = System.currentTimeMillis()
        )

        val result = viewModel.validateReview(invalidReview)
        assertFalse("Review with rating above 5 should return false", result)
    }

    @Test
    fun `calculateAverageRating returns correct average`() {
        val reviews = listOf(
            Review(userId = "user1", rating = 3, reviewText = "Review 1"),
            Review(userId = "user2", rating = 4, reviewText = "Review 2"),
            Review(userId = "user3", rating = 5, reviewText = "Review 3")
        )

        val average = viewModel.calculateAverageRating(reviews)
        assertEquals("Average should be 4.0", 4.0f, average, 0.001f)
    }

    @Test
    fun `calculateAverageRating returns 0 for empty list`() {
        val average = viewModel.calculateAverageRating(emptyList())
        assertEquals("Average for empty list should be 0.0", 0.0f, average, 0.001f)
    }
}
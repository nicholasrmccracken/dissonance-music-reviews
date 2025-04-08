package com.dissonance.app

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dissonance.app.data.model.Review
import com.dissonance.app.viewmodel.ReviewViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.DocumentSnapshot
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.CollectionReference

@RunWith(MockitoJUnitRunner::class)
class ReviewViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var mockFirestore: FirebaseFirestore

    @Mock
    private lateinit var mockCollectionReference: CollectionReference

    @Mock
    private lateinit var mockQuery: Query

    @Mock
    private lateinit var mockTask: Task<QuerySnapshot>

    @Mock
    private lateinit var mockQuerySnapshot: QuerySnapshot

    private lateinit var viewModel: ReviewViewModel

    @Before
    fun setup() {
        // Set up the chain of mocks for Firestore
        `when`(mockFirestore.collection("reviews")).thenReturn(mockCollectionReference)
        `when`(mockCollectionReference.orderBy("timestamp", Query.Direction.DESCENDING)).thenReturn(mockQuery)
        `when`(mockQuery.limit(anyLong())).thenReturn(mockQuery)
        `when`(mockQuery.get()).thenReturn(mockTask)

        viewModel = ReviewViewModel(mockFirestore)
    }

    @Test
    fun `getRecentReviews fetches reviews successfully`() {
        // Create mock data
        val mockReviews = listOf(
            Review(id = "1", title = "Review 1", content = "Good album", rating = 4),
            Review(id = "2", title = "Review 2", content = "Great album", rating = 5)
        )

        // Mock documents
        val mockDocuments = mockReviews.map { review ->
            mock(DocumentSnapshot::class.java).apply {
                `when`(toObject(Review::class.java)).thenReturn(review)
            }
        }

        // Set up the successful task result
        `when`(mockTask.isSuccessful).thenReturn(true)
        `when`(mockQuerySnapshot.documents).thenReturn(mockDocuments)
        `when`(mockTask.result).thenReturn(mockQuerySnapshot)

        // Use Tasks.forResult to create a completed task
        `when`(mockQuery.get()).thenReturn(Tasks.forResult(mockQuerySnapshot))

        // Call the method
        viewModel.getRecentReviews(2)

        // Assert the result
        assertEquals(mockReviews, viewModel.reviewListObserve.value)
    }
}
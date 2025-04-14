package com.dissonance.app.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dissonance.app.data.model.Review
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class ReviewPagingSource(private val db: FirebaseFirestore) : PagingSource<DocumentSnapshot, Review>() {

    override suspend fun load(params: LoadParams<DocumentSnapshot>): LoadResult<DocumentSnapshot, Review> {
        return try {
            val baseQuery = db.collection("reviews")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(params.loadSize.toLong())

            val query = params.key?.let {
                baseQuery.startAfter(it)
            } ?: baseQuery

            val snapshot = query.get().await()
            val reviews = snapshot.documents.mapNotNull { it.toObject(Review::class.java) }

            val nextKey = snapshot.documents.lastOrNull()

            LoadResult.Page(
                data = reviews,
                prevKey = null,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<DocumentSnapshot, Review>): DocumentSnapshot? = null
}
package com.dissonance.app.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dissonance.app.data.model.CommunityStats
import com.dissonance.app.data.model.DiscogSearchModel
import com.dissonance.app.data.model.Pagination
import com.dissonance.app.data.model.PaginationUrls
import com.dissonance.app.data.model.ReleaseResult
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SharedDiscogsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: SharedDiscogsViewModel

    @Before
    fun setup() {
        viewModel = SharedDiscogsViewModel()
    }

    // Tests for validateSearchParams

    @Test
    fun `validateSearchParams returns false for blank query`() {
        assertFalse(viewModel.validateSearchParams("", "Artist"))
    }

    @Test
    fun `validateSearchParams returns false for blank artist`() {
        assertFalse(viewModel.validateSearchParams("Query", ""))
    }

    @Test
    fun `validateSearchParams returns false for blank query and artist`() {
        assertFalse(viewModel.validateSearchParams("", ""))
    }

    @Test
    fun `validateSearchParams returns true for valid inputs`() {
        assertTrue(viewModel.validateSearchParams("Query", "Artist"))
    }

    @Test
    fun `validateSearchParams returns false for whitespace only inputs`() {
        assertFalse(viewModel.validateSearchParams("   ", "Artist"))
        assertFalse(viewModel.validateSearchParams("Query", "   "))
    }

    // Tests for filterValidAlbumPairs

    @Test
    fun `filterValidAlbumPairs removes empty pairs`() {
        val albums = listOf(
            Pair("Album1", "Artist1"),
            Pair("", "Artist2"),
            Pair("Album3", ""),
            Pair("Album4", "Artist4")
        )

        val filtered = viewModel.filterValidAlbumPairs(albums)
        assertEquals(2, filtered.size)
        assertEquals("Album1", filtered[0].first)
        assertEquals("Album4", filtered[1].first)
    }

    @Test
    fun `filterValidAlbumPairs returns empty list for all invalid inputs`() {
        val albums = listOf(
            Pair("", ""),
            Pair("", "Artist2"),
            Pair("Album3", "")
        )

        val filtered = viewModel.filterValidAlbumPairs(albums)
        assertTrue(filtered.isEmpty())
    }

    @Test
    fun `filterValidAlbumPairs handles whitespace only inputs`() {
        val albums = listOf(
            Pair("Album1", "Artist1"),
            Pair("   ", "Artist2"),
            Pair("Album3", "   ")
        )

        val filtered = viewModel.filterValidAlbumPairs(albums)
        assertEquals(1, filtered.size)
        assertEquals("Album1", filtered[0].first)
    }

    @Test
    fun `filterValidAlbumPairs returns empty list for empty input`() {
        val filtered = viewModel.filterValidAlbumPairs(emptyList())
        assertTrue(filtered.isEmpty())
    }

    // Tests for getMostCommonGenre

    @Test
    fun `getMostCommonGenre returns most frequent genre`() {
        // Create pagination object
        val pagination = Pagination(
            perPage = 10,
            pages = 1,
            page = 1,
            items = 4,
            urls = PaginationUrls(
                last = null,
                next = null
            )
        )

        // Create release results with various genres
        val releaseResults1 = listOf(
            createReleaseResult(id = 1, genres = listOf("Rock", "Alternative")),
            createReleaseResult(id = 2, genres = listOf("Rock", "Pop"))
        )

        val releaseResults2 = listOf(
            createReleaseResult(id = 3, genres = listOf("Jazz", "Blues")),
            createReleaseResult(id = 4, genres = listOf("Rock"))
        )

        // Create DiscogSearchModel objects
        val result1 = DiscogSearchModel(
            pagination = pagination,
            results = releaseResults1
        )

        val result2 = DiscogSearchModel(
            pagination = pagination,
            results = releaseResults2
        )

        // Create batch results map
        val batchResults = mapOf(
            Pair("Album1", "Artist1") to result1,
            Pair("Album2", "Artist2") to result2
        )

        assertEquals("Rock", viewModel.getMostCommonGenre(batchResults))
    }

    @Test
    fun `getMostCommonGenre returns null for empty results`() {
        val emptyResults = mapOf<Pair<String, String>, DiscogSearchModel>()
        assertNull(viewModel.getMostCommonGenre(emptyResults))
    }

    @Test
    fun `getMostCommonGenre returns null when no genres exist`() {
        val pagination = Pagination(
            perPage = 10,
            pages = 1,
            page = 1,
            items = 2,
            urls = null
        )

        val releaseResults = listOf(
            createReleaseResult(id = 1, genres = null),
            createReleaseResult(id = 2, genres = emptyList())
        )

        val result = DiscogSearchModel(
            pagination = pagination,
            results = releaseResults
        )

        val batchResults = mapOf(
            Pair("Album1", "Artist1") to result
        )

        assertNull(viewModel.getMostCommonGenre(batchResults))
    }

    @Test
    fun `getMostCommonGenre handles case with multiple equally common genres`() {
        // In case of a tie, the function should return one of the top genres
        val pagination = Pagination(
            perPage = 10,
            pages = 1,
            page = 1,
            items = 2,
            urls = null
        )

        val releaseResults = listOf(
            createReleaseResult(id = 1, genres = listOf("Rock", "Pop")),
            createReleaseResult(id = 2, genres = listOf("Jazz", "Blues"))
        )

        val result = DiscogSearchModel(
            pagination = pagination,
            results = releaseResults
        )

        val batchResults = mapOf(
            Pair("Album1", "Artist1") to result
        )

        val mostCommon = viewModel.getMostCommonGenre(batchResults)
        // Each genre appears exactly once, so any of them could be returned
        assertTrue(mostCommon == "Rock" || mostCommon == "Pop" || mostCommon == "Jazz" || mostCommon == "Blues")
    }

    // Helper method to create ReleaseResult objects
    private fun createReleaseResult(id: Int, genres: List<String>? = null): ReleaseResult {
        return ReleaseResult(
            style = listOf("Test Style"),
            thumb = "thumbnail.jpg",
            title = "Test Release $id",
            country = "US",
            format = listOf("CD", "Digital"),
            uri = "https://api.discogs.com/releases/$id",
            community = CommunityStats(want = 100, have = 200),
            label = listOf("Test Label"),
            catalogNumber = "CAT$id",
            year = "2023",
            genre = genres,
            resourceUrl = "https://api.discogs.com/releases/$id",
            type = "release",
            id = id
        )
    }
}
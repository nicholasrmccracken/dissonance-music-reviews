package com.dissonance.app.data.model
import com.google.gson.annotations.SerializedName

data class DiscogSearchModel(
    @SerializedName("pagination") val pagination: Pagination,
    @SerializedName("results") val results: List<ReleaseResult>
)

data class Pagination(
    @SerializedName("per_page") val perPage: Int,
    @SerializedName("pages") val pages: Int,
    @SerializedName("page") val page: Int,
    @SerializedName("items") val items: Int,
    @SerializedName("urls") val urls: PaginationUrls?
)

data class PaginationUrls(
    @SerializedName("last") val last: String?,
    @SerializedName("next") val next: String?
)

data class ReleaseResult(
    @SerializedName("style") val style: List<String>?,
    @SerializedName("thumb") val thumb: String?,
    @SerializedName("title") val title: String,
    @SerializedName("country") val country: String?,
    @SerializedName("format") val format: List<String>?,
    @SerializedName("uri") val uri: String,
    @SerializedName("community") val community: CommunityStats,
    @SerializedName("label") val label: List<String>?,
    @SerializedName("catno") val catalogNumber: String?,
    @SerializedName("year") val year: String?,
    @SerializedName("genre") val genre: List<String>?,
    @SerializedName("resource_url") val resourceUrl: String,
    @SerializedName("type") val type: String,
    @SerializedName("id") val id: Int
)

data class CommunityStats(
    @SerializedName("want") val want: Int,
    @SerializedName("have") val have: Int
)
package com.example.data.model

import com.example.domain.model.ArtistResultModel
import com.example.domain.model.ArtistSearchModel
import com.example.domain.model.PaginationModel
import com.example.domain.model.PaginationUrlsModel
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ArtistSearchEntity(
    @SerializedName("pagination") val pagination: PaginationEntity,
    @SerializedName("results") val results: List<ArtistResultEntity>
) {
    fun toDomain() = ArtistSearchModel(pagination.toDomain(), results.map { it.toDomain() })
}

@JsonClass(generateAdapter = true)
data class PaginationEntity(
    @SerializedName("page") val page: Int,
    @SerializedName("pages") val pages: Int,
    @SerializedName("per_page") val perPage: Int,
    @SerializedName("items") val items: Int,
    @SerializedName("urls") val urls: PaginationUrlsEntity
) {
    fun toDomain() = PaginationModel(page, pages, perPage, items, urls.toDomain())
}

@JsonClass(generateAdapter = true)
data class PaginationUrlsEntity(
    @SerializedName("first") val first: String?,
    @SerializedName("last") val last: String?,
    @SerializedName("next") val next: String?,
    @SerializedName("prev") val prev: String?
) {
    fun toDomain() = PaginationUrlsModel(last, next, prev)
}

@JsonClass(generateAdapter = true)
data class ArtistResultEntity(
    @SerializedName("id") val id: Int,
    @SerializedName("type") val type: String,
    @SerializedName("master_id") val masterId: String?,
    @SerializedName("master_url") val masterUrl: String?,
    @SerializedName("uri") val uri: String,
    @SerializedName("title") val title: String,
    @SerializedName("thumb") val thumb: String,
    @SerializedName("cover_image") val coverImage: String,
    @SerializedName("resource_url") val resourceUrl: String
) {
    fun toDomain() = ArtistResultModel(
        id, type, masterId, masterUrl, uri, title, thumb, coverImage, resourceUrl
    )
}
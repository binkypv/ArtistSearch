package com.example.domain.model

data class ArtistSearchModel(
    val pagination: PaginationModel,
    val results: List<ArtistResultModel>
)

data class PaginationModel(
    val page: Int,
    val pages: Int,
    val perPage: Int,
    val items: Int,
    val urls: PaginationUrlsModel
)

data class PaginationUrlsModel(
    val last: String?,
    val next: String?,
    val prev: String?
)

data class ArtistResultModel(
    val id: Int,
    val type: String,
    val masterId: String?,
    val masterUrl: String?,
    val uri: String,
    val title: String,
    val thumb: String,
    val coverImage: String,
    val resourceUrl: String
)
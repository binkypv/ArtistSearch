package com.example.domain.model

data class ArtistModel(
    val name: String,
    val id: Int,
    val resourceUrl: String,
    val uri: String,
    val releasesUrl: String,
    val images: List<ArtistImageModel>?,
    val profile: String,
    val dataQuality: String,
    val urls: List<String>?,
    val nameVariations: List<String>?,
    val members: List<MemberModel>?
)

data class ArtistImageModel(
    val type: String,
    val uri: String,
    val resourceUrl: String,
    val uri150: String,
    val width: Int,
    val height: Int
)

data class MemberModel(
    val id: Int,
    val name: String,
    val resourceUrl: String,
    val active: Boolean
)
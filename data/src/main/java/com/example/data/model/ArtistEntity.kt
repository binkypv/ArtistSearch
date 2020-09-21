package com.example.data.model

import com.example.domain.model.ArtistImageModel
import com.example.domain.model.ArtistModel
import com.example.domain.model.MemberModel
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ArtistEntity(
    @SerializedName("name") val name: String,
    @SerializedName("id") val id: Int,
    @SerializedName("resource_url") val resourceUrl: String,
    @SerializedName("uri") val uri: String,
    @SerializedName("releases_url") val releasesUrl: String,
    @SerializedName("images") val images: List<ArtistImageEntity>?,
    @SerializedName("profile") val profile: String,
    @SerializedName("data_quality") val dataQuality: String,
    @SerializedName("urls") val urls: List<String>?,
    @SerializedName("namevariations") val nameVariations: List<String>?,
    @SerializedName("members") val members: List<MemberEntity>?
) {
    fun toDomain() = ArtistModel(
        name,
        id,
        resourceUrl,
        uri,
        releasesUrl,
        images?.map { it.toDomain() },
        profile,
        dataQuality,
        urls,
        nameVariations,
        members?.map { it.toDomain() })
}

@JsonClass(generateAdapter = true)
data class ArtistImageEntity(
    @SerializedName("type") val type: String,
    @SerializedName("uri") val uri: String,
    @SerializedName("resource_url") val resourceUrl: String,
    @SerializedName("uri150") val uri150: String,
    @SerializedName("width") val width: Int,
    @SerializedName("height") val height: Int
) {
    fun toDomain() = ArtistImageModel(
        type, uri, resourceUrl, uri150, width, height
    )
}

@JsonClass(generateAdapter = true)
data class MemberEntity(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("resource_url") val resourceUrl: String,
    @SerializedName("active") val active: Boolean
) {
    fun toDomain() = MemberModel(
        id, name, resourceUrl, active
    )
}
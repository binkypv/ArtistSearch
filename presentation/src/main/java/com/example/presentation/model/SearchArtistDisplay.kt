package com.example.presentation.model

import com.example.domain.model.ArtistResultModel
import com.example.domain.model.ArtistSearchModel

data class ArtistSearchDisplay(
    val nextUrl: String?,
    val lastUrl: String?,
    val results: List<ArtistResultDisplay>
)

data class ArtistResultDisplay(
    val id: Int,
    val title: String,
    val image: String?
)

fun ArtistSearchModel.toDisplay() = ArtistSearchDisplay(
    pagination.urls.next,
    pagination.urls.last,
    results.map { it.toDisplay() }
)

fun ArtistResultModel.toDisplay() = ArtistResultDisplay(
    id, title, thumb
)
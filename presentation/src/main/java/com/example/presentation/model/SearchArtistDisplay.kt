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
): ArtistListItem(){
    override fun areItemsTheSame(other: ArtistListItem) =
        other is ArtistResultDisplay && other.id == id

    override fun areContentsTheSame(other: ArtistListItem) =
        other is ArtistResultDisplay && other == this

}

fun ArtistSearchModel.toDisplay() = ArtistSearchDisplay(
    pagination.urls.next,
    pagination.urls.last,
    results.map { it.toDisplay() }
)

fun ArtistResultModel.toDisplay() = ArtistResultDisplay(
    id, title, thumb
)

sealed class ArtistListItem{
    abstract fun areItemsTheSame(other: ArtistListItem): Boolean
    abstract fun areContentsTheSame(other: ArtistListItem): Boolean
}

object ArtistLoadingItem: ArtistListItem(){
    override fun areItemsTheSame(other: ArtistListItem) =
        other is ArtistLoadingItem

    override fun areContentsTheSame(other: ArtistListItem) =
        other is ArtistLoadingItem

}
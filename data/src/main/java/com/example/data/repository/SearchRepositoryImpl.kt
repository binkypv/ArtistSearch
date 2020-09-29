package com.example.data.repository

import com.example.data.datasource.SearchDataSource
import com.example.domain.model.ArtistModel
import com.example.domain.model.ArtistSearchModel
import com.example.domain.repository.SearchRepository

private const val SEARCH_ARTIST_TYPE = "artist"
private const val PAGE_SIZE = 9

class SearchRepositoryImpl(
    val searchDataSource: SearchDataSource
) : SearchRepository {
    override suspend fun searchArtists(artist: String, getPage: Int): ArtistSearchModel =
        searchDataSource.searchArtists(SEARCH_ARTIST_TYPE, artist, PAGE_SIZE, getPage).toDomain()

    override suspend fun getArtist(id: Int): ArtistModel =
        searchDataSource.getArtist(id).toDomain()
}
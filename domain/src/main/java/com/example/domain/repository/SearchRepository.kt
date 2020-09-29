package com.example.domain.repository

import com.example.domain.model.ArtistModel
import com.example.domain.model.ArtistSearchModel

interface SearchRepository {
    suspend fun searchArtists(artist: String, getPage: Int = 1): ArtistSearchModel
    suspend fun getArtist(id: Int): ArtistModel
}
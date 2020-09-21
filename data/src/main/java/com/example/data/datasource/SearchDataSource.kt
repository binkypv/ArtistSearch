package com.example.data.datasource

import com.example.data.model.ArtistEntity
import com.example.data.model.ArtistSearchEntity
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SearchDataSource {
    @GET("database/search")
    suspend fun searchArtists(
        @Query("type") type: String,
        @Query("q") q: String,
        @Query("per_page") perPage: Int,
        @Query("page") page: Int
    ): ArtistSearchEntity

    @GET("artists/{artistId}")
    suspend fun getArtist(
        @Path("artistId") artistId: Int
    ): ArtistEntity
}
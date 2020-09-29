package com.example.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.data.datasource.SearchDataSource
import com.example.data.model.*
import com.example.domain.repository.SearchRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest

class SearchRepositoryImplTest : KoinTest {

    @get:Rule
    val instantRule = InstantTaskExecutorRule()

    val datasource: SearchDataSource = mockk()
    val repo: SearchRepository = SearchRepositoryImpl(datasource)

    @Before
    fun setup() {
        MockKAnnotations.init()
    }

    @Test
    fun searchArtist() {
        // given
        val entity = ArtistSearchEntity(
            PaginationEntity(1, 1, 10, 1, PaginationUrlsEntity("first", "last", "next", "prev")),
            listOf(
                ArtistResultEntity(
                    1,
                    "type",
                    "masterId",
                    "masterUrl",
                    "uri",
                    "title",
                    "thumb",
                    "coverImage",
                    "resourceUrl"
                )
            )
        )
        coEvery { datasource.searchArtists(any(), any(), any(), any()) } returns entity

        // when
        runBlocking {
            repo.searchArtists("a")
        }

        // then
        coVerify(exactly = 1) {
            datasource.searchArtists("artist", "a", 10, 1)
        }
    }

    @Test
    fun getArtist() {
        // given
        val entity = ArtistEntity(
            "name",
            1,
            "resourceUri",
            "uri",
            "releasesUrl",
            listOf(
                ArtistImageEntity(
                    "type",
                    "uri",
                    "resourceUrl",
                    "uri150",
                    100,
                    100
                )
            ),
            "profile",
            "dataQuality",
            listOf("url"),
            listOf("name"),
            listOf(MemberEntity(1, "name", "resourceUrl", true))
        )
        coEvery { datasource.getArtist(any()) } returns entity

        // when
        runBlocking { repo.getArtist(1) }

        // then
        coVerify(exactly = 1) { datasource.getArtist(1) }
    }
}
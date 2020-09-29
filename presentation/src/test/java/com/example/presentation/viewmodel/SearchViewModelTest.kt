package com.example.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.domain.model.ArtistResultModel
import com.example.domain.model.ArtistSearchModel
import com.example.domain.model.PaginationModel
import com.example.domain.model.PaginationUrlsModel
import com.example.domain.repository.SearchRepository
import com.example.presentation.model.ArtistListItem
import com.example.presentation.model.ArtistLoadingItem
import com.example.presentation.model.toDisplay
import com.example.presentation.utils.CoroutinesRule
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.koin.test.KoinTest

@RunWith(JUnit4::class)
class SearchViewModelTest : KoinTest {

    @get:Rule
    val instantRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesRule = CoroutinesRule()

    val repo: SearchRepository = mockk()
    private val observer = mockk<Observer<List<ArtistListItem>>>(relaxed = true)
    val viewmodel = SearchViewModel(repo)

    @Before
    fun setup() {
        MockKAnnotations.init()
        viewmodel.results.observeForever(observer)
    }

    @Test
    fun onSearchClickedSinglePage() {
        // given
        val model = ArtistSearchModel(
            PaginationModel(1, 1, 10, 1, PaginationUrlsModel("last", null, "prev")),
            listOf(
                ArtistResultModel(
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
        coEvery { repo.searchArtists(any()) } returns model

        // when
        runBlocking {
            viewmodel.onSearchClicked("a")
        }

        // then
        coVerify(exactly = 1) { repo.searchArtists("a") }
        verify { observer.onChanged(model.results.map { it.toDisplay() }) }
    }

    @Test
    fun onSearchClickedMultiplePages() {
        // given
        val model = ArtistSearchModel(
            PaginationModel(1, 1, 10, 1, PaginationUrlsModel("last", "next", "prev")),
            listOf(
                ArtistResultModel(
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
        val result: List<ArtistListItem> = mutableListOf<ArtistListItem>().apply {
            addAll(
                model.results.map { it.toDisplay() })
            add(ArtistLoadingItem)
        }.toList()
        coEvery { repo.searchArtists(any()) } returns model

        // when
        runBlocking {
            viewmodel.onSearchClicked("a")
        }

        // then
        coVerify(exactly = 1) { repo.searchArtists("a") }
        verify { observer.onChanged(result) }
    }

    @Test
    fun loadNextPage() {
        // given
        val model = ArtistSearchModel(
            PaginationModel(1, 1, 10, 1, PaginationUrlsModel("last", "next", "prev")),
            listOf(
                ArtistResultModel(
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
        coEvery { repo.searchArtists(any(), any()) } returns model

        // when
        runBlocking {
            viewmodel.onSearchClicked("a")
            viewmodel.loadNextPage()
        }

        // then
        coVerify(exactly = 1) { repo.searchArtists("a", 2) }
    }
}
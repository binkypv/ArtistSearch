package com.example.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.domain.model.ArtistImageModel
import com.example.domain.model.ArtistModel
import com.example.domain.model.MemberModel
import com.example.domain.repository.SearchRepository
import com.example.presentation.model.ArtistDisplay
import com.example.presentation.model.toDisplay
import com.example.presentation.utils.CoroutinesRule
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest

class ArtistViewModelTest : KoinTest {

    @get:Rule
    val instantRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesRule = CoroutinesRule()

    val repo: SearchRepository = mockk()
    private val observer = mockk<Observer<ArtistDisplay>>(relaxed = true)
    val viewmodel = ArtistViewModel(repo)

    @Before
    fun setup() {
        MockKAnnotations.init()
        viewmodel.artist.observeForever(observer)
    }

    @Test
    fun loadArtist() {
        // given
        val model = ArtistModel(
            "name",
            1,
            "resourceUri",
            "uri",
            "releasesUrl",
            listOf(
                ArtistImageModel(
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
            listOf(MemberModel(1, "name", "resourceUrl", true))
        )
        coEvery { repo.getArtist(any()) } returns model

        // when
        runBlocking {
            viewmodel.loadArtist(1)
        }

        // then
        coVerify(exactly = 1) { repo.getArtist(1) }
        verify { observer.onChanged(model.toDisplay()) }
    }
}
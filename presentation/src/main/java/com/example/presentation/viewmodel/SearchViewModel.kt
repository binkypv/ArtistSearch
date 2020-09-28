package com.example.presentation.viewmodel

import androidx.lifecycle.*
import com.example.domain.model.ArtistSearchModel
import com.example.domain.repository.SearchRepository
import com.example.domain.utils.Resource
import com.example.presentation.model.ArtistListItem
import com.example.presentation.model.ArtistLoadingItem
import com.example.presentation.model.ArtistSearchDisplay
import com.example.presentation.model.toDisplay
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class SearchViewModel(val repository: SearchRepository) : ViewModel() {

    private val _results = MutableLiveData<List<ArtistListItem>>()
    val results: LiveData<List<ArtistListItem>> = _results

    private val _nextUrl = MutableLiveData<String>()
    val nextUrl: LiveData<String> = _nextUrl

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _loading = MutableLiveData<Unit>()
    val loading: LiveData<Unit> = _loading

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        _error.postValue(exception.message)
    }

    private var searchTerm: String? = null
    private var page = 2
    private var nextPage = true
    private var fetching = false

    fun onSearchClicked(search: String) =
        viewModelScope.launch(exceptionHandler) {
            searchTerm = search
            _loading.postValue(Unit)
            val artistSearch = Resource.Success(repository.searchArtists(search)).data?.toDisplay()
            val newResults: MutableList<ArtistListItem> =
                artistSearch?.results?.toMutableList() ?: mutableListOf()
            page = 2
            nextPage = if (artistSearch?.nextUrl.isNullOrEmpty()) {
                false
            } else {
                newResults.add(ArtistLoadingItem)
                true
            }
            _results.postValue(newResults)
        }

    fun loadNextPage() {
        viewModelScope.launch(exceptionHandler) {
            if (searchTerm != null && nextPage && !fetching) {
                fetching = true
                val currentList =
                    _results.value?.toMutableList()?.apply { remove(ArtistLoadingItem) }
                        ?: mutableListOf()
                val artistSearch =
                    Resource.Success(
                        repository.searchArtists(
                            searchTerm ?: "",
                            page
                        )
                    ).data?.toDisplay()
                page++
                val newResults: MutableList<ArtistListItem> =
                    artistSearch?.results?.toMutableList() ?: mutableListOf()
                if (artistSearch?.nextUrl.isNullOrEmpty()) {
                    nextPage = false
                } else {
                    newResults.add(ArtistLoadingItem)
                }
                currentList.addAll(newResults)
                _results.postValue(currentList)
                fetching = false
            }
        }
    }

}
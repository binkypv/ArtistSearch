package com.example.presentation.viewmodel

import androidx.lifecycle.*
import com.example.domain.model.ArtistSearchModel
import com.example.domain.repository.SearchRepository
import com.example.domain.utils.Resource
import com.example.presentation.model.ArtistSearchDisplay
import com.example.presentation.model.toDisplay
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class SearchViewModel(val repository: SearchRepository) : ViewModel() {

    private val _results = MutableLiveData<ArtistSearchDisplay>()
    val results: LiveData<ArtistSearchDisplay> = _results

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _loading = MutableLiveData<Unit>()
    val loading: LiveData<Unit> = _loading

    private val exceptionHandler = CoroutineExceptionHandler{_,  exception ->
        _error.postValue(exception.message)
    }

    fun onSearchClicked(search: String) =
        viewModelScope.launch(exceptionHandler){
            _loading.postValue(Unit)
            _results.postValue(Resource.Success(repository.searchArtists(search)).data?.toDisplay())
        }
}
package com.example.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.repository.SearchRepository
import com.example.presentation.model.ArtistDisplay
import com.example.presentation.model.toDisplay
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class ArtistViewModel(val repository: SearchRepository) : ViewModel() {

    private val _artist = MutableLiveData<ArtistDisplay>()
    val artist: LiveData<ArtistDisplay> = _artist

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _loading = MutableLiveData<Unit>()
    val loading: LiveData<Unit> = _loading

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        _error.postValue(exception.message)
    }

    fun loadArtist(id: Int) =
        viewModelScope.launch(exceptionHandler) {
            _loading.postValue(Unit)
            _artist.postValue(repository.getArtist(id).toDisplay())
        }
}
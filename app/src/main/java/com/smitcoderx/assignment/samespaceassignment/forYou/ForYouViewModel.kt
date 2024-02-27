package com.smitcoderx.assignment.samespaceassignment.forYou

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForYouViewModel @Inject constructor(
    private val forYouRepository: ForYouRepository
) : ViewModel() {

    private val _songListLiveData = MutableLiveData<ForYouModel?>()
    val songListLiveData: LiveData<ForYouModel?>
        get() = _songListLiveData

    private val _songDataLiveData = MutableLiveData<Data>()
    val songDataLiveData: LiveData<Data>
        get() = _songDataLiveData

    fun getSongList() = viewModelScope.launch {
        _songListLiveData.value = forYouRepository.getSongsList()
    }

    fun getSongData(id: Int) = viewModelScope.launch {
        _songDataLiveData.value = forYouRepository.getSongsData(id)
    }


}
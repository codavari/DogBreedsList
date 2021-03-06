package com.example.dogbreedslist.ui.photos

import android.widget.ImageView
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogbreedslist.data.Resource
import com.example.dogbreedslist.data.local.breeds.BreedData
import com.example.dogbreedslist.data.local.favorites.FavoriteData
import com.example.dogbreedslist.data.repository.DataRepository
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.launch

@ActivityScoped
class DogPhotosViewModel @ViewModelInject constructor(private val repository: DataRepository) :
    ViewModel() {

    private val _photos = MutableLiveData<Resource<List<String>>>()
    val photos: LiveData<Resource<List<String>>> = _photos

    private val _favoritesPhotos = MutableLiveData<List<String>>()
    val favoritesPhotos: LiveData<List<String>> = _favoritesPhotos

    private val _favorite = MutableLiveData<Boolean>()
    val favorite: LiveData<Boolean> = _favorite

    fun fetchPhotos(breed: String, subbreed: String) {
        viewModelScope.launch {
            if (subbreed == "no subbreeds") {
                _photos.postValue(repository.requestBreedImages(breed))
            } else {
                _photos.postValue(repository.requestSubbreedImages(breed, subbreed))
            }
        }
    }

    fun fetchPhotosFromLocal(breed: String) {
        viewModelScope.launch {
            _favoritesPhotos.postValue(repository.getPhotosOfBreedFromLocal(breed))
        }
    }

    fun setLike(url: String) {
        viewModelScope.launch {
            _favorite.postValue(repository.isLoved(url))
        }
    }

    fun deleteFavorite(favorite: FavoriteData) {
        viewModelScope.launch {
            repository.deleteFavorite(favorite)
        }
    }

    fun addToFavorites(favorite: FavoriteData) {
        viewModelScope.launch {
            repository.addFavorite(favorite)
        }
    }
}
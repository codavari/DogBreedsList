package com.example.dogbreedslist.ui.breeds.breedadapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.dogbreedslist.data.network.dto.Breed
import com.example.dogbreedslist.databinding.ItemBreedBinding
import com.example.dogbreedslist.ui.breeds.BreedListFragment
import com.example.dogbreedslist.ui.breeds.BreedListFragmentDirections
import com.example.dogbreedslist.ui.breeds.BreedListViewModel

class BreedViewHolder private constructor(val binding: ItemBreedBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(viewModel: BreedListViewModel, item: Breed) {

        binding.apply {
            binding.breedListViewModel = viewModel
            breed = item
            setClickListener { navigateToSubbreeds(breed?.subbreeds.isNullOrEmpty(), item, it) }
            executePendingBindings()
        }
    }

    fun navigateToSubbreeds(isSubbreedsNotExist: Boolean, breed: Breed, view: View) {
        if (isSubbreedsNotExist) openDogsPhotos(breed, view)
        val direction = breed.name?.let { name ->
            BreedListFragmentDirections.actionBreedToSubbreed(name)
        }
        direction?.let { view.findNavController().navigate(it) }
        binding.breedListViewModel?._subbreedClicked?.postValue(breed.subbreeds)
    }

    fun openDogsPhotos(breed: Breed, view: View) {
        val direction = breed.name?.let {name -> BreedListFragmentDirections.actionBreedToPhotos(name)}
        direction?.let { view.findNavController().navigate(it) }
    }

    companion object {
        fun from(parent: ViewGroup): BreedViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemBreedBinding.inflate(layoutInflater, parent, false)
            return BreedViewHolder(binding)
        }
    }
}
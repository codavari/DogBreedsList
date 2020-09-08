package com.example.dogbreedslist.ui.photos

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.example.dogbreedslist.R
import com.example.dogbreedslist.data.Resource
import com.example.dogbreedslist.data.local.favorites.FavoriteData
import com.example.dogbreedslist.databinding.FragmentDogsPhotosBinding
import com.example.dogbreedslist.utils.intentShareText
import com.example.dogbreedslist.utils.setTitle
import com.example.dogbreedslist.utils.toGone
import com.example.dogbreedslist.viewmodel.DogPhotosViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class DogPhotosFragment : Fragment() {

    private lateinit var dogPhotosViewModel: DogPhotosViewModel
    private lateinit var currentImageUrl: String
    private lateinit var binding: FragmentDogsPhotosBinding
    private var isTheFragmentFavorites: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDogsPhotosBinding.inflate(inflater, container, false)
        context ?: return binding.root

        isTheFragmentFavorites = arguments?.get("fragmentFlag") as Boolean

        val imageAdapter = context?.let { DogPhotoAdapter(it) }
        if (imageAdapter != null) {
            initViewModel(imageAdapter, isTheFragmentFavorites)
            initViewPager(binding, dogPhotosViewModel, imageAdapter)
        }
        initUI()
        setHasOptionsMenu(true)
        return binding.root
    }

    private fun initUI() {
        setTitle(getBreedInfo().capitalize())
        activity?.bottom_nav?.toGone()
        binding.apply {
            setClickListener {
                dogPhotosViewModel.setLike(currentImageUrl)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_dogs_collection, menu)
    }

    @Suppress("DEPRECATION")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_share -> {
                intentShareText(requireActivity(), getString(R.string.share_dog_image))
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun declareAttitude(isLoved: Boolean) {
        if (isLoved) addToFavorites(currentImageUrl) else {
            removeFromFavorites(currentImageUrl)
        }
    }

    private fun addToFavorites(url: String) {
        like()
        dogPhotosViewModel.addToFavorites(
            FavoriteData(
                name = getBreedInfo(),
                photoUrl = url
            )
        )
        Toast.makeText(context, "Added to your favorites", Toast.LENGTH_SHORT).show()
    }

    private fun removeFromFavorites(url: String) {
        unlike()
        dogPhotosViewModel.deleteFavorite(
            FavoriteData(
                name = getBreedInfo(),
                photoUrl = url
            )
        )
        Toast.makeText(context, "Removed from your favorites", Toast.LENGTH_SHORT).show()
    }

    private fun unlike() {
        binding.fabImageLiked.setImageResource(R.drawable.ic_love_border)
    }

    private fun like() {
        binding.fabImageLiked.setImageResource(R.drawable.ic_love)
    }

    fun getBreedName(): String {
        return arguments?.getString("breedName").toString()
    }

    fun getSubbreedName(): String {
        return arguments?.getString("subbreedName").toString()
    }

    fun getBreedInfo(): String {
        return if (getSubbreedName() != "no subbreeds" && getSubbreedName() != "null") {
            getSubbreedName()
        } else {
            getBreedName()
        }
    }

    private fun initViewModel(adapter: DogPhotoAdapter, isFavorite: Boolean) {
        dogPhotosViewModel = ViewModelProvider(this).get(DogPhotosViewModel::class.java)
        dogPhotosViewModel.favorite.observe(viewLifecycleOwner, Observer<Boolean> { like ->
            declareAttitude(!like)
        })
        if (isFavorite) {
            dogPhotosViewModel.favoritesPhotos.observe(
                viewLifecycleOwner,
                Observer<List<String>> { images ->
                    images.let {
                        adapter.setImages(it)
                    }
                })
            dogPhotosViewModel.fetchPhotosFromLocal(getBreedInfo())
        } else {
            dogPhotosViewModel.photos.observe(
                viewLifecycleOwner,
                Observer<Resource<List<String>>> { images ->
                    images.let {
                        adapter.setImages(it.data!!)
                    }
                })
            dogPhotosViewModel.fetchPhotos(getBreedName(), getSubbreedName())
            dogPhotosViewModel.fetchPhotosFromLocal(getBreedInfo())
        }
    }

    private fun initViewPager(
        binding: FragmentDogsPhotosBinding,
        viewModel: DogPhotosViewModel,
        adapter: DogPhotoAdapter
    ) {
        val viewPager = binding.photosViewPager
        viewPager.adapter = adapter

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                unlike()
                if (isTheFragmentFavorites) {
                    currentImageUrl = viewModel.favoritesPhotos.value?.get(position).toString()
                } else {
                    currentImageUrl = viewModel.photos.value?.data!![position]
                }
                val currentList = dogPhotosViewModel.favoritesPhotos.value
                if (currentList != null && currentImageUrl in currentList){
                    like()
                }

            }

            override fun onPageSelected(position: Int) {
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })
    }
}
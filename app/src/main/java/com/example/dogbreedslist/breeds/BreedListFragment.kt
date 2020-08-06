package com.example.dogbreedslist.breeds

import android.app.FragmentBreadCrumbs
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dogbreedslist.databinding.ItemBreedBinding
import com.example.dogbreedslist.di.DblViewModelFactory
import com.example.dogbreedslist.breeds.breedadapter.BreedAdapter
import com.example.dogbreedslist.databinding.FragmentBreedlistBinding
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject


class BreedListFragment : Fragment() {

    @Inject
    lateinit var breedListViewModel: BreedListViewModel

    @Inject
    lateinit var viewModelFactory: DblViewModelFactory

    lateinit var binding: FragmentBreedlistBinding
    private lateinit var breedListAdapter: BreedAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBreedlistBinding.inflate(inflater, container, false)
        context ?: return binding.root

        initializeViewModel()
        breedListAdapter = BreedAdapter(breedListViewModel)
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        initRecyclerView()
        init()
    }

    fun init(){
        breedListViewModel.getBreeds()
    }

    private fun initRecyclerView() {

        breedListViewModel.breedList.observe(viewLifecycleOwner, Observer {let {
            breedListAdapter.mDogBreedList
        }
        })
    }

    private fun initializeViewModel() {
        breedListViewModel = viewModelFactory.create(BreedListViewModel::class.java)
    }
}
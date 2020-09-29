package com.example.presentation.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.presentation.adapter.ArtistAdapter
import com.example.presentation.databinding.FragmentSearchBinding
import com.example.presentation.utils.ScrollPaginator
import com.example.presentation.viewmodel.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private val searchViewModel: SearchViewModel by viewModel()
    private val adapter: ArtistAdapter by lazy {
        ArtistAdapter { id ->
            navigateToArtist(id)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViews()
        initListeners()
        initObservers()
    }

    private fun initViews() {
        binding.artistsList.adapter = adapter
        binding.artistsList.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )

        binding.artistsList.addOnScrollListener(object :
            ScrollPaginator(binding.artistsList.layoutManager as LinearLayoutManager) {
            override fun loadMoreItems() {
                searchViewModel.loadNextPage()
            }
        })
    }

    private fun initListeners() {
        binding.searchBar.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchArtist(binding.searchBar.text.toString())
                true
            } else {
                false
            }
        }

        binding.searchButton.setOnClickListener {
            searchArtist(binding.searchBar.text.toString())
        }
    }

    private fun initObservers() {
        searchViewModel.loading.observe(viewLifecycleOwner, {
            binding.loading.visibility = View.VISIBLE
        })

        searchViewModel.results.observe(viewLifecycleOwner, {
            binding.loading.visibility = View.GONE
            adapter.submitList(it)
        })

        searchViewModel.error.observe(viewLifecycleOwner, {
            binding.loading.visibility = View.GONE
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        })
    }

    private fun searchArtist(term: String) {
        binding.searchBar.clearFocus()
        val imm: InputMethodManager =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.searchBar.windowToken, 0)
        searchViewModel.onSearchClicked(term)
    }

    private fun navigateToArtist(id: Int) {
        findNavController().navigate(
            SearchFragmentDirections.actionSearchFragmentToArtistFragment(
                id
            )
        )
    }
}
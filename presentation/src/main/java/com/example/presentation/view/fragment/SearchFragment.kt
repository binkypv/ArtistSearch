package com.example.presentation.view.fragment

import android.content.Context
import android.content.res.Configuration
import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.content.res.Configuration.ORIENTATION_PORTRAIT
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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.presentation.adapter.ArtistAdapter
import com.example.presentation.databinding.FragmentSearchBinding
import com.example.presentation.utils.ScrollPaginator
import com.example.presentation.viewmodel.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val GRID_SIZE = 3
private const val RECYCLER_VIEW_POSITION_KEY = "RECYCLER_VIEW_POSITION_KEY"

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
        initViews(savedInstanceState?.getInt(RECYCLER_VIEW_POSITION_KEY) ?: 0)
        initListeners()
        initObservers()
    }

    private fun initViews(listPosition: Int) {
        binding.artistsList.adapter = adapter
        binding.artistsList.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
        binding.artistsList.scrollToPosition(listPosition)

        changeOrientation(resources.configuration.orientation)
        configurePaging()

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

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        changeOrientation(newConfig.orientation)
    }

    private fun changeOrientation(orientation: Int) {
        when (orientation) {
            ORIENTATION_LANDSCAPE -> {
                binding.artistsList.layoutManager =
                    GridLayoutManager(
                        requireContext(),
                        GRID_SIZE,
                        LinearLayoutManager.VERTICAL,
                        false
                    )
            }
            ORIENTATION_PORTRAIT -> {
                binding.artistsList.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            }
        }
        configurePaging()
    }

    private fun configurePaging() {
        binding.artistsList.addOnScrollListener(object :
            ScrollPaginator(binding.artistsList.layoutManager as LinearLayoutManager) {
            override fun loadMoreItems() {
                searchViewModel.loadNextPage()
            }
        })
    }

    private fun searchArtist(term: String) {
        binding.searchBar.clearFocus()
        val imm: InputMethodManager =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.searchBar.windowToken, 0)
        searchViewModel.onSearchClicked(term)
    }

        override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(
            RECYCLER_VIEW_POSITION_KEY,
            (binding.artistsList.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        )
        super.onSaveInstanceState(outState)
    }

    private fun navigateToArtist(id: Int) {
        findNavController().navigate(
            SearchFragmentDirections.actionSearchFragmentToArtistFragment(
                id
            )
        )
    }
}
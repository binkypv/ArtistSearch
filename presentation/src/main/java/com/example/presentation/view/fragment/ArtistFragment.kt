package com.example.presentation.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.presentation.R
import com.example.presentation.databinding.FragmentArtistBinding
import com.example.presentation.viewmodel.ArtistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ArtistFragment : Fragment() {
    private lateinit var binding: FragmentArtistBinding
    private val artistViewModel: ArtistViewModel by viewModel()
    private val args: ArtistFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentArtistBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        artistViewModel.loadArtist(args.id)
        initObservers()
    }

    private fun initObservers() {
        artistViewModel.artist.observe(viewLifecycleOwner, { display ->
            if (display.image.isNullOrEmpty()) {
                binding.headerImage.setImageDrawable(context?.getDrawable(R.drawable.ic_placeholder))
            } else {
                Glide.with(binding.headerImage)
                    .load(display.image)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(binding.headerImage)
            }

            binding.artistTitle.text = display.title
            binding.artistBio.text = display.profile
            display.members?.let {
                binding.membersHeader.visibility = View.VISIBLE
                binding.members.text = it
            }
        })

        artistViewModel.error.observe(viewLifecycleOwner, {
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        })
    }
}
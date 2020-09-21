package com.example.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.presentation.R
import com.example.presentation.databinding.ArtistItemBinding
import com.example.presentation.model.ArtistResultDisplay

class ArtistAdapter(private val onClick: (Int) -> Unit) :
    ListAdapter<ArtistResultDisplay, ArtistViewHolder>(artistsDiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ArtistViewHolder(
        ArtistItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        holder.bind(getItem(position), onClick)
    }
}

val artistsDiffCallback = object : DiffUtil.ItemCallback<ArtistResultDisplay>() {
    override fun areItemsTheSame(
        oldItem: ArtistResultDisplay,
        newItem: ArtistResultDisplay
    ) = oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: ArtistResultDisplay,
        newItem: ArtistResultDisplay
    ): Boolean = oldItem == newItem
}

class ArtistViewHolder(val binding: ArtistItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(
        model: ArtistResultDisplay,
        onClick: (Int) -> Unit
    ) {
        Glide.with(binding.artistImg)
            .load(if (model.image.isNullOrEmpty()) binding.artistImg.context.getDrawable(R.drawable.ic_placeholder) else model.image)
            .circleCrop()
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.artistImg)

        binding.artistName.text = model.title

        binding.root.setOnClickListener { onClick(model.id) }
    }
}
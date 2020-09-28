package com.example.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.presentation.R
import com.example.presentation.databinding.ArtistItemBinding
import com.example.presentation.databinding.LoadingItemBinding
import com.example.presentation.model.ArtistListItem
import com.example.presentation.model.ArtistLoadingItem
import com.example.presentation.model.ArtistResultDisplay

private const val LOADING_ITEM = 0
private const val ARTIST_ITEM = 1

class ArtistAdapter(private val onClick: (Int) -> Unit) :
    ListAdapter<ArtistListItem, ArtistListItemViewHolder>(artistsDiffCallback) {
    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is ArtistLoadingItem -> LOADING_ITEM
            else -> ARTIST_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        when (viewType) {
            LOADING_ITEM -> {
                LoadingArtsitViewHolder(
                    LoadingItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else -> {
                ArtistViewHolder(
                    ArtistItemBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                )
            }
        }

    override fun onBindViewHolder(holder: ArtistListItemViewHolder, position: Int) {
        if (holder is ArtistViewHolder) {
            holder.bind(getItem(position), onClick)
        }
    }
}

val artistsDiffCallback = object : DiffUtil.ItemCallback<ArtistListItem>() {
    override fun areItemsTheSame(
        oldItem: ArtistListItem,
        newItem: ArtistListItem
    ) = oldItem.areItemsTheSame(newItem)

    override fun areContentsTheSame(
        oldItem: ArtistListItem,
        newItem: ArtistListItem
    ): Boolean = oldItem.areContentsTheSame(newItem)
}

class ArtistViewHolder(val binding: ArtistItemBinding) : ArtistListItemViewHolder(binding.root) {
    fun bind(
        model: ArtistListItem,
        onClick: (Int) -> Unit
    ) {
        if (model is ArtistResultDisplay) {
            Glide.with(binding.artistImg)
                .load(if (model.image.isNullOrEmpty()) binding.artistImg.context.getDrawable(R.drawable.ic_placeholder) else model.image)
                .circleCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.artistImg)

            binding.artistName.text = model.title

            binding.root.setOnClickListener { onClick(model.id) }
        }
    }
}

class LoadingArtsitViewHolder(val binding: LoadingItemBinding) :
    ArtistListItemViewHolder(binding.root)

sealed class ArtistListItemViewHolder(view: View) : RecyclerView.ViewHolder(view)
package com.example.presentation.utils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class ScrollPaginator(private val layoutManager: LinearLayoutManager) :
    RecyclerView.OnScrollListener() {
    abstract fun loadMoreItems()

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val visibleItems = layoutManager.childCount
        val totalItems = layoutManager.itemCount
        val firstVisible = layoutManager.findFirstVisibleItemPosition()

        if (visibleItems + firstVisible >= totalItems && firstVisible >= 0) {
            loadMoreItems()
        }
    }
}
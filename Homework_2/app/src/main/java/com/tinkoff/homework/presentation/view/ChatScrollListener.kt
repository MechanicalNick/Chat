package com.tinkoff.homework.presentation.view

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tinkoff.homework.elm.chat.model.ChatEffect
import com.tinkoff.homework.elm.chat.model.ChatEvent
import com.tinkoff.homework.elm.chat.model.ChatState
import com.tinkoff.homework.utils.Const
import vivid.money.elmslie.core.store.Store

class ChatScrollListener(
    private val layoutManager: LinearLayoutManager,
    private val store: Store<ChatEvent, ChatEffect, ChatState>,
    private val streamId: Long,
    private val topicName: String
) : RecyclerView.OnScrollListener() {
    // The total number of items in the data set after the last load
    private var previousTotalItemCount = 0

    // True if we are still waiting for the last set of data to load.
    private var loading = true

    override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(view, dx, dy)

        val totalItemCount = layoutManager.itemCount
        val firstVisibleItemPosition: Int = layoutManager.findFirstVisibleItemPosition()
        val lastVisibleItemPosition: Int = layoutManager.findLastVisibleItemPosition()

        // If the total item count is zero and the previous isn't, assume the
        // list is invalidated and should be reset back to initial state
        if (totalItemCount < previousTotalItemCount) {
            previousTotalItemCount = totalItemCount
            if (totalItemCount == 0) {
                loading = true
            }
        }

        // If it’s still loading, we check to see if the data set count has
        // changed, if so we conclude it has finished loading and update the current page
        // number and total item count.
        if (loading && totalItemCount > previousTotalItemCount) {
            loading = false
            previousTotalItemCount = totalItemCount
        }

        // If it isn’t currently loading, we check to see if we have breached
        // the mVisibleThreshold and need to reload more data.
        // If we do need to reload some more data, we execute onLoadMore to fetch the data.
        // threshold should reflect how many total columns there are too
        if (dy != 0 && !loading && !isLoading() &&
            firstVisibleItemPosition < Const.MESSAGE_THRESHOLD &&
            firstVisibleItemPosition >= 0 &&
            lastVisibleItemPosition < totalItemCount - Const.MESSAGE_THRESHOLD
        ) {
            loadMoreItems()
            loading = true
        }
    }

    private fun loadMoreItems() {
        store.accept(ChatEvent.Ui.LoadNextPage(streamId = streamId, topicName = topicName))
    }

    private fun isLoading(): Boolean{
        return store.currentState.isLoading || store.currentState.isShowProgress
    }
}
package com.tinkoff.homework.view.fragment

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tinkoff.homework.elm.chat.model.ChatEffect
import com.tinkoff.homework.elm.chat.model.ChatEvent
import com.tinkoff.homework.elm.chat.model.ChatState
import com.tinkoff.homework.utils.Const
import vivid.money.elmslie.core.store.Store

class ChatScrollListener(private val layoutManager: LinearLayoutManager,
                         val store: Store<ChatEvent, ChatEffect, ChatState>) : RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val firstVisibleItemPosition: Int = layoutManager.findFirstVisibleItemPosition()
        val totalItemCount = layoutManager.itemCount

        if (!isLoading() && dy < 0) {
            if(firstVisibleItemPosition < Const.MESSAGE_THRESHOLD) {
                loadMoreItems()
            }
        }
    }

    private fun loadMoreItems() {
        store.accept(ChatEvent.Ui.LoadNextPage)
    }

    private fun isLoading(): Boolean{
        return store.currentState.isLoading || store.currentState.isShowProgress
    }
}
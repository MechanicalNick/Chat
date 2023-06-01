package com.tinkoff.homework.presentation.view.fragment.chat

import android.net.Uri
import androidx.core.view.isVisible
import com.tinkoff.homework.elm.ViewState
import com.tinkoff.homework.elm.chat.model.ChatEvent
import com.tinkoff.homework.elm.chat.model.ChatState

class AllTopicsChatFragment : ChatFragment() {
    private var firstLoadTopic = true
    override val needGroupByTopic = true

    override fun renderAdditionalViews() {
        binding.topicHeaderLayout.root.isVisible = false
        binding.topicSelectorLayout.root.isVisible = true
    }

    override fun render(state: ChatState){
        super.render(state)
        if(state.state == ViewState.ShowData){
            if(firstLoadTopic) {
                val topicName = state.items?.lastOrNull()?.topic.orEmpty()
                binding.topicSelectorLayout.topicSelectorEditText.setText(topicName)
                firstLoadTopic = false
            }
        }
    }

    override fun sendMessage(message: String) {
        streamId?.let { this.store.accept(ChatEvent.Ui.SendMessage(it, getTopic(), message)) }
    }

    override fun loadImage(uri: Uri) {
        streamId?.let { this.store.accept(ChatEvent.Ui.LoadImage(uri, getTopic(), it)) }
    }

    private fun getTopic(): String = binding
        .topicSelectorLayout.topicSelectorEditText.text.toString()
}
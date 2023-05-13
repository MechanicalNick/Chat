package com.tinkoff.homework.presentation.view.fragment.chat

import androidx.core.view.isVisible
import com.tinkoff.homework.elm.ViewState
import com.tinkoff.homework.elm.chat.model.ChatEvent
import com.tinkoff.homework.elm.chat.model.ChatState

class AllTopicsChatFragment : ChatFragment() {
    var firstLoadTopic = true
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
        val topic = binding.topicSelectorLayout.topicSelectorEditText.text.toString()
        streamId?.let { this.store.accept(ChatEvent.Ui.SendMessage(it, topic, message)) }
    }
}
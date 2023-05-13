package com.tinkoff.homework.presentation.view.fragment.chat

import androidx.core.view.isVisible
import com.tinkoff.homework.R
import com.tinkoff.homework.elm.chat.model.ChatEvent

class SingleTopicChatFragment : ChatFragment() {

    override val needGroupByTopic = false

    override fun renderAdditionalViews(){
        binding.topicHeaderLayout.root.isVisible = true
        binding.topicHeaderLayout.header.text = getString(R.string.sharp, topicName)

        binding.topicSelectorLayout.root.isVisible = false
    }

    override fun sendMessage(message: String) {
        streamId?.let { this.store.accept(ChatEvent.Ui.SendMessage(it, topicName, message)) }
    }
}
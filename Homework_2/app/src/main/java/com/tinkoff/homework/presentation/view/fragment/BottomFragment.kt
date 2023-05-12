package com.tinkoff.homework.presentation.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tinkoff.homework.R
import com.tinkoff.homework.databinding.BottomSheetDialogLayoutBinding
import com.tinkoff.homework.domain.data.EmojiResources
import com.tinkoff.homework.domain.data.Reaction
import com.tinkoff.homework.elm.chat.model.ChatEvent
import com.tinkoff.homework.presentation.view.MessageFactory
import com.tinkoff.homework.presentation.view.adapter.BottomSheetDialogAdapter
import com.tinkoff.homework.presentation.view.fragment.ChatFragment.Companion.ARG_MODEL_ID
import com.tinkoff.homework.presentation.view.fragment.ChatFragment.Companion.ARG_SENDER_ID
import com.tinkoff.homework.presentation.viewmodel.ChatViewModel
import javax.inject.Inject

class BottomFragment : BottomSheetDialogFragment() {
    @Inject
    lateinit var messageFactory: MessageFactory

    private val viewModel: ChatViewModel by viewModels(
        ownerProducer = { this.requireParentFragment() }
    )

    private lateinit var binding: BottomSheetDialogLayoutBinding
    private var messageId: Long = -1L
    private var senderId: Long = -1L
    private val emojiCount = 100

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        messageId = requireArguments().getLong(ARG_MODEL_ID)
        senderId = requireArguments().getLong(ARG_SENDER_ID)

        binding = BottomSheetDialogLayoutBinding.bind(
            inflater.inflate(
                R.layout.bottom_sheet_dialog_layout,
                container
            )
        )
        val resources = EmojiResources()
        val list = resources.emojiSet.take(emojiCount)
        val adapter = BottomSheetDialogAdapter(list, this::applyEmoji)
        binding.bottomRecyclerView.adapter = adapter

        return binding.root
    }

    override fun getTheme() = R.style.CustomBottomSheetDialogTheme

    private fun applyEmoji(emojiCode: String, emojiName: String) {
        viewModel.store.accept(
            ChatEvent.Ui.AddReaction(
                messageId,
                Reaction(emojiCode, emojiName, senderId)
            )
        )
        dismiss()
    }
}
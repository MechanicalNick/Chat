package com.tinkoff.homework.presentation.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tinkoff.homework.R
import com.tinkoff.homework.databinding.BottomSheetReactionsLayoutBinding
import com.tinkoff.homework.databinding.FragmentPeopleBinding
import com.tinkoff.homework.domain.data.EmojiResources
import com.tinkoff.homework.domain.data.Reaction
import com.tinkoff.homework.elm.chat.model.ChatEvent
import com.tinkoff.homework.getAppComponent
import com.tinkoff.homework.presentation.view.adapter.BottomSheetDialogAdapter
import com.tinkoff.homework.presentation.view.fragment.chat.ChatFragment.Companion.ARG_MODEL_ID
import com.tinkoff.homework.presentation.view.fragment.chat.ChatFragment.Companion.ARG_SENDER_ID
import com.tinkoff.homework.presentation.viewmodel.ChatViewModel
import javax.inject.Inject

class ReactionFragment : BottomSheetDialogFragment() {
    @Inject
    lateinit var chatViewModel: ChatViewModel

    private var _binding: BottomSheetReactionsLayoutBinding? = null
    private val binding get() = _binding!!
    private var messageId: Long = -1L
    private var senderId: Long = -1L
    private val emojiCount = 7 * 12 // 7 to one row

    override fun onAttach(context: Context) {
        context
            .getAppComponent()
            .inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        messageId = requireArguments().getLong(ARG_MODEL_ID)
        senderId = requireArguments().getLong(ARG_SENDER_ID)

        _binding = BottomSheetReactionsLayoutBinding.bind(
            inflater.inflate(
                R.layout.bottom_sheet_reactions_layout,
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
        chatViewModel.store.accept(
            ChatEvent.Ui.AddReaction(
                messageId,
                Reaction(emojiCode, emojiName, senderId)
            )
        )
        parentFragmentManager.setFragmentResult(
            ARG_REACTION_RESULT,
            bundleOf(ARG_REACTION_RESULT to true)
        )
        dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val ARG_REACTION_RESULT = "reaction"
    }
}
package com.tinkoff.homework.presentation.view.fragment

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tinkoff.homework.R
import com.tinkoff.homework.databinding.BottomSheetActionSelectorLayoutBinding
import com.tinkoff.homework.databinding.LayoutAlertDialogBinding
import com.tinkoff.homework.elm.chat.model.ChatEvent
import com.tinkoff.homework.getAppComponent
import com.tinkoff.homework.presentation.view.fragment.chat.ChatFragment
import com.tinkoff.homework.presentation.viewmodel.ChatViewModel
import javax.inject.Inject

class ActionSelectorFragment : BottomSheetDialogFragment() {
    @Inject
    lateinit var viewModel: ChatViewModel
    private lateinit var binding: BottomSheetActionSelectorLayoutBinding
    private val reactionFragment by lazy { ReactionFragment() }

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
        binding = BottomSheetActionSelectorLayoutBinding.inflate(inflater)
        val messageId = requireArguments().getLong(ChatFragment.ARG_MODEL_ID)

        with(binding) {
            addReactionLayout.setOnClickListener {
                reactionFragment.arguments = arguments
                reactionFragment.show(childFragmentManager, null)
            }
            removeMessageLayout.setOnClickListener {
                actionAndDismiss { removeMessage(messageId) }
            }
            editMessageLayout.setOnClickListener {
                actionAndDismiss { editMessage(messageId) }
            }
            changeTopicLayout.setOnClickListener {
                actionAndDismiss { changeTopic(messageId) }
            }
            copyToBufferLayout.setOnClickListener {
                actionAndDismiss { copyToBuffer(messageId) }
            }
        }

        childFragmentManager.setFragmentResultListener(
            ReactionFragment.ARG_REACTION_RESULT,
            this
        ) { _, _ -> dismiss() }

        return binding.root
    }

    private fun editMessage(messageId: Long) {
        val currentBinding = LayoutAlertDialogBinding.inflate(layoutInflater)
        with(currentBinding) {
            dialogTitle.text = getString(R.string.edit_message)
            userInputDialog.hint = getString(R.string.edit_message_hint)
        }

        AlertDialogFactory().create(
            currentBinding.root
        ) {
            val newText = currentBinding.userInputDialog.text.toString()
            viewModel.store.accept(ChatEvent.Ui.EditMessage(messageId, newText))
        }.show()
    }

    private fun changeTopic(messageId: Long) {
        val currentBinding = LayoutAlertDialogBinding.inflate(layoutInflater)
        with(currentBinding) {
            dialogTitle.text = getString(R.string.change_topic)
            userInputDialog.hint = getString(R.string.change_topic_hint)
        }

        AlertDialogFactory().create(
            currentBinding.root
        ) {
            val newTopic = currentBinding.userInputDialog.text.toString()
            viewModel.store.accept(ChatEvent.Ui.ChangeTopic(messageId, newTopic))
        }.show()
    }

    private fun removeMessage(messageId: Long) {
        viewModel.store.accept(ChatEvent.Ui.RemoveMessage(messageId))
    }

    private fun actionAndDismiss(action: () -> Unit) {
        action()
        dismiss()
    }

    private fun copyToBuffer(messageId: Long) {
        val clipboard =
            requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
        val message = viewModel.store.currentState.items?.firstOrNull { it.id == messageId }
        if (clipboard != null && message != null) {
            val clip = ClipData.newPlainText("", message.text)
            clipboard.setPrimaryClip(clip)
            viewModel.store.accept(ChatEvent.Ui.ShowToast(getString(R.string.copy_message_success)))
        } else {
            viewModel.store.accept(ChatEvent.Ui.ShowToast(getString(R.string.copy_message_unsuccess)))
        }
    }

    override fun getTheme() = R.style.CustomBottomSheetDialogTheme
}
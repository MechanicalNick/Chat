package com.tinkoff.homework.utils.adapter.message

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.tinkoff.homework.data.EmojiWrapper
import com.tinkoff.homework.data.MessageModel
import com.tinkoff.homework.data.Reaction
import com.tinkoff.homework.databinding.MessageLayoutBinding
import com.tinkoff.homework.utils.DelegateItem
import com.tinkoff.homework.utils.FlexboxFactory
import com.tinkoff.homework.utils.adapter.AdapterDelegate
import com.tinkoff.homework.view.fragment.BottomFragment
import com.tinkoff.homework.viewmodel.MainViewModel


class MessageDelegate(
    private val bottomFragment: BottomFragment,
    private val fragmentManager: FragmentManager,
    private val viewModel: MainViewModel
) : AdapterDelegate {
    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val messageLayoutBinding = MessageLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(messageLayoutBinding)
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        item: DelegateItem,
        position: Int
    ) {
        (holder as ViewHolder).bind(item.content() as MessageModel)
    }

    override fun isOfViewType(item: DelegateItem): Boolean = item is MessageDelegateItem

    inner class ViewHolder(private val binding: MessageLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(model: MessageModel) {
            with(binding) {
                root.textView.text = model.text
                root.setOnLongClickListener {
                    showBottomSheetDialog(model.id)
                }

                root.flexbox.removeAllViews()
                FlexboxFactory(model.reactions, binding.root.context).create(
                    { reactionRemove(it, model.id) },
                    { showBottomSheetDialog(model.id) }
                ).forEach {
                    root.flexbox.addView(it)
                }
            }
        }

        private fun reactionRemove(reaction: Reaction, messageId: Int) {
            viewModel.removeEmoji.value = EmojiWrapper(reaction.code, messageId)
        }

        private fun showBottomSheetDialog(id: Int): Boolean {
            bottomFragment.show(fragmentManager, null)
            val args = Bundle()
            args.putInt("modelId", id)
            bottomFragment.arguments = args
            return true
        }
    }
}
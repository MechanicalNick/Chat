package com.tinkoff.homework.utils.adapter.message

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tinkoff.homework.data.domain.MessageModel
import com.tinkoff.homework.databinding.MessageLayoutBinding
import com.tinkoff.homework.utils.DelegateItem
import com.tinkoff.homework.utils.FlexboxFactory
import com.tinkoff.homework.utils.adapter.AdapterDelegate
import com.tinkoff.homework.view.fragment.ChatFragmentCallback


class MyMessageDelegate(private val callback: ChatFragmentCallback) : AdapterDelegate {
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

    override fun isOfViewType(item: DelegateItem): Boolean = item is MyMessageDelegateItem

    inner class ViewHolder(private val binding: MessageLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(model: MessageModel) {
            with(binding) {
                root.textView.text = model.text
                root.setOnLongClickListener {
                    callback.showBottomSheetDialog(model.id, model.senderId)
                }

                root.flexbox.removeAllViews()
                FlexboxFactory(model.reactions, binding.root.context).create(
                    { callback.reactionRemove(it, model.id, model.senderId) },
                    { callback.showBottomSheetDialog(model.id, model.senderId) }
                ).forEach {
                    root.flexbox.addView(it)
                }
            }
        }
    }
}
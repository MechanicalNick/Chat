package com.tinkoff.homework.presentation.view.adapter.message

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.tinkoff.homework.R
import com.tinkoff.homework.databinding.CompanionMessageLayoutBinding
import com.tinkoff.homework.domain.data.MessageModel
import com.tinkoff.homework.presentation.dp
import com.tinkoff.homework.presentation.view.ChatFragmentCallback
import com.tinkoff.homework.presentation.view.DelegateItem
import com.tinkoff.homework.presentation.view.FlexboxFactory
import com.tinkoff.homework.presentation.view.adapter.AdapterDelegate
import com.tinkoff.homework.utils.Const

class CompanionMessageDelegate(
    private val callback: ChatFragmentCallback,
    private val header: LazyHeaders
): AdapterDelegate {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val messageLayoutBinding = CompanionMessageLayoutBinding.inflate(
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

    override fun isOfViewType(item: DelegateItem): Boolean = item is CompanionMessageDelegateItem

    inner class ViewHolder(private val binding: CompanionMessageLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(model: MessageModel) {
            with(binding) {
                root.avatarView.let {
                    Glide.with(binding.root)
                        .load(model.avatarUrl)
                        .placeholder(R.mipmap.ic_launcher_round)
                        .error(R.drawable.error_placeholder)
                        .circleCrop()
                        .into(it)
                }

                root.textName.text = model.senderFullName

                if(model.imageUrl != null){
                    val glideUrl = GlideUrl(model.imageUrl, header)
                    root.textMessage.isVisible = false
                    root.userImage.isVisible = true
                    root.userImage.let {
                        Glide.with(root.userImage)
                            .load(glideUrl)
                            .override(200.dp(root.context), 200.dp(root.context))
                            .placeholder(R.mipmap.ic_launcher_round)
                            .error(R.drawable.error_placeholder)
                            .into(it)
                    }
                } else {
                    root.textMessage.isVisible = true
                    root.userImage.isVisible = false
                    root.textMessage.text = model.text
                }

                root.setOnLongClickListener {
                    callback.showActionSelectorDialog(model.id, model.senderId)
                }

                root.flexbox.removeAllViews()
                FlexboxFactory(model.reactions, binding.root.context, callback.getMyCredentials()).create(
                    { callback.reactionChange(it, model, model.senderId) },
                    { callback.showReactionDialog(model.id, model.senderId) }
                ).forEach {
                    root.flexbox.addView(it)
                }
            }
        }
    }
}
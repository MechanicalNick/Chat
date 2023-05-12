package com.tinkoff.homework.presentation.view.adapter.stream

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tinkoff.homework.R
import com.tinkoff.homework.databinding.StreamItemBinding
import com.tinkoff.homework.domain.data.Stream
import com.tinkoff.homework.presentation.view.DelegateItem
import com.tinkoff.homework.presentation.view.adapter.AdapterDelegate
import com.tinkoff.homework.presentation.view.viewgroup.StreamView

class StreamDelegate(
    private val streamView: StreamView
    ): AdapterDelegate {
    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        ViewHolder(
            StreamItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            parent.context
        )

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        item: DelegateItem,
        position: Int
    ) {
        (holder as ViewHolder).bind(item.content() as Stream, item)
    }

    override fun isOfViewType(item: DelegateItem): Boolean = item is StreamDelegateItem

    inner class ViewHolder(
        private val binding: StreamItemBinding,
        private val context: Context
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: Stream, item: DelegateItem) {
            with(binding) {
                streamName.text = context.getString(R.string.sharp, model.name)
                expanderView.isChecked = model.isExpanded
                val listener = View.OnClickListener {
                    if(!model.isExpanded) {
                        streamView.expand(item as StreamDelegateItem)
                        expanderView.isChecked = true
                    }
                    else{
                        streamView.collapse(item as StreamDelegateItem)
                        expanderView.isChecked = false
                    }
                }
                binding.root.setOnClickListener(listener)
                expanderView.setOnClickListener(listener)
                binding.root.setOnLongClickListener {
                    streamView.goToChat("", model.name, model.id)
                    true
                }
            }
        }
    }
}